package com.lelloman.read.ui.common.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.view.View
import com.lelloman.read.utils.OnKeyboardActionDoneListener

interface IDiscoverUrlViewModel : OnKeyboardActionDoneListener {
    val discoverUrl: ObservableField<String>
    val isFeedDiscoverLoading: MutableLiveData<Boolean>

    fun onDiscoverClicked(view: View?)
}