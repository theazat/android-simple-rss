package com.lelloman.read.ui.walkthrough.viewmodel

import android.view.View
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.ui.common.viewmodel.IDiscoverUrlViewModel
import com.lelloman.read.utils.OnKeyboardActionDoneListener

abstract class WalkthroughViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : IDiscoverUrlViewModel, BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    abstract fun onCloseClicked(view: View)

    abstract fun onMeteredConnectionYesClicked(view: View)
    abstract fun onMeteredConnectionNoClicked(view: View)
    abstract fun onFirstPageOkClicked(view: View)

}