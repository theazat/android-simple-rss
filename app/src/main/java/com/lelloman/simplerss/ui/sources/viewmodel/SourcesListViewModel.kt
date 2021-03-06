package com.lelloman.simplerss.ui.sources.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.persistence.db.model.Source

abstract class SourcesListViewModel(dependencies: Dependencies) : BaseViewModel(dependencies) {

    abstract val sources: MutableLiveData<List<Source>>

    abstract val emptyViewVisible: LiveData<Boolean>

    abstract fun onFabClicked(view: View)

    abstract fun onSourceClicked(source: Source)

    abstract fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean)

    abstract fun onSourceSwiped(source: Source)
}