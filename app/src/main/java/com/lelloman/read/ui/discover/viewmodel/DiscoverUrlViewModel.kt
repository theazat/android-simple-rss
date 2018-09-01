package com.lelloman.read.ui.discover.viewmodel

import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.ui.common.viewmodel.IDiscoverUrlViewModel

abstract class DiscoverUrlViewModel(
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider
) : IDiscoverUrlViewModel, BaseViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
)