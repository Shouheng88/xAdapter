package me.shouheng.xadapter.viewholder

import android.view.View
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/** Add callback when the item is clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemClick(
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    mOnItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}

/** Add callback when the item is long clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemLongClick(
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean
) {
    mOnItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}

/** Add callback when the item child is clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemChildClick(
    @IdRes viewId: Int,
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    mOnItemChildClickListeners[viewId] = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}

/** Add callback when the item child is long clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemChildLongClick(
    @IdRes viewId: Int,
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean
) {
    mOnItemChildLongClickListeners[viewId] = BaseQuickAdapter.OnItemChildLongClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}
