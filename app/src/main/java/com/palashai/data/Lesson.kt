package com.palashai.data

data class Lesson(
    val id: String,
    val classLevel: Int,
    val subject: String,
    val topic: String,
    val hindiText: String,
    val santaliText: String,
    val objective: String,
    val activity: String,
    val assessment: String
)
