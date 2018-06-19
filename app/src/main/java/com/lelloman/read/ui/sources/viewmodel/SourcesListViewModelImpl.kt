package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Completable
import io.reactivex.Scheduler

class SourcesListViewModelImpl(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val sourcesRepository: SourcesRepository,
    resourceProvider: ResourceProvider
) : SourcesListViewModel(resourceProvider) {

    override val sources: MutableLiveData<List<Source>> by LazyLiveData {
        subscription {
            sourcesRepository.fetchSources()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { sources.value = it }
        }
    }

    override fun onFabClicked(view: View) =
        navigate(ScreenNavigationEvent(NavigationScreen.ADD_SOURCE))

    override fun onSourceClicked(sourceId: Long) {
        sourcesRepository
            .getSource(sourceId)
            .firstOrError()
            .flatMapCompletable {
                Completable.fromAction {
                    onSourceIsActiveChanged(sourceId, !it.isActive)
                }
            }
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .subscribe()

        // TODO show dedicated screen
        // navigate(ScreenNavigationEvent(NavigationScreen.SOURCE, arrayOf(sourceId)))
    }


    override fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean) {
        sourcesRepository
            .setSourceIsActive(sourceId, isActive)
            .subscribeOn(ioScheduler)
            .subscribe()
    }
}