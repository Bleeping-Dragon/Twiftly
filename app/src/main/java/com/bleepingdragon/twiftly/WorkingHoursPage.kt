package com.bleepingdragon.twiftly

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bleepingdragon.twiftly.databinding.FragmentHomePageBinding
import com.bleepingdragon.twiftly.databinding.FragmentWorkingHoursPageBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WorkingHoursPage : Fragment() {

    //Fragment binding
    private var _binding: FragmentWorkingHoursPageBinding? = null
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
        _binding = FragmentWorkingHoursPageBinding.inflate(inflater, container, false)

        binding.hoursToWorkLayout.textInputLayout.editText?.setInputType(InputType.TYPE_NULL)
        binding.hoursToWorkLayout.textInputLayout.hint = "Hours to work"
        binding.hoursToWorkLayout.textInputLayout.setEndIconOnClickListener {
            pickTimeForInput(binding.hoursToWorkLayout.textInputLayout)
        }

        binding.startAtLayout.textInputLayout.editText?.setInputType(InputType.TYPE_NULL)
        binding.startAtLayout.textInputLayout.hint = "Hour to start working"
        binding.startAtLayout.textInputLayout.setEndIconOnClickListener {
            pickTimeForInput(binding.startAtLayout.textInputLayout)
        }

        binding.breakStartLayout.textInputLayout.editText?.setInputType(InputType.TYPE_NULL)
        binding.breakStartLayout.textInputLayout.hint = "Break start"
        binding.breakStartLayout.textInputLayout.setEndIconOnClickListener {
            pickTimeForInput(binding.breakStartLayout.textInputLayout)
        }

        binding.breakEndLayout.textInputLayout.editText?.setInputType(InputType.TYPE_NULL)
        binding.breakEndLayout.textInputLayout.hint = "Break end"
        binding.breakEndLayout.textInputLayout.setEndIconOnClickListener {
            pickTimeForInput(binding.breakEndLayout.textInputLayout)
        }

        binding.finishAtLayout.textInputLayout.editText?.setInputType(InputType.TYPE_NULL)
        binding.finishAtLayout.textInputLayout.hint = "Hour to finish working"
        binding.finishAtLayout.textInputLayout.setEndIconOnClickListener {
            pickTimeForInput(binding.finishAtLayout.textInputLayout)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun pickTimeForInput(textInput: TextInputLayout) {

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select an hour to start working")
                .build()

        MaterialTimePicker.Builder().setInputMode(INPUT_MODE_CLOCK)

        picker.show(parentFragmentManager, "tag");

        picker.addOnPositiveButtonClickListener {

            var pickedTime = picker.hour.toString() + " : " + picker.minute.toString()
            textInput.editText?.setText(pickedTime)
        }
        picker.addOnNegativeButtonClickListener {
            // call back code
        }
        picker.addOnCancelListener {
            // call back code
        }
        picker.addOnDismissListener {
            // call back code
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkingHoursPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WorkingHoursPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}