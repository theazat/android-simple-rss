package com.lelloman.simplerss.testutils

import com.lelloman.instrumentedtestutils.TestApplicationRunner

@Suppress("unused")
class MockTestRunner : TestApplicationRunner() {
    override val testAppClassName: String = TestApp::class.java.name
}