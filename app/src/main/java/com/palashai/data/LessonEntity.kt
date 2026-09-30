package com.palashai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey
    val id: String,
    val classLevel: Int,
    val subject: String,
    val topic: String,
    val explanation: String,
    val hindiText: String,
    val santaliText: String,
    val objective: String,
    val activity: String,
    val assessment: String
)
