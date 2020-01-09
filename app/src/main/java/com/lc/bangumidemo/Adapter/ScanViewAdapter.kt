package com.lc.bangumidemo.Adapter


import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.DrawFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat

import com.lc.bangumidemo.KtUtil.*
import com.lc.bangumidemo.Myreadview.Pageview
import com.lc.bangumidemo.Myreadview.ScanView
import com.lc.bangumidemo.R
import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.RxBus.RxBusBaseMessage
import com.lc.bangumidemo.Sqlite.NoveDatabase.BookIndexclass
import com.lc.bangumidemo.Sqlite.NoveDatabase.BookReadclass
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookselect
import com.lc.bangumidemo.Sqlite.NoveDatabase.Bookupdata
import com.lc.bangumidemo.Sqlite.NoveDatabase.MyDatabaseHelper
import com.lc.bangumidemo.Sqlite.NoveDatabase.Selectclass

class ScanViewAdapter(
    internal var context: Context,
    startindex: BookIndexclass
) : PageAdapter() {
    internal var am: AssetManager
    internal var db: MyDatabaseHelper? = null
    internal var selectclass: Selectclass? = null
    internal var startindex: BookIndexclass? = null
    internal var layview:View?=null
    var bg:Drawable?=null
    override val count: Int
        get() = 0

    /**
     * LayoutInflater.from(context).inflate(R.layout.page_layout, null)
     */


    open fun setbkcolor(b:Drawable){ bg=b }

    fun getview():View?
    {
        layview =LayoutInflater.from(context).inflate(R.layout.page_layout, null)
        if (bg!=null)
        {
            var pageviewbg= layview?.findViewById<ImageView>(R.id.pagebg)
            pageviewbg?.setImageDrawable(bg)
        }
        return layview
    }
    init {
        am = context.assets
        db= MyDatabaseHelper(context, "bookstore", null, 1)
        selectclass = Selectclass(
            startindex.bookname,
            startindex.author,
            startindex.pagecount
        )
        this.startindex = startindex
        val mydrawable :Drawable = if (backgroundcolor[0].equals('#')){
            ColorDrawable(Color.parseColor(backgroundcolor))
        }else {
            Drawable.createFromPath(backgroundcolor)!!
        }
        mydrawable.setBounds(0,0, screenwidth, screenheight)
        setbkcolor(mydrawable)
    }
        fun setonPageclickListener(view: ScanView,onpageclick: ScanView.OnpageClick){
            view.setOnpageClick(onpageclick)
        }
    override fun addContent(view: View, position: Int) {
        ScanView.stopleft=false
        ScanView.stopright=false
        var retsult: BookReadclass? = null
        val pageview = view.findViewById<Pageview>(R.id.pageview)
        val pagesize = view.findViewById<TextView>(R.id.pagesize)
        val pagetitle = view.findViewById<TextView>(R.id.pagetitle)
        val pace = view.findViewById<TextView>(R.id.pace)
        updatamenu(context,pagesize,pagetitle, pace)
        var st: String?
        Bookselect.selectalldata(db!!)
        try {
            retsult = selectclass?.let { Bookselect.selectbookreaddata(db!!, it, position) }
            st = retsult!!.bookdata
            pageview.setContent(st)
        } catch (e: Exception) { }

            try {
            if (ScanView.INDEXTAGRIG == true || ScanView.INDEXTAGLETE == true) {
                var requese: BookIndexclass?
                if (ScanView.INDEXTAGRIG == true) {
                    requese = BookIndexclass(
                        null,
                        startindex!!.author,
                        startindex!!.bookname,
                        startindex!!.hardpageindex,
                        startindex!!.hardcontentindex,
                        startindex!!.pagecount,
                        retsult!!.pageindex,
                        retsult.contentindex - 1
                    )
                    Bookupdata.updata(db!!, requese)
                    updatamenu(context,pagesize,pagetitle, pace)
                    Mapupdata(context, requese)
                } else {
                    requese = BookIndexclass(
                        null,
                        startindex!!.author,
                        startindex!!.bookname,
                        startindex!!.hardpageindex,
                        startindex!!.hardcontentindex,
                        startindex!!.pagecount,
                        retsult!!.pageindex,
                        retsult.contentindex + 1
                    )
                    Bookupdata.updata(db!!, requese)
                    updatamenu(context,pagesize,pagetitle, pace)
                    Mapupdata(context, requese)
                }
                ScanView.INDEXTAGRIG = false
                ScanView.INDEXTAGLETE = false
            }}catch (e:KotlinNullPointerException){
                //Toast.makeText(context,"已经没有内容了~~~",Toast.LENGTH_LONG).show()
                if(ScanView.INDEXTAGRIG == true) {
                    Toast.makeText(context, "已经没有内容了~~~", Toast.LENGTH_LONG).show()

                    ScanView.stopright=true
                }
                if(ScanView.INDEXTAGLETE == true){
                    Toast.makeText(context, "已经没有内容了~~~", Toast.LENGTH_LONG).show()
                    ScanView.stopleft=true
                }
            }
    }
    fun updatamenu(context: Context,pagesize:TextView,pagetitle:TextView,pace:TextView){
        var sult = Bookselect.selectbookindex(context)
        var i = MyDatabaseHelper(context, "bookstore", null, 1)
        var selectdata = Selectclass(
            bookDetail!!.data.name,
            bookDetail!!.data.author,
            bookDetail!!.list.size
        )
        var resultnow = Bookselect.selectbookdata(i, selectdata, sult!!.pageindex)
        var list = PagesizeUtil.txttolist(resultnow!!.content, context,
            fontsize,
            linesize
        )
        pagesize.setText("${sult.contentindex+1}/${list.size-1}")
        pagetitle.setText(
            stdeal(
                bookDetail!!.list[sult.pageindex].num,
                10
            )
        )
        val str :String = String.format("%.2f",((sult.pageindex+1).toFloat()/sult.pagecount.toFloat())*100)
        pace.setText(str+"%")
    }
}