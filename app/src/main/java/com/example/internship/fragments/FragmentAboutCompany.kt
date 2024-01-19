package com.example.internship.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.R
import com.example.internship.adapter.DescriptionAdapter
import com.example.internship.databinding.FragmentAboutCompanyBinding
import com.example.internship.model.Internship

@Suppress("DEPRECATION")
class FragmentAboutCompany : Fragment() {

    private lateinit var binding: FragmentAboutCompanyBinding
    private lateinit var benefitRecyclerView: RecyclerView
    private var internship: Internship? = null

    companion object {
        private const val ARG_INTERNSHIP = "arg_internship"

        fun newInstance(item: Internship?): FragmentAboutCompany {
            val fragment = FragmentAboutCompany()
            val args = Bundle()
            args.putSerializable(ARG_INTERNSHIP, item)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutCompanyBinding.inflate(layoutInflater)
        arguments?.let {
            internship = it.getSerializable(FragmentDescription.ARG_INTERNSHIP) as? Internship
        }
        val rootView = inflater.inflate(R.layout.fragment_about_company, container, false)
        benefitRecyclerView = rootView.findViewById(R.id.benefit)
        internship?.let { validInternship ->
            setupRecyclerView(benefitRecyclerView, validInternship.benefit)
        }
        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, dataList: ArrayList<String>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = DescriptionAdapter(dataList) // Replace com.example.internship.adapter.DescriptionAdapter with your adapter type
        recyclerView.adapter = adapter
    }

}