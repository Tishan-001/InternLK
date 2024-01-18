package com.example.internship.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.model.Job
import com.example.internship.R
import com.example.internship.activity.DetailsActivity
import com.example.internship.model.Internship
import com.squareup.picasso.Picasso

class ActivelyHiringAdapter(val items: ArrayList<Internship>, val context: Context?): RecyclerView.Adapter<ActivelyHiringAdapter.Viewholder>() {

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val image: ImageView = itemView.findViewById(R.id.actively_image)
        val title: TextView = itemView.findViewById(R.id.actively_title)
        val company: TextView = itemView.findViewById(R.id.company)
        val location: TextView = itemView.findViewById(R.id.location)
        val time: TextView = itemView.findViewById(R.id.actively_time)
        val applicants: TextView = itemView.findViewById(R.id.applicants)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_actively_hiring, parent, false)
        return Viewholder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentItem = items[position]

        Picasso.get().load(currentItem.imageUrl).into(holder.image)
        holder.title.text = currentItem.title
        holder.company.text = currentItem.companyName
        holder.location.text = currentItem.location
        holder.time.text = currentItem.title
        holder.applicants.text = "${currentItem.applicants} Applicants"

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("item", currentItem)
            context?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}