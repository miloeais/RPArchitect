
package com.rparchitect.app.data.repository

import com.rparchitect.app.data.dao.DocumentDao
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.data.model.RPDocumentField
import kotlinx.coroutines.flow.Flow

class DocumentRepository(
    private val documentDao: DocumentDao
) {

    fun getDocumentsByProject(
        projectId: Long
    ): Flow<List<RPDocument>> {
        return documentDao.getDocumentsByProject(projectId)
    }

    suspend fun getDocumentById(
        documentId: String
    ): RPDocument? {
        return documentDao.getDocumentById(documentId)
    }

    suspend fun getFieldsByDocument(
        documentId: String
    ): List<RPDocumentField> {
        return documentDao.getFieldsByDocument(documentId)
    }

    suspend fun saveCompleteDocument(
        document: RPDocument,
        fields: List<RPDocumentField>
    ) {
        documentDao.saveCompleteDocument(document, fields)
    }

    suspend fun deleteDocument(documentId: String) {
        documentDao.deleteDocumentById(documentId)
    }
}
