
package com.rparchitect.app.data.export

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExportFolderPicker(
    onFolderSelected: (Uri) -> Unit,
    content: @Composable (openFolderPicker: () -> Unit) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            ExportFolderManager.saveFolderPermission(
                context = context,
                folderUri = uri
            )

            onFolderSelected(uri)
        }
    }

    content {
        launcher.launch(null)
    }
}
