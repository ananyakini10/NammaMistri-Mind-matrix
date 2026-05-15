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
import com.nammamistri.app.data.Expense
import com.nammamistri.app.data.Project
import com.nammamistri.app.ui.T

@Composable
fun ExpenseScreen(
    text: T,
    project: Project?,
    expenses: List<Expense>,
    onAddExpense: (String, Double, Long) -> Unit
) {
    if (project == null) {
        EmptyState(text.selectProject)
        return
    }

    var title by remember(project.id) { mutableStateOf("") }
    var amount by remember(project.id) { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        SectionCard {
            Text("${text.expenses}: ${project.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text.addExpense, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text(text.expenseTitle) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { ch -> ch.isDigit() || ch == '.' } },
                label = { Text(text.amount) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                val amountValue = amount.toDoubleOrNull() ?: 0.0
                if (title.isNotBlank()) {
                    onAddExpense(title.trim(), amountValue, project.id)
                    title = ""
                    amount = ""
                }
            }) { Text(text.save) }
        }

        if (expenses.isEmpty()) {
            EmptyState(text.noExpenses)
        } else {
            expenses.forEach { expense ->
                SectionCard {
                    Text(expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${text.amount}: ${expense.amount.money()}")
                }
            }
        }
    }
}
