package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AtlasBackground
import com.example.ui.theme.AtlasBorder
import com.example.ui.theme.AtlasGold
import com.example.ui.theme.AtlasGoldDark
import com.example.ui.theme.AtlasGoldLight
import com.example.ui.theme.AtlasGreen
import com.example.ui.theme.AtlasSurface
import com.example.ui.theme.AtlasSurfaceCard
import com.example.ui.theme.AtlasTextMuted
import com.example.ui.theme.AtlasTextSubtle
import com.example.ui.theme.MyApplicationTheme
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        AtlasDashboardApp()
      }
    }
  }
}

data class ActivityItem(
  val type: String,
  val detail: String,
  val time: String,
  val icon: ImageVector,
  val isSuccess: Boolean = true,
  val isLoading: Boolean = false,
)

data class ToolItem(
  val name: String,
  val icon: ImageVector,
)

data class StatusItem(
  val name: String,
  val icon: ImageVector,
)

@Composable
fun AtlasDashboardApp() {
  var selectedLeftIndex by remember { mutableIntStateOf(0) }
  var selectedStatusIndex by remember { mutableIntStateOf(0) }
  var bottomNavIndex by remember { mutableIntStateOf(0) }
  var promptText by remember { mutableStateOf("") }

  val recentActivities = remember {
    mutableStateListOf(
      ActivityItem(
        type = "Terminal",
        detail = "npm run dev",
        time = "2m ago",
        icon = Icons.Filled.Terminal,
        isSuccess = true,
      ),
      ActivityItem(
        type = "File Operation",
        detail = "Created: src/components/QuantumCore.tsx",
        time = "5m ago",
        icon = Icons.Filled.Folder,
        isSuccess = true,
      ),
      ActivityItem(
        type = "Thinking",
        detail = "Planning next steps...",
        time = "7m ago",
        icon = Icons.Filled.Psychology,
        isSuccess = false,
        isLoading = true,
      ),
    )
  }

  val leftTools = remember {
    listOf(
      ToolItem("Chat", Icons.Filled.ChatBubble),
      ToolItem("Terminal", Icons.Filled.Terminal),
      ToolItem("Files", Icons.Filled.Folder),
      ToolItem("Tools", Icons.Filled.Build),
      ToolItem("Sessions", Icons.Filled.Schedule),
      ToolItem("Settings", Icons.Filled.Settings),
    )
  }

  val statusItems = remember {
    listOf(
      StatusItem("IDLE", Icons.Filled.Adjust),
      StatusItem("LISTENING", Icons.Filled.GraphicEq),
      StatusItem("THINKING", Icons.Filled.Psychology),
      StatusItem("EXECUTING", Icons.Filled.PlayArrow),
      StatusItem("SPEAKING", Icons.Filled.VolumeUp),
      StatusItem("APPROVAL", Icons.Filled.Shield),
      StatusItem("ERROR", Icons.Filled.Cancel),
      StatusItem("OFFLINE", Icons.Filled.CloudOff),
    )
  }

  val infiniteTransition = rememberInfiniteTransition(label = "orbTransition")
  val orbRotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 24000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "orbRotation",
  )
  val pulseValue by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse,
    ),
    label = "pulseValue",
  )

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(AtlasBackground),
    containerColor = AtlasBackground,
    bottomBar = {
      AtlasBottomNavigationBar(
        selectedIndex = bottomNavIndex,
        onSelect = { bottomNavIndex = it },
      )
    },
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .statusBarsPadding()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
      // Top Header
      AtlasTopHeader()

      Spacer(modifier = Modifier.height(14.dp))

      // 3 Info Cards
      AtlasMetricsRow()

      Spacer(modifier = Modifier.height(14.dp))

      // Central Orb Dashboard with Left Tools & Right Status
      AtlasCenterDashboard(
        leftTools = leftTools,
        selectedLeftIndex = selectedLeftIndex,
        onSelectLeft = { selectedLeftIndex = it },
        statusItems = statusItems,
        selectedStatusIndex = selectedStatusIndex,
        onSelectStatus = { selectedStatusIndex = it },
        orbRotation = orbRotation,
        pulseValue = pulseValue,
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Search bar
      AtlasSearchBar(
        query = promptText,
        onQueryChange = { promptText = it },
        onSubmit = {
          if (promptText.isNotBlank()) {
            recentActivities.add(
              0,
              ActivityItem(
                type = "Command",
                detail = promptText.trim(),
                time = "Just now",
                icon = Icons.Filled.Terminal,
                isSuccess = true,
              ),
            )
            promptText = ""
            selectedStatusIndex = 3 // EXECUTING
          }
        },
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 4 Quick Action buttons
      AtlasQuickActionChips(
        onActionClick = { label ->
          recentActivities.add(
            0,
            ActivityItem(
              type = "Action",
              detail = label,
              time = "Just now",
              icon = Icons.Filled.Terminal,
              isSuccess = true,
            ),
          )
        },
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Recent Activity
      AtlasRecentActivityCard(activities = recentActivities)

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun AtlasTopHeader() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    // Stylized "A" Chevron Logo
    AtlasLogoCanvas(modifier = Modifier.size(38.dp, 44.dp))

    Spacer(modifier = Modifier.width(10.dp))

    // Title and subtitle
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = "A.T.L.A.S.",
        color = AtlasGold,
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 3.sp,
        fontFamily = FontFamily.SansSerif,
      )
      Text(
        text = "ADVANCED TACTICAL LOGIC & ASSISTANCE SYSTEM",
        color = AtlasGoldDark,
        fontSize = 8.5.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.1.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }

    // Connected Badge
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFF071B12))
        .border(1.dp, AtlasGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(7.dp)
            .clip(CircleShape)
            .background(AtlasGreen),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "CONNECTED",
          color = AtlasGreen,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.6.sp,
        )
        Text(
          text = " • ",
          color = AtlasGreen,
          fontSize = 11.sp,
        )
        Text(
          text = "Hermes Online",
          color = Color(0xFF7CE4AA),
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
        )
      }
    }

    Spacer(modifier = Modifier.width(4.dp))

    IconButton(
      onClick = {},
      modifier = Modifier
        .size(36.dp)
        .testTag("settings_button"),
    ) {
      Icon(
        imageVector = Icons.Outlined.Settings,
        contentDescription = "Settings",
        tint = AtlasGold.copy(alpha = 0.9f),
        modifier = Modifier.size(22.dp),
      )
    }
  }
}

