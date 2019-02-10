package com.lelloman.simplerss.ui.sources.view

import android.view.ViewGroup
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemSourceBinding
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.ui.sources.viewmodel.SourceListItemViewModel

class SourcesAdapter(
    private val resourceProvider: ResourceProvider,
    private val semanticTimeProvider: SemanticTimeProvider,
    onSourceClickedListener: (source: Source) -> Unit,
    private val onSourceIsActiveChangedListener: (sourceId: Long, isActive: Boolean) -> Unit
) : BaseRecyclerViewAdapter<Long, Source, SourceListItemViewModel, ListItemSourceBinding>(
    onItemClickListener = onSourceClickedListener
) {

    private val viewHolders = mutableListOf<BaseViewHolder<Long, Source, SourceListItemViewModel, *>>()

    override val listItemLayoutResId = R.layout.list_item_source

    override fun bindViewModel(binding: ListItemSourceBinding, viewModel: SourceListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        super.onCreateViewHolder(parent, viewType).also { viewHolders.add(it) }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, Source, SourceListItemViewModel, ListItemSourceBinding>) = SourceListItemViewModel(
        resourceProvider = resourceProvider,
        semanticTimeProvider = semanticTimeProvider,
        onIsActiveChanged = { onSourceIsActiveChangedListener.invoke(viewHolder.item.id, it) }
    )

    fun tick() = viewHolders.forEach { it.viewModel.tick() }
}