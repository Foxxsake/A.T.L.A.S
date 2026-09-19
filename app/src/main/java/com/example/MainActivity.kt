package com.example.atlasdashboard   // change to your package name

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// Colors
val Gold = Color(0xFFFFB800)
val GoldDim = Color(0xFFFFB800).copy(alpha = 0.45f)
val Bg = Color(0xFF05070A)
val CardBg = Color(0xFF0A0E14)
val Green = Color(0xFF00E676)

@Composable
fun AtlasDashboard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header
        HeaderSection()

        Spacer(modifier = Modifier.height(10.dp))

        // Info Cards
        InfoCardsRow()

        Spacer(modifier = Modifier.height(6.dp))

        // Main content
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LeftNavigation()
            CenterOrbSection(modifier = Modifier.weight(1f))
            StatusList()
        }

        // Input
        InputSection()

        Spacer(modifier = Modifier.height(8.dp))

        // Recent Activity
        RecentActivitySection()

        Spacer(modifier = Modifier.height(6.dp))

        // Bottom Nav
        BottomNavigationBar()
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Text(
            text = "A",
            color = Gold,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = "A.T.L.A.S.",
                color = Gold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "ADVANCED TACTICAL LOGIC & ASSISTANCE SYSTEM",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 7.sp,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Connected pill
        Row(
            modifier = Modifier
                .background(Color(0xFF0A1A0A), RoundedCornerShape(20.dp))
                .border(1.dp, Green.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                .padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Green, CircleShape)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "CONNECTED • Hermes Online",
                color = Green,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.Settings, contentDescription = null, tint = Gold, modifier = Modifier.size(19.dp))
    }
}

@Composable
fun InfoCardsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        InfoCard(Icons.Default.Memory, "MODEL", "Claude 3.5 Sonnet", "(OpenRouter)")
        InfoCard(Icons.Outlined.Cloud, "PROVIDER", "OpenRouter Online", null)
        InfoCard(Icons.Outlined.Timer, "SESSION", "Active 2h 14m", null)
    }
}

@Composable
fun RowScope.InfoCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String, sub: String?) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(CardBg, RoundedCornerShape(11.dp))
            .border(1.dp, GoldDim, RoundedCornerShape(11.dp))
            .padding(vertical = 9.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = Gold, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.height(3.dp))
        Text(title, color = Color.White.copy(alpha = 0.55f), fontSize = 8.5.sp)
        Text(
            value,
            color = Color.White,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (sub != null) {
            Text(sub, color = Gold, fontSize = 8.5.sp)
        }
    }
}

@Composable
fun LeftNavigation() {
    val items = listOf(
        Triple(Icons.Default.ChatBubble, "Chat", true),
        Triple(Icons.Default.Terminal, "Terminal", false),
        Triple(Icons.Outlined.Folder, "Files", false),
        Triple(Icons.Outlined.Build, "Tools", false),
        Triple(Icons.Outlined.Layers, "Sessions", false),
        Triple(Icons.Outlined.Settings, "Settings", false),
    )

    Column(
        modifier = Modifier
            .width(68.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { (icon, label, active) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 7.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (active) Gold.copy(alpha = 0.15f) else Color.Transparent,
                            CircleShape
                        )
                        .then(
                            if (active) Modifier.border(1.4.dp, Gold, CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = if (active) Gold else Color.White.copy(alpha = 0.38f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    label,
                    color = if (active) Gold else Color.White.copy(alpha = 0.38f),
                    fontSize = 8.5.sp
                )
            }
        }
    }
}

@Composable
fun CenterOrbSection(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(195.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Gold.copy(alpha = 0.25f),
                    radius = size.minDimension / 2,
                    blendMode = androidx.compose.ui.graphics.BlendMode.Plus
                )
            }

            // Rotating rings
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = rotation }
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                for (i in 1..9) {
                    val radius = (size.minDimension / 2) * (i / 9.5f)
                    drawCircle(
                        color = Gold.copy(alpha = 1.1f - i * 0.1f),
                        radius = radius,
                        center = center,
                        style = Stroke(width = if (i == 1) 2.2f else 1.3f)
                    )
                }
                // Core
                drawCircle(
                    color = Gold,
                    radius = 11f,
                    center = center
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "READY WHEN YOU ARE",
            color = Gold,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Speak · Type · Command · Create",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 10.5.sp
        )
    }
}

@Composable
fun StatusList() {
    val statuses = listOf(
        Triple(Icons.Default.RadioButtonChecked, "IDLE", true),
        Triple(Icons.Default.Mic, "LISTENING", false),
        Triple(Icons.Default.Psychology, "THINKING", false),
        Triple(Icons.Default.PlayArrow, "EXECUTING", false),
        Triple(Icons.Default.VolumeUp, "SPEAKING", false),
        Triple(Icons.Default.VerifiedUser, "APPROVAL", false),
        Triple(Icons.Default.ErrorOutline, "ERROR", false),
        Triple(Icons.Default.CloudOff, "OFFLINE", false),
    )

    Column(
        modifier = Modifier
            .width(92.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        statuses.forEach { (icon, label, active) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.5.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (active) Gold else Color.White.copy(alpha = 0.38f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    label,
                    color = if (active) Gold else Color.White.copy(alpha = 0.38f),
                    fontSize = 9.5.sp,
                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun InputSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        // Search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(26.dp))
                .border(1.dp, GoldDim, RoundedCornerShape(26.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Mic, contentDescription = null, tint = Gold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Ask me anything...",
                color = Color.White.copy(alpha = 0.38f),
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Gold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Black, modifier = Modifier.size(17.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            ActionChip(Icons.Default.ViewInAr, "Build something")
            ActionChip(Icons.Default.Search, "Search the web")
            ActionChip(Icons.Default.Terminal, "Run a command")
            ActionChip(Icons.Default.MoreHoriz, "More")
        }
    }
}

@Composable
fun RowScope.ActionChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        modifier = Modifier
            .weight(1f)
            .background(CardBg, RoundedCornerShape(18.dp))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Gold, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 9.5.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun RecentActivitySection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .background(CardBg, RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("RECENT ACTIVITY", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text("View All", color = Gold, fontSize = 10.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        ActivityItem(Icons.Default.Terminal, "Terminal", "npm run dev", "2m ago", true)
        ActivityItem(Icons.Outlined.InsertDriveFile, "File Operation", "Created: src/components/QuantumCore.tsx", "5m ago", true)
        ActivityItem(Icons.Default.Psychology, "Thinking", "Planning next steps...", "7m ago", false)
    }
}

@Composable
fun ActivityItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, desc: String, time: String, done: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Gold, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(9.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 11.5.sp, fontWeight = FontWeight.Medium)
            Text(desc, color = Color.White.copy(alpha = 0.55f), fontSize = 9.5.sp)
        }
        Text(time, color = Color.White.copy(alpha = 0.38f), fontSize = 9.5.sp)
        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            if (done) Icons.Default.CheckCircle else Icons.Default.Sync,
            contentDescription = null,
            tint = if (done) Green else Color(0xFF00B0FF),
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(Icons.Default.Home, "Home", true)
        BottomNavItem(Icons.Outlined.ChatBubble, "Chat", false)
        BottomNavItem(Icons.Outlined.History, "History", false)
        BottomNavItem(Icons.Outlined.Person, "Profile", false)
    }
}

@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (active) Gold else Color.White.copy(alpha = 0.38f),
            modifier = Modifier.size(21.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            color = if (active) Gold else Color.White.copy(alpha = 0.38f),
            fontSize = 9.5.sp
        )
    }
}
