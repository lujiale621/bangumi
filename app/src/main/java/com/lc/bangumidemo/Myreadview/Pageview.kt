package com.lc.bangumidemo.Myreadview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.lc.bangumidemo.KtUtil.fontsize
import com.lc.bangumidemo.KtUtil.screenheight
import com.lc.bangumidemo.KtUtil.screenwidth

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (content != null) {
            val pen = Paint()
            val fontsize = calcFontSize(mytextsize).toFloat()
            pen.textSize = fontsize
            val wid = fontsize * content!!.length
            val widthsub = mywidth - fontsize
            val linecount = (wid / widthsub).toInt() + 1
            val linesize = (widthsub / fontsize).toInt()
            val count = cuttxt(content!!, linecount, linesize)//返回加载多少个字能填满整个屏幕
            val h = pen.textSize
            for (i in 0 until linecount) {
                canvas.drawText(
                    txtlist[i],
                    fontsize / 2,
                    (myheigh - 16 * (h + 40)) / 2 + (myheigh - 16 * (h + 40)) / 4 + i * (h + 40),
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

    companion object {
        var mytextsize = fontsize
    }
}
