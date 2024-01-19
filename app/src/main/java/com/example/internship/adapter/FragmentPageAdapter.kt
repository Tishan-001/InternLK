package com.example.internship.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.internship.fragments.FragmentAboutCompany
import com.example.internship.fragments.FragmentDescription
import com.example.internship.model.Internship

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val internship: Internship?
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentDescription.newInstance(internship)
            1 -> FragmentAboutCompany.newInstance(internship)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}