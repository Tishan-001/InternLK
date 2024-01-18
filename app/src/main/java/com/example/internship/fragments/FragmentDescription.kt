package com.example.internship.fragments

import com.example.internship.adapter.DescriptionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internship.R
import com.example.internship.databinding.FragmentDescriptionBinding
import com.example.internship.model.Internship

@Suppress("DEPRECATION")
class FragmentDescription:Fragment() {

    private lateinit var binding: FragmentDescriptionBinding
    private lateinit var requirementRecyclerView: RecyclerView
    private lateinit var benefitRecyclerView: RecyclerView
    private var internship: Internship? = null

    companion object {
        private const val ARG_INTERNSHIP = "arg_internship"

        fun newInstance(item: Internship): FragmentDescription {
            val fragment = FragmentDescription()
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
        binding = FragmentDescriptionBinding.inflate(layoutInflater)
        arguments?.let {
            internship = it.getSerializable(ARG_INTERNSHIP) as? Internship
        }
        val rootView = inflater.inflate(R.layout.fragment_description, container, false)
        requirementRecyclerView = rootView.findViewById(R.id.requirement)
        benefitRecyclerView = rootView.findViewById(R.id.benefit)

        // Check if job is not null before proceeding
        internship?.let { validInternship ->
            setupRecyclerView(requirementRecyclerView, validInternship.requirement)
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