package com.lelloman.read.articleslist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.read.core.BaseViewModel
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.persistence.model.Article

abstract class ArticlesListViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val isLoading: MutableLiveData<Boolean>

    abstract val articles: MutableLiveData<List<Article>>

    abstract fun refresh()

    abstract fun onSourcesClicked()
}