@Composable
fun AtlasMetricsRow() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    AtlasMetricCard(
      title = "MODEL",
      subtitle = "Claude 3.5 Sonnet\n(OpenRouter)",
      icon = Icons.Filled.Memory,
      modifier = Modifier.weight(1f),
    )
    AtlasMetricCard(
      title = "PROVIDER",
      subtitle = "OpenRouter Online",
      icon = Icons.Filled.Cloud,
      modifier = Modifier.weight(1f),
    )
    AtlasMetricCard(
      title = "SESSION",
      subtitle = "Active 2h 14m",
      icon = Icons.Filled.Schedule,
      modifier = Modifier.weight(1f),
    )
  }
}

@Composable
fun AtlasMetricCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .height(76.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(AtlasSurfaceCard)
      .border(1.dp, AtlasBorder, RoundedCornerShape(12.dp))
      .padding(horizontal = 9.dp, vertical = 8.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxSize(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = AtlasGoldDark,
        modifier = Modifier.size(20.dp),
      )
      Spacer(modifier = Modifier.width(8.dp))
      Column(verticalArrangement = Arrangement.Center) {
        Text(
          text = title,
          color = AtlasGoldDark,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          color = Color.White.copy(alpha = 0.95f),
          fontSize = 11.5.sp,
          fontWeight = FontWeight.SemiBold,
          lineHeight = 14.sp,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
fun AtlasCenterDashboard(
  leftTools: List<ToolItem>,
  selectedLeftIndex: Int,
  onSelectLeft: (Int) -> Unit,
  statusItems: List<StatusItem>,
  selectedStatusIndex: Int,
  onSelectStatus: (Int) -> Unit,
  orbRotation: Float,
  pulseValue: Float,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(390.dp),
  ) {
    // Center: Animated Golden Rotating Orb and Status text
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      AtlasOrbCanvas(
        rotationAngle = orbRotation,
        pulseValue = pulseValue,
        modifier = Modifier.size(255.dp),
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "READY WHEN YOU ARE",
        color = AtlasGold,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.2.sp,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Speak • Type • Command • Create",
        color = AtlasTextMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
      )
    }

    // Left Tools Column
    Column(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(start = 2.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      leftTools.forEachIndexed { index, tool ->
        val isSelected = selectedLeftIndex == index
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.clickable { onSelectLeft(index) },
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(if (isSelected) Color(0xFF1E1705) else Color.Transparent)
              .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = if (isSelected) AtlasGold else Color(0xFF383120),
                shape = CircleShape,
              ),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = tool.icon,
              contentDescription = tool.name,
              tint = if (isSelected) AtlasGold else Color(0xFF9E8B63),
              modifier = Modifier.size(19.dp),
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = tool.name,
            color = if (isSelected) AtlasGold else Color(0xFF857E70),
            fontSize = 9.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
          )
        }
      }
    }

    // Right Status List Column
    Column(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 4.dp),
      verticalArrangement = Arrangement.spacedBy(7.dp),
      horizontalAlignment = Alignment.Start,
    ) {
      statusItems.forEachIndexed { index, status ->
        val isSelected = selectedStatusIndex == index
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onSelectStatus(index) }
            .padding(vertical = 1.dp),
        ) {
          Icon(
            imageVector = status.icon,
            contentDescription = status.name,
            tint = if (isSelected) AtlasGold else AtlasTextSubtle,
            modifier = Modifier.size(15.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = status.name,
            color = if (isSelected) AtlasGold else AtlasTextSubtle,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 1.sp,
          )
        }
      }
    }
  }
}

