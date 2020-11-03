package com.example.blapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blapp.R
import com.example.blapp.collection.PgmCollection
import com.example.blapp.model.PgmItem


class PgmAdapter(internal var context: FragmentActivity?, internal var itemList:MutableList<PgmItem>):
    RecyclerView.Adapter<PgmViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PgmViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_program_list, parent, false)
        return PgmViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PgmViewHolder, position: Int) {
        var pgmHold: Int? = itemList[position].pgm!!.toInt()
        holder.txt_pgm_num.text = pgmHold.toString()
    }
}