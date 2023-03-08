package com.dimthomas.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dimthomas.cryptoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textHelloWorld.text = "Test binding"

        viewModel = ViewModelProvider(this)[CoinViewModel::class.java]
        viewModel.loadData()
        viewModel.priceList.observe(this, Observer {
            Log.d("TEST_OF_LOADING_DATA", "Success in activity: $it")
        })
    }
}