package me.shouheng.xadapter.adapter

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

    internal fun build(): BaseQuickAdapter<IT, VH> {
        val adapter: BaseQuickAdapter<IT, VH>?
        when {
            definitions.isEmpty() -> {
                adapter = object : BaseQuickAdapter<IT, VH>(0) {
                    override fun convert(helper: VH, item: IT) {
                        // do nothing
                    }
                }
            }
            definitions.size == 1 -> {
                val definition = definitions.values.first()
                adapter = object : BaseQuickAdapter<IT, VH>(definition.layoutId) {
                    override fun convert(helper: VH, item: IT) {
                        definition.setup.converter?.convert(helper, item)
                        addClickListeners(helper, definition)
                    }
                }
            }
            else -> {
                adapter = object : BaseMultiTypeQuickAdapter<IT, VH>(mutableListOf()) {
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
                }
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
