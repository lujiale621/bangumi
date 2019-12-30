package com.lc.bangumidemo.Activity

import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import com.lc.bangumidemo.Adapter.Manhuareadadapter
import com.lc.bangumidemo.KtUtil.mhuaDetail
import com.lc.bangumidemo.MyRetrofit.api.HttpObserver
import com.lc.bangumidemo.MyRetrofit.api.ManhuaContentResult
import com.lc.bangumidemo.MyRetrofit.api.ManhuaLoader

import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.RxBus.RxBusBaseMessage
import com.lc.bangumidemo.Sqlite.ManhuaDatabase.ManhuaIndexclass
import com.lc.bangumidemo.Sqlite.ManhuaDatabase.ManhuaSelect
import com.lc.bangumidemo.Sqlite.ManhuaDatabase.Manhuadatahelper
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindUntilEvent
import kotlinx.android.synthetic.main.manhuaread.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.ManhuaDatabase.Manhuaupdata
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import kotlinx.android.synthetic.main.bookindex.*

class MhuaReadActivity : BaseActivity() {
    val manhuareadlist= mutableListOf<String>()
    lateinit var buttonback: Animation
    lateinit var buttonshow: Animation
    lateinit var toorbarshow: Animation
    lateinit var toorbarback: Animation
    lateinit var leftmenushow: Animation
    lateinit var leftmenuback: Animation

    companion object {
        var ismenushow=false
        var islistshow=false
    }
    override fun setRes(): Int {
        return R.layout.manhuaread
    }
    fun Rxrecive(code:Int){
        RxBus.getInstance()
            .tObservable(code, RxBusBaseMessage::class.java)
            .bindUntilEvent(this, Lifecycle.Event.ON_DESTROY)
            .subscribe { t ->
                if(t!!.code==0){
                    Log.e("RXJAVA","加载")
                    searchmanhuacontent()
                }
            }
    }

    override fun initview() {
        super.initview()
        Rxrecive(0)//监听上拉刷新
        //初始化动画
        buttonback = AnimationUtils.loadAnimation(this, R.anim.buttonback)
        buttonshow= AnimationUtils.loadAnimation(this, R.anim.buttonshow)
        toorbarshow= AnimationUtils.loadAnimation(this, R.anim.toobaranim)
        toorbarback= AnimationUtils.loadAnimation(this, R.anim.toobarbackanim)
        leftmenushow= AnimationUtils.loadAnimation(this, R.anim.leftshow)
        leftmenuback= AnimationUtils.loadAnimation(this, R.anim.leftclose)
        //初始化目录
        var templist:MutableList<String> = mutableListOf()
        for (temp in mhuaDetail!!.list)
        {
            templist.add(temp.num)
        }
        var arrayAdapter= ArrayAdapter<String>(this,R.layout.textview,templist)
        manhualist.adapter=arrayAdapter
        //查询索引
        var sult= ManhuaSelect.selectmanhuaindex(this)
        manhualist.choiceMode= ListView.CHOICE_MODE_SINGLE
        manhualist.setSelected(true);
        manhualist.setSelection(sult!!.manhuaindex)
        manhualist.setItemChecked(sult!!.manhuaindex,true)
        manhuatab.setTitle("   "+ mhuaDetail!!.data.name)
        //动画监听
        leftmenuback.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                manhualeftmenu.isVisible=false
                islistshow=false
                lockscreen(false)
            }

