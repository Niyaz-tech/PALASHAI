package com.palashai.ai.translation.poc

import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import java.io.File
import java.io.InputStreamReader

/**
 * A memory-efficient Pure-Kotlin BPE Tokenizer using streaming JSON parsing.
 * Optimized to handle large vocabulary and merge files within Android's JVM heap limits.
 * Uses primitive arrays for merges and a custom hash map for the vocabulary to avoid boxing.
 */
class BpeTokenizer(private val jsonFile: File) {
    private val TAG = "BpeTokenizer"
    
    // Custom open-addressing hash map for String -> Int to avoid Entry and Boxed Integer overhead.
    private var vocabKeys = arrayOfNulls<String>(16)
    private var vocabValues = IntArray(16) { -1 }
    private var vocabSize = 0
    private var vocabCapacity = 16

    // Inverse vocab for decoding (primitive array)
    private var invVocab = arrayOfNulls<String>(16)

    // Merges stored in primitive arrays to avoid millions of objects.
    private var mergeA = IntArray(0)
    private var mergeB = IntArray(0)
    private var mergeRes = IntArray(0)
    private var numMerges = 0

    init {
        parseJson(jsonFile)
    }

    private fun parseJson(file: File) {
        Log.d(TAG, "Starting ultra-memory-efficient parsing: ${file.name}")
        val reader = JsonReader(InputStreamReader(file.inputStream(), "UTF-8"))
        try {
            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                when (name) {
                    "added_tokens" -> parseAddedTokens(reader)
                    "model" -> parseModel(reader)
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
            Log.d(TAG, "Parsing complete: ${vocabSize} tokens, $numMerges merges")
        } catch (e: Exception) {
            Log.e(TAG, "Critical failure parsing ${file.name}: ${e.message}")
            throw e
        } finally {
            reader.close()
        }
    }

    private fun parseAddedTokens(reader: JsonReader) {
        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            var content = ""
            var id = -1
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "content" -> content = reader.nextString()
                    "id" -> id = reader.nextInt()
                    else -> reader.skipValue()
                }
            }
            if (content.isNotEmpty() && id != -1) {
                putVocab(content, id)
            }
            reader.endObject()
        }
        reader.endArray()
    }

    private fun parseModel(reader: JsonReader) {
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "vocab" -> {
                    // Pre-allocate for ~200k entries.
                    initVocab(250000)
                    reader.beginObject()
                    while (reader.hasNext()) {
                        putVocab(reader.nextName(), reader.nextInt())
                    }
                    reader.endObject()
                    Log.d(TAG, "Vocab loaded: $vocabSize entries")
                }
                "merges" -> {
                    // Pre-allocate merges for ~500k entries.
                    mergeA = IntArray(600000)
                    mergeB = IntArray(600000)
                    mergeRes = IntArray(600000)
                    reader.beginArray()
                    while (reader.hasNext()) {
                        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                            // Format: [ "tokenA", "tokenB" ]
                            reader.beginArray()
                            val s1 = reader.nextString()
                            val s2 = reader.nextString()
                            reader.endArray()
                            addMerge(s1, s2)
                        } else {
                            // Format: "tokenA tokenB"
                            val mergeLine = reader.nextString()
                            val spaceIdx = mergeLine.indexOf(' ')
                            if (spaceIdx != -1) {
                                val s1 = mergeLine.substring(0, spaceIdx)
                                val s2 = mergeLine.substring(spaceIdx + 1)
                                addMerge(s1, s2)
                            }
                        }
                    }
                    reader.endArray()
                    // Reclaim unused pre-allocated capacity
                    mergeA = mergeA.copyOf(numMerges)
                    mergeB = mergeB.copyOf(numMerges)
                    mergeRes = mergeRes.copyOf(numMerges)
                    Log.d(TAG, "Merges loaded: $numMerges rules")
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        System.gc() // Advice the system to clean up temporary strings from the merge resolution
    }

    private fun addMerge(s1: String, s2: String) {
        val id1 = getVocab(s1)
        val id2 = getVocab(s2)
        val resId = getVocab(s1 + s2)
        
        if (id1 != -1 && id2 != -1 && resId != -1) {
            if (numMerges >= mergeA.size) {
                mergeA = mergeA.copyOf(mergeA.size * 2)
                mergeB = mergeB.copyOf(mergeB.size * 2)
                mergeRes = mergeRes.copyOf(mergeRes.size * 2)
            }
            mergeA[numMerges] = id1
            mergeB[numMerges] = id2
            mergeRes[numMerges] = resId
            numMerges++
        }
    }

    private fun initVocab(capacity: Int) {
        vocabCapacity = capacity
        vocabKeys = arrayOfNulls(capacity)
        vocabValues = IntArray(capacity) { -1 }
        invVocab = arrayOfNulls(capacity)
    }

    private fun putVocab(key: String, value: Int) {
        if (vocabSize >= vocabCapacity * 0.6) growVocab()
        var h = (key.hashCode() and 0x7FFFFFFF) % vocabCapacity
        while (vocabKeys[h] != null) {
            if (vocabKeys[h] == key) {
                vocabValues[h] = value
                return
            }
            h = (h + 1) % vocabCapacity
        }
        vocabKeys[h] = key
        vocabValues[h] = value
        vocabSize++
        if (value >= invVocab.size) invVocab = invVocab.copyOf(maxOf(value + 1, invVocab.size * 2))
        invVocab[value] = key
    }

    private fun getVocab(key: String): Int {
        if (vocabCapacity == 0) return -1
        var h = (key.hashCode() and 0x7FFFFFFF) % vocabCapacity
        while (vocabKeys[h] != null) {
            if (vocabKeys[h] == key) return vocabValues[h]
            h = (h + 1) % vocabCapacity
        }
        return -1
    }

    private fun growVocab() {
        val oldKeys = vocabKeys
        val oldValues = vocabValues
        vocabCapacity *= 2
        vocabKeys = arrayOfNulls(vocabCapacity)
        vocabValues = IntArray(vocabCapacity) { -1 }
        vocabSize = 0
        for (i in oldKeys.indices) {
            oldKeys[i]?.let { putVocab(it, oldValues[i]) }
        }
    }

    /**
     * BPE encoding logic working exclusively on primitive token IDs.
     * Preserves original merge rank ordering.
     */
    fun encode(text: String): LongArray {
        var input = text
        val prefixIds = mutableListOf<Int>()
        
        // Handle tags
        if (input.startsWith("__") && input.contains("__ ")) {
            val tagPart = input.substringBefore(" ")
            var tid = getVocab(tagPart)
            if (tid == -1) {
                tid = getVocab(tagPart.replace("__", ""))
            }
            if (tid != -1) {
                prefixIds.add(tid)
                input = input.substringAfter(" ")
            }
        }
        
        // Initial split
        val prepared = "\u2581" + input.replace(" ", "\u2581")
        val unkId = getVocab("<unk>").let { if (it != -1) it else 3 }
        var current = IntArray(prepared.length)
        for (i in prepared.indices) {
            val id = getVocab(prepared[i].toString())
            current[i] = if (id != -1) id else unkId
        }

        // Apply rules
        for (m in 0 until numMerges) {
            val a = mergeA[m]
            val b = mergeB[m]
            val r = mergeRes[m]
            
            var writeIdx = 0
            var readIdx = 0
            var merged = false
            
            while (readIdx < current.size) {
                if (readIdx < current.size - 1 && current[readIdx] == a && current[readIdx + 1] == b) {
                    current[writeIdx++] = r
                    readIdx += 2
                    merged = true
                } else {
                    current[writeIdx++] = current[readIdx++]
                }
            }
            if (merged) {
                current = current.copyOf(writeIdx)
            }
        }
        
        val final = LongArray(prefixIds.size + current.size)
        for (i in prefixIds.indices) final[i] = prefixIds[i].toLong()
        for (i in current.indices) final[prefixIds.size + i] = current[i].toLong()
        return final
    }

    fun decode(ids: LongArray): String {
        val sb = StringBuilder()
        for (id in ids) {
            val idx = id.toInt()
            if (idx >= 0 && idx < invVocab.size) {
                invVocab[idx]?.let { sb.append(it) }
            }
        }
        return sb.toString().replace("\u2581", " ").trim()
    }
}