@Composable
fun AtlasOrbCanvas(
  rotationAngle: Float,
  pulseValue: Float,
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val maxRadius = size.width / 2f

    // 1. Ambient Background Halo
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          Color(0x44FFC107),
          Color(0x18FFB300),
          Color.Transparent,
        ),
        center = center,
        radius = maxRadius * 1.05f,
      ),
      radius = maxRadius * 1.05f,
      center = center,
    )

    // 2. Rotating Layer (Clockwise)
    rotate(degrees = rotationAngle, pivot = center) {
      // Outer segmented circle
      drawCircle(
        color = Color(0x77FFD54F),
        radius = maxRadius * 0.94f,
        center = center,
        style = Stroke(width = 1.2f),
      )

      // Outer orbital tick marks
      val tickCount = 48
      for (i in 0 until tickCount) {
        val angleRad = (i * 2.0 * Math.PI / tickCount).toFloat()
        val tickLen = if (i % 4 == 0) 7f else 3f
        val rOut = maxRadius * 0.94f
        val rIn = rOut - tickLen
        val p1 = Offset(center.x + cos(angleRad) * rOut, center.y + sin(angleRad) * rOut)
        val p2 = Offset(center.x + cos(angleRad) * rIn, center.y + sin(angleRad) * rIn)
        drawLine(
          color = Color(0x99FFE082),
          start = p1,
          end = p2,
          strokeWidth = 1.4f,
        )
      }

      // Dotted orbital particle ring
      val particleCount = 64
      for (i in 0 until particleCount) {
        val angleRad = (i * 2.0 * Math.PI / particleCount).toFloat()
        val dotRadius = if (i % 6 == 0) 2.2f else 1.1f
        val r = maxRadius * 0.81f
        val pos = Offset(center.x + cos(angleRad) * r, center.y + sin(angleRad) * r)
        drawCircle(
          color = Color(0xCCFFD54F),
          radius = dotRadius,
          center = pos,
        )
      }

      // Mid segmented arcs
      for (i in 0 until 4) {
        val startAngle = i * 90f + 12f
        drawArc(
          color = Color(0xAAFFCA28),
          startAngle = startAngle,
          sweepAngle = 66f,
          useCenter = false,
          topLeft = Offset(center.x - maxRadius * 0.67f, center.y - maxRadius * 0.67f),
          size = Size(maxRadius * 1.34f, maxRadius * 1.34f),
          style = Stroke(width = 1.8f),
        )
      }

      // Inner dotted ring
      val innerDots = 36
      for (i in 0 until innerDots) {
        val angleRad = (i * 2.0 * Math.PI / innerDots).toFloat()
        val r = maxRadius * 0.54f
        val pos = Offset(center.x + cos(angleRad) * r, center.y + sin(angleRad) * r)
        drawCircle(
          color = Color(0xDDFFE082),
          radius = 1.3f,
          center = pos,
        )
      }

      // Radiant energy rays
      val rayCount = 16
      for (i in 0 until rayCount) {
        val angleRad = (i * 2.0 * Math.PI / rayCount).toFloat()
        val r1 = maxRadius * 0.35f
        val r2 = maxRadius * 0.86f
        val p1 = Offset(center.x + cos(angleRad) * r1, center.y + sin(angleRad) * r1)
        val p2 = Offset(center.x + cos(angleRad) * r2, center.y + sin(angleRad) * r2)
        drawLine(
          color = Color(0x33FFE082),
          start = p1,
          end = p2,
          strokeWidth = 1f,
        )
      }
    }

    // 3. Counter-Rotating Parallax Particle Ring
    rotate(degrees = -rotationAngle * 0.65f, pivot = center) {
      val revDots = 32
      for (i in 0 until revDots) {
        val angleRad = (i * 2.0 * Math.PI / revDots).toFloat()
        val r = maxRadius * 0.42f
        val pos = Offset(center.x + cos(angleRad) * r, center.y + sin(angleRad) * r)
        drawCircle(
          color = Color(0x88FFE082),
          radius = 1.2f,
          center = pos,
        )
      }
    }

    // 4. Pulsing Solar Flare Core
    val coreRadius = maxRadius * (0.22f + 0.03f * pulseValue)
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          Color.White,
          AtlasGoldLight,
          AtlasGold,
          Color(0xFFE65100).copy(alpha = 0.5f),
          Color.Transparent,
        ),
        center = center,
        radius = coreRadius * 2.2f,
      ),
      radius = coreRadius * 2.2f,
      center = center,
    )

    // Inner bright hot center
    drawCircle(
      color = Color.White,
      radius = coreRadius * 0.45f,
      center = center,
    )
  }
}

