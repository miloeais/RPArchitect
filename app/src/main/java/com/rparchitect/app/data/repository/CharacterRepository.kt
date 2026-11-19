package com.rparchitect.app.data.repository

import com.rparchitect.app.data.model.RPCharacter

class CharacterRepository {

    private val characters = mutableListOf<RPCharacter>()

    fun getAllCharacters(): List<RPCharacter> {
        return characters.toList()
    }

    fun getCharactersByProject(projectId: Long): List<RPCharacter> {
        return characters.filter {
            it.projectId == projectId
        }
    }

    fun addCharacter(character: RPCharacter) {
        characters.add(character)
    }

    fun updateCharacter(character: RPCharacter) {
        val index = characters.indexOfFirst {
            it.id == character.id
        }

        if (index != -1) {
            characters[index] = character
        }
    }

    fun deleteCharacter(characterId: Long) {
        characters.removeAll {
            it.id == characterId
        }
    }
}
