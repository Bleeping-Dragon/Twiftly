package com.bleepingdragon.twiftly

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bleepingdragon.twiftly.databinding.FragmentCompassPageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CompassPage : Fragment(), SensorEventListener {

    //Fragment binding
    private var _binding: FragmentCompassPageBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //Sensors
    private lateinit var sensorManager: SensorManager
    private lateinit var orientationSensor: Sensor

    //Values
    private var newRotationSensorValue: Float = 0.0F
    private var previousRotationValue: Float = 0.0F

    //Variables
    private var isDegreesShown: Boolean = false
    private var isSensorErrorShown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //Get the sensor manager
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentCompassPageBinding.inflate(inflater, container, false)

        //Setup the toolbar
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        //Hide by default the degrees until the sensor picks up values
        binding.compassContainerFrameLayout.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //When the sensor changes it's information, update the compass
    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {

        //Switch (depending on the sensor type, update it's values)
        when (event!!.sensor.type) {
            Sensor.TYPE_ORIENTATION -> newRotationSensorValue = event.values[0]
        }

        if (!isDegreesShown) {
            binding.compassContainerFrameLayout.visibility = View.VISIBLE
            isDegreesShown = true
        }

        try {
            val rotationAnimation = RotateAnimation(
                previousRotationValue,
                -newRotationSensorValue,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )

            rotationAnimation.duration = 150
            binding.compassImageView.startAnimation(rotationAnimation)
            binding.detailsImageView.startAnimation(rotationAnimation)
            previousRotationValue = -newRotationSensorValue

            binding.degreesTextView.text = " " + newRotationSensorValue.toInt() + "º"
        }
        catch(error: Exception){
            Log.e("SensorsError", error.toString())
        }
    }

    //When the accuracy of any sensor has changed
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }


    //When resuming fragment (and app), register the compass sensor if not registered before
    override fun onResume() {
        super.onResume()

        binding.compassContainerFrameLayout.visibility = View.GONE
        isDegreesShown = false

        //Check if the sensor is null to avoid crashes
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {

            orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)!!
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME)

        //Avoid showing the dialog twice
        } else if (!isSensorErrorShown) {
            isSensorErrorShown = true

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.compass_not_supported))
                .setMessage(getString(R.string.your_device_has_no_support_for_compass_sensor))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.take_me_back)) { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        }
    }


    //When the fragment (and app) is on pause, unregister the sensors
    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }


    //Delete the bindings
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompassPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompassPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}