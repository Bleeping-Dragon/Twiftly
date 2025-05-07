package com.bleepingdragon.twiftly

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bleepingdragon.twiftly.adapters.PlantsCareAdapter
import com.bleepingdragon.twiftly.databinding.FragmentPlantsCarePageBinding
import com.bleepingdragon.twiftly.databinding.PlantCareItemCreationModalLayoutBinding
import com.bleepingdragon.twiftly.model.PlantCareItem
import com.bleepingdragon.twiftly.services.LocalDB
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PlantsCarePage : Fragment() {

    //Plant care items
    private lateinit var plantsCareAdapter: PlantsCareAdapter
    private lateinit var plantCareItemsList: MutableList<PlantCareItem>

    //Fragment binding
    private var _binding: FragmentPlantsCarePageBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        plantCareItemsList = LocalDB.getAllPlantsCareItems(requireActivity())

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
        _binding = FragmentPlantsCarePageBinding.inflate(inflater, container, false)

        //Setup the toolbar
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        //Set adapter
        plantsCareAdapter = PlantsCareAdapter(plantCareItemsList, requireContext(), this)
        binding.plantCareItemsRecycler.adapter = plantsCareAdapter
        binding.plantCareItemsRecycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize views

        //Open a modal to create a new plant care item
        binding.createPlantCareItemFloatingActionButton.setOnClickListener {
            openPlantCareItemCreationDialog()
        }

    }

    private fun openPlantCareItemCreationDialog() {

        //Inflate the custom layout and pass it in the setView, so the content can be accessed inside layout logic
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = PlantCareItemCreationModalLayoutBinding.inflate(inflater)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.register_new_plant)
            .setView(binding.root)
            .setNeutralButton("Cancel") { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton("Create") { dialog, which ->
                // Respond to positive button press

                val plantName = binding.plantNameInput.editText?.text.toString()
                val plantNotes = binding.plantNotesInput.editText?.text.toString()

                if (plantName.isEmpty()) {
                    Toast.makeText(requireContext(), R.string.toast_plant_name_empty, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                var newPlant = PlantCareItem(
                    name = plantName,
                    notes = plantNotes
                )

                plantCareItemsList.add(newPlant)

                //Get position and notify where it was inserted
                plantsCareAdapter.notifyItemInserted(plantCareItemsList.size - 1)

                LocalDB.setAllPlantsCareItems(plantCareItemsList, requireActivity())

                //Add listener to the increase decrease buttons
            }.create().apply {

                this.setOnShowListener {
                    val dialog = this //The dialog object
                    val increaseButton = dialog.findViewById<MaterialButton>(R.id.increaseButton)
                    val decreaseButton = dialog.findViewById<MaterialButton>(R.id.decreaseButton)
                    val wateringNumberEditText = dialog.findViewById<EditText>(R.id.wateringNumberEditText)

                    wateringNumberEditText?.doOnTextChanged { text, start, before, count ->

                        //Prevent crashes if teh user modifies from the textbox directly, also clamp the number between 1 and 31
                        if (text.isNullOrEmpty() || text.toString().toInt() < 1 || text.toString().toInt() > 31) {
                            wateringNumberEditText.setText("7")
                        }

                        decreaseButton?.isEnabled = text.toString().toInt() > 1
                        increaseButton?.isEnabled = text.toString().toInt() < 31
                    }

                    increaseButton?.setOnClickListener {

                        val currentText = wateringNumberEditText?.text.toString()

                        if (currentText.toInt() < 31) {
                            wateringNumberEditText?.setText((currentText.toInt() + 1).toString())
                        }
                    }

                    decreaseButton?.setOnClickListener {

                        val currentText = wateringNumberEditText?.text.toString()

                        if (currentText.toInt() > 1) {
                            wateringNumberEditText?.setText((currentText.toInt() - 1).toString())
                        }
                    }
                }
            }
            .show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlantsCarePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlantsCarePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}