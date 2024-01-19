package com.example.internship.activity

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        val item = intent.getSerializableExtra("item") as? Internship
        binding.backUpload.setOnClickListener { onBackPressed() }

        if (item != null) {
            Picasso.get().load(item.imageUrl).into(binding.companyImage)
            binding.titleInput.text = item.title
            binding.CompanyNameInput.text = item.companyName
            binding.locationCompanyInput.text = item.location
            binding.durationCompanyInput.text = item.duration
            binding.requirementsInput.text = item.requirement.toString()
            binding.benefitsInput.text = item.benefit.toString()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this@DetailsCompanyActivity, EditInternshipActivity::class.java)
            intent.putExtra("item", item)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            if (item != null){
                val id = item.id

                val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Internship")
                val internshipRef: DatabaseReference = database.child("internships").child(id)

                internshipRef.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Internship data deleted successfully", Toast.LENGTH_LONG).show()
                        val fragmentHomeCompany = FragmentHomeCompany()

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentHomeCompany, fragmentHomeCompany)
                            .commit()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(applicationContext, "Failed to delete internship data: $error", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}