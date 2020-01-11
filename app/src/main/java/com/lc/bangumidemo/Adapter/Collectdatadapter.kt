package com.lc.bangumidemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lc.bangumidemo.R
import com.lc.bangumidemo.Sqlite.CollectDatabase.Collectdataclass
import java.util.ArrayList
import android.widget.TextView
import com.lid.lib.LabelImageView
import android.graphics.Bitmap
import androidx.cardview.widget.CardView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.lc.bangumidemo.KtUtil.gotoread


class Collectdatadapter(private val context: Context, private val collectlist: ArrayList<Collectdataclass>) : BaseAdapter()  {
    private lateinit var Clicklistener:onCollectClicklistener
    interface onCollectClicklistener{
        fun onItemClick(requestdata:Collectdataclass)
    }
    open fun setOnCollectClicklistener(onClicklistener:onCollectClicklistener){
        Clicklistener=onClicklistener
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cover: LabelImageView?
        var cardview:CardView
        var name:TextView
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.collectgriditem, parent, false)
        cover=view.findViewById(R.id.img_icon)
        cardview=view.findViewById(R.id.cv)
        name=view.findViewById(R.id.colname)
        name.setText(collectlist[position].name)
        //
            Glide.with(context)
                .load(collectlist[position].cover)
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        glideAnimation: GlideAnimation<in Bitmap>
                    ) {
                        //加载完成后的处理
                        cover.setImageBitmap(resource)
                        if(collectlist[position].url.equals("null")){
                            cover.labelText="本地"
                        }
                    }
                })

//
        if(Clicklistener!=null){
            cardview.setOnClickListener {
                Clicklistener.onItemClick(collectlist[position])
            }
        }
        return view
    }

    private fun isnew(
        collectdataclass: Collectdataclass,
        cover: LabelImageView
    ) {
        try {
            gotoread(collectdataclass,context,cover)
        }catch (e:Exception){}

    }

    override fun getItem(position: Int): Any {
        return collectlist[position]
          }

    override fun getItemId(position: Int): Long {
        return position as Long
        }

    override fun getCount(): Int {
        return collectlist.size
        }
     class ViewHolder {
        var imageView: ImageView? = null
    }
}