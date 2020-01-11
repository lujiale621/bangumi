package com.lc.bangumidemo.Myreadview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast

import com.eschao.android.widget.pageflip.Page
import com.eschao.android.widget.pageflip.PageFlip
import com.eschao.android.widget.pageflip.PageFlipState
import com.lc.bangumidemo.KtUtil.*
import java.lang.Exception
import java.util.ArrayList
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import com.lc.bangumidemo.Green.*
import java.time.temporal.Temporal


/**
 * Single page render
 *
 *
 * Every page need 2 texture in single page mode:
 *
 *  * First texture: current page content
 *  * Back texture: back of front content, it is same with first texture
 *
 *  * Second texture: next page content
 *
 *
 *
 * @author eschao
 */

class SinglePageRender(
    context: Context, pageFlip: PageFlip,
    handler: Handler, pageNo: Int
) : PageRender(context, pageFlip, handler, pageNo) {
    private var content: String? = null
    private val myheigh = screenheight
    private val mywidth = screenwidth
    private val txtlist = ArrayList<String>()
    private val tlist = ArrayList<String>()
    var _Store: DaoUtilsStore = DaoUtilsStore.getInstance()
    lateinit var bookreaddatautil: CommonDaoUtils<LocalBookReadClass>
    lateinit var bookindexutil: CommonDaoUtils<LocalBookIndex>
    init {
        bookreaddatautil=_Store.bookreaddataDaoUtils
        bookindexutil=_Store.bookindexDaoUtils
       var temp= bookreaddatautil.queryByQueryBuilder(LocalBookReadClassDao.Properties.Bookname.eq(
            localbookname))
        for(i in temp){
            tlist.add(i.bookdata)
        }
        PageRender.MAX_PAGES=tlist.size
    }
    /**
     * Draw frame
     */
    public override fun onDrawFrame() {
        // 1. delete unused textures
        mPageFlip.deleteUnusedTextures()
        val page = mPageFlip.firstPage

        // 2. handle drawing command triggered from finger moving and animating
        if (mDrawCommand == PageRender.DRAW_MOVING_FRAME || mDrawCommand == PageRender.DRAW_ANIMATING_FRAME) {
            // is forward flip
            if (mPageFlip.flipState == PageFlipState.FORWARD_FLIP) {
                // check if second texture of first page is valid, if not,
                // create new one
                if (!page.isSecondTextureSet) {
                    drawPage(mPageNo + 1)
                    page.setSecondTexture(mBitmap)
                }
            } else if (!page.isFirstTextureSet) {
                drawPage(--mPageNo)
                page.setFirstTexture(mBitmap)
            }// in backward flip, check first texture of first page is valid

            // draw frame for page flip
            mPageFlip.drawFlipFrame()
        } else if (mDrawCommand == PageRender.DRAW_FULL_PAGE) {
            if (!page.isFirstTextureSet) {
                drawPage(mPageNo)
                page.setFirstTexture(mBitmap)
            }

            mPageFlip.drawPageFrame()
        }// draw stationary page without flipping

        // 3. send message to main thread to notify drawing is ended so that
        // we can continue to calculate next animation frame if need.
        // Remember: the drawing operation is always in GL thread instead of
        // main thread
        val msg = Message.obtain()
        msg.what = PageRender.MSG_ENDED_DRAWING_FRAME
        msg.arg1 = mDrawCommand
        mHandler.sendMessage(msg)
    }

    /**
     * Handle GL surface is changed
     *
     * @param width surface width
     * @param height surface height
     */
    public override fun onSurfaceChanged(width: Int, height: Int) {
        // recycle bitmap resources if need
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle()
        }

        if (mBitmap != null) {
            mBitmap.recycle()
        }

        // create bitmap and canvas for page
        //mBackgroundBitmap = background;
        val page = mPageFlip.firstPage
        mBitmap = Bitmap.createBitmap(
            page.width().toInt(), page.height().toInt(),
            Bitmap.Config.ARGB_8888
        )
        mCanvas.setBitmap(mBitmap)
        LoadBitmapTask.get(mContext).set(width, height, 1)
    }

    /**
     * Handle ended drawing event
     * In here, we only tackle the animation drawing event, If we need to
     * continue requesting render, please return true. Remember this function
     * will be called in main thread
     *
     * @param what event type
     * @return ture if need render again
     */
    public override fun onEndedDrawing(what: Int): Boolean {
        if (what == PageRender.DRAW_ANIMATING_FRAME) {
            val isAnimating = mPageFlip.animating()
            // continue animating
            if (isAnimating) {
                mDrawCommand = PageRender.DRAW_ANIMATING_FRAME
                return true
            } else {
                val state = mPageFlip.flipState
                // update page number for backward flip
                if (state == PageFlipState.END_WITH_BACKWARD) {
                    // don't do anything on page number since mPageNo is always
                    // represents the FIRST_TEXTURE no;
                } else if (state == PageFlipState.END_WITH_FORWARD) {
                    mPageFlip.firstPage.setFirstTextureWithSecond()
                    mPageNo++
                }// update page number and switch textures for forward flip

                mDrawCommand = PageRender.DRAW_FULL_PAGE
                return true
            }// animation is finished
        }
        return false
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

    /**
     * Draw page content
     *
     * @param number page number
     */
    private fun drawPage(number: Int) {
        val width = mCanvas.width
        val height = mCanvas.height
        val p = Paint()
        p.isFilterBitmap = true

        // 1. draw background bitmap
        val mydrawable : Drawable = if (backgroundcolor[0] == '#'){
            ColorDrawable(Color.parseColor(backgroundcolor))
        }else {
            Drawable.createFromPath(backgroundcolor)!!
        }
        var bitmapm= mydrawable.toBitmap(width, height)
        var background: Bitmap? = bitmapm
        val rect = Rect(0, 0, width, height)
        mCanvas.drawBitmap(background!!, null, rect, p)
        background.recycle()
        background = null

        try {
            content=tlist[number]
            var res=bookindexutil.queryByQueryBuilder(LocalBookIndexDao.Properties.Bookname.eq(localbookname))
            var upres=res.get(0)
            upres.pageindex=number
            bookindexutil.update(upres)
        }
       catch (e:Exception){
           Log.e("SINGLEPAGERENDER",e.toString())
       }
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
                mCanvas.drawText(
                    txtlist[i],
                    fontsize / 2,
                    (myheigh - linesize * (h + 40)) / 2 + (myheigh - linesize * (h + 40)) / 6 + i * (h + 40),
                    pen
                )
            }
        }
    }
    /**
     * If page can flip forward
     *
     * @return true if it can flip forward
     */
    override fun canFlipForward(): Boolean {
        return mPageNo < PageRender.MAX_PAGES
    }

    /**
     * If page can flip backward
     *
     * @return true if it can flip backward
     */
    override fun canFlipBackward(): Boolean {
        if (mPageNo > 1) {
            mPageFlip.firstPage.setSecondTextureWithFirst()
            return true
        } else {
            return false
        }
    }
}
