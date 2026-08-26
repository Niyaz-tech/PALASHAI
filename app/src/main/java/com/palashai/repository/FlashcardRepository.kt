package com.palashai.repository

import com.palashai.data.Flashcard
import com.palashai.data.FlashcardTopic

object FlashcardRepository {

    fun getTopicsByClass(classLevel: Int): List<FlashcardTopic> {
        return allTopics.filter { it.classLevel == classLevel }
    }

    private val allTopics = listOf(
        // CLASS 1
        FlashcardTopic("c1_t1", "Numbers 1-10", 1, listOf(
            Flashcard("1", "One", "1", "The first number.", "एक", "एक"),
            Flashcard("2", "Two", "2", "", "दो", "दो"),
            Flashcard("3", "Three", "3", "", "तीन", "तीन"),
            Flashcard("4", "Four", "4", "", "चार", "चार"),
            Flashcard("5", "Five", "5", "", "पाँच", "पाँच")
        )),
        FlashcardTopic("c1_t2", "Colours", 1, listOf(
            Flashcard("6", "Red", "Red Colour", "", "लाल रंग", "लाल रंग"),
            Flashcard("7", "Green", "Green Colour", "", "हरा रंग", "हरा रंग"),
            Flashcard("8", "Blue", "Blue Colour", "", "नीला रंग", "नीला रंग"),
            Flashcard("9", "Yellow", "Yellow Colour", "", "पीला रंग", "पीला रंग"),
            Flashcard("10", "Black", "Black Colour", "", "काला रंग", "काला रंग")
        )),
        FlashcardTopic("c1_t3", "Greetings", 1, listOf(
            Flashcard("11", "Hello", "Namaste", "", "नमस्ते", "नमस्ते"),
            Flashcard("12", "Hello Children", "Namaste Bachon", "", "नमस्ते बच्चों", "नमस्ते बच्चों"),
            Flashcard("13", "Good Morning", "Suprabhat", "", "सुप्रभात", "सुप्रभात"),
            Flashcard("14", "Thank You", "Dhanyavad", "", "धन्यवाद", "धन्यवाद")
        )),

        // CLASS 2
        FlashcardTopic("c2_t1", "Classroom Instructions", 2, listOf(
            Flashcard("15", "Sit down", "Baith jao", "", "बैठ जाओ", "बैठ जाओ"),
            Flashcard("16", "Stand up", "Khade ho jao", "", "खड़े हो जाओ", "खड़े हो जाओ"),
            Flashcard("17", "Listen carefully", "Dhyan se suno", "", "ध्यान से सुनो", "ध्यान से सुनो"),
            Flashcard("18", "Open the book", "Kitab kholo", "", "किताब खोलो", "किताब खोलो")
        )),
        FlashcardTopic("c2_t2", "Questions", 2, listOf(
            Flashcard("19", "What is your name?", "Aapka naam kya hai?", "", "आपका नाम क्या है", "आपका नाम क्या है"),
            Flashcard("20", "How are you?", "Aap kaise hain?", "", "आप कैसे हैं", "आप कैसे हैं"),
            Flashcard("21", "Did you understand?", "Samajh mein aaya?", "", "समझ में आया", "समझ में आया")
        ))
    )
}
