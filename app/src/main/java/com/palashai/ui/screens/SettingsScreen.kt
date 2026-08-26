package com.palashai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.palashai.ui.components.PalashAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    var sourceLanguage by remember { mutableStateOf("Hindi") }
    var targetLanguage by remember { mutableStateOf("Santali") }
    var grade by remember { mutableStateOf("Grade 1") }

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Settings",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Settings
            Text(
                text = "Language Preferences",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            ListItem(
                headlineContent = { Text("Source Language") },
                supportingContent = { Text(sourceLanguage) },
                leadingContent = { Icon(Icons.Default.Language, contentDescription = null) },
                trailingContent = {
                    TextButton(onClick = { /* Change logic */ }) {
                        Text("Change")
                    }
                }
            )

            ListItem(
                headlineContent = { Text("Target Language") },
                supportingContent = { Text(targetLanguage) },
                leadingContent = { Icon(Icons.Default.Language, contentDescription = null) },
                trailingContent = {
                    TextButton(onClick = { /* Change logic */ }) {
                        Text("Change")
                    }
                }
            )

            HorizontalDivider()

            // Education Settings
            Text(
                text = "Classroom Settings",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            ListItem(
                headlineContent = { Text("Selected Grade") },
                supportingContent = { Text(grade) },
                leadingContent = { Icon(Icons.Default.School, contentDescription = null) },
                trailingContent = {
                    TextButton(onClick = { /* Change logic */ }) {
                        Text("Select")
                    }
                }
            )

            HorizontalDivider()

            // App Info
            ListItem(
                headlineContent = { Text("About PalashAI") },
                supportingContent = { Text("Version 1.0.0 (MVP)") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Developed for Smart India Hackathon 2026",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
