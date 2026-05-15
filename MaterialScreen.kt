package com.nammamistri.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nammamistri.app.data.Project
import com.nammamistri.app.ui.T
import kotlin.math.ceil

private data class MaterialResult(
    val volume: Double,
    val bricks: Int,
    val cementBags: Int,
    val sandLoads: Double,
    val totalCost: Double
)

@Composable
fun MaterialScreen(
    text: T,
    project: Project?
) {
    if (project == null) {
        EmptyState(text.selectProject)
        return
    }

    var length by remember(project.id) { mutableStateOf("") }
    var height by remember(project.id) { mutableStateOf("") }
    var thickness by remember(project.id) { mutableStateOf("9") }
    var brickRate by remember(project.id) { mutableStateOf("") }
    var cementRate by remember(project.id) { mutableStateOf("") }
    var sandRate by remember(project.id) { mutableStateOf("") }
    var result by remember(project.id) { mutableStateOf<MaterialResult?>(null) }
    var message by remember(project.id) { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        SectionCard {
            Text("${text.materials}: ${project.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text.materialCalculator, fontWeight = FontWeight.Bold)
            NumberField(value = length, onValueChange = { length = it }, label = text.wallLength)
            NumberField(value = height, onValueChange = { height = it }, label = text.wallHeight)
            NumberField(value = thickness, onValueChange = { thickness = it }, label = text.wallThickness)
            NumberField(value = brickRate, onValueChange = { brickRate = it }, label = text.brickRate)
            NumberField(value = cementRate, onValueChange = { cementRate = it }, label = text.cementRate)
            NumberField(value = sandRate, onValueChange = { sandRate = it }, label = text.sandRate)
            Button(
                onClick = {
                    val wallLength = length.toDoubleOrNull()
                    val wallHeight = height.toDoubleOrNull()
                    val wallThickness = thickness.toDoubleOrNull()
                    val brickPrice = brickRate.toDoubleOrNull()
                    val cementPrice = cementRate.toDoubleOrNull()
                    val sandPrice = sandRate.toDoubleOrNull()

                    if (
                        wallLength == null || wallHeight == null || wallThickness == null ||
                        brickPrice == null || cementPrice == null || sandPrice == null ||
                        wallLength <= 0.0 || wallHeight <= 0.0 || wallThickness <= 0.0 ||
                        brickPrice < 0.0 || cementPrice < 0.0 || sandPrice < 0.0
                    ) {
                        result = null
                        message = text.enterValidValues
                        return@Button
                    }

                    val volume = wallLength * wallHeight * (wallThickness / 12.0)
                    val bricks = ceil(volume * 13.5).toInt()
                    val cementBags = ceil(volume * 0.08).toInt()
                    val sandLoads = (volume * 0.25) / 100.0
                    val totalCost = (bricks * brickPrice) + (cementBags * cementPrice) + (sandLoads * sandPrice)

                    result = MaterialResult(
                        volume = volume,
                        bricks = bricks,
                        cementBags = cementBags,
                        sandLoads = sandLoads,
                        totalCost = totalCost
                    )
                    message = ""
                }
            ) {
                Text(text.calculate)
            }

            if (message.isNotBlank()) {
                Text(message, color = MaterialTheme.colorScheme.error)
            }
        }

        result?.let { material ->
            SectionCard {
                Text(text.summary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${text.wallVolume}: ${"%.2f".format(material.volume)} cu ft")
                Text("${text.bricksRequired}: ${material.bricks}")
                Text("${text.cementBags}: ${material.cementBags}")
                Text("${text.sandRequired}: ${"%.2f".format(material.sandLoads)}")
                Text("${text.materialCost}: ${material.totalCost.money()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onValueChange(input.filter { it.isDigit() || it == '.' }) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )
}
