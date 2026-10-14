package com.example.courtsmash.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourtDao {
    @Query("SELECT * FROM courts ORDER BY rating DESC")
    fun getAllCourts(): Flow<List<CourtEntity>>

    @Query("SELECT * FROM courts WHERE id = :id")
    fun getCourtById(id: String): Flow<CourtEntity?>

    @Query("SELECT * FROM courts WHERE id = :id")
    suspend fun getCourtByIdSync(id: String): CourtEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourts(courts: List<CourtEntity>)

    @Query("SELECT COUNT(*) FROM courts")
    suspend fun getCourtsCount(): Int
}
