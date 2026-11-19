package com.rparchitect.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rparchitect.app.data.model.RPCustomTemplate
import com.rparchitect.app.data.model.RPCustomTemplateField
import com.rparchitect.app.data.model.RPCustomTemplateOption
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomTemplateDao {

    // CUSTOM TEMPLATES

    @Query("SELECT * FROM custom_templates ORDER BY createdAt DESC")
    fun getAllTemplates(): Flow<List<RPCustomTemplate>>

    @Query("SELECT * FROM custom_templates WHERE id = :templateId LIMIT 1")
    suspend fun getTemplateById(templateId: String): RPCustomTemplate?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTemplate(template: RPCustomTemplate)

    @Update
    suspend fun updateTemplate(template: RPCustomTemplate)

    @Query("DELETE FROM custom_templates WHERE id = :templateId")
    suspend fun deleteTemplateById(templateId: String)


    // TEMPLATE FIELDS

    @Query("""
        SELECT * FROM custom_template_fields
        WHERE templateId = :templateId
        ORDER BY rowid ASC
    """)
    suspend fun getFieldsByTemplate(templateId: String): List<RPCustomTemplateField>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertField(field: RPCustomTemplateField)

    @Update
    suspend fun updateField(field: RPCustomTemplateField)

    @Query("DELETE FROM custom_template_fields WHERE id = :fieldId")
    suspend fun deleteFieldById(fieldId: String)


    // CATEGORY OPTIONS

    @Query("""
        SELECT * FROM custom_template_options
        WHERE fieldId = :fieldId
        ORDER BY sortOrder ASC
    """)
    suspend fun getOptionsByField(fieldId: String): List<RPCustomTemplateOption>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOption(option: RPCustomTemplateOption)

    @Update
    suspend fun updateOption(option: RPCustomTemplateOption)

    @Query("DELETE FROM custom_template_options WHERE id = :optionId")
    suspend fun deleteOptionById(optionId: String)

    @Query("DELETE FROM custom_template_options WHERE fieldId = :fieldId")
    suspend fun deleteOptionsByField(fieldId: String)
}
