# A.T.L.A.S. — reference UI Android project

This is a native Android/Kotlin implementation of the supplied A.T.L.A.S. reference screen.

## What is included
- Single-activity Android app.
- Custom Canvas renderer for pixel-controlled layout, neon panels, status indicators, navigation, and icons.
- Supplied reference image's central quantum/orb artwork cropped into `app/src/main/res/drawable/quantum_core.png` so the central visual matches the reference closely.
- Functional prompt field, send button, microphone button, quick actions, side navigation and settings tap targets.
- No backend/API dependency: this build is UI-first and can be connected to Hermes/OpenRouter afterward.

## Build in AndroidIDE
1. Copy/extract this folder into AndroidIDE.
2. Open the folder as a Gradle Android project.
3. Let AndroidIDE sync Gradle dependencies.
4. Run `:app:assembleDebug`.
5. Install `app/build/outputs/apk/debug/app-debug.apk`.

## Important
The project is intentionally UI-first. The reference screenshot shows a connected Hermes/OpenRouter state, but the APK does not fake a live backend connection. The status is visual until the Hermes API transport is wired in.
