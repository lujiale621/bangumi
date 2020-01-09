package com.lc.bangumidemo.Util

import androidx.fragment.app.Fragment
import com.lc.bangumidemo.Fragment.*
import com.lc.bangumidemo.R

class FragmentUtil private constructor(){
    val homeFragment by lazy { HomeFragment() }
    val processFragment by lazy { ProcessFragment() }
    val dymatFragment by lazy { DymatFragment() }
    val mineFragment by lazy { MineFragment() }
    val introduceFragmentfirst by lazy { IntroduceFragmentfirst() }
    val introduceFragmentsecond by lazy { IntroduceFragmentsecond() }
    companion object{
        val fragmentUtil by lazy{FragmentUtil()}
    }
    fun getFragment(tabId:Int): Fragment? {
        when(tabId){
            R.id.home->return homeFragment
            R.id.eye->return processFragment
            R.id.genie->return dymatFragment
            R.id.voice->return mineFragment
        }
        return null
    }
}