
package com.rparchitect.app.data.export

import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry

sealed class ExportRequest {

    data class CharacterRecord(
        val character: RPCharacter
    ) : ExportRequest()

    data class CharacterBulk(
        val characters: List<RPCharacter>
    ) : ExportRequest()

    data class WorldLoreRecord(
        val lore: RPWorldLore
    ) : ExportRequest()

    data class WorldLoreBulk(
        val worldLores: List<RPWorldLore>
    ) : ExportRequest()

    data class WorldEntryRecord(
        val entry: RPWorldEntry,
        val allEntries: List<RPWorldEntry>,
        val includeChildren: Boolean
    ) : ExportRequest()

    data class Project(
        val project: RPProject
    ) : ExportRequest()
}
