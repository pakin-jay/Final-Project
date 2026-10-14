package com.example.courtsmash.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CourtEntity::class, BookingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CourtSmashDatabase : RoomDatabase() {

    abstract fun courtDao(): CourtDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: CourtSmashDatabase? = null

        fun getDatabase(context: Context): CourtSmashDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CourtSmashDatabase::class.java,
                    "courtsmash_database"
                )
                .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
