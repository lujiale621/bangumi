package com.lc.bangumidemo.Fragment

import android.content.Intent
import android.os.Bundle
import com.lc.bangumidemo.Activity.Searchactivity
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.videolayout.*

class VideoFragment :BaseFragment() {
    override fun setRes(): Int {
        return R.layout.videolayout
    }
    override fun initlistener() {
        super.initlistener()
        searchv.setOnClickListener {
            var intent = Intent(activity, Searchactivity::class.java)
            var bundle= Bundle()
            bundle.putString("tag","影视")
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}