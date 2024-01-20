package com.example.internship.model
import java.io.Serializable

data class Internship(
    val id: String="",
    val imageUrl: String = "",
    var title: String = "",
    var companyName: String = "",
    val companyId: String = "",
    var location: String = "",
    var duration: String = "",
    val applicants: Int = 0,
    var requirement: ArrayList<String> = ArrayList(),
    var benefit: ArrayList<String> = ArrayList()
) : Serializable
