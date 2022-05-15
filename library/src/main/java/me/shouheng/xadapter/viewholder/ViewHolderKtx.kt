package me.shouheng.xadapter.viewholder

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.xadapter.adapter.AdapterMarker
import me.shouheng.xadapter.listener.FastScrollerTextGetter

/** ViewHolder definition. */
internal open class AdapterViewHolderDefinition<T, VH: BaseViewHolder>(
    val type: Class<T>,
    @LayoutRes val layoutId: Int,
    val setup: AdapterViewHolderSetup<T, VH>
)

/** Internal fast scroller title text getter. */
internal interface InternalFastScrollerTextGetter<IT> {
    fun getTitle(position: Int, item: IT?): String?
}

/** ViewHolder converter, use internal. */
internal interface AdapterViewHolderConverter<IT, VH: BaseViewHolder> {
    fun convert(helper: VH, item: IT)
}

/** Adapter ViewHolder attach to window event callback. */
internal interface AdapterViewHolderAttachCallback<VH: BaseViewHolder> {
    fun onAttachedToWindow(holder: VH)
}

/** Adapter ViewHolder detach from window event callback. */
internal interface AdapterViewHolderDetachCallback<VH: BaseViewHolder> {
    fun onDetachedFromWindow(holder: VH)
}

/** Adapter ViewHolder created event callback. */
internal interface AdapterViewHolderViewCreatedCallback<VH: BaseViewHolder> {
    fun onViewHolderCreated(holder: VH)
}

@AdapterMarker
class AdapterViewHolderSetup<IT, VH: BaseViewHolder> internal constructor() {

    internal var converter: AdapterViewHolderConverter<IT, VH>? = null
    internal var onAttached: AdapterViewHolderAttachCallback<VH>? = null
    internal var onDetached: AdapterViewHolderDetachCallback<VH>? = null
    internal var onCreated: AdapterViewHolderViewCreatedCallback<VH>? = null

    internal var mInternalFastScrollerTextGetter: InternalFastScrollerTextGetter<IT>? = null

    internal var mOnItemClickListener: BaseQuickAdapter.OnItemClickListener? = null
    internal var mOnItemLongClickListener: BaseQuickAdapter.OnItemLongClickListener? = null
    internal var mOnItemChildClickListeners = mutableMapOf<Int, BaseQuickAdapter.OnItemChildClickListener>()
    internal var mOnItemChildLongClickListeners = mutableMapOf<Int, BaseQuickAdapter.OnItemChildLongClickListener>()

    /**
     * On ViewHolder created callback. You are able to add some layout
     * constraints to itemview in this method.
     */
    fun onCreated(block: (holder: VH) -> Unit) {
        onCreated = object : AdapterViewHolderViewCreatedCallback<VH> {
            override fun onViewHolderCreated(holder: VH) {
                block.invoke(holder)
            }
        }
    }

    /** Bind item with holder as BRVAH. */
    fun onBind(block: (helper: VH, item: IT) -> Unit) {
        converter = object : AdapterViewHolderConverter<IT, VH> {
            override fun convert(helper: VH, item: IT) {
                block.invoke(helper, item)
            }
        }
    }

    /** On ViewHolder attached to window. */
    fun onAttached(block: (holder: VH) -> Unit) {
        onAttached = object : AdapterViewHolderAttachCallback<VH> {
            override fun onAttachedToWindow(holder: VH) {
                block.invoke(holder)
            }
        }
    }

    /** On ViewHolder detached from window. */
    fun onDetached(block: (holder: VH) -> Unit) {
        onDetached = object : AdapterViewHolderDetachCallback<VH> {
            override fun onDetachedFromWindow(holder: VH) {
                block.invoke(holder)
            }
        }
    }
}

/** Add callback when for fast scroller text. */
fun <IT, VH: BaseViewHolder> AdapterViewHolderSetup<IT, VH>.onGetFastScrollerText(
    block: (position: Int, item: IT?) -> String?
) {
    mInternalFastScrollerTextGetter = object: InternalFastScrollerTextGetter<IT> {
        override fun getTitle(position: Int, item: IT?): String? {
            return block.invoke(position, item)
        }
    }
}
