package com.bleepingdragon.twiftly.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bleepingdragon.twiftly.PlantsCarePage
import com.bleepingdragon.twiftly.R
import com.bleepingdragon.twiftly.databinding.PlantCareItemLayoutBinding
import com.bleepingdragon.twiftly.model.PlantCareItem
import com.bleepingdragon.twiftly.services.LocalDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

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
            holder.itemBinding.plantCareItemDay7,
            holder.itemBinding.plantCareItemDay8,
            holder.itemBinding.plantCareItemDay9,
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

        //Register camera icon or image tap to take a new photo of the plant
        holder.itemBinding.plantImageFrameLayout.setOnClickListener {

            //Calls a contract inside the fragment to take a photo, as it cannot be called from here
            plantsCarePage.takePhoto(holder.itemBinding.plantImageView, plantsCareList[position].uuid)
        }

        //Set image if the item has an existing image in the path
        val photo = File(context.filesDir, "plants_care_photos" + File.separator + plantsCareList[position].uuid + ".png")

        if (photo.exists()) {
            var temporalUri = FileProvider.getUriForFile(context, "com.bleepingdragon.twiftly.FileProvider", photo)
            holder.itemBinding.plantImageView.setImageURI(temporalUri)
            holder.itemBinding.cameraSymbolImageView.visibility = View.GONE
        }

        //Card buttons logic: delete, edit, water
        holder.itemBinding.deletePlantButton.setOnClickListener {

            MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.modal_delete_plant_title))
                .setMessage(context.getString(R.string.modal_delete_plant_description, plantsCareList[position].name))
                .setNeutralButton(context.getString(R.string.cancel)) { dialog, which ->
                }
                .setPositiveButton(context.getString(R.string.delete)) { dialog, which ->
                    deletePlantsCareItem(plantsCareList[position])
                }
                .show()

        }

        holder.itemBinding.editPlantButton.setOnClickListener {

        }

        holder.itemBinding.waterPlantButton.setOnClickListener {

            //Water the plant
            plantsCareList[position].lastWateringDate = LocalDateTime.now().toString()

            val message = context.getString(R.string.watered_plant, plantsCareList[position].name)
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()

            disableIfWatered(plantsCareList[position], holder.itemBinding)
        }

        disableIfWatered(plantsCareList[position], holder.itemBinding)
    }

    //Disable the watering button if the plant has been already watered the day
    private fun disableIfWatered(plantObject: PlantCareItem, layoutBinding: PlantCareItemLayoutBinding) {

        var dateNow: LocalDate = LocalDateTime.now().toLocalDate()
        var dateLastWatered: LocalDate = LocalDateTime.parse(plantObject.lastWateringDate).toLocalDate()

        //Check only the day, month and years, not hours, minutes, etc...
        if (dateNow == dateLastWatered) {
            layoutBinding.waterPlantButton.isEnabled = false
            layoutBinding.waterPlantButton.text = context.getString(R.string.already_watered)
        } else {
            layoutBinding.waterPlantButton.isEnabled = true
            layoutBinding.waterPlantButton.text = context.getString(R.string.water_plant)
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

            //Delete the associated image file if its exits
            val photo = File(context.filesDir, "plants_care_photos" + File.separator + item.uuid + ".png")
            if (photo.exists()) photo.delete()

            Toast.makeText(context, R.string.toast_plant_deleted, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = plantsCareList.size
}