package com.lc.bangumidemo.KtUtil

import android.content.Context
import com.lc.bangumidemo.RxBus.RxBusBaseMessage
import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.Sqlite.NoveDatabase.*


fun Mapinit(context: Context, data: BookIndexclass) {
    //查询数据
    var db = MyDatabaseHelper(context, "bookstore", null, 1)
    var selectclass = Selectclass(
        data.bookname,
        data.author,
        data.pagecount
    )
    var returnsult = Bookselect.selectbookdata(db, selectclass, data.pageindex)!!
    var list = PagesizeUtil.txttolist(
        returnsult.content,
        context,
        fontsize,
        linesize
    )
    var index = -data.hardcontentindex
    var content = 0
    for (st in list) {
        var readdata = BookReadclass(
            data.author,
            data.bookname,
            data.pagecount,
            st,
            data.pageindex,
            content++,
            -data.hardcontentindex,
            list.size - hardcontentindex,
            index
        )
        //插入数据库
        Bookinsert.insertbookread(db, readdata)
        index++
    }
    db.close()
    RxBus.getInstance().send(1, RxBusBaseMessage(1,"initmapfirst"))

}
fun initupdatapre(context: Context, data: BookIndexclass, selectclass: Selectclass, start:Int, end:Int){
    var db = MyDatabaseHelper(context, "bookstore", null, 1)
    var returnsultpre = Bookselect.selectbookread(db, selectclass, data.pageindex - 1)
    if (data.pageindex != 0) {
        if (returnsultpre == null) {
            var selectclass = Selectclass(
                data.bookname,
                data.author,
                data.pagecount
            )
            var returnsult = Bookselect.selectbookdata(db, selectclass, data.pageindex - 1)
            if (returnsult != null) {
                var list =
                    PagesizeUtil.txttolist(
                        returnsult.content,
                        context,
                        fontsize,
                        linesize
                    )
                var index = start - list.size
                var content = 0
                for (st in list) {
                    var readdata = BookReadclass(
                        data.author,
                        data.bookname,
                        data.pagecount,
                        st,
                        data.pageindex - 1,
                        content++,
                        start - list.size,
                        start,
                        index
                    )
                    //插入数据库
                    Bookinsert.insertbookread(db, readdata)
                    index++

                }
            }
        }
    }
    db.close()
}
fun initupdatanext(context: Context, data: BookIndexclass, selectclass: Selectclass, start:Int, end:Int){
    var db2 = MyDatabaseHelper(context, "bookstore", null, 1)
    var returnsultnext = Bookselect.selectbookread(db2, selectclass, data.pageindex + 1)
    if (returnsultnext == null) {
        var selectclass = Selectclass(
            data.bookname,
            data.author,
            data.pagecount
        )
        var returnsult = Bookselect.selectbookdata(db2, selectclass, data.pageindex + 1)
        if (returnsult != null) {
            var list =
                PagesizeUtil.txttolist(
                    returnsult.content,
                    context,
                    fontsize,
                    linesize
                )
            var index = end
            var content = 0
            for (st in list) {
                var readdata = BookReadclass(
                    data.author,
                    data.bookname,
                    data.pagecount,
                    st,
                    data.pageindex + 1,
                    content++,
                    end,
                    list.size + end,
                    index
                )
                //插入数据库
                Bookinsert.insertbookread(db2, readdata)
                index++

            }
        }
    }
    db2.close()
}
fun InitMapupdata(context: Context, data: BookIndexclass) {
    //获取本章的start end信息
    var db = MyDatabaseHelper(context, "bookstore", null, 1)
    var selectclass = Selectclass(
        data.bookname,
        data.author,
        data.pagecount
    )
    var returnsult = Bookselect.selectbookread(db, selectclass, data.pageindex)!!
    var start = returnsult.start
    var end = returnsult.end
    //查询上下章是否已映射，查询上下章信息 //如果没有数据，则加载
    //如果没有数据，则加载
    if (data.pageindex != 0) {
        var i =
            MyDatabaseHelper(context, "bookstore", null, 1)
        var selectdata = Selectclass(
            bookDetail!!.data.name,
            bookDetail!!.data.author,
            bookDetail!!.list.size
        )
        var resultnow = Bookselect.selectbookdata(i, selectdata, data.pageindex - 1)
        if (resultnow == null){
            toloadbookdatatopage(
                context,
                bookDetail,
                data.pageindex - 1,
                data,
                selectdata,
                start,
                end,
                true
            )
        }
        else{
            initupdatapre(context, data, selectclass, start, end)
            RxBus.getInstance().send(2, RxBusBaseMessage(2,"finishmap"))
        }
        i.close()
    }
        var i =
            MyDatabaseHelper(context, "bookstore", null, 1)
        var selectdata = Selectclass(
            bookDetail!!.data.name,
            bookDetail!!.data.author,
            bookDetail!!.list.size
        )
        var resultnow = Bookselect.selectbookdata(i, selectdata, data.pageindex + 1)
        if (data.pageindex != bookDetail!!.list.size - 1) {
            if (resultnow == null) {
                toloadbookdatatopage(
                    context,
                    bookDetail,
                    data.pageindex + 1,
                    data,
                    selectdata,
                    start,
                    end,
                    false
                )
            }
                else{
                initupdatanext(context, data, selectdata, start, end)
                if(data.pageindex==0)
                {RxBus.getInstance().send(2, RxBusBaseMessage(2,"finishmap"))}
            }
        }
        i.close()
    db.close()
}

