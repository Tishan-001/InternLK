package com.example.internship.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.R
import com.example.internship.activity.AcceptActivity
import com.example.internship.activity.ApplyActivity
import com.example.internship.model.Application
import com.example.internship.model.Company
import com.example.internship.model.Internship
import com.example.internship.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ApplicationAdapter(val items: ArrayList<Application>, val context: Context?): RecyclerView.Adapter<ApplicationAdapter.Viewholder>()  {

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val image: ImageView = itemView.findViewById(R.id.applicants_image)
        val name: TextView = itemView.findViewById(R.id.user_name)
        val interName: TextView = itemView.findViewById(R.id.internName)
        val contactNumber: TextView = itemView.findViewById(R.id.contactNum)
        val experience: TextView = itemView.findViewById(R.id.experienceTime)
        val skills: TextView = itemView.findViewById(R.id.skillsText)
        val acceptButton: MaterialButton = itemView.findViewById(R.id.accept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_application, parent, false)
        return Viewholder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentItem = items[position]

        val databaseReference = FirebaseDatabase.getInstance().getReference("Internship")
        val id = currentItem.internshipId

        if (id != null) {
            databaseReference.child("internships").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val internSnapshot = snapshot.getValue(Internship::class.java)
                    if (internSnapshot != null) {
                        val internName = internSnapshot.title

                        // Update UI only when both application and company data are available
                        Picasso.get().load(currentItem.url).into(holder.image)
                        holder.name.text = currentItem.name
                        holder.interName.text = internName.toString()
                        holder.contactNumber.text = currentItem.contactNumber
                        holder.experience.text = currentItem.experience
                        holder.skills.text = currentItem.skills
                        holder.acceptButton.setOnClickListener{
                            val intent = Intent(context, AcceptActivity::class.java)
                            intent.putExtra("item", currentItem)
                            context?.startActivity(intent)
                        }
                    } else {
                        Log.e("ApplicationAdapter", "Company snapshot is null")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ApplicationAdapter", "Database error: ${error.message}")
                }
            })
        }
    }
}