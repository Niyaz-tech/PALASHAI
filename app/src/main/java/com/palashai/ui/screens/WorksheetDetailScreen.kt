package com.palashai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.palashai.ui.components.PalashAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetDetailScreen(navController: NavHostController, worksheetId: String) {
    // In a real app, we would fetch data based on worksheetId.
    // For the MVP, we'll show consistent placeholder content based on the request.
    val worksheetTitle = when(worksheetId) {
        "1" -> "Alphabet Matching"
        "2" -> "Counting Fruits"
        "3" -> "Animals and Sounds"
        "4" -> "Family Tree"
        else -> "Worksheet"
    }

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Worksheet View",
                onBackClick = { navController.popBackStack() },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = worksheetTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Primary Education • Jharkhand Curriculum",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Worksheet Instructions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Hindi Instruction:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "दी गई गतिविधियों को ध्यान से पढ़ें और अपनी उत्तर पुस्तिका में हल करें।",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Santali Guide:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "ᱮᱢ ᱟᱠᱟᱱ ᱠᱟᱹᱢᱤ ᱠᱚ ᱫᱷᱮᱭᱟᱱ ᱛᱮ ᱯᱟᱲᱦᱟᱣ ᱢᱮ ᱟᱨ ᱟᱢᱟᱜ ᱠᱷᱟᱛᱟ ᱨᱮ ᱚᱞ ᱢᱮ᱾",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Content",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Placeholder for the actual worksheet task content
            Text(
                text = "This worksheet includes 10 interactive items designed for classroom participation. Teachers can use the Voice Translation feature to explain specific terms in Santali to ensure student comprehension.",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { /* In future version: Download/Export PDF */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Print / Export Worksheet")
            }
        }
    }
}
