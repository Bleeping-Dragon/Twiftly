package com.bleepingdragon.twiftly.model

import android.widget.Button
import android.widget.TextView
import com.bleepingdragon.twiftly.R
import com.bleepingdragon.twiftly.databinding.MapMarkerWindowLayoutBinding
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkerWindow(private val mapView: MapView) :
    InfoWindow(R.layout.map_marker_window_layout, mapView) {

    override fun onOpen(item: Any?) {

        //Set up marker info
        var marker = item as Marker

        val idTextView = mView.findViewById<TextView>(R.id.markerIdTextView)
        idTextView.text = marker.id

        val titleTextView = mView.findViewById<TextView>(R.id.markerTitleTextView)
        titleTextView.text = marker.title

        // Following command
        closeAllInfoWindowsOn(mapView)
    }

    override fun onClose() {
        closeAllInfoWindowsOn(mapView)
    }
}