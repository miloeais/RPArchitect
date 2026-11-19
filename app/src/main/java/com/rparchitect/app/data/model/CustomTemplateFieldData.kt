package com.rparchitect.app.data.model

data class CustomTemplateFieldData(
    val field: RPCustomTemplateField,
    val options: List<RPCustomTemplateOption> = emptyList()
)
