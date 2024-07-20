package com.maseria.diary

data class Contact(
    val type: String,
    val name: String,
    val position: String,
    val office: String,
    val email: String,
    val phone: String,
    val officeHour: String,
    val cours: List<CourseItem>
)