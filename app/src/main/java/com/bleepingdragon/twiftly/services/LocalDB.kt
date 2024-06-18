package com.bleepingdragon.twiftly.services

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class LocalDB {

    companion object {

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
    }
}