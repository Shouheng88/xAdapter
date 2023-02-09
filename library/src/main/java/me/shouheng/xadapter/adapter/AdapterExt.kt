package me.shouheng.xadapter.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseViewHolder

/** Set text size (sp). */
fun BaseViewHolder.setTextSize(@IdRes id: Int, size: Float) {
    (getView<View>(id) as? TextView)?.textSize = size
}

/** Set text size with unit. */
fun BaseViewHolder.setTextSize(@IdRes id: Int, unit: Int, size: Float) {
    (getView<View>(id) as? TextView)?.setTextSize(unit, size)
}

/** Set text view visibility. */
fun BaseViewHolder.setVisibility(@IdRes id: Int, visibility: Int) {
    getView<View>(id).visibility = visibility
}

/** Set view background. */
fun BaseViewHolder.setBackground(@IdRes id: Int, background: Drawable?) {
    getView<View>(id).background = background
}

/** Make given view gone if satisfy given condition defined by [goneIf]. */
fun BaseViewHolder.goneIf(@IdRes id: Int, goneIf: Boolean) {
    this.getView<View>(id).visibility = if (goneIf) View.GONE else View.VISIBLE
}
