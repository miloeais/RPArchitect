
package com.rparchitect.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rparchitect.app.ui.theme.RPArchitectColors

@Composable
fun ExportAllButton(
    text: String = "Export All",
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = RPArchitectColors.IconBackground,
            contentColor = RPArchitectColors.PrimaryText,
            disabledContainerColor = RPArchitectColors.Surface,
            disabledContentColor = RPArchitectColors.SecondaryText
        )
    ) {
        Text(text = text)
    }
}
