package com.palashai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.palashai.data.Worksheet
import com.palashai.data.WorksheetTask
import com.palashai.repository.WorksheetRepository
import com.palashai.ui.components.PalashAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetDetailScreen(navController: NavHostController, worksheetId: String) {
    val worksheet = remember(worksheetId) { WorksheetRepository.getWorksheetById(worksheetId) }
    var isSaved by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            PalashAppBar(
                title = "Worksheet Activity",
                onBackClick = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (worksheet == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Worksheet not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = worksheet.emoji, style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = worksheet.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Grade ${worksheet.grade} • ${worksheet.subject}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                InstructionCard(worksheet)

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Activity Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                worksheet.tasks.forEachIndexed { index, task ->
                    TaskItem(index + 1, task)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { 
                        scope.launch {
                            isSaved = true
                            snackbarHostState.showSnackbar("Worksheet saved to Offline Library")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        if (isSaved) Icons.Default.DownloadDone else Icons.Default.SaveAlt, 
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (isSaved) "Saved to Device" else "Save for Offline Use")
                }
                
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun InstructionCard(worksheet: Worksheet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Bilingual Instructions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Hindi:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = worksheet.hindiInstruction,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Santali (Ol Chiki):",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = worksheet.santaliInstruction,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TaskItem(number: Int, task: WorksheetTask) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val isCorrect = selectedOption == task.correctAnswer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (isCorrect) CardDefaults.outlinedCardBorder().copy(brush = SolidColor(MaterialTheme.colorScheme.tertiary), width = 2.dp) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Task $number",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(4.dp))
            Text(text = task.hindiQuestion, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = task.santaliQuestion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            
            Spacer(Modifier.height(12.dp))
            
            Column(Modifier.selectableGroup()) {
                task.options.forEach { option ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = (option == selectedOption),
                                onClick = { selectedOption = option },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = null // null because the row is selectable
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            
            if (isCorrect) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(Modifier.width(8.dp))
                    Text("Correct! / ᱥᱟᱹᱦᱤ ᱜᱮᱭᱟ!", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
