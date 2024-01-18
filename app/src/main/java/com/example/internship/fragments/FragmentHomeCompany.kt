package com.example.internship.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.R
import com.example.internship.activity.UploadActivity
import com.example.internship.adapter.NewInternshipAdapter
import com.example.internship.databinding.FragmentHomeCompanyBinding
import com.example.internship.model.Company
import com.example.internship.model.Internship
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FragmentHomeCompany : Fragment() {

    lateinit var binding: FragmentHomeCompanyBinding
    private lateinit var recyclerViewNewInternship: RecyclerView
    private lateinit var newInternshipList: ArrayList<Internship>
    private lateinit var newInternshipAdapter: NewInternshipAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var uid: String
    private lateinit var company: Company

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeCompanyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Data Loading...")
        progressDialog.setMessage("Processing...")

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Internship")
        databaseReference = FirebaseDatabase.getInstance().getReference("Company")

        uid = firebaseAuth.currentUser?.uid.toString()
        if(uid.isNotEmpty()){
            getCompanyData()
        }
        else{
            Log.e("FragmentHomeUser", "Uid is null")
        }
        newInternshipList = ArrayList()
        newInternshipAdapter = NewInternshipAdapter(newInternshipList)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        })

        val layoutManagerNewInternship = GridLayoutManager(context, 1)
        recyclerViewNewInternship = view.findViewById(R.id.newInternshipCompany)
        recyclerViewNewInternship.layoutManager = layoutManagerNewInternship
        recyclerViewNewInternship.adapter = newInternshipAdapter

        progressDialog.show()
        if (::eventListener.isInitialized) {
            database.removeEventListener(eventListener)
        }
        eventListener = database.child("internships")
            .orderByChild("companyId")
            .equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    newInternshipList.clear()
                    for (data in dataSnapshot.children) {
                        val internship = data.getValue(Internship::class.java)
                        internship?.let {
                            newInternshipList.add(it)
                        }
                    }
                    Log.e("Retrieve Internships", newInternshipList.toString())

                    // Update the adapter after retrieving the data
                    newInternshipAdapter.notifyDataSetChanged()

                    progressDialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Retrieve Internships", "Error getting internships: $error")
                    progressDialog.dismiss()
                }
            })
    }

    private fun getCompanyData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val companySnapshot = snapshot.getValue(Company::class.java)
                if (companySnapshot != null) {
                    company = companySnapshot
                    binding.displayName.text = company.Name.toString()
                    Picasso.get().load(company.imageUrl).into(binding.photo)
                    company.imageUrl?.let { Log.e("Image Url", it) }
                } else {
                    Log.e("FragmentHome", "User snapshot is null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FragmentHome", "Database error: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the listener when the view is destroyed to avoid memory leaks
        if (::eventListener.isInitialized) {
            database.removeEventListener(eventListener)
        }
    }
}