package com.lelloman.read.ui.sources.viewmodel

import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel
import com.lelloman.read.ui.sources.repository.SourcesRepository

abstract class SourceViewModel(
    resourceProvider: ResourceProvider,
    protected val sourcesRepository: SourcesRepository
) : BaseViewModel(resourceProvider) {

}