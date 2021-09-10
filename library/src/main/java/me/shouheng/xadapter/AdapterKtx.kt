package me.shouheng.xadapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.xadapter.adapter.AdapterSetup

/** Create quick adapter. */
fun <IT> createAdapter(
    init: AdapterSetup<IT, BaseViewHolder>.() -> Unit
): BaseQuickAdapter<IT, BaseViewHolder>  {
    val setup = AdapterSetup<IT, BaseViewHolder>()
    setup.apply(init)
    return setup.build()
}
