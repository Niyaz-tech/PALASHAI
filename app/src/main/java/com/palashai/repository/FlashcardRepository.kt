package com.palashai.repository

import com.palashai.data.Flashcard
import com.palashai.data.FlashcardTopic

object FlashcardRepository {

    fun getTopicsByClass(classLevel: Int): List<FlashcardTopic> {
        return allTopics.filter { it.classLevel == classLevel }
    }

    private val allTopics = listOf(
        // GRADE 1
        FlashcardTopic("g1_t1", "Numbers & Counting", 1, listOf(
            Flashcard("101", "एक", "ᱢᱤᱫ", "One", "1️⃣"),
            Flashcard("102", "दो", "ᱵᱟᱨ", "Two", "2️⃣"),
            Flashcard("103", "तीन", "ᱯᱮ", "Three", "3️⃣"),
            Flashcard("104", "चार", "ᱯᱩᱱ", "Four", "4️⃣"),
            Flashcard("105", "पाँच", "ᱢᱚᱬᱮ", "Five", "5️⃣")
        )),
        FlashcardTopic("g1_t2", "Colours & Shapes", 1, listOf(
            Flashcard("111", "लाल", "ᱟᱨᱟᱜ", "Red", "🔴"),
            Flashcard("112", "नीला", "ᱞᱤᱞ", "Blue", "🔵"),
            Flashcard("113", "हरा", "ᱦᱟᱹᱨᱤᱭᱟᱹᱲ", "Green", "🟢"),
            Flashcard("114", "गोल", "ᱜᱩᱞᱟᱹᱴ", "Circle", "⭕"),
            Flashcard("115", "तिकोना", "ᱯᱮᱠᱚᱬ", "Triangle", "🔺")
        )),
        FlashcardTopic("g1_t3", "Family", 1, listOf(
            Flashcard("121", "पिता", "ᱵᱟᱵᱟ", "Father", "👨"),
            Flashcard("122", "माता", "ᱟᱭᱳ", "Mother", "👩"),
            Flashcard("123", "भाई", "ᱵᱚᱭᱦᱟ", "Brother", "👦"),
            Flashcard("124", "बहन", "ᱢᱤᱥᱨᱟ", "Sister", "👧"),
            Flashcard("125", "दादा/नाना", "ᱜᱚᱲᱚᱢ ᱵᱟᱵᱟ", "Grandfather", "👴")
        )),
        FlashcardTopic("g1_t4", "Animals", 1, listOf(
            Flashcard("131", "गाय", "ᱜᱟᱹᱭ", "Cow", "🐄"),
            Flashcard("132", "कुत्ता", "ᱥᱮᱛᱟ", "Dog", "🐕"),
            Flashcard("133", "बकरी", "ᱢᱮᱨᱚᱢ", "Goat", "🐐"),
            Flashcard("134", "बिल्ली", "ᱯᱩᱥᱤ", "Cat", "🐈"),
            Flashcard("135", "हाथी", "ᱦᱟᱹᱛᱤ", "Elephant", "🐘")
        )),
        FlashcardTopic("g1_t5", "Body Parts", 1, listOf(
            Flashcard("141", "आँख", "ᱢᱮᱫ", "Eye", "👁️"),
            Flashcard("142", "नाक", "ᱢᱩ", "Nose", "👃"),
            Flashcard("143", "कान", "ᱞᱩᱛᱩᱨ", "Ear", "👂"),
            Flashcard("144", "हाथ", "ᱛᱤ", "Hand", "✋"),
            Flashcard("145", "पैर", "ᱡᱟᱸᱜᱟ", "Leg", "🦶")
        )),

        // GRADE 2
        FlashcardTopic("g2_t1", "Numbers & Operations", 2, listOf(
            Flashcard("201", "जोड़ना", "ᱢᱮᱥᱟ", "Addition", "➕"),
            Flashcard("202", "घटाना", "ᱵᱷᱮᱜᱟᱨ", "Subtraction", "➖"),
            Flashcard("203", "ग्यारह", "ᱜᱮᱞ ᱢᱤᱫ", "Eleven", "11"),
            Flashcard("204", "बीस", "ᱵᱟᱨ ᱜᱮᱞ", "Twenty", "20"),
            Flashcard("205", "गिनती", "ᱞᱮᱠᱷᱟ", "Counting", "🧮")
        )),
        FlashcardTopic("g2_t2", "Time & Calendar", 2, listOf(
            Flashcard("211", "आज", "ᱛᱮᱦᱮᱧ", "Today", "📅"),
            Flashcard("212", "कल (आने वाला)", "ᱜᱟᱯᱟ", "Tomorrow", "⏭️"),
            Flashcard("213", "दिन", "ᱢᱟᱦᱟᱸ", "Day", "☀️"),
            Flashcard("214", "रात", "ᱧᱤᱫᱟᱹ", "Night", "🌙"),
            Flashcard("215", "समय", "ᱚᱠᱛᱚ", "Time", "⏰")
        )),
        FlashcardTopic("g2_t3", "Plants", 2, listOf(
            Flashcard("221", "पेड़", "ᱫᱟᱨᱮ", "Tree", "🌳"),
            Flashcard("222", "पत्ती", "ᱥᱟᱠᱟᱢ", "Leaf", "🍃"),
            Flashcard("223", "फूल", "ᱵᱟᱦᱟ", "Flower", "🌸"),
            Flashcard("224", "फल", "ᱡᱚ", "Fruit", "🍎"),
            Flashcard("225", "जड़", "ᱨᱮᱦᱮᱫ", "Root", "🌱")
        )),
        FlashcardTopic("g2_t4", "Animals", 2, listOf(
            Flashcard("231", "शेर", "ᱠᱩᱞ", "Lion", "🦁"),
            Flashcard("232", "पक्षी", "ᱪᱮᱬᱮ", "Bird", "🐦"),
            Flashcard("233", "सांप", "ᱵᱤᱧ", "Snake", "🐍"),
            Flashcard("234", "मछली", "ᱦᱟᱹᱠᱩ", "Fish", "🐟"),
            Flashcard("235", "बंदर", "ᱜᱟᱹᱰᱤ", "Monkey", "🐒")
        )),
        FlashcardTopic("g2_t5", "Food & Nutrition", 2, listOf(
            Flashcard("241", "भोजन", "ᱡᱚᱢᱟᱜ", "Food", "🍲"),
            Flashcard("242", "पानी", "ᱫᱟᱜ", "Water", "💧"),
            Flashcard("243", "दूध", "ᱛᱳᱣᱟ", "Milk", "🥛"),
            Flashcard("244", "चावल", "ᱫᱟᱠᱟ", "Rice", "🍚"),
            Flashcard("245", "फल", "ᱡᱚ", "Fruit", "🍇")
        )),

        // GRADE 3
        FlashcardTopic("g3_t1", "Multiplication & Division", 3, listOf(
            Flashcard("301", "गुणा", "ᱜᱟᱱᱟᱣ", "Multiplication", "✖️"),
            Flashcard("302", "भाग", "ᱦᱟᱹᱴᱤᱧ", "Division", "➗"),
            Flashcard("303", "पहाड़ा", "ᱱᱮᱣᱛᱟ", "Table", "📝"),
            Flashcard("304", "बराबर", "ᱥᱚᱢᱟᱱ", "Equal", "＝"),
            Flashcard("305", "हिसाब", "ᱦᱤᱥᱟᱹᱵᱽ", "Calculation", "📓")
        )),
        FlashcardTopic("g3_t2", "Fractions", 3, listOf(
            Flashcard("311", "आधा", "ᱟᱫᱷᱟ", "Half", "🍕"),
            Flashcard("312", "तिहाई", "ᱯᱮ ᱦᱟᱹᱴᱤᱧ", "One-third", "🍰"),
            Flashcard("313", "चौथाई", "ᱯᱩᱱ ᱦᱟᱹᱴᱤᱧ", "Quarter", "🥧")
        )),
        FlashcardTopic("g3_t3", "Time & Measurement", 3, listOf(
            Flashcard("321", "लंबाई", "ᱡᱤᱞᱤᱧ", "Length", "📏"),
            Flashcard("322", "वजन", "ᱦᱟᱢᱟᱞ", "Weight", "⚖️"),
            Flashcard("323", "लीटर", "ᱞᱤᱴᱟᱨ", "Litre", "🧪")
        )),
        FlashcardTopic("g3_t4", "Human Body", 3, listOf(
            Flashcard("331", "शरीर", "ᱦᱚᱲᱢᱚ", "Body", "🚶"),
            Flashcard("332", "हड्डी", "ᱡᱟᱝ", "Bone", "🦴"),
            Flashcard("333", "रक्त/खून", "ᱢᱟᱭᱟᱢ", "Blood", "🩸"),
            Flashcard("334", "दिमाग", "ᱦᱟᱛᱟᱝ", "Brain", "🧠")
        )),
        FlashcardTopic("g3_t5", "Environment", 3, listOf(
            Flashcard("341", "धरती", "ᱫᱷᱟᱹᱨᱛᱤ", "Earth", "🌍"),
            Flashcard("342", "आकाश", "ᱥᱮᱨᱢᱟ", "Sky", "☁️"),
            Flashcard("343", "नदी", "ᱜᱟᱰᱟ", "River", "🌊"),
            Flashcard("344", "हवा", "ᱦᱚᱭ", "Air", "💨")
        )),

        // GRADE 4
        FlashcardTopic("g4_t1", "Large Numbers", 4, listOf(
            Flashcard("401", "हजार", "ᱜᱮᱞ ᱥᱟᱭ", "Thousand", "1000"),
            Flashcard("402", "लाख", "ᱞᱟᱠᱷ", "Lakh", "💰"),
            Flashcard("403", "करोड़", "ᱠᱳᱴᱤ", "Crore", "💎")
        )),
        FlashcardTopic("g4_t2", "Geometry", 4, listOf(
            Flashcard("411", "कोणा", "ᱠᱚᱬ", "Angle", "📐"),
            Flashcard("412", "रेखा", "ᱜᱟᱨ", "Line", "✏️"),
            Flashcard("413", "बिंदु", "ᱴᱩᱰᱟᱹᱜ", "Point", "📍")
        )),
        FlashcardTopic("g4_t3", "Grammar", 4, listOf(
            Flashcard("421", "नाम", "ᱧᱩᱛᱩᱢ", "Noun", "📛"),
            Flashcard("422", "काम", "ᱠᱟᱹᱢᱤ", "Verb", "🛠️"),
            Flashcard("423", "गुण", "ᱜᱩᱱ", "Adjective", "⭐")
        )),
        FlashcardTopic("g4_t4", "Water & Environment", 4, listOf(
            Flashcard("431", "बादल", "ᱨᱤᱢᱤᱞ", "Cloud", "☁️"),
            Flashcard("432", "बारिश", "ᱫᱟᱜ", "Rain", "🌧️"),
            Flashcard("433", "बिजली", "ᱤᱯᱤᱞ", "Electricity", "⚡")
        )),
        FlashcardTopic("g4_t5", "Basic Science", 4, listOf(
            Flashcard("441", "ऊर्जा", "ᱫᱟᱲᱮ", "Energy", "🔋"),
            Flashcard("442", "प्रकाश", "ᱢᱟᱨᱥᱟᱞ", "Light", "💡"),
            Flashcard("443", "ताप", "ᱞᱚᱞᱚ", "Heat", "🔥")
        ))
    )
}
