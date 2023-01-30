package com.drovo.cryptothemoney.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.drovo.cryptothemoney.R
import com.drovo.cryptothemoney.adapter.MarketAdapter
import com.drovo.cryptothemoney.api.ApiInterface
import com.drovo.cryptothemoney.api.ApiUtilities
import com.drovo.cryptothemoney.databinding.FragmentTopLossGainBinding
import com.drovo.cryptothemoney.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import java.util.stream.Collectors

class TopLossGainFragment : Fragment() {

    private lateinit var bindind: FragmentTopLossGainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindind = FragmentTopLossGainBinding.inflate(layoutInflater)

        getMarketData()

        return bindind.root
    }

    private fun getMarketData() {
        val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java)
            if (result.getMarketData().body() != null){
                withContext(Dispatchers.Main){
                    val dataItem = result.getMarketData().body()!!.data.cryptoCurrencyList

                    Collections.sort(dataItem){
                        o1, o2 -> (o2.quotes[0].percentChange24h.toInt())
                        .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    bindind.spinKitView.visibility = GONE

                    val list = ArrayList<CryptoCurrency>()

                    if (position == 0){
                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem[i])
                        }
                        bindind.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), list, "home")
                    }else{
                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem[dataItem.size-1-i])
                        }
                        bindind.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), list, "home")
                    }
                }
            }
        }
    }
}