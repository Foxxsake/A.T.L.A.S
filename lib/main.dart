import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'dart:math' as math;

void main() {
  SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
    statusBarColor: Colors.transparent,
    statusBarIconBrightness: Brightness.light,
  ));
  runApp(const AtlasApp());
}

class AtlasApp extends StatelessWidget {
  const AtlasApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'A.T.L.A.S.',
      debugShowCheckedModeBanner: false,
      theme: ThemeData.dark().copyWith(
        scaffoldBackgroundColor: const Color(0xFF05070A),
        textTheme: GoogleFonts.interTextTheme(ThemeData.dark().textTheme),
      ),
      home: const AtlasDashboard(),
    );
  }
}

class AtlasDashboard extends StatefulWidget {
  const AtlasDashboard({super.key});

  @override
  State<AtlasDashboard> createState() => _AtlasDashboardState();
}

class _AtlasDashboardState extends State<AtlasDashboard>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 18),
    )..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF05070A),
      body: SafeArea(
        child: Column(
          children: [
            _buildHeader(),
            const SizedBox(height: 10),
            _buildInfoCards(),
            const SizedBox(height: 6),
            Expanded(
              child: Row(
                children: [
                  _buildLeftNav(),
                  Expanded(child: _buildCenter()),
                  _buildStatusList(),
                ],
              ),
            ),
            _buildInputArea(),
            const SizedBox(height: 8),
            _buildRecentActivity(),
            const SizedBox(height: 6),
            _buildBottomNav(),
          ],
        ),
      ),
    );
  }

  // ==================== HEADER ====================
  Widget _buildHeader() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(14, 8, 14, 0),
      child: Row(
        children: [
          Row(
            children: [
              Text(
                'A',
                style: GoogleFonts.inter(
                  fontSize: 26,
                  fontWeight: FontWeight.w800,
                  color: const Color(0xFFFFB800),
                ),
              ),
              const SizedBox(width: 6),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'A.T.L.A.S.',
                    style: GoogleFonts.inter(
                      fontSize: 18,
                      fontWeight: FontWeight.w700,
                      color: const Color(0xFFFFB800),
                      letterSpacing: 1,
                    ),
                  ),
                  Text(
                    'ADVANCED TACTICAL LOGIC & ASSISTANCE SYSTEM',
                    style: GoogleFonts.inter(
                      fontSize: 7,
                      color: Colors.white54,
                      letterSpacing: 0.5,
                    ),
                  ),
                ],
              ),
            ],
          ),
          const Spacer(),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 4),
            decoration: BoxDecoration(
              color: const Color(0xFF0A1A0A),
              borderRadius: BorderRadius.circular(20),
              border: Border.all(color: const Color(0xFF00E676).withOpacity(0.45)),
            ),
            child: Row(
              children: [
                Container(
                  width: 6,
                  height: 6,
                  decoration: const BoxDecoration(
                    color: Color(0xFF00E676),
                    shape: BoxShape.circle,
                  ),
                ),
                const SizedBox(width: 5),
                Text(
                  'CONNECTED • Hermes Online',
                  style: GoogleFonts.inter(
                    fontSize: 9.5,
                    fontWeight: FontWeight.w600,
                    color: const Color(0xFF00E676),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(width: 8),
          const Icon(Icons.settings, size: 19, color: Color(0xFFFFB800)),
        ],
      ),
    );
  }

  // ==================== INFO CARDS ====================
  Widget _buildInfoCards() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12),
      child: Row(
        children: [
          _infoCard(Icons.memory, 'MODEL', 'Claude 3.5 Sonnet', '(OpenRouter)'),
          const SizedBox(width: 7),
          _infoCard(Icons.cloud_outlined, 'PROVIDER', 'OpenRouter Online', null),
          const SizedBox(width: 7),
          _infoCard(Icons.timer_outlined, 'SESSION', 'Active 2h 14m', null),
        ],
      ),
    );
  }

  Widget _infoCard(IconData icon, String title, String value, String? sub) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 9, horizontal: 4),
        decoration: BoxDecoration(
          color: const Color(0xFF0A0E14),
          borderRadius: BorderRadius.circular(11),
          border: Border.all(color: const Color(0xFFFFB800).withOpacity(0.45)),
        ),
        child: Column(
          children: [
            Icon(icon, size: 15, color: const Color(0xFFFFB800)),
            const SizedBox(height: 3),
            Text(title, style: const TextStyle(fontSize: 8.5, color: Colors.white54)),
            Text(
              value,
              textAlign: TextAlign.center,
              style: const TextStyle(fontSize: 10.5, fontWeight: FontWeight.w600, color: Colors.white),
            ),
            if (sub != null)
              Text(sub, style: const TextStyle(fontSize: 8.5, color: Color(0xFFFFB800))),
          ],
        ),
      ),
    );
  }

  // ==================== LEFT NAV ====================
  Widget _buildLeftNav() {
    final items = [
      (Icons.chat_bubble, 'Chat', true),
      (Icons.terminal, 'Terminal', false),
      (Icons.folder_outlined, 'Files', false),
      (Icons.build_outlined, 'Tools', false),
      (Icons.layers_outlined, 'Sessions', false),
      (Icons.settings_outlined, 'Settings', false),
    ];

    return SizedBox(
      width: 68,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: items.map((item) {
          final active = item.$3;
          return Padding(
            padding: const EdgeInsets.symmetric(vertical: 7),
            child: Column(
              children: [
                Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: active ? const Color(0xFFFFB800).withOpacity(0.15) : Colors.transparent,
                    border: active ? Border.all(color: const Color(0xFFFFB800), width: 1.4) : null,
                  ),
                  child: Icon(item.$1, size: 18, color: active ? const Color(0xFFFFB800) : Colors.white38),
                ),
                const SizedBox(height: 2),
                Text(
                  item.$2,
                  style: TextStyle(
                    fontSize: 8.5,
                    color: active ? const Color(0xFFFFB800) : Colors.white38,
                  ),
                ),
              ],
            ),
          );
        }).toList(),
      ),
    );
  }

  // ==================== CENTER ORB ====================
  Widget _buildCenter() {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        AnimatedBuilder(
          animation: _controller,
          builder: (_, __) {
            return Transform.rotate(
              angle: _controller.value * 2 * math.pi,
              child: Container(
                width: 195,
                height: 195,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: const Color(0xFFFFB800).withOpacity(0.35),
                      blurRadius: 35,
                      spreadRadius: 4,
                    ),
                  ],
                ),
                child: CustomPaint(painter: OrbPainter()),
              ),
            );
          },
        ),
        const SizedBox(height: 14),
        Text(
          'READY WHEN YOU ARE',
          style: GoogleFonts.inter(
            fontSize: 12.5,
            fontWeight: FontWeight.w600,
            color: const Color(0xFFFFB800),
            letterSpacing: 1,
          ),
        ),
        const SizedBox(height: 3),
        Text(
          'Speak · Type · Command · Create',
          style: GoogleFonts.inter(fontSize: 10.5, color: Colors.white54),
        ),
      ],
    );
  }

  // ==================== STATUS LIST ====================
  Widget _buildStatusList() {
    final list = [
      (Icons.radio_button_checked, 'IDLE', true),
      (Icons.mic, 'LISTENING', false),
      (Icons.psychology, 'THINKING', false),
      (Icons.play_arrow, 'EXECUTING', false),
      (Icons.volume_up, 'SPEAKING', false),
      (Icons.verified_user, 'APPROVAL', false),
      (Icons.error_outline, 'ERROR', false),
      (Icons.cloud_off, 'OFFLINE', false),
    ];

    return SizedBox(
      width: 92,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: list.map((s) {
          final active = s.$3;
          return Padding(
            padding: const EdgeInsets.symmetric(vertical: 4.5),
            child: Row(
              children: [
                Icon(s.$1, size: 14, color: active ? const Color(0xFFFFB800) : Colors.white38),
                const SizedBox(width: 5),
                Text(
                  s.$2,
                  style: TextStyle(
                    fontSize: 9.5,
                    color: active ? const Color(0xFFFFB800) : Colors.white38,
                    fontWeight: active ? FontWeight.w600 : FontWeight.normal,
                  ),
                ),
              ],
            ),
          );
        }).toList(),
      ),
    );
  }

  // ==================== INPUT ====================
  Widget _buildInputArea() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 14),
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            decoration: BoxDecoration(
              color: const Color(0xFF0A0E14),
              borderRadius: BorderRadius.circular(26),
              border: Border.all(color: const Color(0xFFFFB800).withOpacity(0.4)),
            ),
            child: Row(
              children: [
                const Icon(Icons.mic, color: Color(0xFFFFB800), size: 20),
                const SizedBox(width: 10),
                const Expanded(
                  child: Text(
                    'Ask me anything...',
                    style: TextStyle(color: Colors.white38, fontSize: 14),
                  ),
                ),
                Container(
                  width: 34,
                  height: 34,
                  decoration: const BoxDecoration(
                    color: Color(0xFFFFB800),
                    shape: BoxShape.circle,
                  ),
                  child: const Icon(Icons.arrow_forward, color: Colors.black, size: 17),
                ),
              ],
            ),
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              _chip(Icons.view_in_ar, 'Build something'),
              const SizedBox(width: 5),
              _chip(Icons.search, 'Search the web'),
              const SizedBox(width: 5),
              _chip(Icons.terminal, 'Run a command'),
              const SizedBox(width: 5),
              _chip(Icons.more_horiz, 'More'),
            ],
          ),
        ],
      ),
    );
  }

  Widget _chip(IconData icon, String label) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 6),
        decoration: BoxDecoration(
          color: const Color(0xFF0A0E14),
          borderRadius: BorderRadius.circular(18),
          border: Border.all(color: const Color(0xFFFFB800).withOpacity(0.3)),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 12, color: const Color(0xFFFFB800)),
            const SizedBox(width: 3),
            Flexible(
              child: Text(
                label,
                style: const TextStyle(fontSize: 9.5, color: Colors.white70),
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ==================== RECENT ACTIVITY ====================
  Widget _buildRecentActivity() {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 14),
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 6),
      decoration: BoxDecoration(
        color: const Color(0xFF0A0E14),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: Colors.white10),
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('RECENT ACTIVITY', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w600)),
              Text('View All', style: TextStyle(fontSize: 10, color: const Color(0xFFFFB800))),
            ],
          ),
          const SizedBox(height: 8),
          _activity(Icons.terminal, 'Terminal', 'npm run dev', '2m ago', true),
          _activity(Icons.insert_drive_file_outlined, 'File Operation', 'Created: src/components/QuantumCore.tsx', '5m ago', true),
          _activity(Icons.psychology, 'Thinking', 'Planning next steps...', '7m ago', false),
        ],
      ),
    );
  }

  Widget _activity(IconData icon, String title, String desc, String time, bool done) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Row(
        children: [
          Icon(icon, size: 16, color: const Color(0xFFFFB800)),
          const SizedBox(width: 9),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(title, style: const TextStyle(fontSize: 11.5, fontWeight: FontWeight.w500)),
                Text(desc, style: const TextStyle(fontSize: 9.5, color: Colors.white54)),
              ],
            ),
          ),
          Text(time, style: const TextStyle(fontSize: 9.5, color: Colors.white38)),
          const SizedBox(width: 5),
          Icon(
            done ? Icons.check_circle : Icons.sync,
            size: 15,
            color: done ? const Color(0xFF00E676) : const Color(0xFF00B0FF),
          ),
        ],
      ),
    );
  }

  // ==================== BOTTOM NAV ====================
  Widget _buildBottomNav() {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 8),
      decoration: const BoxDecoration(
        color: Color(0xFF0A0E14),
        border: Border(top: BorderSide(color: Colors.white10)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _tab(Icons.home, 'Home', true),
          _tab(Icons.chat_bubble_outline, 'Chat', false),
          _tab(Icons.history, 'History', false),
          _tab(Icons.person_outline, 'Profile', false),
        ],
      ),
    );
  }

  Widget _tab(IconData icon, String label, bool active) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Icon(icon, size: 21, color: active ? const Color(0xFFFFB800) : Colors.white38),
        const SizedBox(height: 2),
        Text(
          label,
          style: TextStyle(
            fontSize: 9.5,
            color: active ? const Color(0xFFFFB800) : Colors.white38,
          ),
        ),
      ],
    );
  }
}

// ==================== ORB PAINTER ====================
class OrbPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final paint = Paint()..style = PaintingStyle.stroke;

    for (int i = 1; i <= 9; i++) {
      final radius = (size.width / 2) * (i / 9.5);
      paint
        ..color = const Color(0xFFFFB800).withOpacity(1.1 - (i * 0.1))
        ..strokeWidth = i == 1 ? 2.2 : 1.3;
      canvas.drawCircle(center, radius, paint);
    }

    // Core glow
    final core = Paint()
      ..color = const Color(0xFFFFB800)
      ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 10);
    canvas.drawCircle(center, 11, core);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
