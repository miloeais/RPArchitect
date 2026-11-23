
package com.rparchitect.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rparchitect.app.data.model.RPWorldLore
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldLoreDao {

    @Query("SELECT * FROM world_lore ORDER BY createdAt DESC")
    fun getAllWorldLore(): Flow<List<RPWorldLore>>

    @Query("SELECT * FROM world_lore WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getWorldLoreByProject(projectId: Long): Flow<List<RPWorldLore>>

    @Query("SELECT * FROM world_lore WHERE id = :loreId LIMIT 1")
    suspend fun getWorldLoreById(loreId: Long): RPWorldLore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorldLore(lore: RPWorldLore)

    @Update
    suspend fun updateWorldLore(lore: RPWorldLore)

    @Delete
    suspend fun deleteWorldLore(lore: RPWorldLore)

    @Query("DELETE FROM world_lore WHERE id = :loreId")
    suspend fun deleteWorldLoreById(loreId: Long)
}
