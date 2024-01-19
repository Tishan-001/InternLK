package com.example.internship.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.internship.R
import com.example.internship.databinding.ActivityAcceptBinding
import com.example.internship.fragments.FragmentApplyCompany
import com.example.internship.fragments.FragmentProfileUser
import com.example.internship.model.Accept
import com.example.internship.model.Application
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class AcceptActivity : AppCompatActivity() {

    lateinit var binding: ActivityAcceptBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcceptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backApply.setOnClickListener { onBackPressed() }

        database = FirebaseDatabase.getInstance().getReference("Accepted")

        val intent = intent

        val item = intent.getSerializableExtra("item") as? Application

        if (item != null) {
            Picasso.get().load(item.url).into(binding.profileImage)
            binding.userNameInput.text = item.name
            binding.contactNumberInput.text = item.contactNumber
            binding.emailInput.text = item.email
            binding.genderInput.text = item.gender
            binding.experienceInput.text = item.experience
            binding.skillsInput.text = item.skills
            binding.universityInput.text = item.university
            binding.descriptionInput.text = item.description
            binding.educationInput.text = item.education

            val companyId = item.companyId
            val intershipId = item.internshipId
            val userId = item.userId

            val accept = Accept(intershipId, companyId)

            val acceptedKey = userId?.let { database.child(it).push().key }

            binding.acceptedButton.setOnClickListener{
                if(acceptedKey != null){
                    database.child(userId).child(acceptedKey).setValue(accept)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfully Accepted", Toast.LENGTH_SHORT).show()
                            val fragmentApplyCompany = FragmentApplyCompany()

                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentApplyCompany, fragmentApplyCompany)
                                .commit()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}