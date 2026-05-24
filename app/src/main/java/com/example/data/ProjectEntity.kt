package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val techStack: String, // Comma-separated (e.g., "Kotlin, Compose, Room, MVVM")
    val description: String,
    val longDescription: String,
    val stars: Int,
    val downloads: Int,
    val isSaved: Boolean = false,
    val codeSize: String, // e.g., "3.4 MB"
    val language: String, // e.g., "Kotlin"
    val creator: String, // e.g., "BraveHex" or user's email
    val isUserUploaded: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
