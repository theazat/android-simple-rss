package com.lelloman.read.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.read.ui.common.viewmodel.IDiscoverUrlViewModel
import com.lelloman.read.ui.walkthrough.ThemeListItem

abstract class WalkthroughViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : IDiscoverUrlViewModel, BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    abstract val themes: MutableLiveData<List<ThemeListItem>>

    abstract fun onCloseClicked(view: View)

    abstract fun onFirstPageOkClicked(view: View)

    abstract fun onMeteredConnectionYesClicked(view: View)
    abstract fun onMeteredConnectionNoClicked(view: View)

    abstract fun onThemeClicked(theme: AppTheme)

}