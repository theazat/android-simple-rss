package com.lelloman.simplerss.feed.fetcher

sealed class TestResult {
    override fun toString(): String = javaClass.simpleName
}

object HttpError : TestResult()
object XmlError : TestResult()
object EmptySource : TestResult()
object UnknownError : TestResult()
class Success(val nArticles: Int, val title: String?) : TestResult()