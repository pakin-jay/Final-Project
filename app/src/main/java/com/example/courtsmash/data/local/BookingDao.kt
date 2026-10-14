package com.example.courtsmash.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    fun getBookingById(id: String): Flow<BookingEntity?>

    @Query("SELECT * FROM bookings WHERE courtId = :courtId AND bookingDate = :date AND status != 'CANCELLED'")
    suspend fun getActiveBookingsForCourtAndDate(courtId: String, date: String): List<BookingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: String, status: String)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBookingById(id: String)

    @Query("DELETE FROM bookings")
    suspend fun clearAllBookings()
}
