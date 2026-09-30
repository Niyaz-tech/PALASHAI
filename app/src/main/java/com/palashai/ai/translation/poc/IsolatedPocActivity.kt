package com.palashai.ai.translation.poc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

class IsolatedPocActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("IndicTrans2 POC Runner")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        val modelPath = File(getExternalFilesDir(null), "indictrans_poc").absolutePath
                        Log.d("PalashPoc", "Activity triggering POC with path: $modelPath")
                        Thread {
                            PocTester.runTest(modelPath)
                        }.start()
                    }) {
                        Text("Run POC Inference")
                    }
                }
            }
        }
    }
}
