package com.rparchitect.app.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: RPArchitectDatabase? = null

    fun getDatabase(context: Context): RPArchitectDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                RPArchitectDatabase::class.java,
                "rp_architect_database"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}