@Composable
fun AtlasLogoCanvas(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val path = Path().apply {
      moveTo(size.width * 0.5f, size.height * 0.04f)
      lineTo(size.width * 0.94f, size.height * 0.94f)
      lineTo(size.width * 0.72f, size.height * 0.94f)
      lineTo(size.width * 0.5f, size.height * 0.46f)
      lineTo(size.width * 0.28f, size.height * 0.94f)
      lineTo(size.width * 0.06f, size.height * 0.94f)
      close()
    }

    drawPath(
      path = path,
      color = Color(0x33FFD043),
    )
    drawPath(
      path = path,
      color = AtlasGold,
      style = Stroke(width = 2.5f, cap = StrokeCap.Round),
    )

    // Inner core triangle
    val innerPath = Path().apply {
      moveTo(size.width * 0.5f, size.height * 0.56f)
      lineTo(size.width * 0.66f, size.height * 0.86f)
      lineTo(size.width * 0.34f, size.height * 0.86f)
      close()
    }
    drawPath(
      path = innerPath,
      color = AtlasGoldLight,
    )
  }
}

@Composable
fun AtlasSearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  onSubmit: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(52.dp)
      .clip(RoundedCornerShape(26.dp))
      .background(Color(0xFF090D15))
      .border(1.2.dp, AtlasBorder, RoundedCornerShape(26.dp))
      .padding(horizontal = 14.dp),
    contentAlignment = Alignment.CenterStart,
  ) {
    Row(
      modifier = Modifier.fillMaxSize(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        imageVector = Icons.Filled.Mic,
        contentDescription = "Microphone",
        tint = AtlasGold,
        modifier = Modifier.size(22.dp),
      )

      Spacer(modifier = Modifier.width(10.dp))

      TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
          .weight(1f)
          .testTag("prompt_input_field"),
        placeholder = {
          Text(
            text = "Ask me anything...",
            color = Color(0xFF867D6F),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
          )
        },
        colors = TextFieldDefaults.colors(
          focusedContainerColor = Color.Transparent,
          unfocusedContainerColor = Color.Transparent,
          disabledContainerColor = Color.Transparent,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White,
        ),
        singleLine = true,
      )

      IconButton(
        onClick = onSubmit,
        modifier = Modifier
          .size(36.dp)
          .testTag("send_prompt_button"),
      ) {
        Icon(
          imageVector = Icons.Filled.Send,
          contentDescription = "Send",
          tint = AtlasGold,
          modifier = Modifier.size(20.dp),
        )
      }
    }
  }
}

