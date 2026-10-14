package com.example.courtsmash.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.courtsmash.domain.model.Court

@Entity(tableName = "courts")
data class CourtEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val description: String,
    val pricePerHour: Int,
    val openingTime: String,
    val closingTime: String,
    val facilitiesJson: String, // Comma separated or JSON string
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val latitude: Double,
    val longitude: Double,
    val isPopular: Boolean
) {
    fun toDomainModel(): Court {
        return Court(
            id = id,
            name = name,
            address = address,
            description = description,
            pricePerHour = pricePerHour,
            openingTime = openingTime,
            closingTime = closingTime,
            facilities = facilitiesJson.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            imageUrl = imageUrl,
            rating = rating,
            reviewCount = reviewCount,
            latitude = latitude,
            longitude = longitude,
            isPopular = isPopular
        )
    }

    companion object {
        fun fromDomainModel(court: Court): CourtEntity {
            return CourtEntity(
                id = court.id,
                name = court.name,
                address = court.address,
                description = court.description,
                pricePerHour = court.pricePerHour,
                openingTime = court.openingTime,
                closingTime = court.closingTime,
                facilitiesJson = court.facilities.joinToString(","),
                imageUrl = court.imageUrl,
                rating = court.rating,
                reviewCount = court.reviewCount,
                latitude = court.latitude,
                longitude = court.longitude,
                isPopular = court.isPopular
            )
        }
    }
}
