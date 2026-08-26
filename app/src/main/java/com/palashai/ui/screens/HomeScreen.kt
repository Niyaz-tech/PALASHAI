package com.palashai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.palashai.navigation.Screen
import com.palashai.ui.components.FeatureCard
import com.palashai.ui.components.PalashAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            PalashAppBar(
                title = "PalashAI",
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome, Teacher",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Empowering mother-tongue based primary education in Jharkhand.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                FeatureCard(
                    title = "Curriculum",
                    description = "Explore lesson plans and teaching materials.",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    onClick = { navController.navigate(Screen.Curriculum.route) }
                )
            }

            item {
                FeatureCard(
                    title = "Voice Translation",
                    description = "Hindi to Santali real-time translation.",
                    icon = Icons.Default.Translate,
                    onClick = { navController.navigate(Screen.VoiceTranslation.route) }
                )
            }

            item {
                FeatureCard(
                    title = "Worksheets",
                    description = "Generate and manage student worksheets.",
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    onClick = { navController.navigate(Screen.Worksheets.route) }
                )
            }

            item {
                FeatureCard(
                    title = "Flashcards",
                    description = "Interactive learning tools for vernacular vocabulary.",
                    icon = Icons.Default.Style,
                    onClick = { navController.navigate(Screen.Flashcards.route) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
