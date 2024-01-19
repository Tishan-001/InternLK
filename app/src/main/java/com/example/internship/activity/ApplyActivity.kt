package com.example.internship.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.internship.R
import com.example.internship.databinding.ActivityApplyBinding
import com.example.internship.fragments.FragmentApplyCompany
import com.example.internship.fragments.FragmentHomeUser
import com.example.internship.model.Application
import com.example.internship.model.Internship
import com.example.internship.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class ApplyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplyBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String
    private lateinit var user: User
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Applications")

        uid = firebaseAuth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()) {
            getUserData()
        } else {
            Log.e("FragmentHomeUser", "Uid is null")
        }

        binding.backApply.setOnClickListener { onBackPressed() }

        val intent = intent
        val item = intent.getSerializableExtra("item") as? Internship

        if (item != null) {
            binding.applyButton.setOnClickListener {
                val name = binding.Name.text.toString()
                val gender = binding.gender.text.toString()
                val contact = binding.ContactNo.text.toString()
                val email = binding.Email.text.toString()
                val university = binding.University.text.toString()
                val skills = binding.Skills.text.toString()
                val description = binding.description.text.toString()
                val experience = binding.experience.text.toString()
                val education = binding.education.text.toString()
                val url = url
                val companyId = item.companyId
                val internshipId = item.id

                val application = Application(
                    name, contact, email, gender, experience, skills, university,
                    description, education, url, companyId, internshipId, uid
                )

                // Generate a unique key for each application under the user's UID
                val applicationKey = database.child(uid).push().key

                if (applicationKey != null) {
                    // Use the generated key to save the application
                    database.child(uid).child(applicationKey).setValue(application)
                        .addOnSuccessListener {
                            binding.Name.text.clear()
                            binding.gender.text.clear()
                            binding.ContactNo.text.clear()
                            binding.Email.text.clear()
                            binding.University.text.clear()
                            binding.Skills.text.clear()
                            binding.description.text.clear()
                            binding.experience.text.clear()
                            binding.education.text.clear()
                            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()

                            val fragmentHomeUser = FragmentHomeUser()

                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentHome, fragmentHomeUser)
                                .commit()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userSnapshot = snapshot.getValue(User::class.java)
                if (userSnapshot != null) {
                    user = userSnapshot
                    url = user.url.toString()
                } else {
                    Log.e("ApplyActivity", "User snapshot is null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ApplyActivity", "Database error: ${error.message}")
            }
        })
    }
}
