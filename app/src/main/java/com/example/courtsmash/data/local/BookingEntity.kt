package com.example.courtsmash.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.courtsmash.domain.model.Booking
import com.example.courtsmash.domain.model.BookingStatus

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val bookingReference: String,
    val courtId: String,
    val courtName: String,
    val courtImageUrl: String,
    val courtAddress: String,
    val bookingDate: String,
    val startTime: String,
    val durationHours: Int,
    val totalPrice: Int,
    val status: String, // "UPCOMING", "COMPLETED", "CANCELLED"
    val createdAt: Long
) {
    fun toDomainModel(): Booking {
        return Booking(
            id = id,
            bookingReference = bookingReference,
            courtId = courtId,
            courtName = courtName,
            courtImageUrl = courtImageUrl,
            courtAddress = courtAddress,
            bookingDate = bookingDate,
            startTime = startTime,
            durationHours = durationHours,
            totalPrice = totalPrice,
            status = try { BookingStatus.valueOf(status) } catch (e: Exception) { BookingStatus.UPCOMING },
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomainModel(booking: Booking): BookingEntity {
            return BookingEntity(
                id = booking.id,
                bookingReference = booking.bookingReference,
                courtId = booking.courtId,
                courtName = booking.courtName,
                courtImageUrl = booking.courtImageUrl,
                courtAddress = booking.courtAddress,
                bookingDate = booking.bookingDate,
                startTime = booking.startTime,
                durationHours = booking.durationHours,
                totalPrice = booking.totalPrice,
                status = booking.status.name,
                createdAt = booking.createdAt
            )
        }
    }
}
