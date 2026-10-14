package com.example.courtsmash

import com.example.courtsmash.data.local.BookingDao
import com.example.courtsmash.data.local.BookingEntity
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.data.repository.BookingValidationResult
import com.example.courtsmash.domain.model.Court
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FakeBookingDao : BookingDao {
    private val bookings = mutableListOf<BookingEntity>()

    override fun getAllBookings(): Flow<List<BookingEntity>> = flowOf(bookings)

    override fun getBookingById(id: String): Flow<BookingEntity?> =
        flowOf(bookings.firstOrNull { it.id == id })

    override suspend fun getActiveBookingsForCourtAndDate(
        courtId: String,
        date: String
    ): List<BookingEntity> {
        return bookings.filter {
            it.courtId == courtId && it.bookingDate == date && it.status != "CANCELLED"
        }
    }

    override suspend fun insertBooking(booking: BookingEntity) {
        bookings.add(booking)
    }

    override suspend fun updateBookingStatus(id: String, status: String) {
        val index = bookings.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = bookings[index]
            bookings[index] = old.copy(status = status)
        }
    }

    override suspend fun deleteBookingById(id: String) {
        bookings.removeAll { it.id == id }
    }

    override suspend fun clearAllBookings() {
        bookings.clear()
    }
}

class BookingLogicTest {

    private lateinit var fakeDao: FakeBookingDao
    private lateinit var repository: BookingRepository
    private lateinit var testCourt: Court
    private val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    @Before
    fun setUp() {
        fakeDao = FakeBookingDao()
        repository = BookingRepository(fakeDao)
        testCourt = Court(
            id = "test_court_1",
            name = "Test Smash Court",
            address = "123 Test St",
            description = "Test Description",
            pricePerHour = 250,
            openingTime = "08:00",
            closingTime = "22:00",
            facilities = listOf("Air Con"),
            imageUrl = ""
        )
    }

    @Test
    fun testPastDateValidation_returnsError() = runBlocking {
        val result = repository.validateBooking(
            court = testCourt,
            bookingDate = "2020-01-01",
            startTime = "10:00",
            durationHours = 2
        )
        assertTrue(result is BookingValidationResult.Error)
        val msg = (result as BookingValidationResult.Error).message
        assertTrue(msg.contains("past date"))
    }

    @Test
    fun testOutsideOperatingHours_returnsError() = runBlocking {
        // Opening is 08:00, attempting to book at 07:00
        val result = repository.validateBooking(
            court = testCourt,
            bookingDate = todayStr,
            startTime = "07:00",
            durationHours = 2
        )
        assertTrue(result is BookingValidationResult.Error)
        val msg = (result as BookingValidationResult.Error).message
        assertTrue(msg.contains("operating hours"))
    }

    @Test
    fun testOverlappingBooking_returnsError() = runBlocking {
        // Pre-insert a booking from 14:00 to 16:00
        fakeDao.insertBooking(
            BookingEntity(
                id = "b1",
                bookingReference = "CS-11111",
                courtId = testCourt.id,
                courtName = testCourt.name,
                courtImageUrl = "",
                courtAddress = testCourt.address,
                bookingDate = todayStr,
                startTime = "14:00",
                durationHours = 2,
                totalPrice = 500,
                status = "UPCOMING",
                createdAt = System.currentTimeMillis()
            )
        )

        // Attempting to book 15:00 to 17:00 (overlaps with 14:00-16:00)
        val result = repository.validateBooking(
            court = testCourt,
            bookingDate = todayStr,
            startTime = "15:00",
            durationHours = 2
        )
        assertTrue(result is BookingValidationResult.Error)
        val msg = (result as BookingValidationResult.Error).message
        assertTrue(msg.contains("already booked"))
    }

    @Test
    fun testAdjacentBooking_returnsSuccess() = runBlocking {
        // Pre-insert a booking from 14:00 to 16:00
        fakeDao.insertBooking(
            BookingEntity(
                id = "b1",
                bookingReference = "CS-11111",
                courtId = testCourt.id,
                courtName = testCourt.name,
                courtImageUrl = "",
                courtAddress = testCourt.address,
                bookingDate = todayStr,
                startTime = "14:00",
                durationHours = 2,
                totalPrice = 500,
                status = "UPCOMING",
                createdAt = System.currentTimeMillis()
            )
        )

        // Attempting to book adjacent slot 16:00 to 18:00 (no overlap)
        val result = repository.validateBooking(
            court = testCourt,
            bookingDate = todayStr,
            startTime = "16:00",
            durationHours = 2
        )
        assertTrue(result is BookingValidationResult.Success)
    }

    @Test
    fun testCreateBooking_calculatesPriceCorrectly() = runBlocking {
        val result = repository.createBooking(
            court = testCourt,
            bookingDate = todayStr,
            startTime = "10:00",
            durationHours = 3
        )
        assertTrue(result.isSuccess)
        val booking = result.getOrNull()!!
        assertEquals(750, booking.totalPrice) // 250 * 3
        assertTrue(booking.bookingReference.startsWith("CS-"))
    }
}
