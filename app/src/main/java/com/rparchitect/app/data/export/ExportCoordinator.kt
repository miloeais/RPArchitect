
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
        loadProjectDocuments: suspend (Long) -> List<RPDocument> = {
            emptyList()
        },
        loadDocumentFields: suspend (RPDocument) -> List<RPDocumentField> = {
            emptyList()
        }
    ): Int {

        return when (request) {

            is ExportRequest.CharacterRecord -> {
                RecordExportService.exportCharacter(
                    context,
                    folderUri,
                    request.character
                )
                1
            }

            is ExportRequest.CharacterBulk -> {
                request.characters.forEach { character ->
                    RecordExportService.exportCharacter(
                        context,
                        folderUri,
                        character
                    )
                }
                request.characters.size
            }

            is ExportRequest.WorldLoreRecord -> {
                RecordExportService.exportWorldLore(
                    context,
                    folderUri,
                    request.lore
                )
                1
            }

            is ExportRequest.WorldLoreBulk -> {
                request.worldLores.forEach { lore ->
                    RecordExportService.exportWorldLore(
                        context,
                        folderUri,
                        lore
                    )
                }
                request.worldLores.size
            }

            is ExportRequest.WorldEntryRecord -> {
                RecordExportService.exportWorldEntry(
                    context,
                    folderUri,
                    request.entry,
                    request.allEntries,
                    request.includeChildren
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
                    loadProjectDocuments = loadProjectDocuments,
                    loadDocumentFields = loadDocumentFields
                )
            }
        }
    }
}
