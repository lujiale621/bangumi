package com.lc.bangumidemo.KtUtil

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import com.lc.bangumidemo.Activity.ErrorActivity
import com.lc.bangumidemo.MyRetrofit.ResClass.BookContent
import com.lc.bangumidemo.MyRetrofit.ResClass.BookDetail
import com.lc.bangumidemo.MyRetrofit.Retrofit.Retrofitcall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.lc.bangumidemo.RxBus.RxBusBaseMessage
import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.Sqlite.NoveDatabase.*


fun loadbookdatatopage(context:Context, book: BookDetail?, positon:Int, data: BookIndexclass, selectdata: Selectclass, start:Int, end:Int, ispre:Boolean) {
    position =positon
    val mHamdler1 = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                2 -> {
                    try {
                        var result= msg.obj as BookContent
                        var string= result.getString(context)
                        var list = PagesizeUtil.txttolist(
                            string,
                            context,
                            fontsize,
                            linesize
                        )
                        //把数据插入数据库
                        var bookdatacls: BookDataclass
                        var db = MyDatabaseHelper(
                            context,
                            "bookstore",
                            null,
                            1
                        )
                        bookdatacls = BookDataclass(
                            bookDetail!!.data.author, bookDetail!!.data.name, string, list.size,
                            bookDetail!!.list.size, positon
                        )
                        Bookinsert.insertbookdata(db,bookdatacls)
                        if (ispre) {
                            updatapre(
                                context,
                                data,
                                selectdata,
                                start,
                                end
                            )
                        }
                        else {
                            updatanext(
                                context,
                                data,
                                selectdata,
                                start,
                                end
                            )
                        }
                        db.close()
                    }catch (e:Exception){
                        var intent = Intent(context, ErrorActivity::class.java)
                        intent.putExtra("msg",e.toString())
                        intent.putExtra("error","loadbookdatatopage")
                        intent.putExtra("tag","Threadkt")
                        context.startActivity(intent)
                    }

                }
            }
        }
    }
    Thread(Runnable {
        var message = Message()
        try {
            val call = Retrofitcall().getAPIServercontent().getCall(book!!.list[positon].url)
            call.enqueue(object : Callback<BookContent> {
                override fun onResponse(call: Call<BookContent>, response: Response<BookContent>) {
                    val st = response.body()
                    println(st)
                    message.obj = st
                    message.what = 2
                    mHamdler1.sendMessage(message)
                }

                override fun onFailure(call: Call<BookContent>, t: Throwable) {
                    Log.e("loadbookdatatopage","连接失败")
                    message.obj = null
                    message.what = 2
                    mHamdler1.sendMessage(message)
                }

            })
        }catch (e:Exception){
            var intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra("msg",e.toString())
            intent.putExtra("error","loadbookdatatopage")
            intent.putExtra("tag","Threadkt")
            context.startActivity(intent)
        }
    }).start()
}
fun toloadbookdatatopage(context:Context, book: BookDetail?, positon:Int, data: BookIndexclass, selectdata: Selectclass, start:Int, end:Int, ispre:Boolean) {
    position =positon
    val mHamdler1 = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                2 -> {
                    try {
                        var result= msg.obj as BookContent
                        var string= result.getString(context)
                        var list = PagesizeUtil.txttolist(
                            string,
                            context,
                            fontsize,
                            linesize
                        )
                        //把数据插入数据库
                        var bookdatacls: BookDataclass
                        var db = MyDatabaseHelper(
                            context,
                            "bookstore",
                            null,
                            1
                        )
                        bookdatacls = BookDataclass(
                            bookDetail!!.data.author, bookDetail!!.data.name, string, list.size,
                            bookDetail!!.list.size, positon
                        )
                        Bookinsert.insertbookdata(db,bookdatacls)
                        if (ispre) {
                            initupdatapre(
                                context,
                                data,
                                selectdata,
                                start,
                                end
                            )
                            db.close()
                            RxBus.getInstance().send(2, RxBusBaseMessage(2,"finishmap"))
                        }
                        else {
                            initupdatanext(
                                context,
                                data,
                                selectdata,
                                start,
                                end
                            )
                            db.close()
                            if(data.pageindex==0)
                            {
                                RxBus.getInstance().send(2, RxBusBaseMessage(2,"finishmap"))
                            }
                        }
                    }catch (e:Exception){
                        var intent = Intent(context, ErrorActivity::class.java)
                        intent.putExtra("msg",e.toString())
                        intent.putExtra("error","toloadbookdatatopage")
                        intent.putExtra("tag","Threadkt")
                        context.startActivity(intent)
                    }

                }
            }
        }
    }
    Thread(Runnable {
        var message = Message()
        try {
            val call = Retrofitcall().getAPIServercontent().getCall(book!!.list[positon].url)
            call.enqueue(object : Callback<BookContent> {
                override fun onResponse(call: Call<BookContent>, response: Response<BookContent>) {
                    val st = response.body()
                    println(st)
                    message.obj=st
                    message.what=2
                    mHamdler1.sendMessage(message)
                }
                override fun onFailure(call: Call<BookContent>, t: Throwable) {
                    Log.e("toloadbookdatatopage","连接失败")
                    message.obj=null
                    message.what=2
                    mHamdler1.sendMessage(message)
                }

            })
        }catch (e:Exception){
            var intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra("msg",e.toString())
            intent.putExtra("error","toloadbookdatatopage")
            intent.putExtra("tag","Threadkt")
            context.startActivity(intent)
        }
    }).start()
}
fun initloadbookdatatopage(context:Context,book: BookDetail?, positon:Int) {
    position =positon
    Bookreadclean.clean(context)
    val mHamdler1 = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                2 -> {
                    var result:BookContent
                    try {
                         result= msg.obj as BookContent
                        var string= result.getString(context)
                        var list = PagesizeUtil.txttolist(
                            string,
                            context,
                            fontsize,
                            linesize
                        )
                        //把数据插入数据库
                        var bookdatacls: BookDataclass
                        var db = MyDatabaseHelper(
                            context,
                            "bookstore",
                            null,
                            1
                        )
                        bookdatacls = BookDataclass(
                            bookDetail!!.data.author, bookDetail!!.data.name, string, list.size,
                            bookDetail!!.list.size, positon
                        )
                        Bookinsert.insertbookdata(db,bookdatacls)
                        db.close()
                        RxBus.getInstance().send(0, RxBusBaseMessage(0,"initpage"))
                    }catch (e:Exception){
                        var intent = Intent(context, ErrorActivity::class.java)
                        intent.putExtra("msg",e.toString())
                        intent.putExtra("error","initloadbookdatatopage")
                        intent.putExtra("tag","Threadkt")
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
    Thread(Runnable {
        var message = Message()
        try {
            val call = Retrofitcall().getAPIServercontent().getCall(book!!.list[positon].url)
            call.enqueue(object : Callback<BookContent> {
                override fun onResponse(call: Call<BookContent>, response: Response<BookContent>) {
                    val st = response.body()
                    println(st)
                    message.obj=st
                    message.what=2
                    mHamdler1.sendMessage(message)
                }
                override fun onFailure(call: Call<BookContent>, t: Throwable) {

                    Log.e("initloadbookdatatopage","连接失败")
                    message.obj=null
                    message.what=2
                    mHamdler1.sendMessage(message)
                }

            })
        }catch (e:Exception){
            var intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra("msg",e.toString())
            intent.putExtra("error","initloadbookdatatopage")
            intent.putExtra("tag","Threadkt")
            context.startActivity(intent)
        }


    }).start()
}
fun Thdownloadbook(context:Context,book: BookDetail?, positon:Int) {
    position = positon
    val mHamdler1 = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                2 -> {
                    var result: BookContent
                    try {
                        var temp:bookdetailinfo
                        result = msg.obj as BookContent
                        if(result.code==0){
                        var string = result.getString(context)
                        var list = PagesizeUtil.txttolist(
                            string,
                            context,
                            fontsize,
                            linesize
                        )
                         temp=bookdetailinfo(list, position)
                        }else{
                            var list = PagesizeUtil.txttolist(
                                "无法加载本章资源",
                                context,
                                fontsize,
                                linesize
                            )
                            temp=bookdetailinfo(list, position)
                        }
                        RxBus.getInstance().send(7,RxBusBaseMessage(7,temp))
                    } catch (e: Exception) {
                        Log.e("downerror", e.toString())
                    }
                }
            }
        }
    }
    Thread(Runnable {
        var message = Message()
        val call = Retrofitcall().getAPIServercontent().getCall(book!!.list[positon].url)
        call.enqueue(object : Callback<BookContent> {
            override fun onResponse(call: Call<BookContent>, response: Response<BookContent>) {
                val st = response.body()
                println(st)
                message.obj = st
                message.what = 2
                mHamdler1.sendMessage(message)
            }

            override fun onFailure(call: Call<BookContent>, t: Throwable) {
                Log.e("initloadbookdatatopage", "连接失败")
                var list = PagesizeUtil.txttolist(
                    "无法加载本章资源",
                    context,
                    fontsize,
                    linesize
                )
                var temp=bookdetailinfo(list, position)
                RxBus.getInstance().send(7,RxBusBaseMessage(7,temp))

            }
        })


    }).start()
}
