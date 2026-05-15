package com.nammamistri.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import com.nammamistri.app.data.Photo
import com.nammamistri.app.data.Project
import com.nammamistri.app.ui.T

@Composable
fun PhotoScreen(
    text: T,
    project: Project?,
    photos: List<Photo>,
    onAddPhoto: (String, Long) -> Unit
) {
    if (project == null) {
        EmptyState(text.selectProject)
        return
    }

    var manualUri by remember(project.id) { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) onAddPhoto(uri.toString(), project.id)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        SectionCard {
            Text("${text.photos}: ${project.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Button(onClick = { launcher.launch("image/*") }) { Text(text.pickPhoto) }
            OutlinedTextField(value = manualUri, onValueChange = { manualUri = it }, label = { Text(text.photoUri) }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                if (manualUri.isNotBlank()) {
                    onAddPhoto(manualUri.trim(), project.id)
                    manualUri = ""
                }
            }) { Text(text.save) }
        }

        if (photos.isEmpty()) {
            EmptyState(text.noPhotos)
        } else {
            photos.forEach { photo ->
                SectionCard {
                    Text(text.photoUri, fontWeight = FontWeight.Bold)
                    Text(photo.uri)
                }
            }
        }
    }
}
