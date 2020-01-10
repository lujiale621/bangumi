package com.lc.bangumidemo.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.animation.Animation
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
import com.lc.bangumidemo.KtUtil.*
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdataupdata
import io.reactivex.Observer
import android.graphics.drawable.ColorDrawable
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.Menu
import android.view.MotionEvent
import android.widget.*
import androidx.core.app.ActivityCompat
import com.lc.bangumidemo.Adapter.Recadapt
import com.lc.bangumidemo.MyRetrofit.ResClass.BookResult
import com.lc.bangumidemo.MyRetrofit.ResClass.Bookdata
import com.lc.bangumidemo.MyRetrofit.Retrofit.Retrofitcall
import com.lc.bangumidemo.Sqlite.CollectDatabase.*
import com.lc.bangumidemo.Sqlite.NoveDatabase.*
import com.lc.bangumidemo.Util.FileUtils
import kotlinx.android.synthetic.main.search.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ReadActivity :BaseActivity()  , TextToSpeech.OnInitListener {
     var nowindex: BookIndexclass? = null
    var novesourcelist: MutableList<Bookdata> = mutableListOf()//小说列表
    lateinit var buttonback:Animation
    lateinit var buttonshow:Animation
    lateinit var toorbarshow:Animation
    lateinit var toorbarback:Animation
    lateinit var leftmenushow:Animation
    lateinit var leftmenuback:Animation
    var templist:MutableList<String> = mutableListOf()
    var templistdefult:MutableList<String> = mutableListOf()
     var adapt: ScanViewAdapter?=null
    lateinit var runnable:Runnable
    lateinit var handler:Handler
    var isautoread=false
    var isspeek=false

    companion object {
        var ismenushow=false
        var islistshow=false
        val PICTURE = 10086 //requestcode
        }
    lateinit var disposable: Disposable
    //tts语言
    lateinit var tts :TextToSpeech
    var mParams : HashMap<String,String> = HashMap()
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
        menufloat.isVisible=false
        huanyuanlayout.isVisible=false
        anmoread.hide()
        avi.show()
        //设置悬浮按钮
        menufloat.menuButtonColorNormal=Color.parseColor("#80DEEA")
        menufloat.menuButtonColorPressed=Color.parseColor("#80CBC4")
        menufloat.menuButtonColorRipple=Color.parseColor("#99FFFFFF")
        menu_hide.colorNormal=Color.parseColor("#80DEEA")
        menu_hide.colorPressed=Color.parseColor("#80CBC4")
        menu_hide.colorRipple=Color.parseColor("#99FFFFFF")
        menu_start.colorNormal=Color.parseColor("#80DEEA")
        menu_start.colorPressed=Color.parseColor("#80CBC4")
        menu_start.colorRipple=Color.parseColor("#99FFFFFF")
        menu_end.colorNormal=Color.parseColor("#80DEEA")
        menu_end.colorPressed=Color.parseColor("#80CBC4")
        menu_end.colorRipple=Color.parseColor("#99FFFFFF")

        if(backgroundcolor.equals("#413F3F"))
        {
            pencolor=Color.parseColor("#ffffff")
        }else{
            pencolor=Color.parseColor("#000000")
        }
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result = tts.setLanguage(Locale.CHINESE)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            else {
                //不支持中文就将语言设置为英文
                tts.setLanguage(Locale.US);
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

                @SuppressLint("LongLogTag")
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
                    if (t.code == 4) {
                        Log.e("RXJAVA", "更新索引")
                       nowindex = t.`object` as BookIndexclass
                        Log.i("bookindex-info-bookname", nowindex!!.bookname)
                        Log.i("bookindex-info-author", nowindex!!.author)
                        Log.i("bookindex-info-contentindex", nowindex!!.contentindex.toString())
                        Log.i("bookindex-info-hardcontentindex", nowindex!!.hardcontentindex.toString())
                        Log.i("bookindex-info-hardpageindex", nowindex!!.hardpageindex.toString())
                        list.choiceMode=ListView.CHOICE_MODE_SINGLE
                        list.setSelected(true);
                        list.setSelection(nowindex!!.pageindex)
                        list.setItemChecked(nowindex!!.pageindex,true)

                    }
                    if (t.code == 5) {
                        Log.e("RXJAVA", "播放语音")
                        var res=getnowbookreaddata()
                        if (res != null) {
                            tts.speak(res,TextToSpeech.QUEUE_FLUSH,null)
                        }

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
        //初始化语言模块
        tts=TextToSpeech(this,this)
        Rxrecive(0)//注册初始化订阅者
        Rxrecive(1)//注册初始化订阅者
        Rxrecive(2)//注册初始化订阅者
        Rxrecive(3)//注册listview订阅者
        Rxrecive(4)//注册索引订阅者
        Rxrecive(5)//注册语言播放
        //查询索引信息
        Bookselect.selectbookindex(this)

        destoryandsave(this)
        //
        //开始进行加载
        initloadbookdatatopage(this, bookDetail, hardpageindex)
        handler=Handler()
            runnable = Runnable{
                if(ismenushow){
                    buttonmenu.startAnimation(buttonback)
                    readtoolbar.startAnimation(toorbarback)
                }
                if (isautoread) {
                    click(1000, 500)
                    lockscreen(false)
                    handler.postDelayed(runnable, 10000)
                }
                if (isspeek){
                    lockscreen(false)
                            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                                override fun onStart(utteranceId: String?) {
                                print("onStart")
                                }

                                override fun onStop(utteranceId: String?, interrupted: Boolean) {
                                    super.onStop(utteranceId, interrupted)
                                    print("onStop")
                                }

                                override fun onDone(utteranceId: String?) {
                                    print("onDone")
                                    click(1000, 100)
                                    RxBus.getInstance().send(5, RxBusBaseMessage(5,"null"))
                                }

                                override fun onError(utteranceId: String?) {
                                    Log.e("语音播放失败","utteranceId")
                                }
                            })
                             mParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID")
                             RxBus.getInstance().send(5, RxBusBaseMessage(5,"null"))
                }
        }
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
            closeothermenu()
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
        list.setOnItemClickListener { v1, v2, position, v3 ->
            lockscreen(true)
            var isdaoxu=false
            //检查是否倒序
            if(v2 is TextView){
                Log.i("text:",v2.text.toString())
                if(!v2.text.toString().equals(templistdefult[position])){
                    isdaoxu=true
                }
            }
            var db = MyDatabaseHelper(this, "bookstore", null, 1)
            var temposition=0
            if(!isdaoxu){
                temposition=position
            }else{
                temposition=bookDetail!!.list.size-position-1
            }
            var updata = BookIndexclass(
                null,
                bookDetail!!.data.author,
                bookDetail!!.data.name,
                temposition,
                0,
                bookDetail!!.list.size,
                temposition,
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
                if(isautoread||isspeek){
                    menufloat.isVisible=true
                }
            }


        })

        //初始化阅读界面菜单
        readtoolbar.overflowIcon= this.getDrawable(R.drawable.menulet)
        setSupportActionBar(readtoolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.iconback)

        //初始化目录
        for (temp in bookDetail!!.list)
        {
            templist.add(temp.num)
            templistdefult.add(temp.num)
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
        //初始化书源

    }
    fun searchbooksource(){
        var tempsourcelist:MutableList<String> = mutableListOf<String>()
        var i=0
        for(ie in novesourcelist){
                tempsourcelist.add("书名:"+ie.name+" 新:"+ie.num+" 时间:"+ie.time)
                i++
        }
        sourcesize.setText("已搜索到：$i"+"个类似书源")
        var sourceadapter=ArrayAdapter<String>(this,R.layout.sourcetextview, tempsourcelist)
        huanyuanlist.adapter=sourceadapter
    }
    fun searchbook(name: String?) {
        anmoread.show()
        novesourcelist.clear()
        val mHamdler1 = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    2 -> {
                        try {
                            var result = msg.obj as BookResult
                            if (result != null) {
                                anmoread.hide()
                                if (result.list==null){throw NullPointerException()}
                                getbookdata(result)
                            }
                        } catch (e: Exception) {
                            anmoread.hide()
                            Toast.makeText(this@ReadActivity,"搜索不到相关资源~",Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
        Thread(Runnable {
            var message = Message()
            val call = name?.let { Retrofitcall().getAPIService().getCall(it) }
            if (call != null) {
                call.enqueue(object : Callback<BookResult> {
                    override fun onResponse(
                        call: Call<BookResult>,
                        response: Response<BookResult>
                    ) {
                        val st = response.body()
                        println(st)
                        message.obj = st
                        message.what = 2
                        mHamdler1.sendMessage(message)
                    }

                    override fun onFailure(call: Call<BookResult>, t: Throwable) {
                        anmoread.hide()
                        Toast.makeText(this@ReadActivity,"网络连接失败~",Toast.LENGTH_SHORT).show()
                        message.obj = null
                        message.what = 2
                        mHamdler1.sendMessage(message)
                    }
                })
            }
        }).start()
    }

    fun getbookdata(result: BookResult) {

        val hand = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {

                    3 -> {
                        try {
                            for (i in result!!.list) {
                                novesourcelist.add(i)
                            }
                            searchbooksource()
                          } catch (e: Exception) {
                            var intent =
                            Intent(this@ReadActivity, ErrorActivity::class.java)
                            intent.putExtra("msg", e.toString())
                            intent.putExtra("error","getbookdata")
                            intent.putExtra("tag", "ReadActivity")
                            anmoread.hide()
                            startActivity(intent)
                        }

                    }
                }
            }
        }
        Thread(Runnable {
            hand.sendEmptyMessage(3)
        }).start()
    }
    private fun closeothermenu(){
        setmenu.isVisible=false
        setliangdu.isVisible=false
        seakbar.isVisible=false
        huanyuanlayout.isVisible=false
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity.REQUEST_PERMISSION_CODE) {
            var tempint=0
            for(i in permissions)
            {
                Log.i("ReadActivity", "申请的权限为：" + i + ",申请结果：" + grantResults[tempint]);
                if(grantResults[tempint]==-1){
                    toast("未启用权限的部分功能将无法使用。").show()
                }
                tempint++
            }
        }
    }
    override fun initlistener() {
        super.initlistener()
        huanyuanlist.setOnItemClickListener { parent, view, position, id ->
            var urls = novesourcelist[position].url
            if (urls != null) {
                var start = Intent(this, BookDetailActivity::class.java)
                var bundle = Bundle()
                bundle.putString("url", urls)
                bundle.putInt("position", position)
                start.putExtras(bundle)
                this.finish()
                startActivity(start)
            }
        }
        huanyuan.setOnClickListener {
            closeothermenu()
            huanyuanlayout.isVisible=true
            searchbook(bookDetail!!.data.name)
        }
        menu_hide.setOnClickListener {
            menufloat.isVisible=false
        }
        menu_start.setOnClickListener {
            lockscreen(true)
            menufloat.alpha=0.3f
            menufloat.close(true)
            handler.postDelayed(runnable,0)
        }
        menu_end.setOnClickListener { handler.removeCallbacks(runnable)
            isautoread=false
            isspeek=false
            tts.stop()
            menufloat.alpha=1f
            menufloat.close(true)
            menufloat.isVisible=false
        }
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
            //检查是否申请权限
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        MainActivity.PERMISSIONS_STORAGE,
                        MainActivity.REQUEST_PERMISSION_CODE
                    )
                }else{
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
            }


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
            if (fontsize<15){ fontsize=15}
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
            if(linesize<12){linesize=12}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        yejian.setOnClickListener {
            backgroundcolor="#413F3F"
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
            if (!userbackground.equals("null")) {
                backgroundcolor = userbackground
                Userdataupdata.updatauserdata(this)
                this.recreate()
            }
        }
    }

    fun getnowbookreaddata(): String? {
        //查询索引信息
        var resultant:BookReadclass
         if(nowindex!=null){
            var i = MyDatabaseHelper(this, "bookstore", null, 1)
            //语音
             i= MyDatabaseHelper(this, "bookstore", null, 1)
             var selectclass=Selectclass(bookDetail!!.data.name, bookDetail!!.data.author, bookDetail!!.list.size)
             var rest=Bookselect.selectbookdata(i,selectclass, nowindex!!.pageindex)
             var list = PagesizeUtil.txttolist(
                 rest!!.content,
                 this,
                 fontsize,
                 linesize
             )
             i.close()
             var temp =nowindex!!.contentindex
             if(temp>=(list.size-1)){temp=list.size-1}
             if(temp<=0){temp=0}
             return list[temp]
        }
        return null
    }
    private fun click(x:Int,y:Int) {
        val order = listOf("input",
            "tap",
            "" + x,
            "" + y)
        ProcessBuilder(order).start()
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
                R.id.fanye->{
                    if(!isspeek)
                    {
                        isautoread=true
                        menufloat.isVisible=true
                    }else{
                        isspeek=false
                        isautoread=true
                        menufloat.isVisible=true
                    }

                    }
                R.id.downloadbook ->{}
                R.id.listenbook ->{
                    if(!isautoread) {
                        isspeek = true
                        menufloat.isVisible = true
                    }else{
                        isautoread=false
                        isspeek = true
                        menufloat.isVisible = true

                    }
                }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.readmenu, menu)
        if (menu != null) {
            if (menu.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
                try {
                    val method = menu.javaClass.getDeclaredMethod(
                        "setOptionalIconsVisible",
                        java.lang.Boolean.TYPE
                    )
                    method.isAccessible = true
                    method.invoke(menu, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRestart() {
        super.onRestart()
        //查询索引信息
        Bookselect.selectbookindex(this)
    }



}