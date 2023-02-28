package com.dimthomas.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dimthomas.cryptoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textHelloWorld.text = "Test binding"
    }
}