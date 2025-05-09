package com.bleepingdragon.twiftly.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bleepingdragon.twiftly.PlantsCarePage
import com.bleepingdragon.twiftly.R
import com.bleepingdragon.twiftly.databinding.PlantCareItemLayoutBinding
import com.bleepingdragon.twiftly.model.PlantCareItem
import com.bleepingdragon.twiftly.services.LocalDB
import java.time.LocalDate

class PlantsCareAdapter (var plantsCareList: MutableList<PlantCareItem>, var context: Context, var plantsCarePage: PlantsCarePage)
    : RecyclerView.Adapter<PlantsCareAdapter.MyView>() {

    inner class MyView(val itemBinding: PlantCareItemLayoutBinding): RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(PlantCareItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {

        //Set the name and notes (hide the notes if there are none)
        holder.itemBinding.plantNameTextView.text = plantsCareList[position].name

        val notes = plantsCareList[position].notes

        if (notes.isEmpty()) {
            holder.itemBinding.plantNotesTextView.visibility = View.GONE
        } else {
            holder.itemBinding.plantNotesTextView.visibility = View.VISIBLE
            holder.itemBinding.plantNotesTextView.text = plantsCareList[position].notes
        }

        //Set the normal days and the days of watering
        val dayViews = listOf(
            holder.itemBinding.plantCareItemDay0,
            holder.itemBinding.plantCareItemDay1,
            holder.itemBinding.plantCareItemDay2,
            holder.itemBinding.plantCareItemDay3,
            holder.itemBinding.plantCareItemDay4,
            holder.itemBinding.plantCareItemDay5,
            holder.itemBinding.plantCareItemDay6,
        )

        var wateringDays = plantsCareList[position].getWateringSchedule(10)

        var lastMonthLabel: String = ""

        dayViews.forEach {

            var currentMonth = wateringDays[dayViews.indexOf(it)].month.toString()

            //Only show the month indicator in the first displayed day of that month
            if (lastMonthLabel == "" || currentMonth != lastMonthLabel) {
                lastMonthLabel = currentMonth
                it.monthTextView.text = currentMonth
                it.monthTextView.visibility = View.VISIBLE
            } else {
                it.monthTextView.visibility = View.GONE
            }

            it.dayTextView.text = wateringDays[dayViews.indexOf(it)].dayOfMonth.toString()
        }
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