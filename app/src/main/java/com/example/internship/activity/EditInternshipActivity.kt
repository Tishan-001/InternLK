package com.example.internship.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.internship.databinding.ActivityEditInternshipBinding
import com.example.internship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class EditInternshipActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditInternshipBinding
    private lateinit var database: DatabaseReference
    private lateinit var companyId: String
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInternshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backUpload.setOnClickListener { onBackPressed() }

        auth = FirebaseAuth.getInstance()
        companyId = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("Internship")

        val item = intent.getSerializableExtra("item") as? Internship

        binding.btnSave.setOnClickListener {
            if (companyId.isNotEmpty()) {
                val title = binding.titleEdit.text.toString()
                val companyName = binding.companynameEdit.text.toString()
                val location = binding.locationEdit.text.toString()
                val duration = binding.durationEdit.text.toString()
                val requirements = binding.requirementsEdit.text.toString()
                val benefits = binding.benefitsEdit.text.toString()
                val internshipId = item?.id
                val imageUrl = item?.imageUrl


                val requirementArrayList = ArrayList<String>()
                val benefitArrayList = ArrayList<String>()

                val requirementsArray = requirements.split(",").map { it.trim() }
                requirementArrayList.addAll(requirementsArray)

                val benefitArray = benefits.split(",").map { it.trim() }
                benefitArrayList.addAll(benefitArray)

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Data Saving...")
                progressDialog.setMessage("Processing...")
                progressDialog.show()

                if (internshipId != null) {
                    if (imageUrl != null) {
                        val internship = Internship(internshipId, imageUrl, title, companyName, companyId, location, duration, 0, requirementArrayList, benefitArrayList)
                        database.child("internships").child(internshipId).setValue(internship)
                            .addOnSuccessListener {
                                progressDialog.dismiss()
                                Toast.makeText(applicationContext, "Internship data saved successfully", Toast.LENGTH_LONG).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext, "Failed to save internship data", Toast.LENGTH_LONG).show()
                            }
                    } else {
                            Toast.makeText(applicationContext, "Image URL not initialized", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
