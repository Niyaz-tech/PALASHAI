package com.palashai.repository

import com.palashai.data.Worksheet
import com.palashai.data.WorksheetTask

object WorksheetRepository {

    fun getAllWorksheets(): List<Worksheet> = allWorksheets

    fun getWorksheetById(id: String): Worksheet? = allWorksheets.find { it.id == id }

    private val allWorksheets = listOf(
        // GRADE 1
        Worksheet(
            id = "w101", grade = 1, subject = "Math", title = "Counting Animals", emoji = "🔢",
            hindiInstruction = "नीचे दिए गए चित्रों को गिनें और सही संख्या लिखें।",
            santaliInstruction = "ᱞᱟᱛᱟᱨ ᱨᱮ ᱮᱢ ᱟᱠᱟᱱ ᱪᱤᱛᱟᱹᱨ ᱠᱚ ᱞᱮᱠᱷᱟᱭ ᱯᱮ ᱟᱨ ᱥᱟᱹᱦᱤ ᱞᱮᱠᱷᱟ ᱚᱞ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "कितनी गाय हैं? (How many cows?)", "ᱛᱤᱱᱟᱹᱜ ᱜᱟᱹᱭ ᱢᱮᱱᱟᱜ ᱠᱚᱣᱟ?", listOf("2", "3", "5"), "3"),
                WorksheetTask("t2", "कितने कुत्ते हैं? (How many dogs?)", "ᱛᱤᱱᱟᱹᱜ ᱥᱮᱛᱟ ᱢᱮᱱᱟᱜ ᱠᱚᱣᱟ?", listOf("1", "4", "2"), "4")
            )
        ),
        Worksheet(
            id = "w102", grade = 1, subject = "EVS", title = "My Body Parts", emoji = "👃",
            hindiInstruction = "सही शरीर के अंग का मिलान करें।",
            santaliInstruction = "ᱦᱚᱲᱢᱚ ᱨᱮᱱᱟᱜ ᱥᱟᱹᱦᱤ ᱦᱟᱹᱴᱤᱧ ᱥᱟᱶ ᱢᱤᱞᱟᱹᱣ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "सूँघने के लिए किसका उपयोग करते हैं?", "ᱥᱚ ᱞᱟᱹᱜᱤᱫ ᱪᱮᱫ ᱵᱚᱱ ᱵᱮᱵᱷᱟᱨᱟ?", listOf("आँख", "नाक", "कान"), "नाक"),
                WorksheetTask("t2", "सुनने के लिए किसका उपयोग करते हैं?", "ᱟᱸᱡᱚᱢ ᱞᱟᱹᱜᱤᱫ ᱪᱮᱫ ᱵᱚᱱ ᱵᱮᱵᱷᱟᱨᱟ?", listOf("हाथ", "मुँह", "कान"), "कान")
            )
        ),

        // GRADE 2
        Worksheet(
            id = "w201", grade = 2, subject = "Math", title = "Simple Addition", emoji = "➕",
            hindiInstruction = "नीचे दिए गए जोड़ के सवालों को हल करें।",
            santaliInstruction = "ᱞᱟᱛᱟᱨ ᱨᱮ ᱮᱢ ᱟᱠᱟᱱ ᱢᱮᱥᱟ ᱞᱮᱠᱷᱟ ᱠᱚ ᱥᱚᱞᱦᱮ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "10 + 5 = ?", "ᱜᱮᱞ + ᱢᱚᱬᱮ = ?", listOf("12", "15", "18"), "15"),
                WorksheetTask("t2", "8 + 4 = ?", "ᱜᱮᱞ ᱵᱟᱨ + ᱯᱩᱱ = ?", listOf("10", "12", "14"), "12")
            )
        ),
        Worksheet(
            id = "w202", grade = 2, subject = "Science", title = "Plant Parts", emoji = "🌿",
            hindiInstruction = "पौधे के भागों को पहचानें।",
            santaliInstruction = "ᱫᱟᱨᱮ ᱨᱮᱱᱟᱜ ᱦᱟᱹᱴᱤᱧ ᱠᱚ ᱩᱨᱩᱢ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "पौधे का कौन सा भाग जमीन के अंदर होता है?", "ᱫᱟᱨᱮ ᱨᱮᱱᱟᱜ ᱚᱠᱟ ᱦᱟᱹᱴᱤᱧ ᱦᱟᱥᱟ ᱞᱟᱛᱟᱨ ᱨᱮ ᱛᱟᱦᱮᱸᱱᱟ?", listOf("फूल", "पत्ती", "जड़"), "जड़"),
                WorksheetTask("t2", "पौधे का सुंदर और रंगीन भाग क्या है?", "ᱫᱟᱨᱮ ᱨᱮᱱᱟᱜ ᱪᱚᱨᱚᱠ ᱟᱨ ᱨᱚᱝ ᱟᱱ ᱦᱟᱹᱴᱤᱧ ᱫᱚ ᱪᱮᱫ?", listOf("तना", "फूल", "बीज"), "फूल")
            )
        ),

        // GRADE 3
        Worksheet(
            id = "w301", grade = 3, subject = "Math", title = "Multiplication Fun", emoji = "✖️",
            hindiInstruction = "गुणा करें और सही उत्तर चुनें।",
            santaliInstruction = "ᱜᱟᱱᱟᱣ ᱯᱮ ᱟᱨ ᱥᱟᱹᱦᱤ ᱛᱮᱞᱟ ᱵᱟᱪᱷᱟᱣ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "3 x 4 = ?", "ᱯᱮ x ᱯᱩᱱ = ?", listOf("7", "12", "10"), "12"),
                WorksheetTask("t2", "5 x 2 = ?", "ᱢᱚᱬᱮ x ᱵᱟᱨ = ?", listOf("10", "7", "15"), "10")
            )
        ),
        Worksheet(
            id = "w302", grade = 3, subject = "EVS", title = "Our Environment", emoji = "🌍",
            hindiInstruction = "पर्यावरण सुरक्षा के बारे में सही विकल्प चुनें।",
            santaliInstruction = "ᱯᱚᱨᱤᱣᱮᱥ ᱵᱟᱧᱪᱟᱣ ᱵᱟᱵᱚᱛ ᱥᱟᱹᱦᱤ ᱵᱟᱪᱷᱟᱣ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "हमें कचरा कहाँ फेंकना चाहिए?", "ᱵᱚᱱ ᱚᱠᱟ ᱨᱮ ᱡᱷᱚᱨᱟ ᱜᱤᱰᱤ ᱞᱟᱹᱠᱛᱤ ᱠᱟᱱᱟ?", listOf("सड़क पर", "कचरे के डिब्बे में", "नदी में"), "कचरे के डिब्बे में"),
                WorksheetTask("t2", "हमें पानी का क्या करना चाहिए?", "ᱵᱚᱱ ᱫᱟᱜ ᱪᱮᱫ ᱞᱟᱹᱠᱛᱤ ᱠᱟᱱᱟ?", listOf("बर्बाद", "बचत", "प्रदूषित"), "बचत")
            )
        ),

        // GRADE 4
        Worksheet(
            id = "w401", grade = 4, subject = "Language", title = "Hindi Grammar", emoji = "📖",
            hindiInstruction = "संज्ञा और सर्वनाम की पहचान करें।",
            santaliInstruction = "ᱧᱩᱛᱩᱢ ᱟᱨ ᱩᱪᱟᱹᱲ ᱧᱩᱛᱩᱢ ᱩᱨᱩᱢ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "'राम स्कूल जा रहा है' में संज्ञा क्या है?", "'Ram school senok kana' ᱨᱮ ᱧᱩᱛᱩᱢ ᱫᱚ ᱚᱠᱟ ᱠᱟᱱᱟ?", listOf("राम", "जा रहा", "स्कूल"), "राम"),
                WorksheetTask("t2", "'वह खेल रहा है' में सर्वनाम क्या है?", "'Uni enec kana' ᱨᱮ ᱩᱪᱟᱹᱲ ᱧᱩᱛᱩᱢ ᱫᱚ ᱚᱠᱟ ᱠᱟᱱᱟ?", listOf("खेल", "रहा", "वह"), "वह")
            )
        ),
        Worksheet(
            id = "w402", grade = 4, subject = "Science", title = "Water Cycle", emoji = "☁️",
            hindiInstruction = "जल चक्र की प्रक्रियाओं को समझें।",
            santaliInstruction = "ᱫᱟᱜ ᱪᱟᱠᱷᱟ ᱨᱮᱱᱟᱜ ᱠᱟᱹᱢᱤ ᱠᱚ ᱵᱩᱡᱷᱟᱹᱣ ᱯᱮ᱾",
            tasks = listOf(
                WorksheetTask("t1", "बादल कैसे बनते हैं?", "ᱨᱤᱢᱤᱞ ᱪᱮᱞᱮᱠᱟᱛᱮ ᱵᱮᱱᱟᱜᱼᱟ?", listOf("वाष्पीकरण", "मिट्टी", "हवा"), "वाष्पीकरण"),
                WorksheetTask("t2", "वर्षा का पानी कहाँ जाता है?", "ᱫᱟᱜ ᱡᱟᱹᱲᱤ ᱫᱟᱜ ᱚᱠᱟ ᱥᱮᱫ ᱥᱮᱱᱚᱜᱼᱟ?", listOf("आकाश", "नदी और समुद्र", "सूरज"), "नदी और समुद्र")
            )
        )
    )
}
