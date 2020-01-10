package com.lc.bangumidemo.Activity
import android.os.Bundle
import android.preference.PreferenceActivity
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.setlayout.*

class SetActivity : PreferenceActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setlayout);
        addPreferencesFromResource(R.xml.preferces)
        settoolbar.overflowIcon=getDrawable(R.drawable.iconback)
        
    }

}