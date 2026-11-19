package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rparchitect.app.data.model.RPWorldEntry

private val CreateEntryBackground = Color(0xFF101116)
private val CreateEntryCard = Color(0xFF1B1D26)
private val CreateEntryAccent = Color(0xFF9B8CFF)
private val CreateEntrySecondary = Color(0xFF9A9BA8)

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

    val availableParents = entries.filter {
        it.type != "Headquarters"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreateEntryBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Locations & Factions", color = CreateEntryAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Create World Entry",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Add a location or organization to your world.",
            color = CreateEntrySecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        EntryInputLabel("NAME")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter name") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("ENTRY TYPE")

        ExposedDropdownMenuBox(
            expanded = typeExpanded,
            onExpandedChange = { typeExpanded = !typeExpanded }
        ) {
            OutlinedTextField(
                value = type,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = typeExpanded
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = createEntryFieldColors()
            )

            ExposedDropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false }
            ) {
                worldEntryTypes.forEach { entryType ->
                    DropdownMenuItem(
                        text = { Text(entryType) },
                        onClick = {
                            type = entryType
                            typeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("PARENT ENTRY (OPTIONAL)")

        ExposedDropdownMenuBox(
            expanded = parentExpanded,
            onExpandedChange = {
                parentExpanded = !parentExpanded
            }
        ) {
            OutlinedTextField(
                value = availableParents.firstOrNull {
                    it.id == parentId
                }?.name ?: "None",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = parentExpanded
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = createEntryFieldColors()
            )

            ExposedDropdownMenu(
                expanded = parentExpanded,
                onDismissRequest = {
                    parentExpanded = false
                }
            ) {
                DropdownMenuItem(
                    text = { Text("None") },
                    onClick = {
                        parentId = null
                        parentExpanded = false
                    }
                )

                availableParents.forEach { entry ->
                    DropdownMenuItem(
                        text = {
                            Text("${entry.name} (${entry.type})")
                        },
                        onClick = {
                            parentId = entry.id
                            parentExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("DESCRIPTION")

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Describe this entry...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("HISTORY")

        OutlinedTextField(
            value = history,
            onValueChange = { history = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Background and historical events...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("GOVERNMENT / POLITICAL SYSTEM")

        OutlinedTextField(
            value = government,
            onValueChange = { government = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Government or political structure...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("LEADER / AUTHORITY")

        OutlinedTextField(
            value = leader,
            onValueChange = { leader = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Leader, founder or person in charge...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("HEADQUARTERS")

        OutlinedTextField(
            value = headquarters,
            onValueChange = { headquarters = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Main headquarters or base...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("TERRITORY / AREA OF INFLUENCE")

        OutlinedTextField(
            value = territory,
            onValueChange = { territory = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = { Text("Territory, jurisdiction or area controlled...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EntryInputLabel("ADDITIONAL NOTES")

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = { Text("Other information...") },
            shape = RoundedCornerShape(12.dp),
            colors = createEntryFieldColors()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
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
                    notes.trim(),
                    ""
                )
            },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CreateEntryAccent
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Create Entry",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun EntryInputLabel(label: String) {
    Text(
        label,
        color = CreateEntrySecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun createEntryFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = CreateEntryCard,
        unfocusedContainerColor = CreateEntryCard,
        focusedBorderColor = CreateEntryAccent,
        unfocusedBorderColor = Color(0xFF353744),
        focusedPlaceholderColor = CreateEntrySecondary,
        unfocusedPlaceholderColor = CreateEntrySecondary,
        cursorColor = CreateEntryAccent
    )
