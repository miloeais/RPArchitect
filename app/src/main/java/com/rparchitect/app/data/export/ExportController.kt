
package com.rparchitect.app.data.export

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.data.model.RPWorldEntry
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.data.model.RPDocumentField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ExportController(
    characters: List<RPCharacter>,
    worldLores: List<RPWorldLore>,
    worldEntries: List<RPWorldEntry>,
    documents: List<RPDocument>,
    loadDocumentFields: suspend (RPDocument) -> List<RPDocumentField>,
    onExportCompleted: (Int) -> Unit,
    onExportError: (String) -> Unit,
    content: @Composable (
        requestExport: (ExportRequest) -> Unit
    ) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var pendingRequest by remember {
        mutableStateOf<ExportRequest?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { folderUri: Uri? ->

        val request = pendingRequest
        pendingRequest = null

        if (folderUri != null && request != null) {
            try {
                ExportFolderManager.saveFolderPermission(
                    context = context,
                    folderUri = folderUri
                )

                scope.launch {
                    try {
                        val count = withContext(Dispatchers.IO) {
                            ExportCoordinator.execute(
                                context = context,
                                folderUri = folderUri,
                                request = request,
                                characters = characters,
                                worldLores = worldLores,
                                worldEntries = worldEntries,
                                documents = documents,
                                loadDocumentFields = loadDocumentFields
                            )
                        }

                        onExportCompleted(count)

                    } catch (error: Exception) {
                        onExportError(
                            error.message ?: "Export failed."
                        )
                    }
                }

            } catch (error: Exception) {
                onExportError(
                    error.message ?: "Unable to access folder."
                )
            }
        }
    }

    content { request ->
        pendingRequest = request
        launcher.launch(null)
    }
}
