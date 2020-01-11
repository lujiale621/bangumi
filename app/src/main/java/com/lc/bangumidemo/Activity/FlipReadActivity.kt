package com.lc.bangumidemo.Activity

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager

import com.lc.bangumidemo.Myreadview.Constants
import com.lc.bangumidemo.Myreadview.LoadBitmapTask
import com.lc.bangumidemo.Myreadview.PageFlipView
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.filpage.*
import org.jetbrains.anko.contentView


class FlipReadActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    internal lateinit var mPageFlipView: PageFlipView
    internal lateinit var mGestureDetector: GestureDetector
    var customView: View? = null

    init {

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
//        localavi.show()

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
        } else if (duration == 5000) {
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
    private var moveLenght: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    // 获取滑动速度
    private var vt: VelocityTracker? = null

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev!!.action==MotionEvent.ACTION_DOWN){
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
        if(ev.action==MotionEvent.ACTION_MOVE){
            moveLenght = ev.x - lastX
        }
        if (ev.action == MotionEvent.ACTION_UP ) {
            var lastX = ev.x
            if((lastX > 300 && lastX < 750 && Math.abs(moveLenght) < 30&&Math.abs(vt!!.xVelocity)<30))
            {
                onclickcenterscreen()
            }else{
                mPageFlipView.onFingerUp(ev.x, ev.y)
                return true

            }
        }
        return mGestureDetector.onTouchEvent(ev)
    }


    private fun onclickcenterscreen() {

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
}
