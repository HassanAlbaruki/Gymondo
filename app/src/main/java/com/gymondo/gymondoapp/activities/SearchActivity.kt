package com.gymondo.gymondoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.gymondo.gymondoapp.Consts
import com.gymondo.gymondoapp.OnLoadMoreListener
import com.gymondo.gymondoapp.R
import com.gymondo.gymondoapp.RecyclerViewLoadMoreScroll
import com.gymondo.gymondoapp.adapters.ExerciseAdapter
import com.gymondo.gymondoapp.models.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.main_progress

class SearchActivity : AppCompatActivity() {

    lateinit var exList: ExercisesList
    lateinit var exCategory: ArrayList<Category>
    lateinit var exMuscles: ArrayList<Muscle>
    lateinit var exEquipment: ArrayList<Equipment>
    lateinit var exImages: ArrayList<Images>
    lateinit var exercisesList: ArrayList<Exercise?>
    var pagenumber: Int = 1
    var query: String = ""
    var isLastPage: Boolean = true
    lateinit var loadMoreItemsCells: ArrayList<Exercise?>
    lateinit var adapter: ExerciseAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        exercisesList = ArrayList()
        exCategory = ArrayList()
        exMuscles = ArrayList()
        exEquipment = ArrayList()
        exImages = ArrayList()
        var b:Bundle
        b= intent.extras!!
        if (b!=null) {
            exImages= b.get("images") as ArrayList<Images>
            exMuscles= b.get("muscles") as ArrayList<Muscle>
            exEquipment= b.get("equipments") as ArrayList<Equipment>
            exCategory= b.get("categories") as ArrayList<Category>
            query=b.get("query") as String
            title=query
            setRVLayoutManager()
            setRVScrollListener()
            loadData()
        }

    }

    private fun LoadMoreData() {

        if (isLastPage) {

            loadNext(pagenumber++)
            adapter.addLoadingView()
            loadMoreItemsCells = ArrayList()

        }
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(this)
        search_recycler_view.layoutManager = mLayoutManager
        search_recycler_view.setHasFixedSize(true)
    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        scrollListener =
            RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
        search_recycler_view.addOnScrollListener(scrollListener)
    }
    fun loadData() {

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://wger.de/api/v2/exercise")
            .addHeaders("token", Consts.APIKEY)
            .build()
            .getAsObject(ExercisesList::class.java, object : ParsedRequestListener<ExercisesList> {
                override fun onResponse(response: ExercisesList) {

                    exList = response
                    for (exercise in response.results) {
                        if (exercise.name!!.toLowerCase().contains(query.toLowerCase()))
                            exercisesList.add(exercise)
                    }
                    if (exercisesList.isEmpty())
                        {
                            no_data_lyout.visibility=View.VISIBLE
                            main_progress.visibility = View.GONE
                            search_data_lyout.visibility = View.GONE

                        }
                    adapter =  ExerciseAdapter( exercisesList,  exImages, exMuscles, exEquipment, exCategory )
                    adapter.notifyDataSetChanged()
                    search_recycler_view.adapter = adapter
                    main_progress.visibility = View.GONE
                    search_data_lyout.visibility = View.VISIBLE
                    search_no_connection_lyout.visibility = View.GONE
                }

                override fun onError(anError: ANError?) {
                    main_progress.visibility = View.GONE
                    search_data_lyout.visibility = View.GONE
                    search_no_connection_lyout.visibility = View.VISIBLE
                    Log.d("Error", anError.toString())
                }
            }
            )
    }

    fun loadNext(int: Int) {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get(Consts.URL + Consts.EXERCISEAPI)
            .addHeaders("token", Consts.APIKEY)
            .addQueryParameter("page", int.toString())
            .build()
            .getAsObject(ExercisesList::class.java, object : ParsedRequestListener<ExercisesList> {
                override fun onResponse(response: ExercisesList) {
                    exList = response
                    for (exercise in response.results) {
                        if (exercise.name!!.toLowerCase().contains(query.toLowerCase()))
                            loadMoreItemsCells.add(exercise)
                    }
                    //loadMoreItemsCells.addAll(response.results)

                    Handler().postDelayed({
                        adapter.removeLoadingView()
                        adapter.addData(loadMoreItemsCells)
                        scrollListener.setLoaded()
                        search_recycler_view.post { adapter.notifyDataSetChanged()
                        }
                    }, 1000)
                    if (exList.next == null)
                        isLastPage = false

                }

                override fun onError(anError: ANError?) {

                    Log.d("Error", anError.toString())
                }
            }

            )
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
