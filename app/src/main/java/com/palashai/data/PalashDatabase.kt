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

@Database(entities = [LessonEntity::class], version = 2, exportSchema = false)
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
                .fallbackToDestructiveMigration()
                .addCallback(PalashDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PalashDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("PalashCurriculum", "Database created. Populating...")
            scope.launch(Dispatchers.IO) {
                INSTANCE?.let { database ->
                    populateDatabase(database.lessonDao())
                }
            }
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Log.d("PalashCurriculum", "Database destructive migration. Re-populating...")
            scope.launch(Dispatchers.IO) {
                INSTANCE?.let { database ->
                    populateDatabase(database.lessonDao())
                }
            }
        }

        suspend fun populateDatabase(lessonDao: LessonDao) {
            val lessons = listOf(
                // Grade 1
                LessonEntity(
                    id = "101", classLevel = 1, subject = "Math", topic = "Numbers & Counting",
                    explanation = "Let's learn to count our fingers and toys.",
                    hindiText = "1 से 10 तक गिनना सीखें।",
                    santaliText = "ᱢᱤᱫ ᱠᱷᱚᱱ ᱜᱮᱞ ᱫᱷᱟᱹᱵᱤᱡ ᱞᱮᱠᱷᱟᱭ ᱯᱮ",
                    objective = "Count 1-10 accurately.",
                    activity = "Count stones in the garden.",
                    assessment = "How many fingers do you have?"
                ),
                LessonEntity(
                    id = "102", classLevel = 1, subject = "Arts", topic = "Colours & Shapes",
                    explanation = "The world is full of beautiful colors.",
                    hindiText = "लाल, नीला और हरा रंग पहचानें।",
                    santaliText = "ᱟᱨᱟᱜ, ᱞᱤᱞ ᱟᱨ ᱦᱟᱹᱨᱤᱭᱟᱹᱲ ᱨᱚᱝ ᱩᱨᱩᱢ ᱯᱮ",
                    objective = "Identify basic colours.",
                    activity = "Point to red objects in the room.",
                    assessment = "What color is the sky?"
                ),
                LessonEntity(
                    id = "103", classLevel = 1, subject = "Social", topic = "Family & Relationships",
                    explanation = "Our family members love and care for us.",
                    hindiText = "माँ, पिता, भाई और बहन के बारे में बताएं।",
                    santaliText = "ᱟᱭᱳ, ᱵᱟᱵᱟ, ᱵᱚᱭᱦᱟ ᱟᱨ ᱢᱤᱥᱨᱟ ᱵᱟᱵᱚᱛ ᱞᱟᱹᱭ ᱯᱮ",
                    objective = "Name family members.",
                    activity = "Draw a picture of your family.",
                    assessment = "Who is your father's brother?"
                ),
                LessonEntity(
                    id = "104", classLevel = 1, subject = "EVS", topic = "Animals",
                    explanation = "Animals live all around us in nature.",
                    hindiText = "गाय, बकरी और कुत्ता पहचानें।",
                    santaliText = "ᱜᱟᱹᱭ, ᱢᱮᱨᱚᱢ ᱟᱨ ᱥᱮᱛᱟ ᱩᱨᱩᱢ ᱯᱮ",
                    objective = "Name common domestic animals.",
                    activity = "Make sounds like different animals.",
                    assessment = "Which animal gives us milk?"
                ),
                LessonEntity(
                    id = "105", classLevel = 1, subject = "Science", topic = "Body Parts",
                    explanation = "Our body parts help us see, hear, and play.",
                    hindiText = "आँख, कान, नाक और हाथों के नाम।",
                    santaliText = "ᱢᱮᱫ, ᱞᱩᱛᱩᱨ, ᱢᱩ ᱟᱨ ᱛᱤ ᱠᱚᱣᱟᱜ ᱧᱩᱛᱩᱢ",
                    objective = "Name major body parts.",
                    activity = "Simon Says 'Touch your nose'.",
                    assessment = "What do we use to see?"
                ),

                // Grade 2
                LessonEntity(
                    id = "201", classLevel = 2, subject = "Math", topic = "Addition & Subtraction",
                    explanation = "Adding is joining, subtracting is taking away.",
                    hindiText = "20 तक की संख्याओं को जोड़ना और घटाना।",
                    santaliText = "ᱢᱮᱥᱟ ᱟᱨ ᱵᱷᱮᱜᱟᱨ ᱞᱮᱠᱷᱟ",
                    objective = "Add and subtract within 20.",
                    activity = "Use sticks to solve 12 + 4.",
                    assessment = "What is 10 plus 5?"
                ),
                LessonEntity(
                    id = "202", classLevel = 2, subject = "Math", topic = "Numbers & Time",
                    explanation = "A clock tells us the time of the day.",
                    hindiText = "घड़ी में समय देखना सीखें।",
                    santaliText = "ᱜᱷᱟᱹᱲᱤ ᱧᱮᱞ ᱪᱮᱫ ᱯᱮ",
                    objective = "Read clock hours.",
                    activity = "Set the dummy clock to 3 o'clock.",
                    assessment = "When is your lunchtime?"
                ),
                LessonEntity(
                    id = "203", classLevel = 2, subject = "EVS", topic = "Plants",
                    explanation = "Plants grow from seeds and need water.",
                    hindiText = "जड़, तना और पत्तों की पहचान।",
                    santaliText = "ᱫᱟᱨᱮ ᱨᱮᱱᱟᱜ ᱦᱟᱹᱴᱤᱧ ᱠᱚ",
                    objective = "Identify roots, stem, and leaf.",
                    activity = "Label parts on a real plant.",
                    assessment = "Which part of the plant is under the soil?"
                ),
                LessonEntity(
                    id = "204", classLevel = 2, subject = "EVS", topic = "Animals & Habitats",
                    explanation = "Forest animals live in the wild.",
                    hindiText = "जंगल और घर में रहने वाले जानवर।",
                    santaliText = "ᱡᱟᱱᱣᱟᱨ ᱠᱚᱣᱟᱜ ᱚᱲᱟᱜ",
                    objective = "Distinguish between wild and domestic habitats.",
                    activity = "Sort animal cards by where they live.",
                    assessment = "Where does a lion live?"
                ),
                LessonEntity(
                    id = "205", classLevel = 2, subject = "Science", topic = "Food & Nutrition",
                    explanation = "Eat fruits and vegetables to stay healthy.",
                    hindiText = "स्वस्थ और अस्वास्थ्यकर भोजन।",
                    santaliText = "ᱱᱟᱯᱟᱭ ᱡᱚᱢᱟᱜ ᱟᱨ ᱵᱟᱹᱲᱤᱡ ᱡᱚᱢᱟᱜ",
                    objective = "Identify healthy food items.",
                    activity = "Circle the healthy foods in a list.",
                    assessment = "Is an apple healthy or junk food?"
                ),

                // Grade 3
                LessonEntity(
                    id = "301", classLevel = 3, subject = "Math", topic = "Multiplication & Division",
                    explanation = "Multiplication is repeated addition.",
                    hindiText = "2 से 5 तक का पहाड़ा और भाग।",
                    santaliText = "ᱜᱟᱱᱟᱣ ᱟᱨ ᱦᱟᱹᱴᱤᱧ",
                    objective = "Understand tables up to 5.",
                    activity = "Share 12 sweets equally among 3 children.",
                    assessment = "What is 3 times 4?"
                ),
                LessonEntity(
                    id = "302", classLevel = 3, subject = "Math", topic = "Fractions",
                    explanation = "Fractions are parts of a whole thing.",
                    hindiText = "आधा और एक-चौथाई की अवधारणा।",
                    santaliText = "ᱟᱫᱷᱟ ᱟᱨ ᱯᱩᱱ ᱦᱟᱹᱴᱤᱧ",
                    objective = "Identify half and quarter.",
                    activity = "Fold a paper circle into 4 parts.",
                    assessment = "Color half of the rectangle."
                ),
                LessonEntity(
                    id = "303", classLevel = 3, subject = "Science", topic = "Human Body",
                    explanation = "Bones give our body shape and strength.",
                    hindiText = "हड्डियों और मांसपेशियों का महत्व।",
                    santaliText = "ᱡᱟᱝ ᱟᱨ ᱡᱤᱞ ᱨᱮᱱᱟᱜ ᱠᱟᱹᱢᱤ",
                    objective = "Know about skeleton and muscles.",
                    activity = "Feel the bones in your arm.",
                    assessment = "How many main bones are in your hand?"
                ),
                LessonEntity(
                    id = "304", classLevel = 3, subject = "EVS", topic = "Environment & Water",
                    explanation = "Every drop of water is very important.",
                    hindiText = "पानी बचाना और जल संरक्षण।",
                    santaliText = "ᱫᱟᱜ ᱵᱟᱧᱪᱟᱣ ᱪᱮᱫ ᱯᱮ",
                    objective = "Learn ways to save water.",
                    activity = "Make a 'Save Water' poster for the school.",
                    assessment = "How can we reuse rainwater at home?"
                ),
                LessonEntity(
                    id = "305", classLevel = 3, subject = "Language", topic = "Basic Grammar",
                    explanation = "Nouns name things, verbs show actions.",
                    hindiText = "संज्ञा और क्रिया की पहचान।",
                    santaliText = "ᱧᱩᱛᱩᱢ ᱟᱨ ᱠᱟᱹᱢᱤ ᱥᱟᱵᱟᱫᱽ",
                    objective = "Identify nouns and verbs.",
                    activity = "List 5 action words you do in school.",
                    assessment = "Circle the verb in: 'The boy runs'."
                ),

                // Grade 4
                LessonEntity(
                    id = "401", classLevel = 4, subject = "Math", topic = "Large Numbers & Geometry",
                    explanation = "Numbers go beyond thousands in the big world.",
                    hindiText = "बड़ी संख्याएं और विभिन्न आकार।",
                    santaliText = "ᱢᱟᱨᱟᱝ ᱞᱮᱠᱷᱟ ᱟᱨ ᱨᱩᱯ ᱠᱚ",
                    objective = "Understand numbers up to 10000.",
                    activity = "Build a pyramid using straws and tape.",
                    assessment = "Which shape has four equal sides?"
                ),
                LessonEntity(
                    id = "402", classLevel = 4, subject = "Math", topic = "Fractions & Measurement",
                    explanation = "We measure weight in kilograms and grams.",
                    hindiText = "वजन और माप की इकाइयाँ।",
                    santaliText = "ᱡᱚᱠᱷᱟ ᱟᱨ ᱦᱟᱢᱟᱞ",
                    objective = "Understand Kgs and Litres.",
                    activity = "Weigh different books using a balance.",
                    assessment = "How many grams are there in 1 kilogram?"
                ),
                LessonEntity(
                    id = "403", classLevel = 4, subject = "Language", topic = "Grammar & Parts of Speech",
                    explanation = "Adjectives describe the quality of things.",
                    hindiText = "सर्वनाम और विशेषण का प्रयोग।",
                    santaliText = "ᱜᱩᱱ ᱥᱟᱵᱟᱫᱽ ᱨᱮᱱᱟᱜ ᱵᱮᱵᱷᱟᱨ",
                    objective = "Use pronouns and adjectives correctly.",
                    activity = "Describe your best friend using 3 adjectives.",
                    assessment = "Identify the pronoun in: 'They are playing'."
                ),
                LessonEntity(
                    id = "404", classLevel = 4, subject = "Science", topic = "Plants & Water Cycle",
                    explanation = "Rain comes from clouds and goes to rivers.",
                    hindiText = "प्रकाश संश्लेषण और वर्षा चक्र।",
                    santaliText = "ᱫᱟᱜ ᱪᱟᱠᱷᱟ ᱨᱮᱱᱟᱜ ᱠᱟᱹᱢᱤ",
                    objective = "Understand photosynthesis and rain.",
                    activity = "Observe water droplets on a jar lid.",
                    assessment = "What 3 things do plants need to make food?"
                ),
                LessonEntity(
                    id = "405", classLevel = 4, subject = "EVS", topic = "Food, Health & Environment",
                    explanation = "Pollution makes our earth sick.",
                    hindiText = "प्रदूषण के प्रकार और रोकथाम।",
                    santaliText = "ᱯᱚᱨᱤᱣᱮᱥ ᱥᱟᱯᱷᱟ ᱫᱚᱦᱚ",
                    objective = "Identify types of pollution.",
                    activity = "Clean up the plastic waste in the school.",
                    assessment = "List two ways to reduce air pollution."
                )
            )
            lessonDao.insertAll(lessons)
        }
    }
}
