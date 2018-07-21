package com.lelloman.read.ui.articles.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.core.navigation.ViewIntentNavigationEvent
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.repository.ArticlesRepository
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Observable
import io.reactivex.Scheduler

class ArticlesListViewModelImpl(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val articlesRepository: ArticlesRepository,
    private val sourcesRepository: SourcesRepository,
    private val appSettings: AppSettings,
    resourceProvider: ResourceProvider
) : ArticlesListViewModel(resourceProvider) {

    override val emptyViewVisible = MutableLiveData<Boolean>()
    override val emptyViewDescriptionText = MutableLiveData<String>()
    override val emptyViewButtonText = MutableLiveData<String>()

    private var emptyViewAction: (() -> Unit)? = null

    override val isLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            articlesRepository.loading
                .distinctUntilChanged()
                .subscribeOn(ioScheduler)
                .subscribe { isLoading.postValue(it) }
        }
    }

    override val articles: MutableLiveData<List<Article>> by LazyLiveData {
        subscription {
            articlesRepository.fetchArticles()
                .flatMap { articles ->
                    if (articles.isEmpty()) {
                        sourcesRepository.fetchSources()
                            .first(emptyList())
                            .observeOn(uiScheduler)
                            .doOnSuccess(::setEmptyViewValues)
                            .map { articles }
                            .toObservable()
                    } else {
                        Observable.just(articles)
                    }
                }
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    articles.value = it
                    emptyViewVisible.value = it.isEmpty()
                }
        }
    }

    private val openArticlesInApp get() = appSettings.openArticlesInApp.blockingFirst()

    init {
        emptyViewVisible.value = false
    }

    override fun refresh() = articlesRepository.refresh()

    override fun onSourcesClicked() = navigate(ScreenNavigationEvent(NavigationScreen.SOURCES_LIST))

    override fun onEmptyViewButtonClicked() {
        emptyViewAction?.invoke()
    }

    override fun onArticleClicked(article: Article) = if (openArticlesInApp) {
        navigate(ScreenNavigationEvent(NavigationScreen.ARTICLE, article))
    } else {
        navigate(ViewIntentNavigationEvent(article.link))
    }

    override fun onSettingsClicked() = navigate(ScreenNavigationEvent(NavigationScreen.SETTINGS))

    private fun setEmptyViewValues(sources: List<Source>) {
        when {
            sources.isEmpty() -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_no_source)
                emptyViewButtonText.value = getString(R.string.add_source)
                emptyViewAction = { navigate(ScreenNavigationEvent(NavigationScreen.ADD_SOURCE)) }
            }
            !sources.any(Source::isActive) -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_sources_disabled)
                emptyViewButtonText.value = getString(R.string.enable_sources)
                emptyViewAction = { navigate(ScreenNavigationEvent(NavigationScreen.SOURCES_LIST)) }
            }
            else -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_must_refresh)
                emptyViewButtonText.value = getString(R.string.refresh)
                emptyViewAction = ::refresh
            }
        }
    }
}
