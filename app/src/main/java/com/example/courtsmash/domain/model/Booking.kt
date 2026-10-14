package com.example.courtsmash.domain.model

data class Booking(
    val id: String,
    val bookingReference: String,
    val courtId: String,
    val courtName: String,
    val courtImageUrl: String,
    val courtAddress: String,
    val bookingDate: String, // Format: "YYYY-MM-DD"
    val startTime: String,   // Format: "HH:mm"
    val durationHours: Int,
    val totalPrice: Int,     // in THB
    val status: BookingStatus,
    val createdAt: Long = System.currentTimeMillis()
) {
    // Calculates end time string e.g. 14:00 + 2h -> 16:00
    val endTime: String
        get() {
            val parts = startTime.split(":")
            if (parts.size != 2) return startTime
            val startHour = parts[0].toIntOrNull() ?: 0
            val startMin = parts[1]
            val endHour = startHour + durationHours
            return String.format(java.util.Locale.US, "%02d:%s", endHour, startMin)
        }
}
