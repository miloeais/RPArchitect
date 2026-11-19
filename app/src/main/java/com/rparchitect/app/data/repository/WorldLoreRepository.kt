
package com.rparchitect.app.data.repository

import com.rparchitect.app.data.dao.WorldLoreDao
import com.rparchitect.app.data.model.RPWorldLore
import kotlinx.coroutines.flow.Flow

class WorldLoreRepository(
    private val worldLoreDao: WorldLoreDao
) {

    fun getAllWorldLores(): Flow<List<RPWorldLore>> {
        return worldLoreDao.getAllWorldLore()
    }

    fun getWorldLoresByProject(projectId: Long): Flow<List<RPWorldLore>> {
        return worldLoreDao.getWorldLoreByProject(projectId)
    }

    suspend fun getWorldLoreById(worldLoreId: Long): RPWorldLore? {
        return worldLoreDao.getWorldLoreById(worldLoreId)
    }

    suspend fun addWorldLore(worldLore: RPWorldLore) {
        worldLoreDao.insertWorldLore(worldLore)
    }

    suspend fun updateWorldLore(worldLore: RPWorldLore) {
        worldLoreDao.updateWorldLore(worldLore)
    }

    suspend fun deleteWorldLore(worldLoreId: Long) {
        worldLoreDao.deleteWorldLoreById(worldLoreId)
    }
}
