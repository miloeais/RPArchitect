
package com.rparchitect.app.data.repository

import com.rparchitect.app.data.dao.ProjectDao
import com.rparchitect.app.data.model.RPProject
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao
) {

    fun getAllProjects(): Flow<List<RPProject>> {
        return projectDao.getAllProjects()
    }

    suspend fun getProjectById(projectId: Long): RPProject? {
        return projectDao.getProjectById(projectId)
    }

    suspend fun addProject(project: RPProject) {
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: RPProject) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(projectId: Long) {
        projectDao.deleteProjectById(projectId)
    }
}
