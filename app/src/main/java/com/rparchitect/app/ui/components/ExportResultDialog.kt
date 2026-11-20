
package com.rparchitect.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.rparchitect.app.ui.theme.RPArchitectColors

@Composable
fun ExportResultDialog(
    message: String,
    isError: Boolean = false,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isError) "Export Failed" else "Export Completed",
                color = RPArchitectColors.PrimaryText
            )
        },
        text = {
            Text(
                text = message,
                color = RPArchitectColors.SecondaryText
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "OK",
                    color = RPArchitectColors.PrimaryText
                )
            }
        },
        containerColor = RPArchitectColors.Surface
    )
}
