
package com.rparchitect.app.data.export

import android.content.Context
import android.net.Uri
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.data.model.RPDocumentField

object ProjectExportService {

    suspend fun exportProject(
        context: Context,
        folderUri: Uri,
        project: RPProject,
        characters: List<RPCharacter>,
        worldLores: List<RPWorldLore>,
        worldEntries: List<RPWorldEntry>,
        loadProjectDocuments: suspend (Long) -> List<RPDocument>,
        loadDocumentFields: suspend (RPDocument) -> List<RPDocumentField>
    ): Int {

        var exportedCount = 0

        fun writeFile(name: String, content: String) {
            MarkdownFolderWriter.write(
                context = context,
                folderUri = folderUri,
                fileName = "${MarkdownFolderWriter.sanitizeFileName(name)}.md",
                content = content
            )
            exportedCount++
        }

        writeFile(
            "Project_Info_${project.id}",
            RPMarkdownExporter.generateProjectHeader(project)
        )

        characters.forEach { character ->
            writeFile(
                "Character_${character.id}_${character.name}",
                RPMarkdownExporter.generateCharacter(character)
            )
        }

        worldLores.forEach { lore ->
            writeFile(
                "WorldLore_${lore.id}_${lore.title}",
                RPMarkdownExporter.generateWorldLore(lore)
            )
        }

        val roots = worldEntries.filter { entry ->
            entry.parentId == null ||
                worldEntries.none { it.id == entry.parentId }
        }

        roots.forEach { root ->
            writeFile(
                "Location_Root_${root.id}_${root.name}",
                RPMarkdownExporter.generateWorldEntry(root)
            )

            writeFile(
                "Location_Combined_${root.id}_${root.name}",
                RPMarkdownExporter.generateWorldEntry(
                    entry = root,
                    includeChildren = true,
                    allEntries = worldEntries
                )
            )
        }

        worldEntries
            .filter { it.parentId != null }
            .forEach { subEntry ->
                writeFile(
                    "Location_Sub_${subEntry.id}_${subEntry.name}",
                    RPMarkdownExporter.generateWorldEntry(subEntry)
                )
            }

        val documents = loadProjectDocuments(project.id)

        documents.forEach { document ->
            val fields = loadDocumentFields(document)

            writeFile(
                "Document_${document.id}_${document.title}",
                RPMarkdownExporter.generateDocument(
                    document = document,
                    fields = fields
                )
            )
        }

        return exportedCount
    }
}
