package com.lc.bangumidemo.Fragment

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.lc.bangumidemo.Activity.ErrorActivity
import com.lc.bangumidemo.Activity.MhuaDetailActivity
import com.lc.bangumidemo.Activity.MhuaReadActivity
import com.lc.bangumidemo.Activity.ReadActivity
import com.lc.bangumidemo.Adapter.Collectdatadapter
import com.lc.bangumidemo.KtUtil.bookDetail
import com.lc.bangumidemo.KtUtil.gotoread
import com.lc.bangumidemo.KtUtil.mhuaDetail
import com.lc.bangumidemo.MyRetrofit.ResClass.BookDetail
import com.lc.bangumidemo.MyRetrofit.Retrofit.Retrofitcall
import com.lc.bangumidemo.MyRetrofit.api.HttpObserver
import com.lc.bangumidemo.MyRetrofit.api.ManhuaDetailResult
import com.lc.bangumidemo.MyRetrofit.api.ManhuaLoader
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.CollectDatabase.CollectdataSelect.selectcollectalldata
import com.lc.bangumidemo.Sqlite.CollectDatabase.Collectdataclass
import kotlinx.android.synthetic.main.dymatfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import java.net.DatagramSocketImplFactory
import java.util.ArrayList
import java.util.stream.LongStream

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
                try {
                    gotoread(requestdata,mcontext,null)
                }catch (e:Exception){
                    lockscreen(false)
                    animcol.hide()
                    print(e.toString())
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