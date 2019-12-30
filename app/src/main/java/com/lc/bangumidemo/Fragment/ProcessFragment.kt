package com.lc.bangumidemo.Fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lc.bangumidemo.R
import kotlinx.android.synthetic.main.processfragment.*

class ProcessFragment :BaseFragment() {
    override fun setRes(): Int {
        return R.layout.processfragment
    }

    override fun startaction() {
        super.startaction()
        tab_viewpager.adapter = myviewpageadapter(childFragmentManager)
        tab_layout.setupWithViewPager(tab_viewpager)
    }
    class myviewpageadapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
        var Titlelist: ArrayList<String> = arrayListOf()
        var Fragmentlist: ArrayList<Fragment> = arrayListOf()


        init {
            Fragmentlist.add(NovebookFragment())
            Fragmentlist.add(VideoFragment())
            Fragmentlist.add(ManhuaFragment())
            Fragmentlist.add(ZhongheFragment())
            Titlelist.add("小说")
            Titlelist.add("影视")
            Titlelist.add("漫画")
            Titlelist.add("综合")
        }
        override fun getItem(position: Int): Fragment {
            return Fragmentlist[position]
        }

        override fun getCount(): Int {
            return Titlelist.size//To change body of created functions use File | Settings | File Templates.
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return Titlelist[position]
        }
    }
}