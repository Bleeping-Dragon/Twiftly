package com.bleepingdragon.twiftly.services

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.bleepingdragon.twiftly.R
import com.bleepingdragon.twiftly.model.CategoryOfMapPoints
import com.bleepingdragon.twiftly.model.MapPoint
import com.bleepingdragon.twiftly.model.MarkerWindow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.osmdroid.util.GeoPoint

class LocalDB {

    companion object {

        //region General

        public fun getActivityPreferences(activity: Activity): SharedPreferences {
            return activity.getPreferences(Context.MODE_PRIVATE)
        }

        public fun getString(name: String, activity: Activity): String? {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            //Custom defaults
            when (name) {
                "inputHereDefaultName" -> return sharedPref.getString(name, null)
                else -> {
                    return sharedPref.getString(name, null)
                }
            }
        }

        public fun setString(name: String, setTo: String, activity: Activity) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            //Apply setting using with to the object directly
            //https://www.develou.com/funcion-with-en-kotlin/
            with (sharedPref.edit()) {
                putString(name, setTo)
                apply()
            }
        }

        public fun getBool(name: String, activity: Activity): Boolean {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            //Custom defaults
            when (name) {
                "inputHereDefaultName" -> return sharedPref.getBoolean(name, false)
                else -> {
                    return sharedPref.getBoolean(name, true)
                }
            }
        }

        public fun setBool(name: String, setTo: Boolean, activity: Activity) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            //Apply setting using with to the object directly
            //https://www.develou.com/funcion-with-en-kotlin/
            with (sharedPref.edit()) {
                putBoolean(name, setTo)
                apply()
            }
        }

        //endregion

        //region Maps

        private var loadedCategoriesOfMapPoints: MutableList<CategoryOfMapPoints>? = null

        public fun getAllCategoriesOfMapPoints(activity: Activity): MutableList<CategoryOfMapPoints> {

            //If the categories have already been loaded, don't parse them, get the object directly
            if (loadedCategoriesOfMapPoints != null)
                return loadedCategoriesOfMapPoints as MutableList<CategoryOfMapPoints>

            //Else get them from the shared preferences
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val dataJson = sharedPref.getString("categoriesOfMapPoints", "")

            if (dataJson != null && dataJson != "") {
                var result = Json.decodeFromString<MutableList<CategoryOfMapPoints>>(dataJson)
                loadedCategoriesOfMapPoints = result
                return result

            } else {
                loadedCategoriesOfMapPoints = mutableListOf()
                return mutableListOf()
            }
        }

        public fun setAllCategoriesOfMapPoints(setTo: MutableList<CategoryOfMapPoints>, activity: Activity) {

            loadedCategoriesOfMapPoints = setTo

            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            with (sharedPref.edit()) {
                putString("categoriesOfMapPoints", Json.encodeToString(setTo))
                apply()
            }
        }

        public fun deleteMapPointFromUuid(uuid: String, activity: Activity) {

            for (category in loadedCategoriesOfMapPoints!!) {

                for (mapPoint in category.listOfMapPoints) {

                    if (mapPoint.uuid == uuid) {
                        category.listOfMapPoints.remove(mapPoint)
                        break
                    }
                }
            }
        }

        //endregion
    }
}