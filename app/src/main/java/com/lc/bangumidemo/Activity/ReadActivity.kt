package com.lc.bangumidemo.Activity

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.animation.Animation
import android.widget.ArrayAdapter

import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle

import com.lc.bangumidemo.Adapter.ScanViewAdapter
import com.lc.bangumidemo.Myreadview.ScanView


import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.RxBus.RxBusBaseMessage


import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindUntilEvent

import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.tesst.*

import android.view.animation.AnimationUtils
import android.widget.ListView
import com.lc.bangumidemo.KtUtil.*
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.NoveDatabase.BookIndexclass
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookselect
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookupdata
import com.lc.bangumidemo.Sqlite.NoveDatabase.MyDatabaseHelper
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdatahelper
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdataupdata
import io.reactivex.Observer


class ReadActivity :BaseActivity() {
    lateinit var buttonback:Animation
    lateinit var buttonshow:Animation
    lateinit var toorbarshow:Animation
    lateinit var toorbarback:Animation
    lateinit var leftmenushow:Animation
    lateinit var leftmenuback:Animation

    companion object {
        var ismenushow=false
        var islistshow=false
        }
    lateinit var disposable: Disposable

    override fun setRes(): Int {
        return R.layout.tesst
    }
    override fun initview() {
        super.initview()
        readtoolbar.isVisible=false
        buttonmenu.isVisible=false
        leftmenu.isVisible=false
        //隐藏设置栏
        setmenu.isVisible=false
        avi.show()


    }

    fun Rxrecive(code:Int){
        RxBus.getInstance()
            .tObservable(code, RxBusBaseMessage::class.java)
            .bindUntilEvent(this,Lifecycle.Event.ON_DESTROY)
            .subscribe(object : Observer<RxBusBaseMessage> {
                override fun onError(e: Throwable) {

                }

                override fun onNext(t: RxBusBaseMessage) {
                        if (t!!.code == 0) {
                            Log.e("RXJAVA", "初始化初始章节")
                            var indexres = Bookselect.selectbookindex(this@ReadActivity)!!
                            Mapinit(this@ReadActivity, indexres)
                        }
                        if (t!!.code == 1) {
                            Log.e("RXJAVA", "开始初始化前后章")
                            var indexres = Bookselect.selectbookindex(this@ReadActivity)!!
                            InitMapupdata(this@ReadActivity, indexres)
                        }
                        if (t!!.code == 2) {
                            Log.e("RXJAVA", "初始化完成进入视图")
                            initmyview()
                            avi.hide()
                        }
                        if (t!!.code == 3) {
                            Log.e("RXJAVA", "关闭左侧菜单")
                            leftmenu.startAnimation(leftmenuback)
                        }
                }
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }
            })
    }

    override fun initaction() {
        super.initaction()
        Rxrecive(0)//注册初始化订阅者
        Rxrecive(1)//注册初始化订阅者
        Rxrecive(2)//注册初始化订阅者
        Rxrecive(3)//注册listview订阅者

        //查询索引信息
        Bookselect.selectbookindex(this)
        //开始进行加载
        initloadbookdatatopage(this, bookDetail, hardpageindex)

        }

    fun initmyview() {
        //初始化动画
        avi.hide()
        lockscreen(false)
        buttonback = AnimationUtils.loadAnimation(this@ReadActivity, R.anim.buttonback)
        buttonshow= AnimationUtils.loadAnimation(this@ReadActivity, R.anim.buttonshow)
        toorbarshow=AnimationUtils.loadAnimation(this@ReadActivity, R.anim.toobaranim)
        toorbarback=AnimationUtils.loadAnimation(this@ReadActivity, R.anim.toobarbackanim)
        leftmenushow=AnimationUtils.loadAnimation(this@ReadActivity, R.anim.leftshow)
        leftmenuback=AnimationUtils.loadAnimation(this@ReadActivity, R.anim.leftclose)
        //动画监听
        leftmenuback.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                leftmenu.isVisible=false
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
                buttonmenu.isVisible=false
                readtoolbar.isVisible=false
                ismenushow=false
                closeothermenu()
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
        mulu.setOnClickListener {
            readtoolbar.isVisible=false
            buttonmenu.isVisible=false
            ismenushow=false

            //open list
            leftmenu.isVisible=true
            leftmenu.startAnimation(leftmenushow)
            islistshow=true
        }
        shezhi.setOnClickListener {
            setmenu.isVisible=true
        }

        list.setOnItemClickListener { parent, view, position, id ->
            lockscreen(true)
            var db =
                MyDatabaseHelper(this, "bookstore", null, 1)
            var updata = BookIndexclass(
                null,
                bookDetail!!.data.author,
                bookDetail!!.data.name,
                position,
                0,
                bookDetail!!.list.size,
                position,
                0
            )
            Bookupdata.updata(db, updata)
            //更新索引
            this.recreate()

        }

        var returnsult= Bookselect.selectbookindex(this)
        var adapt= returnsult?.let { ScanViewAdapter(this, it) }
        adapt?.let { pageview.setAdapter(it) }
        adapt?.setonPageclickListener(pageview,object :ScanView.OnpageClick{
            override fun onItemClick() {
                when(ismenushow){
                    true->{
                          buttonmenu.startAnimation(buttonback)
                          readtoolbar.startAnimation(toorbarback)

                    }
                    false->{
                        readtoolbar.isVisible=true
                        buttonmenu.isVisible=true
                        ismenushow=true
                        buttonmenu.startAnimation(buttonshow)
                        readtoolbar.startAnimation(toorbarshow)
                    }
                }
                when(islistshow){
                    true->{
                        leftmenu.isVisible=false
                        islistshow=false
                    }
                }

            }
        })

        //初始化阅读界面菜单
        setSupportActionBar(readtoolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        //初始化目录
        var templist:MutableList<String> = mutableListOf()
        for (temp in bookDetail!!.list)
        {
            templist.add(temp.num)
        }
        var arrayAdapter=ArrayAdapter<String>(this,R.layout.textview,templist)
        list.adapter=arrayAdapter
        //查询索引
        var sult= Bookselect.selectbookindex(this)
        list.choiceMode=ListView.CHOICE_MODE_SINGLE
        list.setSelected(true);
        list.setSelection(sult!!.hardpageindex)
        list.setItemChecked(sult!!.hardpageindex,true)
        tab.setTitle("   "+ bookDetail!!.data.name)
//        updatamenu()


    }
    private fun closeothermenu(){
        setmenu.isVisible=false
    }
    override fun initlistener() {
        super.initlistener()
        addfontsize.setOnClickListener {
            avi.show()
            lockscreen(true)
            fontsize += 1
            if (fontsize>28){ fontsize=28 }
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        cutfontsize.setOnClickListener {
            avi.show()
            lockscreen(true)
            fontsize -= 1
            if (fontsize<17){ fontsize=17}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        addlinesize.setOnClickListener {
            avi.show()
            lockscreen(true)
            linesize += 1
            if(linesize>21){linesize=21}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        cutlinesize.setOnClickListener {
            avi.show()
            lockscreen(true)
            linesize -= 1
            if(linesize<16){linesize=16}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        destoryandsave(this)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
                android.R.id.home ->  {
                    destoryandsave(this)
                    finish()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        //查询索引信息
        Bookselect.selectbookindex(this)
    }
}