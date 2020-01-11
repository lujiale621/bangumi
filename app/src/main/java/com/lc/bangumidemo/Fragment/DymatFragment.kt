package com.lc.bangumidemo.Fragment

import android.content.Context

import com.lc.bangumidemo.Adapter.Collectdatadapter

import com.lc.bangumidemo.KtUtil.gotoread

import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.CollectDatabase.CollectdataSelect.selectcollectalldata
import com.lc.bangumidemo.Sqlite.CollectDatabase.Collectdataclass
import kotlinx.android.synthetic.main.dymatfragment.*

import java.util.ArrayList
import androidx.core.os.HandlerCompat.postDelayed

import android.os.Handler
import com.lc.bangumidemo.Activity.FlipReadActivity
import com.lc.bangumidemo.KtUtil.localbookname
import org.jetbrains.anko.startActivity


class DymatFragment :BaseFragment() {
    lateinit var mcontext: Context
    override fun setRes(): Int {
        return R.layout.dymatfragment
    }

    override fun initview() {
        super.initview()
        lockscreen(true)
        mcontext= this!!.context!!
    }

    override fun initlistener() {
        super.initlistener()
    }

    override fun startaction() {
        super.startaction()
        animcol.hide()
        var datalist=selectcollectalldata(mcontext)
        var collectadapter=Collectdatadapter(mcontext, datalist as ArrayList<Collectdataclass>)
        collectadapter.setOnCollectClicklistener(object: Collectdatadapter.onCollectClicklistener {
            override fun onItemClick(requestdata: Collectdataclass) {
                lockscreen(true)
                animcol.show()
                if (requestdata.url.equals("null")){
                    localbookname=requestdata.name
                    mcontext.startActivity<FlipReadActivity>()
                }else{
                gotoread(requestdata,mcontext,null)
                }
            }
        })
        dymgridview.adapter=collectadapter
        (dymgridview.adapter as Collectdatadapter).notifyDataSetChanged()
        lockscreen(false)
    }

    override fun onStart() {
        super.onStart()
    }
}