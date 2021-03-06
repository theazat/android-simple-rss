package com.lelloman.simplerss.unit

import com.google.common.truth.Truth
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.Date
import com.lelloman.common.utils.model.DateImpl
import com.lelloman.common.utils.model.DateTime
import com.lelloman.common.utils.model.DayOfTheWeek
import com.lelloman.common.utils.model.TimeImpl
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
        override fun getDate(utcMs: Long): Date = now()
        override fun getDateTime(utcMs: Long): DateTime = now()
        override fun getTime(utcMs: Long) = DateTime(
            time = TimeImpl(0, 0, 0),
            date = DateImpl(0, 0, 0, DayOfTheWeek.SUNDAY)
        )
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
        val tester = Observable.fromIterable(Xmls.ALL_RSS)
            .flatMapSingle(Xmls::readFile)
            .flatMapSingle(tested::parseFeeds)
            .test()

        tester.assertValueCount(6)
        tester.assertValueAt(0) { it.size == 23 }
        tester.assertValueAt(1) { it.size == 118 }
        tester.assertValueAt(2) { it.size == 118 }
        tester.assertValueAt(3) { it.size == 3 }
        tester.assertValueAt(4) { it.size == 10 }
        tester.assertValueAt(5) { it.size == 30 }
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
        val tester = Xmls.readFile(Xmls.VICE)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertNoErrors()
        tester.assertValueCount(1)
        val feeds = tester.values()[0]
        Truth.assertThat(feeds[0]).isEqualTo(Xmls.VICE_FEED_0)
        Truth.assertThat(feeds[1]).isEqualTo(Xmls.VICE_FEED_1)
    }

    @Test
    fun parsesFeedsFromVox() {
        val tester = Xmls.readFile(Xmls.VOX)
            .flatMap(tested::parseFeeds)
            .test()

        tester.assertNoErrors()
        tester.assertValueCount(1)
        tester.assertValueAt(0) { it.size == 10 }
    }
}