package com.example.internship.model

import java.io.Serializable

data class Application(
    var name: String? = null,
    val contactNumber: String? = null,
    val email: String? = null,
    val gender: String?= null,
    val experience: String? = null,
    val skills: String? = null,
    val university: String? = null,
    val description: String? = null,
    val education: String? = null,
    var url: String? = null,
    val companyId: String? = null,
    val internshipId: String? = null,
    val userId: String? = null,
): Serializable
