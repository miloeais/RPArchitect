package com.rparchitect.app.data.repository

import com.rparchitect.app.data.model.RPWorldEntry

class WorldEntryRepository {

    private val entries = mutableListOf<RPWorldEntry>()

    fun getAllEntries(): List<RPWorldEntry> {
        return entries.toList()
    }

    fun getEntriesByProject(projectId: Long): List<RPWorldEntry> {
        return entries.filter {
            it.projectId == projectId
        }
    }

    fun getEntriesByParent(parentId: Long?): List<RPWorldEntry> {
        return entries.filter {
            it.parentId == parentId
        }
    }

    fun addEntry(entry: RPWorldEntry) {
        entries.add(entry)
    }

    fun updateEntry(entry: RPWorldEntry) {
        val index = entries.indexOfFirst {
            it.id == entry.id
        }

        if (index != -1) {
            entries[index] = entry
        }
    }

    fun deleteEntry(entryId: Long) {
        entries.removeAll {
            it.id == entryId || it.parentId == entryId
        }
    }
}
