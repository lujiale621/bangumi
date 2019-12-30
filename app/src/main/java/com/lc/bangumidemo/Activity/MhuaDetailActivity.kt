package com.lc.bangumidemo.Activity

import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.lc.bangumidemo.KtUtil.mhuaDetail
import com.lc.bangumidemo.KtUtil.stnull
import com.lc.bangumidemo.R
import com.lc.bangumidemo.MyRetrofit.api.HttpObserver
import com.lc.bangumidemo.MyRetrofit.api.ManhuaDetailResult
import com.lc.bangumidemo.MyRetrofit.api.ManhuaLoader
import com.lc.bangumidemo.Sqlite.CollectDatabase.*
import com.lc.bangumidemo.Sqlite.ManhuaDatabase.*
import kotlinx.android.synthetic.main.manhuadetail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

import java.lang.NullPointerException

class MhuaDetailActivity :BaseActivity(){
    override fun setRes(): Int { return R.layout.manhuadetail }
    override fun initview() {
        super.initview()
            lockscreen(true)
            setSupportActionBar(manhuatoolbar)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun initlistener() {
        super.initlistener()
        taggcell.setOnClickListener {
            mhuacell.toggle(false)
            simpletext.isVisible=false
        }
        startreadmanhua.setOnClickListener { startActivity<MhuaReadActivity>() }
        collectmhua.setOnClickListener {
            Toast.makeText(this,"已添加到收藏夹",Toast.LENGTH_LONG).show()
            var bundle = intent.extras
            val url = bundle!!.getString("url")
            if(url!=null) {
                var db = Collectdbhelper(this, "collect.db", null, 1)
                var selectdata = Collectdataclass(
                    mhuaDetail!!.data.name,
                    mhuaDetail!!.data.author,
                    mhuaDetail!!.list.size,
                    mhuaDetail!!.data.time,
                    "漫画",
                    mhuaDetail!!.data.cover,
                    url
                )
                var result = CollectdataSelect.selectcollectdata(db, selectdata)
                if (result == null) {
                    CollectdataInsert.insertcollectdata(db, selectdata)
                } else {
                    CollectdataUpdata.updata(db, selectdata)
                }
                db.close()
            }
        }
    }

    override fun initaction() {
        super.initaction()
        var bundle = intent.extras
        val urls = bundle!!.getString("url")
        var manhualoader= ManhuaLoader()
        if (urls != null) {
            manhualoader.getManhuaDetail(urls).subscribe(object : HttpObserver<ManhuaDetailResult>(){
                override fun onSuccess(t: ManhuaDetailResult?) {
                    loaddata(t!!)
                    loadlist(t!!)
                }

                override fun onError(code: Int, msg: String?) {
                    super.onError(code, msg)
                    lockscreen(false)
                }
            })
        }

    }

    private fun loadlist(data: ManhuaDetailResult) {
        var templist = mutableListOf<String>()
        for(i in data?.list)
        {
            templist.add(i.num)
        }
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.textviewfordetail, templist)
        gridview.adapter=adapter
        gridview.setOnItemClickListener { parent, view, position, id ->
           toast(data.list[position].num)
            //更新索引
            var db=Manhuadatahelper(this,"manhua.db",null,1)
            var insertmanhuaindex=ManhuaIndexclass(data.data.name,data.data.author,data.list.size,position,0)
            Manhuaupdata.updata(db,insertmanhuaindex)
            db.close()
            startActivity<MhuaReadActivity>()
        }

    }

    private fun loaddata(data: ManhuaDetailResult) {
        lockscreen(false)
        if (data == null) return

        //查询如果没有索引则添加一条索引
        var db=Manhuadatahelper(this,"manhua.db",null,1)
        var insertmanhuaindex=ManhuaIndexclass(data.data.name,data.data.author,data.list.size,0,0)
        var result=ManhuaSelect.selectManhuaIndex(db,insertmanhuaindex)
        if(result==null)
        {
            ManhuaInsert.insertindex(db,insertmanhuaindex)
        }
        db.close()

        mhuaDetail =data
        try {
            Glide.with(this).load(data.data.cover).into(manhuacoverm)
        } catch (e: NullPointerException) {
            Log.e("NullPointerException", e.toString())
        }
        try {
            mhuanamem.setText("漫画名称:" + stnull(data.data.name))
            mhuauthorm.setText("作者:" + stnull(data.data.author))
            mhualatestm.setText("最新章节:" + stnull(data.data.latest))
            mhuastatusm.setText("状态:" + stnull(data.data.status))
            mhuatimem.setText("更新时间:" + stnull(data.data.time))
            mhuatagm.setText("类型:" + stnull(data.data.tag))
            simpletext.setText("简介:" + stnull(data.data.introduce))
            mhualistsize.setText("共${data.list.size}章 >  ")
        }catch (e:Exception){
            Log.e("Exception", e.toString())
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}