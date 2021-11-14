package com.example.dataimplementation


import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapterData(var longClickListener: onLongItemClickListener) : ListAdapter<Data,RecyclerViewAdapterData.DataViewHolder>(
    object :DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.productId==newItem.productId
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return newItem==oldItem
        }

    }
){
    class DataViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val tvproductId=itemView.findViewById<TextView>(R.id.textViewProductId)
        val tvproductName=itemView.findViewById<TextView>(R.id.textViewProductName)
        val tvprice=itemView.findViewById<TextView>(R.id.textViewPrice)
        val tvstatus=itemView.findViewById<TextView>(R.id.textViewStatus)
        val tvposition=itemView.findViewById<TextView>(R.id.textViewNO)

         fun initialize(item: Data,action:onLongItemClickListener){
             tvposition.text=item.position.toString()
             tvproductId.text=item.productId.toString()
             tvproductName.text=item.productName
             tvprice.text=item.price.toString()
             tvstatus.text=item.status.toString()
             itemView.setOnLongClickListener {
                  action.onLongClick(item,adapterPosition)
                  true
              }

         }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_data,parent,false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.initialize(getItem(position),longClickListener)

    }
    interface onLongItemClickListener{
        fun onLongClick(item:Data,position: Int)



    }

}


