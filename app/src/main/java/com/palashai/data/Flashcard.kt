package com.palashai.data

data class Flashcard(
    val id: String,
    val hindi: String,
    val santali: String,
    val english: String = "",
    val emoji: String = ""
)

data class FlashcardTopic(
    val id: String,
    val title: String,
    val classLevel: Int,
    val cards: List<Flashcard>
)
