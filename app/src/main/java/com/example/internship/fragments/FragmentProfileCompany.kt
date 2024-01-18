package com.example.internship.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.internship.R
import com.example.internship.activity.EditCompanyProfileActivity
import com.example.internship.activity.LetStartActivity
import com.example.internship.databinding.FragmentProfileCompanyBinding
import com.example.internship.model.Company
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FragmentProfileCompany : Fragment() {

    private lateinit var binding: FragmentProfileCompanyBinding
    private lateinit var database: DatabaseReference
    private lateinit var company: Company
    private lateinit var uid: String
    private lateinit var auth : FirebaseAuth
    private lateinit var btnLogOut: Button
    private lateinit var btnEdit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileCompanyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        btnLogOut = view.findViewById(R.id.btn_signOutCompany)
        btnEdit = view.findViewById(R.id.btn_editCompany)

        database = FirebaseDatabase.getInstance().getReference("Company")
        if(uid.isNotEmpty()){
            getCompanyData()
        }
        else{
            Log.e("FragmentHome", "Uid is null")
        }

        btnLogOut.setOnClickListener{
            Firebase.auth.signOut()

            val intent = Intent(requireContext(), LetStartActivity::class.java)
            startActivity(intent)
        }

        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditCompanyProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCompanyData() {
        database.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val companySnapshot = snapshot.getValue(Company::class.java)
                if (companySnapshot != null) {
                    company = companySnapshot
                    binding.CompanyNameInput.text = company.Name
                    binding.emailInputCompany.text = company.email
                    binding.locationInput.text = company.location
                    Picasso.get().load(company.imageUrl).into(binding.companyImage)
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
}