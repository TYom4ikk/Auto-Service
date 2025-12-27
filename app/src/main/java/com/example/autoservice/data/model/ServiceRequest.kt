package com.example.autoservice.data.model

data class ServiceRequest(
    val id: Int,
    val title: String,
    val status: String, // "В работе", "Завершена", etc.
    val carBrand: String,
    val carPlate: String,
    val description: String,
    val date: String
)