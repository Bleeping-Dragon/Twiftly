package com.bleepingdragon.twiftly.model

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bleepingdragon.twiftly.R
import com.bleepingdragon.twiftly.services.LocalDB
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkerWindow(private val mapView: MapView, private var activity: FragmentActivity) :
    InfoWindow(R.layout.map_marker_window_layout, mapView) {

    override fun onOpen(item: Any?) {

        closeAllInfoWindowsOn(mapView)

        //Set up marker info
        var marker = item as Marker

        val idTextView = mView.findViewById<TextView>(R.id.markerIdTextView)
        idTextView.text = marker.id

        val titleTextView = mView.findViewById<TextView>(R.id.markerTitleTextView)
        titleTextView.text = marker.title

        val deleteButton = mView.findViewById<Button>(R.id.deleteMarkerButton)

        deleteButton.setOnClickListener {
            LocalDB.deleteMapPointFromUuid(marker.id, activity)
        }
    }

    override fun onClose() {
        closeAllInfoWindowsOn(mapView)
    }
}