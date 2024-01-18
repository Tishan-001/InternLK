package com.example.internship.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.internship.R
import com.example.internship.databinding.ActivityCompanyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CompanyActivity : AppCompatActivity() {

    lateinit var binding: ActivityCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewCompany)
        val navController = Navigation.findNavController(this, R.id.mainFragmentCompany)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}