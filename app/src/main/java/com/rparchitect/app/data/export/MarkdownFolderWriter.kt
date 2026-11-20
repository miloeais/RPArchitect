
package com.rparchitect.app.data.export

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract

object MarkdownFolderWriter {

    private const val MIME_TYPE = "text/markdown"

    fun write(
        context: Context,
        folderUri: Uri,
        fileName: String,
        content: String
    ): Uri {

        val directoryId = DocumentsContract.getTreeDocumentId(folderUri)

        val directoryUri = DocumentsContract.buildDocumentUriUsingTree(
            folderUri,
            directoryId
        )

        val safeName = sanitizeFileName(fileName)

        val fileUri = DocumentsContract.createDocument(
            context.contentResolver,
            directoryUri,
            MIME_TYPE,
            safeName
        ) ?: error("Unable to create file: $safeName")

        val outputStream = context.contentResolver
            .openOutputStream(fileUri, "w")
            ?: error("Unable to open file: $safeName")

        outputStream.bufferedWriter(Charsets.UTF_8).use {
            it.write(content)
        }

        return fileUri
    }

    fun sanitizeFileName(name: String): String {
        return name
            .trim()
            .replace(Regex("[\\\\/:*?\"<>|]"), "_")
            .replace(Regex("\\s+"), "_")
            .ifBlank { "RP_Export" }
    }
}
