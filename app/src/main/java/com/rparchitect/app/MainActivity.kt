
package com.rparchitect.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rparchitect.app.data.database.DatabaseProvider
import com.rparchitect.app.data.export.DocumentMarkdownExporter
import com.rparchitect.app.data.export.DocumentPreviewField
import com.rparchitect.app.data.export.ExportController
import com.rparchitect.app.data.export.ExportRequest
import com.rparchitect.app.data.model.*
import com.rparchitect.app.data.model.RPTemplate
import com.rparchitect.app.data.repository.*
import com.rparchitect.app.data.template.PresetLibrary
import com.rparchitect.app.ui.components.ExportResultDialog
import com.rparchitect.app.ui.screens.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

private val RPArchitectColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF08090B),

    secondary = Color(0xFFA0A2A8),
    onSecondary = Color(0xFF08090B),

    background = Color(0xFF08090B),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF24262B),
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF35383E),
    onSurfaceVariant = Color(0xFFA0A2A8),

    outline = Color(0xFF60636A),
    error = Color(0xFFFF5252)
)

private enum class AppScreen {
    HOME,
    PROJECTS,
    CREATE_PROJECT,
    EDIT_PROJECT,
    WORKSPACE,
    CHARACTERS,
    CREATE_CHARACTER,
    EDIT_CHARACTER,
    WORLD_LORE,
    CREATE_WORLD_LORE,
    EDIT_WORLD_LORE,
    WORLD_ENTRIES,
    CREATE_WORLD_ENTRY,
    EDIT_WORLD_ENTRY,
    TEMPLATE_LIBRARY,
    TEMPLATE_DETAIL,
    CUSTOM_TEMPLATE_BUILDER,
    DOCUMENTS,
    CREATE_DOCUMENT,
    DOCUMENT_EDITOR,
    DOCUMENT_PREVIEW
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RPArchitectApp()
        }
    }
}