@Composable
fun AtlasQuickActionChips(onActionClick: (String) -> Unit) {
  val chips = listOf(
    Pair("Build something", Icons.Filled.ViewInAr),
    Pair("Search the web", Icons.Filled.Search),
    Pair("Run a command", Icons.Filled.Terminal),
    Pair("More", Icons.Filled.GridView),
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    chips.forEach { (label, icon) ->
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(AtlasSurface)
          .border(1.dp, AtlasBorder.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
          .clickable { onActionClick(label) }
          .padding(horizontal = 12.dp, vertical = 8.dp),
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = icon,
            contentDescription = label,
            tint = AtlasGold,
            modifier = Modifier.size(15.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = label,
            color = Color(0xFFE2D6BE),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
          )
        }
      }
    }
  }
}

@Composable
fun AtlasRecentActivityCard(activities: List<ActivityItem>) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(AtlasSurface)
      .border(1.dp, AtlasBorder, RoundedCornerShape(16.dp))
      .padding(14.dp),
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "RECENT ACTIVITY",
          color = AtlasGoldDark,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp,
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
          modifier = Modifier.weight(1f),
          color = AtlasGoldDark.copy(alpha = 0.25f),
          thickness = 1.dp,
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      activities.forEachIndexed { index, activity ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF141923))
              .border(1.dp, Color(0xFF333D4F), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = activity.icon,
              contentDescription = activity.type,
              tint = AtlasGoldDark,
              modifier = Modifier.size(16.dp),
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = activity.type,
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = activity.detail,
              color = AtlasTextMuted,
              fontSize = 11.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }

          Text(
            text = activity.time,
            color = AtlasTextSubtle,
            fontSize = 11.sp,
          )

          Spacer(modifier = Modifier.width(8.dp))

          if (activity.isLoading) {
            CircularProgressIndicator(
              modifier = Modifier.size(16.dp),
              color = Color(0xFF2979FF),
              strokeWidth = 2.dp,
            )
          } else if (activity.isSuccess) {
            Icon(
              imageVector = Icons.Filled.CheckCircle,
              contentDescription = "Success",
              tint = Color(0xFF00E676),
              modifier = Modifier.size(18.dp),
            )
          }
        }

        if (index < activities.size - 1) {
          HorizontalDivider(
            color = Color.White.copy(alpha = 0.08f),
            thickness = 1.dp,
          )
        }
      }
    }
  }
}

@Composable
fun AtlasBottomNavigationBar(
  selectedIndex: Int,
  onSelect: (Int) -> Unit,
) {
  val items = listOf(
    Pair("Home", Icons.Filled.Home),
    Pair("Chat", Icons.Outlined.ChatBubbleOutline),
    Pair("History", Icons.Filled.History),
    Pair("Profile", Icons.Outlined.Person),
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(AtlasBackground)
      .navigationBarsPadding(),
  ) {
    HorizontalDivider(
      color = AtlasBorder.copy(alpha = 0.25f),
      thickness = 1.dp,
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, bottom = 4.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items.forEachIndexed { index, (label, icon) ->
        val isSelected = selectedIndex == index
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onSelect(index) }
            .padding(horizontal = 12.dp, vertical = 2.dp),
        ) {
          Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) AtlasGold else AtlasTextSubtle,
            modifier = Modifier.size(24.dp),
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = label,
            color = if (isSelected) AtlasGold else AtlasTextSubtle,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
          )
          Spacer(modifier = Modifier.height(2.dp))
          if (isSelected) {
            Box(
              modifier = Modifier
                .width(14.dp)
                .height(2.dp)
                .background(AtlasGold, RoundedCornerShape(1.dp)),
            )
          } else {
            Spacer(modifier = Modifier.height(2.dp))
          }
        }
      }
    }

    // Home indicator bar
    Box(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(vertical = 4.dp)
        .width(110.dp)
        .height(3.5.dp)
        .background(Color.White.copy(alpha = 0.35f), RoundedCornerShape(2.dp)),
    )
  }
}

// Keep Greeting composable for Robolectric tests / GreetingScreenshotTest
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}
