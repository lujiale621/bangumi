package com.lc.bangumidemo.Activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.lc.bangumidemo.R
import org.jetbrains.anko.startActivity

class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sliderPage = SliderPage()
        sliderPage.title = "搜索页"
        sliderPage.description = "你可以在此处搜索感兴趣的内容"
        sliderPage.imageDrawable = R.drawable.introduce1
        sliderPage.bgColor = Color.parseColor("#B2EBF2")
        addSlide(AppIntroFragment.newInstance(sliderPage))
        val sliderPage2 = SliderPage()
        sliderPage2.title = "收藏夹"
        sliderPage2.description = "小说和漫画都能收藏~~"
        sliderPage2.imageDrawable = R.drawable.introduce2
        sliderPage2.bgColor = Color.parseColor("#DCEDC8")
        addSlide(AppIntroFragment.newInstance(sliderPage2))
        val sliderPage3 = SliderPage()
        sliderPage3.title = "简介"
        sliderPage3.description = "在此处可以打开目录"
        sliderPage3.imageDrawable = R.drawable.introduce3
        sliderPage3.bgColor = Color.parseColor("#8D7D65")
        addSlide(AppIntroFragment.newInstance(sliderPage3))
        val sliderPage4 = SliderPage()
        sliderPage4.title = "菜单栏"
        sliderPage4.description = "一次性设置字体大小和行数"
        sliderPage4.imageDrawable = R.drawable.introduce4
        sliderPage4.bgColor = Color.parseColor("#D1C4E9")
        addSlide(AppIntroFragment.newInstance(sliderPage4))
        val sliderPage5 = SliderPage()
        sliderPage5.title = "菜单栏"
        sliderPage5.description = "进行背景自定义"
        sliderPage5.imageDrawable = R.drawable.introduce5
        sliderPage5.bgColor = Color.parseColor("#FFCDD2")
        addSlide(AppIntroFragment.newInstance(sliderPage5))

        setCustomTransformer(ZoomOutPageTransformer())
        showSkipButton(true)
        isProgressButtonEnabled = true
        setVibrate(true)
        setVibrateIntensity(30)
    }
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Do something when users tap on Skip button.
        lockscreen(true)
        this.finish()
        startActivity<MainActivity>()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        lockscreen(true)
        this.finish()
        startActivity<MainActivity>()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }

    inner class ZoomOutPageTransformer : ViewPager.PageTransformer {


        private fun transformFade(position: Float, page: View) {

            if (position <= -1.0f || position >= 1.0f) {

                page.translationX = page.width.toFloat()

                page.alpha = 0.0f

                page.isClickable = false

            } else if (position == 0.0f) {

                page.translationX = 0.0f

                page.alpha = 1.0f

                page.isClickable = true

            } else {

                // position is between -1.0F & 0.0F OR 0.0F & 1.0F

                page.translationX = page.width * -position

                page.alpha = 1.0f - Math.abs(position)

            }

        }
        override fun transformPage(page: View, position: Float) {
            if (position <= -1.0f || position >= 1.0f) {

                page.translationX = page.width.toFloat()

                page.alpha = 0.0f

                page.isClickable = false

            } else if (position == 0.0f) {

                page.translationX = 0.0f

                page.alpha = 1.0f

                page.isClickable = true

            } else {

                // position is between -1.0F & 0.0F OR 0.0F & 1.0F

                page.translationX = page.width * -position

                page.alpha = 1.0f - Math.abs(position)

            }

        }


    }
    open fun lockscreen(islock:Boolean){
        if(islock) { this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) }
        else { this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) } }

}
