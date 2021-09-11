# xAdapter: Kotlin DSL 风格的 Adapter 封装

<p align="center">
  <a href="http://www.apache.org/licenses/LICENSE-2.0">
    <img src="https://img.shields.io/hexpm/l/plug.svg" alt="License" />
  </a>
  <a href="https://bintray.com/beta/#/easymark/Android/xadapter?tab=overview">
    <img src="https://img.shields.io/maven-metadata/v/https/s01.oss.sonatype.org/service/local/repo_groups/public/content/com/github/Shouheng88/xadapter/maven-metadata.xml.svg" alt="Version" />
  </a>
  <a href="https://www.codacy.com/gh/Shouheng88/xAdapter/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Shouheng88/xAdapter&amp;utm_campaign=Badge_Grade">
    <img src="https://app.codacy.com/project/badge/Grade/695fe1f33d7b45f5b89767b02a2c33b0"/>
  </a>
  <a href="https://travis-ci.org/Shouheng88/xAdapter">
    <img src="https://travis-ci.org/Shouheng88/xAdapter.svg?branch=master" alt="Build"/>
  </a>
  <a href="https://developer.android.com/about/versions/android-4.2.html">
    <img src="https://img.shields.io/badge/API-21%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
   <a href="https://github.com/Shouheng88">
    <img src="https://img.shields.io/badge/Author-wsh-orange.svg?style=flat-square" alt="Author" />
  </a>
  <a target="_blank" href="https://shang.qq.com/wpa/qunwpa?idkey=2711a5fa2e3ecfbaae34bd2cf2c98a5b25dd7d5cc56a3928abee84ae7a984253">
    <img src="https://img.shields.io/badge/QQ%E7%BE%A4-1018235573-orange.svg?style=flat-square" alt="QQ Group" />
  </a>
</P>

## 1、简介

该项目是 KotlinDSL 风格的 Adapter 框架封装，用来简化 Adapter 调用，思想是采用工厂和构建者方式获取 Adapter 避免代码中定义大量的 Adapter 类。该项目在 [BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper) 的 Adapter 的基础上进行了二次封装。采用 Kotlin DSL 风格之后更加优雅和方便快捷，同时更好的支持多类型布局效果。

<div align="center" style="height:400px">
    <image src="images/sample1.gif">
    <image src="images/sample2.gif" style="margin-left:20px">
</div>

## 2、使用

### 2.1 引入依赖

首先，该项目依赖于 [BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)，所以，你需要引入该库之后菜可以使用。该项目已经上传到了 MavenCenteral，你需要先在项目中引入该仓库，

```
allprojects {
    repositories {
        mavenCentral()
    }
}
```

然后在项目中添加如下依赖，

```
implementation "com.github.Shouheng88:xadapter:${latest_version}"
```

### 2.2 使用 Adapter 构建者方法

使用 xAdapter 之后，当你需要定义一个 Adapter 的时候，你无需单独创建一个类文件，只需要通过 `createAdapter()` 方法获取一个 Adapter，

```kotlin
adapter = createAdapter {
    withType(Item::class.java, R.layout.item_eyepetizer_home) {
        // Bind data with viewholder.
        onBind { helper, item ->
            helper.setText(R.id.tv_title, item.data.title)
            helper.setText(R.id.tv_sub_title, item.data.author?.name + " | " + item.data.category)
            helper.loadCover(requireContext(), R.id.iv_cover, item.data.cover?.homepage, R.drawable.recommend_summary_card_bg_unlike)
            helper.loadRoundImage(requireContext(), R.id.iv_author, item.data.author?.icon, R.mipmap.eyepetizer, 20f.dp2px())
        }
        // Item level click and long click events.
        onItemClick { _, _, position ->
            adapter?.getItem(position)?.let {
                toast("Clicked item: " + it.data.title)
            }
        }
    }
}
```

在这种新的调用方式中，你需要通过 `withType()` 方法指定数据类型及其对应的布局文件，然后在 `onBind()` 方法中即可实现数据到 ViewHolder 的绑定操作。这里的 `onBind()` 方法的使用与 BRVAH 中的 `convert()` 方法使用一致，可以通过阅读该库了解如何使用。总之，xAapter 在 BRVAH 的基础上做了二次封装，可以说，比简单更简单。

xAdapter 支持为每个 ViewHolder 绑定点击和长按事件，同时也支持为 ViewHolder 上的某个单独的 View 添加点击和长按事件。使用方式如上所示，只需要添加 `onItemClick()` 方法并实现自己的逻辑即可。其他的点击事件可以参考项目的示例代码。

### 2.3 使用多类型 Adapter

多类型 Adapter 的使用方式非常简单，类似于上面的调用方式，只需要在 `createAdapter()` 内再添加一个 `withType()` 方法即可。下面是一个写起来可能相当复杂的 Adapter，但是采用了 xAdpater 的调用方式之后，一切变得非常简单，

```kotlin
    private fun createAdapter() {
        adapter = createAdapter {
            withType(MultiTypeDataGridStyle::class.java, R.layout.item_list) {
                onBind { helper, item ->
                    val rv = helper.getView<RecyclerView>(R.id.rv)
                    rv.layoutManager = GridLayoutManager(context, 3)
                    val adapter = createSubAdapter(R.layout.item_home_page_data_module_1, 1)
                    rv.adapter = adapter
                    adapter.setNewData(item.items)
                }
            }
            withType(MultiTypeDataListStyle1::class.java, R.layout.item_home_page_data_module_2) {
                onBind { helper, item ->
                    converter.invoke(helper, item)
                }
                onItemClick { _, _, position ->
                    (adapter?.getItem(position) as? MultiTypeDataListStyle1)?.let {
                        toast("Clicked style[2] item: " + it.item.data.title)
                    }
                }
            }
            withType(MultiTypeDataListStyle2::class.java, R.layout.item_list) {
                onBind { helper, item ->
                    val rv = helper.getView<RecyclerView>(R.id.rv)
                    rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    val adapter = createSubAdapter(R.layout.item_home_page_data_module_4, 3)
                    rv.adapter = adapter
                    adapter.setNewData(item.items)
                }
            }
            withType(MultiTypeDataListStyle3::class.java, R.layout.item_home_page_data_module_3) {
                onBind { helper, item ->
                    converter.invoke(helper, item)
                }
                onItemClick { _, _, position ->
                    (adapter?.getItem(position) as? MultiTypeDataListStyle3)?.let {
                        toast("Clicked style[4] item: " + it.item.data.title)
                    }
                }
            }
        }
    }
```

可以通过阅读源码来了解设计思路，以上是部分功能展示，可以通过阅读源码了解更多。后续我们会支持更多 Adapter 特性的封装来实现快速调用。

## 3、关于

- 1.0：首次发布

## License

```
Copyright (c) 2021-2021 Shouheng Wang.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
