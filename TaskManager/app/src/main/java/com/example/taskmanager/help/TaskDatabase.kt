package com.example.taskmanager.help

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TaskDao::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getInstance(context: Context): TodoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "task_database"
                    ).build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}