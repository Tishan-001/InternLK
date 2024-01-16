package com.example.internship.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.internship.R
import com.example.internship.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val notRegisterButton = findViewById<TextView>(R.id.notSignupBtn)
        val loginButton = findViewById<Button>(R.id.loginBtn)


        notRegisterButton.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@UserLoginActivity, UserSignupActivity::class.java)
            startActivity(intent)
        })

        loginButton.setOnClickListener(View.OnClickListener {
            val email = binding.loginEmail.text.toString()
            val pass = binding.loginPassword.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this@UserLoginActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else{
                Toast.makeText(this,"Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show()
            }
        })

    }
}