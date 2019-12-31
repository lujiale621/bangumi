package com.lc.bangumidemo.KtUtil

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity
import com.lc.bangumidemo.Activity.MhuaReadActivity
import com.lc.bangumidemo.Activity.ReadActivity
import com.lc.bangumidemo.MyRetrofit.ResClass.BookDetail
import com.lc.bangumidemo.MyRetrofit.ResClass.Bookdata
import com.lc.bangumidemo.MyRetrofit.Retrofit.Retrofitcall
import com.lc.bangumidemo.MyRetrofit.api.*
import com.lc.bangumidemo.Sqlite.CollectDatabase.CollectdataSelect
import com.lc.bangumidemo.Sqlite.CollectDatabase.Collectdataclass
import com.lc.bangumidemo.Sqlite.CollectDatabase.Collectdbhelper
import com.lc.bangumidemo.Sqlite.NoveDatabase.*
import com.lid.lib.LabelImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.ArrayList


var bookDetail : BookDetail?=null
var mhuaDetail : ManhuaDetailResult?=null
var imglist : MutableList<Bitmap> = ArrayList<Bitmap>()
var novelists: MutableList<Bookdata> = mutableListOf()//小说列表
var mhualists: MutableList<ManhuaDetailData> = mutableListOf()//漫画列表
var movielists: MutableList<DetailResult> = mutableListOf()//漫画列表
var moviesource: DetailList? = null//电影源
var screenwidth=0      //初始屏幕宽度
var screenheight=0    //初始屏幕高度
var fontsize =23 //默认字体大小
var linesize =16 //默认显示行数
var position:Int=0

var hardpageindex=0
var hardcontentindex=0



/**
 * 获取一页字数（字体大小，显示行数）
 */
object PagesizeUtil{

    fun getpagesize(context: Context, fontsize: Int, lintcount: Int): Int {

        return getcount(
            context,
            screenwidth,
            screenheight,
            fontsize,
            lintcount
        )
    }
    fun getcount(context: Context, width: Int, height: Int, fontsize: Int, lincount: Int): Int {

        val st = "吖"
        val si = cal(fontsize, context)
        val w1 = si * st.length
        val widthsub = width - si
        val linecount = (w1 / widthsub).toInt() + 1
        val linesize = (widthsub / si).toInt()
        val count =
            cuttxtt(st, linecount, linesize)//返回加载多少个字能填满一行
        return lincount * count
    }
    fun getlinecount(context: Context, width: Int, height: Int, fontsize: Int, lincount: Int): Int {

        val st = "吖"
        val si = cal(fontsize, context)
        val w1 = si * st.length
        val widthsub = width - si
        val linecount = (w1 / widthsub).toInt() + 1
        val linesize = (widthsub / si).toInt()
        val count = cuttxtt(st, linecount, linesize)//返回加载多少个字能填满一行
        return count
    }
    fun cal(size: Int, context: Context): Float {
        return (size * context.resources.displayMetrics

            .scaledDensity).toInt().toFloat()
    }
    fun cuttxtt(string: String, linecount: Int, linesize: Int): Int {
        val subsize = string.length
        val addsize = linesize * linecount - subsize

        return subsize + addsize
    }

    fun txttolist(st :String,context: Context, fontsize: Int, lintcount: Int): MutableList<String> {
        var listpage: MutableList<String> = ArrayList()
        var sting=st
        var apagesize=
            getpagesize(context, fontsize, lintcount)
        var pagec=st.length/apagesize+1
        var sumsize = pagec*apagesize
        var add=sumsize-st.length
        while (add!=0)
        {
            sting += " "
            add--
        }
        for (i in 0 until pagec) {
            var ste=sting.substring(i*apagesize,(i+1)*apagesize)
            listpage.add(ste)
        }
        return listpage
    }
}
fun destoryandsave(context: Context)
{
    Bookreadclean.clean(context)
    //查询是否存在索引
    var db= MyDatabaseHelper(context, "bookstore", null, 1)
    var selectindex = Selectclass(
        bookDetail!!.data.name,
        bookDetail!!.data.author,
        bookDetail!!.list.size
    )
    var returnsult= Bookselect.selectindex(db,selectindex)
    if (returnsult != null) {
        hardpageindex =returnsult.pageindex
    }
    if (returnsult != null) {
        hardcontentindex =returnsult.contentindex
    }
    var destoryvalue= BookIndexclass(
        null, bookDetail!!.data.author, bookDetail!!.data.name,
        hardpageindex,
        hardcontentindex, bookDetail!!.list.size,
        hardpageindex,
        hardcontentindex
    )
    Bookupdata.updata(db,destoryvalue)
}
fun stdeal(string: String?,line:Int): String {
    if (string == null) {
        return "无"
    }
    if (string.length > line) {
        return string.substring(0, line) + "..."
    } else {
        return string
    }
}

fun stnull(string: String?): String {
    if (string == null) {
        return "无"
    }
    return string
}
fun gotoread(requestdata: Collectdataclass,context: Context, cover: LabelImageView?){
    when(requestdata.tag){
        "小说"->{
            val call = Retrofitcall().getAPIServerdetail().getCall(requestdata.url )
            call.enqueue(object : Callback<BookDetail> {
                override fun onFailure(call: Call<BookDetail>, t: Throwable) {
                    println("连接失败")
                }

                override fun onResponse(
                    call: Call<BookDetail>,
                    response: Response<BookDetail>
                ) {
                    val st = response.body()
                    if (cover==null) {
                        println(st)
                        try {
                            bookDetail = st
                            var intent = Intent(context, ReadActivity::class.java)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            print("此书已失效")

                        }
                    }else{
                        var db = Collectdbhelper(context, "collect.db", null, 1)
                        var selectdata = Collectdataclass(
                            requestdata.name,
                            requestdata.author,
                            requestdata.size,
                            requestdata.updatatime,
                            requestdata.tag,
                            requestdata.cover,
                            requestdata.url
                        )
                        var result = CollectdataSelect.selectcollectdata(db, selectdata)
                        if (result!=null){
                            if(result.size>requestdata.size)
                            {
                                cover.labelText="new"
                            }
                        }
                    }
                }
            })}
        "漫画"->{
            var manhualoader= ManhuaLoader()
            if (requestdata.url != null) {
                manhualoader.getManhuaDetail(requestdata.url).subscribe(object : HttpObserver<ManhuaDetailResult>(){
                    override fun onSuccess(t: ManhuaDetailResult?) {
                        mhuaDetail=t
                        if (cover==null){
                        var intent = Intent(context, MhuaReadActivity::class.java)
                        context.startActivity(intent)
                        }else{
                            var db = Collectdbhelper(context, "collect.db", null, 1)
                            var selectdata = Collectdataclass(
                                requestdata.name,
                                requestdata.author,
                                requestdata.size,
                                requestdata.updatatime,
                                requestdata.tag,
                                requestdata.cover,
                                requestdata.url
                            )
                            var result = CollectdataSelect.selectcollectdata(db, selectdata)
                            if (result!=null){
                                if(result.size>requestdata.size)
                                {
                                    cover.labelText="new"
                                }
                            }
                        }
                    }
                    override fun onError(code: Int, msg: String?) {
                        super.onError(code, msg)
                        print("此书已失效")
                    }
                })
            }}
    }
}



