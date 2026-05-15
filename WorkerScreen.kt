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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nammamistri.app.data.Attendance
import com.nammamistri.app.data.Project
import com.nammamistri.app.data.Worker
import com.nammamistri.app.ui.T

@Composable
fun WorkerScreen(
    text: T,
    project: Project?,
    workers: List<Worker>,
    attendance: List<Attendance>,
    onAddWorker: (String, Double, Double, Long) -> Unit,
    onAttendanceUpdate: (Long, Long, Int) -> Unit
) {
    if (project == null) {
        EmptyState(text.selectProject)
        return
    }

    var name by remember(project.id) { mutableStateOf("") }
    var wage by remember(project.id) { mutableStateOf("") }
    var advance by remember(project.id) { mutableStateOf("") }
    val dayInputs = remember(project.id) { mutableStateMapOf<Long, String>() }

    LaunchedEffect(attendance) {
        attendance.forEach { dayInputs[it.workerId] = it.daysWorked.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        SectionCard {
            Text("${text.workers}: ${project.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text.addWorker, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(text.workerName) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = wage, onValueChange = { wage = it.filter { ch -> ch.isDigit() || ch == '.' } }, label = { Text(text.wage) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = advance, onValueChange = { advance = it.filter { ch -> ch.isDigit() || ch == '.' } }, label = { Text(text.advance) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                val wageValue = wage.toDoubleOrNull() ?: 0.0
                val advanceValue = advance.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank()) {
                    onAddWorker(name.trim(), wageValue, advanceValue, project.id)
                    name = ""
                    wage = ""
                    advance = ""
                }
            }) { Text(text.save) }
        }

        if (workers.isEmpty()) {
            EmptyState(text.noWorkers)
        } else {
            workers.forEach { worker ->
                SectionCard {
                    Text(worker.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${text.wage}: ${worker.wage.money()}")
                    Text("${text.advance}: ${worker.advance.money()}")
                    Text("${text.labourCost}: ${(worker.wage - worker.advance).money()}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = dayInputs[worker.id] ?: "",
                            onValueChange = { dayInputs[worker.id] = it.filter(Char::isDigit) },
                            label = { Text(text.daysWorked) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = {
                            onAttendanceUpdate(worker.id, project.id, dayInputs[worker.id]?.toIntOrNull() ?: 0)
                        }) { Text(text.save) }
                    }
                }
            }
        }
    }
}
