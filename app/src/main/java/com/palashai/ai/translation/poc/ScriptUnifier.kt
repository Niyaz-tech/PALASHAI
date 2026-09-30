package com.palashai.ai.translation.poc

import java.text.Normalizer

/**
 * Utility to handle script unification for IndicTrans2.
 * This class handles mapping Ol Chiki characters to/from the unified space.
 */
object ScriptUnifier {

    /**
     * Normalizes text using Unicode NFC.
     */
    fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFC)
            .replace("\u200D", "") // Remove ZWJ
            .replace("\u200C", "") // Remove ZWNJ
    }

    /**
     * Maps Ol Chiki characters to the model's internal unified script range (Unified Devanagari).
     * This is required for sat_Olck input/output.
     */
    fun olChikiToDevanagari(text: String): String {
        // Simplified mapping based on Indic NLP Library / AI4Bharat standards
        // Note: In a production app, the full 48-character phonetic mapping is required.
        // For the POC, we focus on the core phonetic equivalents.
        val mapping = mapOf(
            '\u1C5A' to '\u0905', // A
            '\u1C5B' to '\u0915', // KA
            '\u1C5C' to '\u0917', // GA
            '\u1C5E' to '\u0924', // TA
            '\u1C5F' to '\u092A', // PA
            '\u1C60' to '\u0921', // DA
            '\u1C61' to '\u091A', // CA
            '\u1C64' to '\u0932', // LA
            '\u1C65' to '\u092E', // MA
            '\u1C69' to '\u0928', // NA
            '\u1C6A' to '\u0930'  // RA
        )
        
        val result = StringBuilder()
        for (char in text) {
            result.append(mapping[char] ?: char)
        }
        return result.toString()
    }

    /**
     * Maps unified Devanagari output back to Ol Chiki characters.
     */
    fun devanagariToOlChiki(text: String): String {
        val mapping = mapOf(
            '\u0905' to '\u1C5A', 
            '\u0915' to '\u1C5B',
            '\u0917' to '\u1C5C',
            '\u0924' to '\u1C5E',
            '\u092A' to '\u1C5F',
            '\u0921' to '\u1C60',
            '\u091A' to '\u1C61',
            '\u0932' to '\u1C64',
            '\u092E' to '\u1C65',
            '\u0928' to '\u1C69',
            '\u0930' to '\u1C6A'
        )
        
        val result = StringBuilder()
        for (char in text) {
            result.append(mapping[char] ?: char)
        }
        return result.toString()
    }
}
