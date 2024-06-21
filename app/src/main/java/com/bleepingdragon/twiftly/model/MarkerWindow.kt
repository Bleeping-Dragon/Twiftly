package com.bleepingdragon.twiftly.model

import android.widget.Button
import com.bleepingdragon.twiftly.R
import com.bleepingdragon.twiftly.databinding.MapMarkerWindowLayoutBinding
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkerWindow(private val mapView: MapView) :
    InfoWindow(R.layout.map_marker_window_layout, mapView) {

    override fun onOpen(item: Any?) {

        // Following command
        closeAllInfoWindowsOn(mapView)
    }

    override fun onClose() {
        closeAllInfoWindowsOn(mapView)
    }
}