package com.example.internship.activity

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.internship.R
import com.example.internship.databinding.ActivityUploadBinding
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.internship.model.Internship
import java.lang.Exception
import java.util.UUID

@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {

    lateinit var binding: ActivityUploadBinding
    lateinit var image: ImageView
    lateinit var imageUrl: String
    var fileUri: Uri? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Internship")

        binding.backUpload.setOnClickListener { onBackPressed() }

        image = findViewById(R.id.image_view)

        binding.chooseImage.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Image to Upload"),0
            )
        }

        binding.applyButton.setOnClickListener {
            uploadImage()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                image.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Upload Image", "Error: $e")
            }
        }
    }

    private fun uploadImage(){
        if(fileUri != null){
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    Log.d("Image URL", imageUrl)
                    saveInternshipData()
                }.addOnFailureListener {
                    Log.e("Image URL", "Error getting download URL: $it")
                }
            }.addOnFailureListener {
                Log.e("Upload Image", "File Upload Failed")
            }
        }
    }

    private fun saveInternshipData() {

        val title = binding.editTextTitle.text.toString()
        val companyName = binding.editTextName.text.toString()
        val location = binding.editTextLocation.text.toString()
        val duration = binding.editTextDuration.text.toString()
        val requirements = binding.requirements.text.toString()
        val benefits = binding.benefits.text.toString()
        val uid = firebaseAuth.currentUser?.uid.toString()

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

        val internshipId = database.child("internships").push().key
        if (internshipId != null) {
            val internship = Internship(internshipId, imageUrl, title, companyName, uid, location, duration, 0, requirementArrayList, benefitArrayList)
            database.child("internships").child(internshipId).setValue(internship)
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
}
