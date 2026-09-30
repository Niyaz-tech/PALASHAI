package com.palashai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
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
import com.palashai.ui.components.PalashAppBar
import com.palashai.viewmodel.LessonViewModel
import com.palashai.viewmodel.LessonViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    navController: NavHostController,
    lessonId: String,
    viewModel: LessonViewModel = viewModel(
        factory = LessonViewModelFactory(
            LocalContext.current.applicationContext as PalashApplication,
            (LocalContext.current.applicationContext as PalashApplication).repository
        )
    )
) {
    val selectedLesson by viewModel.selectedLesson.collectAsState()
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(lessonId) {
        android.util.Log.d("PalashCurriculum", "LessonScreen LaunchedEffect with ID: $lessonId")
        viewModel.getLessonById(lessonId)
    }

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Lesson Details",
                onBackClick = { navController.popBackStack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { 
                            selectedLesson?.let { lesson ->
                                if (isTtsSpeaking) {
                                    viewModel.stopSpeaking()
                                } else {
                                    viewModel.speakLesson(lesson.hindiText)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTtsSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp, 
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isTtsSpeaking) "Stop" else "Listen")
                    }
                    OutlinedButton(
                        onClick = { 
                            scope.launch {
                                snackbarHostState.showSnackbar("Santali Translation is available offline in the Voice Translation feature.")
                            }
                        },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Translate")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (selectedLesson == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val lesson = selectedLesson!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = lesson.topic,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${lesson.subject} • Class ${lesson.classLevel}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (isTtsSpeaking) {
                    Text(
                        text = "🔊 Speaking Teacher's Guide (Hindi)...",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                LessonSection(title = "Objective", content = lesson.objective)

                Spacer(modifier = Modifier.height(16.dp))

                LessonSection(title = "Learn", content = lesson.explanation)
                
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hindi Instruction",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = lesson.hindiText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Santali Translation (MVP)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = lesson.santaliText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                LessonSection(title = "Activity", content = lesson.activity)
                
                Spacer(modifier = Modifier.height(16.dp))
                LessonSection(title = "Assessment", content = lesson.assessment)
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LessonSection(title: String, content: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
