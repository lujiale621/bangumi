package com.lc.bangumidemo.Sqlite.CollectDatabase

/**
 *   val CREATE_COLLECT = ("create table CollectData ("
+ "id integer primary key autoincrement, "
+ "name text, "
+ "author text, "
+ "size integer, "
+ "updatatime text, "
+ "tag text, "
+ "cover text, "
+ "url text)"
)
 */
data class Collectdataclass (
    var name:String,
    var author:String,
    var size:Int,
    var updatatime:String,
    var tag:String,
    var cover:String,
    var url:String
)