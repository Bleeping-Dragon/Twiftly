package com.bleepingdragon.twiftly

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bleepingdragon.twiftly.databinding.FragmentMapPageBinding
import com.bleepingdragon.twiftly.databinding.MapMarkerCreationAlertLayoutBinding
import com.bleepingdragon.twiftly.model.CategoryOfMapPoints
import com.bleepingdragon.twiftly.model.MapPoint
import com.bleepingdragon.twiftly.model.MarkerWindow
import com.bleepingdragon.twiftly.services.LocalDB
import com.bleepingdragon.twiftly.services.MiscTools.Companion.getBitmapFromVectorDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MapPage : Fragment() {

    //Fragment binding
    private var _binding: FragmentMapPageBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //References
    private lateinit var activity: MainActivity
    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay

    private var userLocation: Location? = null
    private var selectedCategoryOfMapPoints: CategoryOfMapPoints? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Save activity to pass it on markers
        activity = requireActivity() as MainActivity

        //Inflate the layout for this fragment
        _binding = FragmentMapPageBinding.inflate(inflater, container, false)

        //Setup the toolbar
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        //Setup the map view
        getInstance().load(requireContext(), LocalDB.getActivityPreferences(requireActivity()))
        map = binding.mapView
        map.setTileSource(TileSourceFactory.MAPNIK)

        //Request the location and add the position overlay
        requestLocation()

        val rotationGestureOverlay = RotationGestureOverlay(map)
        rotationGestureOverlay.isEnabled
        map.setMultiTouchControls(true)
        map.overlays.add(rotationGestureOverlay)

        //Show modal to register a new point on long press
        val mapReceiver: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(geoPoint: GeoPoint): Boolean {
                return false
            }

            override fun longPressHelper(geoPoint: GeoPoint): Boolean {
                createMarkerDialog(geoPoint)
                return false
            }
        }

        val eventsOverlay = MapEventsOverlay(mapReceiver)
        eventsOverlay.isEnabled = true
        map.overlays.add(eventsOverlay)

        //Load all categories, if there is none, create a default one
        var loadedCategoriesOfMapPoints = LocalDB.getAllCategoriesOfMapPoints(requireActivity())

        if (loadedCategoriesOfMapPoints.isEmpty()) {
            var defaultCategory = CategoryOfMapPoints("My markers")
            LocalDB.setAllCategoriesOfMapPoints(mutableListOf(defaultCategory), requireActivity())
            loadedCategoriesOfMapPoints = LocalDB.getAllCategoriesOfMapPoints(requireActivity())
        }

        //Create all the markers
        for (category in loadedCategoriesOfMapPoints) {
            for (mapPoint in category.listOfMapPoints) {
                createMarker(mapPoint)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun createMarkerDialog(geoPoint: GeoPoint) {

        //Inflate the custom layout and pass it in the setView, so the content can be accessed inside layout logic
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = MapMarkerCreationAlertLayoutBinding.inflate(inflater)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.create_marker))
            .setMessage(getString(R.string.create_a_marker_in_the_selected_location))
            .setView(binding.root)
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(getString(R.string.create)) { dialog, which ->
                // Respond to positive button press
                var name = binding.textInputLayout.editText?.text.toString()
                var newMapPoint = LocalDB.createMapPoint(name, geoPoint, getSelectedCategoryUuid(), requireActivity())
                createMarker(newMapPoint)
            }
            .show()
    }


    private fun createMarker(mapPoint: MapPoint) {

        var point = GeoPoint(mapPoint.latitude!!.toDouble(), mapPoint.longitude!!.toDouble())

        var newMarker = org.osmdroid.views.overlay.Marker(map).apply {
            id = mapPoint.uuid
            position = point
            title = mapPoint.name
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.twotone_location_on_42)
            infoWindow = MarkerWindow(map, activity)
        }

        map.overlays.add(newMarker)
        map.invalidate()
    }


    private fun getSelectedCategoryUuid() : String {
        if (selectedCategoryOfMapPoints == null)
            selectedCategoryOfMapPoints = LocalDB.getAllCategoriesOfMapPoints(requireActivity()).first()
        return selectedCategoryOfMapPoints!!.uuid
    }


    //Request location depending on android version
    @SuppressLint("MissingPermission")
    private fun getLocation() : Location? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { //API 31+
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
        }
        else { //API 30-
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var foundLocation: Location?

            //GPS (Precise)
            foundLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            //Network (Less precise)
            if (foundLocation == null)
                foundLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            //Passive (Worst in precision)
            if (foundLocation == null)
                foundLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            return foundLocation
        }

    }

    private fun requestLocation() {

        //Get location permissions
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    userLocation = getLocation()
                    decideStartingMapPoint()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    userLocation = getLocation()
                    decideStartingMapPoint()
                } else -> {

                    //No location access granted
                    decideStartingMapPoint()
                    Toast.makeText(requireContext(), "Location could not be obtained.", Toast.LENGTH_LONG).show()
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    private fun decideStartingMapPoint() {

        //Move to a location
        val mapController = map.controller
        mapController.setZoom(9.5)

        if (userLocation != null) {
            val startPoint = GeoPoint(userLocation!!.latitude, userLocation!!.longitude);
            mapController.setCenter(startPoint)

            locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this.map)
            locationOverlay.enableMyLocation()
            locationOverlay.setPersonIcon(getBitmapFromVectorDrawable(requireContext(), R.drawable.twotone_my_location_36))
            locationOverlay.setPersonAnchor(0.5f,0.5f)
            map.overlays.add(locationOverlay)
        }
        else { //If location is null, move to Málaga City
            val startPoint = GeoPoint(36.719444, -4.420000);
            mapController.setCenter(startPoint)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}