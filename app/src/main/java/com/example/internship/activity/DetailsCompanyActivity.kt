package com.example.internship.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.internship.R
import com.example.internship.databinding.ActivityDetailsCompanyBinding
import com.example.internship.fragments.FragmentHomeCompany
import com.example.internship.model.Internship
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class DetailsCompanyActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsCompanyBinding
    lateinit var internship: Internship
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        val item = intent.getSerializableExtra("item") as? Internship
        binding.backUpload.setOnClickListener { onBackPressed() }

        database = FirebaseDatabase.getInstance().getReference("Internship")

        if (item != null) {
            Picasso.get().load(item.imageUrl).into(binding.companyImage)
            internship = item

            binding.titleEdit.setText(item.title)
            binding.companynameEdit.setText(item.companyName)
            binding.locationEdit.setText(item.location)
            binding.durationEdit.setText(item.duration)
            binding.requirementsEdit.setText(item.requirement.joinToString(", "))
            binding.benefitsEdit.setText(item.benefit.joinToString (", "))
        }

        binding.btnEdit.setOnClickListener {
            saveData()
        }

        binding.btnDelete.setOnClickListener {
            if (item != null){
                val id = item.id
                val internshipRef: DatabaseReference = database.child("internships").child(id)

                internshipRef.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Internship data deleted successfully", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(applicationContext, "Failed to delete internship data: $error", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun saveData(){
        internship.title = binding.titleEdit.text.toString()
        internship.companyName = binding.companynameEdit.text.toString()
        internship.location = binding.locationEdit.text.toString()
        internship.duration = binding.durationEdit.text.toString()
        val requirements = binding.requirementsEdit.text.toString()
        val benefits = binding.benefitsEdit.text.toString()
        val requirementArrayList = ArrayList<String>()
        val benefitArrayList = ArrayList<String>()

        val requirementsArray = requirements.split(",").map { it.trim() }
        requirementArrayList.addAll(requirementsArray)

        val benefitArray = benefits.split(",").map { it.trim() }
        benefitArrayList.addAll(benefitArray)

        internship.requirement = requirementArrayList
        internship.benefit = benefitArrayList

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Data Saving...")
        progressDialog.setMessage("Processing...")
        progressDialog.show()

        database.child("internships").child(internship.id).setValue(internship)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Internship data saved successfully", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to save internship data", Toast.LENGTH_LONG).show()
            }
    }
}