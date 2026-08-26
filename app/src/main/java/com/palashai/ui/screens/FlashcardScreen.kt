package com.palashai.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.palashai.PalashApplication
import com.palashai.ui.components.PalashAppBar
import com.palashai.viewmodel.FlashcardViewModel
import com.palashai.viewmodel.FlashcardViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    navController: NavHostController,
    viewModel: FlashcardViewModel = viewModel(
        factory = FlashcardViewModelFactory(LocalContext.current.applicationContext as PalashApplication)
    )
) {
    val selectedClass by viewModel.selectedClass.collectAsState()
    val availableTopics by viewModel.availableTopics.collectAsState()
    val selectedTopic by viewModel.selectedTopic.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val isNativeSantaliSupported by viewModel.isNativeSantaliSupported.collectAsState()
    
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        label = "rotation"
    )

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Flashcards",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Class Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(1, 2, 3, 4).forEach { level ->
                    FilterChip(
                        selected = selectedClass == level,
                        onClick = { viewModel.selectClass(level) },
                        label = { Text("Class $level") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Topic Selector
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableTopics) { topic ->
                    SuggestionChip(
                        onClick = { viewModel.selectTopic(topic) },
                        label = { Text(topic.title) },
                        colors = if (selectedTopic?.id == topic.id) 
                            SuggestionChipDefaults.suggestionChipColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        else SuggestionChipDefaults.suggestionChipColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedTopic != null && selectedTopic!!.cards.isNotEmpty()) {
                val currentCard = selectedTopic!!.cards[currentIndex]
                
                Text(
                    text = "${selectedTopic!!.title} - Card ${currentIndex + 1} of ${selectedTopic!!.cards.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Flashcard
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 8 * density
                        }
                        .clickable { viewModel.toggleFlip() },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (rotation <= 90f) {
                            // Front - Question (English + Hindi)
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = currentCard.question,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentCard.hindiQuestion,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                IconButton(onClick = {
                                    if (isTtsSpeaking) viewModel.stopSpeaking() else viewModel.speakFlashcard(currentCard, true)
                                }) {
                                    Icon(
                                        imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Listen",
                                        tint = if (isTtsSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else {
                            // Back - Answer (English + Hindi + Santali)
                            Column(
                                modifier = Modifier
                                    .graphicsLayer { rotationY = 180f }
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Answer:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = currentCard.answer,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = currentCard.hindiAnswer,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                val santali = viewModel.getSantaliTranslation(currentCard.hindiAnswer)
                                Text(
                                    text = if (isNativeSantaliSupported) "Santali (MVP):" else "Santali (MVP - No voice):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = santali,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = if (santali.contains("not available")) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
                                )

                                if (currentCard.explanation.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = currentCard.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Listen Button on back
                                IconButton(onClick = {
                                    if (isTtsSpeaking) viewModel.stopSpeaking() else viewModel.speakFlashcard(currentCard, false)
                                }) {
                                    Icon(
                                        imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Listen",
                                        tint = if (isTtsSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousCard() },
                        enabled = currentIndex > 0
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous")
                    }

                    Button(onClick = { viewModel.toggleFlip() }) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (isFlipped) "Show Question" else "Reveal Answer")
                    }

                    IconButton(
                        onClick = { viewModel.nextCard() },
                        enabled = currentIndex < selectedTopic!!.cards.size - 1
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next")
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select a topic to start learning")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tap the card to flip",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
