package com.example.internship.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.R
import com.example.internship.adapter.ActivelyHiringAdapter
import com.example.internship.adapter.ApplicationAdapter
import com.example.internship.databinding.FragmentApplyCompanyBinding
import com.example.internship.databinding.FragmentHomeUserBinding
import com.example.internship.model.Application
import com.example.internship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentApplyCompany : Fragment() {

    private lateinit var binding: FragmentApplyCompanyBinding
    private lateinit var recyclerViewApplications: RecyclerView
    private lateinit var applovations: ArrayList<Application>
    private lateinit var applicationAdapter: ApplicationAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplyCompanyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Data Loading...")
        progressDialog.setMessage("Processing...")

        val layoutManagerApplicants = GridLayoutManager(context, 1)
        recyclerViewApplications = view.findViewById(R.id.applicationsRecycle)
        recyclerViewApplications.layoutManager = layoutManagerApplicants
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewApplications)
        applovations = ArrayList()
        applicationAdapter = ApplicationAdapter(applovations)
        recyclerViewApplications.adapter = applicationAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Applications")
        val currentUser = firebaseAuth.currentUser

        progressDialog.show()

        currentUser?.let { user ->
            // Assuming companyId is a field in your user model
            val companyId = user.uid

            database.orderByChild("companyId").equalTo(companyId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        applovations.clear()
                        val array = ArrayList<String>()
                        for (data in dataSnapshot.children) {
                            val application = data.getValue()
                            application?.let {
                                array.add(it.toString())
                            }
                        }
                        Log.e("Retrieve Applications", array.toString())

                        // Update the adapter after retrieving the data
                        applicationAdapter.notifyDataSetChanged()

                        progressDialog.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Retrieve Applications", "Error getting applications: $error")
                        progressDialog.dismiss()
                    }
                })
        }
    }
}