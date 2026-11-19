package com.rparchitect.app.data.export

data class DocumentPreviewField(
    val label: String,
    val value: String
)

object DocumentMarkdownExporter {

    fun generate(
        title: String,
        templateName: String,
        fields: List<DocumentPreviewField>
    ): String {

        return buildString {

            appendLine("# $title")
            appendLine()
            appendLine("**Template:** $templateName")
            appendLine()
            appendLine("---")
            appendLine()

            fields.forEach { field ->

                appendLine("## ${field.label}")
                appendLine()

                val value = field.value.trim()

                if (value.isEmpty()) {
                    appendLine("_Not filled_")
                } else {
                    appendLine(value)
                }

                appendLine()
            }
        }.trimEnd()
    }
}
