package com.lc.bangumidemo.Activity

import android.app.Activity
import android.graphics.Color
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
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdataupdata
import io.reactivex.Observer
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import android.widget.SeekBar
import com.lc.bangumidemo.Sqlite.CollectDatabase.*
import com.lc.bangumidemo.Util.FileUtils
import org.jetbrains.anko.toast
import java.util.*

class ReadActivity :BaseActivity() {
    lateinit var buttonback:Animation
    lateinit var buttonshow:Animation
    lateinit var toorbarshow:Animation
    lateinit var toorbarback:Animation
    lateinit var leftmenushow:Animation
    lateinit var leftmenuback:Animation
    var templist:MutableList<String> = mutableListOf()
     var adapt: ScanViewAdapter?=null
    companion object {
        var ismenushow=false
        var islistshow=false
        val PICTURE = 10086 //requestcode
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
        setliangdu.isVisible=false
        avi.show()
        val mydrawable : Drawable = if (backgroundcolor[0] == '#'){
            ColorDrawable(Color.parseColor(backgroundcolor))
        }else {
            Drawable.createFromPath(backgroundcolor)!!
        }
        mydrawable.setBounds(0,0, screenwidth, screenheight)
        bgcolor.setImageDrawable(mydrawable)

        //初始化进度条
        var sult = Bookselect.selectbookindex(this@ReadActivity)
        if (sult!=null) {
            var temp=((sult.pageindex + 1).toFloat()/sult.pagecount.toFloat())*100
            if(temp>=100)
            {seekbar.progress=100}
            else
            {
                seekbar.progress= temp.toInt()
            }
        }
    }

    fun Rxrecive(code:Int){
        RxBus.getInstance()
            .tObservable(code, RxBusBaseMessage::class.java)
            .bindUntilEvent(this,Lifecycle.Event.ON_DESTROY)
            .subscribe(object : Observer<RxBusBaseMessage> {
                override fun onError(e: Throwable) {

                }

                override fun onNext(t: RxBusBaseMessage) {
                        if (t.code == 0) {
                            Log.e("RXJAVA", "初始化初始章节")
                            var indexres = Bookselect.selectbookindex(this@ReadActivity)!!
                            Mapinit(this@ReadActivity, indexres)
                        }
                        if (t.code == 1) {
                            Log.e("RXJAVA", "开始初始化前后章")
                            var indexres = Bookselect.selectbookindex(this@ReadActivity)!!
                            InitMapupdata(this@ReadActivity, indexres)
                        }
                        if (t.code == 2) {
                            Log.e("RXJAVA", "初始化完成进入视图")
                            initmyview()
                            avi.hide()
                        }
                        if (t.code == 3) {
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
                closeothermenu()
                seakbar.isVisible=true
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
            closeothermenu()
            setmenu.isVisible=true
        }
        liangdu.setOnClickListener {
            closeothermenu()
            setliangdu.isVisible=true
        }
        list.setOnItemClickListener { _, _, position, _ ->
            lockscreen(true)
            var db = MyDatabaseHelper(this, "bookstore", null, 1)
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
        adapt= returnsult?.let { ScanViewAdapter(this, it) }
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
        list.setItemChecked(sult.hardpageindex,true)
        tab.setTitle("   "+ bookDetail!!.data.name)



    }
    private fun closeothermenu(){
        setmenu.isVisible=false
        setliangdu.isVisible=false
        seakbar.isVisible=false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (data == null) { return }
        val uri = data.data
        when (requestCode) {
            PICTURE -> {
                var imagepath =
                    FileUtils.getUriPath(this, uri) //（因为4.4以后图片uri发生了变化）通过文件工具类 对uri进行解析得到图片路径
                userbackground = imagepath
                backgroundcolor = userbackground
                Userdataupdata.updatauserdata(this)
                this.recreate()
            }
        }
    }

    override fun initlistener() {
        super.initlistener()
        collike.setOnClickListener {
            if(bookDetail!!.data.url!=null) {
                var db = Collectdbhelper(this, "collect.db", null, 1)
                var selectdata = Collectdataclass(
                    bookDetail!!.data.name,
                    bookDetail!!.data.author,
                    bookDetail!!.list.size,
                    bookDetail!!.data.time,
                    "小说",
                    bookDetail!!.data.cover,
                    bookDetail!!.data.url
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
        //倒序监听
        dore.setOnClickListener {
            templist.reverse()
            (list.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        }
        //刷新监听
        fleshbutton.setOnClickListener {  }
        //亮度监听
        liangduseekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
             var window = this@ReadActivity.window
             var layoutParams = window.attributes;
                layoutParams.screenBrightness = progress/255.0f
                window.attributes = layoutParams
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                   }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                 }
        })
        selectbackground.setOnClickListener {
            val intent = Intent()
            if (Build.VERSION.SDK_INT < 19) {//因为Android SDK在4.4版本后图片action变化了 所以在这里先判断一下
                intent.action = Intent.ACTION_GET_CONTENT
            } else {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
            }
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, PICTURE)
        }
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
        //设置背景颜色
        colwrite.setOnClickListener {
            backgroundcolor="#FFFFFF"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        colpick.setOnClickListener {
            backgroundcolor="#FFCDD2"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        colgreen.setOnClickListener {
            backgroundcolor="#C8E6C9"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        colgreen_2.setOnClickListener {
            backgroundcolor="#DCEDC8"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        colyellew.setOnClickListener {
            backgroundcolor="#F0F4C3"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        custombackground.setOnClickListener {
            backgroundcolor= userbackground
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