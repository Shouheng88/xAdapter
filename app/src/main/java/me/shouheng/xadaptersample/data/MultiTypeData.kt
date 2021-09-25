package me.shouheng.xadaptersample.data

interface IMultiTypeData

/** Multi type data for [Item]. */
open class MultiTypeData(val item: Item): IMultiTypeData

/** Multi type data for list style one. */
class MultiTypeDataListStyle1(item: Item): MultiTypeData(item)

/** Multi type data for list style two. */
class MultiTypeDataListStyle2(val items: List<Item>): IMultiTypeData

/** Multi type data for list style three. */
class MultiTypeDataListStyle3(item: Item): MultiTypeData(item)

/** Multi type data for grid style. */
class MultiTypeDataGridStyle(val items: List<Item>): IMultiTypeData
