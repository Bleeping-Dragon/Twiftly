package com.bleepingdragon.twiftly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bleepingdragon.twiftly.databinding.FragmentHomePageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomePage : Fragment() {

    //Fragment binding
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


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
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)

        //Go to Working hours fragment
        binding.workingHoursButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homePage_to_workingHoursPage) //Use app_navigation.xml action
        }

        //Go to Compass fragment
        binding.compassButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homePage_to_compassPage) //Use app_navigation.xml action
        }

        //Go to Heads Tails fragment
        binding.headsTailsButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homePage_to_headsTailsPage) //Use app_navigation.xml action
        }

        //Go to Flashlight fragment
        binding.flashlightButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homePage_to_flashlightPage) //Use app_navigation.xml action
        }

        //Go to Map fragment
        binding.mapButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homePage_to_mapPage) //Use app_navigation.xml action
        }

        //Go to Plants Care fragment
        binding.plantsCareButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homePage_to_plantsCarePage) //Use app_navigation.xml action
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}