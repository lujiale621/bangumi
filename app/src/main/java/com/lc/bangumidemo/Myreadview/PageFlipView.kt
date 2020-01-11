package com.lc.bangumidemo.Myreadview

import android.content.Context
import android.content.SharedPreferences
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log

import androidx.preference.PreferenceManager

import com.eschao.android.widget.pageflip.PageFlip
import com.eschao.android.widget.pageflip.PageFlipException
import com.lc.bangumidemo.Green.CommonDaoUtils
import com.lc.bangumidemo.Green.DaoUtilsStore
import com.lc.bangumidemo.Green.LocalBookIndex
import com.lc.bangumidemo.Green.LocalBookIndexDao
import com.lc.bangumidemo.KtUtil.localbookname

import java.util.concurrent.locks.ReentrantLock

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class PageFlipView : GLSurfaceView, GLSurfaceView.Renderer {
    internal var mPageNo: Int = 0
    lateinit var bookindexutil: CommonDaoUtils<LocalBookIndex>

    /**
     * Get duration of animating
     *
     * @return duration of animating
     */
    /**
     * Set animate duration
     *
     * @param duration duration of animating
     */
    var animateDuration: Int = 0
    internal lateinit var mHandler: Handler
    internal lateinit var mPageFlip: PageFlip
    internal var mPageRender: PageRender? = null
    internal lateinit var mDrawLock: ReentrantLock
    var _Store: DaoUtilsStore = DaoUtilsStore.getInstance()

    /**
     * Is auto page mode enabled?
     *
     * @return true if auto page mode enabled
     */
    val isAutoPageEnabled: Boolean
        get() = mPageFlip.isAutoPageEnabled

    /**
     * Get pixels of mesh
     *
     * @return pixels of mesh
     */
    val pixelsOfMesh: Int
        get() = mPageFlip.pixelsOfMesh

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        bookindexutil=_Store.bookindexDaoUtils

        // create handler to tackle message
        newHandler()

        // load preferences
        val pref = PreferenceManager
            .getDefaultSharedPreferences(context)
        animateDuration = pref.getInt(Constants.PREF_DURATION, 1000)
        val pixelsOfMesh = pref.getInt(Constants.PREF_MESH_PIXELS, 10)
        val isAuto = pref.getBoolean(Constants.PREF_PAGE_MODE, true)

        // create PageFlip
        mPageFlip = PageFlip(context)
        mPageFlip.setSemiPerimeterRatio(0.8f)
            .setShadowWidthOfFoldEdges(5f, 60f, 0.3f)
            .setShadowWidthOfFoldBase(5f, 80f, 0.4f)
            .setPixelsOfMesh(pixelsOfMesh)
            .enableAutoPage(isAuto)
        setEGLContextClientVersion(2)

        // init others

        val res = bookindexutil.queryByQueryBuilder(LocalBookIndexDao.Properties.Bookname.eq(localbookname))
        try {
            val upres = res.get(0)
            mPageNo = upres.pageindex
        }catch (e:Exception){
            mPageNo = 1
        }
        mDrawLock = ReentrantLock()
        mPageRender = SinglePageRender(
            context, mPageFlip,
            mHandler, mPageNo
        )
        // configure render
        setRenderer(this)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    /**
     * Enable/Disable auto page mode
     *
     * @param enable true is enable
     */
    fun enableAutoPage(enable: Boolean) {
        if (mPageFlip.enableAutoPage(enable)) {
            try {
                mDrawLock.lock()
                if (mPageFlip.secondPage != null && mPageRender is SinglePageRender) {
                    mPageRender = DoublePagesRender(
                        context,
                        mPageFlip,
                        mHandler,
                        mPageNo
                    )
                    mPageRender!!.onSurfaceChanged(
                        mPageFlip.surfaceWidth,
                        mPageFlip.surfaceHeight
                    )
                } else if (mPageFlip.secondPage == null && mPageRender is DoublePagesRender) {
                    mPageRender = SinglePageRender(
                        context,
                        mPageFlip,
                        mHandler,
                        mPageNo
                    )
                    mPageRender!!.onSurfaceChanged(
                        mPageFlip.surfaceWidth,
                        mPageFlip.surfaceHeight
                    )
                }
                requestRender()
            } finally {
                mDrawLock.unlock()
            }
        }
    }

    /**
     * Handle finger down event
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    fun onFingerDown(x: Float, y: Float) {
        // if the animation is going, we should ignore this event to avoid
        // mess drawing on screen
        if (!mPageFlip.isAnimating && mPageFlip.firstPage != null) {
            mPageFlip.onFingerDown(x, y)
        }
    }

    /**
     * Handle finger moving event
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    fun onFingerMove(x: Float, y: Float) {
        if (mPageFlip.isAnimating) {
            // nothing to do during animating
        } else if (mPageFlip.canAnimate(x, y)) {
            // if the point is out of current page, try to start animating
            onFingerUp(x, y)
        } else if (mPageFlip.onFingerMove(x, y)) {
            try {
                mDrawLock.lock()
                if (mPageRender != null && mPageRender!!.onFingerMove(x, y)) {
                    requestRender()
                }
            } finally {
                mDrawLock.unlock()
            }
        }// move page by finger
    }

    /**
     * Handle finger up event and start animating if need
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    fun onFingerUp(x: Float, y: Float) {
        if (!mPageFlip.isAnimating) {
            mPageFlip.onFingerUp(x, y, animateDuration)
            try {
                mDrawLock.lock()
                if (mPageRender != null && mPageRender!!.onFingerUp(x, y)) {
                    requestRender()
                }
            } finally {
                mDrawLock.unlock()
            }
        }
    }

    /**
     * Draw frame
     *
     * @param gl OpenGL handle
     */
    override fun onDrawFrame(gl: GL10) {
        try {
            mDrawLock.lock()
            if (mPageRender != null) {
                mPageRender!!.onDrawFrame()
            }
        } finally {
            mDrawLock.unlock()
        }
    }

    /**
     * Handle surface is changed
     *
     * @param gl OpenGL handle
     * @param width new width of surface
     * @param height new height of surface
     */
    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        try {
            mPageFlip.onSurfaceChanged(width, height)

            // if there is the second page, create double page render when need
            val pageNo = mPageRender!!.pageNo
            if (mPageFlip.secondPage != null && width > height) {
                if (mPageRender !is DoublePagesRender) {
                    mPageRender!!.release()
                    mPageRender = DoublePagesRender(
                        context,
                        mPageFlip,
                        mHandler,
                        pageNo
                    )
                }
            } else if (mPageRender !is SinglePageRender) {
                mPageRender!!.release()
                mPageRender = SinglePageRender(
                    context,
                    mPageFlip,
                    mHandler,
                    pageNo
                )
            }// if there is only one page, create single page render when need

            // let page render handle surface change
            mPageRender!!.onSurfaceChanged(width, height)
        } catch (e: PageFlipException) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceChanged")
        }

    }

    /**
     * Handle surface is created
     *
     * @param gl OpenGL handle
     * @param config EGLConfig object
     */
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        try {
            mPageFlip.onSurfaceCreated()
        } catch (e: PageFlipException) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceCreated")
        }

    }

    /**
     * Create message handler to cope with messages from page render,
     * Page render will send message in GL thread, but we want to handle those
     * messages in main thread that why we need handler here
     */
    private fun newHandler() {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    PageRender.MSG_ENDED_DRAWING_FRAME -> try {
                        mDrawLock.lock()
                        // notify page render to handle ended drawing
                        // message
                        if (mPageRender != null && mPageRender!!.onEndedDrawing(msg.arg1)) {
                            requestRender()
                        }
                    } finally {
                        mDrawLock.unlock()
                    }

                    else -> {
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = "PageFlipView"
    }
}
