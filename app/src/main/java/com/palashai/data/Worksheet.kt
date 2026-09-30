package com.palashai.data

data class Worksheet(
    val id: String,
    val grade: Int,
    val subject: String,
    val title: String,
    val emoji: String,
    val hindiInstruction: String,
    val santaliInstruction: String,
    val tasks: List<WorksheetTask>
)

data class WorksheetTask(
    val id: String,
    val hindiQuestion: String,
    val santaliQuestion: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String = ""
)
