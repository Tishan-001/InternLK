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
import com.example.internship.databinding.FragmentAcceptedBinding
import com.example.internship.databinding.FragmentAppliedBinding
import com.example.internship.model.Accept
import com.example.internship.model.Application
import com.example.internship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentAccepted : Fragment() {

    lateinit var binding: FragmentAcceptedBinding

    private lateinit var recyclerViewNewInternship: RecyclerView
    private lateinit var newInternshipList: ArrayList<Internship>
    private lateinit var newInternshipAdapter: NewInternshipAdapter

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcceptedBinding.inflate(layoutInflater)
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
        newInternshipAdapter = NewInternshipAdapter(newInternshipList)

        val layoutManagerNewInternship = GridLayoutManager(context, 1)
        recyclerViewNewInternship = view.findViewById(R.id.acceptedRecycle)
        recyclerViewNewInternship.layoutManager = layoutManagerNewInternship
        recyclerViewNewInternship.adapter = newInternshipAdapter

        progressDialog.show()

        userId?.let { uid ->
            val acceptrdReference = FirebaseDatabase.getInstance().getReference("Accepted")

            acceptrdReference.child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        newInternshipList.clear()

                        for (acceptSnapshot in snapshot.children) {
                            val accept = acceptSnapshot.getValue(Accept::class.java)
                            accept?.let {
                                // Assuming there is an internshipId field in your Application model
                                val internshipId = it.internshipId
                                if (internshipId != null) {
                                    // Retrieve the corresponding internship data
                                    val internshipReference = FirebaseDatabase.getInstance().getReference("Internship")
                                        .child("internships").child(internshipId)

                                    internshipReference.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
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