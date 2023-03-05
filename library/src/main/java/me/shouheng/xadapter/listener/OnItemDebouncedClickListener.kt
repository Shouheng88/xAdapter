package me.shouheng.xadapter.listener

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * On item debounced click listener.
 *
 * @Author wangshouheng
 * @Time 2023/3/5
 */
abstract class OnItemDebouncedClickListener: BaseQuickAdapter.OnItemClickListener {

    private var lastClickTime: Long = 0

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onItemNoDoubleClick(adapter, view, position)
        }
    }

    abstract fun onItemNoDoubleClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)

    companion object {
        var MIN_CLICK_DELAY_TIME                       = 500L
    }
}
