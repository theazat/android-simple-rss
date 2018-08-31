package com.lelloman.read.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.read.R
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivitySourcesListBinding
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import com.lelloman.read.utils.ItemSwipeListener
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SourcesListActivity
    : BaseActivity<SourcesListViewModel, ActivitySourcesListBinding>() {

    private lateinit var adapter: SourcesAdapter

    @Inject
    lateinit var semanticTimeProvider: SemanticTimeProvider

    @Inject
    @field:UiScheduler
    lateinit var uiScheduler: Scheduler

    @Inject
    lateinit var loggerFactory: LoggerFactory

    private val logger by lazy { loggerFactory.getLogger(SourcesListActivity::class.java.simpleName) }

    private var timerSubscription: Disposable? = null

    override fun getLayoutId() = R.layout.activity_sources_list

    override fun getViewModelClass() = SourcesListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        adapter = SourcesAdapter(
            semanticTimeProvider = semanticTimeProvider,
            onSourceClickedListener = viewModel::onSourceClicked,
            onSourceIsActiveChangedListener = viewModel::onSourceIsActiveChanged
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        ItemSwipeListener.set(binding.recyclerView) { viewModel.onSourceSwiped(adapter.getItem(it)) }
        binding.viewModel = viewModel
        viewModel.sources.observe(this, adapter)
    }

    override fun onStart() {
        super.onStart()
        timerSubscription = Observable
            .interval(1, TimeUnit.SECONDS)
            .observeOn(uiScheduler)
            .subscribe {
                adapter.tick()
                logger.d("1s tick")
            }
    }

    override fun onStop() {
        super.onStop()
        timerSubscription?.dispose()
        timerSubscription = null
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SourcesListActivity::class.java))
        }
    }

}
