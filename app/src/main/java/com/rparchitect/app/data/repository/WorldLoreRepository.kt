package com.rparchitect.app.data.repository

import com.rparchitect.app.data.model.RPWorldLore

class WorldLoreRepository {

    private val worldLores = mutableListOf<RPWorldLore>()

    fun getAllWorldLores(): List<RPWorldLore> {
        return worldLores.toList()
    }

    fun getWorldLoresByProject(projectId: Long): List<RPWorldLore> {
        return worldLores.filter {
            it.projectId == projectId
        }
    }

    fun addWorldLore(worldLore: RPWorldLore) {
        worldLores.add(worldLore)
    }

    fun updateWorldLore(worldLore: RPWorldLore) {
        val index = worldLores.indexOfFirst {
            it.id == worldLore.id
        }

        if (index != -1) {
            worldLores[index] = worldLore
        }
    }

    fun deleteWorldLore(worldLoreId: Long) {
        worldLores.removeAll {
            it.id == worldLoreId
        }
    }
}
