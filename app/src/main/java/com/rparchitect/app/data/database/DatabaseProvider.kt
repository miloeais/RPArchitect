
package com.rparchitect.app.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE characters ADD COLUMN occupation TEXT NOT NULL DEFAULT ''"
        )

        db.execSQL(
            "ALTER TABLE characters ADD COLUMN relationship TEXT NOT NULL DEFAULT ''"
        )
    }
}

object DatabaseProvider {

    @Volatile
    private var INSTANCE: RPArchitectDatabase? = null

    fun getDatabase(context: Context): RPArchitectDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                RPArchitectDatabase::class.java,
                "rp_architect_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}
