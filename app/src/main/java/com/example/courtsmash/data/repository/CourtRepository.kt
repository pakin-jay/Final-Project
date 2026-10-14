package com.example.courtsmash.data.repository

import com.example.courtsmash.data.local.CourtDao
import com.example.courtsmash.data.local.CourtEntity
import com.example.courtsmash.data.local.CourtSeedData
import com.example.courtsmash.domain.model.Court
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CourtRepository(private val courtDao: CourtDao) {

    val allCourts: Flow<List<Court>> = courtDao.getAllCourts().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun ensureSeedData() = withContext(Dispatchers.IO) {
        if (courtDao.getCourtsCount() < CourtSeedData.sampleCourts.size) {
            val entities = CourtSeedData.sampleCourts.map { CourtEntity.fromDomainModel(it) }
            courtDao.insertCourts(entities)
        }
    }

    fun getCourtById(id: String): Flow<Court?> {
        return courtDao.getCourtById(id).map { it?.toDomainModel() }
    }

    suspend fun getCourtByIdSync(id: String): Court? = withContext(Dispatchers.IO) {
        courtDao.getCourtByIdSync(id)?.toDomainModel()
    }
}
