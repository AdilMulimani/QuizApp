package com.adilmulimani.quizapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adilmulimani.quizapp.databinding.HistoryitemBinding
import com.adilmulimani.quizapp.model.HistoryModelClasa
import com.google.firebase.firestore.FieldValue
import java.sql.Timestamp
import java.util.Date

class HistoryAdapter(var listHistory:ArrayList<HistoryModelClasa>)
    : RecyclerView.Adapter<HistoryAdapter.HistoryCoinViewHolder>()
{
    class HistoryCoinViewHolder(var binding:HistoryitemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {
       return HistoryCoinViewHolder(HistoryitemBinding
           .inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {

        var timeStamp= Timestamp(listHistory[position].timeAndDate.toLong())
        holder.binding.time.text=Date(timeStamp.time).toString()
        holder.binding.status.text=if(listHistory[position].isWithDrawal)"Money WIthdrawal" else "Money Credited"
        holder.binding.coins.text=listHistory[position].coin
    }

    override fun getItemCount(): Int {
     return listHistory.size
    }

}