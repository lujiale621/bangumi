package com.lc.bangumidemo.Fragment

import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.homefragment.*


class HomeFragment : BaseFragment() {
    var imglist= mutableListOf<String>()
    override fun setRes(): Int {
        return R.layout.homefragment
    }

    override fun initview() {
        super.initview()
        //
        setimglist()

    }
    fun setimglist()
    {
        imglist.add("https://i0.hdslb.com/bfs/sycp/creative_img/201912/e3e67c419296822d3cdd226960fe0d46.jpg@1539w_678h_1c_100q.webp")
        imglist.add("//i0.hdslb.com/bfs/archive/4a401a552cdeb98438b2ca2d1443513379cb51b2.jpg@1539w_678h_1c_100q.webp")
        imglist.add("//i0.hdslb.com/bfs/archive/19158e26014b7a59f08ba59ad997b4fd6e03f05e.png@620w_220h.png")
    }

}