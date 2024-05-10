package com.bleepingdragon.twiftly

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bleepingdragon.twiftly.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.setDecorFitsSystemWindows(false)
    }
}