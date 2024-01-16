package com.example.internship.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.internship.databinding.FragmentProfileBinding
import com.example.internship.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.internship.R
import com.example.internship.activity.LetStartActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class FragmentProfile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String
    private lateinit var auth : FirebaseAuth
    private lateinit var btnLogOut: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        btnLogOut = view.findViewById(R.id.btn_signOut)

        database = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            getUserData()
        }
        else{
            Log.e("FragmentHome", "Uid is null")
        }

        btnLogOut.setOnClickListener{
            Firebase.auth.signOut()

            val intent = Intent(requireContext(), LetStartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserData() {

        database.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userSnapshot = snapshot.getValue(User::class.java)
                if (userSnapshot != null) {
                    user = userSnapshot
                    binding.username.text = user.name
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