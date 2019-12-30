package com.lc.bangumidemo.Myreadview

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.RectF
import android.graphics.Shader.TileMode
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.lc.bangumidemo.Activity.ReadActivity

import com.lc.bangumidemo.Adapter.PageAdapter
import com.lc.bangumidemo.Adapter.ScanViewAdapter
import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.RxBus.RxBusBaseMessage
import kotlinx.android.synthetic.main.tesst.*
import kotlinx.android.synthetic.main.tesst.view.*

import java.util.Timer
import java.util.TimerTask

/**
 * @author chenjing
 */
class ScanView : RelativeLayout {
    private var isInit = true
    // 滑动的时候存在两页可滑动，要判断是哪一页在滑动
    private var isPreMoving = true
    private var isCurrMoving = true
    // 当前是第几页
    private var index: Int = 0
    private var lastX: Float = 0.toFloat()
    // 前一页，当前页，下一页的左边位置
    private var prePageLeft = 0
    private var currPageLeft = 0
    private var nextPageLeft = 0
    // 三张页面
    private var prePage: View? = null
    private var currPage: View? = null
    private var nextPage: View? = null
    private var state = STATE_STOP
    // 正在滑动的页面右边位置，用于绘制阴影
    private var right: Float = 0.toFloat()
    // 手指滑动的距离
    private var moveLenght: Float = 0.toFloat()
    // 页面宽高
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    // 获取滑动速度
    private var vt: VelocityTracker? = null
    // 防止抖动
    private val speed_shake = 20f
    // 当前滑动速度
    private var speed: Float = 0.toFloat()
    private var timer: Timer? = null
    private var mTask: MyTimerTask? = null
    // 页面适配器
    private var adapter: PageAdapter? = null
    internal var context: Context? = null
    lateinit var onclick :OnpageClick

    interface OnpageClick{fun onItemClick()}
    /**
     * 过滤多点触碰的控制变量
     */
    private var mEvents: Int = 0

