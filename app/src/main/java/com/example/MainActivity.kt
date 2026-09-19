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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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
val MutedWhite = Color.White.copy(alpha = 0.5f)
val FaintWhite = Color.White.copy(alpha = 0.35f)
val RingDim = Color.White.copy(alpha = 0.12f)

@Composable
fun AtlasDashboard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Header()

        Spacer(modifier = Modifier.height(8.dp))

        InfoCards()

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LeftNav()
            CenterOrb(modifier = Modifier.weight(1f))
            RightStatus()
        }

        InputBar()

        Spacer(modifier = Modifier.height(6.dp))

        RecentActivity()

        Spacer(modifier = Modifier.height(4.dp))

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
        AtlasLogo(logoSize = 34.dp)

        Spacer(modifier = Modifier.width(8.dp))

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
                color = MutedWhite,
                fontSize = 6.5.sp,
                letterSpacing = 0.4.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ConnectedBadge()

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .size(30.dp)
                .border(1.dp, GoldDim, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Settings, null, tint = Gold, modifier = Modifier.size(15.dp))
        }
    }
}

/** Stylized triangular "A" logomark, drawn in-canvas so no drawable asset is required. */
@Composable
fun AtlasLogo(logoSize: Dp) {
    Canvas(modifier = Modifier.size(logoSize)) {
        val w = logoSize.toPx()
        val h = logoSize.toPx()
        val gradient = Brush.verticalGradient(listOf(GoldBright, Gold))

        val mark = Path().apply {
            moveTo(w * 0.5f, 0f)
            lineTo(w * 0.98f, h)
            lineTo(w * 0.74f, h)
            lineTo(w * 0.5f, h * 0.45f)
            lineTo(w * 0.26f, h)
            lineTo(w * 0.02f, h)
            close()
        }
        drawPath(mark, brush = gradient)

        // Negative-space crossbar so the shape reads as "A"
        drawRect(
            color = Bg,
            topLeft = Offset(w * 0.40f, h * 0.60f),
            size = Size(w * 0.20f, h * 0.10f)
        )
    }
}

@Composable
fun ConnectedBadge() {
    Row(
        modifier = Modifier
            .background(Color(0xFF0A1A0A), RoundedCornerShape(50))
            .border(1.dp, Green.copy(alpha = 0.4f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(Green, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = "CONNECTED",
                color = Green,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )
            Text(
                text = "Hermes Online",
                color = Green.copy(alpha = 0.75f),
                fontSize = 8.sp
            )
        }
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
        InfoCard(Icons.Default.Memory, "MODEL", "Claude 3.5 Sonnet", "(OpenRouter)", Gold)
        InfoCard(Icons.Outlined.Public, "PROVIDER", "OpenRouter", "Online", MutedWhite)
        InfoCard(Icons.Outlined.Storage, "SESSION", "Active", "2h 14m", MutedWhite)
    }
}

@Composable
fun RowScope.InfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    sub: String?,
    subColor: Color
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(CardBg, RoundedCornerShape(10.dp))
            .border(1.dp, GoldDim, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(1.dp, GoldDim, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Gold, modifier = Modifier.size(12.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, color = MutedWhite, fontSize = 8.sp, letterSpacing = 0.3.sp)
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
            Text(sub, color = subColor, fontSize = 8.sp)
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
                        .border(
                            1.5.dp,
                            if (active) Gold else RingDim,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon, label,
                        tint = if (active) Gold else FaintWhite,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    label,
                    color = if (active) Gold else FaintWhite,
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
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )
    val counterRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot2"
    )

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer soft ambient glow
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Gold.copy(alpha = 0.30f),
                            Gold.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    radius = size.minDimension / 1.7f
                )
            }

            // Fine radiating spokes for a "circuit burst" texture
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = counterRotation }
            ) {
                val c = Offset(size.width / 2, size.height / 2)
                val maxR = size.minDimension / 2
                val spokes = 48
                for (i in 0 until spokes) {
                    val angle = (i * (360f / spokes)) * (PI / 180f)
                    val lenFactor = 0.55f + ((i * 37) % 45) / 100f
                    val r1 = maxR * 0.4f
                    val r2 = maxR * lenFactor
                    val alpha = 0.08f + ((i * 13) % 30) / 100f
                    drawLine(
                        color = Gold.copy(alpha = alpha),
                        start = Offset(
                            c.x + r1 * cos(angle).toFloat(),
                            c.y + r1 * sin(angle).toFloat()
                        ),
                        end = Offset(
                            c.x + r2 * cos(angle).toFloat(),
                            c.y + r2 * sin(angle).toFloat()
                        ),
                        strokeWidth = 1f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // Rotating concentric rings + bright core
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = rotation }
            ) {
                val c = Offset(size.width / 2, size.height / 2)
                val maxR = size.minDimension / 2

                for (i in 1..14) {
                    val r = maxR * (i / 14.5f)
                    val alpha = (1.05f - i * 0.06f).coerceIn(0.10f, 0.95f)
                    drawCircle(
                        color = Gold.copy(alpha = alpha),
                        radius = r,
                        center = c,
                        style = Stroke(width = if (i <= 2) 2.2f else 1f)
                    )
                }

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoldBright, Gold, Gold.copy(alpha = 0.6f))
                    ),
                    radius = 16f,
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
            color = MutedWhite,
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
            .width(92.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        statuses.forEach { (icon, label, active) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(1.dp, if (active) Gold else RingDim, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon, null,
                        tint = if (active) Gold else FaintWhite,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    label,
                    color = if (active) Gold else FaintWhite,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Standalone mic button, separate from the search pill
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, Gold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, null, tint = Gold, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Search pill with trailing send button
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(CardBg, RoundedCornerShape(24.dp))
                    .border(1.dp, GoldDim, RoundedCornerShape(24.dp))
                    .padding(start = 14.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Chip(Icons.Default.AutoAwesome, "Build something")
            Chip(Icons.Default.Search, "Search the web")
            Chip(Icons.Default.Terminal, "Run a command")
            Chip(Icons.Default.MoreHoriz, "More")
        }
    }
}

@Composable
fun RowScope.Chip(icon: ImageVector, label: String) {
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

        ActivityRow(Icons.Default.Terminal, "Terminal", "npm run dev", "2m ago", true, monospace = true)
        ActivityRow(Icons.Outlined.InsertDriveFile, "File Operation", "Created: src/components/QuantumCore.tsx", "5m ago", true, monospace = true)
        ActivityRow(Icons.Default.Psychology, "Thinking", "Planning next steps...", "7m ago", false)
    }
}

@Composable
fun ActivityRow(
    icon: ImageVector,
    title: String,
    desc: String,
    time: String,
    done: Boolean,
    monospace: Boolean = false
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
            Text(
                desc,
                color = MutedWhite,
                fontSize = 9.sp,
                fontFamily = if (monospace) FontFamily.Monospace else FontFamily.Default
            )
        }
        Text(time, color = FaintWhite, fontSize = 9.sp)
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
fun BottomItem(icon: ImageVector, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon, label,
            tint = if (active) Gold else FaintWhite,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            color = if (active) Gold else FaintWhite,
            fontSize = 9.sp
        )
    }
}
