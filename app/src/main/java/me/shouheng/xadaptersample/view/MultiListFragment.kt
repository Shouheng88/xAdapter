package me.shouheng.xadaptersample.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.uix.widget.rv.listener.DataLoadScrollListener
import me.shouheng.utils.ktx.dp
import me.shouheng.utils.stability.L
import me.shouheng.vmlib.base.ViewBindingFragment
import me.shouheng.xadapter.createAdapter
import me.shouheng.xadapter.viewholder.onItemChildClick
import me.shouheng.xadapter.viewholder.onItemClick
import me.shouheng.xadaptersample.R
import me.shouheng.xadaptersample.data.*
import me.shouheng.xadaptersample.databinding.FragmentListBinding
import me.shouheng.xadaptersample.vm.EyeViewModel
import me.shouheng.xadaptersample.widget.loadCover
import me.shouheng.xadaptersample.widget.loadRoundImage

/**
 * Multi type list fragment sample.
 *
 * @Author wangshouheng
 * @Time 2021/9/10
 */
class MultiListFragment: ViewBindingFragment<EyeViewModel, FragmentListBinding>() {

    private var scrollListener: DataLoadScrollListener? = null

    private var adapter: BaseQuickAdapter<IMultiTypeData, BaseViewHolder>? = null

    private val converter = { helper: BaseViewHolder , item: MultiTypeData ->
        helper.setText(R.id.tv1, item.item.data.title)
        helper.setText(R.id.tv2, item.item.data.author?.name + " | " + item.item.data.category)
        helper.loadRoundImage(requireContext(), R.id.iv, item.item.data.cover?.homepage, R.drawable.recommend_summary_card_bg_unlike, 4f.dp())
    }

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

    private fun createAdapter() {
        adapter = createAdapter {
            withType(MultiTypeDataGridStyle::class.java, R.layout.item_list) {
                onBind { helper, item ->
                    val rv = helper.getView<RecyclerView>(R.id.rv)
                    rv.layoutManager = GridLayoutManager(context, 3)
                    val adapter = createSubAdapter(R.layout.item_home_page_data_module_1, 1)
                    rv.adapter = adapter
                    adapter.setNewData(item.items)
                }
            }
            withType(MultiTypeDataListStyle1::class.java, R.layout.item_home_page_data_module_2) {
                onBind { helper, item ->
                    converter.invoke(helper, item)
                }
                onItemClick { _, _, position ->
                    (adapter?.getItem(position) as? MultiTypeDataListStyle1)?.let {
                        toast("Clicked style[2] item: " + it.item.data.title)
                    }
                }
                onAttached {
                    L.d("On viewholder attached to window: $it, pos: ${it.adapterPosition}")
                }
                onDetached {
                    L.d("On viewholder detached From window: $it, pos: ${it.adapterPosition}")
                }
            }
            withType(MultiTypeDataListStyle2::class.java, R.layout.item_list) {
                onBind { helper, item ->
                    val rv = helper.getView<RecyclerView>(R.id.rv)
                    rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    val adapter = createSubAdapter(R.layout.item_home_page_data_module_4, 3)
                    rv.adapter = adapter
                    adapter.setNewData(item.items)
                }
            }
            withType(MultiTypeDataListStyle3::class.java, R.layout.item_home_page_data_module_3) {
                onBind { helper, item ->
                    converter.invoke(helper, item)
                }
                onItemClick { _, _, position ->
                    (adapter?.getItem(position) as? MultiTypeDataListStyle3)?.let {
                        toast("Clicked style[4] item: " + it.item.data.title)
                    }
                }
                onItemChildClick(R.id.iv_more) { _, _, position ->
                    (adapter?.getItem(position) as? MultiTypeDataListStyle3)?.let {
                        toast("Clicked style[4] more: " + it.item.data.title)
                    }
                }
            }
        }
    }

    private fun createSubAdapter(@LayoutRes layout: Int, style: Int): BaseQuickAdapter<Item, BaseViewHolder> {
        return createAdapter {
            withType(Item::class.java, layout) {
                onBind { helper, item ->
                    helper.setText(R.id.tv1, item.data.title)
                    helper.setText(R.id.tv2, item.data.author?.name + " | " + item.data.category)
                    helper.loadCover(requireContext(), R.id.iv, item.data.cover?.homepage, R.drawable.recommend_summary_card_bg_unlike)
                }
                onItemClick { adapter, _, position ->
                    (adapter.getItem(position) as? Item)?.let {
                        toast("Clicked style[${style}] item: " + it.data.title)
                    }
                }
                onCreated {
                    L.d("On viewholder is created : $it")
                }
            }
        }
    }

    private fun observes() {
        observe(HomeBean::class.java, {
            val items = mutableListOf<Item>()
            it.data.issueList.forEach { issue ->
                issue.itemList.forEach { item ->
                    if (item.data.cover != null
                        && item.data.author != null
                    ) items.add(item)
                }
            }
            val list = mutableListOf<IMultiTypeData>()
            if (items.isNotEmpty()) {
                list.add(MultiTypeDataGridStyle(items.subList(0, minOf(6, items.size))))
                items.subList(0, items.size/2).forEach { item ->
                    list.add(MultiTypeDataListStyle1(item))
                }
                list.add(MultiTypeDataListStyle2(items.subList(0, minOf(6, items.size))))
                items.subList(items.size/2, items.size).forEach { item ->
                    list.add(MultiTypeDataListStyle3(item))
                }
            }
            adapter?.addData(list)
            scrollListener?.loading = false
        }, fail = {
            scrollListener?.loading = false
            toast(it.message)
        })
    }
}