@Composable
fun RPArchitectApp() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var markdownToExport by remember {
        mutableStateOf("")
    }

    var exportDialogMessage by remember {
        mutableStateOf<String?>(null)
    }

    var exportDialogIsError by remember {
        mutableStateOf(false)
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/markdown")
    ) { uri ->

        if (uri != null) {

            runCatching {
                val outputStream = context.contentResolver
                    .openOutputStream(uri)
                    ?: error("Unable to open file")

                outputStream.bufferedWriter().use {
                    it.write(markdownToExport)
                }

            }.onSuccess {
                Toast.makeText(
                    context,
                    "Markdown exported successfully",
                    Toast.LENGTH_SHORT
                ).show()

            }.onFailure {
                Toast.makeText(
                    context,
                    "Export failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val database = remember {
        DatabaseProvider.getDatabase(context)
    }

    val projectRepository = remember(database) {
        ProjectRepository(database.projectDao())
    }

    val characterRepository = remember(database) {
        CharacterRepository(database.characterDao())
    }

    val worldLoreRepository = remember(database) {
        WorldLoreRepository(database.worldLoreDao())
    }

    val worldEntryRepository = remember(database) {
        WorldEntryRepository(database.worldEntryDao())
    }

    val customTemplateRepository = remember(database) {
        CustomTemplateRepository(database.customTemplateDao())
    }

    val documentRepository = remember(database) {
        DocumentRepository(database.documentDao())
    }

    val projects by projectRepository
        .getAllProjects()
        .collectAsState(initial = emptyList())

    val allCharacters by characterRepository
        .getAllCharacters()
        .collectAsState(initial = emptyList())

    val allWorldLores by worldLoreRepository
        .getAllWorldLores()
        .collectAsState(initial = emptyList())

    val allWorldEntries by worldEntryRepository
        .getAllEntries()
        .collectAsState(initial = emptyList())

    val customTemplates by customTemplateRepository
        .getAllTemplates()
        .collectAsState(initial = emptyList())

    var currentScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    var selectedProject by remember {
        mutableStateOf<RPProject?>(null)
    }

    var selectedCharacter by remember {
        mutableStateOf<RPCharacter?>(null)
    }

    var selectedWorldLore by remember {
        mutableStateOf<RPWorldLore?>(null)
    }

    var selectedWorldEntry by remember {
        mutableStateOf<RPWorldEntry?>(null)
    }

    var selectedTemplate by remember {
        mutableStateOf<RPTemplate?>(null)
    }

    var selectedCustomTemplate by remember {
        mutableStateOf<RPCustomTemplate?>(null)
    }

    var selectedCustomFields by remember {
        mutableStateOf<List<CustomTemplateFieldData>>(emptyList())
    }

    // DOCUMENT STATE

    var selectedDocument by remember {
        mutableStateOf<RPDocument?>(null)
    }

    var documentTemplate by remember {
        mutableStateOf<RPTemplate?>(null)
    }

    var documentFields by remember {
        mutableStateOf<List<RPDocumentField>>(emptyList())
    }

    var documentTitle by remember {
        mutableStateOf("")
    }

    var newDocumentTitle by remember {
        mutableStateOf("")
    }

    var newDocumentTemplate by remember {
        mutableStateOf<RPTemplate?>(null)
    }

    var previewFields by remember {
        mutableStateOf<List<DocumentPreviewField>>(emptyList())
    }

    var editorValues by remember {
        mutableStateOf<Map<String, String>>(emptyMap())
    }

    // PROJECT DATA

    val characters = allCharacters.filter {
        it.projectId == selectedProject?.id
    }

    val worldLores = allWorldLores.filter {
        it.projectId == selectedProject?.id
    }

    val worldEntries = allWorldEntries.filter {
        it.projectId == selectedProject?.id
    }

    val documentFlow = remember(selectedProject?.id) {
        selectedProject?.id?.let {
            documentRepository.getDocumentsByProject(it)
        } ?: flowOf<List<RPDocument>>(emptyList())
    }

    val documents by documentFlow.collectAsState(
        initial = emptyList()
    )

    val libraryTemplates = PresetLibrary.presets + customTemplates.map { template ->
        RPTemplate(
            id = "custom:${template.id}",
            name = template.name,
            description = template.description,
            fields = emptyList(),
            isCustom = true
        )
    }

    // PROJECT FUNCTIONS

    fun createProject(
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

        coroutineScope.launch {
            projectRepository.addProject(project)
            currentScreen = AppScreen.PROJECTS
        }
    }

    fun updateProject(
        name: String,
        description: String,
        genre: String
    ) {
        val oldProject = selectedProject ?: return

        val updatedProject = oldProject.copy(
            name = name,
            description = description,
            genre = genre,
            updatedAt = System.currentTimeMillis()
        )

        coroutineScope.launch {
            projectRepository.updateProject(updatedProject)
            selectedProject = updatedProject
            currentScreen = AppScreen.PROJECTS
        }
    }

    // CHARACTER FUNCTIONS

    fun createCharacter(
        name: String,
        role: String,
        age: String,
        gender: String,
        occupation: String,
        personality: String,
        background: String,
        appearance: String,
        relationship: String,
        notes: String
    ) {
        val project = selectedProject ?: return
        val now = System.currentTimeMillis()

        val character = RPCharacter(
            id = now,
            projectId = project.id,
            name = name,
            role = role,
            age = age,
            gender = gender,
            occupation = occupation,
            personality = personality,
            background = background,
            appearance = appearance,
            relationship = relationship,
            notes = notes,
            createdAt = now,
            updatedAt = now
        )

        coroutineScope.launch {
            characterRepository.addCharacter(character)
            currentScreen = AppScreen.CHARACTERS
        }
    }

    fun updateCharacter(
        name: String,
        role: String,
        age: String,
        gender: String,
        occupation: String,
        personality: String,
        background: String,
        appearance: String,
        relationship: String,
        notes: String
    ) {
        val oldCharacter = selectedCharacter ?: return

        val updatedCharacter = oldCharacter.copy(
            name = name,
            role = role,
            age = age,
            gender = gender,
            occupation = occupation,
            personality = personality,
            background = background,
            appearance = appearance,
            relationship = relationship,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )

        coroutineScope.launch {
            characterRepository.updateCharacter(updatedCharacter)
            selectedCharacter = updatedCharacter
            currentScreen = AppScreen.CHARACTERS
        }
    }

    // WORLD LORE FUNCTIONS

    fun createWorldLore(
        title: String,
        history: String,
        culture: String,
        worldRules: String,
        notes: String
    ) {
        val project = selectedProject ?: return
        val now = System.currentTimeMillis()

        val lore = RPWorldLore(
            id = now,
            projectId = project.id,
            title = title,
            history = history,
            culture = culture,
            worldRules = worldRules,
            notes = notes,
            createdAt = now,
            updatedAt = now
        )

        coroutineScope.launch {
            worldLoreRepository.addWorldLore(lore)
            currentScreen = AppScreen.WORLD_LORE
        }
    }

    fun updateWorldLore(
        title: String,
        history: String,
        culture: String,
        worldRules: String,
        notes: String
    ) {
        val oldLore = selectedWorldLore ?: return

        val updatedLore = oldLore.copy(
            title = title,
            history = history,
            culture = culture,
            worldRules = worldRules,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )

        coroutineScope.launch {
            worldLoreRepository.updateWorldLore(updatedLore)
            selectedWorldLore = updatedLore
            currentScreen = AppScreen.WORLD_LORE
        }
    }

    // WORLD ENTRY FUNCTIONS

    fun createWorldEntry(
        parentId: Long?,
        name: String,
        type: String,
        description: String,
        history: String,
        government: String,
        leader: String,
        headquarters: String,
        territory: String,
        notes: String
    ) {
        val project = selectedProject ?: return
        val now = System.currentTimeMillis()

        val entry = RPWorldEntry(
            id = now,
            projectId = project.id,
            parentId = parentId,
            name = name,
            type = type,
            description = description,
            history = history,
            government = government,
            leader = leader,
            headquarters = headquarters,
            territory = territory,
            notes = notes,
            createdAt = now,
            updatedAt = now
        )

        coroutineScope.launch {
            worldEntryRepository.addEntry(entry)
            currentScreen = AppScreen.WORLD_ENTRIES
        }
    }

    fun updateWorldEntry(
        parentId: Long?,
        name: String,
        type: String,
        description: String,
        history: String,
        government: String,
        leader: String,
        headquarters: String,
        territory: String,
        notes: String
    ) {
        val oldEntry = selectedWorldEntry ?: return

        val updatedEntry = oldEntry.copy(
            parentId = parentId,
            name = name,
            type = type,
            description = description,
            history = history,
            government = government,
            leader = leader,
            headquarters = headquarters,
            territory = territory,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )

        coroutineScope.launch {
            worldEntryRepository.updateEntry(updatedEntry)
            selectedWorldEntry = updatedEntry
            currentScreen = AppScreen.WORLD_ENTRIES
        }
    }

    // DOCUMENT FUNCTIONS

    fun prepareNewDocument(
        title: String,
        template: RPTemplate
    ) {
        coroutineScope.launch {

            var completeTemplate = template

            if (template.id.startsWith("custom:")) {

                val customId = template.id.removePrefix("custom:")

                val record = customTemplateRepository
                    .getTemplateById(customId)

                if (record != null) {

                    val fieldData = customTemplateRepository
                        .getCompleteTemplate(customId)

                    val fields = fieldData.map { data ->

                        RPTemplateField(
                            id = data.field.id,
                            label = data.field.label,
                            type = runCatching {
                                TemplateFieldType.valueOf(data.field.type)
                            }.getOrDefault(TemplateFieldType.TEXT),
                            required = data.field.required,
                            options = data.options
                                .sortedBy { it.sortOrder }
                                .map { it.value }
                        )
                    }

                    completeTemplate = RPTemplate(
                        id = "custom:${record.id}",
                        name = record.name,
                        description = record.description,
                        fields = fields,
                        isCustom = true
                    )
                }
            }

            selectedDocument = null
            documentFields = emptyList()
            documentTitle = title
            documentTemplate = completeTemplate
            editorValues = emptyMap()
            previewFields = emptyList()

            currentScreen = AppScreen.DOCUMENT_EDITOR
        }
    }

    fun openExistingDocument(document: RPDocument) {
        coroutineScope.launch {

            val fields = documentRepository
                .getFieldsByDocument(document.id)

            val templateFields = fields.map { field ->

                RPTemplateField(
                    id = field.fieldId,
                    label = field.label,
                    type = runCatching {
                        TemplateFieldType.valueOf(field.type)
                    }.getOrDefault(TemplateFieldType.TEXT),
                    required = field.required,
                    options = DocumentFieldMapper.decodeOptions(field.options)
                )
            }

            selectedDocument = document
            documentFields = fields
            documentTitle = document.title

            editorValues = fields.associate {
                it.fieldId to it.value
            }

            documentTemplate = RPTemplate(
                id = document.templateId,
                name = document.templateName,
                description = "",
                fields = templateFields,
                isCustom = document.templateId.startsWith("custom:")
            )

            currentScreen = AppScreen.DOCUMENT_EDITOR
        }
    }

    fun saveDocument(values: Map<String, String>) {
        val project = selectedProject ?: return
        val template = documentTemplate ?: return

        val now = System.currentTimeMillis()
        val oldDocument = selectedDocument

        val documentId = oldDocument?.id
            ?: UUID.randomUUID().toString()

        val document = RPDocument(
            id = documentId,
            projectId = project.id,
            templateId = template.id,
            templateName = template.name,
            title = documentTitle,
            createdAt = oldDocument?.createdAt ?: now,
            updatedAt = now
        )

        val fields = if (oldDocument != null) {

            documentFields.map { field ->
                field.copy(
                    value = values[field.fieldId] ?: ""
                )
            }

        } else {

            DocumentFieldMapper.createSnapshot(
                documentId = documentId,
                template = template
            ).map { field ->
                field.copy(
                    value = values[field.fieldId] ?: ""
                )
            }
        }

        coroutineScope.launch {
            documentRepository.saveCompleteDocument(
                document,
                fields
            )

            selectedDocument = null
            documentFields = emptyList()
            documentTemplate = null
            editorValues = emptyMap()
            previewFields = emptyList()

            newDocumentTitle = ""
            newDocumentTemplate = null

            currentScreen = AppScreen.DOCUMENTS
        }
    }

    // EXPORT ENGINE

    ExportController(
        characters = allCharacters,
        worldLores = allWorldLores,
        worldEntries = allWorldEntries,

        loadProjectDocuments = { projectId ->
            documentRepository
                .getDocumentsByProject(projectId)
                .first()
        },

        loadDocumentFields = { document ->
            documentRepository.getFieldsByDocument(document.id)
        },

        onExportCompleted = { count ->
            exportDialogIsError = false
            exportDialogMessage = "$count Markdown files exported successfully."
        },

        onExportError = { message ->
            exportDialogIsError = true
            exportDialogMessage = message
        }

    ) { requestExport ->

        MaterialTheme(
            colorScheme = RPArchitectColorScheme
        ) {

            when (currentScreen) {

                AppScreen.HOME -> {
                    HomeScreen(
                        onProjectsClick = {
                            currentScreen = AppScreen.PROJECTS
                        },
                        onTemplatesClick = {
                            currentScreen = AppScreen.TEMPLATE_LIBRARY
                        },
                        onSettingsClick = {},
                        onRecentProjectsClick = {
                            currentScreen = AppScreen.PROJECTS
                        }
                    )
                }

                AppScreen.PROJECTS -> {
                    ProjectManagerScreen(
                        projects = projects,
                        onBackClick = {
                            currentScreen = AppScreen.HOME
                        },
                        onCreateProject = {
                            currentScreen = AppScreen.CREATE_PROJECT
                        },
                        onProjectClick = { project ->
                            selectedProject = project
                            currentScreen = AppScreen.WORKSPACE
                        },
                        onEditProject = { project ->
                            selectedProject = project
                            currentScreen = AppScreen.EDIT_PROJECT
                        },
                        onDeleteProject = { projectId ->
                            coroutineScope.launch {
                                projectRepository.deleteProject(projectId)

                                if (selectedProject?.id == projectId) {
                                    selectedProject = null
                                }
                            }
                        },
                        onExportProject = { project ->
                            requestExport(
                                ExportRequest.Project(project)
                            )
                        }
                    )
                }

                AppScreen.CREATE_PROJECT -> {
                    CreateProjectScreen(
                        onBackClick = {
                            currentScreen = AppScreen.PROJECTS
                        },
                        onCreateClick = { name, description, genre ->
                            createProject(name, description, genre)
                        }
                    )
                }

                AppScreen.EDIT_PROJECT -> {
                    val project = selectedProject

                    if (project != null) {
                        EditProjectScreen(
                            project = project,
                            onBackClick = {
                                currentScreen = AppScreen.PROJECTS
                            },
                            onSaveClick = { name, description, genre ->
                                updateProject(name, description, genre)
                            }
                        )
                    }
                }

                AppScreen.WORKSPACE -> {
                    val project = selectedProject

                    if (project != null) {
                        WorkspaceScreen(
                            project = project,
                            onBackClick = {
                                currentScreen = AppScreen.PROJECTS
                            },
                            onCharactersClick = {
                                currentScreen = AppScreen.CHARACTERS
                            },
                            onWorldBuildingClick = {
                                currentScreen = AppScreen.WORLD_LORE
                            },
                            onLocationsFactionsClick = {
                                currentScreen = AppScreen.WORLD_ENTRIES
                            },
                            onDocumentsClick = {
                                currentScreen = AppScreen.DOCUMENTS
                            }
                        )
                    }
                }

                AppScreen.CHARACTERS -> {
                    val project = selectedProject

                    if (project != null) {
                        CharacterScreen(
                            projectName = project.name,
                            characters = characters,
                            onBackClick = {
                                currentScreen = AppScreen.WORKSPACE
                            },
                            onCreateCharacter = {
                                currentScreen = AppScreen.CREATE_CHARACTER
                            },
                            onEditCharacter = { character ->
                                selectedCharacter = character
                                currentScreen = AppScreen.EDIT_CHARACTER
                            },
                            onDeleteCharacter = { characterId ->
                                coroutineScope.launch {
                                    characterRepository.deleteCharacter(characterId)
                                }
                            },
                            onExportCharacter = { character ->
                                requestExport(
                                    ExportRequest.CharacterRecord(character)
                                )
                            },
                            onExportAllCharacters = {
                                requestExport(
                                    ExportRequest.CharacterBulk(characters)
                                )
                            }
                        )
                    }
                }

                AppScreen.CREATE_CHARACTER -> {
                    CreateCharacterScreen(
                        onBackClick = {
                            currentScreen = AppScreen.CHARACTERS
                        },
                        onCreateClick = {
                            name, role, age, gender, occupation,
                            personality, background, appearance, relationship, notes ->

                            createCharacter(
                                name, role, age, gender, occupation,
                                personality, background, appearance, relationship, notes
                            )
                        }
                    )
                }

                AppScreen.EDIT_CHARACTER -> {
                    val character = selectedCharacter

                    if (character != null) {
                        EditCharacterScreen(
                            character = character,
                            onBackClick = {
                                currentScreen = AppScreen.CHARACTERS
                            },
                            onSaveClick = {
                                name, role, age, gender, occupation,
                                personality, background, appearance, relationship, notes ->

                                updateCharacter(
                                    name, role, age, gender, occupation,
                                    personality, background, appearance, relationship, notes
                                )
                            }
                        )
                    }
                }

                AppScreen.WORLD_LORE -> {
                    val project = selectedProject

                    if (project != null) {
                        WorldLoreScreen(
                            projectName = project.name,
                            worldLores = worldLores,
                            onBackClick = {
                                currentScreen = AppScreen.WORKSPACE
                            },
                            onCreateLore = {
                                currentScreen = AppScreen.CREATE_WORLD_LORE
                            },
                            onEditLore = { lore ->
                                selectedWorldLore = lore
                                currentScreen = AppScreen.EDIT_WORLD_LORE
                            },
                            onDeleteLore = { loreId ->
                                coroutineScope.launch {
                                    worldLoreRepository.deleteWorldLore(loreId)
                                }
                            },
                            onExportLore = { lore ->
                                requestExport(
                                    ExportRequest.WorldLoreRecord(lore)
                                )
                            },
                            onExportAllLores = {
                                requestExport(
                                    ExportRequest.WorldLoreBulk(worldLores)
                                )
                            }
                        )
                    }
                }

                AppScreen.CREATE_WORLD_LORE -> {
                    CreateWorldLoreScreen(
                        onBackClick = {
                            currentScreen = AppScreen.WORLD_LORE
                        },
                        onCreateClick = { title, history, culture, rules, notes ->
                            createWorldLore(
                                title, history, culture, rules, notes
                            )
                        }
                    )
                }

                AppScreen.EDIT_WORLD_LORE -> {
                    val lore = selectedWorldLore

                    if (lore != null) {
                        EditWorldLoreScreen(
                            worldLore = lore,
                            onBackClick = {
                                currentScreen = AppScreen.WORLD_LORE
                            },
                            onSaveClick = { title, history, culture, rules, notes ->
                                updateWorldLore(
                                    title, history, culture, rules, notes
                                )
                            }
                        )
                    }
                }

                AppScreen.WORLD_ENTRIES -> {
                    val project = selectedProject

                    if (project != null) {
                        WorldEntryScreen(
                            projectName = project.name,
                            entries = worldEntries,
                            onBackClick = {
                                currentScreen = AppScreen.WORKSPACE
                            },
                            onCreateEntry = {
                                currentScreen = AppScreen.CREATE_WORLD_ENTRY
                            },
                            onEditEntry = { entry ->
                                selectedWorldEntry = entry
                                currentScreen = AppScreen.EDIT_WORLD_ENTRY
                            },
                            onDeleteEntry = { entryId ->
                                coroutineScope.launch {
                                    worldEntryRepository.deleteEntry(entryId)
                                }
                            },
                            onExportEntry = { entry, includeChildren ->
                                requestExport(
                                    ExportRequest.WorldEntryRecord(
                                        entry = entry,
                                        allEntries = worldEntries,
                                        includeChildren = includeChildren
                                    )
                                )
                            }
                        )
                    }
                }

                AppScreen.CREATE_WORLD_ENTRY -> {
                    CreateWorldEntryScreen(
                        entries = worldEntries,
                        onBackClick = {
                            currentScreen = AppScreen.WORLD_ENTRIES
                        },
                        onCreateClick = {
                            parentId, name, type, description,
                            history, government, leader, headquarters,
                            territory, notes ->

                            createWorldEntry(
                                parentId,
                                name,
                                type,
                                description,
                                history,
                                government,
                                leader,
                                headquarters,
                                territory,
                                notes
                            )
                        }
                    )
                }

                AppScreen.EDIT_WORLD_ENTRY -> {
                    val entry = selectedWorldEntry

                    if (entry != null) {
                        EditWorldEntryScreen(
                            entry = entry,
                            entries = worldEntries,
                            onBackClick = {
                                currentScreen = AppScreen.WORLD_ENTRIES
                            },
                            onUpdateClick = {
                                parentId, name, type, description,
                                history, government, leader, headquarters,
                                territory, notes ->

                                updateWorldEntry(
                                    parentId,
                                    name,
                                    type,
                                    description,
                                    history,
                                    government,
                                    leader,
                                    headquarters,
                                    territory,
                                    notes
                                )
                            }
                        )
                    }
                }

                // TEMPLATE LIBRARY

                AppScreen.TEMPLATE_LIBRARY -> {
                    TemplateLibraryScreen(
                        templates = libraryTemplates,
                        onBackClick = {
                            currentScreen = AppScreen.HOME
                        },
                        onTemplateClick = { template ->

                            when {
                                template.id.startsWith("custom:") -> {
                                    val customId = template.id.removePrefix("custom:")

                                    coroutineScope.launch {
                                        val record = customTemplateRepository
                                            .getTemplateById(customId)

                                        if (record != null) {
                                            val fieldData = customTemplateRepository
                                                .getCompleteTemplate(customId)

                                            val templateFields = fieldData.map { data ->
                                                RPTemplateField(
                                                    id = data.field.id,
                                                    label = data.field.label,
                                                    type = runCatching {
                                                        TemplateFieldType.valueOf(
                                                            data.field.type
                                                        )
                                                    }.getOrDefault(TemplateFieldType.TEXT),
                                                    required = data.field.required,
                                                    options = data.options
                                                        .sortedBy { it.sortOrder }
                                                        .map { it.value }
                                                )
                                            }

                                            selectedCustomTemplate = record
                                            selectedCustomFields = fieldData

                                            selectedTemplate = RPTemplate(
                                                id = "custom:${record.id}",
                                                name = record.name,
                                                description = record.description,
                                                fields = templateFields,
                                                isCustom = true
                                            )

                                            currentScreen = AppScreen.TEMPLATE_DETAIL
                                        }
                                    }
                                }

                                template.isCustom &&
                                    template.name == "Custom Template" &&
                                    template.fields.isEmpty() -> {

                                    selectedCustomTemplate = null
                                    selectedCustomFields = emptyList()
                                    currentScreen = AppScreen.CUSTOM_TEMPLATE_BUILDER
                                }

                                else -> {
                                    selectedTemplate = template
                                    currentScreen = AppScreen.TEMPLATE_DETAIL
                                }
                            }
                        }
                    )
                }

                AppScreen.TEMPLATE_DETAIL -> {
                    val template = selectedTemplate

                    if (template != null) {
                        TemplateDetailScreen(
                            template = template,
                            onBackClick = {
                                currentScreen = AppScreen.TEMPLATE_LIBRARY
                            },
                            onEditClick = {
                                if (template.isCustom) {
                                    currentScreen = AppScreen.CUSTOM_TEMPLATE_BUILDER
                                }
                            },
                            onDeleteClick = {
                                val customId = selectedCustomTemplate?.id

                                if (customId != null) {
                                    coroutineScope.launch {
                                        customTemplateRepository.deleteTemplate(customId)

                                        selectedCustomTemplate = null
                                        selectedCustomFields = emptyList()
                                        selectedTemplate = null

                                        currentScreen = AppScreen.TEMPLATE_LIBRARY
                                    }
                                }
                            }
                        )
                    } else {
                        currentScreen = AppScreen.TEMPLATE_LIBRARY
                    }
                }

                AppScreen.CUSTOM_TEMPLATE_BUILDER -> {
                    CustomTemplateBuilderScreen(
                        existingTemplate = selectedCustomTemplate,
                        existingFields = selectedCustomFields,
                        onBack = {
                            currentScreen = AppScreen.TEMPLATE_LIBRARY
                        },
                        onSave = { template, fields ->
                            coroutineScope.launch {
                                customTemplateRepository.saveCompleteTemplate(
                                    template,
                                    fields
                                )

                                selectedCustomTemplate = null
                                selectedCustomFields = emptyList()
                                selectedTemplate = null

                                currentScreen = AppScreen.TEMPLATE_LIBRARY
                            }
                        }
                    )
                }

                // DOCUMENT ENGINE

                AppScreen.DOCUMENTS -> {
                    val project = selectedProject

                    if (project != null) {
                        DocumentScreen(
                            projectName = project.name,
                            documents = documents,
                            onBackClick = {
                                currentScreen = AppScreen.WORKSPACE
                            },
                            onCreateDocument = {
                                newDocumentTitle = ""
                                newDocumentTemplate = null
                                currentScreen = AppScreen.CREATE_DOCUMENT
                            },
                            onDocumentClick = { document ->
                                openExistingDocument(document)
                            },
                            onDeleteDocument = { documentId ->
                                coroutineScope.launch {
                                    documentRepository.deleteDocument(documentId)
                                }
                            }
                        )
                    }
                }

                AppScreen.CREATE_DOCUMENT -> {
                    CreateDocumentScreen(
                        templates = libraryTemplates,
                        title = newDocumentTitle,
                        selectedTemplate = newDocumentTemplate,
                        onTitleChange = {
                            newDocumentTitle = it
                        },
                        onTemplateSelected = {
                            newDocumentTemplate = it
                        },
                        onBackClick = {
                            currentScreen = AppScreen.DOCUMENTS
                        },
                        onContinue = { title, template ->
                            prepareNewDocument(title, template)
                        }
                    )
                }

                AppScreen.DOCUMENT_EDITOR -> {
                    val template = documentTemplate

                    if (template != null) {

                        DocumentEditorScreen(
                            documentTitle = documentTitle,
                            template = template,
                            initialValues = editorValues,
                            onBackClick = {
                                currentScreen = if (selectedDocument == null) {
                                    AppScreen.CREATE_DOCUMENT
                                } else {
                                    AppScreen.DOCUMENTS
                                }
                            },
                            onPreview = { values ->

                                editorValues = values

                                previewFields = template.fields.map { field ->
                                    DocumentPreviewField(
                                        label = field.label,
                                        value = values[field.id].orEmpty()
                                    )
                                }

                                currentScreen = AppScreen.DOCUMENT_PREVIEW
                            },
                            onSave = { values ->
                                saveDocument(values)
                            }
                        )
                    }
                }

                AppScreen.DOCUMENT_PREVIEW -> {
                    val template = documentTemplate

                    if (template != null) {

                        DocumentPreviewScreen(
                            title = documentTitle,
                            templateName = template.name,
                            fields = previewFields,
                            onBackClick = {
                                currentScreen = AppScreen.DOCUMENT_EDITOR
                            },
                            onExportMarkdown = {

                                markdownToExport = DocumentMarkdownExporter.generate(
                                    title = documentTitle,
                                    templateName = template.name,
                                    fields = previewFields
                                )

                                val fileName = documentTitle
                                    .replace(Regex("[\\\\/:*?\"<>|]"), "_")
                                    .ifBlank { "RP_Document" }

                                exportLauncher.launch("$fileName.md")
                            }
                        )
                    }
                }
            }

            if (exportDialogMessage != null) {
                ExportResultDialog(
                    message = exportDialogMessage!!,
                    isError = exportDialogIsError,
                    onDismiss = {
                        exportDialogMessage = null
                    }
                )
            }
        }
    }
}
