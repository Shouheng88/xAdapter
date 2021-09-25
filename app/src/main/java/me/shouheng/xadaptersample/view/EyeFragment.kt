package me.shouheng.xadaptersample.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.uix.widget.rv.listener.DataLoadScrollListener
import me.shouheng.utils.ktx.dp2px
import me.shouheng.vmlib.base.ViewBindingFragment
import me.shouheng.xadapter.createAdapter
import me.shouheng.xadapter.viewholder.onItemChildClick
import me.shouheng.xadapter.viewholder.onItemChildLongClick
import me.shouheng.xadapter.viewholder.onItemClick
import me.shouheng.xadapter.viewholder.onItemLongClick
import me.shouheng.xadaptersample.R
import me.shouheng.xadaptersample.databinding.FragmentListBinding
import me.shouheng.xadaptersample.data.HomeBean
import me.shouheng.xadaptersample.data.Item
import me.shouheng.xadaptersample.vm.EyeViewModel
import me.shouheng.xadaptersample.widget.loadCover
import me.shouheng.xadaptersample.widget.loadRoundImage

/** Sample fragment for xAdatper of eye. */
class EyeFragment : ViewBindingFragment<EyeViewModel, FragmentListBinding>() {

    private var scrollListener: DataLoadScrollListener? = null

    private var adapter: BaseQuickAdapter<Item, BaseViewHolder>? = null

    override fun doCreateView(savedInstanceState: Bundle?) {
        createAdapter()
        binding.rv.adapter = adapter
        scrollListener = object : DataLoadScrollListener(binding.rv.layoutManager as LinearLayoutManager) {
            override fun loadMore() {
                vm.nextPage()
            }
        }
        binding.rv.addOnScrollListener(scrollListener!!)
        observes()
        vm.firstPage()
    }

    /** Create an adapter based on the kotlin DSL. */
    private fun createAdapter() {
        adapter = createAdapter {
            withType(Item::class.java, R.layout.item_eyepetizer_home) {
                // Bind data with viewholder.
                onBind { helper, item ->
                    helper.setText(R.id.tv_title, item.data.title)
                    helper.setText(R.id.tv_sub_title, item.data.author?.name + " | " + item.data.category)
                    helper.loadCover(requireContext(), R.id.iv_cover, item.data.cover?.homepage, R.drawable.recommend_summary_card_bg_unlike)
                    helper.loadRoundImage(requireContext(), R.id.iv_author, item.data.author?.icon, R.mipmap.eyepetizer, 20f.dp2px())
                }
                // Item level click and long click events.
                onItemClick { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Clicked item: " + it.data.title)
                    }
                }
                onItemLongClick { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Long clicked item: " + it.data.title)
                    }
                    true
                }
                // Item child level click and long click events.
                onItemChildClick(R.id.iv_author) { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Clicked icon: " + it.data.title)
                    }
                }
                onItemChildLongClick(R.id.iv_author) { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Long clicked icon: " + it.data.title)
                    }
                    true
                }
                onItemChildClick(R.id.tv_title) { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Clicked title: " + it.data.title)
                    }
                }
                onItemChildClick(R.id.tv_sub_title) { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Clicked subtitle: " + it.data.title)
                    }
                }
                onItemChildLongClick(R.id.tv_sub_title) { _, _, position ->
                    adapter?.getItem(position)?.let {
                        toast("Long clicked subtitle: " + it.data.title)
                    }
                    true
                }
            }
        }
    }

    private fun observes() {
        observe(HomeBean::class.java, success = {
            val list = mutableListOf<Item>()
            it.data.issueList.forEach { issue ->
                issue.itemList.forEach { item ->
                    if (item.data.cover != null
                        && item.data.author != null
                    ) list.add(item)
                }
            }
            adapter?.addData(list as List<Item>)
            scrollListener?.loading = false
        }, fail = {
            scrollListener?.loading = false
            toast(it.message)
        })
    }
}
