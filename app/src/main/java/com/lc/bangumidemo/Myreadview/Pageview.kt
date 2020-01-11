package com.lc.bangumidemo.Myreadview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.lc.bangumidemo.KtUtil.*

import java.util.ArrayList

class Pageview : View {
    private var content: String? = null
    private val myheigh = screenheight
    private val mywidth = screenwidth
    private val txtlist = ArrayList<String>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setContent(string: String) {
        content = string
    }
/*
     (myheigh - mylinesize * (h + 40)) / 2 + (myheigh - mylinesize * (h + 40)) / 4 + i * (h + 40),
 */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (content != null) {
            val pen = Paint()
            val fontsize = calcFontSize(fontsize).toFloat()
            pen.textSize = fontsize
            pen.setColor(pencolor)
            val wid = fontsize * content!!.length
            val widthsub = mywidth - fontsize
            val linecount = (wid / widthsub).toInt() + 1
            val linesizes = (widthsub / fontsize).toInt()
            val count = cuttxt(content!!, linecount, linesizes)//返回加载多少个字能填满整个屏幕
            val h = pen.textSize
            for (i in 0 until txtlist.size) {
                canvas.drawText(
                    txtlist[i],
                    fontsize / 2,
                    (myheigh - linesize * (h + 40)) / 2 + (myheigh - linesize * (h + 40)) / 6 + i * (h + 40),
                    pen
                )
            }
        }
    }

    fun cuttxt(string: String, linecount: Int, linesize: Int): Int {
        var string = string
        txtlist.clear()
        val subsize = string.length
        var addsize = linesize * linecount - subsize
        val temp = subsize + addsize
        while (addsize != 0) {
            if (addsize<0){break}
            string = "$string "
            addsize--
        }
        val leng = string.length
        for (i in 0 until linecount) {
            val t = string.substring(linesize * i, linesize * (i + 1))
            txtlist.add(t)
        }
        return temp
    }

    protected fun calcFontSize(size: Int): Int {
        return (size * context!!.resources.displayMetrics
            .scaledDensity).toInt()
    }

}
