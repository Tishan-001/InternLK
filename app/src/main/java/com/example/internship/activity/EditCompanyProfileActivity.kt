package com.example.internship.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.internship.R
import com.example.internship.databinding.ActivityEditCompanyProfileBinding
import com.example.internship.fragments.FragmentProfileCompany
import com.example.internship.model.Company
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class EditCompanyProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditCompanyProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var company: Company
    private lateinit var uid: String
    private lateinit var auth : FirebaseAuth
    private lateinit var imageUrl: String

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCompanyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageView: ImageView = findViewById(R.id.profileImage)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("Company")

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // Handle the selected image
                    val data: Intent? = result.data
                    data?.data?.let { selectedImageUri ->
                        // Set the selected image to the ImageView
                        binding.profileImage.setImageURI(selectedImageUri)

                        // Upload the image to Firebase Storage
                        uploadImage(selectedImageUri)
                    }
                }
        }

        imageView.setOnClickListener {
            // Open the phone's gallery
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)
        }


        binding.btnsaveCompany.setOnClickListener {
            if(uid.isNotEmpty()){
                getCompanyData()
                val name = binding.editTextName.text.toString()
                val email = binding.editTextEmail.text.toString()
                val location = binding.editTextLocation.text.toString()

                val imgUrl = imageUrl

                val company = Company(name, email, imgUrl, location)
                database.child(uid).setValue(company).addOnSuccessListener {

                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Faild", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Log.e("FragmentHome", "Uid is null")
            }
        }
        if (uid.isNotEmpty()) {
            database = FirebaseDatabase.getInstance().getReference("Company")
            database.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val companySnapshot = snapshot.getValue(Company::class.java)
                    if (companySnapshot != null) {
                        company = companySnapshot
                        binding.editTextName.setText(company.Name)
                        binding.editTextEmail.setText(company.email)
                        binding.editTextLocation.setText(company.location)
                    } else {
                        Log.e("EditCompanyProfileActivity", "User snapshot is null")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("EditCompanyProfileActivity", "Database error: ${error.message}")
                }
            })
        } else {
            Log.e("EditCompanyProfileActivity", "Uid is null")
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val imageRef: StorageReference = FirebaseStorage.getInstance().getReference("Company Pictures")
            .child(UUID.randomUUID().toString())

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                // Image uploaded successfully
                Log.e("Upload Image","Image uploaded successfully")

                // Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the image URL to Firebase Database under the user's data
                    imageUrl = uri.toString()
                    Log.e("image Uel",imageUrl)
                }
            }
            .addOnFailureListener { exception: Exception ->
                // Handle errors
                Log.e("Upload Image","Image upload failed: $exception")
            }
    }

    private fun getCompanyData() {
        company.Name = binding.editTextName.text.toString()
        company.email = binding.editTextEmail.text.toString()

        // Save the updated user data to Firebase
        database.child(uid).setValue(company)
            .addOnSuccessListener {
                Log.d("FragmentCompanyHome", "User data updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("FragmentCompanyHome", "Error updating user data: ${e.message}")
            }
    }
}