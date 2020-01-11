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

        var _Store = DaoUtilsStore.getInstance()
        bookdatautil= _Store.bookdataDaoUtils
        var temp= LocalBookData()
        temp.author="asd"
        temp.bookname="adada"
        temp.content="sadasads"
        temp.contentsize=123
        temp.pagecount=1233
        temp.pageindex=123132
        bookdatautil.insert(temp)
        var temp2=LocalBookReadClass()
        temp2.author="adasda"
        temp2.bookname="cxczxdds"
        temp2.pagecount=123
        _Store.bookreaddataDaoUtils.insert(temp2)
        var data=bookdatautil.queryAll()
        var data2=_Store.bookreaddataDaoUtils.queryAll()
        testtextview.setText(data2[0].author+data2[0]._id+data2[0].bookname)

    }
}