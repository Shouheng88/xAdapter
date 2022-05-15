package me.shouheng.xadapter.listener

/**
 * Fast scroller text getter.
 *
 * @Author wangshouheng
 * @Time 2022/5/14
 */
interface FastScrollerTextGetter {

    /** Get title to display for fast scroller. */
    fun getTitle(position: Int): String?
}