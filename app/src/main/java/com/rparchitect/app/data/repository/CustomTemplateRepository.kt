package com.rparchitect.app.data.repository

import com.rparchitect.app.data.dao.CustomTemplateDao
import com.rparchitect.app.data.model.CustomTemplateFieldData
import com.rparchitect.app.data.model.RPCustomTemplate
import com.rparchitect.app.data.model.RPCustomTemplateField
import com.rparchitect.app.data.model.RPCustomTemplateOption
import kotlinx.coroutines.flow.Flow

class CustomTemplateRepository(
    private val customTemplateDao: CustomTemplateDao
) {

    // CUSTOM TEMPLATES

    fun getAllTemplates(): Flow<List<RPCustomTemplate>> {
        return customTemplateDao.getAllTemplates()
    }

    suspend fun getTemplateById(templateId: String): RPCustomTemplate? {
        return customTemplateDao.getTemplateById(templateId)
    }

    suspend fun addTemplate(template: RPCustomTemplate) {
        customTemplateDao.insertTemplate(template)
    }

    suspend fun updateTemplate(template: RPCustomTemplate) {
        customTemplateDao.updateTemplate(template)
    }

    suspend fun deleteTemplate(templateId: String) {
        customTemplateDao.deleteTemplateById(templateId)
    }

    // TEMPLATE FIELDS

    suspend fun getFieldsByTemplate(
        templateId: String
    ): List<RPCustomTemplateField> {
        return customTemplateDao.getFieldsByTemplate(templateId)
    }

    suspend fun addField(field: RPCustomTemplateField) {
        customTemplateDao.insertField(field)
    }

    suspend fun updateField(field: RPCustomTemplateField) {
        customTemplateDao.updateField(field)
    }

    suspend fun deleteField(fieldId: String) {
        customTemplateDao.deleteFieldById(fieldId)
    }

    // CATEGORY OPTIONS

    suspend fun getOptionsByField(
        fieldId: String
    ): List<RPCustomTemplateOption> {
        return customTemplateDao.getOptionsByField(fieldId)
    }

    suspend fun addOption(option: RPCustomTemplateOption) {
        customTemplateDao.insertOption(option)
    }

    suspend fun updateOption(option: RPCustomTemplateOption) {
        customTemplateDao.updateOption(option)
    }

    suspend fun deleteOption(optionId: String) {
        customTemplateDao.deleteOptionById(optionId)
    }

    suspend fun deleteOptionsByField(fieldId: String) {
        customTemplateDao.deleteOptionsByField(fieldId)
    }

    // COMPLETE TEMPLATE

    suspend fun getCompleteTemplate(
        templateId: String
    ): List<CustomTemplateFieldData> {
        val fields = customTemplateDao.getFieldsByTemplate(templateId)

        return fields.map { field ->
            CustomTemplateFieldData(
                field = field,
                options = customTemplateDao.getOptionsByField(field.id)
            )
        }
    }

    suspend fun saveCompleteTemplate(
        template: RPCustomTemplate,
        fields: List<CustomTemplateFieldData>
    ) {
        customTemplateDao.saveCompleteTemplate(template, fields)
    }
}
