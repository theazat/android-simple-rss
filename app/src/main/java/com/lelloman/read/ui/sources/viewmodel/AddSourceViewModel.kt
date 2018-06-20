package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.viewmodel.BaseViewModel

abstract class AddSourceViewModel(
    resourceProvider: ResourceProvider
) : BaseViewModel(resourceProvider) {

    abstract val sourceName: ObservableField<String>
    abstract val sourceNameError: MutableLiveData<String>

    abstract val sourceUrl: ObservableField<String>
    abstract val sourceUrlError: MutableLiveData<String>
    abstract val sourceUrlDrwable: MutableLiveData<Int>

    abstract val testingUrl: MutableLiveData<Boolean>


    abstract fun onCloseClicked()

    abstract fun onSaveClicked()

    abstract fun onTestUrlClicked()
}