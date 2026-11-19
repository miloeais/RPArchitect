
package com.rparchitect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rparchitect.app.data.database.DatabaseProvider
import com.rparchitect.app.data.model.*
import com.rparchitect.app.data.template.PresetLibrary
import com.rparchitect.app.data.repository.*
import com.rparchitect.app.ui.screens.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.UUID

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
    DOCUMENT_EDITOR
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
        } ?: flowOf(emptyList())
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
        personality: String,
        background: String,
        appearance: String,
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
            personality = personality,
            background = background,
            appearance = appearance,
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
        personality: String,
        background: String,
        appearance: String,
        notes: String
    ) {
        val oldCharacter = selectedCharacter ?: return

        val updatedCharacter = oldCharacter.copy(
            name = name,
            role = role,
            age = age,
            gender = gender,
            personality = personality,
            background = background,
            appearance = appearance,
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

            currentScreen = AppScreen.DOCUMENTS
        }
    }

    // UI

    MaterialTheme {

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
                        name, role, age, gender,
                        personality, background, appearance, notes ->

                        createCharacter(
                            name, role, age, gender,
                            personality, background, appearance, notes
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
                            name, role, age, gender,
                            personality, background, appearance, notes ->

                            updateCharacter(
                                name, role, age, gender,
                                personality, background, appearance, notes
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

                    val initialValues = remember(
                        documentFields,
                        selectedDocument
                    ) {
                        documentFields.associate {
                            it.fieldId to it.value
                        }
                    }

                    DocumentEditorScreen(
                        documentTitle = documentTitle,
                        template = template,
                        initialValues = initialValues,
                        onBackClick = {
                            currentScreen = if (selectedDocument == null) {
                                AppScreen.CREATE_DOCUMENT
                            } else {
                                AppScreen.DOCUMENTS
                            }
                        },
                        onSave = { values ->
                            saveDocument(values)
                        }
                    )
                }
            }
        }
    }
}
