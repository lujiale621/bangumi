package com.lc.bangumidemo.Fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.lc.bangumidemo.Activity.Searchactivity
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.novebook.*

class NovebookFragment :BaseFragment() {
    override fun setRes(): Int {
        return R.layout.novebook
    }

    override fun initview() {
        super.initview()

    }

    override fun initlistener() {
        super.initlistener()
        searchf.setOnClickListener {
            var intent =Intent(activity, Searchactivity::class.java)
            var bundle=Bundle()
            bundle.putString("tag","小说")
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}