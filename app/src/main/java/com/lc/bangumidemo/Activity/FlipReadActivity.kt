package com.lc.bangumidemo.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.lc.bangumidemo.KtUtil.*
import com.lc.bangumidemo.MyRetrofit.ResClass.BookResult
import com.lc.bangumidemo.MyRetrofit.ResClass.Bookdata
import com.lc.bangumidemo.MyRetrofit.Retrofit.Retrofitcall
import com.lc.bangumidemo.Myreadview.Constants
import com.lc.bangumidemo.Myreadview.LoadBitmapTask
import com.lc.bangumidemo.Myreadview.PageFlipView
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdataupdata
import kotlinx.android.synthetic.main.filpage.*
import kotlinx.android.synthetic.main.filpage.custombackground
import kotlinx.android.synthetic.main.tesst.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FlipReadActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    internal lateinit var mPageFlipView: PageFlipView
    internal lateinit var mGestureDetector: GestureDetector
    private var moveLenght: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    var clickableytop=0
    var clickableybutton=0
    var clickablexleft=0
    var clickablexright=0
    // 获取滑动速度
    private var vt: VelocityTracker? = null
    var novesourcelist: MutableList<Bookdata> = mutableListOf()//小说列表
    lateinit var buttonback: Animation
    lateinit var buttonshow: Animation
    lateinit var toorbarshow: Animation
    lateinit var toorbarback: Animation
    lateinit var leftmenushow: Animation
    lateinit var leftmenuback: Animation
    companion object {
        var islocalmenushow=false
        var islocallistshow=false
        val PICTURE = 10086 //requestcode
    }
    var customView: View? = null

    init {
        clickableytop = (screenheight/4)
        clickableybutton = screenheight-screenheight/4
        clickablexleft= screenwidth/4
        clickablexright=screenwidth-screenwidth/4
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPageFlipView = PageFlipView(this)
        setContentView(mPageFlipView)
        var params2:RelativeLayout.LayoutParams =  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        var params1: View? = layoutInflater.inflate(R.layout.filpage,null) as RelativeLayout
        var s= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params1!!.tag=s
        addContentView(params1,params2)

        mGestureDetector = GestureDetector(this, this)
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            mPageFlipView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        initview()
    }

    private fun initview() {
        if(backgroundcolor.equals("#413F3F"))
        {
            pencolor= Color.parseColor("#ffffff")
        }else{
            pencolor= Color.parseColor("#000000")
        }
        islocalmenushow=false
        islocallistshow=false
        localreadtoolbar.isVisible=false
        localbuttonmenu.isVisible=false
        localleftmenu.isVisible=false
        //隐藏设置栏
        localsetmenu.isVisible=false
        localsetliangdu.isVisible=false
        localmenufloat.isVisible=false
        localhuanyuanlayout.isVisible=false
        localdiag.isVisible=false
        localanmoread.hide()
        localavi.hide()
        setSupportActionBar(localreadtoolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.iconback)
        buttonback = AnimationUtils.loadAnimation(this, R.anim.buttonback)
        buttonshow= AnimationUtils.loadAnimation(this, R.anim.buttonshow)
        toorbarshow= AnimationUtils.loadAnimation(this, R.anim.toobaranim)
        toorbarback= AnimationUtils.loadAnimation(this, R.anim.toobarbackanim)
        leftmenushow= AnimationUtils.loadAnimation(this, R.anim.leftshow)
        leftmenuback= AnimationUtils.loadAnimation(this, R.anim.leftclose)
        //动画监听
        leftmenuback.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                localleftmenu.isVisible=false
                islocallistshow=false
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
                localbuttonmenu.isVisible=false
                localreadtoolbar.isVisible=false
                islocalmenushow=false
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
                localseakbar.isVisible=true
            }
        })
        localmulu.setOnClickListener {
            closeothermenu()
            localreadtoolbar.isVisible=false
            localbuttonmenu.isVisible=false
            islocalmenushow=false
            //open list
            localleftmenu.isVisible=true
            localleftmenu.startAnimation(leftmenushow)
            islocallistshow=true

        }
        localshezhi.setOnClickListener {
            closeothermenu()
            localsetmenu.isVisible=true
        }
        localliangdu.setOnClickListener {
            closeothermenu()
            localsetliangdu.isVisible=true
        }
        localhuanyuan.setOnClickListener {
            closeothermenu()
            localhuanyuanlayout.isVisible=true
            searchbook(localbookname)
        }

    }

    override fun onStart() {
        super.onStart()
        initlistener()
    }

    private fun closeothermenu() {
        localsetmenu.isVisible=false
        localsetliangdu.isVisible=false
        localseakbar.isVisible=false
        localhuanyuanlayout.isVisible=false
    }

    private fun showAbout() {
        val aboutView = layoutInflater.inflate(
            R.layout.about, null,
            false
        )

        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(R.string.app_name)
        builder.setView(aboutView)
        builder.create()
        builder.show()
    }

    override fun onDown(e: MotionEvent): Boolean {
        mPageFlipView.onFingerDown(e.x, e.y)
        return true
    }

    override fun onShowPress(e: MotionEvent) {

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        mPageFlipView.onFingerMove(e2.x, e2.y)
        return true
    }

    override fun onLongPress(e: MotionEvent) {

    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.optionmenus, menu)

        val duration = mPageFlipView.animateDuration
        if (duration == 1000) {
            menu.findItem(R.id.animation_1s).isChecked = true
        } else if (duration == 2000) {
            menu.findItem(R.id.animation_2s).isChecked = true
        } else if (duration == 5000){
            menu.findItem(R.id.animation_5s).isChecked = true
        }

        if (mPageFlipView.isAutoPageEnabled) {
            menu.findItem(R.id.auoto_page).isChecked = true
        } else {
            menu.findItem(R.id.single_page).isChecked = true
        }

        val pref = PreferenceManager
            .getDefaultSharedPreferences(this)
        val pixels = pref.getInt("MeshPixels", mPageFlipView.pixelsOfMesh)
        when (pixels) {
            2 -> menu.findItem(R.id.mesh_2p).isChecked = true
            5 -> menu.findItem(R.id.mesh_5p).isChecked = true
            10 -> menu.findItem(R.id.mesh_10p).isChecked = true
            20 -> menu.findItem(R.id.mesh_20p).isChecked = true
            else -> {
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isHandled = true
        val pref = PreferenceManager
            .getDefaultSharedPreferences(this)
        val editor = pref.edit()
        when (item.itemId) {
                android.R.id.home -> {
                    destoryandsave(this)
                    finish() }
            R.id.animation_1s -> {
                mPageFlipView.animateDuration = 1000
                editor.putInt(Constants.PREF_DURATION, 1000)
            }
            R.id.animation_2s -> {
                mPageFlipView.animateDuration = 2000
                editor.putInt(Constants.PREF_DURATION, 2000)
            }
            R.id.animation_5s -> {
                mPageFlipView.animateDuration = 5000
                editor.putInt(Constants.PREF_DURATION, 5000)
            }
            R.id.auoto_page -> {
                mPageFlipView.enableAutoPage(true)
                editor.putBoolean(Constants.PREF_PAGE_MODE, true)
            }
            R.id.single_page -> {
                mPageFlipView.enableAutoPage(false)
                editor.putBoolean(Constants.PREF_PAGE_MODE, false)
            }
            R.id.mesh_2p -> editor.putInt(Constants.PREF_MESH_PIXELS, 2)
            R.id.mesh_5p -> editor.putInt(Constants.PREF_MESH_PIXELS, 5)
            R.id.mesh_10p -> editor.putInt(Constants.PREF_MESH_PIXELS, 10)
            R.id.mesh_20p -> editor.putInt(Constants.PREF_MESH_PIXELS, 20)
            R.id.about_menu -> {
                showAbout()
                return true
            }
            else -> isHandled = false
        }

        if (isHandled) {
            item.isChecked = true
            editor.apply()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(!localbuttonmenu.isVisible) {
            return touchpageviewevent(ev)
        }else{
            return touchmenuviewevent(ev)
        }
    }


    fun touchmenuviewevent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            lastX = ev.x
            lastY = ev.y
            try {
                if (vt == null) {
                    vt = VelocityTracker.obtain()
                } else {
                    vt!!.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            vt!!.addMovement(ev)
        }
        if (ev.action == MotionEvent.ACTION_MOVE) {
            moveLenght = ev.x - lastX
        }
        if (ev.action == MotionEvent.ACTION_UP) {
            var lastX = ev.x
            var lastY = ev.y
            if ((lastX > clickablexleft && lastX < clickablexright &&lastY > clickableytop && lastY < clickableybutton&& Math.abs(moveLenght) < 30 && Math.abs(vt!!.xVelocity) < 30)) {
                onclickcenterscreen()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
    fun touchpageviewevent(ev: MotionEvent?): Boolean {
        when(islocallistshow){
            true-> {localleftmenu.startAnimation(leftmenuback)
                return mGestureDetector.onTouchEvent(ev)}
        }
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            lastX = ev.x
            try {
                if (vt == null) {
                    vt = VelocityTracker.obtain()
                } else {
                    vt!!.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            vt!!.addMovement(ev)
        }
        if (ev.action == MotionEvent.ACTION_MOVE) {
            moveLenght = ev.x - lastX
        }
        if (ev.action == MotionEvent.ACTION_UP) {
            var lastX = ev.x
            if ((lastX > 300 && lastX < 750 && Math.abs(moveLenght) < 30 && Math.abs(vt!!.xVelocity) < 30)) {
                onclickcenterscreen()
            } else {
                mPageFlipView.onFingerUp(ev.x, ev.y)
                return true

            }
        }
        return mGestureDetector.onTouchEvent(ev)
    }


    private fun initlistener() {
        localselectbackground.setOnClickListener {
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
        localaddfontsize.setOnClickListener {
            localavi.show()
            lockscreen(true)
            fontsize += 1
            if (fontsize>28){ fontsize=28 }
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localcutfontsize.setOnClickListener {
            localavi.show()
            lockscreen(true)
            fontsize -= 1
            if (fontsize<15){ fontsize=15}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localaddlinesize.setOnClickListener {
            localavi.show()
            lockscreen(true)
            linesize += 1
            if(linesize>21){linesize=21}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localcutlinesize.setOnClickListener {
            localavi.show()
            lockscreen(true)
            linesize -= 1
            if(linesize<12){linesize=12}
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localyejian.setOnClickListener {
            backgroundcolor="#413F3F"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        //设置背景颜色
        localcolwrite.setOnClickListener {
            backgroundcolor="#FFFFFF"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localcolpick.setOnClickListener {
            backgroundcolor="#FFCDD2"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localcolgreen.setOnClickListener {
            backgroundcolor="#C8E6C9"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localcolgreen_2.setOnClickListener {
            backgroundcolor="#DCEDC8"
            Userdataupdata.updatauserdata(this)
            this.recreate()
        }
        localcolyellew.setOnClickListener {
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
        }      //亮度监听
        localliangduseekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
             }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var window = this@FlipReadActivity.window
                var layoutParams = window.attributes;
                layoutParams.screenBrightness = progress / 255.0f
                window.attributes = layoutParams
            }
        })
        localhuanyuanlist.setOnItemClickListener { parent, view, position, id ->
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
    }
    private fun onclickcenterscreen() {
        when(islocalmenushow){
            true->{
                localbuttonmenu.startAnimation(buttonback)
                localreadtoolbar.startAnimation(toorbarback)

            }
            false->{
                localreadtoolbar.isVisible=true
                localbuttonmenu.isVisible=true
                islocalmenushow=true
                localbuttonmenu.startAnimation(buttonshow)
                localreadtoolbar.startAnimation(toorbarshow)
            }
        }
        when(islocallistshow){
            true->{
                localleftmenu.isVisible=false
                islocallistshow=false
            }
        }
      }
    private fun click(x:Int,y:Int) {
        val order = listOf("input",
            "tap",
            "" + x,
            "" + y)
        ProcessBuilder(order).start()
    }
    override fun onPause() {
        super.onPause()
        mPageFlipView.onPause()
        LoadBitmapTask.get(this).stop()
    }

    override fun onResume() {
        super.onResume()
        LoadBitmapTask.get(this).start()
        mPageFlipView.onResume()
    }

     fun lockscreen(islock:Boolean){
        if(islock) { this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) }
        else { this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) } }
    fun searchbook(name: String?) {
        localanmoread.show()
        novesourcelist.clear()
        val mHamdler1 = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    2 -> {
                        try {
                            var result = msg.obj as BookResult
                            if (result != null) {
                                localanmoread.hide()
                                if (result.list==null){throw NullPointerException()}
                                getbookdata(result)
                            }
                        } catch (e: Exception) {
                            localanmoread.hide()
                            Toast.makeText(this@FlipReadActivity,"搜索不到相关资源~", Toast.LENGTH_SHORT).show()
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
                        localanmoread.hide()
                        Toast.makeText(this@FlipReadActivity,"网络连接失败~", Toast.LENGTH_SHORT).show()
                        message.obj = null
                        message.what = 2
                        mHamdler1.sendMessage(message)
                    }
                })
            }
        }).start()
    }

    fun searchbooksource(){
        var tempsourcelist:MutableList<String> = mutableListOf<String>()
        var i=0
        for(ie in novesourcelist){
            tempsourcelist.add("书名:"+ie.name+" 新:"+ie.num+" 时间:"+ie.time)
            i++
        }
        localsourcesize.setText("已搜索到：$i"+"个类似书源")
        var sourceadapter= ArrayAdapter<String>(this,R.layout.sourcetextview, tempsourcelist)
        localhuanyuanlist.adapter=sourceadapter
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
                                Intent(this@FlipReadActivity, ErrorActivity::class.java)
                            intent.putExtra("msg", e.toString())
                            intent.putExtra("error","getbookdata")
                            intent.putExtra("tag", "FlipReadActivity")
                            localanmoread.hide()
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
}
