package me.shouheng.xadaptersample.adapter

import me.shouheng.xadapter.R
import me.shouheng.xadapter.createAdapter
import me.shouheng.xadapter.viewholder.onItemChildClick

/////////////////////////////////////////////////////////////////////////////////////////////////////
fun test() {
    createAdapter<CharSequence> {
        withType(CharSequence::class.java, R.layout.brvah_quick_view_load_more) {
            onBind { helper, item ->

            }
//            onItemChildClick { adapter, view, position ->
//
//            }
        }
    }
    createAdapter<S> {
//        withType(String::class.java, R.layout.brvah_quick_view_load_more) { // invalid
//
//        }
        withType(S1::class.java, R.layout.brvah_quick_view_load_more) {

        }
        withType(S2::class.java, R.layout.brvah_quick_view_load_more) {

        }
    }
}

interface S
class S1: S
class S2: S