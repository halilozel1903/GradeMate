package com.halil.ozel.grademate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
    var result by remember { mutableStateOf("") }
    var gradeColor by remember { mutableStateOf(Color.Black) }

    var midtermWeight by remember { mutableStateOf(40f) }
    val finalWeightComputed = 100f - midtermWeight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🎓 Grade Calculator", fontSize = 24.sp)

        OutlinedTextField(
            value = midtermInput,
            onValueChange = { midtermInput = it },
            label = { Text("Midterm Grade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = finalInput,
            onValueChange = { finalInput = it },
            label = { Text("Final Grade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text("Midterm Weight: ${midtermWeight.toInt()}% — Final Weight: ${finalWeightComputed.toInt()}%")

        Slider(
            value = midtermWeight,
            onValueChange = { midtermWeight = it },
            valueRange = 0f..100f
        )

        Button(onClick = {
            val midtermGrade = midtermInput.toFloatOrNull()
            val finalGrade = finalInput.toFloatOrNull()

            if (midtermGrade != null && finalGrade != null) {
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
                gradeColor = color
                result = "Average: %.2f → Letter Grade: %s".format(average, grade)
            } else {
                result = "Please enter valid numbers!"
            }
        }) {
            Text("CALCULATE")
        }

        if (result.isNotEmpty()) {
            Text(
                text = result,
                fontSize = 18.sp,
                color = gradeColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
