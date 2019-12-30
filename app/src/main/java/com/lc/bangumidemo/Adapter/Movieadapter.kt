package com.lc.bangumidemo.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lc.bangumidemo.KtUtil.stnull
import com.lc.bangumidemo.MyRetrofit.api.DetailResult
import com.lc.bangumidemo.R
import com.ramotion.foldingcell.FoldingCell

class Movieadapter(var list:MutableList<DetailResult>,private val context: Context): RecyclerView.Adapter<Movieadapter.ViewHolder>() {
        private lateinit var onMovieAdapterChristened:onMovieAdapterChecklists
    interface onMovieAdapterChecklists{
        fun onMovieItemClick(
            detailResult: DetailResult,
            posi: Int
        )
    }
    open fun setonMovieAdapterClicklistener(onAdapterClicklistener:onMovieAdapterChecklists){
        onMovieAdapterChristened=onAdapterClicklistener
    }
    private lateinit var Clicklistener:onMovieClicklistener
    interface onMovieClicklistener{
        fun onItemClick(cell: FoldingCell)
    }
    open fun setOnMovieClicklistener(onClicklistener:onMovieClicklistener){
        Clicklistener=onClicklistener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movieitem, parent, false)
        println(parent)
        return Movieadapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Glide.with(context).load(list[position].data.cover).into(holder.cover)
        }catch (e:Exception){Log.e("加载不到图片",e.toString())}
        try{
        holder.moviename.setText("影视:" + stnull(list[position].data.name))
        holder.director.setText("导演:" + stnull(list[position].data.director))
        holder.performer.setText("演员:" + stnull(list[position].data.performer))
        holder.region.setText("国家:" + stnull(list[position].data.region))
        holder.movielanguage.setText("语言:" + stnull(list[position].data.Language))
        holder.genre.setText("类型:" + stnull(list[position].data.genre))
        holder.movietime.setText("更新时间:" + stnull(list[position].data.time))
        holder.release.setText("年代:" + stnull(list[position].data.Release))
            var templist = mutableListOf<String>()
            for(i in list[position].list)
            {
                templist.add(i.num)
            }
            var adapter: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.textviewfordetail, templist)
            holder.moviegridlist.adapter=adapter
                       if(onMovieAdapterChristened!=null){
                           try{
                               holder.moviegridlist.setOnItemClickListener(object:AdapterView.OnItemClickListener{
                                   override fun onItemClick(
                                       parent: AdapterView<*>?,
                                       view: View?,
                                       posi: Int,
                                       id: Long
                                   ) {
                                       onMovieAdapterChristened.onMovieItemClick(list[position],posi)
                                   }
                               })
                           }catch (e:Exception){}
            }
            if(Clicklistener!=null)
        {
            holder.moviecardview.setOnClickListener {
                Clicklistener.onItemClick(holder.cell)
            }
        }
        }catch (e:Exception){
            Log.e("数据为空",e.toString())
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cover: ImageView
        var moviename: TextView
        var director: TextView
        var performer: TextView
        var region: TextView
        var movielanguage: TextView
        var genre: TextView
        var movietime: TextView
        var release: TextView
        var moviecardview:CardView
        var moviegridlist:GridView
        lateinit var cell: FoldingCell
        init {
            cover = itemView.findViewById(R.id.moviecover)
            director = itemView.findViewById(R.id.director)
            moviename = itemView.findViewById(R.id.moviename)
            performer = itemView.findViewById(R.id.performer)
            region = itemView.findViewById(R.id.region)
            movielanguage = itemView.findViewById(R.id.movielanguage)
            genre = itemView.findViewById(R.id.genre)
            release = itemView.findViewById(R.id.release)
            moviegridlist=itemView.findViewById(R.id.moviegridlist)
            movietime = itemView.findViewById(R.id.movietime)
            moviecardview=itemView.findViewById(R.id.moviecardview)
            cell=itemView.findViewById(R.id.moviecell)
        }
    }
}