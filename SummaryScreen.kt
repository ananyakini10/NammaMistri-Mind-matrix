package com.nammamistri.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
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
import com.nammamistri.app.data.Expense
import com.nammamistri.app.data.Project
import com.nammamistri.app.data.Worker
import com.nammamistri.app.ui.T
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SummaryScreen(
    text: T,
    project: Project?,
    workers: List<Worker>,
    expenses: List<Expense>,
    onUpdateProject: (Project) -> Unit
) {
    if (project == null) {
        EmptyState(text.selectProject)
        return
    }

    var completedInput by remember(project.id, project.completedDays) { mutableStateOf(project.completedDays.toString()) }
    val labourCost = workers.sumOf { it.wage - it.advance }
    val totalExpenses = expenses.sumOf { it.amount }
    val finalTotal = labourCost + totalExpenses
    val progress = if (project.totalDays > 0) project.completedDays.toFloat() / project.totalDays.toFloat() else 0f
    val percent = (progress.coerceIn(0f, 1f) * 100).toInt()
    val remaining = (project.totalDays - project.completedDays).coerceAtLeast(0)
    val date = remember(project.createdDate) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(project.createdDate))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        SectionCard {
            Text(project.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("${text.createdDate}: $date")
            Text("${text.progress}: $percent%")
            LinearProgressIndicator(progress = { progress.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
                Text("${text.completedDays}: ${project.completedDays}")
                Text("${text.remainingDays}: $remaining")
            }
        }

        SectionCard {
            Text(text.summary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${text.totalWorkers}: ${workers.size}")
            Text("${text.labourCost}: ${labourCost.money()}")
            Text("${text.totalExpenses}: ${totalExpenses.money()}")
            Text("${text.finalTotal}: ${finalTotal.money()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        SectionCard {
            Text(text.updateProgress, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = completedInput,
                onValueChange = { completedInput = it.filter(Char::isDigit) },
                label = { Text(text.completedDays) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                val completed = (completedInput.toIntOrNull() ?: 0).coerceIn(0, project.totalDays)
                onUpdateProject(project.copy(completedDays = completed))
            }) { Text(text.save) }
        }
    }
}
