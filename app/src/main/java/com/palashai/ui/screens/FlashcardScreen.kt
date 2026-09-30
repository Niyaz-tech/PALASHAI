package com.palashai.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
        animationSpec = tween(durationMillis = 400),
        label = "rotation"
    )

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Bilingual Flashcards",
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
                        label = { Text("Grade $level") }
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
                    text = "${selectedTopic!!.title} • ${currentIndex + 1}/${selectedTopic!!.cards.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Flashcard
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12 * density
                        }
                        .clickable { viewModel.toggleFlip() },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (rotation <= 90f) {
                            // Front - Hindi Concept
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (currentCard.emoji.isNotEmpty()) {
                                    Text(
                                        text = currentCard.emoji,
                                        fontSize = 80.sp,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }
                                
                                Text(
                                    text = "In Hindi:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = currentCard.hindi,
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Button(
                                    onClick = {
                                        if (isTtsSpeaking) viewModel.stopSpeaking() else viewModel.speakFlashcard(currentCard, true)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isTtsSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Listen"
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Listen")
                                }
                            }
                        } else {
                            // Back - Santali / Ol Chiki Translation
                            Column(
                                modifier = Modifier
                                    .graphicsLayer { rotationY = 180f }
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "In Santali (Ol Chiki):",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = currentCard.santali,
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                if (currentCard.english.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "(${currentCard.english})",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(32.dp))
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = {
                                            if (isTtsSpeaking) viewModel.stopSpeaking() else viewModel.speakFlashcard(currentCard, false)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isTtsSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Icon(
                                            imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = "Listen"
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text("Listen")
                                    }
                                    
                                    if (!isNativeSantaliSupported) {
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "(Hindi TTS)",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
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
                    FilledTonalIconButton(
                        onClick = { viewModel.previousCard() },
                        enabled = currentIndex > 0,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous")
                    }

                    Button(
                        onClick = { viewModel.toggleFlip() },
                        modifier = Modifier.height(56.dp).padding(horizontal = 16.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (isFlipped) "Show Hindi" else "Flip to Santali")
                    }

                    FilledTonalIconButton(
                        onClick = { viewModel.nextCard() },
                        enabled = currentIndex < selectedTopic!!.cards.size - 1,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next")
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select a grade and topic to start learning")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tap the card to see the translation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
