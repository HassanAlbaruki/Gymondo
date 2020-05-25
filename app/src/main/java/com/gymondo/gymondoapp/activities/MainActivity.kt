package com.gymondo.gymondoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.gymondo.gymondoapp.Consts
import com.gymondo.gymondoapp.R
import com.gymondo.gymondoapp.models.ExerciseImageList
import com.gymondo.gymondoapp.models.Images
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var imagePagenumber: Int = 1
    lateinit var exImages: ArrayList<Images>
    lateinit var getStart :Button
    lateinit var ttb1 : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.NoActionBar)
        setContentView(R.layout.activity_main)
        val ttb= AnimationUtils.loadAnimation(this,
            R.anim.ttb
        );
        val stb= AnimationUtils.loadAnimation(this,
            R.anim.stb
        );
        ttb1= AnimationUtils.loadAnimation(this,
            R.anim.ttb1
        )
        val logo =findViewById(R.id.iv_logo) as ImageView
        val titleB=findViewById(R.id.tv_big) as TextView
        val titleS=findViewById(R.id.tv_small) as TextView
        getStart = findViewById(R.id.bt_start)
        getStart.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("images",exImages)
            startActivity(intent)
        }
        logo.startAnimation(ttb)
        titleB.startAnimation(stb)
        titleS.startAnimation(stb)
        exImages = ArrayList()
        loadImages()

    }
    private fun loadImages() {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get(Consts.URL + Consts.IMAGESAPI)
            .addHeaders("token", Consts.APIKEY)
            .addQueryParameter("page", imagePagenumber.toString())
            .build()
            .getAsObject(
                ExerciseImageList::class.java,
                object : ParsedRequestListener<ExerciseImageList> {
                    override fun onResponse(response: ExerciseImageList) {
                        exImages.addAll(response.results)
                        if (response.next != null) {
                            imagePagenumber++
                            loadImages()
                        } else
                        {
                            animationView.visibility=View.GONE
                            getStart.visibility= View.VISIBLE
                            getStart.startAnimation(ttb1)

                        }

                    }

                    override fun onError(anError: ANError?) {

                        Log.d("Error", anError.toString())
                    }
                }

            )

    }

}
