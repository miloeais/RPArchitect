
package com.rparchitect.app.data.template

import com.rparchitect.app.data.model.RPTemplate
import com.rparchitect.app.data.model.RPTemplateField
import com.rparchitect.app.data.model.TemplateFieldType

object PresetLibrary {

    val presets = listOf(

        RPTemplate(
            id = "character_bible",
            name = "Character Bible",
            description = "Build detailed character profiles.",
            fields = listOf(
                RPTemplateField("name", "Character Name", TemplateFieldType.TEXT, true),
                RPTemplateField("role", "Role", TemplateFieldType.CATEGORY),
                RPTemplateField("age", "Age", TemplateFieldType.NUMBER),
                RPTemplateField("gender", "Gender", TemplateFieldType.CATEGORY),
                RPTemplateField("appearance", "Appearance", TemplateFieldType.LONG_TEXT),
                RPTemplateField("personality", "Personality", TemplateFieldType.LONG_TEXT),
                RPTemplateField("background", "Background", TemplateFieldType.LONG_TEXT),
                RPTemplateField("abilities", "Skills / Abilities", TemplateFieldType.LONG_TEXT),
                RPTemplateField("relationships", "Relationships", TemplateFieldType.LONG_TEXT),
                RPTemplateField("notes", "Additional Notes", TemplateFieldType.LONG_TEXT)
            )
        ),

        RPTemplate(
            id = "world_lore",
            name = "World Lore",
            description = "Document the history, culture and rules of a world.",
            fields = listOf(
                RPTemplateField("title", "Title", TemplateFieldType.TEXT, true),
                RPTemplateField("history", "History", TemplateFieldType.LONG_TEXT),
                RPTemplateField("geography", "Geography", TemplateFieldType.LONG_TEXT),
                RPTemplateField("culture", "Culture", TemplateFieldType.LONG_TEXT),
                RPTemplateField("religion", "Religion / Beliefs", TemplateFieldType.LONG_TEXT),
                RPTemplateField("technology", "Technology", TemplateFieldType.LONG_TEXT),
                RPTemplateField("world_rules", "World Rules", TemplateFieldType.LONG_TEXT),
                RPTemplateField("notes", "Additional Notes", TemplateFieldType.LONG_TEXT)
            )
        ),

        RPTemplate(
            id = "relationship_bible",
            name = "Relationship Bible",
            description = "Record connections and dynamics between characters.",
            fields = listOf(
                RPTemplateField("title", "Relationship Title", TemplateFieldType.TEXT, true),
                RPTemplateField("character_a", "Character A", TemplateFieldType.TEXT, true),
                RPTemplateField("character_b", "Character B", TemplateFieldType.TEXT, true),
                RPTemplateField("relationship_type", "Relationship Type", TemplateFieldType.CATEGORY),
                RPTemplateField("history", "Relationship History", TemplateFieldType.LONG_TEXT),
                RPTemplateField("dynamics", "Relationship Dynamics", TemplateFieldType.LONG_TEXT),
                RPTemplateField("conflicts", "Conflicts", TemplateFieldType.LONG_TEXT),
                RPTemplateField("notes", "Additional Notes", TemplateFieldType.LONG_TEXT)
            )
        ),

        RPTemplate(
            id = "location_bible",
            name = "Location Bible",
            description = "Document locations, organizations and factions.",
            fields = listOf(
                RPTemplateField("name", "Name", TemplateFieldType.TEXT, true),
                RPTemplateField("type", "Location / Faction Type", TemplateFieldType.CATEGORY),
                RPTemplateField("parent", "Parent Location / Organization", TemplateFieldType.TEXT),
                RPTemplateField("description", "Description", TemplateFieldType.LONG_TEXT),
                RPTemplateField("history", "History", TemplateFieldType.LONG_TEXT),
                RPTemplateField("government", "Government / Structure", TemplateFieldType.LONG_TEXT),
                RPTemplateField("leader", "Leader", TemplateFieldType.TEXT),
                RPTemplateField("headquarters", "Headquarters", TemplateFieldType.TEXT),
                RPTemplateField("territory", "Territory / Influence", TemplateFieldType.LONG_TEXT),
                RPTemplateField("notes", "Additional Notes", TemplateFieldType.LONG_TEXT)
            )
        ),

        RPTemplate(
            id = "rp_rules",
            name = "RP Rules",
            description = "Define rules and boundaries for roleplay.",
            fields = listOf(
                RPTemplateField("title", "Rule Set Title", TemplateFieldType.TEXT, true),
                RPTemplateField("general", "General Rules", TemplateFieldType.LONG_TEXT),
                RPTemplateField("character", "Character Rules", TemplateFieldType.LONG_TEXT),
                RPTemplateField("combat", "Combat Rules", TemplateFieldType.LONG_TEXT),
                RPTemplateField("interaction", "Interaction Rules", TemplateFieldType.LONG_TEXT),
                RPTemplateField("boundaries", "Content Boundaries", TemplateFieldType.LONG_TEXT),
                RPTemplateField("notes", "Additional Notes", TemplateFieldType.LONG_TEXT)
            )
        ),

        RPTemplate(
            id = "story_arc",
            name = "Story Arc",
            description = "Plan storylines, conflicts and narrative developments.",
            fields = listOf(
                RPTemplateField("title", "Story Arc Title", TemplateFieldType.TEXT, true),
                RPTemplateField("genre", "Genre", TemplateFieldType.CATEGORY),
                RPTemplateField("status", "Status", TemplateFieldType.CATEGORY),
                RPTemplateField("start_date", "Start Date", TemplateFieldType.DATE),
                RPTemplateField("end_date", "End Date", TemplateFieldType.DATE),
                RPTemplateField("summary", "Summary", TemplateFieldType.LONG_TEXT),
                RPTemplateField("main_conflict", "Main Conflict", TemplateFieldType.LONG_TEXT),
                RPTemplateField("key_events", "Key Events", TemplateFieldType.LONG_TEXT),
                RPTemplateField("resolution", "Resolution", TemplateFieldType.LONG_TEXT),
                RPTemplateField("notes", "Additional Notes", TemplateFieldType.LONG_TEXT)
            )
        ),

        RPTemplate(
            id = "custom_template",
            name = "Custom Template",
            description = "Create your own template and fields.",
            fields = emptyList(),
            isCustom = true
        )
    )
}
