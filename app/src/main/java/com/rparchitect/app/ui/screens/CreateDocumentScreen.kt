
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.RPTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDocumentScreen(
    templates: List<RPTemplate>,
    title: String,
    selectedTemplate: RPTemplate?,
    onTitleChange: (String) -> Unit,
    onTemplateSelected: (RPTemplate) -> Unit,
    onBackClick: () -> Unit,
    onContinue: (String, RPTemplate) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val availableTemplates = templates.filter {
        !(it.isCustom &&
            it.name == "Custom Template" &&
            it.fields.isEmpty())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Document")
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = {
                    Text("Document Title")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {

                OutlinedTextField(
                    value = selectedTemplate?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Select Template")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    availableTemplates.forEach { template ->

                        DropdownMenuItem(
                            text = {
                                Text(template.name)
                            },
                            onClick = {
                                onTemplateSelected(template)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val template = selectedTemplate

                    if (title.isNotBlank() && template != null) {
                        onContinue(title.trim(), template)
                    }
                },
                enabled = title.isNotBlank() && selectedTemplate != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}
