package com.lc.bangumidemo.Activity

import android.view.KeyEvent
import com.lc.bangumidemo.KtUtil.destoryandsave
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookreadclean
import org.jetbrains.anko.toast

class ErrorActivity : BaseActivity() {
    override fun setRes(): Int {
        return R.layout.error
    }

    override fun initview() {
        super.initview()
         toast(intent.getStringExtra("msg"))

         toast(intent.getStringExtra("tag"))
    }

    override fun startaction() {
        super.startaction()
        //出错清理缓存数据
        Bookreadclean.clean(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        var tag=intent.getStringExtra("tag")
        when(tag){
                "BookDetailActivity"->{ }
                 "Read_Activity"->{
                destoryandsave(this)
            }
        }

    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}