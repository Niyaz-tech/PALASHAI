package com.palashai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.palashai.PalashApplication
import com.palashai.data.Lesson
import com.palashai.navigation.Screen
import com.palashai.ui.components.PalashAppBar
import com.palashai.viewmodel.LessonViewModel
import com.palashai.viewmodel.LessonViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumScreen(
    navController: NavHostController,
    viewModel: LessonViewModel = viewModel(
        factory = LessonViewModelFactory(
            LocalContext.current.applicationContext as PalashApplication,
            (LocalContext.current.applicationContext as PalashApplication).repository
        )
    )
) {
    var selectedGrade by remember { mutableStateOf(1) }
    val grades = listOf(1, 2, 3, 4)
    
    val lessons by viewModel.lessons.collectAsState()

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Curriculum",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Grade Selector
            PrimaryScrollableTabRow(
                selectedTabIndex = grades.indexOf(selectedGrade),
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                grades.forEach { grade ->
                    Tab(
                        selected = selectedGrade == grade,
                        onClick = { 
                            selectedGrade = grade
                            viewModel.loadLessonsByClass(grade)
                        },
                        text = { Text("Grade $grade") }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lessons for Grade $selectedGrade",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { /* Filter logic */ }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                    }
                }

                if (lessons.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No lessons found for Grade $selectedGrade",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    items(lessons) { lesson ->
                        LessonCard(lesson) {
                            navController.navigate(Screen.Lesson.createRoute(lesson.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: Lesson, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.topic,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${lesson.subject} • Class ${lesson.classLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}
