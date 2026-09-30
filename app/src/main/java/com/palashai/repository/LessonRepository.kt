package com.palashai.repository

import com.palashai.data.Lesson
import com.palashai.data.LessonDao
import com.palashai.data.LessonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonRepository(private val lessonDao: LessonDao) {
    
    val allLessons: Flow<List<Lesson>> = lessonDao.getAllLessons().map { entities ->
        entities.map { it.toDomainModel() }
    }

    fun getLessonsByClass(classLevel: Int): Flow<List<Lesson>> {
        return lessonDao.getLessonsByClass(classLevel).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getLessonById(id: String): Lesson? {
        return lessonDao.getLessonById(id)?.toDomainModel()
    }

    private fun LessonEntity.toDomainModel(): Lesson {
        return Lesson(
            id = id,
            classLevel = classLevel,
            subject = subject,
            topic = topic,
            explanation = explanation,
            hindiText = hindiText,
            santaliText = santaliText,
            objective = objective,
            activity = activity,
            assessment = assessment
        )
    }
}
