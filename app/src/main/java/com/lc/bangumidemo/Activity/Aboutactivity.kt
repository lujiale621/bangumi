package com.lc.bangumidemo.Activity

import android.view.MenuItem
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.aboutactivity.*

class Aboutactivity :BaseActivity() {
    override fun setRes(): Int {
        return R.layout.aboutactivity
    }

    override fun initview() {
        super.initview()
        setSupportActionBar(abouttoolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}