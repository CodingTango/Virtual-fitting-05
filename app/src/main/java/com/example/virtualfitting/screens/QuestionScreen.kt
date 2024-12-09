package com.example.virtualfitting.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionScreen(
    question: String,
    options: List<String>,
    initialSelections: List<String>, // 초기 선택 항목 추가
    onSelect: (List<String>) -> Unit
) {
    var selectedOptions by remember { mutableStateOf(initialSelections) } // 초기 선택 항목 설정

    Column {
        Text(text = question, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = selectedOptions.contains(option),
                        onValueChange = {
                            selectedOptions = if (it) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                            onSelect(selectedOptions) // 선택 상태를 ViewModel에 전달
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = null
                )
                Text(option)
            }
        }
    }
}

