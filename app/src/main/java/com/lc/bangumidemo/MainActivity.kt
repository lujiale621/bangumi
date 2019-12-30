package com.lc.bangumidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lc.bangumidemo.MyRetrofit.api.DetailResult
import com.lc.bangumidemo.MyRetrofit.api.HttpObserver
import com.lc.bangumidemo.MyRetrofit.api.MovieLoader
import com.lc.bangumidemo.MyRetrofit.api.NovelLoader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var ysname = "刀剑神域"
        var ysurl = "605vod-detail-id-22816.html"

        var xsname = "灵气逼人"
        var xsurl = "qbxshttps://www.x23qb.com/book/12893/"
        val movieLoader = MovieLoader()
        val novelLoader = NovelLoader()

        novelLoader.getNovelDetail(xsurl).subscribe(object : HttpObserver<DetailResult>() {
            override fun onSuccess(t: DetailResult?) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Throwable) {
                Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}
