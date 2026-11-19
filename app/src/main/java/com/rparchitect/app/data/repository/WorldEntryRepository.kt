
package com.rparchitect.app.data.repository

import com.rparchitect.app.data.dao.WorldEntryDao
import com.rparchitect.app.data.model.RPWorldEntry
import kotlinx.coroutines.flow.Flow

class WorldEntryRepository(
    private val worldEntryDao: WorldEntryDao
) {

    fun getAllEntries(): Flow<List<RPWorldEntry>> {
        return worldEntryDao.getAllEntries()
    }

    fun getEntriesByProject(projectId: Long): Flow<List<RPWorldEntry>> {
        return worldEntryDao.getEntriesByProject(projectId)
    }

    fun getEntriesByParent(parentId: Long?): Flow<List<RPWorldEntry>> {
        return if (parentId == null) {
            worldEntryDao.getRootEntries()
        } else {
            worldEntryDao.getEntriesByParent(parentId)
        }
    }

    suspend fun getEntryById(entryId: Long): RPWorldEntry? {
        return worldEntryDao.getEntryById(entryId)
    }

    suspend fun addEntry(entry: RPWorldEntry) {
        worldEntryDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: RPWorldEntry) {
        worldEntryDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entryId: Long) {
        val idsToDelete = mutableSetOf<Long>()

        suspend fun collectChildren(parentId: Long) {
            if (!idsToDelete.add(parentId)) return

            val children = worldEntryDao.getChildrenOnce(parentId)

            children.forEach { child ->
                collectChildren(child.id)
            }
        }

        collectChildren(entryId)

        if (idsToDelete.isNotEmpty()) {
            worldEntryDao.deleteEntriesByIds(idsToDelete.toList())
        }
    }
}
