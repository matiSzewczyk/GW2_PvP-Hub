package com.app.gw2_pvp_hub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gw2_pvp_hub.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}