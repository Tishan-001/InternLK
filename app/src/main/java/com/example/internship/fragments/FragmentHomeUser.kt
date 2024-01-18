package com.example.internship.fragments

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
    private lateinit var activelyHiringList: ArrayList<Job>
    private lateinit var activelyHiringAdapter: ActivelyHiringAdapter

    private lateinit var arrayRequirementGojek: ArrayList<String>
    private lateinit var arrayBenefitGojek: ArrayList<String>

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManagerActivelyHiring = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewActivelyHiring = view.findViewById(R.id.activelyHiring)
        recyclerViewActivelyHiring.layoutManager = layoutManagerActivelyHiring
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewActivelyHiring)
        activelyHiringList = ArrayList()
        arrayRequirementGojek = ArrayList()
        arrayBenefitGojek = ArrayList()

        arrayRequirementGojek.add("Proven to ever work as a UI/UX Designer or similar role.")
        arrayRequirementGojek.add("Understanding about Visual design and UX Design and Have a strong design taste.")
        arrayRequirementGojek.add("Up-to-date knowledge of design software like Adobe Illustrator and Photoshop.")

        arrayBenefitGojek.add("$200 Monthly Allowance and bonus.")
        arrayBenefitGojek.add("Professional mentors.")

        activelyHiringList.add(Job(R.drawable.netflix, "UX Writer Intern", "Netflix", "Bandung", "6 Months", 189, arrayRequirementGojek, arrayBenefitGojek))
        activelyHiringList.add(Job(R.drawable.gojek, "UI Designer Intern", "Gojko", "Jakarta", "3 Months", 112, arrayRequirementGojek, arrayBenefitGojek))

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