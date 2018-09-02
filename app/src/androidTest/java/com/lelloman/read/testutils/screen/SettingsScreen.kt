package com.lelloman.read.testutils.screen

import android.support.test.espresso.Espresso.pressBack
import com.lelloman.read.R
import com.lelloman.read.testutils.setToggleSettingChecked
import com.lelloman.read.testutils.viewIsDisplayed
import com.lelloman.read.testutils.viewWithId

class SettingsScreen : Screen() {
    init {
        viewIsDisplayed(R.id.settings_root)
    }

    fun backToArticlesList() = with(pressBack()) { ArticlesListScreen() }

    fun setOpenArticlesInApp(openInApp: Boolean) = apply {
        viewWithId(R.id.toggle_setting_open_articles_in_app).perform(setToggleSettingChecked(openInApp))
    }

    fun setArticlesImages(hasImages: Boolean) = apply {
        viewWithId(R.id.toggle_setting_artcles_images).perform(setToggleSettingChecked(hasImages))
    }
}