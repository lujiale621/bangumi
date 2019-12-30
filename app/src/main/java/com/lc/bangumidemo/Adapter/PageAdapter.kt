package com.lc.bangumidemo.Adapter


import android.view.View

abstract class PageAdapter {
    /**
     * @return 页面view
     */
    abstract val view: View

    abstract val count: Int

    /**
     * 将内容添加到view中
     *
     * @param view
     * 包含内容的view
     * @param position
     * 第position页
     */
    abstract fun addContent(view: View, position: Int)
}