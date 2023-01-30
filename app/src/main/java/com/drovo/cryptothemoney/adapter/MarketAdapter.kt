package com.drovo.cryptothemoney.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drovo.cryptothemoney.R
import com.drovo.cryptothemoney.databinding.CurrencyItemLayoutBinding
import com.drovo.cryptothemoney.fragments.HomeFragmentDirections
import com.drovo.cryptothemoney.models.CryptoCurrency

class MarketAdapter constructor(
    var context: Context,
    var list: List<CryptoCurrency>
): RecyclerView.Adapter<MarketAdapter.MarketViewHolder>(){

    inner class MarketViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = CurrencyItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)

        holder.binding.currencyPriceTextView.text = "${String.format("$%.02f", item.quotes[0].price)}"

        if (item.quotes!![0].percentChange24h > 0){
            //coin's gain
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        } else {
            //coin's loss
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        }

        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+item.id+".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)

        //pass data from Home fragment(top gainers and top losers) to Details fragment
        holder.itemView.setOnClickListener {
            findNavController(it).navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}