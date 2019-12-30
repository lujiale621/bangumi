package com.lc.bangumidemo.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(setRes(),container,false)
        initview()
        return view
    }

    open fun initview() {

    }


    /**
    *初始化初始布局
    */
    abstract fun setRes():Int

    override fun onStart() {
        super.onStart()
        initlistener()
        startaction()
        lockscreen(false)
    }

    /**
     * 事件活动
     */
    open fun startaction(){
    }

    /**
     * 监听
     */
    open fun initlistener(){}
    open fun lockscreen(islock:Boolean){
        if(islock) { activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) }
        else {activity!!.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) } }

}