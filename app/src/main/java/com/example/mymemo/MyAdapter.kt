package com.example.mymemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class MyAdapter(private val context: Context, private val itemList: MutableList<String>, private val username: String, private val onItemClick: (Int) -> Unit, private val onItemLongClick: (Int) -> Unit) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    init {
        Log.d("adapterLogin", "init")
        val userInfo = MyDatabase(context, "memo", null, 1)
        Log.d("adapterLogin", "db init")
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        val cursor = infoR.rawQuery("select * from item where username = ? order by id ASC", arrayOf(username))
        if (cursor.moveToFirst()) {
            do {
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                itemList.add(content)
                notifyItemInserted(itemList.size - 1)
            } while (cursor.moveToNext())
            cursor.close()
            userInfo.close()
        } else {
            cursor.close()
            userInfo.close()
        }
    }

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
        val userInfo = MyDatabase(context, "memo", null, 1)
        val infoW: SQLiteDatabase = userInfo.writableDatabase
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        val id: Int = itemList.size - 1
        infoW.execSQL("insert into item(id, content, username) values(?, ?, ?)", arrayOf(id, item, username))
        userInfo.close()
        notifyItemInserted(itemList.size - 1)
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        val userInfo = MyDatabase(context, "memo", null, 1)
        val infoW: SQLiteDatabase = userInfo.writableDatabase
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        infoW.execSQL("delete from item where username = ? and id = ?", arrayOf(username, position))
        userInfo.close()
        notifyItemRemoved(position)
    }

    fun editItem(position: Int, newText: String) {
        itemList[position] = newText
        val userInfo = MyDatabase(context, "memo", null, 1)
        val infoW:SQLiteDatabase = userInfo.writableDatabase
        infoW.execSQL("update item set content = ? where username = ? and id = ?", arrayOf(newText, username, position))
        userInfo.close()
        notifyItemChanged(position)
    }
}