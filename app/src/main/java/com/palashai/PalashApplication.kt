package com.palashai

import android.app.Application
import com.palashai.data.PalashDatabase
import com.palashai.repository.LessonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PalashApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { PalashDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { LessonRepository(database.lessonDao()) }
}