fun Mapupdata(context: Context, data: BookIndexclass) {
    //获取本章的start end信息
    var db = MyDatabaseHelper(context, "bookstore", null, 1)
    var selectclass = Selectclass(
        data.bookname,
        data.author,
        data.pagecount
    )
    var returnsult = Bookselect.selectbookread(db, selectclass, data.pageindex)!!
    var start = returnsult.start
    var end = returnsult.end
    //查询上下章是否已映射，查询上下章信息 //如果没有数据，则加载
    //如果没有数据，则加载
    if (data.pageindex != 0) {
        var i =
            MyDatabaseHelper(context, "bookstore", null, 1)
        var selectdata = Selectclass(
            bookDetail!!.data.name,
            bookDetail!!.data.author,
            bookDetail!!.list.size
        )
        var resultnow = Bookselect.selectbookdata(i, selectdata, data.pageindex - 1)
        if (resultnow == null){
            loadbookdatatopage(
                context,
                bookDetail,
                data.pageindex - 1,
                data,
                selectdata,
                start,
                end,
                true
            )
        }
        else{
            updatapre(context, data, selectclass, start, end)
        }
        i.close()
    }
    var i = MyDatabaseHelper(context, "bookstore", null, 1)
    var selectdata = Selectclass(
        bookDetail!!.data.name,
        bookDetail!!.data.author,
        bookDetail!!.list.size
    )
    var resultnow = Bookselect.selectbookdata(i, selectdata, data.pageindex + 1)
    if (data.pageindex != bookDetail!!.list.size - 1) {
        if (resultnow == null)
        {
            loadbookdatatopage(
                context,
                bookDetail,
                data.pageindex + 1,
                data,
                selectdata,
                start,
                end,
                false
            )
        }
        else{
            updatanext(context, data, selectdata, start, end)
        }
    }
    i.close()
    db.close()
}
fun updatapre(context: Context, data: BookIndexclass, selectclass: Selectclass, start:Int, end:Int){
    var db = MyDatabaseHelper(context, "bookstore", null, 1)
    var returnsultpre = Bookselect.selectbookread(db, selectclass, data.pageindex - 1)
    if (data.pageindex != 0) {
        if (returnsultpre == null) {
            var selectclass = Selectclass(
                data.bookname,
                data.author,
                data.pagecount
            )
            var returnsult = Bookselect.selectbookdata(db, selectclass, data.pageindex - 1)
            if (returnsult != null) {
                var list =
                    PagesizeUtil.txttolist(
                        returnsult.content,
                        context,
                        fontsize,
                        linesize
                    )
                var index = start - list.size
                var content = 0
                for (st in list) {
                    var readdata = BookReadclass(
                        data.author,
                        data.bookname,
                        data.pagecount,
                        st,
                        data.pageindex - 1,
                        content++,
                        start - list.size,
                        start,
                        index
                    )
                    //插入数据库
                    Bookinsert.insertbookread(db, readdata)
                    index++

                }
            }
        }
    }
    db.close()
}
fun updatanext(context: Context, data: BookIndexclass, selectclass: Selectclass, start:Int, end:Int){
    var db2 = MyDatabaseHelper(context, "bookstore", null, 1)
    var returnsultnext = Bookselect.selectbookread(db2, selectclass, data.pageindex + 1)
    if (returnsultnext == null) {
        var selectclass = Selectclass(
            data.bookname,
            data.author,
            data.pagecount
        )
        var returnsult = Bookselect.selectbookdata(db2, selectclass, data.pageindex + 1)
        if (returnsult != null) {
            var list =
                PagesizeUtil.txttolist(
                    returnsult.content,
                    context,
                    fontsize,
                    linesize
                )
            var index = end
            var content = 0
            for (st in list) {
                var readdata = BookReadclass(
                    data.author,
                    data.bookname,
                    data.pagecount,
                    st,
                    data.pageindex + 1,
                    content++,
                    end,
                    list.size + end,
                    index
                )
                //插入数据库
                Bookinsert.insertbookread(db2, readdata)
                index++

            }
        }
    }
    db2.close()
}