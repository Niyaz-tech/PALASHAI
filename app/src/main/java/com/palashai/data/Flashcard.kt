package com.palashai.data

data class Flashcard(
    val id: String,
    val question: String,
    val answer: String,
    val explanation: String = "",
    val hindiQuestion: String = "",
    val hindiAnswer: String = ""
)

data class FlashcardTopic(
    val id: String,
    val title: String,
    val classLevel: Int,
    val cards: List<Flashcard>
)
