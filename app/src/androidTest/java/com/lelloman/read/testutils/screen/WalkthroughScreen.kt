package com.lelloman.read.testutils.screen

import com.lelloman.common.view.AppTheme
import com.lelloman.read.R
import com.lelloman.testutils.checkRecyclerViewCount
import com.lelloman.testutils.clickView
import com.lelloman.testutils.clickViewWithText
import com.lelloman.testutils.viewIsDisplayed
import com.lelloman.testutils.viewWithTextIsDisplayed


class WalkthroughScreen : Screen() {

    init {
        viewIsDisplayed(R.id.walkthrough_root)
    }

    fun firstPageIsDisplayed() = apply { viewWithTextIsDisplayed(string(R.string.walkthrough_first_page)) }

    fun clickOk() = apply { clickView(R.id.button_ok) }

    fun themesAreDisplayed() = apply {
        viewWithTextIsDisplayed(AppTheme.LIGHT.name)
        viewWithTextIsDisplayed(AppTheme.DARCULA.name)
        checkRecyclerViewCount(AppTheme.values().size, R.id.themes_recycler_view)
    }

    fun clickOnThemes() = apply {
        clickViewWithText(AppTheme.DARCULA.name)
        clickViewWithText(AppTheme.LIGHT.name)
    }

    fun swipeLeft() = apply { com.lelloman.testutils.swipeLeft(R.id.view_pager) }
    fun swipeRight() = apply { com.lelloman.testutils.swipeRight(R.id.view_pager) }

    fun typeInUrlIsDisplayed() = apply { viewWithTextIsDisplayed(string(R.string.type_in_url)) }

    fun clickNo() = with(clickViewWithText(string(R.string.NO))) {
        ArticlesListScreen()
    }

    fun pressClose() = with(clickView(R.id.skip)) { ArticlesListScreen() }
}