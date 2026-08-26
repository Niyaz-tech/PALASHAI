package com.palashai.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LessonEntity::class], version = 1, exportSchema = false)
abstract class PalashDatabase : RoomDatabase() {

    abstract fun lessonDao(): LessonDao

    companion object {
        @Volatile
        private var INSTANCE: PalashDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PalashDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PalashDatabase::class.java,
                    "palash_database"
                )
                .addCallback(PalashDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PalashDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("PalashCurriculum", "Database created. Populating...")
            scope.launch(Dispatchers.IO) {
                INSTANCE?.let { database ->
                    populateDatabase(database.lessonDao())
                }
            }
        }

        suspend fun populateDatabase(lessonDao: LessonDao) {
            val lessons = listOf(
                LessonEntity(
                    id = "101", classLevel = 1, subject = "Numeracy", topic = "Counting 1-10",
                    hindiText = "बच्चों को 1 से 10 तक की संख्याएँ गिनना सिखाएँ।",
                    santaliText = "Gidra ko 1 khon 10 dhabik lekha ceda ko pe.",
                    objective = "Students will be able to recognize and count numbers from 1 to 10.",
                    activity = "Ask students to count classroom objects such as pencils, books, or stones from 1 to 10.",
                    assessment = "Ask the student to identify and count a set of objects containing 1 to 10 items."
                ),
                LessonEntity(
                    id = "102", classLevel = 1, subject = "Language", topic = "Self Introduction",
                    hindiText = "अपना नाम और परिचय देना सीखें।",
                    santaliText = "Apanak nutum ar uprum em ceda pe.",
                    objective = "Students will be able to introduce themselves in Hindi.",
                    activity = "Stand in a circle and have each student say 'Mera naam [Name] hai'.",
                    assessment = "Observe if the student can clearly state their name when asked."
                ),
                LessonEntity(
                    id = "201", classLevel = 2, subject = "Science", topic = "Parts of a Plant",
                    hindiText = "पौधों के विभिन्न भागों की पहचान करें।",
                    santaliText = "Dare reak jutajuda hatin ko uprum pe.",
                    objective = "Students will identify roots, stem, leaves, and flowers.",
                    activity = "Take students to the school garden and point out different parts of a small plant.",
                    assessment = "Draw a plant on the board and ask students to name the parts pointed to."
                ),
                LessonEntity(
                    id = "301", classLevel = 3, subject = "Math", topic = "Basic Addition",
                    hindiText = "दो अंकों की संख्याओं को जोड़ना सीखें।",
                    santaliText = "Bar gotan ank reak lekha mesawa ceda pe.",
                    objective = "Students will perform addition of two-digit numbers without carrying.",
                    activity = "Use pebbles to demonstrate adding groups of 10s and 1s.",
                    assessment = "Provide 5 simple addition problems for students to solve in their notebooks."
                ),
                LessonEntity(
                    id = "401", classLevel = 4, subject = "Social Studies", topic = "Our Environment",
                    hindiText = "अपने पर्यावरण को स्वच्छ रखना क्यों जरूरी है?",
                    santaliText = "Apanak pariwesh sapha doho chedak jarura?",
                    objective = "Understand the importance of cleanliness and waste management.",
                    activity = "Group activity to identify 'biodegradable' and 'non-biodegradable' waste around the school.",
                    assessment = "List three ways to keep the classroom clean."
                )
            )
            lessonDao.insertAll(lessons)
        }
    }
}
