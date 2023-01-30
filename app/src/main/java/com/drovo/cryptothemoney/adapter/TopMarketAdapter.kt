package com.drovo.cryptothemoney.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drovo.cryptothemoney.R
import com.drovo.cryptothemoney.databinding.TopCurrencyLayoutBinding
import com.drovo.cryptothemoney.models.CryptoCurrency


class TopMarketAdapter constructor(
    var context: Context,
    var list: List<CryptoCurrency>
): RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketViewHolder {
        return TopMarketViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TopMarketViewHolder, position: Int) {
        val item = list[position]
        //coin name
        holder.bindind.topCurrencyNameTextView.text = item.name
        //coin image
        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.bindind.topCurrencyImageView)
        //coin's loss or gain
        if (item.quotes!![0].percentChange24h > 0){
            //coin's gain
            holder.bindind.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.bindind.topCurrencyChangeTextView.text = "+${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        } else {
            //coin's loss
            holder.bindind.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.bindind.topCurrencyChangeTextView.text = "${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TopMarketViewHolder(view: View): RecyclerView.ViewHolder(view){
        var bindind = TopCurrencyLayoutBinding.bind(view)
    }
}