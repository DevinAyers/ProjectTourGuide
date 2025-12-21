package com.example.tourguide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HistoryAdapter (private val list: ArrayList<HistoryData>)
    : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgHist)
        val judul: TextView = itemView.findViewById(R.id.tvHistJudul)
        val nama: TextView = itemView.findViewById(R.id.tvHistNama)
        val telpon: TextView = itemView.findViewById(R.id.tvHistTelpon)
        val jumlah: TextView = itemView.findViewById(R.id.tvHistJumlah)
        val tanggal: TextView = itemView.findViewById(R.id.tvHistTanggal)
        val jam: TextView = itemView.findViewById(R.id.tvHistJam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = list[position]

        holder.judul.text = item.judul
        holder.nama.text = "Nama: ${item.nama}"
        holder.telpon.text = "Telpon: ${item.telpon}"
        holder.jumlah.text = "Jumlah: ${item.jumlah}"
        holder.tanggal.text = "Tanggal: ${item.tanggal}"
        holder.jam.text = "Jam: ${item.jam}"

        Glide.with(holder.itemView.context)
            .load(item.gambarUrl)
            .placeholder(R.drawable.banner2)
            .error(R.drawable.banner2)
            .into(holder.img)
    }
}