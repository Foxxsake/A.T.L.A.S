package com.atlas.app

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var atlas: AtlasView
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.setNavigationBarContrastEnforced(false)
        window.decorView.setOnApplyWindowInsetsListener { v, insets -> insets }
        if (android.os.Build.VERSION.SDK_INT >= 30) window.setDecorFitsSystemWindows(false)
        window.insetsController?.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)

        val root = FrameLayout(this).apply { setBackgroundColor(Color.BLACK) }
        atlas = AtlasView(this) { action -> handleAction(action) }
        root.addView(atlas, FrameLayout.LayoutParams(-1, -1))

        input = EditText(this).apply {
            setSingleLine(true)
            hint = "Ask me anything..."
            setHintTextColor(Color.rgb(120, 135, 155))
            setTextColor(Color.rgb(235, 240, 250))
            textSize = 15f
            typeface = android.graphics.Typeface.MONOSPACE
            setPadding(18, 0, 12, 0)
            background = null
            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_SEND
            setOnEditorActionListener { _, _, _ -> sendPrompt(); true }
        }
        root.addView(input, FrameLayout.LayoutParams(100, 56))
        setContentView(root)
        atlas.onLaidOut = { updateInputPosition() }
        atlas.post { updateInputPosition() }
    }

    private fun updateInputPosition() {
        val w = atlas.width.toFloat()
        if (w <= 0f) return
        val s = w / 1024f
        val h = atlas.height.toFloat()
        val designTop = if (h / w > 1.75f) 1017f else 1017f
        val lp = input.layoutParams as FrameLayout.LayoutParams
        lp.width = (716f * s).toInt()
        lp.height = (56f * s).toInt()
        lp.leftMargin = (161f * s).toInt()
        lp.topMargin = (designTop * s).toInt()
        input.layoutParams = lp
    }

    private fun sendPrompt() {
        val text = input.text.toString().trim()
        if (text.isNotEmpty()) {
            atlas.activity("Thinking", text)
            input.text.clear()
            Toast.makeText(this, "A.T.L.A.S. received the command", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAction(action: String) {
        when (action) {
            "send" -> sendPrompt()
            "mic" -> Toast.makeText(this, "Listening…", Toast.LENGTH_SHORT).show()
            "build" -> atlas.activity("Build", "Build workflow opened")
            "search" -> atlas.activity("Web Search", "Search workflow opened")
            "command" -> atlas.activity("Terminal", "Command workflow opened")
            "settings" -> atlas.activity("Settings", "Settings opened")
            else -> atlas.activity(action, "Panel opened")
        }
    }
}
