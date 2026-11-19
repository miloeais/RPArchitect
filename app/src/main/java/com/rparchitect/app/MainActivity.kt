
package com.rparchitect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*

import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry

import com.rparchitect.app.data.repository.ProjectRepository
import com.rparchitect.app.data.repository.CharacterRepository
import com.rparchitect.app.data.repository.WorldLoreRepository
import com.rparchitect.app.data.repository.WorldEntryRepository

import com.rparchitect.app.ui.screens.HomeScreen
import com.rparchitect.app.ui.screens.ProjectManagerScreen
import com.rparchitect.app.ui.screens.CreateProjectScreen
import com.rparchitect.app.ui.screens.EditProjectScreen
import com.rparchitect.app.ui.screens.WorkspaceScreen

import com.rparchitect.app.ui.screens.CharacterScreen
import com.rparchitect.app.ui.screens.CreateCharacterScreen
import com.rparchitect.app.ui.screens.EditCharacterScreen

import com.rparchitect.app.ui.screens.WorldLoreScreen
import com.rparchitect.app.ui.screens.CreateWorldLoreScreen
import com.rparchitect.app.ui.screens.EditWorldLoreScreen

import com.rparchitect.app.ui.screens.WorldEntryScreen
import com.rparchitect.app.ui.screens.CreateWorldEntryScreen
import com.rparchitect.app.ui.screens.EditWorldEntryScreen

private enum class AppScreen {
    HOME,
    PROJECTS,
    CREATE_PROJECT,
    EDIT_PROJECT,
    WORKSPACE,
    CHARACTERS,
    CREATE_CHARACTER,
    EDIT_CHARACTER,
    WORLD_LORE,
    CREATE_WORLD_LORE,
    EDIT_WORLD_LORE,
    WORLD_ENTRIES,
    CREATE_WORLD_ENTRY,
    EDIT_WORLD_ENTRY
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

    val worldLoreRepository = remember {
        WorldLoreRepository()
    }

    val worldEntryRepository = remember {
        WorldEntryRepository()
    }

    var projects by remember {
        mutableStateOf(projectRepository.getAllProjects())
    }

    var characters by remember {
        mutableStateOf(emptyList<RPCharacter>())
    }

    var worldLores by remember {
        mutableStateOf(emptyList<RPWorldLore>())
    }

