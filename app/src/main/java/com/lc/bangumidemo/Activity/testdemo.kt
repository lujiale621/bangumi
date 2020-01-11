package com.lc.bangumidemo.Activity

import com.lc.bangumidemo.Green.CommonDaoUtils
import com.lc.bangumidemo.Green.DaoUtilsStore
import com.lc.bangumidemo.Green.LocalBookData
import com.lc.bangumidemo.Green.LocalBookReadClass
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.tess.*
import org.greenrobot.greendao.query.WhereCondition

class testdemo :BaseActivity() {

    lateinit var bookdatautil: CommonDaoUtils<LocalBookData>
    override fun setRes(): Int {
        return R.layout.tess
    }

    override fun startaction() {
        super.startaction()
    }
}