
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.RPWorldEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorldEntryScreen(
    entry: RPWorldEntry,
    entries: List<RPWorldEntry>,
    onBackClick: () -> Unit,
    onUpdateClick: (
        Long?,
        String,
        String,
        String,
        String,
        String,
        String,
        String,
        String,
        String
    ) -> Unit
) {
    var name by remember(entry.id) { mutableStateOf(entry.name) }
    var type by remember(entry.id) { mutableStateOf(entry.type) }
    var parentId by remember(entry.id) { mutableStateOf(entry.parentId) }

    var description by remember(entry.id) { mutableStateOf(entry.description) }
    var history by remember(entry.id) { mutableStateOf(entry.history) }
    var government by remember(entry.id) { mutableStateOf(entry.government) }
    var leader by remember(entry.id) { mutableStateOf(entry.leader) }
    var headquarters by remember(entry.id) { mutableStateOf(entry.headquarters) }
    var territory by remember(entry.id) { mutableStateOf(entry.territory) }
    var notes by remember(entry.id) { mutableStateOf(entry.notes) }

    var typeExpanded by remember { mutableStateOf(false) }
    var parentExpanded by remember { mutableStateOf(false) }

    val entryTypes = listOf(
        "Country",
        "Region",
        "City / Town",
        "Building / Facility",
        "Organization",
        "Government / Kingdom",
        "Group",
        "Headquarters"
    )

    val hasChildren = remember(entry.id, entries) {
        entries.any { it.parentId == entry.id }
    }

    val availableEntryTypes = entryTypes.filter { option ->
        option != "Headquarters" ||
            !hasChildren ||
            type == "Headquarters"
    }

    val descendantIds = remember(entry.id, entries) {
        val descendants = mutableSetOf<Long>()

        fun collectChildren(parentId: Long) {
            entries
                .filter { it.parentId == parentId }
                .forEach { child ->
                    if (descendants.add(child.id)) {
                        collectChildren(child.id)
                    }
                }
        }

        collectChildren(entry.id)
        descendants
    }

    val parentOptions = remember(entries, entry.id, descendantIds) {
        entries.filter {
            it.id != entry.id &&
                it.id !in descendantIds &&
                it.type != "Headquarters"
        }
    }

    val validParentId = parentId?.takeIf { id ->
        parentOptions.any { it.id == id }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Location / Faction")
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
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
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Entry Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = typeExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = {
                        typeExpanded = false
                    }
                ) {
                    availableEntryTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                type = option
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = parentExpanded,
                onExpandedChange = {
                    parentExpanded = !parentExpanded
                }
            ) {
                OutlinedTextField(
                    value = parentOptions.firstOrNull {
                        it.id == validParentId
                    }?.let {
                        "${it.name} (${it.type})"
                    } ?: "None (Root Entry)",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Parent Entry") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = parentExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = parentExpanded,
                    onDismissRequest = {
                        parentExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("None (Root Entry)")
                        },
                        onClick = {
                            parentId = null
                            parentExpanded = false
                        }
                    )

                    parentOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text("${option.name} (${option.type})")
                            },
                            onClick = {
                                parentId = option.id
                                parentExpanded = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider()

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
                maxLines = 5
            )

            OutlinedTextField(
                value = history,
                onValueChange = { history = it },
                label = { Text("History") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
                maxLines = 5
            )

            OutlinedTextField(
                value = government,
                onValueChange = { government = it },
                label = { Text("Government / Structure") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
                maxLines =4
            )

            OutlinedTextField(
                value = leader,
                onValueChange = { leader = it },
                label = { Text("Leader") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = headquarters,
                onValueChange = { headquarters = it },
                label = { Text("Headquarters") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = territory,
                onValueChange = { territory = it },
                label = { Text("Territory / Area of Influence") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
                maxLines = 5
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Additional Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
                maxLines = 5
            )

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onUpdateClick(
                            validParentId,
                            name.trim(),
                            type,
                            description.trim(),
                            history.trim(),
                            government.trim(),
                            leader.trim(),
                            headquarters.trim(),
                            territory.trim(),
                            notes.trim()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Save Changes")
            }
        }
    }
}
