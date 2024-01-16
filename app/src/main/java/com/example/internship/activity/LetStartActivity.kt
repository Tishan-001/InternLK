package com.example.internship.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.internship.R

class LetStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_let_start)

        val letsGoButton =findViewById<Button>(R.id.letsStartAsStudent)
        val letsGoCompany = findViewById<Button>(R.id.letsStartAsCompany)

        letsGoButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LetStartActivity, UserLoginActivity::class.java)
            startActivity(intent)
        })

        letsGoCompany.setOnClickListener(View.OnClickListener {

        })
    }
}