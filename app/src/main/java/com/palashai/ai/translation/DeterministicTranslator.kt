package com.palashai.ai.translation

/**
 * DeterministicTranslator provides a curated, offline mapping for Hindi to Santali translation.
 * This is used for the SIH 2026 MVP to demonstrate core functionality reliably.
 */
object DeterministicTranslator {

    private val dictionary = mapOf(
        // Greetings
        "नमस्ते" to "ᱡᱚᱦᱟᱨ",
        "नमस्कार" to "ᱡᱚᱦᱟᱨ",
        "सुप्रभात" to "ᱥᱮᱛᱟᱜ ᱡᱚᱦᱟᱨ",
        "नमस्ते बच्चों" to "ᱡᱚᱦᱟᱨ ᱜᱤᱫᱽᱨᱟᱹ ᱠᱚ",
        "नमस्कार बच्चों" to "ᱡᱚᱦᱟᱨ ᱜᱤᱫᱽᱨᱟᱹ ᱠᱚ",

        // Teacher Instructions
        "बैठ जाओ" to "ᱫᱩᱲᱩᱵ ᱯᱮ",
        "खड़े हो जाओ" to "ᱛᱤᱸᱜᱩᱱ ᱯᱮ",
        "ध्यान से सुनो" to "ᱫᱷᱮᱭᱟᱱ ᱛᱮ ᱟᱸᱡᱚᱢ ᱯᱮ",
        "मेरी बात सुनो" to "ᱤᱧᱟᱜ ᱠᱟᱛᱷᱟ ᱟᱸᱡᱚᱢ ᱯᱮ",
        "इधर देखो" to "ᱱᱚᱛᱮ ᱠᱚᱭᱚᱜᱽ ᱯᱮ",
        "बोर्ड की तरफ देखो" to "ᱵᱚᱨᱰ ᱥᱮᱫ ᱠᱚᱭᱚᱜᱽ ᱯᱮ",
        "किताब खोलो" to "ᱯᱩᱛᱷᱤ ᱡᱷᱤᱡ ᱯᱮ",
        "किताब बंद करो" to "ᱯᱩᱛᱷᱤ ᱵᱚᱸᱫᱚ ᱯᱮ",
        "अपनी किताब खोलो" to "ᱟᱯᱟᱱ ᱟᱹᱯᱤᱱ ᱯᱩᱛᱷᱤ ᱡᱷᱤᱡ ᱯᱮ",
        "किताब पढ़ो" to "ᱯᱩᱛᱷᱤ ᱯᱟᱲᱦᱟᱣ ᱯᱮ",
        "लिखो" to "ᱚᱞ ᱯᱮ",
        "पढ़ो" to "ᱯᱟᱲᱦᱟᱣ ᱯᱮ",
        "दोहराओ" to "ᱫᱚᱦᱲᱟ ᱛᱮ ᱢᱮᱱ ᱯᱮ",
        "फिर से बोलो" to "ᱟᱨᱦᱚᱸ ᱢᱮᱱ ᱢᱮ",
        "धीरे बोलो" to "ᱵᱟᱹᱭ ᱵᱟᱹᱭ ᱛᱮ ᱢᱮᱱ ᱢᱮ",
        "जोर से बोलो" to "ᱡᱚᱨ ᱛᱮ ᱢᱮᱱ ᱢᱮ",
        "हाथ उठाओ" to "ᱛᱤ ᱛᱩᱞ ᱯᱮ",
        "अपना हाथ उठाओ" to "ᱟᱯᱱᱟᱨ ᱛᱤ ᱛᱩᱞ ᱯᱮ",

        // Classroom Questions
        "आपका नाम क्या है" to "ᱟᱹᱵᱤᱱᱟᱜ ᱧᱩᱛᱩᱢ ᱫᱚ ᱪᱮᱫ?",
        "तुम्हारा नाम क्या है" to "ᱟᱢᱟᱜ ᱧᱩᱛᱩᱢ ᱫᱚ ᱪᱮᱫ?",
        "आप कैसे हैं" to "ᱟᱹᱵᱤᱱ ᱪᱮᱞᱮᱠᱟ ᱢᱮᱱᱟᱜ ᱵᱤᱱᱟ?",
        "यह क्या है" to "ᱱᱚᱣᱟ ᱫᱚ ᱪᱮᱫ ᱠᱟᱱᱟ?",
        "यह कौन सा रंग है" to "ᱱᱚᱣᱟ ᱫᱚ ᱪᱮᱫ ᱨᱚᱝ ᱠᱟᱱᱟ?",
        "कितने बच्चे हैं" to "ᱛᱤᱱᱟᱹᱜ ᱜᱤᱫᱽᱨᱟᱹ ᱢᱮᱱᱟᱜ ᱯᱮᱭᱟ?",
        "कौन जवाब देगा" to "ᱚᱠᱚᱭ ᱛᱮᱞᱟᱭ ᱮᱢᱟ?",
        "किसने यह किया" to "ᱚᱠᱚᱭ ᱱᱚᱣᱟᱭ ᱠᱟᱹᱢᱤ ᱠᱮᱫᱟ?",
        "समझ में आया" to "ᱵᱩᱡᱷᱟᱹᱣ ᱠᱮᱫᱟ ᱯᱮ?",
        "क्या तुम समझे" to "ᱪᱮᱫ ᱟᱢᱮᱢ ᱵᱩᱡᱷᱟᱹᱣ ᱠᱮᱫᱟ?",

        // Learning
        "अक्षर पढ़ो" to "ᱟᱠᱷᱚᱨ ᱯᱟᱲᱦᱟᱣ ᱢᱮ",
        "शब्द पढ़ो" to "ᱥᱟᱵᱟᱫᱽ ᱯᱟᱲᱦᱟᱣ ᱢᱮ",
        "वाक्य पढ़ो" to "ᱟᱹᱭᱟᱹᱛ ᱯᱟᱲᱦᱟᱣ ᱢᱮ",
        "गिनती बोलो" to "ᱞᱮᱠᱷᱟ ᱢᱮᱱ ᱢᱮ",
        "एक दो तीन चार पाँच" to "ᱢᱤᱫ ᱵᱟᱨ ᱯᱮ ᱯᱩᱱ ᱢᱚᱬᱮ",
        "चित्र देखो" to "ᱪᱤᱛᱟᱹᱨ ᱠᱚᱭᱚᱜᱽ ᱢᱮ",
        "चित्र बनाओ" to "ᱪᱤᱛᱟᱹᱨ ᱵᱮᱱᱟᱣ ᱢᱮ",
        "अपना नाम लिखो" to "ᱟᱯᱱᱟᱨ ᱧᱩᱛᱩᱢ ᱚᱞ ᱢᱮ",
        "उत्तर लिखो" to "ᱛᱮᱞᱟ ᱚᱞ ᱢᱮ",
        "सवाल पढ़ो" to "ᱠᱩᱠᱞᱤ ᱯᱟᱲᱦᱟᱣ ᱢᱮ",

        // Classroom Objects
        "किताब" to "ᱯᱩᱛᱷᱤ",
        "कलम" to "ᱠᱚᱞᱚᱢ",
        "पेंसिल" to "ᱯᱮᱱᱥᱤᱞ",
        "कॉपी" to "ᱠᱷᱟᱛᱟ",
        "बोर्ड" to "ᱵᱚᱨᱰ",
        "कुर्सी" to "ᱢᱟᱹᱪᱤ",
        "मेज" to "ᱢᱮᱡᱽ",
        "बैग" to "ᱡᱷᱚᱞᱟ",
        "पानी" to "ᱫᱟᱜ",

        // Classroom/Everyday
        "पानी पियो" to "ᱫᱟᱜ ᱧᱩᱭ ᱢᱮ",
        "पानी लाओ" to "ᱫᱟᱜ ᱟᱹᱜᱩᱭ ᱢᱮ",
        "यहाँ आओ" to "ᱱᱚᱸᱰᱮ ᱦᱤᱡᱩᱜ ᱢᱮ",
        "वहाँ जाओ" to "ᱦᱟᱸᱰᱮ ᱥᱮᱱᱚᱜ ᱢᱮ",
        "मेरे पास आओ" to "ᱤᱧ ᱴᱷᱮᱱ ᱦᱤᱡᱩᱜ ᱢᱮ",
        "शांत रहो" to "ᱛᱷᱤᱨ ᱠᱚᱜ ᱯᱮ",
        "चुप रहो" to "ᱦᱟᱯᱮ ᱠᱚᱜ ᱯᱮ",
        "इंतजार करो" to "ᱛᱟᱺᱜᱤ ᱢᱮ",
        "जल्दी करो" to "ᱞᱚᱜᱚᱱ ᱢᱮ",

        // Encouragement
        "बहुत अच्छा" to "ᱟᱹᱰᱤ ᱱᱟᱯᱟᱭ",
        "शाबाश" to "ᱥᱟᱵᱟᱥ",
        "अच्छा काम" to "ᱱᱟᱯᱟᱭ ᱠᱟᱹᱢᱤ",
        "बहुत बढ़िया" to "ᱟᱹᱰᱤ ᱢᱚᱡᱽ",
        "सही जवाब" to "ᱥᱟᱹᱦᱤ ᱛᱮᱞᱟ",
        "फिर कोशिश करो" to "ᱟᱨᱦᱚᱸ ᱠᱩᱨᱩᱢᱩᱴᱩ ᱢᱮ",

        // Colors
        "लाल रंग" to "ᱟᱨᱟᱜ ᱨᱚᱝ",
        "हरा रंग" to "ᱦᱟᱹᱨᱤᱭᱟᱹᱲ ᱨᱚᱝ",
        "नीला रंग" to "ᱞᱤᱞ ᱨᱚᱝ",
        "पीला रंग" to "ᱥᱟᱥᱟᱝ ᱨᱚᱝ",
        "काला रंग" to "ᱦᱮᱸᱫᱮ ᱨᱚᱝ",
        "सफेद रंग" to "ᱯᱩᱸᱰ ᱨᱚᱝ",

        // Numbers
        "एक" to "ᱢᱤᱫ",
        "दो" to "ᱵᱟᱨ",
        "तीन" to "ᱯᱮ",
        "चार" to "ᱯᱩᱱ",
        "पाँच" to "ᱢᱚᱬᱮ",
        "दस" to "ᱜᱮᱞ",

        // Existing MVP fields
        "मेरा नाम पलश है" to "ᱤᱧᱟᱜ ᱧᱩᱛᱩᱢ ᱫᱚ ᱯᱟᱞᱟᱥ",
        "धन्यवाद" to "ᱥᱟᱨᱦᱟᱣ",
        "रंगों के नाम" to "ᱨᱚᱝ ᱠᱚᱣᱟᱜ ᱧᱩᱛᱩᱢ",
        "आज हम पढ़ेंगे" to "ᱛᱮᱦᱮᱧ ᱵᱚᱱ ᱯᱟᱲᱦᱟᱣᱟ"
    )

    /**
     * Translates Hindi text to Santali (Ol Chiki) using the curated dictionary.
     * Returns a fallback message if no match is found.
     */
    fun translate(hindiText: String): String {
        val cleanInput = hindiText.trim().lowercase()
            .replace("?", "")
            .replace(".", "")
            .replace("।", "")
            .replace("!", "")

        if (cleanInput.isEmpty()) return ""

        // Try exact match first
        dictionary[cleanInput]?.let { return it }

        // Try fuzzy lookup (if input contains the key or vice versa)
        for ((key, value) in dictionary) {
            val cleanKey = key.lowercase()
            if (cleanInput.contains(cleanKey) || cleanKey.contains(cleanInput)) {
                return value
            }
        }

        return "Translation not available in curated MVP vocabulary."
    }
}
