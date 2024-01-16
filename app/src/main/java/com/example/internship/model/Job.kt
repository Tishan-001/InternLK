package com.example.internship.model
import java.io.Serializable

data class Job(
    val image: Int,
    val title: String,
    val company: String,
    val location: String,
    val duration: String,
    val applicants: Int,
    val requirement: ArrayList<String>,
    val benefit: ArrayList<String>
): Serializable
