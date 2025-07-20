package com.halil.ozel.grademate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GradeResult(val text: String, val color: Color)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GradeCalculatorScreen()
        }
    }
}

@Composable
fun GradeCalculatorScreen() {
    var midtermInput by remember { mutableStateOf("") }
    var finalInput by remember { mutableStateOf("") }
    var gradeResult by remember { mutableStateOf<GradeResult?>(null) }

    var midtermError by remember { mutableStateOf<String?>(null) }
    var finalError by remember { mutableStateOf<String?>(null) }

    var midtermWeight by remember { mutableFloatStateOf(40f) }
    val finalWeightComputed = 100f - midtermWeight

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text("🎓 Grade Calculator", fontSize = 24.sp)

        OutlinedTextField(
            value = midtermInput,
            onValueChange = {
                midtermInput = it.filter { c -> c.isDigit() }.take(3)
                midtermError = when {
                    midtermInput.isBlank() -> {
                        gradeResult = null
                        "Midterm grade cannot be empty"
                    }
                    midtermInput.toIntOrNull() !in 0..100 -> "Enter a value between 0 and 100"
                    else -> null
                }
            },
            label = { Text("Midterm Grade") },
            isError = midtermError != null,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        midtermError?.let { Text(text = it, color = Color.Red, fontSize = 12.sp) }

        OutlinedTextField(
            value = finalInput,
            onValueChange = {
                finalInput = it.filter { c -> c.isDigit() }.take(3)
                finalError = when {
                    finalInput.isBlank() -> {
                        gradeResult = null
                        "Final grade cannot be empty"
                    }
                    finalInput.toIntOrNull() !in 0..100 -> "Enter a value between 0 and 100"
                    else -> null
                }
            },
            label = { Text("Final Grade") },
            isError = finalError != null,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        finalError?.let { Text(text = it, color = Color.Red, fontSize = 12.sp) }

        Text("Midterm Weight: ${midtermWeight.toInt()}% — Final Weight: ${finalWeightComputed.toInt()}%")

        Slider(
            value = midtermWeight,
            onValueChange = { midtermWeight = it },
            valueRange = 0f..100f
        )

        Button(
            onClick = {
                val midtermGrade = midtermInput.trim().toFloatOrNull()
                val finalGrade = finalInput.trim().toFloatOrNull()

                if (midtermGrade == null || finalGrade == null) {
                    gradeResult = GradeResult("Please enter valid numbers!", Color.Red)
                } else if (midtermGrade !in 0f..100f || finalGrade !in 0f..100f) {
                    gradeResult = GradeResult("Grades must be between 0 and 100!", Color.Red)
                } else {
                    val average = midtermGrade * (midtermWeight / 100f) + finalGrade * (finalWeightComputed / 100f)
                    val (grade, color) = when (average) {
                        in 90f..100f -> "AA" to Color(0xFF2E7D32)
                        in 85f..<90f -> "BA" to Color(0xFF388E3C)
                        in 80f..<85f -> "BB" to Color(0xFF66BB6A)
                        in 70f..<80f -> "CB" to Color(0xFFFFF176)
                        in 60f..<70f -> "CC" to Color(0xFFFFEE58)
                        in 50f..<60f -> "DC" to Color(0xFFFFB74D)
                        in 40f..<50f -> "DD" to Color(0xFFFFA726)
                        else -> "FF" to Color(0xFFD32F2F)
                    }
                    val formatted = "Average: %.2f → Letter Grade: %s".format(average, grade)
                    gradeResult = GradeResult(formatted, color)
                }
            },
            enabled = midtermInput.isNotBlank() && finalInput.isNotBlank() && midtermError == null && finalError == null
        ) {
            Text("CALCULATE")
        }

        gradeResult?.let {
            Text(
                text = it.text,
                fontSize = 18.sp,
                color = it.color,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
