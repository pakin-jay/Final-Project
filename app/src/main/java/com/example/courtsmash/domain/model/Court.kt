package com.example.courtsmash.domain.model

data class Court(
    val id: String,
    val name: String,
    val address: String,
    val description: String,
    val pricePerHour: Int, // Price in THB
    val openingTime: String, // e.g. "08:00"
    val closingTime: String, // e.g. "22:00"
    val facilities: List<String>,
    val imageUrl: String,
    val rating: Double = 4.8,
    val reviewCount: Int = 120,
    val latitude: Double = 13.7563,
    val longitude: Double = 100.5018,
    val isPopular: Boolean = false
)
