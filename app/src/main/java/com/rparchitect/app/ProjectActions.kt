
package com.rparchitect.app

import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.repository.ProjectRepository

object ProjectActions {

    suspend fun createProject(
        repository: ProjectRepository,
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
    }

    suspend fun updateProject(
        repository: ProjectRepository,
        oldProject: RPProject,
        name: String,
        description: String,
        genre: String
    ): RPProject {
        val updatedProject = oldProject.copy(
            name = name,
            description = description,
            genre = genre,
            updatedAt = System.currentTimeMillis()
        )

        repository.updateProject(updatedProject)

        return updatedProject
    }
}
