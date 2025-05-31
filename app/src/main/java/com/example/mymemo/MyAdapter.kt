package com.example.mymemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog

class MyAdapter(private val itemList: MutableList<String>, private val onItemClick: (Int) -> Unit, private val onItemLongClick: (Int) -> Unit) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.itemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = itemList[position]
        holder.itemView.setOnClickListener {
            onItemClick(position)
//            AlertDialog.Builder(holder.itemView.context)
//                .setTitle("备忘录详情")
//                .setMessage(itemList[position])
//                .setPositiveButton("编辑", null)
//                .show()
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(item: String) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int, newText: String) {
        itemList[position] = newText
        notifyItemChanged(position)
    }
}