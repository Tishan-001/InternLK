package com.example.internship.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.internship.R
import com.example.internship.databinding.ActivityCompanyLoginBinding
import com.google.firebase.auth.FirebaseAuth

class CompanyLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityCompanyLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val notRegisterButton = findViewById<TextView>(R.id.notSignupBtnCompany)
        val loginButton = findViewById<Button>(R.id.loginBtnCompany)


        notRegisterButton.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@CompanyLoginActivity, CompanySignupActivity::class.java)
            startActivity(intent)
        })

        loginButton.setOnClickListener(View.OnClickListener {
            val email = binding.loginEmailCompany.text.toString()
            val pass = binding.loginPasswordCompany.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this@CompanyLoginActivity, CompanyActivity::class.java)
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