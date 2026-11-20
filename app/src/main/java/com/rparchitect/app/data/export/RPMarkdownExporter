
package com.rparchitect.app.data.export

import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.data.model.RPDocumentField

object RPMarkdownExporter {

    fun generateProjectHeader(project: RPProject): String {
        return buildString {
            appendLine("# ${project.name}")
            appendLine()
            appendLine("**Genre:** ${project.genre.ifBlank { "Not specified" }}")
            appendLine()
            appendLine("## Project Description")
            appendLine()
            appendLine(project.description.ifBlank { "_No description_" })
            appendLine()
            appendLine("---")
        }.trimEnd()
    }

    fun generateCharacter(character: RPCharacter): String {
        return buildString {
            appendLine("# ${character.name}")
            appendLine()
            appendLine("**Role:** ${character.role.ifBlank { "Not specified" }}")
            appendLine("**Age:** ${character.age.ifBlank { "Not specified" }}")
            appendLine("**Gender:** ${character.gender.ifBlank { "Not specified" }}")
            appendLine()
            appendSection("Personality", character.personality)
            appendSection("Background", character.background)
            appendSection("Appearance", character.appearance)
            appendSection("Notes", character.notes)
        }.trimEnd()
    }

    fun generateWorldLore(lore: RPWorldLore): String {
        return buildString {
            appendLine("# ${lore.title}")
            appendLine()
            appendSection("History", lore.history)
            appendSection("Culture", lore.culture)
            appendSection("World Rules", lore.worldRules)
            appendSection("Notes", lore.notes)
        }.trimEnd()
    }

    fun generateWorldEntry(
        entry: RPWorldEntry,
        includeChildren: Boolean = false,
        allEntries: List<RPWorldEntry> = emptyList()
    ): String {
        return buildString {
            appendLine("# ${entry.name}")
            appendLine()
            appendLine("**Type:** ${entry.type.ifBlank { "Not specified" }}")
            appendLine()
            appendSection("Description", entry.description)
            appendSection("History", entry.history)
            appendSection("Government", entry.government)
            appendSection("Leader", entry.leader)
            appendSection("Headquarters", entry.headquarters)
            appendSection("Territory", entry.territory)
            appendSection("Notes", entry.notes)

            if (includeChildren) {
                val visited = mutableSetOf<Long>()
                visited.add(entry.id)

                fun appendChildren(parentId: Long, depth: Int) {
                    val children = allEntries.filter {
                        it.parentId == parentId
                    }

                    children.forEach { child ->
                        if (visited.add(child.id)) {
                            appendLine()
                            appendLine("${"#".repeat((depth + 2).coerceAtMost(6))} ${child.name}")
                            appendLine()
                            appendLine("**Type:** ${child.type.ifBlank { "Not specified" }}")
                            appendSection("Description", child.description)
                            appendSection("History", child.history)
                            appendSection("Government", child.government)
                            appendSection("Leader", child.leader)
                            appendSection("Headquarters", child.headquarters)
                            appendSection("Territory", child.territory)
                            appendSection("Notes", child.notes)

                            appendChildren(child.id, depth + 1)
                        }
                    }
                }

                appendChildren(entry.id, 1)
            }
        }.trimEnd()
    }

    fun generateDocument(
        document: RPDocument,
        fields: List<RPDocumentField>
    ): String {
        return buildString {
            appendLine("# ${document.title}")
            appendLine()
            appendLine("**Template:** ${document.templateName}")
            appendLine()
            appendLine("---")
            appendLine()

            fields.sortedBy { it.sortOrder }.forEach { field ->
                appendLine("## ${field.label}")
                appendLine()

                val value = field.value.trim()

                appendLine(
                    if (value.isEmpty()) "_Not filled_" else value
                )

                appendLine()
            }
        }.trimEnd()
    }

    private fun StringBuilder.appendSection(
        title: String,
        value: String
    ) {
        appendLine("## $title")
        appendLine()

        appendLine(
            value.trim().ifEmpty { "_Not filled_" }
        )

        appendLine()
    }
}
