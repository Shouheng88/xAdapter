package me.shouheng.xadapter.viewholder

import android.view.View
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/** Add callback when the item is clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemClick(
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    mOnItemClickListener = OnItemClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}

/** Add callback when the item is long clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemLongClick(
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean
) {
    mOnItemLongClickListener = OnItemLongClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}

/** Add callback when the item child is clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemChildClick(
    @IdRes viewId: Int,
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    mOnItemChildClickListeners[viewId] = OnItemChildClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}

/** Add callback when the item child is long clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemChildLongClick(
    @IdRes viewId: Int,
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Boolean
) {
    mOnItemChildLongClickListeners[viewId] = OnItemChildLongClickListener { adapter, view, position ->
        block.invoke(adapter, view, position)
    }
}
