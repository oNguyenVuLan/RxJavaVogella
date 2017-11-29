package com.example.framgianguyenvulan.rxvogella.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.framgianguyenvulan.rxvogella.R
import com.example.framgianguyenvulan.rxvogella.model.StockUpdate
import kotlinx.android.synthetic.main.stock_update_item.view.*

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/17/17.
 */
class StockDataAdapter( list: MutableList<StockUpdate>) : RecyclerView.Adapter<StockDataAdapter.StockUpdateViewHolder>() {

    var listData = list
    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockUpdateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stock_update_item, parent, false)
        return StockUpdateViewHolder(v)
    }

    override fun onBindViewHolder(holder: StockUpdateViewHolder, position: Int) {
        holder.bindData(listData[position])
    }

    class StockUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(stockUpdate: StockUpdate) {
            itemView.stock_item_symbol.text = stockUpdate.stockSymbol
            itemView.stock_item_price.text = stockUpdate.price.toString()
        }
    }
}