
package com.rparchitect.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.data.model.RPDocumentField
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    // DOCUMENTS

    @Query("""
        SELECT * FROM rp_documents
        WHERE projectId = :projectId
        ORDER BY updatedAt DESC
    """)
    fun getDocumentsByProject(
        projectId: Long
    ): Flow<List<RPDocument>>

    @Query("""
        SELECT * FROM rp_documents
        WHERE id = :documentId
        LIMIT 1
    """)
    suspend fun getDocumentById(
        documentId: String
    ): RPDocument?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDocument(document: RPDocument)

    @Update
    suspend fun updateDocument(document: RPDocument)

    @Query("DELETE FROM rp_documents WHERE id = :documentId")
    suspend fun deleteDocumentById(documentId: String)


    // DOCUMENT FIELDS

    @Query("""
        SELECT * FROM rp_document_fields
        WHERE documentId = :documentId
        ORDER BY sortOrder ASC
    """)
    suspend fun getFieldsByDocument(
        documentId: String
    ): List<RPDocumentField>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertField(field: RPDocumentField)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertFields(fields: List<RPDocumentField>)

    @Update
    suspend fun updateField(field: RPDocumentField)

    @Query("""
        DELETE FROM rp_document_fields
        WHERE documentId = :documentId
    """)
    suspend fun deleteFieldsByDocument(documentId: String)


    // SAVE COMPLETE DOCUMENT

    @Transaction
    suspend fun saveCompleteDocument(
        document: RPDocument,
        fields: List<RPDocumentField>
    ) {
        val existing = getDocumentById(document.id)

        if (existing == null) {
            insertDocument(document)
        } else {
            updateDocument(document)
            deleteFieldsByDocument(document.id)
        }

        if (fields.isNotEmpty()) {
            insertFields(fields)
        }
    }
}
