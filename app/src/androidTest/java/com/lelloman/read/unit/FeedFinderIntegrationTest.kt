package com.lelloman.read.unit

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.MeteredConnectionChecker
import com.lelloman.read.core.TimeProviderImpl
import com.lelloman.read.core.logger.Logger
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.feed.FeedParser
import com.lelloman.read.feed.fetcher.FeedFetcher
import com.lelloman.read.feed.finder.FeedFinder
import com.lelloman.read.feed.finder.FeedFinderHttpClient
import com.lelloman.read.feed.finder.FeedFinderParser
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.html.HtmlParser
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.UrlValidator
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.mock

class FeedFinderIntegrationTest {

    private val httpClient = object : HttpClient {

        private val urlMap = mutableMapOf<String, HttpResponse>()

        fun map(url: String, stringBody: String) = map(
            url = url,
            httpResponse = HttpResponse(200, true, stringBody.toByteArray())
        )

        fun map(url: String, httpResponse: HttpResponse) {
            urlMap[url] = httpResponse
        }

        override fun request(request: HttpRequest): Single<HttpResponse> = Maybe
            .fromCallable {
                urlMap[request.url]
                    ?: HttpResponse(code = 404, isSuccessful = false, body = byteArrayOf())
            }
            .toSingle()
    }

    private val urlValidator = UrlValidator()
    private val htmlParser = HtmlParser()

    private val loggerFactory = object : LoggerFactory {
        override fun getLogger(tag: String) = mock(Logger::class.java)
    }

    private val meteredConnectionChecker = object : MeteredConnectionChecker {
        override fun isNetworkMetered() = false
    }

    private val appSettings = mock(AppSettings::class.java)

    private val timeProvider = TimeProviderImpl()

    private val feedParser = FeedParser(timeProvider = timeProvider)

    private val feedFinderParser = FeedFinderParser(
        urlValidator = urlValidator,
        htmlParser = htmlParser,
        loggerFactory = loggerFactory
    )

    private val feedFetcher = FeedFetcher(
        httpClient = httpClient,
        feedParser = feedParser,
        htmlParser = htmlParser,
        meteredConnectionChecker = meteredConnectionChecker,
        appSettings = appSettings,
        loggerFactory = loggerFactory
    )

    private val feedFinderHttpClient = FeedFinderHttpClient(
        httpClient = httpClient,
        urlValidator = urlValidator
    )

    private val tested = FeedFinder(
        httpClient = feedFinderHttpClient,
        parser = feedFinderParser,
        feedFetcher = feedFetcher,
        loggerFactory = loggerFactory,
        newThreadScheduler = Schedulers.newThread()
    )

    private fun List<FoundFeed>.assertContains(url: String, nArticles: Int, name: String) {
        assertThat(any { it.url == url && it.nArticles == nArticles && it.name == name }).isTrue()
    }

    @Test
    fun findsLinksInHtml1() {
        httpClient.map(URL_1, HTML_1)
        httpClient.map("$URL_1/feed", VALID_FEED_XML)
        httpClient.map("$URL_1/somefeed", VALID_FEED_XML)
        httpClient.map("http://www.staceppa.com", VALID_FEED_XML)

        val foundFeeds = tested.findValidFeedUrls(URL_1).toList().blockingGet()

        assertThat(foundFeeds).hasSize(3)
        foundFeeds.assertContains(url = "$URL_1/feed", nArticles = 1, name = "RSS di   - ANSA.it")
        foundFeeds.assertContains(url = "http://www.staceppa.com", nArticles = 1, name = "RSS di   - ANSA.it")
        foundFeeds.assertContains(url = "$URL_1/somefeed", nArticles = 1, name = "RSS di   - ANSA.it")
    }

    @Ignore
    @Test
    fun findsLinksInHtml2() {
        httpClient.map(URL_1, HTML_1)
        httpClient.map("$URL_1/feed", VALID_FEED_XML)
        httpClient.map("$URL_1/somefeed", HTML_2)
        httpClient.map("http://www.rsssomething.com", VALID_FEED_XML)
        httpClient.map("http://www.staceppa2.com", VALID_FEED_XML)

        val foundFeeds = tested.findValidFeedUrls(URL_1).toList().blockingGet()

        assertThat(foundFeeds).hasSize(3)
        foundFeeds.assertContains(url = "$URL_1/feed", nArticles = 1, name = "RSS di   - ANSA.it")
        foundFeeds.assertContains(url = "http://www.staceppa2.com", nArticles = 1, name = "RSS di   - ANSA.it")
        foundFeeds.assertContains(url = "http://www.rsssomething.com", nArticles = 1, name = "RSS di   - ANSA.it")
    }

    private companion object {
        const val URL_1 = "http://www.url1.it"
        const val HTML_1 = """
<html>
    <head>
        <link type="application/rss+xml" href="http://www.staceppa.com" />
        <link type="text/xml" href="http://wwww.staceppa.com/2" />
    </head>
    <body>
        <p>
            asdasd
            <a href="/somefeed">asdasd</a>
            asdasd
        </p>
    </body>
</html>
        """

        const val HTML_2 = """
<html>
    <head>
        <link type="application/atom+xml" href="http://www.staceppa2.com" />
        <link type="text/xml" href="http://wwww.staceppa.com/3" />
    </head>
    <body>
        <p>
            asdasd
            <a href="http://www.rsssomething.com">asdasd</a>
            asdasd
        </p>
    </body>
</html>
        """

        const val VALID_FEED_XML = """
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
	<channel>
		<atom:link rel="self" type="application/rss+xml" href="http://www.ansa.it/sito/ansait_rss.xml"></atom:link>
		<title>   RSS di   - ANSA.it      </title>
		  <link>http://www.ansa.it/</link>
		  <description>Updated every day - FOR PERSONAL USE ONLY</description>
		<language>it</language>
		<copyright>Copyright: (C) ANSA, http://www.ansa.it/sito/static/disclaimer.html</copyright>
		<item>
		<title><![CDATA[Spia russa in ambasciata Usa a Mosca]]></title>
		<description><![CDATA[Una donna che lavorato dieci anni per il Secret Service]]></description><link>http://www.ansa.it/sito/notizie/mondo/nordamerica/2018/08/03/spia-russa-in-ambasciata-usa-a-mosca_c4381ef5-9fbd-4125-a571-ff66dd7a561d.html</link><pubDate>Fri, 3 Aug 2018 07:20:31 +0200</pubDate>
		<guid>http://www.ansa.it/sito/notizie/mondo/nordamerica/2018/08/03/spia-russa-in-ambasciata-usa-a-mosca_c4381ef5-9fbd-4125-a571-ff66dd7a561d.html</guid>
		</item>
	</channel>
</rss>
        """
    }
}