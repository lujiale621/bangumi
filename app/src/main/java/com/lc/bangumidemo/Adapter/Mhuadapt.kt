package com.lc.bangumidemo.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lc.bangumidemo.KtUtil.stdeal
import com.lc.bangumidemo.KtUtil.stnull

import com.lc.bangumidemo.R
import com.lc.bangumidemo.MyRetrofit.api.ManhuaDetailData
import java.io.IOException
import java.io.InputStream
import java.net.URL

class Mhuadapt(private val list: List<ManhuaDetailData>, private val context: Context) : RecyclerView.Adapter<Mhuadapt.ViewHolder>() {
    private var mOnItemClickListener: Mhuadapt.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.manhuacard, parent, false)
        return Mhuadapt.ViewHolder(view)
    }

    // 点击事件接口
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    fun setOnItemClickListener(mOnItemClickListener: Mhuadapt.OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }


    override fun getItemCount(): Int { return list.size }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].cover).into( holder.cover)
        holder.name.setText("漫画名称:" + stnull(list[position].name))
        holder.time.setText("更新时间:" + stnull(list[position].time))
        holder.author.setText("作者:" + stnull(list[position].author))
        holder.num.setText("最新章节:" + stdeal(list[position].latest, 8))
        holder.tag.setText("类型:" + stnull(list[position].tag))
        holder.statues.setText("状态:" + stnull(
            stdeal(
                list[position].status,
                8
            )
        )
        )
        if (mOnItemClickListener != null) {
            holder.cardView.setOnClickListener {
                mOnItemClickListener!!.onItemClick(
                    holder.itemView,
                    position
                )
            }
        }
        }
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        var cover: ImageView
        var name: TextView
        var time: TextView
        var author: TextView
        var num: TextView
        var tag: TextView
        var statues: TextView
        var cardView: CardView
        lateinit var imageUrl: String
        init {
            cover = itemView.findViewById(R.id.cover)
            name = itemView.findViewById(R.id.mhuaname)
            time = itemView.findViewById(R.id.mhuatime)
            num = itemView.findViewById(R.id.mhualatest)
            tag = itemView.findViewById(R.id.mhuatag)
            author = itemView.findViewById(R.id.mhuauthor)
            statues = itemView.findViewById(R.id.mhuastatus)
            cardView = itemView.findViewById(R.id.mahuacardview)
        }
    }
}