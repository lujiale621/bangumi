package com.lc.bangumidemo.Fragment

import android.content.Intent
import android.os.Bundle
import com.lc.bangumidemo.Activity.Searchactivity
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.manhualayout.*

class ManhuaFragment :BaseFragment() {
    override fun setRes(): Int {
        return R.layout.manhualayout
    }
    override fun initlistener() {
        super.initlistener()
        searchm.setOnClickListener {
            var intent = Intent(activity, Searchactivity::class.java)
            var bundle= Bundle()
            bundle.putString("tag","漫画")
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}