package com.example.internship.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.R
import com.example.internship.adapter.NewInternshipAdapter
import com.example.internship.databinding.FragmentAppliedBinding
import com.example.internship.databinding.FragmentHomeCompanyBinding
import com.example.internship.model.Application
import com.example.internship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentApplied : Fragment() {

    lateinit var binding: FragmentAppliedBinding
    private lateinit var recyclerViewNewInternship: RecyclerView
    private lateinit var newInternshipList: ArrayList<Internship>
    private lateinit var newInternshipAdapter: NewInternshipAdapter

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppliedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Data Loading...")
        progressDialog.setMessage("Processing...")

        firebaseAuth = FirebaseAuth.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        newInternshipList = ArrayList()
        newInternshipAdapter = NewInternshipAdapter(newInternshipList, requireContext())

        val layoutManagerNewInternship = GridLayoutManager(context, 1)
        recyclerViewNewInternship = view.findViewById(R.id.applicationsRecycle)
        recyclerViewNewInternship.layoutManager = layoutManagerNewInternship
        recyclerViewNewInternship.adapter = newInternshipAdapter

        progressDialog.show()

        userId?.let { uid ->
            // Reference to the Applications node
            val applicationsReference = FirebaseDatabase.getInstance().getReference("Applications")

            // Query applications where the key is equal to the user's ID
            applicationsReference.child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        newInternshipList.clear()

                        for (applicationSnapshot in snapshot.children) {
                            val application = applicationSnapshot.getValue(Application::class.java)
                            application?.let {
                                // Assuming there is an internshipId field in your Application model
                                val internshipId = it.internshipId
                                if (internshipId != null) {
                                    // Retrieve the corresponding internship data
                                    val internshipReference = FirebaseDatabase.getInstance().getReference("Internship")
                                        .child("internships").child(internshipId)

                                    internshipReference.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(internshipSnapshot: DataSnapshot) {
                                            val internship = internshipSnapshot.getValue(Internship::class.java)
                                            internship?.let {
                                                newInternshipList.add(it)
                                                newInternshipAdapter.notifyDataSetChanged()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            // Handle the error
                                            Log.e("Firebase", "Error getting internships: $error")
                                        }
                                    })
                                }
                            }
                        }

                        progressDialog.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error
                        Log.e("Firebase", "Error getting applications: $error")
                        progressDialog.dismiss()
                    }
                })
        }
    }
}