    var worldEntries by remember {
        mutableStateOf(emptyList<RPWorldEntry>())
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

    var selectedWorldLore by remember {
        mutableStateOf<RPWorldLore?>(null)
    }

    var selectedWorldEntry by remember {
        mutableStateOf<RPWorldEntry?>(null)
    }

    fun refreshProjects() {
        projects = projectRepository.getAllProjects()
    }

    fun refreshCharacters(projectId: Long) {
        characters = characterRepository.getCharactersByProject(projectId)
    }

    fun refreshWorldLores(projectId: Long) {
        worldLores = worldLoreRepository.getWorldLoresByProject(projectId)
    }

    fun refreshWorldEntries(projectId: Long) {
        worldEntries = worldEntryRepository.getEntriesByProject(projectId)
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

    fun createWorldLore(
        title: String,
        history: String,
        culture: String,
        worldRules: String,
        notes: String
    ) {
        val project = selectedProject ?: return
        val now = System.currentTimeMillis()

        val lore = RPWorldLore(
            id = now,
            projectId = project.id,
            title = title,
            history = history,
            culture = culture,
            worldRules = worldRules,
            notes = notes,
            createdAt = now,
            updatedAt = now
        )

        worldLoreRepository.addWorldLore(lore)
        refreshWorldLores(project.id)
        currentScreen = AppScreen.WORLD_LORE
    }

    fun updateWorldLore(
        title: String,
        history: String,
        culture: String,
        worldRules: String,
        notes: String
    ) {
        val oldLore = selectedWorldLore ?: return
        val project = selectedProject ?: return

        val updatedLore = oldLore.copy(
            title = title,
            history = history,
            culture = culture,
            worldRules = worldRules,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )

        worldLoreRepository.updateWorldLore(updatedLore)
        selectedWorldLore = updatedLore

        refreshWorldLores(project.id)
        currentScreen = AppScreen.WORLD_LORE
    }

    fun createWorldEntry(
        parentId: Long?,
        name: String,
        type: String,
        description: String,
        history: String,
        government: String,
        leader: String,
        headquarters: String,
        territory: String,
        notes: String
    ) {
        val project = selectedProject ?: return
        val now = System.currentTimeMillis()

        val entry = RPWorldEntry(
            id = now,
            projectId = project.id,
            parentId = parentId,
            name = name,
            type = type,
            description = description,
            history = history,
            government = government,
            leader = leader,
            headquarters = headquarters,
            territory = territory,
            notes = notes,
            createdAt = now,
            updatedAt = now
        )

        worldEntryRepository.addEntry(entry)
        refreshWorldEntries(project.id)
        currentScreen = AppScreen.WORLD_ENTRIES
    }

    fun updateWorldEntry(
        parentId: Long?,
        name: String,
        type: String,
        description: String,
        history: String,
        government: String,
        leader: String,
        headquarters: String,
        territory: String,
        notes: String
    ) {
        val oldEntry = selectedWorldEntry ?: return
        val project = selectedProject ?: return

        val updatedEntry = oldEntry.copy(
            parentId = parentId,
            name = name,
            type = type,
            description = description,
            history = history,
            government = government,
            leader = leader,
            headquarters = headquarters,
            territory = territory,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )

        worldEntryRepository.updateEntry(updatedEntry)
        selectedWorldEntry = updatedEntry

        refreshWorldEntries(project.id)
        currentScreen = AppScreen.WORLD_ENTRIES
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
                        refreshWorldLores(project.id)
                        refreshWorldEntries(project.id)
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
                        onWorldBuildingClick = {
                            refreshWorldLores(project.id)
                            currentScreen = AppScreen.WORLD_LORE
                        },
                        onLocationsFactionsClick = {
                            refreshWorldEntries(project.id)
                            currentScreen = AppScreen.WORLD_ENTRIES
                        }
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
                    onCreateClick = {
                        name, role, age, gender,
                        personality, background, appearance, notes ->

                        createCharacter(
                            name, role, age, gender,
                            personality, background, appearance, notes
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
                        onSaveClick = {
                            name, role, age, gender,
                            personality, background, appearance, notes ->

                            updateCharacter(
                                name, role, age, gender,
                                personality, background, appearance, notes
                            )
                        }
                    )
                }
            }

            AppScreen.WORLD_LORE -> {
                val project = selectedProject

                if (project != null) {
                    WorldLoreScreen(
                        projectName = project.name,
                        worldLores = worldLores,
                        onBackClick = {
                            currentScreen = AppScreen.WORKSPACE
                        },
                        onCreateLore = {
                            currentScreen = AppScreen.CREATE_WORLD_LORE
                        },
                        onEditLore = { lore ->
                            selectedWorldLore = lore
                            currentScreen = AppScreen.EDIT_WORLD_LORE
                        },
                        onDeleteLore = { loreId ->
                            worldLoreRepository.deleteWorldLore(loreId)
                            refreshWorldLores(project.id)
                        }
                    )
                }
            }

            AppScreen.CREATE_WORLD_LORE -> {
                CreateWorldLoreScreen(
                    onBackClick = {
                        currentScreen = AppScreen.WORLD_LORE
                    },
                    onCreateClick = { title, history, culture, rules, notes ->
                        createWorldLore(
                            title, history, culture, rules, notes
                        )
                    }
                )
            }

            AppScreen.EDIT_WORLD_LORE -> {
                val lore = selectedWorldLore

                if (lore != null) {
                    EditWorldLoreScreen(
                        worldLore = lore,
                        onBackClick = {
                            currentScreen = AppScreen.WORLD_LORE
                        },
                        onSaveClick = { title, history, culture, rules, notes ->
                            updateWorldLore(
                                title, history, culture, rules, notes
                            )
                        }
                    )
                }
            }

            AppScreen.WORLD_ENTRIES -> {
                val project = selectedProject

                if (project != null) {
                    WorldEntryScreen(
                        projectName = project.name,
                        entries = worldEntries,
                        onBackClick = {
                            currentScreen = AppScreen.WORKSPACE
                        },
                        onCreateEntry = {
                            currentScreen = AppScreen.CREATE_WORLD_ENTRY
                        },
                        onEditEntry = { entry ->
                            selectedWorldEntry = entry
                            currentScreen = AppScreen.EDIT_WORLD_ENTRY
                        },
                        onDeleteEntry = { entryId ->
                            worldEntryRepository.deleteEntry(entryId)
                            refreshWorldEntries(project.id)
                        }
                    )
                }
            }

            AppScreen.CREATE_WORLD_ENTRY -> {
                CreateWorldEntryScreen(
                    entries = worldEntries,
                    onBackClick = {
                        currentScreen = AppScreen.WORLD_ENTRIES
                    },
                    onCreateClick = {
                        parentId, name, type, description,
                        history, government, leader, headquarters,
                        territory, notes ->

                        createWorldEntry(
                            parentId,
                            name,
                            type,
                            description,
                            history,
                            government,
                            leader,
                            headquarters,
                            territory,
                            notes
                        )
                    }
                )
            }

            AppScreen.EDIT_WORLD_ENTRY -> {
                val entry = selectedWorldEntry

                if (entry != null) {
                    EditWorldEntryScreen(
                        entry = entry,
                        entries = worldEntries,
                        onBackClick = {
                            currentScreen = AppScreen.WORLD_ENTRIES
                        },
                        onUpdateClick = {
                            parentId, name, type, description,
                            history, government, leader, headquarters,
                            territory, notes ->

                            updateWorldEntry(
                                parentId,
                                name,
                                type,
                                description,
                                history,
                                government,
                                leader,
                                headquarters,
                                territory,
                                notes
                            )
                        }
                    )
                }
            }
        }
    }
}
