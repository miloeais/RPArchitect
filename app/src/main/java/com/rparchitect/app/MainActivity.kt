package com.rparchitect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.repository.ProjectRepository
import com.rparchitect.app.ui.screens.HomeScreen
import com.rparchitect.app.ui.screens.ProjectManagerScreen
import com.rparchitect.app.ui.screens.CreateProjectScreen
import com.rparchitect.app.ui.screens.EditProjectScreen

private enum class AppScreen {
    HOME,
    PROJECTS,
    CREATE_PROJECT,
    EDIT_PROJECT
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

    val repository = remember {
        ProjectRepository()
    }

    var projects by remember {
        mutableStateOf(repository.getAllProjects())
    }

    var currentScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    var selectedProject by remember {
        mutableStateOf<RPProject?>(null)
    }

    fun refreshProjects() {
        projects = repository.getAllProjects()
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

        repository.addProject(project)
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

        repository.updateProject(updatedProject)
        refreshProjects()
        selectedProject = updatedProject
        currentScreen = AppScreen.PROJECTS
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
                     currentScreen = AppScreen.EDIT_PROJECT
                     },
                 onDeleteProject = { projectId ->
                     repository.deleteProject(projectId)
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
        }
    }
}
