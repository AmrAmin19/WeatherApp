package com.example.weatherapp.view.map


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private var names: ArrayList<String>,var myListenerr:(String)->Unit) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.nameTextView.text = names[position]
        holder.nameTextView.setOnClickListener {
            myListenerr.invoke(names[position])
        }
    }

    override fun getItemCount(): Int = names.size

    fun setNames(newNames: ArrayList<String>) {
        names = newNames
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(android.R.id.text1)
    }
}