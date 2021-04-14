package com.example.blapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.blapp.R
import com.example.blapp.collection.LogCollection
import com.example.blapp.model.PgmItem
import org.w3c.dom.Text

class LogAdapter(internal var context: FragmentActivity?, internal var itemList:MutableList<String>):
    RecyclerView.Adapter<LogViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.log_list, parent, false)
        return LogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.txt_log.text = LogCollection.logCollection[position]
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}