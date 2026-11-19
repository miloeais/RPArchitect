
package com.rparchitect.app.data.repository

import com.rparchitect.app.data.dao.CharacterDao
import com.rparchitect.app.data.model.RPCharacter
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val characterDao: CharacterDao
) {

    fun getAllCharacters(): Flow<List<RPCharacter>> {
        return characterDao.getAllCharacters()
    }

    fun getCharactersByProject(projectId: Long): Flow<List<RPCharacter>> {
        return characterDao.getCharactersByProject(projectId)
    }

    suspend fun getCharacterById(characterId: Long): RPCharacter? {
        return characterDao.getCharacterById(characterId)
    }

    suspend fun addCharacter(character: RPCharacter) {
        characterDao.insertCharacter(character)
    }

    suspend fun updateCharacter(character: RPCharacter) {
        characterDao.updateCharacter(character)
    }

    suspend fun deleteCharacter(characterId: Long) {
        characterDao.deleteCharacterById(characterId)
    }
}
