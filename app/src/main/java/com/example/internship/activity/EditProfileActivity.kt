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
import com.example.internship.databinding.ActivityEditProfileBinding
import com.example.internship.fragments.FragmentProfileUser
import com.example.internship.model.User
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

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    private lateinit var uid: String
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var imageUrl: String

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageView: ImageView = findViewById(R.id.profileImage)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("Users")
        storage = FirebaseStorage.getInstance()

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the selected image
                val data: Intent? = result.data
                data?.data?.let { selectedImageUri ->
                    // Set the selected image to the ImageView
                    binding.profileImage.setImageURI(selectedImageUri)

                    // Upload the image to Firebase Storage
                    uploadImage(selectedImageUri)
                    user.url = selectedImageUri.toString()
                }
            }
        }

        imageView.setOnClickListener {
            // Open the phone's gallery
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)
        }

        binding.btnEdit.setOnClickListener {
            if(uid.isNotEmpty()){
                getUserData()
                val name = binding.editTextName.text.toString()
                val email = binding.editTextEmail.text.toString()
                val gender = binding.editTextGender.text.toString()
                val experience = binding.editTextExperience.text.toString()
                val skills = binding.editTextSkills.text.toString()
                val university = binding.editTextUniversity.text.toString()
                val description = binding.editTextDescription.text.toString()
                val proffesion = binding.editTextProffesion.text.toString()

                val imgUrl = imageUrl

                if (imgUrl != null) {
                    val user = User(name, email, gender, experience, skills, university, description, proffesion, imgUrl)
                    database.child(uid).setValue(user).addOnSuccessListener {

                        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Faild", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("EditProfileActivity", "Image URL is null")
                }
            }
            else{
                Log.e("FragmentHome", "Uid is null")
            }
        }
        if (uid.isNotEmpty()) {
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userSnapshot = snapshot.getValue(User::class.java)
                    if (userSnapshot != null) {
                        user = userSnapshot
                        binding.editTextName.setText(user.name)
                        binding.editTextEmail.setText(user.email)
                        binding.editTextProffesion.setText(user.proffesion)
                        binding.editTextGender.setText(user.gender)
                        binding.editTextExperience.setText(user.experience)
                        binding.editTextSkills.setText(user.skills)
                        binding.editTextUniversity.setText(user.university)
                        binding.editTextDescription.setText(user.description)

                    } else {
                        Log.e("EditProfileActivity", "User snapshot is null")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("EditProfileActivity", "Database error: ${error.message}")
                }
            })
        } else {
            Log.e("EditProfileActivity", "Uid is null")
        }


    }

    private fun uploadImage(imageUri: Uri) {
        val imageRef: StorageReference = FirebaseStorage.getInstance().getReference("Profile Pictures")
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


    private fun getUserData() {
        user.name = binding.editTextName.text.toString()
        user.email = binding.editTextEmail.text.toString()

        // Save the updated user data to Firebase
        database.child(uid).setValue(user)
            .addOnSuccessListener {
                finish()
                Log.d("FragmentHome", "User data updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("FragmentHome", "Error updating user data: ${e.message}")
            }
    }

}