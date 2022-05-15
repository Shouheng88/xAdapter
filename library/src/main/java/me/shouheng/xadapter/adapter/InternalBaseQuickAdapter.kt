package me.shouheng.xadapter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.xadapter.listener.FastScrollerTextGetter

/**
 * Internal base quick adapter for xadapter.
 *
 * @Author wangshouheng
 * @Time 2022/5/14
 */
abstract class InternalBaseQuickAdapter<T, K: BaseViewHolder>: BaseQuickAdapter<T, K>, FastScrollerTextGetter {

    constructor(layoutResId: Int, data: MutableList<T>?) : super(layoutResId, data)
    constructor(data: MutableList<T>?) : super(data)
    constructor(layoutResId: Int) : super(layoutResId)
}