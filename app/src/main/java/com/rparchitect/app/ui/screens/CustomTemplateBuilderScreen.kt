package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.CustomTemplateFieldData
import com.rparchitect.app.data.model.RPCustomTemplate
import com.rparchitect.app.data.model.RPCustomTemplateField
import com.rparchitect.app.data.model.RPCustomTemplateOption
import com.rparchitect.app.data.model.TemplateFieldType
import java.util.UUID

private data class BuilderFieldState(
    val id: String = UUID.randomUUID().toString(),
    var label: String = "",
    var type: TemplateFieldType = TemplateFieldType.TEXT,
    var required: Boolean = false,
    var options: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTemplateBuilderScreen(
    existingTemplate: RPCustomTemplate? = null,
    existingFields: List<CustomTemplateFieldData> = emptyList(),
    onBack: () -> Unit,
    onSave: (
        RPCustomTemplate,
        List<CustomTemplateFieldData>
    ) -> Unit
) {
    var templateName by remember(existingTemplate?.id) {
        mutableStateOf(existingTemplate?.name ?: "")
    }

    var description by remember(existingTemplate?.id) {
        mutableStateOf(existingTemplate?.description ?: "")
    }

    val fields = remember(existingTemplate?.id) {
        mutableStateListOf<BuilderFieldState>().apply {
            existingFields.forEach { data ->
                add(
                    BuilderFieldState(
                        id = data.field.id,
                        label = data.field.label,
                        type = runCatching {
                            TemplateFieldType.valueOf(data.field.type)
                        }.getOrDefault(TemplateFieldType.TEXT),
                        required = data.field.required,
                        options = data.options.joinToString(", ") {
                            it.value
                        }
                    )
                )
            }
        }
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = if (existingTemplate == null) {
                "Create Custom Template"
            } else {
                "Edit Custom Template"
            },
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = templateName,
            onValueChange = {
                templateName = it
                errorMessage = ""
            },
            label = { Text("Template Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Template Fields",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${fields.size} fields",
                style = MaterialTheme.typography.bodySmall
            )
        }

        fields.forEachIndexed { index, field ->

            var typeExpanded by remember(field.id) {
                mutableStateOf(false)
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Field ${index + 1}",
                            style = MaterialTheme.typography.titleSmall
                        )

                        TextButton(
                            onClick = {
                                fields.removeAt(index)
                            }
                        ) {
                            Text("Remove")
                        }
                    }

                    OutlinedTextField(
                        value = field.label,
                        onValueChange = {
                            field.label = it
                        },
                        label = { Text("Field Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = {
                            typeExpanded = !typeExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = field.type.name
                                .replace("_", " "),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Field Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = typeExpanded
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = {
                                typeExpanded = false
                            }
                        ) {
                            TemplateFieldType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            type.name.replace("_", " ")
                                        )
                                    },
                                    onClick = {
                                        field.type = type
                                        typeExpanded = false
                                        if (type != TemplateFieldType.CATEGORY) {
                                            field.options = ""
                                        }
                                    }
                                )
                            }
                        }
                    }

                    if (field.type == TemplateFieldType.CATEGORY) {
                        OutlinedTextField(
                            value = field.options,
                            onValueChange = {
                                field.options = it
                            },
                            label = { Text("Category Options") },
                            placeholder = {
                                Text("Example: Hero, Villain, Neutral")
                            },
                            supportingText = {
                                Text("Separate each option with a comma.")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = field.required,
                            onCheckedChange = {
                                field.required = it
                            }
                        )

                        Text("Required field")
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                fields.add(BuilderFieldState())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Add Field")
        }

        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                when {
                    templateName.isBlank() -> {
                        errorMessage = "Template name is required."
                    }

                    fields.isEmpty() -> {
                        errorMessage = "Add at least one field."
                    }

                    fields.any { it.label.isBlank() } -> {
                        errorMessage = "Every field needs a name."
                    }

                    fields.any {
                        it.type == TemplateFieldType.CATEGORY &&
                            it.options.split(",").none { option ->
                                option.isNotBlank()
                            }
                    } -> {
                        errorMessage =
                            "Every category field needs at least one option."
                    }

                    else -> {
                        val now = System.currentTimeMillis()

                        val template = RPCustomTemplate(
                            id = existingTemplate?.id
                                ?: UUID.randomUUID().toString(),
                            name = templateName.trim(),
                            description = description.trim(),
                            createdAt = existingTemplate?.createdAt ?: now,
                            updatedAt = now
                        )

                        val fieldData = fields.mapIndexed { index, item ->

                            val fieldId = item.id

                            val savedField = RPCustomTemplateField(
                                id = fieldId,
                                templateId = template.id,
                                label = item.label.trim(),
                                type = item.type.name,
                                required = item.required
                            )

                            val options = if (
                                item.type == TemplateFieldType.CATEGORY
                            ) {
                                item.options
                                    .split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }
                                    .distinct()
                                    .mapIndexed { optionIndex, value ->
                                        RPCustomTemplateOption(
                                            id = UUID.randomUUID().toString(),
                                            fieldId = fieldId,
                                            value = value,
                                            sortOrder = optionIndex
                                        )
                                    }
                            } else {
                                emptyList()
                            }

                            CustomTemplateFieldData(
                                field = savedField,
                                options = options
                            )
                        }

                        onSave(template, fieldData)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Template")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
