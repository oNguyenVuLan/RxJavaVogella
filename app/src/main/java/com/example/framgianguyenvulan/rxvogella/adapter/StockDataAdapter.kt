package com.example.framgianguyenvulan.rxvogella.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.framgianguyenvulan.rxvogella.R
import kotlinx.android.synthetic.main.stock_update_item.view.*

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/17/17.
 */
public class StockDataAdapter(var list: List<String>) : RecyclerView.Adapter<StockDataAdapter.StockUpdateViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockUpdateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stock_update_item, parent, false)
        return StockUpdateViewHolder(v)
    }

    override fun onBindViewHolder(holder: StockUpdateViewHolder, position: Int) {
        holder.bindText(list[position])
    }

     class StockUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindText(string: String) {
            itemView.stock_item_symbol.text = string
        }
    }
}