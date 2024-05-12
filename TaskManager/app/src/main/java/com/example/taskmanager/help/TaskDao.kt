package com.example.taskmanager.help

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.model.Task

interface TaskDao {
    @Query("SELECT * FROM taskTable ORDER BY timestamp DESC")
    fun getAllTodos(): List<Task>

    @Insert
    fun insert(todo: Task): Long

    @Update
    fun update(todo: Task)

    @Delete
    fun delete(todo: Task)
}