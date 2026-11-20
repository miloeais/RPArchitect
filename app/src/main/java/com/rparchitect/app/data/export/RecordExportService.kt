
package com.rparchitect.app.data.export

import android.content.Context
import android.net.Uri
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry

object RecordExportService {

    fun exportCharacter(
        context: Context,
        folderUri: Uri,
        character: RPCharacter
    ) {
        MarkdownFolderWriter.write(
            context = context,
            folderUri = folderUri,
            fileName = "${MarkdownFolderWriter.sanitizeFileName("Character_${character.id}_${character.name}")}.md",
            content = RPMarkdownExporter.generateCharacter(character)
        )
    }

    fun exportWorldLore(
        context: Context,
        folderUri: Uri,
        lore: RPWorldLore
    ) {
        MarkdownFolderWriter.write(
            context = context,
            folderUri = folderUri,
            fileName = "${MarkdownFolderWriter.sanitizeFileName("WorldLore_${lore.id}_${lore.title}")}.md",
            content = RPMarkdownExporter.generateWorldLore(lore)
        )
    }

    fun exportWorldEntry(
        context: Context,
        folderUri: Uri,
        entry: RPWorldEntry,
        allEntries: List<RPWorldEntry>,
        includeChildren: Boolean
    ) {
        val exportType = if (includeChildren) {
            "Combined"
        } else if (entry.parentId == null) {
            "Root"
        } else {
            "Sub"
        }

        MarkdownFolderWriter.write(
            context = context,
            folderUri = folderUri,
            fileName = "${MarkdownFolderWriter.sanitizeFileName("Location_${exportType}_${entry.id}_${entry.name}")}.md",
            content = RPMarkdownExporter.generateWorldEntry(
                entry = entry,
                includeChildren = includeChildren,
                allEntries = allEntries
            )
        )
    }
}
