package me.shouheng.xadapter.viewholder

import android.view.View
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.xadapter.listener.OnItemChildDebouncedClickListener
import me.shouheng.xadapter.listener.OnItemDebouncedClickListener

/** Add callback when the item is clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemDebouncedClick(
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    mOnItemClickListener = object : OnItemDebouncedClickListener() {
        override fun onItemNoDoubleClick(
            adapter: BaseQuickAdapter<*, *>,
            view: View,
            position: Int
        ) {
            block.invoke(adapter, view, position)
        }
    }
}


/** Add callback when the item child is clicked. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onItemChildDebouncedClick(
    @IdRes viewId: Int,
    block: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    mOnItemChildClickListeners[viewId] = object : OnItemChildDebouncedClickListener() {
        override fun onItemChildNoDoubleClick(
            adapter: BaseQuickAdapter<*, *>,
            view: View,
            position: Int
        ) {
            block.invoke(adapter, view, position)
        }
    }
}
