package com.lelloman.deviceinfo.ui.view

import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.databinding.ListItemNetworkInterfaceBinding
import com.lelloman.deviceinfo.device.NetworkInterface
import com.lelloman.deviceinfo.ui.viewmodel.NetworkInterfaceListItemViewModel

class NetworkInterfaceAdapter : BaseRecyclerViewAdapter<NetworkInterface, NetworkInterfaceListItemViewModel, ListItemNetworkInterfaceBinding>() {
    override val listItemLayoutResId = R.layout.list_item_network_interface

    override fun bindViewModel(binding: ListItemNetworkInterfaceBinding, viewModel: NetworkInterfaceListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseRecyclerViewAdapter.BaseViewHolder<NetworkInterface, NetworkInterfaceListItemViewModel, ListItemNetworkInterfaceBinding>) = NetworkInterfaceListItemViewModel()
}