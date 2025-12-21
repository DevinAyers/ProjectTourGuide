package com.example.tourguide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TourAdapter( private var list : List<TourData>, private val onItemClick: (TourData) -> Unit)
    : RecyclerView.Adapter<TourAdapter.TourViewHolder>() {

        class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val img : ImageView = itemView.findViewById(R.id.imgTour)
            val judul : TextView = itemView.findViewById(R.id.tvTourJudul)
            val kota : TextView = itemView.findViewById(R.id.tvTourKota)
        }

    private var listFull : List<TourData> = ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tour, parent, false)
        return TourViewHolder(view)
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val item = list[position]
        holder.judul.text = item.judul
        holder.kota.text = item.kota

        Glide.with(holder.itemView.context).load(item.gambarUrl).into(holder.img)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }


    fun updateData(lists : List<TourData>){
        list = lists
        listFull = lists
        notifyDataSetChanged()
    }

    fun query(query : String){
        if (query.isEmpty()){
            list = listFull
        }else{
            val queryBersih = query.lowercase().trim()
            list = listFull.filter {
                it.judul.lowercase().contains(queryBersih) || it.kota.lowercase().contains(queryBersih)
            }

        }
        notifyDataSetChanged()

    }


}