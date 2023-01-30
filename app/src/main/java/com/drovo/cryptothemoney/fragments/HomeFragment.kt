package com.drovo.cryptothemoney.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.drovo.cryptothemoney.R
import com.drovo.cryptothemoney.adapter.TopLossGainAdapter
import com.drovo.cryptothemoney.adapter.TopMarketAdapter
import com.drovo.cryptothemoney.api.ApiInterface
import com.drovo.cryptothemoney.api.ApiUtilities
import com.drovo.cryptothemoney.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
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

        setTabLayout()

        return binding.root
    }

    private fun setTabLayout() {
        val adapter = TopLossGainAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0){
                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }else{
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager){
            tab, position->
            var title = if (position == 0){
                "Top Gainers"
            }else{
                "Top Losers"
            }
            tab.text = title
        }.attach()
    }

    private fun getTopCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java)
            //it will run in main thread
            //because we run ui things in main thread
            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(), result.getMarketData().body()!!.data.cryptoCurrencyList)
            }
        }
    }
}