
package com.rparchitect.app.data.repository

import com.rparchitect.app.data.model.RPDocumentField
import com.rparchitect.app.data.model.RPTemplate
import org.json.JSONArray
import java.util.UUID

object DocumentFieldMapper {

    fun createSnapshot(
        documentId: String,
        template: RPTemplate
    ): List<RPDocumentField> {

        return template.fields.mapIndexed { index, field ->

            RPDocumentField(
                id = UUID.randomUUID().toString(),
                documentId = documentId,
                fieldId = field.id,
                label = field.label,
                type = field.type.name,
                value = "",
                required = field.required,
                sortOrder = index,
                options = JSONArray(field.options).toString()
            )
        }
    }

    fun decodeOptions(options: String): List<String> {
        return try {
            val jsonArray = JSONArray(options)

            List(jsonArray.length()) { index ->
                jsonArray.getString(index)
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
