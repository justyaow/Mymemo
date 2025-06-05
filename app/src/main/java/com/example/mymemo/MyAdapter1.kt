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

class MyAdapter1(private val context: Context, private val itemList: MutableList<String>, private val username: String, private val onItemClick: (Int) -> Unit, private val onItemLongClick: (Int) -> Unit) :
    RecyclerView.Adapter<MyAdapter1.ViewHolder>() {
    private val idList = mutableListOf<Int>()

    init {
//        Log.d("adapterLogin", "init")
        val userInfo = MyDatabase(context, "memo", null, 1)
//        Log.d("adapterLogin", "db init")
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        Log.d("aaaaaa", "rusername:" + username)
        val cursor = infoR.rawQuery("select * from recycler where rusername = ? order by rid ASC", arrayOf(username))
        if (cursor.moveToFirst()) {
            do {
                val rcontent = cursor.getString(cursor.getColumnIndexOrThrow("rcontent"))
                Log.d("aaaaaa", rcontent)
                itemList.add(rcontent)
                val rid = cursor.getInt(cursor.getColumnIndexOrThrow("rid"))
                idList.add(rid)
                notifyItemInserted(itemList.size - 1)
            } while (cursor.moveToNext())
            cursor.close()
            userInfo.close()
        } else {
            Log.d("aaaaaa", "没有数据")
            cursor.close()
            userInfo.close()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.itemTextView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout1, parent, false)
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

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        val userInfo = MyDatabase(context, "memo", null, 1)
        val infoW: SQLiteDatabase = userInfo.writableDatabase
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        infoW.execSQL("delete from recycler where rusername = ? and rid = ?", arrayOf(username, idList[position]))
        userInfo.close()
        notifyItemRemoved(position)
    }

    fun editItem(position: Int, newText: String) {
//        Log.d("aaaaaa", "开始删除")
        val content: String = itemList[position]
        itemList.removeAt(position)
        val userInfo = MyDatabase(context, "memo", null, 1)
        val infoW: SQLiteDatabase = userInfo.writableDatabase
        val infoR: SQLiteDatabase = userInfo.readableDatabase
        infoW.execSQL("delete from recycler where rusername = ? and rid = ?", arrayOf(username, idList[position]))
//        Log.d("aaaaaa", "删除item完成")
        var id: Int = 0
        val hashTable = HashMap<Int, Int>()
        val cursor = infoR.rawQuery("select * from item where username = ?", arrayOf(username))
        if (cursor.moveToFirst()) {
            do {
                val x: Int = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
//                Log.d("aaaaaa", "x:" + x.toString())
                hashTable[x] = 1
            } while (cursor.moveToNext())
        } else {
            cursor.close()
        }
        while (hashTable[id] == 1) {
            ++id
        }
//        Log.d("aaaaaa", id.toString())
        infoW.execSQL("insert into item(id, content, username) values(?, ?, ?)", arrayOf(id, content, username))
        Log.d("aaaaaa", "还原完成" + username)
        userInfo.close()
        notifyItemRemoved(position)
    }
}