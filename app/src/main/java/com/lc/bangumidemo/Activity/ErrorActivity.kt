package com.lc.bangumidemo.Activity

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import com.lc.bangumidemo.KtUtil.destoryandsave
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookreadclean
import kotlinx.android.synthetic.main.error.*
import org.jetbrains.anko.toast

class ErrorActivity : BaseActivity() {
    val TAG="ErrorActivity"
    override fun setRes(): Int {
        return R.layout.error
    }

    override fun initview(){
        super.initview()
        Log.e(TAG,intent.getStringExtra("error"))
        Log.e(TAG,intent.getStringExtra("msg"))
        Log.e(TAG,intent.getStringExtra("tag"))
    }

    override fun startaction() {
        super.startaction()
        //出错清理缓存数据
        Bookreadclean.clean(this)
    }

    override fun initlistener() {
        super.initlistener()
        backhome.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        var tag=intent.getStringExtra("tag")
        when(tag){
                "BookDetailActivity"->{ }
                "ReadActivity"->{ destoryandsave(this) }
        }

    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}