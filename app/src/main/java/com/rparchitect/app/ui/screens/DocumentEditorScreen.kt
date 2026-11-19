
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.RPTemplate
import com.rparchitect.app.data.model.TemplateFieldType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentEditorScreen(
    documentTitle: String,
    template: RPTemplate,
    initialValues: Map<String, String> = emptyMap(),
    onBackClick: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    val values = remember(template.id, initialValues) {
        mutableStateMapOf<String, String>().apply {
            template.fields.forEach { field ->
                this[field.id] = initialValues[field.id] ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(documentTitle)
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = template.name,
                style = MaterialTheme.typography.titleLarge
            )

            template.fields.forEach { field ->

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = field.label +
                            if (field.required) " *" else "",
                        style = MaterialTheme.typography.titleSmall
                    )

                    when (field.type) {

                        TemplateFieldType.TEXT -> {
                            OutlinedTextField(
                                value = values[field.id] ?: "",
                                onValueChange = {
                                    values[field.id] = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        TemplateFieldType.LONG_TEXT -> {
                            OutlinedTextField(
                                value = values[field.id] ?: "",
                                onValueChange = {
                                    values[field.id] = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                        }

                        TemplateFieldType.NUMBER -> {
                            OutlinedTextField(
                                value = values[field.id] ?: "",
                                onValueChange = { input ->
                                    if (input.isEmpty() ||
                                        input.toDoubleOrNull() != null
                                    ) {
                                        values[field.id] = input
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = {
                                    Text("Enter number")
                                }
                            )
                        }

                        TemplateFieldType.DATE -> {
                            OutlinedTextField(
                                value = values[field.id] ?: "",
                                onValueChange = {
                                    values[field.id] = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = {
                                    Text("DD/MM/YYYY")
                                }
                            )
                        }

                        TemplateFieldType.CATEGORY -> {
                            var expanded by remember(field.id) {
                                mutableStateOf(false)
                            }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = {
                                    expanded = !expanded
                                }
                            ) {
                                OutlinedTextField(
                                    value = values[field.id] ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults
                                            .TrailingIcon(
                                                expanded = expanded
                                            )
                                    },
                                    placeholder = {
                                        Text("Select category")
                                    }
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = {
                                        expanded = false
                                    }
                                ) {
                                    field.options.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(option)
                                            },
                                            onClick = {
                                                values[field.id] = option
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val missingRequired = template.fields.any { field ->
                        field.required &&
                            values[field.id].orEmpty().isBlank()
                    }

                    if (!missingRequired) {
                        onSave(values.toMap())
                    }
                },
                enabled = template.fields.none { field ->
                    field.required &&
                        values[field.id].orEmpty().isBlank()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Document")
            }
        }
    }
}
