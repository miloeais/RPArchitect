
package com.rparchitect.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rparchitect.app.data.model.RPCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY createdAt DESC")
    fun getAllCharacters(): Flow<List<RPCharacter>>

    @Query("SELECT * FROM characters WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getCharactersByProject(projectId: Long): Flow<List<RPCharacter>>

    @Query("SELECT * FROM characters WHERE id = :characterId LIMIT 1")
    suspend fun getCharacterById(characterId: Long): RPCharacter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: RPCharacter)

    @Update
    suspend fun updateCharacter(character: RPCharacter)

    @Delete
    suspend fun deleteCharacter(character: RPCharacter)

    @Query("DELETE FROM characters WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Long)
}
