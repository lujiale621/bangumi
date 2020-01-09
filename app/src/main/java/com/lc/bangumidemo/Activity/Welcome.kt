package com.lc.bangumidemo.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.lc.bangumidemo.KtUtil.*
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdataclass
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdatahelper
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdatainsert
import com.lc.bangumidemo.Sqlite.UserDatadatabase.Userdataselect
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class Welcome : AppCompatActivity() ,ViewPropertyAnimatorListener{
    override fun onAnimationCancel(view: View?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationStart(view: View?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationEnd(view: View?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
       //获取屏幕数据

        var display = getWindowManager().getDefaultDisplay();
        screenwidth =display.width
        screenheight =display.height
        //启动前检查数据是否有误
        //启动前检查是否存在用户数据
        var userdbhelper=Userdatahelper(this,"user.db",null,1)
        var result=Userdataselect.selectUserdata(userdbhelper)
        if(result==null){
            Userdatainsert.insertuserdata(userdbhelper, Userdataclass(fontsize, linesize,
                userbackground,backgroundcolor))
        } else{
            fontsize=result.fontsize
            linesize=result.linesize
            backgroundcolor=result.backgroundcolor
            userbackground=result.mybackground
        }
        userdbhelper.close()
        if(isfirst) {
            startActivity<IntroActivity>()
        }else
        {
            startActivity<MainActivity>()
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.animate(welcome).scaleX(1.0f).scaleY(1.0f).setDuration(2000).setListener(this)
    }

}
