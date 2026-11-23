
package com.rparchitect.app.data.model

data class RPTemplate(
    val id: String,
    val name: String,
    val description: String,
    val fields: List<RPTemplateField>,
    val isCustom: Boolean = false
)

data class RPTemplateField(
    val id: String,
    val label: String,
    val type: TemplateFieldType,
    val required: Boolean = false,
    val options: List<String> = emptyList()
)

enum class TemplateFieldType {
    TEXT,
    LONG_TEXT,
    NUMBER,
    DATE,
    CATEGORY
}
