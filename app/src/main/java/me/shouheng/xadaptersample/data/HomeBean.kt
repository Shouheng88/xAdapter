package me.shouheng.xadaptersample.data

import java.io.Serializable

/** Eye data */
data class HomeBean(
    val dialog: Any,
    val issueList: List<Issue>,
    val newestIssueType: String,
    val nextPageUrl: String,
    val nextPublishTime: Long
) : Serializable

/** Eye data */
data class Issue(
    val count: Int,
    val date: Long,
    val itemList: List<Item>,
    val publishTime: Long,
    val releaseTime: Long,
    val type: String
) : Serializable

/** Eye data */
data class Item(
    val `data`: Data,
    val adIndex: Int,
    val id: Int,
    val tag: Any,
    val type: String
) : Serializable

/** Eye data */
data class Data(
    val ad: Boolean,
    val adTrack: Any,
    val author: Author?,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: Cover?,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: Any,
    val duration: Int,
    val favoriteAdTrack: Any,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: Any,
    val labelList: List<Any>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<PlayInfo>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: Provider,
    val releaseTime: Long,
    val remark: Any,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: String,
    val src: Any,
    val subtitles: List<Any>,
    val tags: List<Tag>,
    val thumbPlayUrl: Any,
    val title: String,
    val titlePgc: Any,
    val type: String,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrl
) : Serializable

/** Eye data */
data class PlayInfo(
    val height: Int,
    val name: String,
    val type: String,
    val url: String,
    val urlList: List<Url>,
    val width: Int
) : Serializable

/** Eye data */
data class Url(
    val name: String,
    val size: Int,
    val url: String
) : Serializable

/** Eye data */
data class Provider(
    val alias: String,
    val icon: String,
    val name: String
) : Serializable

/** Eye data */
data class WebUrl(
    val forWeibo: String,
    val raw: String
) : Serializable

/** Eye data */
data class Tag(
    val actionUrl: String,
    val adTrack: Any,
    val bgPicture: String,
    val childTagIdList: Any,
    val childTagList: Any,
    val communityIndex: Int,
    val desc: String,
    val headerImage: String,
    val id: Int,
    val name: String,
    val tagRecType: String
) : Serializable

/** Eye data */
data class Consumption(
    val collectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
) : Serializable

/** Eye data */
data class Cover(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String,
    val sharing: Any
) : Serializable

/** Eye data */
data class Author(
    val adTrack: Any,
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: Follow,
    val icon: String,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: Shield,
    val videoNum: Int
) : Serializable

/** Eye data */
data class Shield(
    val itemId: Int,
    val itemType: String,
    val shielded: Boolean
) : Serializable

/** Eye data */
data class Follow(
    val followed: Boolean,
    val itemId: Int,
    val itemType: String
) : Serializable