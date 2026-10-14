package com.example.courtsmash.data.repository

import com.example.courtsmash.data.local.BookingDao
import com.example.courtsmash.data.local.BookingEntity
import com.example.courtsmash.domain.model.Booking
import com.example.courtsmash.domain.model.BookingStatus
import com.example.courtsmash.domain.model.Court
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

sealed class BookingValidationResult {
    object Success : BookingValidationResult()
    data class Error(val message: String) : BookingValidationResult()
}

class BookingRepository(private val bookingDao: BookingDao) {

    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings().map { entities ->
        entities.map { it.toDomainModel() }
    }

    fun getBookingById(id: String): Flow<Booking?> {
        return bookingDao.getBookingById(id).map { it?.toDomainModel() }
    }

    suspend fun validateBooking(
        court: Court,
        bookingDate: String, // "YYYY-MM-DD"
        startTime: String,   // "HH:mm"
        durationHours: Int
    ): BookingValidationResult = withContext(Dispatchers.IO) {
        if (durationHours <= 0) {
            return@withContext BookingValidationResult.Error("Booking duration must be at least 1 hour.")
        }

        // 1. Date Validation - Cannot book past dates
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        try {
            val selectedDate = sdf.parse(bookingDate)
            val todayStr = sdf.format(Date())
            val today = sdf.parse(todayStr)
            if (selectedDate != null && today != null && selectedDate.before(today)) {
                return@withContext BookingValidationResult.Error("Cannot book a court for a past date.")
            }
        } catch (e: Exception) {
            return@withContext BookingValidationResult.Error("Invalid date format.")
        }

        // 2. Opening & Closing Hours Validation
        val startMinutes = timeToMinutes(startTime)
        val endMinutes = startMinutes + durationHours * 60
        val openingMinutes = timeToMinutes(court.openingTime)
        val closingMinutes = timeToMinutes(court.closingTime)

        if (startMinutes < openingMinutes || endMinutes > closingMinutes) {
            return@withContext BookingValidationResult.Error(
                "Court operating hours are ${court.openingTime} - ${court.closingTime}. Selected time is outside business hours."
            )
        }

        // 3. Overlap Validation
        val existingBookings = bookingDao.getActiveBookingsForCourtAndDate(court.id, bookingDate)
        for (b in existingBookings) {
            val exStart = timeToMinutes(b.startTime)
            val exEnd = exStart + b.durationHours * 60
            if (startMinutes < exEnd && exStart < endMinutes) {
                return@withContext BookingValidationResult.Error(
                    "Selected court is already booked from ${b.startTime} to ${b.startTime.split(":")[0].toInt() + b.durationHours}:00."
                )
            }
        }

        return@withContext BookingValidationResult.Success
    }

    suspend fun createBooking(
        court: Court,
        bookingDate: String,
        startTime: String,
        durationHours: Int
    ): Result<Booking> = withContext(Dispatchers.IO) {
        val validation = validateBooking(court, bookingDate, startTime, durationHours)
        if (validation is BookingValidationResult.Error) {
            return@withContext Result.failure(IllegalArgumentException(validation.message))
        }

        val refNum = (10000..99999).random()
        val bookingRef = "CS-$refNum"
        val totalPrice = court.pricePerHour * durationHours

        val booking = Booking(
            id = UUID.randomUUID().toString(),
            bookingReference = bookingRef,
            courtId = court.id,
            courtName = court.name,
            courtImageUrl = court.imageUrl,
            courtAddress = court.address,
            bookingDate = bookingDate,
            startTime = startTime,
            durationHours = durationHours,
            totalPrice = totalPrice,
            status = BookingStatus.UPCOMING,
            createdAt = System.currentTimeMillis()
        )

        bookingDao.insertBooking(BookingEntity.fromDomainModel(booking))
        return@withContext Result.success(booking)
    }

    suspend fun cancelBooking(bookingId: String) = withContext(Dispatchers.IO) {
        bookingDao.updateBookingStatus(bookingId, BookingStatus.CANCELLED.name)
    }

    suspend fun deleteBooking(bookingId: String) = withContext(Dispatchers.IO) {
        bookingDao.deleteBookingById(bookingId)
    }

    suspend fun clearAllBookings() = withContext(Dispatchers.IO) {
        bookingDao.clearAllBookings()
    }

    private fun timeToMinutes(timeStr: String): Int {
        val parts = timeStr.split(":")
        if (parts.size != 2) return 0
        val hours = parts[0].toIntOrNull() ?: 0
        val mins = parts[1].toIntOrNull() ?: 0
        return hours * 60 + mins
    }
}
