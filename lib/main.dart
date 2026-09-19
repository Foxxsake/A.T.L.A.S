import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

void main() {
  runApp(const AtlasApp());
}

class AtlasApp extends StatelessWidget {
  const AtlasApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'A.T.L.A.S.',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        brightness: Brightness.dark,
        scaffoldBackgroundColor: const Color(0xFF05070A),
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFFFFC837),
          surface: Color(0xFF0B0F17),
          background: Color(0xFF05070A),
        ),
        textTheme: GoogleFonts.rajdhaniTextTheme(ThemeData.dark().textTheme),
      ),
      home: const AtlasDashboardScreen(),
    );
  }
}

class AtlasDashboardScreen extends StatefulWidget {
  const AtlasDashboardScreen({super.key});

  @override
  State<AtlasDashboardScreen> createState() => _AtlasDashboardScreenState();
}

class _AtlasDashboardScreenState extends State<AtlasDashboardScreen>
    with TickerProviderStateMixin {
  late AnimationController _orbRotationController;
  late AnimationController _pulseController;
  late AnimationController _spinnerController;

  int _selectedLeftIndex = 0;
  int _selectedStatusIndex = 0;
  int _bottomNavIndex = 0;

  final TextEditingController _promptController = TextEditingController();

  final List<Map<String, dynamic>> _leftTools = [
    {'name': 'Chat', 'icon': Icons.chat_bubble_rounded},
    {'name': 'Terminal', 'icon': Icons.terminal_rounded},
    {'name': 'Files', 'icon': Icons.folder_rounded},
    {'name': 'Tools', 'icon': Icons.build_rounded},
    {'name': 'Sessions', 'icon': Icons.access_time_rounded},
    {'name': 'Settings', 'icon': Icons.settings_rounded},
  ];

  final List<Map<String, dynamic>> _statusItems = [
    {'name': 'IDLE', 'icon': Icons.adjust_rounded},
    {'name': 'LISTENING', 'icon': Icons.graphic_eq_rounded},
    {'name': 'THINKING', 'icon': Icons.psychology_rounded},
    {'name': 'EXECUTING', 'icon': Icons.play_arrow_outlined},
    {'name': 'SPEAKING', 'icon': Icons.volume_up_outlined},
    {'name': 'APPROVAL', 'icon': Icons.verified_user_outlined},
    {'name': 'ERROR', 'icon': Icons.cancel_outlined},
    {'name': 'OFFLINE', 'icon': Icons.cloud_off_outlined},
  ];

  final List<Map<String, dynamic>> _recentActivities = [
    {
      'type': 'Terminal',
      'detail': 'npm run dev',
      'time': '2m ago',
      'icon': Icons.terminal_rounded,
      'isSuccess': true,
      'isLoading': false,
    },
    {
      'type': 'File Operation',
      'detail': 'Created: src/components/QuantumCore.tsx',
      'time': '5m ago',
      'icon': Icons.folder_rounded,
      'isSuccess': true,
      'isLoading': false,
    },
    {
      'type': 'Thinking',
      'detail': 'Planning next steps...',
      'time': '7m ago',
      'icon': Icons.psychology_rounded,
      'isSuccess': false,
      'isLoading': true,
    },
  ];

  @override
  void initState() {
    super.initState();
    _orbRotationController = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 24),
    )..repeat();

    _pulseController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 2200),
    )..repeat(reverse: true);

    _spinnerController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1400),
    )..repeat();
  }

  @override
  void dispose() {
    _orbRotationController.dispose();
    _pulseController.dispose();
    _spinnerController.dispose();
    _promptController.dispose();
    super.dispose();
  }

  void _submitPrompt([String? text]) {
    final query = text ?? _promptController.text.trim();
    if (query.isEmpty) return;

    setState(() {
      _recentActivities.insert(0, {
        'type': 'Command',
        'detail': query,
        'time': 'Just now',
        'icon': Icons.flash_on_rounded,
        'isSuccess': true,
        'isLoading': false,
      });
      _promptController.clear();
      _selectedStatusIndex = 3; // EXECUTING
    });
  }

  @override
  Widget build(BuildContext context) {
    const goldPrimary = Color(0xFFFFD043);
    const goldAccent = Color(0xFFF3BA2F);
    const darkCard = Color(0xFF0A0E17);
    const goldBorder = Color(0x55E5A93C);

    return Scaffold(
      backgroundColor: const Color(0xFF05070A),
      body: SafeArea(
        bottom: false,
        child: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                physics: const BouncingScrollPhysics(),
                padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Top Bar
                    _buildTopHeader(goldPrimary),
                    const SizedBox(height: 14),

                    // 3 Metric Cards: MODEL, PROVIDER, SESSION
                    _buildMetricsRow(darkCard, goldBorder, goldAccent),
                    const SizedBox(height: 16),

                    // Main Interactive Section: Left Tools, Center Orb, Right Status
                    _buildCoreDashboard(goldPrimary, goldAccent),
                    const SizedBox(height: 16),

                    // Search / Prompt Bar
                    _buildSearchBar(goldPrimary, goldBorder),
                    const SizedBox(height: 12),

                    // 4 Quick Action Buttons
                    _buildActionChips(darkCard, goldBorder, goldPrimary),
                    const SizedBox(height: 16),

                    // Recent Activity Panel
                    _buildRecentActivitySection(darkCard, goldBorder, goldAccent),
                    const SizedBox(height: 20),
                  ],
                ),
              ),
            ),

            // Bottom Navigation Bar
            _buildBottomNavigationBar(goldPrimary),
          ],
        ),
      ),
    );
  }

  Widget _buildTopHeader(Color goldPrimary) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // Stylized "A" Logo
        CustomPaint(
          size: const Size(38, 42),
          painter: AtlasLogoPainter(),
        ),
        const SizedBox(width: 10),

        // Title and Subtitle
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'A.T.L.A.S.',
                style: GoogleFonts.orbitron(
                  fontSize: 22,
                  fontWeight: FontWeight.w900,
                  letterSpacing: 3.0,
                  color: goldPrimary,
                  shadows: [
                    Shadow(
                      color: goldPrimary.withOpacity(0.55),
                      blurRadius: 10,
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 1),
              Text(
                'ADVANCED TACTICAL LOGIC & ASSISTANCE SYSTEM',
                style: GoogleFonts.rajdhani(
                  fontSize: 8.5,
                  fontWeight: FontWeight.w700,
                  letterSpacing: 1.1,
                  color: const Color(0xFFC7A868),
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
        ),

        // Connected Badge
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
          decoration: BoxDecoration(
            color: const Color(0xFF071B12),
            borderRadius: BorderRadius.circular(16),
            border: Border.all(
              color: const Color(0xFF00FF7F).withOpacity(0.35),
              width: 1.0,
            ),
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                width: 7,
                height: 7,
                decoration: const BoxDecoration(
                  color: Color(0xFF00FF7F),
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: Color(0xFF00FF7F),
                      blurRadius: 6,
                      spreadRadius: 1,
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 6),
              RichText(
                text: TextSpan(
                  style: GoogleFonts.rajdhani(fontSize: 11, fontWeight: FontWeight.bold),
                  children: const [
                    TextSpan(
                      text: 'CONNECTED',
                      style: TextStyle(color: Color(0xFF00FF7F), letterSpacing: 0.8),
                    ),
                    TextSpan(
                      text: ' • ',
                      style: TextStyle(color: Color(0xFF00FF7F)),
                    ),
                    TextSpan(
                      text: 'Hermes Online',
                      style: TextStyle(color: Color(0xFF7CE4AA)),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
        const SizedBox(width: 8),

        // Settings Icon
        IconButton(
          constraints: const BoxConstraints(minWidth: 36, minHeight: 36),
          padding: EdgeInsets.zero,
          icon: Icon(
            Icons.settings_outlined,
            color: goldPrimary.withOpacity(0.85),
            size: 22,
          ),
          onPressed: () {},
        ),
      ],
    );
  }

  Widget _buildMetricsRow(Color darkCard, Color goldBorder, Color goldAccent) {
    return Row(
      children: [
        Expanded(
          child: _buildMetricCard(
            title: 'MODEL',
            subtitle: 'Claude 3.5 Sonnet\n(OpenRouter)',
            icon: Icons.memory_rounded,
            darkCard: darkCard,
            goldBorder: goldBorder,
            goldAccent: goldAccent,
          ),
        ),
        const SizedBox(width: 8),
        Expanded(
          child: _buildMetricCard(
            title: 'PROVIDER',
            subtitle: 'OpenRouter Online',
            icon: Icons.cloud_outlined,
            darkCard: darkCard,
            goldBorder: goldBorder,
            goldAccent: goldAccent,
          ),
        ),
        const SizedBox(width: 8),
        Expanded(
          child: _buildMetricCard(
            title: 'SESSION',
            subtitle: 'Active 2h 14m',
            icon: Icons.access_time_rounded,
            darkCard: darkCard,
            goldBorder: goldBorder,
            goldAccent: goldAccent,
          ),
        ),
      ],
    );
  }

  Widget _buildMetricCard({
    required String title,
    required String subtitle,
    required IconData icon,
    required Color darkCard,
    required Color goldBorder,
    required Color goldAccent,
  }) {
    return Container(
      height: 74,
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
      decoration: BoxDecoration(
        color: darkCard,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: goldBorder, width: 1.0),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFFE5A93C).withOpacity(0.08),
            blurRadius: 10,
            spreadRadius: 1,
          ),
        ],
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: goldAccent, size: 20),
          const SizedBox(width: 8),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  title,
                  style: GoogleFonts.rajdhani(
                    fontSize: 10,
                    fontWeight: FontWeight.w700,
                    letterSpacing: 1.1,
                    color: const Color(0xFFD4AF37),
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  subtitle,
                  style: GoogleFonts.rajdhani(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    height: 1.15,
                    color: Colors.white.withOpacity(0.95),
                  ),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCoreDashboard(Color goldPrimary, Color goldAccent) {
    return SizedBox(
      height: 380,
      child: Stack(
        children: [
          // Center Animated Orb & Readiness Headline
          Positioned.fill(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                AnimatedBuilder(
                  animation: Listenable.merge([_orbRotationController, _pulseController]),
                  builder: (context, child) {
                    return SizedBox(
                      width: 250,
                      height: 250,
                      child: CustomPaint(
                        painter: AtlasOrbPainter(
                          rotationAngle: _orbRotationController.value * 2 * math.pi,
                          pulseValue: _pulseController.value,
                        ),
                      ),
                    );
                  },
                ),
                const SizedBox(height: 16),
                Text(
                  'READY WHEN YOU ARE',
                  style: GoogleFonts.orbitron(
                    fontSize: 14.5,
                    fontWeight: FontWeight.bold,
                    letterSpacing: 2.2,
                    color: goldPrimary,
                    shadows: [
                      Shadow(
                        color: goldPrimary.withOpacity(0.4),
                        blurRadius: 8,
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  'Speak • Type • Command • Create',
                  style: GoogleFonts.rajdhani(
                    fontSize: 12,
                    fontWeight: FontWeight.w500,
                    letterSpacing: 1.0,
                    color: const Color(0xFF8B9BB4),
                  ),
                ),
              ],
            ),
          ),

          // Left Tool Column
          Positioned(
            left: 0,
            top: 10,
            bottom: 30,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: List.generate(_leftTools.length, (index) {
                final tool = _leftTools[index];
                final isSelected = _selectedLeftIndex == index;
                return GestureDetector(
                  onTap: () {
                    setState(() {
                      _selectedLeftIndex = index;
                    });
                  },
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        width: 44,
                        height: 44,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: isSelected
                              ? const Color(0xFF1E1705)
                              : Colors.transparent,
                          border: Border.all(
                            color: isSelected
                                ? goldPrimary
                                : const Color(0xFF423B2A),
                            width: isSelected ? 1.8 : 1.0,
                          ),
                          boxShadow: isSelected
                              ? [
                                  BoxShadow(
                                    color: goldPrimary.withOpacity(0.35),
                                    blurRadius: 10,
                                    spreadRadius: 1,
                                  ),
                                ]
                              : [],
                        ),
                        child: Icon(
                          tool['icon'] as IconData,
                          size: 20,
                          color: isSelected ? goldPrimary : const Color(0xFF9E8B63),
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        tool['name'] as String,
                        style: GoogleFonts.rajdhani(
                          fontSize: 10,
                          fontWeight: isSelected ? FontWeight.bold : FontWeight.w500,
                          color: isSelected ? goldPrimary : const Color(0xFF857E70),
                        ),
                      ),
                    ],
                  ),
                );
              }),
            ),
          ),

          // Right Status Column
          Positioned(
            right: 0,
            top: 12,
            bottom: 40,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: List.generate(_statusItems.length, (index) {
                final item = _statusItems[index];
                final isSelected = _selectedStatusIndex == index;
                return GestureDetector(
                  onTap: () {
                    setState(() {
                      _selectedStatusIndex = index;
                    });
                  },
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 2.5),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Icon(
                          item['icon'] as IconData,
                          size: 16,
                          color: isSelected ? goldPrimary : const Color(0xFF7A8393),
                        ),
                        const SizedBox(width: 6),
                        Text(
                          item['name'] as String,
                          style: GoogleFonts.rajdhani(
                            fontSize: 11,
                            fontWeight: isSelected ? FontWeight.bold : FontWeight.w600,
                            letterSpacing: 1.0,
                            color: isSelected ? goldPrimary : const Color(0xFF7A8393),
                            shadows: isSelected
                                ? [
                                    Shadow(
                                      color: goldPrimary.withOpacity(0.5),
                                      blurRadius: 8,
                                    ),
                                  ]
                                : null,
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              }),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSearchBar(Color goldPrimary, Color goldBorder) {
    return Container(
      height: 52,
      padding: const EdgeInsets.symmetric(horizontal: 14),
      decoration: BoxDecoration(
        color: const Color(0xFF090D15),
        borderRadius: BorderRadius.circular(26),
        border: Border.all(color: goldBorder, width: 1.2),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFFE5A93C).withOpacity(0.12),
            blurRadius: 12,
            spreadRadius: 1,
          ),
        ],
      ),
      child: Row(
        children: [
          Icon(Icons.mic_none_rounded, color: goldPrimary, size: 22),
          const SizedBox(width: 10),
          Expanded(
            child: TextField(
              controller: _promptController,
              onSubmitted: _submitPrompt,
              style: GoogleFonts.rajdhani(
                color: Colors.white,
                fontSize: 14,
                fontWeight: FontWeight.w600,
              ),
              decoration: InputDecoration(
                hintText: 'Ask me anything...',
                hintStyle: GoogleFonts.rajdhani(
                  color: const Color(0xFF867D6F),
                  fontSize: 14,
                  fontWeight: FontWeight.w500,
                ),
                border: InputBorder.none,
                isDense: true,
              ),
            ),
          ),
          IconButton(
            onPressed: _submitPrompt,
            icon: Icon(Icons.send_rounded, color: goldPrimary, size: 22),
          ),
        ],
      ),
    );
  }

  Widget _buildActionChips(Color darkCard, Color goldBorder, Color goldPrimary) {
    final actions = [
      {'label': 'Build something', 'icon': Icons.view_in_ar_rounded},
      {'label': 'Search the web', 'icon': Icons.search_rounded},
      {'label': 'Run a command', 'icon': Icons.terminal_rounded},
      {'label': 'More', 'icon': Icons.grid_view_rounded},
    ];

    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      physics: const BouncingScrollPhysics(),
      child: Row(
        children: actions.map((item) {
          return Padding(
            padding: const EdgeInsets.only(right: 8),
            child: InkWell(
              onTap: () => _submitPrompt(item['label'] as String),
              borderRadius: BorderRadius.circular(20),
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                decoration: BoxDecoration(
                  color: darkCard,
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: goldBorder.withOpacity(0.4), width: 1.0),
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(item['icon'] as IconData, color: goldPrimary, size: 15),
                    const SizedBox(width: 6),
                    Text(
                      item['label'] as String,
                      style: GoogleFonts.rajdhani(
                        fontSize: 11.5,
                        fontWeight: FontWeight.w600,
                        color: const Color(0xFFE2D6BE),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          );
        }).toList(),
      ),
    );
  }

  Widget _buildRecentActivitySection(Color darkCard, Color goldBorder, Color goldAccent) {
    return Container(
      decoration: BoxDecoration(
        color: darkCard,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: goldBorder, width: 1.0),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFFE5A93C).withOpacity(0.08),
            blurRadius: 10,
          ),
        ],
      ),
      padding: const EdgeInsets.all(14),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Text(
                'RECENT ACTIVITY',
                style: GoogleFonts.rajdhani(
                  fontSize: 11,
                  fontWeight: FontWeight.w700,
                  letterSpacing: 1.5,
                  color: const Color(0xFFD4AF37),
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Container(
                  height: 1,
                  color: const Color(0x33D4AF37),
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          ..._recentActivities.asMap().entries.map((entry) {
            final index = entry.key;
            final item = entry.value;
            final isLast = index == _recentActivities.length - 1;

            return Column(
              children: [
                Padding(
                  padding: const EdgeInsets.symmetric(vertical: 7),
                  child: Row(
                    children: [
                      Container(
                        width: 32,
                        height: 32,
                        decoration: BoxDecoration(
                          color: const Color(0xFF141923),
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(
                            color: const Color(0xFF333D4F),
                            width: 1.0,
                          ),
                        ),
                        child: Icon(
                          item['icon'] as IconData,
                          size: 16,
                          color: goldAccent,
                        ),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              item['type'] as String,
                              style: GoogleFonts.rajdhani(
                                fontSize: 13,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                            ),
                            Text(
                              item['detail'] as String,
                              style: GoogleFonts.rajdhani(
                                fontSize: 11,
                                fontWeight: FontWeight.w500,
                                color: const Color(0xFF8E9BAF),
                              ),
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                            ),
                          ],
                        ),
                      ),
                      Text(
                        item['time'] as String,
                        style: GoogleFonts.rajdhani(
                          fontSize: 11,
                          color: const Color(0xFF7A8393),
                        ),
                      ),
                      const SizedBox(width: 8),
                      if (item['isLoading'] == true)
                        RotationTransition(
                          turns: _spinnerController,
                          child: const SizedBox(
                            width: 16,
                            height: 16,
                            child: CircularProgressIndicator(
                              strokeWidth: 2.0,
                              color: Color(0xFF2979FF),
                            ),
                          ),
                        )
                      else if (item['isSuccess'] == true)
                        const Icon(
                          Icons.check_circle_rounded,
                          size: 18,
                          color: Color(0xFF00E676),
                        ),
                    ],
                  ),
                ),
                if (!isLast)
                  const Divider(
                    color: Color(0x1AFFFFFF),
                    height: 1,
                  ),
              ],
            );
          }),
        ],
      ),
    );
  }

  Widget _buildBottomNavigationBar(Color goldPrimary) {
    final navItems = [
      {'label': 'Home', 'icon': Icons.home_rounded},
      {'label': 'Chat', 'icon': Icons.chat_bubble_outline_rounded},
      {'label': 'History', 'icon': Icons.history_rounded},
      {'label': 'Profile', 'icon': Icons.person_outline_rounded},
    ];

    return Container(
      padding: const EdgeInsets.only(top: 8, bottom: 12),
      decoration: const BoxDecoration(
        color: Color(0xFF05070A),
        border: Border(
          top: BorderSide(color: Color(0x22D4AF37), width: 1.0),
        ),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: List.generate(navItems.length, (index) {
              final item = navItems[index];
              final isSelected = _bottomNavIndex == index;
              return GestureDetector(
                onTap: () {
                  setState(() {
                    _bottomNavIndex = index;
                  });
                },
                behavior: HitTestBehavior.opaque,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(
                      item['icon'] as IconData,
                      size: 24,
                      color: isSelected ? goldPrimary : const Color(0xFF6E788A),
                    ),
                    const SizedBox(height: 3),
                    Text(
                      item['label'] as String,
                      style: GoogleFonts.rajdhani(
                        fontSize: 11,
                        fontWeight: isSelected ? FontWeight.bold : FontWeight.w500,
                        color: isSelected ? goldPrimary : const Color(0xFF6E788A),
                      ),
                    ),
                    const SizedBox(height: 2),
                    if (isSelected)
                      Container(
                        width: 14,
                        height: 2,
                        decoration: BoxDecoration(
                          color: goldPrimary,
                          borderRadius: BorderRadius.circular(1),
                        ),
                      )
                    else
                      const SizedBox(height: 2),
                  ],
                ),
              );
            }),
          ),
          const SizedBox(height: 6),
          // iOS / Android Home Indicator line
          Container(
            width: 110,
            height: 3.5,
            decoration: BoxDecoration(
              color: const Color(0x55FFFFFF),
              borderRadius: BorderRadius.circular(2),
            ),
          ),
        ],
      ),
    );
  }
}

