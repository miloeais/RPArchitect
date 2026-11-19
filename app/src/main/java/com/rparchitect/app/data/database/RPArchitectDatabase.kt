package com.rparchitect.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rparchitect.app.data.dao.CharacterDao
import com.rparchitect.app.data.dao.CustomTemplateDao
import com.rparchitect.app.data.dao.ProjectDao
import com.rparchitect.app.data.dao.WorldEntryDao
import com.rparchitect.app.data.dao.WorldLoreDao
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPCustomTemplate
import com.rparchitect.app.data.model.RPCustomTemplateField
import com.rparchitect.app.data.model.RPCustomTemplateOption
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPWorldEntry
import com.rparchitect.app.data.model.RPWorldLore

@Database(
    entities = [
        RPProject::class,
        RPCharacter::class,
        RPWorldLore::class,
        RPWorldEntry::class,
        RPCustomTemplate::class,
        RPCustomTemplateField::class,
        RPCustomTemplateOption::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RPArchitectDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    abstract fun characterDao(): CharacterDao

    abstract fun worldLoreDao(): WorldLoreDao

    abstract fun worldEntryDao(): WorldEntryDao

    abstract fun customTemplateDao(): CustomTemplateDao
}
