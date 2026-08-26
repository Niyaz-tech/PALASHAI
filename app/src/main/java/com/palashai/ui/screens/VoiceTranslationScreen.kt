package com.palashai.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.palashai.ui.components.PalashAppBar
import com.palashai.viewmodel.VoiceTranslationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceTranslationScreen(
    navController: NavHostController,
    viewModel: VoiceTranslationViewModel = viewModel()
) {
    var sourceText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    val recordingDuration by viewModel.recordingDuration.collectAsState()
    val recognizedText by viewModel.recognizedText.collectAsState()
    val translatedTextResult by viewModel.translatedText.collectAsState()
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val isNativeSantaliSupported by viewModel.isNativeSantaliSupported.collectAsState()
    
    val context = LocalContext.current

    // Update source text when ASR finishes
    LaunchedEffect(recognizedText) {
        if (recognizedText.isNotEmpty()) {
            sourceText = recognizedText
        }
    }

    // Update translated text when engine produces result
    LaunchedEffect(translatedTextResult) {
        if (translatedTextResult.isNotEmpty()) {
            translatedText = translatedTextResult
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startRecording()
        }
    }

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Voice Translation",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language Selector Row
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hindi", fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                    Text("Santali", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Status indicator
            RecordingStatus(uiState, recordingDuration)

            Spacer(modifier = Modifier.height(16.dp))

            // Recognized Text Area
            OutlinedTextField(
                value = sourceText,
                onValueChange = { sourceText = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                label = { Text("Recognized Speech (Hindi)") },
                placeholder = { Text("Start recording to capture speech...") },
                trailingIcon = {
                    if (sourceText.isNotEmpty()) {
                        IconButton(onClick = { 
                            sourceText = ""
                            translatedText = ""
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Translation Text Area (Placeholder)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Translation (Santali - Curated MVP Engine)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = translatedText.ifEmpty { "Curated Santali translation will appear here..." },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (translatedText.isEmpty()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    if (translatedText.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { 
                                    if (isTtsSpeaking) viewModel.stopSpeaking() else viewModel.speakCurrentTranslation()
                                },
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
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Text(
                                text = if (isNativeSantaliSupported) "Santali Audio" else "Teacher's Guide (Hindi)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Microphone/Stop Button
            val isRecording = uiState is VoiceTranslationViewModel.RecordingState.Recording
            val isBusy = uiState is VoiceTranslationViewModel.RecordingState.Initializing || 
                         uiState is VoiceTranslationViewModel.RecordingState.Processing

            LargeFloatingActionButton(
                onClick = {
                    if (isBusy) return@LargeFloatingActionButton
                    
                    if (isRecording) {
                        viewModel.stopRecording()
                    } else {
                        when (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                            PackageManager.PERMISSION_GRANTED -> viewModel.startRecording()
                            else -> permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                },
                modifier = Modifier.size(80.dp),
                containerColor = when {
                    isBusy -> MaterialTheme.colorScheme.outline
                    isRecording -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                }
            ) {
                if (isBusy) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.surface)
                } else {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when {
                    uiState is VoiceTranslationViewModel.RecordingState.Initializing -> "Initializing AI..."
                    uiState is VoiceTranslationViewModel.RecordingState.Processing -> "Processing Speech..."
                    isRecording -> "Recording..."
                    else -> "Tap to Record"
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RecordingStatus(state: VoiceTranslationViewModel.RecordingState, duration: Long) {
    AnimatedVisibility(
        visible = state !is VoiceTranslationViewModel.RecordingState.Idle,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (state) {
                    is VoiceTranslationViewModel.RecordingState.Error -> MaterialTheme.colorScheme.errorContainer
                    is VoiceTranslationViewModel.RecordingState.Success -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = when (state) {
                        is VoiceTranslationViewModel.RecordingState.Initializing -> "🔄 Preparing offline speech engine..."
                        is VoiceTranslationViewModel.RecordingState.Recording -> "🎙️ Recording... ${duration}s"
                        is VoiceTranslationViewModel.RecordingState.Processing -> "🧠 Transcribing Hindi speech..."
                        is VoiceTranslationViewModel.RecordingState.Success -> "✅ Transcription successful."
                        is VoiceTranslationViewModel.RecordingState.Error -> "❌ Error: ${state.message}"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
