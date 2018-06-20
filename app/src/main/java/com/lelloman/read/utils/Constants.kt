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
    }

}