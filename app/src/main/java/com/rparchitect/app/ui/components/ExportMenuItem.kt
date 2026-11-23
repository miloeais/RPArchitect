
package com.rparchitect.app.ui.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rparchitect.app.ui.theme.RPArchitectColors

@Composable
fun ExportMenuItem(
    text: String = "Export Markdown",
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                color = RPArchitectColors.PrimaryText
            )
        },
        onClick = onClick
    )
}
