package me.shouheng.xadapter.viewholder

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.xadapter.adapter.AdapterMarker

/** ViewHolder definition. */
internal open class AdapterViewHolderDefinition<T, VH: BaseViewHolder>(
    val type: Class<T>,
    @LayoutRes val layoutId: Int,
    val setup: AdapterViewHolderSetup<T, VH>
)

/** ViewHolder converter, use internal. */
internal interface AdapterViewHolderConverter<IT, VH: BaseViewHolder> {
    fun convert(helper: VH, item: IT)
}

@AdapterMarker
class AdapterViewHolderSetup<IT, VH: BaseViewHolder> internal constructor() {

    internal var converter: AdapterViewHolderConverter<IT, VH>? = null

    internal var mOnItemClickListener: BaseQuickAdapter.OnItemClickListener? = null
    internal var mOnItemLongClickListener: BaseQuickAdapter.OnItemLongClickListener? = null
    internal var mOnItemChildClickListeners = mutableMapOf<Int, BaseQuickAdapter.OnItemChildClickListener>()
    internal var mOnItemChildLongClickListeners = mutableMapOf<Int, BaseQuickAdapter.OnItemChildLongClickListener>()

    /** Bind item with holder as BRVAH. */
    fun onBind(block: (helper: VH, item: IT) -> Unit) {
        converter = object : AdapterViewHolderConverter<IT, VH> {
            override fun convert(helper: VH, item: IT) {
                block.invoke(helper, item)
            }
        }
    }
}
