package com.lc.bangumidemo.Activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView

import com.lc.bangumidemo.R
import com.lc.bangumidemo.Util.FragmentUtil
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.app_bar_base.*
import kotlinx.android.synthetic.main.mainlayout.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MainActivity :BaseActivity(){
    lateinit var icon:ImageView
    companion object{

        val PERMISSIONS_STORAGE = arrayOf(

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        //请求状态码

        val REQUEST_PERMISSION_CODE = 1


    }
    override fun setRes(): Int { return R.layout.mainlayout }

    override fun initview() {
        super.initview()
        // 在调用init方法前设置自定义更新对话框布局

        Bugly.init(getApplicationContext(), "23e079b718", false);
        icon=ImageView(this)
        icon.setImageResource(R.mipmap.menuleft)
        toolbar.addView(icon)
        toolbar.title=""
        setSupportActionBar(toolbar)
        navigationView.setCheckedItem(R.id.nav_gallery)
        navigationView.setItemIconTintList(null);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            var tempint=0
            for(i in permissions)
            {
                Log.i("MainActivity", "申请的权限为：" + i + ",申请结果：" + grantResults[tempint]);
                if(grantResults[tempint]==-1){
                    toast("未启用权限的部分功能将无法使用。").show()
                }
                tempint++
            }
         }
    }

    override fun initaction() {
        super.initaction()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE)
            }
        }

    }

    override fun initlistener() {
        super.initlistener()
        icon.setOnClickListener {drawer.openDrawer(GravityCompat.START)  }
        //左侧菜单监听

        navigationView.setNavigationItemSelectedListener { p0 ->
            when(p0.itemId) {
                R.id.nav_gallery->{}
            }
            true
        }
        //bottombar监听用于切换fragment
        bottomBar.setOnTabSelectListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, FragmentUtil.fragmentUtil.getFragment(it)!!,it.toString())
            transaction.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.download->{

            }
            R.id.shezhi->{
            startActivity<FlipReadActivity>()
            }
            R.id.clean->{

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menumain, menu)
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
                    if(drawer.isDrawerOpen(GravityCompat.START)&&Math.abs(movelengy)<30&&Math.abs(movelengx)<30&&Math.abs(speedy)<30&&Math.abs(speedx)<30){
                        drawer.closeDrawer(GravityCompat.START)
                    }
                }
            }
        }

            return super.dispatchTouchEvent(ev)
    }
    override fun onBackPressed() {
            super.onBackPressed()

    }

    override fun onRestart() {
        super.onRestart()
    }

}