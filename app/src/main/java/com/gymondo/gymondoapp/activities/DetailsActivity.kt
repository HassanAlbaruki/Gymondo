package com.gymondo.gymondoapp.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gymondo.gymondoapp.R
import com.gymondo.gymondoapp.adapters.SliderAdapter
import com.gymondo.gymondoapp.models.Exercise
import com.gymondo.gymondoapp.models.Images
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.jsoup.Jsoup
import java.util.*

class DetailsActivity : AppCompatActivity() {

    var imagesList: List<Images> = ArrayList<Images>()
    var category=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        var b:Bundle
        b= intent.extras!!
        if (b!=null)
        {
            imagesList= b.get("images") as List<Images>
            var exercise = b.get("Exercise") as Exercise
            title=exercise.name
            tv_muscles.text= b.get("muscles").toString()
            tv_equipment.text= b.get("equipments").toString()
            ctv_category.text=b.get("category").toString()
            tv_desc.text= html2text(exercise.description)
            val adapter = SliderAdapter()
            imageSlider.setSliderAdapter(adapter)

            //we need to seach in the images array for the exercise Id then add it to the image adapter
            var index = 0
            for (image in imagesList) {
                if (exercise.id == image.exercise) {
                    adapter.addItem(Images(exercise.id!!,index, image.image))
                    index++
                }
            }
            if (index == 0) {
                imageSlider.setBackground(resources.getDrawable(R.drawable.no_image))
            }

            imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
            imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
            imageSlider.setIndicatorSelectedColor(Color.WHITE)
            imageSlider.setIndicatorUnselectedColor(Color.GRAY)
            imageSlider.setScrollTimeInSec(6)
            imageSlider.startAutoCycle()
        }
    }

    fun html2text(html: String?): String? {
        return Jsoup.parse(html).text()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
