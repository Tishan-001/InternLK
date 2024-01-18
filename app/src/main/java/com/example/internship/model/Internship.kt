package com.example.internship.model

import java.io.Serializable

data class Internship(
    val imageUrl: String = "",
    val title: String = "",
    val companyName: String = "",
    val companyId: String = "",
    val location: String = "",
    val duration: String = "",
    val applicants: Int = 0,
    val requirement: ArrayList<String> = ArrayList(),
    val benefit: ArrayList<String> = ArrayList()
) : Serializable
