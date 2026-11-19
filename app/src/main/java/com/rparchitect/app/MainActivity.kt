
package com.rparchitect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RPArchitectApp()
        }
    }
}

private val BackgroundDark = Color(0xFF101116)
private val CardDark = Color(0xFF1B1D26)
private val AccentColor = Color(0xFF9B8CFF)
private val SecondaryText = Color(0xFF9A9BA8)

data class WorkspaceItem(
    val title: String,
    val description: String,
    val symbol: String
)

@Composable
fun RPArchitectApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                HeaderSection()

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Your Creative Workspace",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Build worlds. Develop characters. Shape stories.",
                    color = SecondaryText,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentColor
                    )
                ) {
                    Text(
                        text = "+  Create New Project",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "DOCUMENTATION",
                    color = SecondaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                val workspaceItems = listOf(
                    WorkspaceItem("Character Bible", "Characters and profiles", "♙"),
                    WorkspaceItem("World Lore", "History, culture and rules", "◈"),
                    WorkspaceItem("Relationships", "Connections and dynamics", "♡"),
                    WorkspaceItem("Timeline", "Events and chronology", "◷"),
                    WorkspaceItem("Locations", "Places and environments", "⌖"),
                    WorkspaceItem("RP Rules", "Rules and guidelines", "≡"),
                    WorkspaceItem("Story Arcs", "Plots and story development", "✦"),
                    WorkspaceItem("Custom Templates", "Create your own structure", "+")
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(workspaceItems) { item ->
                        WorkspaceCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "RP ARCHITECT",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )

            Text(
                text = "ROLEPLAY WORLD BUILDER",
                color = AccentColor,
                fontSize = 9.sp,
                letterSpacing = 2.sp
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(CardDark, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚙",
                color = Color.White,
                fontSize = 21.sp
            )
        }
    }
}

@Composable
fun WorkspaceCard(item: WorkspaceItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.symbol,
                color = AccentColor,
                fontSize = 25.sp
            )

            Column {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    color = SecondaryText,
                    fontSize = 10.sp
                )
            }
        }
    }
}
