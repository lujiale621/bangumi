package com.lc.bangumidemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.lc.bangumidemo.R
import com.wang.avi.AVLoadingIndicatorView
import org.w3c.dom.Text


class Manhuareadadapter(private val list: MutableList<String>, private val context: Context) : RecyclerView.Adapter<Manhuareadadapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Manhuareadadapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.manhuaitem, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        print(list.size)
        return list.size
    }
    override fun onBindViewHolder(holder: Manhuareadadapter.ViewHolder, position: Int) {
        Glide.with(context).load(list[position]).into( holder.img)
    }
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        var img: ImageView
        lateinit var anim : AVLoadingIndicatorView
        init {
            img = itemView.findViewById(R.id.manhuaimageView)
            anim=itemView.findViewById(R.id.manhuaanim)
        }
    }

}