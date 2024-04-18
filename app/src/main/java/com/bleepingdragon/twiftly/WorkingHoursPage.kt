package com.bleepingdragon.twiftly

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bleepingdragon.twiftly.databinding.FragmentWorkingHoursPageBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime

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
                .setTitleText("Select an hour")
                .build()

        MaterialTimePicker.Builder().setInputMode(INPUT_MODE_CLOCK)

        picker.show(parentFragmentManager, "tag");

        picker.addOnPositiveButtonClickListener {

            var hours = if (picker.hour.toString().length == 1) "0" + picker.hour.toString() else picker.hour.toString()
            var minutes = if (picker.minute.toString().length == 1) "0" + picker.minute.toString() else picker.minute.toString()

            var pickedTime = "$hours:$minutes"
            textInput.editText?.setText(pickedTime)

            var calculatedExit = tryCalculateExitHour()

            if (calculatedExit != null) {

                var calculatedHours = if (calculatedExit.hour.toString().length == 1) "0" + calculatedExit.hour.toString()
                                      else calculatedExit.hour.toString()

                var calculatedMinutes = if (calculatedExit.minute.toString().length == 1) "0" + calculatedExit.minute.toString()
                                        else calculatedExit.minute.toString()

                binding.calculatedDateTextView.text = "$calculatedHours:$calculatedMinutes"
            }
            else {
                binding.calculatedDateTextView.text = ""
            }


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

    private fun tryCalculateExitHour(): LocalDateTime? {

        val toWorkText = binding.hoursToWorkLayout.textInputLayout.editText?.text
        if (toWorkText.isNullOrBlank())
            return null

        val toWorkHours = (toWorkText[0].toString() + toWorkText[1].toString()).toInt()
        val toWorkMinutes = (toWorkText[3].toString() + toWorkText[4].toString()).toInt()
        val hoursToWork = LocalDateTime.now().withHour(toWorkHours).withMinute(toWorkMinutes)

        val startAtText = binding.startAtLayout.textInputLayout.editText?.text
        if (startAtText.isNullOrBlank())
            return null

        val startAtHours = (startAtText[0].toString() + startAtText[1].toString()).toInt()
        val startAtMinutes = (startAtText[3].toString() + startAtText[4].toString()).toInt()
        val startAt = LocalDateTime.now().withHour(startAtHours).withMinute(startAtMinutes)

        val breakStartText = binding.breakStartLayout.textInputLayout.editText?.text
        if (breakStartText.isNullOrBlank())
            return null

        val breakStartHours = (breakStartText[0].toString() + breakStartText[1].toString()).toInt()
        val breakStartMinutes = (breakStartText[3].toString() + breakStartText[4].toString()).toInt()
        val breakStart = LocalDateTime.now().withHour(breakStartHours).withMinute(breakStartMinutes)

        val breakEndText = binding.breakEndLayout.textInputLayout.editText?.text
        if (breakEndText.isNullOrBlank())
            return null

        val breakEndHours = (breakEndText[0].toString() + breakEndText[1].toString()).toInt()
        val breakEndMinutes = (breakEndText[3].toString() + breakEndText[4].toString()).toInt()
        val breakEnd = LocalDateTime.now().withHour(breakEndHours).withMinute(breakEndMinutes)

        var breakLengthInHours = breakEnd.minusHours(breakStart.hour.toLong()).minusMinutes(breakStart.minute.toLong())

        //Add work hours to the starting hour
        var finishHour = startAt.plusHours(hoursToWork.hour.toLong()).plusMinutes(hoursToWork.minute.toLong())
        finishHour = finishHour.plusHours(breakLengthInHours.hour.toLong()).plusMinutes(breakLengthInHours.minute.toLong())

        return finishHour
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