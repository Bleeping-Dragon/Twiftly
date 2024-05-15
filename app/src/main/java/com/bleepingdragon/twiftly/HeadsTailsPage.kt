package com.bleepingdragon.twiftly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
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

class HeadsTailsPage : Fragment() {

    //Fragment binding
    private var _binding: FragmentHeadsTailsPageBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //Motionlayout references
    private lateinit var startConstraintSet: ConstraintSet
    private lateinit var endConstraintSet: ConstraintSet

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
        _binding = FragmentHeadsTailsPageBinding.inflate(inflater, container, false)

        //Setup the toolbar
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize views
        startConstraintSet = binding.motionLayout.coinFlipMotionLayout.getConstraintSet(R.id.start)
        endConstraintSet = binding.motionLayout.coinFlipMotionLayout.getConstraintSet(R.id.end)

        chooseCoinSide(true)

        binding.flipCoinButton.setOnClickListener {
            chooseCoinSide(false)
        }
    }

    private fun chooseCoinSide(inPageLoad: Boolean) {

        lifecycleScope.launch {

            println("Hello")

            var random = (0..1).random()

            binding.motionLayout.coinFlipMotionLayout.jumpToState(R.id.start)
            binding.motionLayout.coinFlipMotionLayout.transitionToState(R.id.end)

            delay(1090L)

            println("World")

            //Switch after delay, Heads = 0, Tails = 1
            if (random == 0) {

                binding.motionLayout.headsImageView.visibility = View.VISIBLE
                binding.motionLayout.tailsImageView.visibility = View.INVISIBLE

//                binding.motionLayout.headsImageView.setVisibilityOfMotionChild(View.VISIBLE)
//                binding.motionLayout.tailsImageView.setVisibilityOfMotionChild(View.INVISIBLE)

//                startConstraintSet.setVisibilityMode(R.id.headsImageView, View.VISIBLE)
//                startConstraintSet.setVisibilityMode(R.id.tailsImageView, View.INVISIBLE)
            }
            else {

                binding.motionLayout.tailsImageView.visibility = View.VISIBLE
                binding.motionLayout.headsImageView.visibility = View.INVISIBLE

//                binding.motionLayout.tailsImageView.setVisibilityOfMotionChild(View.VISIBLE)
//                binding.motionLayout.headsImageView.setVisibilityOfMotionChild(View.INVISIBLE)

//                startConstraintSet.setVisibilityMode(R.id.tailsImageView, View.VISIBLE)
//                startConstraintSet.setVisibilityMode(R.id.headsImageView, View.INVISIBLE)
            }

        }


    }

    private fun View.setVisibilityOfMotionChild(visibility: Int) {

        val motionLayout = parent as MotionLayout
        motionLayout.constraintSetIds.forEach {
            val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
            constraintSet.setVisibility(this.id, visibility)
            constraintSet.applyTo(motionLayout)
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
            HeadsTailsPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}