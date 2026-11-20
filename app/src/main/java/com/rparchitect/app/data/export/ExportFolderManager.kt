
package com.rparchitect.app.data.export

import android.content.Context
import android.content.Intent
import android.net.Uri

object ExportFolderManager {

    fun saveFolderPermission(
        context: Context,
        folderUri: Uri
    ) {
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        context.contentResolver.takePersistableUriPermission(
            folderUri,
            flags
        )
    }

    fun hasFolderPermission(
        context: Context,
        folderUri: Uri
    ): Boolean {
        return context.contentResolver.persistedUriPermissions.any {
            it.uri == folderUri && it.isWritePermission
        }
    }
}
