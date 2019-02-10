package com.lelloman.simplerss.unit

import com.google.common.truth.Truth
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.Time
import com.lelloman.common.utils.model.WeekTime
import com.lelloman.simplerss.feed.FeedParser
import com.lelloman.simplerss.feed.exception.InvalidFeedTagException
import com.lelloman.simplerss.feed.exception.MalformedXmlException
import com.lelloman.simplerss.testutils.Xmls
import io.reactivex.Observable
import org.junit.Test


class FeedParserTest {

    private val time = 0L
    private val timeProvider = object : TimeProvider {
        override fun now() = getTime(0)
        override fun nowUtcMs() = time
        override fun getTime(utcMs: Long) = Time(WeekTime(0, 0), DayTime(0, 0))
    }

    private val tested = FeedParser(timeProvider)

    @Test
    fun throwsMalformedXmlException() {
        val tester = Xmls.readFile(Xmls.MALFORMED_XML1)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is MalformedXmlException }
    }

    @Test
    fun throwsInvalidRootTagException() {
        val tester = Xmls.readFile(Xmls.INVALID_ROOT_TAG_XML)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertError { it is InvalidFeedTagException }
    }

    @Test
    fun parsesCorrectNumberOfFeedsFromXmls() {
        val test = Observable.fromIterable(Xmls.ALL_RSS)
            .flatMapSingle(Xmls::readFile)
            .flatMapSingle(tested::parseFeeds)
            .test()

        test.assertValueCount(6)
        test.assertValueAt(0) { it.size == 23 }
        test.assertValueAt(1) { it.size == 118 }
        test.assertValueAt(2) { it.size == 118 }
        test.assertValueAt(3) { it.size == 3 }
        test.assertValueAt(4) { it.size == 10 }
        test.assertValueAt(5) { it.size == 30 }
    }

    @Test
    fun parsesFeedsFromSampleXml() {
        val tester = Xmls.readFile(Xmls.SAMPLE)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertNoErrors()
        tester.assertValueCount(1)
        val feeds = tester.values()[0]
        Truth.assertThat(feeds[0]).isEqualTo(Xmls.SAMPLE_FEEDS[0])
        Truth.assertThat(feeds[1]).isEqualTo(Xmls.SAMPLE_FEEDS[1])
        Truth.assertThat(feeds[2]).isEqualTo(Xmls.SAMPLE_FEEDS[2])
    }

    @Test
    fun parsesFeedsFromViceXml() {
        val test = Xmls.readFile(Xmls.VICE)
            .flatMap(tested::parseFeeds)
            .test()

        test.assertNoErrors()
        test.assertValueCount(1)
        val feeds = test.values()[0]
        Truth.assertThat(feeds[0]).isEqualTo(Xmls.VICE_FEED_0)
        Truth.assertThat(feeds[1]).isEqualTo(Xmls.VICE_FEED_1)
    }
}