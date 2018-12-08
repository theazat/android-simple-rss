package com.lelloman.pdfscores.ui.recentscores.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfscores.persistence.PdfScore
import com.lelloman.pdfscores.persistence.PdfScoresDao
import com.lelloman.pdfscores.ui.PdfScoresScreen
import com.lelloman.pdfscores.ui.PdfScoresScreen.Companion.EXTRA_PDF_URI
import com.lelloman.pdfscores.ui.recentscores.PdfScoreViewModelItem
import io.reactivex.Scheduler

class RecentScoresViewModelImpl(
    dependencies: BaseViewModel.Dependencies,
    private val pdfScoresDao: PdfScoresDao,
    private val ioScheduler: Scheduler
) : RecentScoresViewModel(dependencies) {

    override val recentScores: MutableLiveData<List<PdfScoreViewModelItem>> by LazyLiveData {
        subscription {
            pdfScoresDao
                .getAll()
                .map {
                    it.map {
                        PdfScoreViewModelItem(it)
                    }
                }
                .subscribeOn(ioScheduler)
                .subscribe {
                    recentScores.postValue(it)
                }
        }
    }

    override val progressVisible: MutableLiveData<Boolean> by LazyLiveData {
        progressVisible.postValue(true)
    }

    override fun onPdfScoreClicked(pdfScore: PdfScore) {
        DeepLink(PdfScoresScreen.PDF_VIEWER)
            .putString(EXTRA_PDF_URI, pdfScore.uri)
            .let(::navigate)
    }
}