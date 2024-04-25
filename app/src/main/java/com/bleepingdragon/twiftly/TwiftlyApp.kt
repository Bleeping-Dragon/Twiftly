package com.bleepingdragon.twiftly

import android.app.Application
import com.google.android.material.color.DynamicColors

class TwiftlyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}