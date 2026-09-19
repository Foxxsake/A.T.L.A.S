package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Bg,
                    surface = CardBg,
                    primary = Gold
                )
            ) {
                AtlasDashboard()
            }
        }
    }
}

// Colors matching the HD mockup
val Gold = Color(0xFFFFB800)
val GoldBright = Color(0xFFFFD54F)
val GoldDim = Color(0xFFFFB800).copy(alpha = 0.4f)
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
        Header()
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Info cards
        InfoCards()
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Main area
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LeftNav()
            CenterOrb(modifier = Modifier.weight(1f))
            RightStatus()
        }
        
        // Input
        InputBar()
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // Recent activity
        RecentActivity()
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Bottom nav
        BottomNav()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Text(
            text = "A",
            color = Gold,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(
                text = "A.T.L.A.S.",
                color = Gold,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "ADVANCED TACTICAL LOGIC & ASSISTANCE SYSTEM",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 6.5.sp,
                letterSpacing = 0.4.sp
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Connected badge
        Row(
            modifier = Modifier
                .background(Color(0xFF0A1A0A), RoundedCornerShape(20.dp))
                .border(1.dp, Green.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 3.5.dp),
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
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.width(6.dp))
        Icon(Icons.Default.Settings, null, tint = Gold, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun InfoCards() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        InfoCard(Icons.Default.Memory, "MODEL", "Claude 3.5 Sonnet", "(OpenRouter)")
        InfoCard(Icons.Outlined.Cloud, "PROVIDER", "OpenRouter Online", null)
        InfoCard(Icons.Outlined.Timer, "SESSION", "Active 2h 14m", null)
    }
}

@Composable
fun RowScope.InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    sub: String?
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(CardBg, RoundedCornerShape(10.dp))
            .border(1.dp, GoldDim, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = Gold, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(title, color = Color.White.copy(alpha = 0.5f), fontSize = 8.sp)
        Text(
            value,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (sub != null) {
            Text(sub, color = Gold, fontSize = 8.sp)
        }
    }
}

@Composable
fun LeftNav() {
    val items = listOf(
        Triple(Icons.Default.ChatBubble, "Chat", true),
        Triple(Icons.Default.Terminal, "Terminal", false),
        Triple(Icons.Outlined.Folder, "Files", false),
        Triple(Icons.Outlined.Build, "Tools", false),
        Triple(Icons.Outlined.Layers, "Sessions", false),
        Triple(Icons.Outlined.Settings, "Settings", false)
    )

    Column(
        modifier = Modifier
            .width(64.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { (icon, label, active) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (active) Gold.copy(alpha = 0.15f) else Color.Transparent,
                            CircleShape
                        )
                        .then(
                            if (active) Modifier.border(1.5.dp, Gold, CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon, label,
                        tint = if (active) Gold else Color.White.copy(alpha = 0.35f),
                        modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    label,
                    color = if (active) Gold else Color.White.copy(alpha = 0.35f),
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun CenterOrb(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer soft glow
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Gold.copy(alpha = 0.35f),
                            Gold.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    radius = size.minDimension / 1.8f
                )
            }

            // Rotating rings
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = rotation }
            ) {
                val c = Offset(size.width / 2, size.height / 2)
                val maxR = size.minDimension / 2

                // Multiple rings with decreasing opacity
                for (i in 1..10) {
                    val r = maxR * (i / 10.5f)
                    val alpha = (1.1f - i * 0.09f).coerceIn(0.15f, 0.95f)
                    drawCircle(
                        color = Gold.copy(alpha = alpha),
                        radius = r,
                        center = c,
                        style = Stroke(width = if (i <= 2) 2.2f else 1.2f)
                    )
                }

                // Bright core
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoldBright, Gold, Gold.copy(alpha = 0.6f))
                    ),
                    radius = 14f,
                    center = c
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            "READY WHEN YOU ARE",
            color = Gold,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "Speak · Type · Command · Create",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp
        )
    }
}

@Composable
fun RightStatus() {
    val statuses = listOf(
        Triple(Icons.Default.RadioButtonChecked, "IDLE", true),
        Triple(Icons.Default.Mic, "LISTENING", false),
        Triple(Icons.Default.Psychology, "THINKING", false),
        Triple(Icons.Default.PlayArrow, "EXECUTING", false),
        Triple(Icons.Default.VolumeUp, "SPEAKING", false),
        Triple(Icons.Default.VerifiedUser, "APPROVAL", false),
        Triple(Icons.Default.ErrorOutline, "ERROR", false),
        Triple(Icons.Default.CloudOff, "OFFLINE", false)
    )

    Column(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        statuses.forEach { (icon, label, active) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    icon, null,
                    tint = if (active) Gold else Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    label,
                    color = if (active) Gold else Color.White.copy(alpha = 0.35f),
                    fontSize = 9.sp,
                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun InputBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        // Search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(24.dp))
                .border(1.dp, GoldDim, RoundedCornerShape(24.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Mic, null, tint = Gold, modifier = Modifier.size(19.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Ask me anything...",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 13.5.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Gold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowForward, null, tint = Color.Black, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Quick chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Chip(Icons.Default.ViewInAr, "Build something")
            Chip(Icons.Default.Search, "Search the web")
            Chip(Icons.Default.Terminal, "Run a command")
            Chip(Icons.Default.MoreHoriz, "More")
        }
    }
}

@Composable
fun RowScope.Chip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        modifier = Modifier
            .weight(1f)
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(1.dp, Gold.copy(alpha = 0.28f), RoundedCornerShape(16.dp))
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Gold, modifier = Modifier.size(11.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 9.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun RecentActivity() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(CardBg, RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("RECENT ACTIVITY", color = Color.White, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold)
            Text("View All", color = Gold, fontSize = 9.5.sp)
        }

        Spacer(modifier = Modifier.height(6.dp))

        ActivityRow(Icons.Default.Terminal, "Terminal", "npm run dev", "2m ago", true)
        ActivityRow(Icons.Outlined.InsertDriveFile, "File Operation", "Created: src/components/QuantumCore.tsx", "5m ago", true)
        ActivityRow(Icons.Default.Psychology, "Thinking", "Planning next steps...", "7m ago", false)
    }
}

@Composable
fun ActivityRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    time: String,
    done: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Gold, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(desc, color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
        }
        Text(time, color = Color.White.copy(alpha = 0.35f), fontSize = 9.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            if (done) Icons.Default.CheckCircle else Icons.Default.Sync,
            null,
            tint = if (done) Green else Color(0xFF00B0FF),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun BottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomItem(Icons.Default.Home, "Home", true)
        BottomItem(Icons.Outlined.ChatBubble, "Chat", false)
        BottomItem(Icons.Outlined.History, "History", false)
        BottomItem(Icons.Outlined.Person, "Profile", false)
    }
}

@Composable
fun BottomItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon, label,
            tint = if (active) Gold else Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            color = if (active) Gold else Color.White.copy(alpha = 0.35f),
            fontSize = 9.sp
        )
    }
}
