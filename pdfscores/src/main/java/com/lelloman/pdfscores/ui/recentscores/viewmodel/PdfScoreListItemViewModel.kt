package com.lelloman.pdfscores.ui.recentscores.viewmodel

import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.pdfscores.ui.recentscores.PdfScoreViewModelItem

class PdfScoreListItemViewModel : BaseListItemViewModel<PdfScoreViewModelItem> {

    var title = ""
        private set

    override fun bind(item: PdfScoreViewModelItem) {
        title = item.title
    }
}