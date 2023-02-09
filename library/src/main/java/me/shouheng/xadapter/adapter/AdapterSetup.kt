package me.shouheng.xadapter.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.xadapter.viewholder.AdapterViewHolderDefinition
import me.shouheng.xadapter.viewholder.AdapterViewHolderSetup

@DslMarker
annotation class AdapterMarker

@AdapterMarker
class AdapterSetup<IT, VH: BaseViewHolder> internal constructor() {

    private var definitions = mutableMapOf<Int, AdapterViewHolderDefinition<IT, VH>>()

    /** Specify the data type [type], layout [layoutId] and DSL builder [init] for ViewHolder. */
    fun <T: IT> withType(
        type: Class<T>,
        @LayoutRes layoutId: Int,
        init: AdapterViewHolderSetup<T, VH>.() -> Unit
    ) {
        val setup = AdapterViewHolderSetup<T, VH>()
        setup.apply(init)
        val definition = AdapterViewHolderDefinition(type, layoutId, setup)
        definitions[type.hashCode()] = definition as AdapterViewHolderDefinition<IT, VH>
    }

    /** Register click and long click events of child views for viewholder. */
    private fun addClickListeners(helper: VH, definition: AdapterViewHolderDefinition<IT, VH>) {
        definition.setup.mOnItemChildClickListeners.keys.forEach {
            helper.addOnClickListener(it)
        }
        definition.setup.mOnItemChildLongClickListeners.keys.forEach {
            helper.addOnLongClickListener(it)
        }
    }

    /** Get definition from adapter for position. */
    private fun getDefinition(
        adapter: BaseQuickAdapter<IT, VH>,
        position: Int
    ): AdapterViewHolderDefinition<IT, VH>? {
        if (definitions.size == 1) {
            return definitions.values.first()
        }
        val type = adapter.getItemViewType(position)
        return definitions[type]
    }

    /** Create empty adapter. */
    private fun createEmptyAdapter(): BaseQuickAdapter<IT, VH> {
        return object : InternalBaseQuickAdapter<IT, VH>(0) {
            override fun convert(helper: VH, item: IT) {
                // do nothing
            }

            override fun getTitle(position: Int): String? = null
        }
    }

    /**
     * Create single type adapter. The [createMultiTypeAdapter] is also suitable for
     * single type adapter. The main reason we separate multi and single is for better
     * performance for single type.
     */
    private fun createSingleTypeAdapter(): BaseQuickAdapter<IT, VH> {
        val definition = definitions.values.first()
        return object : InternalBaseQuickAdapter<IT, VH>(definition.layoutId) {
            override fun convert(helper: VH, item: IT) {
                definition.setup.converter?.convert(helper, item)
                addClickListeners(helper, definition)
            }

            override fun onViewAttachedToWindow(holder: VH) {
                super.onViewAttachedToWindow(holder)
                definition.setup.onAttached?.onAttachedToWindow(holder)
            }

            override fun onViewDetachedFromWindow(holder: VH) {
                super.onViewDetachedFromWindow(holder)
                definition.setup.onDetached?.onDetachedFromWindow(holder)
            }

            override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): VH {
                val vh = super.onCreateDefViewHolder(parent, viewType)
                definition.setup.onCreated?.onViewHolderCreated(vh)
                return vh
            }

            override fun getTitle(position: Int): String? {
                return definition.setup
                    .mInternalFastScrollerTextGetter
                    ?.getTitle(position, getItem(position))
            }
        }
    }

    /** Create multi-type adapter. */
    private fun createMultiTypeAdapter(): BaseQuickAdapter<IT, VH> {
        return object : BaseMultiTypeQuickAdapter<IT, VH>(mutableListOf()) {
            init {
                definitions.entries.forEach {
                    addItemType(it.key, it.value.layoutId)
                }
            }
            override fun convert(helper: VH, item: IT) {
                val definition = definitions[item!!::class.java.hashCode()]
                definition?.setup?.converter?.convert(helper, item)
                definition?.let {
                    addClickListeners(helper, definition)
                }
            }

            override fun onViewAttachedToWindow(holder: VH) {
                super.onViewAttachedToWindow(holder)
                definitions[holder.itemViewType]?.setup?.onAttached?.onAttachedToWindow(holder)
            }

            override fun onViewDetachedFromWindow(holder: VH) {
                super.onViewDetachedFromWindow(holder)
                definitions[holder.itemViewType]?.setup?.onDetached?.onDetachedFromWindow(holder)
            }

            override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): VH {
                val vh = super.onCreateDefViewHolder(parent, viewType)
                definitions[viewType]?.setup?.onCreated?.onViewHolderCreated(vh)
                return vh
            }

            override fun getTitle(position: Int): String? {
                return getDefinition(this, position)
                    ?.setup
                    ?.mInternalFastScrollerTextGetter
                    ?.getTitle(position, getItem(position))
            }
        }
    }

    internal fun build(): BaseQuickAdapter<IT, VH> {
        val adapter: BaseQuickAdapter<IT, VH> = when {
            definitions.isEmpty() -> {
                createEmptyAdapter()
            }
            definitions.size == 1 -> {
                createSingleTypeAdapter()
            }
            else -> {
                createMultiTypeAdapter()
            }
        }
        // Set click events for adapter. Here we use the item view type as key to get
        // definition of item and then invoke its click callback.
        adapter.setOnItemClickListener { a, view, position ->
            getDefinition(adapter, position)?.setup
                ?.mOnItemClickListener
                ?.onItemClick(a, view, position)
        }
        adapter.setOnItemChildClickListener { a, view, position ->
            getDefinition(adapter, position)?.setup
                ?.mOnItemChildClickListeners
                ?.get(view.id)
                ?.onItemChildClick(a, view, position)
        }
        adapter.setOnItemLongClickListener { a, view, position ->
            return@setOnItemLongClickListener getDefinition(adapter, position)?.setup
                ?.mOnItemLongClickListener
                ?.onItemLongClick(a, view, position)
                ?: false
        }
        adapter.setOnItemChildLongClickListener { a, view, position ->
            return@setOnItemChildLongClickListener getDefinition(adapter, position)?.setup
                ?.mOnItemChildLongClickListeners
                ?.get(view.id)
                ?.onItemChildLongClick(a, view, position)
                ?: false
        }
        return adapter
    }
}
