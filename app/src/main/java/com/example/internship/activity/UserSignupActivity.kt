package com.example.internship.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.internship.R
import com.example.internship.databinding.ActivitySignupBinding
import com.example.internship.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserSignupActivity : AppCompatActivity() {


    private lateinit var binding : ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        val alreadySignupButton = findViewById<TextView>(R.id.alreadySignupBtn)


        alreadySignupButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@UserSignupActivity, UserLoginActivity::class.java)
            startActivity(intent)
        })

        binding.signupBtn.setOnClickListener {
            val name = binding.signupName.text.toString()
            val email = binding.signupEmail.text.toString()
            val pass = binding.signupPassword.text.toString()
            val conformPass = binding.signupConformPassword.text.toString()

            val user = User(name,email)
            if(email.isNotEmpty() && pass.isNotEmpty() && conformPass.isNotEmpty() && name.isNotEmpty()){
                if(pass == conformPass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val uid = firebaseAuth.currentUser?.uid
                            if (uid != null) {
                                database.child(uid).setValue(user).addOnSuccessListener {
                                    binding.signupName.text.clear()
                                    binding.signupEmail.text.clear()
                                    binding.signupPassword.text.clear()
                                    binding.signupConformPassword.text.clear()
                                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Faild", Toast.LENGTH_SHORT).show()
                                }
                            }
                            val intent = Intent(this@UserSignupActivity, UserLoginActivity::class.java)
                            startActivity(intent)
                        } else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this,"Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}