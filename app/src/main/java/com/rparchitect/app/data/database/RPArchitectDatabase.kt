package com.rparchitect.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rparchitect.app.data.dao.CharacterDao
import com.rparchitect.app.data.dao.ProjectDao
import com.rparchitect.app.data.dao.WorldEntryDao
import com.rparchitect.app.data.dao.WorldLoreDao
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPWorldEntry
import com.rparchitect.app.data.model.RPWorldLore

@Database(
    entities = [
        RPProject::class,
        RPCharacter::class,
        RPWorldLore::class,
        RPWorldEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RPArchitectDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    abstract fun characterDao(): CharacterDao

    abstract fun worldLoreDao(): WorldLoreDao

    abstract fun worldEntryDao(): WorldEntryDao
}
