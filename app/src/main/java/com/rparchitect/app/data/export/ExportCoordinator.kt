
package com.rparchitect.app.data.export

import android.content.Context
import android.net.Uri
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.data.model.RPDocumentField

object ExportCoordinator {

    suspend fun execute(
        context: Context,
        folderUri: Uri,
        request: ExportRequest,
        characters: List<RPCharacter> = emptyList(),
        worldLores: List<RPWorldLore> = emptyList(),
        worldEntries: List<RPWorldEntry> = emptyList(),
        documents: List<RPDocument> = emptyList(),
        loadDocumentFields: suspend (RPDocument) -> List<RPDocumentField> = {
            emptyList()
        }
    ): Int {

        return when (request) {

            is ExportRequest.CharacterRecord -> {
                RecordExportService.exportCharacter(
                    context = context,
                    folderUri = folderUri,
                    character = request.character
                )
                1
            }

            is ExportRequest.WorldLoreRecord -> {
                RecordExportService.exportWorldLore(
                    context = context,
                    folderUri = folderUri,
                    lore = request.lore
                )
                1
            }

            is ExportRequest.WorldEntryRecord -> {
                RecordExportService.exportWorldEntry(
                    context = context,
                    folderUri = folderUri,
                    entry = request.entry,
                    allEntries = request.allEntries,
                    includeChildren = request.includeChildren
                )
                1
            }

            is ExportRequest.Project -> {
                ProjectExportService.exportProject(
                    context = context,
                    folderUri = folderUri,
                    project = request.project,
                    characters = characters.filter {
                        it.projectId == request.project.id
                    },
                    worldLores = worldLores.filter {
                        it.projectId == request.project.id
                    },
                    worldEntries = worldEntries.filter {
                        it.projectId == request.project.id
                    },
                    documents = documents.filter {
                        it.projectId == request.project.id
                    },
                    loadDocumentFields = loadDocumentFields
                )
            }
        }
    }
}
