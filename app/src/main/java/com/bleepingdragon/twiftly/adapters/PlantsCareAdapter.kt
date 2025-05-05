package com.bleepingdragon.twiftly.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bleepingdragon.twiftly.PlantsCarePage
import com.bleepingdragon.twiftly.databinding.PlantCareItemLayoutBinding
import com.bleepingdragon.twiftly.model.PlantCareItem
import com.bleepingdragon.twiftly.services.LocalDB

class PlantsCareAdapater (var plantsCareList: MutableList<PlantCareItem>, var context: Context, var plantsCarePage: PlantsCarePage)
    : RecyclerView.Adapter<PlantsCareAdapater.MyView>() {

    inner class MyView(val itemBinding: PlantCareItemLayoutBinding): RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(PlantCareItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {

    }

    private fun deletePlantsCareItem(item: PlantCareItem) {

        val index = plantsCareList.indexOf(item)

        if (index != -1) {

            //Remove from the list first
            plantsCareList.removeAt(index)

            //Notify the adapter of the change
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, plantsCareList.size) //Notify the adapter of the change after the items of the one removed

            //Delete from the database
            LocalDB.deletePlantsCareItemFromUuid(item.uuid, context as Activity)

            Toast.makeText(context, "Plant care item deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = plantsCareList.size
}