package com.bleepingdragon.twiftly

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bleepingdragon.twiftly.databinding.FragmentFlashlightPageBinding
import com.bleepingdragon.twiftly.databinding.FragmentHeadsTailsPageBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.logging.LogManager
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FlashlightPage : Fragment() {

    //Fragment binding
    private var _binding: FragmentFlashlightPageBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //Variables
    private var currentState: Boolean = true

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
        //Inflate the layout for this fragment
        _binding = FragmentFlashlightPageBinding.inflate(inflater, container, false)

        //Setup the toolbar
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Turn off the flash
        alternateFlash()

        //Initialize views
        binding.flashlightFrameView.setOnClickListener {
            alternateFlash()
            alternateImages()
        }
    }

    //Alternate physical torch state
    private fun alternateFlash() {

        currentState = !currentState

        var cameraManager: CameraManager? = null
        cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.setTorchMode("0", currentState)
    }

    //Alternate physical torch state
    private fun alternateImages() {

        //ON
        if (currentState) {
            binding.flashlightOnImageView.visibility = View.VISIBLE
            binding.flashlightOffImageView.visibility = View.GONE
        }
        //OFF
        else {
            binding.flashlightOnImageView.visibility = View.GONE
            binding.flashlightOffImageView.visibility = View.VISIBLE
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlashlightPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FlashlightPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}