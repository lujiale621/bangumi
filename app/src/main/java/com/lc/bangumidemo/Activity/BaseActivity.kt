package com.lc.bangumidemo.Activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setRes())
        initview()
        initaction()

    }




    override fun onStart() {
        super.onStart()
        initlistener()
        startaction()
    }

    /**
     * 初始化初始布局
     */
    abstract fun setRes():Int

    /**
     * 初始化视图
     */
    open fun initview() {}
    /**
     * 初始化监听
     */
    open fun initlistener() {}
    open fun initaction() {}
    /**
     * 编写你的行为
     */
    open fun startaction(){}

    open fun lockscreen(islock:Boolean){
        if(islock) { this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) }
        else { this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) } }

    override fun onRestart() {
        super.onRestart()
        lockscreen(false)
    }
}