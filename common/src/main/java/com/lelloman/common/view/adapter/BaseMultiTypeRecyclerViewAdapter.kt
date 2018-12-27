package com.lelloman.common.view.adapter

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.lelloman.common.utils.ModelWithIdListDiffCalculator
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel

interface ItemType<M : ModelWithId, VM : BaseListItemViewModel<M>, DB : ViewDataBinding> {
    val ordinal: Int
    fun createBinding(parent: ViewGroup): ViewDataBinding
    fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?): VM
    fun bindViewModel(viewModel: VM, binding: DB, item: M)
}

abstract class BaseMultiTypeRecyclerViewAdapter<M : ModelWithId>(
    private val onClickListener: ((Any) -> Unit)?,
    private val resourceProvider: ResourceProvider
) : RecyclerView.Adapter<BaseMultiTypeRecyclerViewAdapter.ViewHolder<M>>(),
    Observer<List<M>> {

    protected var data = emptyList<M>()
    private val listItemDiffCalculator = ModelWithIdListDiffCalculator()

    protected abstract val itemsMap: Map<Any, ItemType<M, BaseListItemViewModel<M>, ViewDataBinding>>

    private val viewTypesMap by lazy {
        itemsMap
            .values
            .map { it.ordinal to it }
            .toMap()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<M> {
        val itemType = viewTypesMap[viewType]!!
        val binding = itemType.createBinding(parent)

        return ViewHolder(
            binding = binding,
            onClickListener = onClickListener,
            viewModel = itemType.createViewModel(resourceProvider, onClickListener)
        )
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) =
        getItemType(getItem(position)).ordinal

    override fun onBindViewHolder(viewHolder: ViewHolder<M>, position: Int) {
        val item = getItem(position)
        val itemType = getItemType(item)
        viewHolder.bind(item)
        viewHolder.viewModel.bind(item)
        itemType.bindViewModel(viewHolder.viewModel, viewHolder.binding, viewHolder.item)
        viewHolder.binding.executePendingBindings()
    }

    override fun onChanged(newData: List<M>?) {
        newData?.let {
            val diff = listItemDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    private fun getItem(position: Int): M = data[position]

    private fun getItemType(item: Any): ItemType<M, BaseListItemViewModel<M>, ViewDataBinding> =
        itemsMap[item::class.java]
            ?: throw IllegalArgumentException("No ItemType defined for class ${item::class.java.name}")

    class ViewHolder<M : ModelWithId>(
        val binding: ViewDataBinding,
        val viewModel: BaseListItemViewModel<M>,
        onClickListener: ((M) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var item: M

        init {
            if (onClickListener != null) {
                binding.root.setOnClickListener { onClickListener.invoke(item) }
            }
        }

        fun bind(item: M) {
            this.item = item
        }
    }
}