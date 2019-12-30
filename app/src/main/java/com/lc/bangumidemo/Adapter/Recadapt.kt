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
import com.lc.bangumidemo.MyRetrofit.ResClass.Bookdata
import com.lc.bangumidemo.R
import java.io.IOException
import java.io.InputStream
import java.net.URL


class Recadapt(private val list: List<Bookdata>, private val context: Context) : RecyclerView.Adapter<Recadapt.ViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carditem, parent, false)
        println(parent)
        return ViewHolder(view)
    }

    // 点击事件接口
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].cover).into( holder.cover)
        holder.name.setText("书名:" + stnull(list[position].name))
        holder.time.setText("更新时间:" + stnull(list[position].time))
        holder.author.setText("作者:" + stnull(list[position].author))
        holder.num.setText("最新章节:" + stdeal(list[position].num, 8))
        holder.tag.setText("类型:" + stnull(list[position].tag))
        holder.statues.setText("状态:" + stnull(stdeal(list[position].status, 8)))
        if (mOnItemClickListener != null) {
            holder.cardView.setOnClickListener {
                mOnItemClickListener!!.onItemClick(
                    holder.itemView,
                    position
                )
            }
        }
    }
    override fun getItemCount(): Int {
        println("COUNT:" + list.size)
        return list.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            name = itemView.findViewById(R.id.name)
            time = itemView.findViewById(R.id.time)
            num = itemView.findViewById(R.id.num)
            tag = itemView.findViewById(R.id.tag)
            author = itemView.findViewById(R.id.author)
            statues = itemView.findViewById(R.id.status)
            cardView = itemView.findViewById(R.id.cardview)
        }
    }




}