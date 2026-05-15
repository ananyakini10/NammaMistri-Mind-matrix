package com.nammamistri.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammamistri.app.R
import com.nammamistri.app.ui.AppLanguage
import com.nammamistri.app.ui.T

enum class AppScreen { Projects, Materials, Workers, Expenses, Photos, Summary }

@Composable
fun Header(
    text: T,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF135D66), Color(0xFFE15A3D))
                )
            )
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_namma_logo),
                contentDescription = text.appName,
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text.appName, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text.tagline, color = Color(0xFFFFF4D2), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text.language, color = Color.White, style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (language == AppLanguage.English) {
                        Button(onClick = { onLanguageChange(AppLanguage.Kannada) }) { Text(text.kannada) }
                    } else {
                        Button(onClick = { onLanguageChange(AppLanguage.English) }) { Text(text.english) }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationTabs(
    text: T,
    current: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            AppScreen.Projects to text.projects,
            AppScreen.Materials to text.materials,
            AppScreen.Workers to text.workers,
            AppScreen.Expenses to text.expenses,
            AppScreen.Photos to text.photos,
            AppScreen.Summary to text.summary
        ).forEach { (screen, label) ->
            if (screen == current) {
                Button(onClick = { onNavigate(screen) }) { Text(label) }
            } else {
                OutlinedButton(onClick = { onNavigate(screen) }) { Text(label) }
            }
        }
    }
}

@Composable
fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            content()
        }
    }
}

@Composable
fun EmptyState(message: String) {
    SectionCard {
        Text(message, style = MaterialTheme.typography.bodyLarge)
    }
}

fun Double.money(): String = "₹%.2f".format(this)

@Composable
fun ScreenSpacer() {
    Spacer(Modifier.height(16.dp))
}
