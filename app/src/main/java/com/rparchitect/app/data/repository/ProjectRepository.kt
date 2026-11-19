package com.rparchitect.app.data.repository

import com.rparchitect.app.data.model.RPProject

class ProjectRepository {

    private val projects = mutableListOf<RPProject>()

    fun getAllProjects(): List<RPProject> {
        return projects.toList()
    }

    fun addProject(project: RPProject) {
        projects.add(project)
    }

    fun updateProject(project: RPProject) {
        val index = projects.indexOfFirst {
            it.id == project.id
        }

        if (index != -1) {
            projects[index] = project
        }
    }

    fun deleteProject(projectId: Long) {
        projects.removeAll {
            it.id == projectId
        }
    }
}
