package com.lelloman.read.ui.walkthrough.view

import android.arch.lifecycle.LifecycleOwner
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.databinding.PagerItemDiscoverUrlBinding
import com.lelloman.read.databinding.PagerItemWalkthrough1Binding
import com.lelloman.read.databinding.PagerItemWalkthroughMeteredNetworkBinding
import com.lelloman.read.ui.walkthrough.viewmodel.WalkthroughViewModel

class WalkthroughPagerAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val walkthroughViewModel: WalkthroughViewModel
) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = LAYOUT_IDS.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = LAYOUT_IDS[position]

        val view = LayoutInflater.from(container.context).inflate(layoutId, container, false)

        val binding: ViewDataBinding? = when (layoutId) {
            R.layout.pager_item_discover_url -> bind<PagerItemDiscoverUrlBinding>(view).apply {
                viewModel = this@WalkthroughPagerAdapter.walkthroughViewModel
            }
            R.layout.pager_item_walkthrough_metered_network -> bind<PagerItemWalkthroughMeteredNetworkBinding>(view).apply {
                viewModel = this@WalkthroughPagerAdapter.walkthroughViewModel
            }
            R.layout.pager_item_walkthrough_1 -> bind<PagerItemWalkthrough1Binding>(view).apply {
                viewModel = this@WalkthroughPagerAdapter.walkthroughViewModel
            }
            else -> null
        }

        binding?.setLifecycleOwner(lifecycleOwner)

        container.addView(view)

        return view
    }

    private fun <T : ViewDataBinding> bind(view: View) = DataBindingUtil.bind<T>(view)!!

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    private companion object {
        val LAYOUT_IDS = arrayOf(
            R.layout.pager_item_walkthrough_1,
            R.layout.pager_item_discover_url,
            R.layout.pager_item_walkthrough_metered_network
        )
    }
}