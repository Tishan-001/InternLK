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
    internship: Internship?
) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }
    val item = internship
    override fun createFragment(position: Int): Fragment {
        return if (position == 0 && item != null)
            FragmentDescription.newInstance(item)
        else
            FragmentAboutCompany()
    }
}