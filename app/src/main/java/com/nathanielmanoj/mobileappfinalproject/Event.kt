package com.nathanielmanoj.mobileappfinalproject

// Using nullable types and default null values is a good practice for Firebase data classes
data class Event(
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
    val location: String? = null
)
