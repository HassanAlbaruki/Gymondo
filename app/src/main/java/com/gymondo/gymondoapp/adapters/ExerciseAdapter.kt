package com.gymondo.gymondoapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gymondo.gymondoapp.Consts
import com.gymondo.gymondoapp.activities.DetailsActivity
import com.gymondo.gymondoapp.R
import com.gymondo.gymondoapp.models.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.new_item.view.*
import java.io.Serializable


class ExerciseAdapter(private var exersiceList: ArrayList<Exercise?>,private val imageList:ArrayList<Images>,private val muscleList:ArrayList<Muscle>,private val equipmentList:ArrayList<Equipment>,private val categoryList:ArrayList<Category>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var mcontext: Context
    var randomInteger=0
    var itemMuscles=""
    var itemEquipments=""
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<Exercise?>) {
        this.exersiceList.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }
    private fun remove(r: Exercise) {
        val position: Int = exersiceList.indexOf(r)
        if (position > -1) {
            exersiceList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): Exercise {
        return exersiceList.get(position)!!
    }
    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun addLoadingView() {
        Handler().post {
            exersiceList.add(null)
            notifyItemInserted(exersiceList.size - 1)
        }
    }

    fun removeLoadingView() {
        if (exersiceList.size != 0) {
            exersiceList.removeAt(exersiceList.size - 1)
            notifyItemRemoved(exersiceList.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == Consts.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.new_item, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.loading_layout, parent, false)
            LoadingViewHolder(
                view
            )
        }
    }

    override fun getItemCount(): Int {
        return exersiceList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (exersiceList[position] == null) {
            Consts.VIEW_TYPE_LOADING
        } else {
            Consts.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val exersice=exersiceList.get(position)
        if (holder.itemViewType == Consts.VIEW_TYPE_ITEM) {
            holder.itemView.setOnClickListener(){
                val intent = Intent(mcontext, DetailsActivity::class.java)
                intent.putExtra("Exercise", exersiceList.get(position) as Serializable)
                intent.putExtra("images", imageList as Serializable)
                intent.putExtra("muscles", holder.itemView.ex_muscles.getText())
                intent.putExtra("equipments", holder.itemView.ex_equipments.getText())
                intent.putExtra("category", holder.itemView.ex_category.getText())
                mcontext.startActivity(intent)
            }
            if(exersiceList[position]!!.name!="")
                holder.itemView.ex_name.text = exersiceList[position]!!.name
            else
                holder.itemView.ex_name.text = "NO NAME"
            holder.itemView.ly_background.setBackgroundColor(getColor())
            for (category in categoryList)
                if (category!!.id==exersiceList[position]!!.category)
                {
                    holder.itemView.ex_category.text=category!!.name
                    break
                }

            for (image in imageList)
                if (image.exercise==exersiceList[position]!!.id)
                {
                    Picasso.get().load(image.image).into(holder.itemView.img_item)
                    break
                }
            else
                    Picasso.get().load(R.drawable.no_image).into(holder.itemView.img_item)

            itemMuscles=""
                 for (muscle in muscleList)
                     if (exersiceList[position]!!.muscles!!.isNotEmpty()) {
                         if (exersiceList[position]!!.muscles!!.contains(muscle.id)) {
                             itemMuscles += muscle.name + ", "

                         }

                     }

            if(itemMuscles.equals(""))
                holder.itemView.ex_muscles.text="No Muscles"
            else
             holder.itemView.ex_muscles.text=itemMuscles
             itemEquipments=""
                 for (equipment in equipmentList)
                     if (exersiceList[position]!!.equipment!!.isNotEmpty()) {
                         if (exersiceList[position]!!.equipment!!.contains(equipment.id)) {
                             itemEquipments += equipment.name + ", "

                         }
                     }
            if(itemEquipments.equals(""))
                holder.itemView.ex_equipments.text="No Equipments"
            else
             holder.itemView.ex_equipments.text=itemEquipments

        }
    }
    fun getColor():Int{
        val colorList = ArrayList<Int>()
        colorList.add(mcontext.resources.getColor(R.color.blue))
        colorList.add(mcontext.resources.getColor(R.color.green))
        colorList.add(mcontext.resources.getColor(R.color.purble))
        colorList.add(mcontext.resources.getColor(R.color.yellow))
        randomInteger++
        if (randomInteger==4)
            randomInteger=0
        return colorList.get(randomInteger)
    }
}