            override fun onAnimationStart(animation: Animation?) {
                lockscreen(true)
            }
        })
        buttonback.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                manhuabuttonmenu.isVisible=false
                mhuareadtoolbar.isVisible=false
                ismenushow=false
                lockscreen(false)
            }

            override fun onAnimationStart(animation: Animation?) {
                lockscreen(true)
            }
        })
        buttonshow.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                lockscreen(false)
            }

            override fun onAnimationStart(animation: Animation?) {
                lockscreen(true)
            }
        })

        manhuamulu.setOnClickListener {
            mhuareadtoolbar.isVisible=false
            manhuabuttonmenu.isVisible=false
            ismenushow=false

            //open list
            manhualeftmenu.isVisible=true
            manhualeftmenu.startAnimation(leftmenushow)
            islistshow=true
        }

          var manhuareadadapter=Manhuareadadapter(manhuareadlist,this)
        manhuaview.layoutManager = LinearLayoutManager(this)
        manhuaview.adapter=manhuareadadapter
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        
        //菜单初始化
        mhuareadtoolbar.isVisible=false
        manhuabuttonmenu.isVisible=false
        manhualeftmenu.isVisible=false
    }

    override fun initlistener() {
        super.initlistener()
        refreshLayout.setOnLoadMoreListener {
            //更新索引
            var db=Manhuadatahelper(this,"manhua.db",null,1)
            var index=ManhuaSelect.selectmanhuaindex(this)
            if (index!=null) {
                var insertmanhuaindex = ManhuaIndexclass(
                    index.manhuaname,
                    index.manhuauthor,
                    index.manhualistsize,
                    index.manhuaindex+1,
                    0
                )
                Manhuaupdata.updata(db, insertmanhuaindex)
            }
            db.close()
            refreshLayout.finishLoadMore(true)
            RxBus.getInstance().send(0, RxBusBaseMessage(0,"加载"))
            true
        }
        manhualist.setOnItemClickListener { parent, view, position, id ->
            //更新索引
            var db=Manhuadatahelper(this,"manhua.db",null,1)
            var insertmanhuaindex=ManhuaIndexclass(mhuaDetail!!.data.name,mhuaDetail!!.data.author,mhuaDetail!!.list.size,position,0)
            Manhuaupdata.updata(db,insertmanhuaindex)
            db.close()
            manhuareadlist.clear()
            (manhuaview.adapter as Manhuareadadapter).notifyDataSetChanged()
            RxBus.getInstance().send(0, RxBusBaseMessage(0,"加载"))
        }
    }

    override fun initaction() {
        super.initaction()

    }

    override fun startaction() {
        super.startaction()
        searchmanhuacontent()
    }
    fun searchmanhuacontent()
    {
        var result=ManhuaSelect.selectmanhuaindex(this)
        var manhualoader=ManhuaLoader()
        manhualoader.getManhuaContent(mhuaDetail!!.list[result!!.manhuaindex].url).subscribe(object :HttpObserver<ManhuaContentResult>(){
            override fun onSuccess(t: ManhuaContentResult?) {
                print("success")
                for(i in t!!.list)
                {
                    manhuareadlist.add(i.img)
                }
                (manhuaview.adapter as Manhuareadadapter).notifyDataSetChanged()
            }

            override fun onError(code: Int, msg: String?) {
                super.onError(code, msg)
            }
        })
    }
    var oldy:Float = 0.0f
    var oldx:Float = 0.0f
    var movelengx=0.0f
    var movelengy=0.0f
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        var vt: VelocityTracker? = VelocityTracker.obtain()

        if (ev != null) {
            vt!!.addMovement(ev)
            var speedy=vt.yVelocity
            var speedx=vt.xVelocity
            when (ev.actionMasked)
            {
                MotionEvent.ACTION_DOWN->{
                    oldy= ev.y
                    oldx= ev.x
                }
                MotionEvent.ACTION_MOVE->{
                }
                MotionEvent.ACTION_UP -> {
                    movelengy=ev.y-oldy
                    movelengx=ev.x-oldx
                    if (ev.x > 300 && ev.x < 750&&Math.abs(movelengy)<30&&Math.abs(movelengx)<30&&Math.abs(speedy)<30&&Math.abs(speedx)<30&&!islistshow) {
                        onclick()
                    }
                    if(islistshow&&Math.abs(movelengy)<30&&Math.abs(movelengx)<30&&Math.abs(speedy)<30&&Math.abs(speedx)<30){
                        manhualeftmenu.startAnimation(leftmenuback)
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun onclick() {

            when(ismenushow){
                true->{
                    manhuabuttonmenu.startAnimation(buttonback)
                    mhuareadtoolbar.startAnimation(toorbarback)

                }
                false->{
                    mhuareadtoolbar.isVisible=true
                    manhuabuttonmenu.isVisible=true
                    ismenushow=true
                    manhuabuttonmenu.startAnimation(buttonshow)
                    mhuareadtoolbar.startAnimation(toorbarshow)
                }
            }
            when(islistshow){
                true->{
                    manhualeftmenu.isVisible=false
                    islistshow=false
                }
            }
        }
}