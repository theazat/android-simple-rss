package com.lelloman.simplerss.feed

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.simplerss.feed.fetcher.FaviconFetcher
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.persistence.db.ArticlesDao
import com.lelloman.simplerss.persistence.db.SourcesDao
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.persistence.settings.SourceRefreshInterval
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class FeedRefresherImpl(
    private val ioScheduler: Scheduler,
    private val newThreadScheduler: Scheduler,
    private val httpPoolScheduler: Scheduler,
    private val sourcesDao: SourcesDao,
    private val articlesDao: ArticlesDao,
    private val timeProvider: TimeProvider,
    private val appSettings: AppSettings,
    private val faviconFetcher: FaviconFetcher,
    loggerFactory: LoggerFactory,
    private val feedFetcher: FeedFetcher
) : FeedRefresher {

    private val isLoadingSubject = BehaviorSubject.create<Boolean>()
    override val isLoading: Observable<Boolean> = isLoadingSubject
        .hide()
        .distinctUntilChanged()

    private val logger = loggerFactory.getLogger(javaClass)

    init {
        isLoadingSubject.onNext(false)
    }

    @Synchronized
    override fun refresh() {
        logger.d("refresh()")
        if (isLoadingSubject.value == true) {
            logger.d("refresh() isLoadingSubject = true, early return")
            return
        }
        isLoadingSubject.onNext(true)

        @Suppress("UNUSED_VARIABLE")
        val ignored = Single
            .zip(
                sourcesDao
                    .getActiveSources()
                    .firstOrError(),
                appSettings
                    .sourceRefreshMinInterval
                    .firstOrError(),
                BiFunction<List<Source>, SourceRefreshInterval, Pair<List<Source>, SourceRefreshInterval>> { sources, minRefreshInterval ->
                    sources to minRefreshInterval
                }
            )
            .flatMapObservable { (sources, interval) ->
                Observable.fromIterable(sources.map { it to interval })
            }
            .filter { (source, minRefreshInterval) ->
                timeProvider.nowUtcMs() - source.lastFetched > minRefreshInterval.ms
            }
            .map { (source, _) -> source }
            .flatMapMaybe { source ->
                feedFetcher.fetchFeed(source)
                    .subscribeOn(httpPoolScheduler)
                    .onErrorComplete()
            }
            .doAfterTerminate { isLoadingSubject.onNext(false) }
            .observeOn(ioScheduler)
            .subscribe({ (source, articles) ->
                articlesDao.deleteArticlesFromSource(source.id)
                articlesDao.insertAll(*articles.toTypedArray())
                sourcesDao.updateSourceLastFetched(source.id, timeProvider.nowUtcMs())
            }, {
                logger.w("Something went wrong in refresh subscription", it)
            })

        sourcesDao
            .getAll()
            .flatMap { Flowable.fromIterable(it) }
            .filter { it.favicon == null }
            .flatMapMaybe { source ->
                logger.d("trying to fetch favicon for source $source")
                faviconFetcher
                    .getPngFavicon(source.url)
                    .map { source to it }
                    .subscribeOn(httpPoolScheduler)
                    .onErrorComplete()
            }
            .flatMapCompletable { (source, pngBytes) ->
                Completable.fromAction {
                    logger.d("storing favicon for source $source into db, ${pngBytes.size} png bytes")
                    source.favicon = pngBytes
                    sourcesDao.updateSource(source)
                }
            }
            .subscribeOn(newThreadScheduler)
            .subscribe()
    }
}