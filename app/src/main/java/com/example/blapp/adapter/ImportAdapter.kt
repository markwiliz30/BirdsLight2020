package com.example.blapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.blapp.ImportFragment
import com.example.blapp.R
import com.example.blapp.model.PgmItem
import kotlinx.android.synthetic.main.fragment_import_list.view.*

class ImportAdapter(internal var context: FragmentActivity?,internal var itemList:MutableList<PgmItem>):
    RecyclerView.Adapter<ImportViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImportViewHolder {
        val itemView= LayoutInflater.from(context)
            .inflate(R.layout.fragment_import_list, parent, false)
        return ImportViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: ImportViewHolder, position: Int) {
        holder.importCardCheckBox.isChecked = ImportFragment.isSelectedAll

        var importhold: String? = itemList[position].name
        holder.imporTitle.text = importhold.toString()
        holder.importCard.setOnClickListener{
            itemList[position].isClicked = !itemList[position].isClicked
                holder.importCardCheckBox.isChecked = itemList[position].isClicked
        }
        holder.importCardCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            itemList[position].isClicked = isChecked
        }
    }
}