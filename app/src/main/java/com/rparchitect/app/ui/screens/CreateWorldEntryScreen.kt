
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.RPWorldEntry

private val worldEntryTypes = listOf(
    "Country",
    "Region",
    "City / Town",
    "Building / Facility",
    "Organization",
    "Government / Kingdom",
    "Group",
    "Headquarters"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorldEntryScreen(
    entries: List<RPWorldEntry>,
    onBackClick: () -> Unit,
    onCreateClick: (
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
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(worldEntryTypes.first()) }
    var parentId by remember { mutableStateOf<Long?>(null) }

    var description by remember { mutableStateOf("") }
    var history by remember { mutableStateOf("") }
    var government by remember { mutableStateOf("") }
    var leader by remember { mutableStateOf("") }
    var headquarters by remember { mutableStateOf("") }
    var territory by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var typeExpanded by remember { mutableStateOf(false) }
    var parentExpanded by remember { mutableStateOf(false) }

    val parentOptions = remember(entries) {
        val result = mutableListOf<Pair<RPWorldEntry, Int>>()
        val visited = mutableSetOf<Long>()

        val entriesByParent = entries
            .filter { it.type != "Headquarters" }
            .groupBy { it.parentId }

        val validIds = entriesByParent.values
            .flatten()
            .map { it.id }
            .toSet()

        val roots = entries.filter {
            it.type != "Headquarters" &&
                (it.parentId == null || it.parentId !in validIds)
        }

        fun addChildren(parentId: Long?, depth: Int) {
            val children = if (parentId == null) {
                roots
            } else {
                entriesByParent[parentId].orEmpty()
            }

            children.forEach { entry ->
                if (visited.add(entry.id)) {
                    result.add(entry to depth)
                    addChildren(entry.id, depth + 1)
                }
            }
        }

        addChildren(null, 0)

        entries.forEach { entry ->
            if (
                entry.type != "Headquarters" &&
                entry.id !in visited
            ) {
                result.add(entry to 0)
            }
        }

        result
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Location / Faction")
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
                    worldEntryTypes.forEach { option ->
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
                        it.first.id == parentId
                    }?.let {
                        "${it.first.name} (${it.first.type})"
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

                    parentOptions.forEach { (entry, depth) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "${"   ".repeat(depth)}${entry.name} (${entry.type})"
                                )
                            },
                            onClick = {
                                parentId = entry.id
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
                minLines = 3,
                maxLines = 4
            )

            OutlinedTextField(
                value = history,
                onValueChange = { history = it },
                label = { Text("History") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 4
            )

            OutlinedTextField(
                value = government,
                onValueChange = { government = it },
                label = { Text("Government / Structure") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
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
                minLines = 2,
                maxLines = 4
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Additional Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 4
            )

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onCreateClick(
                            parentId,
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
                Text("Create Entry")
            }
        }
    }
}
