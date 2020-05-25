package com.gymondo.gymondoapp.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gymondo.gymondoapp.R
import com.gymondo.gymondoapp.models.Images
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import java.util.*


class SliderAdapter() : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private val mSliderItems: MutableList<Images> = ArrayList<Images>()
    fun addItem(sliderItem: Images) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout, null)
        return SliderAdapterVH(
            inflate
        )
    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterVH,
        position: Int
    ) {
        val sliderItem: Images = mSliderItems[position]
        viewHolder.textViewDescription.textSize = 16f
        viewHolder.textViewDescription.setTextColor(Color.WHITE)
        if (sliderItem.image.equals("")) Picasso.get().load(R.drawable.no_image)
            .into(viewHolder.imageViewBackground) else Picasso.get().load(sliderItem.image)
            .into(viewHolder.imageViewBackground)
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

     class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        var imageViewBackground: ImageView
        var imageGifContainer: ImageView
        var textViewDescription: TextView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container)
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
        }
    }

}