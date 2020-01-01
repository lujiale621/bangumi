package com.lc.bangumidemo.Fragment

import android.content.Intent
import android.widget.Toast
import com.lc.bangumidemo.Activity.Aboutactivity
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookreadclean
import kotlinx.android.synthetic.main.minefragment.*

class MineFragment :BaseFragment(){
    override fun setRes(): Int {
        return R.layout.minefragment
    }

    override fun initlistener() {
        super.initlistener()
        cleandb.setOnClickListener {
            Bookreadclean.clean(context)
            Bookreadclean.cleancollect(context)
        }
        suggest.setOnClickListener {
            Toast.makeText(context,"send email to 751874332@qq.com",Toast.LENGTH_LONG).show()
        }
        aboutapp.setOnClickListener {
            var intent = Intent(context, Aboutactivity::class.java)
            startActivity(intent)
        }
    }

    override fun startaction() {
        super.startaction()
    }
}