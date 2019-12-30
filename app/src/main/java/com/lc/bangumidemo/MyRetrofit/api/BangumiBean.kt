package com.lc.bangumidemo.MyRetrofit.api

data class ContentResult(
    val content: List<String>,
    val mum: String,
    val message: String,
    val code: Int,
    val list: List<String>
)

data class DetailResult(
    val data: DetailData,
    val list: List<DetailList>,
    val message: String,
    val code: Int
)

data class SearchResult(
    val list: List<DetailData>,
    val message: String,
    val code: Int
)

data class DetailData(
    val name: String,
    val genre: String,
    val time: String,
    val url: String,
    val cover:String,
    val introduce: String,
    val Release:String,
    val director:String,
    val performer:String,
    val region:String,
    val Language:String
)

data class DetailList(
    val m3u8url: String,
    val onlineurl: String,
    val download: String,
    val num: String,
    val name: String,
    val url: String
)
data class ManhuaSearchResult(
    val list: List<ManhuaDetailData>,
    val message: String,
    val code: Int
)
data class ManhuaDetailResult(
    val data: ManhuaDetailData,
    val list: List<ManhuaDetailList>,
    val message: String,
    val code: Int
)
data class ManhuaDetailList(
    val num: String,
    val url: String
)
data class ManhuaDetailData(
    val name: String,
    val cover: String,
    val introduce: String,
    val author: String,
    val status: String,
    val tag: String,
    val time:String,
    val url:String,
    val latest:String
)

data class ManhuaContentResult(
    val list: List<ManhuaImg>,
    val message: String,
    val code: Int
)
data class ManhuaImg(
    val img:String
)
