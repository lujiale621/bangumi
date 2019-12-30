package com.lc.bangumidemo.Sqlite.NoveDatabase

/**
 *    val CREATE_BOOKDATA = ("create table BookData ("
+ "id integer primary key autoincrement, "
+ "author text, "
+ "bookname text, "
+ "pagecount integer, "
+ "pageindex interger, "
+ "contentindex interger)"
)
 */
data class BookDataclass (
    val author:String,
    val bookname:String,
    val content:String,
    val contentsize:Int,
    val pagecount:Int,
    val pageindex:Int

)