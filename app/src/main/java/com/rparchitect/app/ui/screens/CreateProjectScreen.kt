package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CreateBackground = Color(0xFF101116)
private val CreateCard = Color(0xFF1B1D26)
private val CreateAccent = Color(0xFF9B8CFF)
private val CreateSecondary = Color(0xFF9A9BA8)

private val genreOptions = listOf(
    "Fantasy",
    "Modern",
    "Sci-Fi",
    "Crime",
    "Romance",
    "Action",
    "Historical",
    "Supernatural",
    "Horror",
    "Custom"
)

@Composable
fun CreateProjectScreen(
    onBackClick: () -> Unit,
    onCreateClick: (
        name: String,
        description: String,
        genre: String
    ) -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Fantasy") }
    var customGenre by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreateBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Back", color = CreateAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Create Project",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Start building a new roleplay world.",
            color = CreateSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text("PROJECT NAME", color = CreateSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = projectName,
            onValueChange = { projectName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter project name") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = CreateAccent,
                unfocusedBorderColor = CreateCard,
                focusedPlaceholderColor = CreateSecondary,
                unfocusedPlaceholderColor = CreateSecondary
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text("DESCRIPTION", color = CreateSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = projectDescription,
            onValueChange = { projectDescription = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Describe your project...") },
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = CreateAccent,
                unfocusedBorderColor = CreateCard,
                focusedPlaceholderColor = CreateSecondary,
                unfocusedPlaceholderColor = CreateSecondary
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text("GENRE", color = CreateSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedButton(
                onClick = { genreExpanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    selectedGenre,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text("⌄", color = CreateAccent)
            }

            DropdownMenu(
                expanded = genreExpanded,
                onDismissRequest = { genreExpanded = false }
            ) {
                genreOptions.forEach { genre ->
                    DropdownMenuItem(
                        text = { Text(genre) },
                        onClick = {
                            selectedGenre = genre
                            genreExpanded = false
                        }
                    )
                }
            }
        }

        if (selectedGenre == "Custom") {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = customGenre,
                onValueChange = { customGenre = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter custom genre") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = CreateAccent,
                    unfocusedBorderColor = CreateCard
                )
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        Button(
            onClick = {
                val finalGenre = if (selectedGenre == "Custom") {
                    customGenre.trim()
                } else {
                    selectedGenre
                }

                if (
                    projectName.isNotBlank() &&
                    finalGenre.isNotBlank()
                ) {
                    onCreateClick(
                        projectName.trim(),
                        projectDescription.trim(),
                        finalGenre
                    )
                }
            },
            enabled = projectName.isNotBlank() &&
                (selectedGenre != "Custom" || customGenre.isNotBlank()),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CreateAccent,
                disabledContainerColor = CreateCard
            )
        ) {
            Text(
                "CREATE PROJECT",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
