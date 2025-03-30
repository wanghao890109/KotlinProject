package org.example.project.discovery

import kotlinx.serialization.Serializable


@Serializable
data class InfoFlowRequest(
    val contentType: Int,
    val preQueryOffset: Long? = null,
    val pushNewAlgorithmFlag: Int? = null,
    val pullRefresh: Int,
    val userId: String,
    val pageSize: Int = 20
)

/**
 * 信息流详情
 */
@Serializable
data class InfoFlowBean(
    /**
     * 信息流id
     * 该字段在[com.yofolive.xm.channel.utils.AppJumpHelperKt.jumpToApp]被使用，如果需要需要同步修改
     */
    var infoFlowId: Long = 0,
    /**
     * 用户Id
     */
    var userId: String? = null,
    /**
     * 昵称
     */
    var userNickName: String? = null,
    /**
     * 头像
     */
    var userFaceUrl: String? = null,
    /**
     * 文本内容
     */
    var content: String? = null,
    /**
     * 是否点赞
     */
    var likeFlag: Boolean = false,
    /**
     * 发布时间
     */
    var timestamp: Int = 0,
    /**
     * 发布时间
     */
    var createdTime: String? = null,
    /**
     * 更新时间
     */
    var updateTime: String? = null,
    /**
     * 点赞数量
     */
    var likeCount: Long = 0,
    /**
     * 分享数量
     */
    var shareCount: Long = 0,
    /**
     * 评论数量
     */
    var commentCount: Long = 0,
    /**
     * 阅读数量
     */
    var viewCount: Long = 0,
    /**
     * 视频列表
     */
    var videoInfoList: List<VideoInfoBean>? = listOf(),
    /**
     * 图片列表
     */
    var imageInfoList: List<ImageInfoBean> = listOf(),

    /**
     * 点赞动画类型
     * 0：从左往右(R.layout.layout_like_anmi_view)，1：从右往左(R.layout_like_right_anmi_view)
     */
    var likeSvgaType: Int = 0,
    /**
     * 信息是否可见
     */
    var infoIsVisible: Boolean = true,

    /**
     * 查询算法
     *
     * 在上拉时候需要再次传递过去
     */
    var pushNewAlgorithmFlag: Int? = null,
    /**
     * 参考DiscoveryPublishType
     */
    var contentType: Int = 0,
    /**
     * 0：算法 1：大数据
     */
    val queryFrom: Int = 0,
    /**
     * 审核状态 1：待自动审核 2：自动审核通过 3：自动审核不通过 5：人工审核通过 6：人工审核不通过
     */
    var reviewStatus: Int? = null,
    /**
     * 我的帖子-删除标记,0：正常，1：已删除
     */
    var deletedFlag: Int = 0
) {

    //获取信息流封面，目前只做短视频，取videoInfoList的videoFirstFrameUrl字段
    fun getCover(): String? {
        val firstVideoInfo = videoInfoList?.getOrNull(0)
        return if (firstVideoInfo == null || firstVideoInfo.videoFirstFrameUrl?.isEmpty() == true) {
            imageInfoList.getOrNull(0)?.imageUrl
        } else {
            firstVideoInfo.videoFirstFrameUrl
        }
    }

    //是否为视频
    fun isVideo(): Boolean {
        return videoInfoList?.isNotEmpty() ?: false
    }


    /**
     * 获取图片列表
     */
    fun getPostImageList(): MutableList<ImageInfoBean> {
        if (imageInfoList.isNotEmpty()) {
            return imageInfoList.toMutableList()
        }
        return mutableListOf()
    }

    /**
     * 获取视频列表
     */
    fun getPostVideoList(): MutableList<VideoInfoBean> {
        if (videoInfoList?.isNotEmpty() == true) {
            return videoInfoList!!.toMutableList()
        }
        return mutableListOf()
    }
}

@kotlinx.serialization.Serializable
data class VideoInfoBean(
    var videoUrl: String? = null, //视频链接地址
    /**
     * 视频时长
     * 单位：秒
     */
    var duration: String? = null,
    var videoFirstFrameUrl: String? = null, //视频第一帧url
    var videoFirstFrameWidth: Int? = null, //视频第一帧宽
    var videoFirstFrameHeight: Int? = null, //视频第一帧高
    var videoId: String? = null//腾讯云点播上传的文件id
)

@kotlinx.serialization.Serializable
data class ImageInfoBean(
    var imageUrl: String? = null, //图片链接地址
    var imageWidth: Int? = null, //图片宽
    var imageHeight: Int? = null, //图片高
    var isVideo: Boolean = false, //扩展字段，区分是否视频
)
