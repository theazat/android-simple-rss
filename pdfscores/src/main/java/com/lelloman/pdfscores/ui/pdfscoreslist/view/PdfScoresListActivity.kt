package com.lelloman.pdfscores.ui.pdfscoreslist.view

import android.support.v7.widget.LinearLayoutManager
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.pdfscores.R
import com.lelloman.pdfscores.databinding.ActivityRecentScoresBinding
import com.lelloman.pdfscores.databinding.ListItemPdfScoreBinding
import com.lelloman.pdfscores.ui.pdfscoreslist.PdfScoreViewModelItem
import com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel.PdfScoreListItemViewModel
import com.lelloman.pdfscores.ui.pdfscoreslist.viewmodel.PdfScoresListViewModel

class PdfScoresListActivity : BaseActivity<PdfScoresListViewModel, ActivityRecentScoresBinding>() {

    override val layoutResId = R.layout.activity_recent_scores

    private val adapter = object : BaseRecyclerViewAdapter<PdfScoreViewModelItem, PdfScoreListItemViewModel, ListItemPdfScoreBinding>(
        onItemClickListener = { viewModel.onPdfScoreClicked(it) }
    ) {
        override val listItemLayoutResId = R.layout.list_item_pdf_score

        override fun bindViewModel(binding: ListItemPdfScoreBinding, viewModel: PdfScoreListItemViewModel) {
            binding.viewModel = viewModel
        }

        override fun createViewModel(viewHolder: BaseViewHolder<PdfScoreViewModelItem, PdfScoreListItemViewModel, ListItemPdfScoreBinding>): PdfScoreListItemViewModel {
            return PdfScoreListItemViewModel()
        }
    }

    override fun setViewModel(binding: ActivityRecentScoresBinding, viewModel: PdfScoresListViewModel) {
        binding.viewModel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        viewModel.recentScores.observe(this, adapter)
    }

    override fun getViewModelClass() = PdfScoresListViewModel::class.java
}