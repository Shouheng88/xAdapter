package me.shouheng.xadapter.adapter

import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.shouheng.xadapter.listener.FastScrollerTextGetter

/**
 * Custom typed quick adapter as [com.chad.library.adapter.base.BaseMultiItemQuickAdapter].
 * This adapter use the class type id as the item view type, so you don't need to implement
 * any interfaces and extend any classes.
 *
 * @Author wangshouheng
 * @Time 2021/9/10
 */
abstract class BaseMultiTypeQuickAdapter<T, K : BaseViewHolder>(data: MutableList<T>? = null)
    : BaseQuickAdapter<T, K>(0, data), FastScrollerTextGetter {
    private val layouts: SparseIntArray by lazy(LazyThreadSafetyMode.NONE) { SparseIntArray() }

    override fun getDefItemViewType(position: Int): Int {
        val item = data[position]
        if (item != null) return item!!::class.hashCode()
        return 0
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        val layoutResId = layouts.get(viewType)
        require(layoutResId != 0) { "ViewType: $viewType found layoutResId，please use addItemType() first!" }
        return createBaseViewHolder(parent, layoutResId)
    }

    /**
     * 调用此方法，设置多布局
     * @param type Int
     * @param layoutResId Int
     */
    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        layouts.put(type, layoutResId)
    }
}