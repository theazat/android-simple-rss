package com.lelloman.read.utils

import com.lelloman.read.persistence.settings.SourceRefreshInterval

object Constants {

    const val SOURCE_TABLE_NAME = "Sources"
    const val ARTICLE_TABLE_NAME = "Articles"

    const val APP_DATABASE_NAME = "AppDb"
    const val APP_DATABASE_VERSION = 1

    object AppSettings {
        const val SHARED_PREFS_NAME = "AppSettings"

        const val KEY_MIN_SOURCE_REFRESH_INTERVAL = "MinSourceRefreshInterval"
        val DEFAULT_MIN_SOURCE_REFRESH_INTERVAL = SourceRefreshInterval.NEUROTIC

        const val KEY_ARTICLE_LIST_IMAGES = "ArticleListImages"
        const val DEFAULT_ARTICLES_LIST_IMAGES = true

        const val KEY_USE_METERED_NETWORK = "UseMeteredNetwork"
        const val DEFAULT_USE_METERED_NETWORK = true

        const val KEY_OPEN_ARTICLES_IN_APP = "OpenArticlesInApp"
        const val DEFAULT_OPEN_ARTICLES_IN_APP = false

        const val KEY_SHOULD_SHOW_WALKTHROUGH = "ShouldShowWalkthrough"
        const val DEFAULT_SHOULD_SHOW_WALKTHROUGH = true
    }

}