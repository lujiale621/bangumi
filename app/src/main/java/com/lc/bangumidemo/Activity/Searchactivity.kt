package com.lc.bangumidemo.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lc.bangumidemo.Adapter.Mhuadapt
import com.lc.bangumidemo.Adapter.Movieadapter
import com.lc.bangumidemo.Adapter.Recadapt
import com.lc.bangumidemo.KtUtil.*
import com.lc.bangumidemo.MyRetrofit.ResClass.BookResult
import com.lc.bangumidemo.MyRetrofit.Retrofit.Retrofitcall
import com.lc.bangumidemo.MyRetrofit.api.*
import com.lc.bangumidemo.R
import com.lc.bangumidemo.RxBus.RxBus
import com.lc.bangumidemo.RxBus.RxBusBaseMessage
import com.ramotion.foldingcell.FoldingCell
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.search.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

class Searchactivity : BaseActivity() {
    lateinit var searchItem: MenuItem
    lateinit var searchView: SearchView
    override fun setRes(): Int {
        return R.layout.search
    }

    override fun initview() {
        super.initview()
        setSupportActionBar(toolbar_search)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        Rxrecive(11)//监听视频加载
        anmo.hide()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initsearchlistener() {
        //

        searchView = searchItem.actionView as SearchView
        searchView!!.isSubmitButtonEnabled = false // 提交按钮
        searchView!!.queryHint = "点击搜索相关内容"
        searchView!!.onActionViewExpanded()
        searchView!!.isIconified = false
        searchView!!.clearFocus() // 收起键盘
        //
        val bundle = this.intent.extras //读取intent的数据给bundle对象
        val tag = bundle!!.getString("tag") //通过key得到value
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                anmo.show()
                initadapt(tag!!)
                when (tag) {
                    "小说" -> {
                        clearlist()
                        searchbook(query)//查找书本
                    }
                    "影视" -> {
                        clearlist()
                        searchmovie(query)
                    }
                    "漫画" -> {
                        clearlist()
                        searchmhua(query)
                    }
                    "综合" -> {
                    }
                }

                searchView!!.clearFocus() // 收起键盘
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun Rxrecive(code: Int) {
        RxBus.getInstance()
            .tObservable(code, RxBusBaseMessage::class.java)
            .bindUntilEvent(this, Lifecycle.Event.ON_DESTROY)
            .subscribe(object : Observer<RxBusBaseMessage> {
                override fun onError(e: Throwable) {

                }

                override fun onNext(t: RxBusBaseMessage) {
                    if (t!!.code == 11) {
                        Log.e("RXJAVA", "加载moviedetail")
                        searchmoviedetail(t.`object` as DetailData)
                    }
                }

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }
            })
    }


    private fun searchmoviedetail(data: DetailData?) {
        val movieLoader = MovieLoader()
        if (data != null) {
            movieLoader.getMovieDetail(data.url).subscribe(object : HttpObserver<DetailResult>() {
                override fun onSuccess(t: DetailResult?) {
                    t?.let { movielists.add(it) }
                    (listview.adapter as Movieadapter).notifyDataSetChanged()
                }

                override fun onError(code: Int, msg: String?) {
                    super.onError(code, msg)
                    anmo.hide()
                    Toast.makeText(this@Searchactivity,"网络连接失败~",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun searchmovie(query: String?) {
        val movieLoader = MovieLoader()
        if (query != null) {
            movieLoader.getMovies(query).subscribe(object : HttpObserver<SearchResult>() {
                override fun onSuccess(t: SearchResult?) {
                    if(t!!.list==null){
                        anmo.hide()
                        Toast.makeText(this@Searchactivity,"搜索不到相关资源~",Toast.LENGTH_SHORT).show()
                        return
                    }
                    for (i in t!!.list) {
                        RxBus.getInstance().send(11, RxBusBaseMessage(11, i))
                    }

                    anmo.hide()
                }

                override fun onError(code: Int, msg: String?) {
                    super.onError(code, msg)
                    anmo.hide()
                    Toast.makeText(this@Searchactivity,"网络连接失败~",Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun clearlist() {
        movielists.clear()
        mhualists.clear()
        novelists.clear()     //清除缓存
    }

    private fun searchmhua(query: String?) {
        var manhuaLoader: ManhuaLoader = ManhuaLoader()
        if (query != null) {
            manhuaLoader.getManhua(query).subscribe(object : HttpObserver<ManhuaSearchResult>() {
                override fun onSuccess(t: ManhuaSearchResult?) {
                    if(t!!.list==null){
                        anmo.hide()
                        Toast.makeText(this@Searchactivity,"搜索不到相关资源~",Toast.LENGTH_SHORT).show()
                        return
                    }
                    try {
                        for (i in t!!.list) {
                            mhualists.add(i)
                        }
                        (listview.adapter as Mhuadapt).notifyDataSetChanged()
                        anmo.hide()
                    } catch (e: Exception) {
                        Log.e("searchmanhua", "加载数据异常")
                        anmo.hide()
                    }
                }

                override fun onError(code: Int, msg: String?) {
                    super.onError(code, msg)
                        anmo.hide()
                        Toast.makeText(this@Searchactivity,"网络连接失败~",Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menusearch, menu)
        searchItem = menu!!.findItem(R.id.action_search)
        initsearchlistener()
        return super.onCreateOptionsMenu(menu)
    }

    fun initadapt(tag: String) {
        when (tag) {
            "小说" -> {
                //初始化小说适配器
                clearlist()
                var adapt = Recadapt(novelists, this)
                initlistener(adapt)
                listview.setItemViewCacheSize(50)
                listview.setLayoutManager(LinearLayoutManager(this@Searchactivity))
                listview.adapter = adapt
            }
            "影视" -> {
                clearlist()
                var adapt = Movieadapter(movielists, this)
                initlistener(adapt)
                listview.setItemViewCacheSize(50)
                listview.setLayoutManager(LinearLayoutManager(this@Searchactivity))
                listview.adapter = adapt
            }
            "漫画" -> {
                //初始化漫画适配器
                clearlist()
                var adapt = Mhuadapt(mhualists, this)
                initlistener(adapt)
                listview.setItemViewCacheSize(50)
                listview.setLayoutManager(LinearLayoutManager(this@Searchactivity))
                listview.adapter = adapt
            }
            "综合" -> {
            }

            //初始化漫画适配器

        }
    }

    private fun initlistener(adapter: Any) {
        if (adapter is Movieadapter) {
            adapter.setOnMovieClicklistener(object : Movieadapter.onMovieClicklistener {
                override fun onItemClick(cell: FoldingCell) {
                    cell.toggle(false)
                }
            })
            adapter.setonMovieAdapterClicklistener(object : Movieadapter.onMovieAdapterChecklists {
                override fun onMovieItemClick(detailResult: DetailResult, posi: Int) {
                    try {
                        var num: String = stnull(detailResult.list[posi].num)
                        var url: String = stnull(detailResult.list[posi].url)
                        var download: String = stnull(detailResult.list[posi].download)
                        var m3u8url: String = stnull(detailResult.list[posi].m3u8url)
                        var onlineurl: String = stnull(detailResult.list[posi].onlineurl)
                        var name: String = stnull(detailResult.list[posi].name)
                        moviesource = DetailList(m3u8url, onlineurl, download, num, name, url)
                        startActivity<MoviePlayerActivity>()
                    } catch (e: Exception) {
                    }
                }
            })
        }
        if (adapter is Recadapt) {
            adapter.setOnItemClickListener(object : Recadapt.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    lockscreen(true)
                    var urls = novelists[position].url
                    if (urls != null) {
                        var start = Intent(this@Searchactivity, BookDetailActivity::class.java)
                        var bundle = Bundle()
                        bundle.putString("url", urls)
                        bundle.putInt("position", position)
                        start.putExtras(bundle)
                        startActivity(start)
                    } else {
                        var intent = Intent(this@Searchactivity, ErrorActivity::class.java)
                        intent.putExtra("msg", "连接失败，请检查你的网络")
                        intent.putExtra("tag", "Search_Activity")
                        anmo.hide()
                        startActivity(intent)
                    }
                }
            })
        }
        if (adapter is Mhuadapt) {
            adapter.setOnItemClickListener(object : Mhuadapt.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    lockscreen(true)
                    var urls = mhualists[position].url
                    if (urls != null) {
                        var start = Intent(this@Searchactivity, MhuaDetailActivity::class.java)
                        var bundle = Bundle()
                        bundle.putString("url", urls)
                        bundle.putInt("position", position)
                        start.putExtras(bundle)
                        startActivity(start)
                    } else {
                        var intent = Intent(this@Searchactivity, ErrorActivity::class.java)
                        intent.putExtra("msg", "连接失败，请检查你的网络")
                        intent.putExtra("tag", "Search_Activity")
                        anmo.hide()
                        startActivity(intent)
                    }
                }
            })
        }


    }

    fun searchbook(name: String?) {
        val mHamdler1 = object : Handler() {

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    2 -> {
                        try {
                            var result = msg.obj as BookResult
                            if (result != null) {
                                anmo.hide()
                                if (result.list==null){throw NullPointerException()}
                                getbookdata(result)
                            }
                        } catch (e: Exception) {
                            anmo.hide()
                            Toast.makeText(this@Searchactivity,"搜索不到相关资源~",Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
        Thread(Runnable {
            var message = Message()
            val call = name?.let { Retrofitcall().getAPIService().getCall(it) }
            if (call != null) {
                call.enqueue(object : Callback<BookResult> {
                    override fun onResponse(
                        call: Call<BookResult>,
                        response: Response<BookResult>
                    ) {
                        val st = response.body()
                        println(st)
                        message.obj = st
                        message.what = 2
                        mHamdler1.sendMessage(message)
                    }

                    override fun onFailure(call: Call<BookResult>, t: Throwable) {
                        anmo.hide()
                        Toast.makeText(this@Searchactivity,"网络连接失败~",Toast.LENGTH_SHORT).show()
                        message.obj = null
                        message.what = 2
                        mHamdler1.sendMessage(message)
                    }
                })
            }
        }).start()
    }

    fun getbookdata(result: BookResult) {

        val hand = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {

                    3 -> {
                        try {
                            for (i in result!!.list) {
                                novelists.add(i)
                            }
                            (listview.adapter as Recadapt).notifyDataSetChanged()
                        } catch (e: Exception) {
                            var intent =
                                Intent(this@Searchactivity, ErrorActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                )
                            intent.putExtra("msg", "无效的书源")
                            intent.putExtra("tag", "Search_Activity")
                            anmo.hide()
                            startActivity(intent)
                        }

                    }
                }
            }
        }
        Thread(Runnable {
            hand.sendEmptyMessage(3)
        }).start()
    }

    override fun onRestart() {
        super.onRestart()
    }
}