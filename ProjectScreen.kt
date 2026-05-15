package com.nammamistri.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun ProjectScreen(
    text: T,
    projects: List<Project>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAddProject: (String, Int) -> Unit,
    onUpdateProject: (Project) -> Unit,
    onDeleteProject: (Project) -> Unit,
    onOpenProject: (Project, AppScreen) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var totalDays by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        SectionCard {
            Text(text.addProject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(text.projectName) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = totalDays,
                onValueChange = { totalDays = it.filter(Char::isDigit) },
                label = { Text(text.totalDays) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val days = totalDays.toIntOrNull() ?: 0
                    if (name.isNotBlank() && days > 0) {
                        onAddProject(name.trim(), days)
                        name = ""
                        totalDays = ""
                    }
                }
            ) { Text(text.save) }
        }

        SectionCard {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                label = { Text(text.searchProjects) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (projects.isEmpty()) {
            EmptyState(text.noProjects)
        } else {
            projects.forEach { project ->
                ProjectItem(
                    text = text,
                    project = project,
                    onUpdateProject = onUpdateProject,
                    onDeleteProject = onDeleteProject,
                    onOpenProject = onOpenProject
                )
            }
        }
    }
}

@Composable
private fun ProjectItem(
    text: T,
    project: Project,
    onUpdateProject: (Project) -> Unit,
    onDeleteProject: (Project) -> Unit,
    onOpenProject: (Project, AppScreen) -> Unit
) {
    var editing by remember(project.id) { mutableStateOf(false) }
    var deleteOpen by remember(project.id) { mutableStateOf(false) }
    var editName by remember(project.id, project.name) { mutableStateOf(project.name) }

    SectionCard {
        if (editing) {
            OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text(text.projectName) }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (editName.isNotBlank()) {
                        onUpdateProject(project.copy(name = editName.trim()))
                        editing = false
                    }
                }) { Text(text.save) }
                OutlinedButton(onClick = { editing = false }) { Text(text.cancel) }
            }
        } else {
            Text(project.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("${text.completedDays}: ${project.completedDays} / ${project.totalDays}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { onOpenProject(project, AppScreen.Summary) }) { Text(text.open) }
                OutlinedButton(onClick = { editing = true }) { Text(text.edit) }
                OutlinedButton(onClick = { deleteOpen = true }) { Text(text.delete) }
            }
            OutlinedButton(
                onClick = { onOpenProject(project, AppScreen.Materials) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text.materials)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { onOpenProject(project, AppScreen.Workers) }) { Text(text.workers) }
                OutlinedButton(onClick = { onOpenProject(project, AppScreen.Expenses) }) { Text(text.expenses) }
                OutlinedButton(onClick = { onOpenProject(project, AppScreen.Photos) }) { Text(text.photos) }
            }
        }
    }

    if (deleteOpen) {
        AlertDialog(
            onDismissRequest = { deleteOpen = false },
            title = { Text(text.confirmDelete) },
            text = { Text(text.deleteProjectMessage) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteProject(project)
                    deleteOpen = false
                }) { Text(text.delete) }
            },
            dismissButton = {
                TextButton(onClick = { deleteOpen = false }) { Text(text.cancel) }
            }
        )
    }
}
