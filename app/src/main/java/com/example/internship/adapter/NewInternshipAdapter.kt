package com.example.internship.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.model.Job
import com.example.internship.R

class NewInternshipAdapter(val items: ArrayList<Job>): RecyclerView.Adapter<NewInternshipAdapter.Viewholder>()  {

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val image: ImageView = itemView.findViewById(R.id.image_row_new)
        val title: TextView = itemView.findViewById(R.id.title_row_new)
        val company: TextView = itemView.findViewById(R.id.company_row_new)
        val location: TextView = itemView.findViewById(R.id.company_row_new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_new_internship, parent, false)
        return Viewholder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentItem = items[position]

        holder.image.setImageResource(currentItem.image)
        holder.title.text = currentItem.title
        holder.company.text = currentItem.company
        holder.location.text = currentItem.location
    }
}