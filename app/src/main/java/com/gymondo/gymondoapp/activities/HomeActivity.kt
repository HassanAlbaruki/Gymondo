package com.gymondo.gymondoapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.gymondo.gymondoapp.Consts.Companion.APIKEY
import com.gymondo.gymondoapp.Consts.Companion.CATEGORYAPI
import com.gymondo.gymondoapp.Consts.Companion.EQUIPMENTAPI
import com.gymondo.gymondoapp.Consts.Companion.EXERCISEAPI
import com.gymondo.gymondoapp.Consts.Companion.MUSCLEAPI
import com.gymondo.gymondoapp.Consts.Companion.URL
import com.gymondo.gymondoapp.OnLoadMoreListener
import com.gymondo.gymondoapp.R
import com.gymondo.gymondoapp.RecyclerViewLoadMoreScroll
import com.gymondo.gymondoapp.adapters.ExerciseAdapter
import com.gymondo.gymondoapp.models.*
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    var currentCategory: String? = null
    lateinit var exCategory: ArrayList<Category>
    lateinit var exMuscles: ArrayList<Muscle>
    lateinit var exEquipment: ArrayList<Equipment>
    lateinit var exImages: ArrayList<Images>
    lateinit var exercisesList: ArrayList<Exercise?>
    lateinit var exList: ExercisesList
    var pagenumber: Int = 1
    var isLastPage: Boolean = true
    lateinit var loadMoreItemsCells: ArrayList<Exercise?>
    lateinit var adapter: ExerciseAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar()!!.setElevation(0F);
        setTitle("Exercises List")
        setContentView(R.layout.activity_home)

        exercisesList = ArrayList()
        exCategory = ArrayList()
        exMuscles = ArrayList()
        exEquipment = ArrayList()
        exImages = ArrayList()

        var b:Bundle
        b= intent.extras!!
        if (b!=null) {
            exImages= b.get("images") as ArrayList<Images>
        }
        loadCategories()
        loadMuscles()
        loadEquipments()
        loadData()

        setRVLayoutManager()
        setRVScrollListener()
        iv_filter.setOnClickListener {
            val popupMenu = PopupMenu(this, iv_filter)
            for (category in exCategory) {
                popupMenu.getMenu().add(0, category.id, 0, category.name);
            }
            popupMenu.getMenu().add(0, 0, 0, "All");

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                currentCategory= item!!.itemId.toString()
                if (currentCategory.equals("0"))
                    currentCategory=null
                if (!adapter.isEmpty())
                {
                    adapter.removeLoadingView()
                    adapter.clear()}
                pagenumber = 1
                if (!isLastPage)
                    setRVScrollListener()
                tv_filter.text=item!!.title
                isLastPage=true
                loadData()
                true
            })
            popupMenu.show()
        }
        retry.setOnClickListener{
            if (exCategory.isEmpty())
            loadCategories()
            if (exMuscles.isEmpty())
            loadMuscles()
            if (exEquipment.isEmpty())
            loadEquipments()
            loadData()

        }
    }

    private fun loadEquipments() {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get(URL + EQUIPMENTAPI)
            .addHeaders("token", APIKEY)
            .build()
            .getAsObject(EquipmentList::class.java, object : ParsedRequestListener<EquipmentList> {
                override fun onResponse(response: EquipmentList) {
                    exEquipment.addAll(response.results)
                }

                override fun onError(anError: ANError?) {

                    Log.d("Error", anError.toString())
                }
            }


            )

    }
    private fun loadMuscles() {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get(URL + MUSCLEAPI)
            .addHeaders("token", APIKEY)
            .build()
            .getAsObject(MuscleList::class.java, object : ParsedRequestListener<MuscleList> {
                override fun onResponse(response: MuscleList) {
                    exMuscles.addAll(response.results)
                }

                override fun onError(anError: ANError?) {

                    Log.d("Error", anError.toString())
                }
            }

            )

    }
    private fun loadCategories() {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get(URL + CATEGORYAPI)
            .addHeaders("token", APIKEY)
            .build()
            .getAsObject(CategoryList::class.java, object : ParsedRequestListener<CategoryList> {
                override fun onResponse(response: CategoryList) {
                    exCategory.addAll(response.results)
                }

                override fun onError(anError: ANError?) {
                    Log.d("Error", anError.toString())
                }
            }

            )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return if (item.itemId == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onBackPressed() {
        finish()
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
        recycler_view.layoutManager = mLayoutManager
        recycler_view.setHasFixedSize(true)
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
        recycler_view.addOnScrollListener(scrollListener)
    }

    fun loadData() {

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://wger.de/api/v2/exercise")
            .addHeaders("token", APIKEY)
            .addQueryParameter("category", currentCategory)
            .build()
            .getAsObject(ExercisesList::class.java, object : ParsedRequestListener<ExercisesList> {
                override fun onResponse(response: ExercisesList) {
                    exList = response
                    exercisesList.addAll(response.results)
                    adapter = ExerciseAdapter( exercisesList,  exImages, exMuscles, exEquipment, exCategory )
                    adapter.notifyDataSetChanged()
                    recycler_view.adapter = adapter
                    main_progress.visibility = View.GONE
                    data_lyout.visibility = View.VISIBLE
                    no_connection_lyout.visibility = View.GONE
                }

                override fun onError(anError: ANError?) {
                    data_lyout.visibility = View.GONE
                    no_connection_lyout.visibility = View.VISIBLE
                    Log.d("Error", anError.toString())
                }
            }
            )
    }

    fun loadNext(int: Int) {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get(URL + EXERCISEAPI)
            .addHeaders("token", APIKEY)
            .addQueryParameter("page", int.toString())
            .addQueryParameter("category", currentCategory)
            .build()
            .getAsObject(ExercisesList::class.java, object : ParsedRequestListener<ExercisesList> {
                override fun onResponse(response: ExercisesList) {
                    exList = response
                    loadMoreItemsCells.addAll(response.results)

                    Handler().postDelayed({
                        adapter.removeLoadingView()
                        adapter.addData(loadMoreItemsCells)
                        scrollListener.setLoaded()
                        recycler_view.post {
                            adapter.notifyDataSetChanged()
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(this,query,Toast.LENGTH_LONG)
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("images",exImages)
        intent.putExtra("muscles",exMuscles)
        intent.putExtra("equipments",exEquipment)
        intent.putExtra("categories",exCategory)
        intent.putExtra("query",query)
        startActivity(intent)
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}
