
package com.rparchitect.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rparchitect.app.data.model.RPWorldEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldEntryDao {

    @Query("SELECT * FROM world_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<RPWorldEntry>>

    @Query("SELECT * FROM world_entries WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getEntriesByProject(projectId: Long): Flow<List<RPWorldEntry>>

    @Query("SELECT * FROM world_entries WHERE parentId IS NULL ORDER BY name ASC")
    fun getRootEntries(): Flow<List<RPWorldEntry>>

    @Query("SELECT * FROM world_entries WHERE parentId = :parentId ORDER BY name ASC")
    fun getEntriesByParent(parentId: Long): Flow<List<RPWorldEntry>>

    @Query("SELECT * FROM world_entries WHERE parentId = :parentId")
    suspend fun getChildrenOnce(parentId: Long): List<RPWorldEntry>

    @Query("SELECT * FROM world_entries WHERE id = :entryId LIMIT 1")
    suspend fun getEntryById(entryId: Long): RPWorldEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: RPWorldEntry)

    @Update
    suspend fun updateEntry(entry: RPWorldEntry)

    @Delete
    suspend fun deleteEntry(entry: RPWorldEntry)

    @Query("DELETE FROM world_entries WHERE id IN (:entryIds)")
    suspend fun deleteEntriesByIds(entryIds: List<Long>)

    @Query("DELETE FROM world_entries WHERE id = :entryId")
    suspend fun deleteEntryById(entryId: Long)
}
