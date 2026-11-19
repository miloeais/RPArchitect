
package com.rparchitect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.repository.ProjectRepository
import com.rparchitect.app.data.repository.CharacterRepository
import com.rparchitect.app.ui.screens.HomeScreen
import com.rparchitect.app.ui.screens.ProjectManagerScreen
import com.rparchitect.app.ui.screens.CreateProjectScreen
import com.rparchitect.app.ui.screens.EditProjectScreen
import com.rparchitect.app.ui.screens.WorkspaceScreen
import com.rparchitect.app.ui.screens.CharacterScreen
import com.rparchitect.app.ui.screens.CreateCharacterScreen
import com.rparchitect.app.ui.screens.EditCharacterScreen

private enum class AppScreen {
    HOME,
    PROJECTS,
    CREATE_PROJECT,
    EDIT_PROJECT,
    WORKSPACE,
    CHARACTERS,
    CREATE_CHARACTER,
    EDIT_CHARACTER
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RPArchitectApp()
        }
    }
}

@Composable
fun RPArchitectApp() {

    val projectRepository = remember {
        ProjectRepository()
    }

    val characterRepository = remember {
        CharacterRepository()
    }

    var projects by remember {
        mutableStateOf(projectRepository.getAllProjects())
    }

    var characters by remember {
        mutableStateOf(emptyList<RPCharacter>())
    }

    var currentScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    var selectedProject by remember {
        mutableStateOf<RPProject?>(null)
    }

    var selectedCharacter by remember {
        mutableStateOf<RPCharacter?>(null)
    }

    fun refreshProjects() {
        projects = projectRepository.getAllProjects()
    }

    fun refreshCharacters(projectId: Long) {
        characters = characterRepository.getCharactersByProject(projectId)
    }

    fun createProject(
        name: String,
        description: String,
        genre: String
    ) {
        val now = System.currentTimeMillis()

        val project = RPProject(
            id = now,
            name = name,
            description = description,
            genre = genre,
            createdAt = now,
            updatedAt = now
        )

        projectRepository.addProject(project)
        refreshProjects()
        currentScreen = AppScreen.PROJECTS
    }

    fun updateProject(
        name: String,
        description: String,
        genre: String
    ) {
        val oldProject = selectedProject ?: return

        val updatedProject = oldProject.copy(
            name = name,
            description = description,
            genre = genre,
            updatedAt = System.currentTimeMillis()
        )

        projectRepository.updateProject(updatedProject)
        refreshProjects()
        selectedProject = updatedProject
        currentScreen = AppScreen.PROJECTS
    }

    fun createCharacter(
        name: String,
        role: String,
        age: String,
        gender: String,
        personality: String,
        background: String,
        appearance: String,
        notes: String
    ) {
        val project = selectedProject ?: return
        val now = System.currentTimeMillis()

        val character = RPCharacter(
            id = now,
            projectId = project.id,
            name = name,
            role = role,
            age = age,
            gender = gender,
            personality = personality,
            background = background,
            appearance = appearance,
            notes = notes,
            createdAt = now,
            updatedAt = now
        )

        characterRepository.addCharacter(character)
        refreshCharacters(project.id)
        currentScreen = AppScreen.CHARACTERS
    }

    fun updateCharacter(
        name: String,
        role: String,
        age: String,
        gender: String,
        personality: String,
        background: String,
        appearance: String,
        notes: String
    ) {
        val oldCharacter = selectedCharacter ?: return
        val project = selectedProject ?: return

        val updatedCharacter = oldCharacter.copy(
            name = name,
            role = role,
            age = age,
            gender = gender,
            personality = personality,
            background = background,
            appearance = appearance,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )

        characterRepository.updateCharacter(updatedCharacter)
        selectedCharacter = updatedCharacter

        refreshCharacters(project.id)
        currentScreen = AppScreen.CHARACTERS
    }

    MaterialTheme {

        when (currentScreen) {

            AppScreen.HOME -> {
                HomeScreen(
                    onProjectsClick = {
                        currentScreen = AppScreen.PROJECTS
                    },
                    onTemplatesClick = {},
                    onSettingsClick = {},
                    onRecentProjectsClick = {
                        currentScreen = AppScreen.PROJECTS
                    }
                )
            }

            AppScreen.PROJECTS -> {
                ProjectManagerScreen(
                    projects = projects,
                    onBackClick = {
                        currentScreen = AppScreen.HOME
                    },
                    onCreateProject = {
                        currentScreen = AppScreen.CREATE_PROJECT
                    },
                    onProjectClick = { project ->
                        selectedProject = project
                        refreshCharacters(project.id)
                        currentScreen = AppScreen.WORKSPACE
                    },
                    onEditProject = { project ->
                        selectedProject = project
                        currentScreen = AppScreen.EDIT_PROJECT
                    },
                    onDeleteProject = { projectId ->
                        projectRepository.deleteProject(projectId)
                        refreshProjects()
                    }
                )
            }

            AppScreen.CREATE_PROJECT -> {
                CreateProjectScreen(
                    onBackClick = {
                        currentScreen = AppScreen.PROJECTS
                    },
                    onCreateClick = { name, description, genre ->
                        createProject(name, description, genre)
                    }
                )
            }

            AppScreen.EDIT_PROJECT -> {
                val project = selectedProject

                if (project != null) {
                    EditProjectScreen(
                        project = project,
                        onBackClick = {
                            currentScreen = AppScreen.PROJECTS
                        },
                        onSaveClick = { name, description, genre ->
                            updateProject(name, description, genre)
                        }
                    )
                }
            }

            AppScreen.WORKSPACE -> {
                val project = selectedProject

                if (project != null) {
                    WorkspaceScreen(
                        project = project,
                        onBackClick = {
                            currentScreen = AppScreen.PROJECTS
                        },
                        onCharactersClick = {
                            refreshCharacters(project.id)
                            currentScreen = AppScreen.CHARACTERS
                        },
                        onWorldBuildingClick = {},
                        onStoryClick = {}
                    )
                }
            }

            AppScreen.CHARACTERS -> {
                val project = selectedProject

                if (project != null) {
                    CharacterScreen(
                        projectName = project.name,
                        characters = characters,
                        onBackClick = {
                            currentScreen = AppScreen.WORKSPACE
                        },
                        onCreateCharacter = {
                            currentScreen = AppScreen.CREATE_CHARACTER
                        },
                        onEditCharacter = { character ->
                            selectedCharacter = character
                            currentScreen = AppScreen.EDIT_CHARACTER
                        },
                        onDeleteCharacter = { characterId ->
                            characterRepository.deleteCharacter(characterId)
                            refreshCharacters(project.id)
                        }
                    )
                }
            }

            AppScreen.CREATE_CHARACTER -> {
                CreateCharacterScreen(
                    onBackClick = {
                        currentScreen = AppScreen.CHARACTERS
                    },
                    onCreateClick = { name, role, age, gender,
                                      personality, background, appearance, notes ->
                        createCharacter(
                            name,
                            role,
                            age,
                            gender,
                            personality,
                            background,
                            appearance,
                            notes
                        )
                    }
                )
            }

            AppScreen.EDIT_CHARACTER -> {
                val character = selectedCharacter

                if (character != null) {
                    EditCharacterScreen(
                        character = character,
                        onBackClick = {
                            currentScreen = AppScreen.CHARACTERS
                        },
                        onSaveClick = { name, role, age, gender,
                                        personality, background, appearance, notes ->
                            updateCharacter(
                                name,
                                role,
                                age,
                                gender,
                                personality,
                                background,
                                appearance,
                                notes
                            )
                        }
                    )
                }
            }
        }
    }
}
