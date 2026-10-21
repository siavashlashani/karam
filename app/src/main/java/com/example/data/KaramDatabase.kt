package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class, RoutineEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KaramDatabase : RoomDatabase() {
    abstract fun karamDao(): KaramDao

    companion object {
        @Volatile
        private var INSTANCE: KaramDatabase? = null

        fun getDatabase(context: Context): KaramDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KaramDatabase::class.java,
                    "karam_local_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
