package com.drovo.cryptothemoney.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.drovo.cryptothemoney.R
import com.drovo.cryptothemoney.adapter.TopMarketAdapter
import com.drovo.cryptothemoney.api.ApiInterface
import com.drovo.cryptothemoney.api.ApiUtilities
import com.drovo.cryptothemoney.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getTopCurrencyList()

        return binding.root
    }

    private fun getTopCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java)
            //it will run in main thread
            //because we run ui things in main thread
            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(), result.getMarketData().body()!!.data.cryptoCurrencyList)
            }


            Log.d("MyImportantData", "getTopCurrencyList: ${result.getMarketData().body()!!.data.cryptoCurrencyList}")
        }
    }
}