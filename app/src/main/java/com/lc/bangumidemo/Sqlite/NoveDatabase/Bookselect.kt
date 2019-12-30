package com.lc.bangumidemo.Sqlite.NoveDatabase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lc.bangumidemo.Activity.ErrorActivity
import com.lc.bangumidemo.KtUtil.bookDetail
import com.lc.bangumidemo.KtUtil.hardcontentindex
import com.lc.bangumidemo.KtUtil.hardpageindex

object Bookselect {
    fun selectbookindex(context: Context): BookIndexclass? {
        //查询索引信息
        try {
            var db= MyDatabaseHelper(
                context,
                "bookstore",
                null,
                1
            )
            var selectindex = Selectclass(
                bookDetail!!.data.name,
                bookDetail!!.data.author,
                bookDetail!!.list.size
            )
            var returnsult=
                selectindex(db, selectindex)
            hardcontentindex = returnsult!!.hardcontentindex
            hardpageindex =returnsult!!.hardpageindex
            db.close()
            return returnsult
        }catch (e:Exception){
            var intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra("msg","查询不到索引")
            intent.putExtra("tag","Bookselectindex")
            context.startActivity(intent)
        }
        return null
    }
    fun selectindex(dbhelper: MyDatabaseHelper, data: Selectclass): BookIndexclass? {
        val db = dbhelper.writableDatabase
        val cursor = db.query("BookIndex", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                val hardpageindex = cursor.getInt(cursor.getColumnIndex("hardpageindex"))
                val hardcontentindex = cursor.getInt(cursor.getColumnIndex("hardcontentindex"))
                val pagecount = cursor.getInt(cursor.getColumnIndex("pagecount"))
                val pageindex = cursor.getInt(cursor.getColumnIndex("pageindex"))
                val contentindex = cursor.getInt(cursor.getColumnIndex("contentindex"))
                if (data.author == author && data.bookname == bookname ) {
                    return BookIndexclass(
                        id,
                        author,
                        bookname,
                        hardpageindex,
                        hardcontentindex,
                        pagecount,
                        pageindex,
                        contentindex
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return null
    }

    fun selectbookdata(
        dbhelper: MyDatabaseHelper,
        data: Selectclass,
        position: Int
    ): BookDataclass? {
        val db = dbhelper.writableDatabase
        val cursor = db.query("BookData", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val contentsize = cursor.getInt(cursor.getColumnIndex("contentsize"))
                val pagecount = cursor.getInt(cursor.getColumnIndex("pagecount"))
                val pageindex = cursor.getInt(cursor.getColumnIndex("pageindex"))
                if (data.author == author && data.bookname == bookname &&pageindex == position) {
                    return BookDataclass(
                        author,
                        bookname,
                        content,
                        contentsize,
                        pagecount,
                        pageindex
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return null
    }

    /**
     * val CREATE_BOOKREAD = ("create table BookRead ("
     * + "id integer primary key autoincrement, "
     * + "author text, "
     * + "bookname text, "
     * + "pagecount integer, "
     * + "data text, "
     * + "pageindex interger, "
     * + "contentindex interger, "
     * + "start interger, "
     * + "end interger, "
     * + "index interger)"
     * )
     * @param dbhelper
     * @param data
     * @param position
     * @return
     */
    fun selectbookread(
        dbhelper: MyDatabaseHelper,
        data: Selectclass,
        position: Int
    ): BookReadclass? {
        val db = dbhelper.writableDatabase
        val cursor = db.query("BookRead", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                val pagecount = cursor.getInt(cursor.getColumnIndex("pagecount"))
                val bookdata = cursor.getString(cursor.getColumnIndex("bookdata"))
                val pageindex = cursor.getInt(cursor.getColumnIndex("pageindex"))
                val contentindex = cursor.getInt(cursor.getColumnIndex("contentindex"))
                val start = cursor.getInt(cursor.getColumnIndex("start"))
                val end = cursor.getInt(cursor.getColumnIndex("end"))
                val index = cursor.getInt(cursor.getColumnIndex("indexx"))
                if (data.author == author && data.bookname == bookname && pageindex == position) {
                    return BookReadclass(
                        author,
                        bookname,
                        pagecount,
                        bookdata,
                        pageindex,
                        contentindex,
                        start,
                        end,
                        index
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return null
    }

    fun selectbookreaddata(
        dbhelper: MyDatabaseHelper,
        data: Selectclass,
        conindex: Int
    ): BookReadclass? {
        val db = dbhelper.writableDatabase
        val cursor = db.query("BookRead", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                val pagecount = cursor.getInt(cursor.getColumnIndex("pagecount"))
                val datatxt = cursor.getString(cursor.getColumnIndex("bookdata"))
                val pageindex = cursor.getInt(cursor.getColumnIndex("pageindex"))
                val contentindex = cursor.getInt(cursor.getColumnIndex("contentindex"))
                val start = cursor.getInt(cursor.getColumnIndex("start"))
                val end = cursor.getInt(cursor.getColumnIndex("end"))
                val index = cursor.getInt(cursor.getColumnIndex("indexx"))
                if (data.author == author && data.bookname == bookname  && index == conindex) {
                    return BookReadclass(
                        author,
                        bookname,
                        pagecount,
                        datatxt,
                        pageindex,
                        contentindex,
                        start,
                        end,
                        index
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return null
    }

    @SuppressLint("LongLogTag")
    fun selectalldata(dbhelper: MyDatabaseHelper) {
        var i = 0
        var j = 0
        var k = 0
        val db = dbhelper.writableDatabase
        val cursor = db.query("BookData", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val contentsize = cursor.getInt(cursor.getColumnIndex("contentsize"))
                val pagecount = cursor.getInt(cursor.getColumnIndex("pagecount"))
                val pageindex = cursor.getInt(cursor.getColumnIndex("pageindex"))
                j++
                try {
                    Log.i("BookData:author", author)
                    Log.i("BookData:bookname", bookname)
                    Log.i("BookData:content", content)
                    Log.i("BookData:contentsize", contentsize.toString())
                    Log.i("BookData:pagecount", pagecount.toString())
                    Log.i("BookData:pageindex", pageindex.toString())
                } catch (e: Exception) {
                    println(e)
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        val db3 = dbhelper.writableDatabase
        val cursor3 = db3.query("BookRead", null, null, null, null, null, null)
        if (cursor3.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印

                val author = cursor3.getString(cursor3.getColumnIndex("author"))
                val bookname = cursor3.getString(cursor3.getColumnIndex("bookname"))
                val pagecount = cursor3.getInt(cursor3.getColumnIndex("pagecount"))
                val datatxt = cursor3.getString(cursor3.getColumnIndex("bookdata"))
                val pageindex = cursor3.getInt(cursor3.getColumnIndex("pageindex"))
                val contentindex = cursor3.getInt(cursor3.getColumnIndex("contentindex"))
                val start = cursor3.getInt(cursor3.getColumnIndex("start"))
                val end = cursor3.getInt(cursor3.getColumnIndex("end"))
                val index = cursor3.getInt(cursor3.getColumnIndex("indexx"))
                k++
                try {
                    Log.i("BookRead:author", author)
                    Log.i("BookRead:bookname", bookname)
                    Log.i("BookRead:datatxt", datatxt)
                    Log.i("BookRead:pagecount", pagecount.toString())
                    Log.i("BookRead:pageindex", pageindex.toString())
                    Log.i("BookRead:contentindex", contentindex.toString())
                    Log.i("BookRead:start", start.toString())
                    Log.i("BookRead:end", end.toString())
                    Log.i("BookRead:index", index.toString())
                } catch (e: Exception) {
                    println(e)
                }

            } while (cursor3.moveToNext())
        }
        cursor3.close()
        val db2 = dbhelper.writableDatabase
        val cursor2 = db2.query("BookIndex", null, null, null, null, null, null)
        if (cursor2.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor2.getString(cursor2.getColumnIndex("author"))
                val bookname = cursor2.getString(cursor2.getColumnIndex("bookname"))
                val hardpageindex = cursor2.getInt(cursor2.getColumnIndex("hardpageindex"))
                val hardcontentindex = cursor2.getInt(cursor2.getColumnIndex("hardcontentindex"))
                val pagecount = cursor2.getInt(cursor2.getColumnIndex("pagecount"))
                val pageindex = cursor2.getInt(cursor2.getColumnIndex("pageindex"))
                val contentindex = cursor2.getInt(cursor2.getColumnIndex("contentindex"))
                i++
                try {
                    Log.i("BookIndex:author", author)
                    Log.i("BookIndex:bookname", bookname)
                    Log.i("BookIndex:hardpageindex", hardpageindex.toString())
                    Log.i("BookIndex:hardcontentindex", hardcontentindex.toString())
                    Log.i("BookIndex:pagecount", pagecount.toString())
                    Log.i("BookIndex:pageindex", pageindex.toString())
                    Log.i("BookIndex:contentindex", contentindex.toString())


                } catch (e: Exception) {
                    println(e)
                }

            } while (cursor2.moveToNext())
        }
        cursor2.close()
        Log.i("BookData:indexsize=", j.toString())
        Log.i("BookIndex:indexsize=", i.toString())
        Log.i("BookRead:indexsize=", k.toString())
    }


}