/// Custom Painter for the Stylized Sci-Fi "A" Chevron Logo
class AtlasLogoPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = const Color(0xFFFFD043)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 2.5
      ..strokeJoin = StrokeJoin.miter;

    final fillPaint = Paint()
      ..color = const Color(0x33FFD043)
      ..style = PaintingStyle.fill;

    final path = Path();
    // Outer "A" chevron
    path.moveTo(size.width * 0.5, size.height * 0.05);
    path.lineTo(size.width * 0.92, size.height * 0.95);
    path.lineTo(size.width * 0.72, size.height * 0.95);
    path.lineTo(size.width * 0.5, size.height * 0.45);
    path.lineTo(size.width * 0.28, size.height * 0.95);
    path.lineTo(size.width * 0.08, size.height * 0.95);
    path.close();

    canvas.drawPath(path, fillPaint);
    canvas.drawPath(path, paint);

    // Inner cross / core chevron
    final innerPath = Path();
    innerPath.moveTo(size.width * 0.5, size.height * 0.55);
    innerPath.lineTo(size.width * 0.65, size.height * 0.85);
    innerPath.lineTo(size.width * 0.35, size.height * 0.85);
    innerPath.close();

    final innerPaint = Paint()
      ..color = const Color(0xFFFFEA7A)
      ..style = PaintingStyle.fill;
    canvas.drawPath(innerPath, innerPaint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

/// Custom Painter for the Rotating Glowing Golden Sci-Fi Orb
class AtlasOrbPainter extends CustomPainter {
  final double rotationAngle;
  final double pulseValue;

  AtlasOrbPainter({
    required this.rotationAngle,
    required this.pulseValue,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final maxRadius = size.width / 2;

    // Deep ambient golden aura behind everything
    final ambientPaint = Paint()
      ..shader = RadialGradient(
        colors: [
          const Color(0x44FFC107),
          const Color(0x1AFFB300),
          Colors.transparent,
        ],
        stops: const [0.0, 0.45, 1.0],
      ).createShader(Rect.fromCircle(center: center, radius: maxRadius * 1.1));
    canvas.drawCircle(center, maxRadius * 1.1, ambientPaint);

    // Save canvas for rotating elements
    canvas.save();
    canvas.translate(center.dx, center.dy);
    canvas.rotate(rotationAngle);

    // Ring 1: Outer glowing segmented orbit with ticks
    final outerRingPaint = Paint()
      ..color = const Color(0x77FFD54F)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 1.0;
    canvas.drawCircle(Offset.zero, maxRadius * 0.95, outerRingPaint);

    // Draw orbital tick marks along outer ring
    final tickPaint = Paint()
      ..color = const Color(0x99FFE082)
      ..strokeWidth = 1.2;
    const tickCount = 48;
    for (int i = 0; i < tickCount; i++) {
      final angle = (i * 2 * math.pi) / tickCount;
      final tickLen = (i % 4 == 0) ? 6.0 : 2.5;
      final p1 = Offset(math.cos(angle) * (maxRadius * 0.95), math.sin(angle) * (maxRadius * 0.95));
      final p2 = Offset(math.cos(angle) * (maxRadius * 0.95 - tickLen), math.sin(angle) * (maxRadius * 0.95 - tickLen));
      canvas.drawLine(p1, p2, tickPaint);
    }

    // Ring 2: Dotted particle orbit ring
    final particlePaint = Paint()
      ..color = const Color(0xCCFFD54F)
      ..style = PaintingStyle.fill;
    const particleCount = 72;
    for (int i = 0; i < particleCount; i++) {
      final angle = (i * 2 * math.pi) / particleCount;
      final dotRadius = (i % 6 == 0) ? 1.8 : 0.9;
      final pos = Offset(
        math.cos(angle) * (maxRadius * 0.82),
        math.sin(angle) * (maxRadius * 0.82),
      );
      canvas.drawCircle(pos, dotRadius, particlePaint);
    }

    // Ring 3: Mid dense tech ring with dash arcs
    final midRingPaint = Paint()
      ..color = const Color(0xAAFFCA28)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 1.5;
    for (int i = 0; i < 4; i++) {
      final startAngle = (i * math.pi / 2) + 0.15;
      canvas.drawArc(
        Rect.fromCircle(center: Offset.zero, radius: maxRadius * 0.68),
        startAngle,
        math.pi / 2 - 0.3,
        false,
        midRingPaint,
      );
    }

    // Concentric dotted inner ring
    const innerDotCount = 40;
    for (int i = 0; i < innerDotCount; i++) {
      final angle = (i * 2 * math.pi) / innerDotCount;
      final pos = Offset(
        math.cos(angle) * (maxRadius * 0.54),
        math.sin(angle) * (maxRadius * 0.54),
      );
      canvas.drawCircle(pos, 1.2, particlePaint);
    }

    // Radial radiant spikes / energy beams
    final rayPaint = Paint()
      ..color = const Color(0x33FFE082)
      ..strokeWidth = 0.9;
    const rayCount = 16;
    for (int i = 0; i < rayCount; i++) {
      final angle = (i * 2 * math.pi) / rayCount;
      final p1 = Offset(math.cos(angle) * (maxRadius * 0.35), math.sin(angle) * (maxRadius * 0.35));
      final p2 = Offset(math.cos(angle) * (maxRadius * 0.88), math.sin(angle) * (maxRadius * 0.88));
      canvas.drawLine(p1, p2, rayPaint);
    }

    canvas.restore();

    // Ring 4: Reverse rotating fine particle orbit for celestial parallax effect
    canvas.save();
    canvas.translate(center.dx, center.dy);
    canvas.rotate(-rotationAngle * 0.7);

    final reverseDotPaint = Paint()
      ..color = const Color(0x88FFE082)
      ..style = PaintingStyle.fill;
    const reverseDotCount = 36;
    for (int i = 0; i < reverseDotCount; i++) {
      final angle = (i * 2 * math.pi) / reverseDotCount;
      final pos = Offset(
        math.cos(angle) * (maxRadius * 0.42),
        math.sin(angle) * (maxRadius * 0.42),
      );
      canvas.drawCircle(pos, 0.9, reverseDotPaint);
    }
    canvas.restore();

    // Central Core: Golden solar flare glowing orb with pulsating intensity
    final coreRadius = maxRadius * (0.22 + 0.03 * pulseValue);
    final coreGlowPaint = Paint()
      ..shader = RadialGradient(
        colors: [
          const Color(0xFFFFFFFF),
          const Color(0xFFFFEA7A),
          const Color(0xFFFFB300),
          const Color(0xFFE65100).withOpacity(0.4),
          Colors.transparent,
        ],
        stops: const [0.0, 0.25, 0.6, 0.85, 1.0],
      ).createShader(Rect.fromCircle(center: center, radius: coreRadius * 2.2));

    canvas.drawCircle(center, coreRadius * 2.2, coreGlowPaint);

    // Inner bright hot core
    final hotCorePaint = Paint()
      ..color = Colors.white
      ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 4.0);
    canvas.drawCircle(center, coreRadius * 0.4, hotCorePaint);
  }

  @override
  bool shouldRepaint(covariant AtlasOrbPainter oldDelegate) {
    return oldDelegate.rotationAngle != rotationAngle ||
        oldDelegate.pulseValue != pulseValue;
  }
}
