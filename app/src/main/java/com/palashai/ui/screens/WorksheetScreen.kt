package com.palashai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.palashai.navigation.Screen
import com.palashai.ui.components.PalashAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetScreen(navController: NavHostController) {
    val mockWorksheets = listOf(
        WorksheetItem("1", "Grade 1", "Alphabet Matching", "Hindi/Santali"),
        WorksheetItem("2", "Grade 1", "Counting Fruits", "Math"),
        WorksheetItem("3", "Grade 2", "Animals and Sounds", "Science"),
        WorksheetItem("4", "Grade 3", "Family Tree", "Social Studies"),
    )

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Worksheets",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Available Worksheets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Downloadable offline resources for classroom activities",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(mockWorksheets) { worksheet ->
                WorksheetCard(worksheet) {
                    navController.navigate(Screen.WorksheetDetail.createRoute(worksheet.id))
                }
            }
        }
    }
}

@Composable
fun WorksheetCard(worksheet: WorksheetItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(min = 64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = worksheet.title, 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    lineHeight = MaterialTheme.typography.titleMedium.lineHeight
                )
                Text(
                    text = "${worksheet.grade} • ${worksheet.subject}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                Icons.AutoMirrored.Filled.OpenInNew, 
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

data class WorksheetItem(val id: String, val grade: String, val title: String, val subject: String)
