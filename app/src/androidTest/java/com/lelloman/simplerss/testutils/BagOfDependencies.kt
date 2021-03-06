package com.lelloman.simplerss.testutils

import androidx.test.core.app.ApplicationProvider
import com.lelloman.common.di.BaseApplicationModuleFactory
import com.lelloman.common.http.HttpClient
import com.lelloman.common.http.internal.HttpClientImpl
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.settings.BaseSettingsModuleFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.TimeProviderImpl
import com.lelloman.common.utils.UrlValidatorImpl
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.simplerss.SimpleRssApplication
import com.lelloman.simplerss.feed.FeedParser
import com.lelloman.simplerss.feed.fetcher.FeedFetcher
import com.lelloman.simplerss.feed.finder.FeedFinder
import com.lelloman.simplerss.feed.finder.FeedFinderHttpClient
import com.lelloman.simplerss.feed.finder.FeedFinderImpl
import com.lelloman.simplerss.feed.finder.FeedFinderParser
import com.lelloman.simplerss.html.HtmlParser
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.persistence.settings.AppSettingsImpl
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

class BagOfDependencies {
    private val timeProvider: TimeProvider
    private val htmlParser: HtmlParser
    private val meteredConnectionChecker: MeteredConnectionChecker
    private val httpClient: HttpClient
    val feedFinder: FeedFinder
    private val feedParser: FeedParser
    private val baseAppSettings: BaseApplicationSettings
    private val appSettings: AppSettings

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val targetContext: SimpleRssApplication = ApplicationProvider.getApplicationContext()
        val baseAppModule = BaseApplicationModuleFactory()
        val loggerFactory = baseAppModule.provideLoggerFactory()
        timeProvider = TimeProviderImpl()
        htmlParser = HtmlParser()
        val urlValidator = UrlValidatorImpl()

        meteredConnectionChecker = baseAppModule.provideMeteredConnectionChecker(targetContext)

        baseAppSettings = BaseSettingsModuleFactory().provideBaseApplicationSettings(
            targetContext,
            AppTheme.DEFAULT
        )
        appSettings = AppSettingsImpl(targetContext, baseAppSettings)

        feedParser = FeedParser(timeProvider)

        httpClient = HttpClientImpl(
            okHttpClient = okHttpClient,
            loggerFactory = loggerFactory,
            timeProvider = timeProvider
        )

        val feedFetcher = FeedFetcher(
            httpClient = httpClient,
            feedParser = feedParser,
            htmlParser = htmlParser,
            meteredConnectionChecker = meteredConnectionChecker,
            appSettings = appSettings,
            loggerFactory = loggerFactory
        )

        val feedFinderHttpClient = FeedFinderHttpClient(
            httpClient = httpClient,
            urlValidator = urlValidator
        )

        val feedFinderParser = FeedFinderParser(
            urlValidator = urlValidator,
            htmlParser = htmlParser,
            loggerFactory = loggerFactory
        )

        feedFinder = FeedFinderImpl(
            httpClient = feedFinderHttpClient,
            parser = feedFinderParser,
            feedFetcher = feedFetcher,
            loggerFactory = loggerFactory,
            scheduler = Schedulers.newThread()
        )
    }
}