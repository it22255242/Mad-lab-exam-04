package com.example.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val timestamp: String
)


