package com.example.internship.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.adapter.ActivelyHiringAdapter
import com.example.internship.model.Job
import com.example.internship.adapter.NewInternshipAdapter
import com.example.internship.R
import com.example.internship.databinding.FragmentHomeUserBinding
import com.example.internship.model.Internship
import com.example.internship.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FragmentHomeUser : Fragment() {

    private lateinit var binding: FragmentHomeUserBinding
    private lateinit var recyclerViewActivelyHiring: RecyclerView
    private lateinit var activelyHiringList: ArrayList<Internship>
    private lateinit var activelyHiringAdapter: ActivelyHiringAdapter


    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database : DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String
    private lateinit var eventListener: ValueEventListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Data Loading...")
        progressDialog.setMessage("Processing...")

        val layoutManagerActivelyHiring = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewActivelyHiring = view.findViewById(R.id.activelyHiring)
        recyclerViewActivelyHiring.layoutManager = layoutManagerActivelyHiring
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewActivelyHiring)
        activelyHiringList = ArrayList()

        activelyHiringAdapter = ActivelyHiringAdapter(activelyHiringList, requireContext())
        recyclerViewActivelyHiring.adapter = activelyHiringAdapter

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            getUserData()
        }
        else{
            Log.e("FragmentHomeUser", "Uid is null")
        }
        database = FirebaseDatabase.getInstance().getReference("Internship")

        progressDialog.show()
        if (::eventListener.isInitialized) {
            database.removeEventListener(eventListener)
        }
        database.child("internships")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    activelyHiringList.clear()
                    for (data in dataSnapshot.children) {
                        val internship = data.getValue(Internship::class.java)
                        internship?.let {
                            activelyHiringList.add(it)
                        }
                    }
                    Log.e("Retrieve Internships", activelyHiringList.toString())

                    // Update the adapter after retrieving the data
                    activelyHiringAdapter.notifyDataSetChanged()

                    progressDialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Retrieve Internships", "Error getting internships: $error")
                    progressDialog.dismiss()
                }
            })

    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userSnapshot = snapshot.getValue(User::class.java)
                if (userSnapshot != null) {
                    user = userSnapshot
                    binding.displayName.text = user.name
                    Picasso.get().load(user.url).into(binding.photo)
                } else {
                    Log.e("FragmentHomeUser", "User snapshot is null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FragmentHomeUser", "Database error: ${error.message}")
            }
        })
    }

}