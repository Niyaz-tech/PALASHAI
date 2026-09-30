                                                package com.palashai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.palashai.data.Worksheet
import com.palashai.navigation.Screen
import com.palashai.repository.WorksheetRepository
import com.palashai.ui.components.PalashAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetScreen(navController: NavHostController) {
    val worksheets = remember { WorksheetRepository.getAllWorksheets() }
    var selectedGrade by remember { mutableStateOf(0) } // 0 means all

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Educational Worksheets",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Grade Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedGrade == 0,
                    onClick = { selectedGrade = 0 },
                    label = { Text("All") }
                )
                listOf(1, 2, 3, 4).forEach { grade ->
                    FilterChip(
                        selected = selectedGrade == grade,
                        onClick = { selectedGrade = grade },
                        label = { Text("Grade $grade") }
                    )
                }
            }

            val filteredWorksheets = if (selectedGrade == 0) worksheets 
                                   else worksheets.filter { it.grade == selectedGrade }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (selectedGrade == 0) "All Worksheets" else "Grade $selectedGrade Worksheets",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Interactive offline activities for bilingual learning",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                items(filteredWorksheets) { worksheet ->
                    WorksheetCard(worksheet) {
                        navController.navigate(Screen.WorksheetDetail.createRoute(worksheet.id))
                    }
                }
                
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun WorksheetCard(worksheet: Worksheet, onClick: () -> Unit) {
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
                    Text(text = worksheet.emoji, style = MaterialTheme.typography.headlineSmall)
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
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Grade ${worksheet.grade} • ${worksheet.subject}",
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
