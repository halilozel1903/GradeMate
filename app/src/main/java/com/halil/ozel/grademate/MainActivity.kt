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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var midterm by remember { mutableStateOf("") }
    var finalExam by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🎓 Grade Calculator", fontSize = 24.sp)

        OutlinedTextField(
            value = midterm,
            onValueChange = { midterm = it },
            label = { Text("Midterm Grade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = finalExam,
            onValueChange = { finalExam = it },
            label = { Text("Final Grade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            val midtermGrade = midterm.toFloatOrNull()
            val finalGrade = finalExam.toFloatOrNull()

            if (midtermGrade != null && finalGrade != null) {
                val average = midtermGrade * 0.4f + finalGrade * 0.6f
                val letterGrade = when (average) {
                    in 90f..100f -> "AA"
                    in 85f..<90f -> "BA"
                    in 80f..<85f -> "BB"
                    in 70f..<80f -> "CB"
                    in 60f..<70f -> "CC"
                    in 50f..<60f -> "DC"
                    in 40f..<50f -> "DD"
                    else -> "FF"
                }
                result = "Average: %.2f → Letter Grade: %s".format(average, letterGrade)
            } else {
                result = "Please enter valid numbers!"
            }
        }) {
            Text("CALCULATE")
        }

        if (result.isNotEmpty()) {
            Text(text = result, fontSize = 18.sp)
        }
    }
}