    internal var updateHandler: Handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            if (state != STATE_MOVE)
                return
            // 移动页面
            // 翻回，先判断当前哪一页处于未返回状态
            if (prePageLeft > -mWidth && speed < -0) {
                // 前一页处于未返回状态
                moveLeft(PRE)
            } else if (currPageLeft < 0 && speed > 0) {
                // 当前页处于未返回状态
                moveRight(CURR)
            } else if (speed < 0) {
                // 向左翻，翻动的是当前页
                moveLeft(CURR)
                if (currPageLeft == -mWidth) {
                    index++
                    INDEXTAGRIG = true
                    // 翻过一页，在底下添加一页，把最上层页面移除
                    addNextPage()
                }
            } else if (speed > 0) {
                // 向右翻，翻动的是前一页
                moveRight(PRE)
                if (prePageLeft == 0) {
                    index--
                    // 翻回一页，添加一页在最上层，隐藏在最左边
                    INDEXTAGLETE = true
                    addPrePage()
                }
            }
            if (right == 0f || right == mWidth.toFloat()) {
                releaseMoving()
                state = STATE_STOP
                quitMove()
            }
            this@ScanView.requestLayout()
        }

    }

    fun setAdapter(adapter: ScanViewAdapter) {
        removeAllViews()
        this.adapter = adapter
        prePage = adapter.view
        addView(
            prePage, 0, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        adapter.addContent(prePage!!, index - 1)

        currPage = adapter.view
        addView(
            currPage, 0, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        adapter.addContent(currPage!!, index)

        nextPage = adapter.view
        addView(
            nextPage, 0, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        adapter.addContent(nextPage!!, index + 1)

    }

    /**
     * 向左滑。注意可以滑动的页面只有当前页和前一页
     *
     * @param which
     */
    private fun moveLeft(which: Int) {
        when (which) {
            PRE -> {
                prePageLeft -= MOVE_SPEED
                if (prePageLeft < -mWidth)
                    prePageLeft = -mWidth
                right = (mWidth + prePageLeft).toFloat()
            }
            CURR -> {
                currPageLeft -= MOVE_SPEED
                if (currPageLeft < -mWidth)
                    currPageLeft = -mWidth
                right = (mWidth + currPageLeft).toFloat()
            }
        }
    }

    /**
     * 向右滑。注意可以滑动的页面只有当前页和前一页
     *
     * @param which
     */
    private fun moveRight(which: Int) {
        when (which) {
            PRE -> {
                prePageLeft += MOVE_SPEED
                if (prePageLeft > 0)
                    prePageLeft = 0
                right = (mWidth + prePageLeft).toFloat()
            }
            CURR -> {
                currPageLeft += MOVE_SPEED
                if (currPageLeft > 0)
                    currPageLeft = 0
                right = (mWidth + currPageLeft).toFloat()
            }
        }
    }

    /**
     * 当往回翻过一页时添加前一页在最左边
     */
    private fun addPrePage() {
        removeView(nextPage)
        addView(
            nextPage, -1, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        // 从适配器获取前一页内容
        adapter!!.addContent(nextPage!!, index - 1)
        // 交换顺序
        val temp = nextPage
        nextPage = currPage
        currPage = prePage
        prePage = temp
        prePageLeft = -mWidth
    }

    /**
     * 当往前翻过一页时，添加一页在最底下
     */
    private fun addNextPage() {
        removeView(prePage)
        addView(
            prePage, 0, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        // 从适配器获取后一页内容
        adapter!!.addContent(prePage!!, index + 1)
        // 交换顺序
        val temp = currPage
        currPage = nextPage
        nextPage = prePage
        prePage = temp
        currPageLeft = 0
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        this.context = context
        init()
    }

    constructor(context: Context) : super(context) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
        init()
    }

    /**
     * 退出动画翻页
     */
    fun quitMove() {
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
    }

    private fun init() {
        index = 0
        timer = Timer()
        mTask = MyTimerTask(updateHandler)
    }

    /**
     * 释放动作，不限制手滑动方向
     */
    private fun releaseMoving() {
        isPreMoving = true
        isCurrMoving = true
    }
    open fun onclick(){ if(onclick!=null) onclick.onItemClick() }
    open fun setOnpageClick(click:OnpageClick){
        onclick=click
    }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (adapter != null)
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    try {
                        if (vt == null) {
                            vt = VelocityTracker.obtain()
                        } else {
                            vt!!.clear()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    vt!!.addMovement(event)
                    mEvents = 0
                }
                MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                    mEvents = -1
                    vt!!.computeCurrentVelocity(500)
                }
                MotionEvent.ACTION_MOVE -> {
                    // 取消动画
                    quitMove()

                    Log.d(
                        "index", "mEvents = " + mEvents + ", isPreMoving = "
                                + isPreMoving + ", isCurrMoving = " + isCurrMoving
                    )
                    vt!!.addMovement(event)
                    vt!!.computeCurrentVelocity(500)
                    speed = vt!!.xVelocity
                    moveLenght = event.x - lastX
                    if ((moveLenght > movesp || !isCurrMoving) && isPreMoving
                        && mEvents == 0&& !ReadActivity.ismenushow&& !ReadActivity.islistshow
                    ) {
                        isPreMoving = true
                        isCurrMoving = false
                        if (stopleft) {
                            // 第一页不能再往右翻，跳转到前一个activity
                            state = STATE_STOP
                            releaseMoving()

                        } else {

                            // 非第一页
                            prePageLeft += moveLenght.toInt()
                            // 防止滑过边界
                            if (prePageLeft > 0)
                                prePageLeft = 0
                            else if (prePageLeft < -mWidth) {

                                // 边界判断，释放动作，防止来回滑动导致滑动前一页时当前页无法滑动
                                prePageLeft = -mWidth
                                releaseMoving()
                            }
                            right = (mWidth + prePageLeft).toFloat()
                            state = STATE_MOVE
                        }
                    } else if ((moveLenght < -movesp || !isPreMoving) && isCurrMoving
                        && mEvents == 0&& !ReadActivity.ismenushow&& !ReadActivity.islistshow
                    ) {
                        isPreMoving = false
                        isCurrMoving = true
                        if (stopright) {
                            // 最后一页不能再往左翻

                            state = STATE_STOP
                            releaseMoving()
                        } else {

                            currPageLeft += moveLenght.toInt()
                            // 防止滑过边界
                            if (currPageLeft < -mWidth)
                                currPageLeft = -mWidth
                            else if (currPageLeft > 0) {
                                // 边界判断，释放动作，防止来回滑动导致滑动当前页是前一页无法滑动
                                currPageLeft = 0
                                releaseMoving()
                            }
                            right = (mWidth + currPageLeft).toFloat()
                            state = STATE_MOVE
                        }

                    } else
                        mEvents = 0
                    lastX = event.x
                    requestLayout()
                }
                MotionEvent.ACTION_UP -> {
                    if ((lastX > 300 && lastX < 750 && Math.abs(moveLenght) < 30&&Math.abs(vt!!.xVelocity)<30)||(ReadActivity.ismenushow)) {
                     onclick()
                    }
                    if (lastX > 750 && lastX < 1500 && vt!!.xVelocity <= 50 && Math.abs(moveLenght) < 30 && !ReadActivity.ismenushow&& !ReadActivity.islistshow) {
                        isPreMoving = false
                        isCurrMoving = true
                        vt!!.addMovement(event)
                        speed = -1000f
                        state = STATE_MOVE
                        releaseMoving()
                        requestLayout()
                    }
                    if (lastX > 0 && lastX < 300 && vt!!.xVelocity >= -50 && Math.abs(moveLenght) < 30 && !ReadActivity.ismenushow&& !ReadActivity.islistshow) {
                        isPreMoving = true
                        isCurrMoving = false
                        vt!!.addMovement(event)
                        speed = 1000f
                        state = STATE_MOVE
                        releaseMoving()
                        requestLayout()
                    }
                    if(ReadActivity.islistshow){
                        RxBus.getInstance().send(3, RxBusBaseMessage(3,"closemenulist"))
                    }

                    if (Math.abs(speed) < speed_shake)
                        speed = 0f
                    quitMove()
                    mTask = MyTimerTask(updateHandler)
                    timer!!.schedule(mTask, 0, 5)
                    try {
                        vt!!.clear()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                else -> {
                }
            }
        super.dispatchTouchEvent(event)
        return true
    }

    /*
     * （非 Javadoc） 在这里绘制翻页阴影效果
     *
     * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (right == 0f || right == mWidth.toFloat())
            return
        val rectF = RectF(right, 0f, mWidth.toFloat(), mHeight.toFloat())
        val paint = Paint()
        paint.isAntiAlias = true
        val linearGradient = LinearGradient(
            right, 0f,
            right + 36, 0f, -0x444445, 0x00bbbbbb, TileMode.CLAMP
        )
        paint.shader = linearGradient
        paint.style = Style.FILL
        canvas.drawRect(rectF, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        if (isInit) {
            // 初始状态，一页放在左边隐藏起来，两页叠在一块
            prePageLeft = -mWidth
            currPageLeft = 0
            nextPageLeft = 0
            isInit = false
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (adapter == null)
            return
        prePage!!.layout(
            prePageLeft, 0,
            prePageLeft + prePage!!.measuredWidth,
            prePage!!.measuredHeight
        )
        currPage!!.layout(
            currPageLeft, 0,
            currPageLeft + currPage!!.measuredWidth,
            currPage!!.measuredHeight
        )
        nextPage!!.layout(
            nextPageLeft, 0,
            nextPageLeft + nextPage!!.measuredWidth,
            nextPage!!.measuredHeight
        )
        invalidate()
    }

    internal inner class MyTimerTask(var handler: Handler) : TimerTask() {

        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }

    }

    companion object {
        val TAG = "ScanView"
        var INDEXTAGRIG = false
        var INDEXTAGLETE = false
        // 页面状态
        private val STATE_MOVE = 0
        private val STATE_STOP = 1
        // 滑动的页面，只有前一页和当前页可滑
        private val PRE = 2
        private val CURR = 3
        // 滑动动画的移动速度
        val MOVE_SPEED = 40
        //滑动条件
        val movesp = 30
        //防止溢出
        var stopleft=false
        var stopright=false

    }
}
