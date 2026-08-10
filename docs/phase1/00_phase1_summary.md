# Phase 1: Core Systems Prototype — Summary Report

**Project:** Ankh: The Sutra Saga  
**Phase:** 1 (Core Systems Prototype)  
**Status:** Complete & Verified  

---

## 1. Overview of Accomplishments

Phase 1 successfully establishes the core mathematical engine and a vertical slice UI for **World 1 (Ekadhikena Purvena)** built with **Kotlin + Jetpack Compose** under an MVVM architecture.

### Built Components:
- **Project Infrastructure:** Configured Gradle Android build scripts (`build.gradle.kts`, `app/build.gradle.kts`, `local.properties`) targeting API 34 (min SDK 24).
- **Pure Kotlin Engine (`com.ankh.sutrasaga.engine`):**
  - `SutraProblemGenerator`: Core problem generation interface.
  - `EkadhikenaPurvenaGenerator`: Implementation for World 1 (Squaring numbers ending in 5) supporting Tier 1 (2-digit: 15–95) and Tier 2 (3-digit: 105–995) difficulty ranges. Computes canonical decomposition steps ($n \rightarrow n+1 \rightarrow n(n+1) \rightarrow n(n+1)25$) and realistic distractors.
  - `NikhilamGenerator` & `EkanyunenaGenerator`: Engine stubs for Worlds 2 and 3.
- **Unit Test Suite (`EkadhikenaPurvenaGeneratorTest`):**
  - 6 comprehensive JUnit test cases verifying canonical worked example $65^2 = 4225$, 30+ randomized numbers against $N^2$, decomposition step values, and distractor uniqueness. **All tests passing 100%**.
- **Room Database (`com.ankh.sutrasaga.data`):**
  - `AppDatabase` & `UserProgressDao` persisting world completion status and high scores locally.
- **Jetpack Compose Vertical Slice UI (`com.ankh.sutrasaga.ui`):**
  - `WorldSelectScreen`: Displays unlocked World 1 and locked Worlds 2–5.
  - `StoryBeatScreen`: Story dialogue card with placeholder narrative text.
  - `TutorialScreen`: Worked example ($65^2$) step-by-step interactive decomposition reveal.
  - `PracticeArenaScreen`: 5 generated problems with hybrid input (tap-to-reveal decomposition hint tiles + custom numeric keypad for final answer submission + immediate feedback).
  - `BossBattleScreen`: 5 harder 3-digit problems (untimed for World 1 per locked decisions) with boss status header and score tracking.
  - `RewardScreen`: Victory summary, persists high score to Room DB, button to return home.

---

## 2. How to Build & Run

### Running Unit Tests:
Execute the following Gradle command from the root directory `d:\game_dev\VedicMathematics`:
```powershell
.\gradlew test
```
Or using the installed Gradle daemon:
```powershell
C:\Users\Admin\.gradle\wrapper\dists\gradle-9.3.0-bin\79n14ral3mx1ozqr3csh2u872\gradle-9.3.0\bin\gradle.bat test
```

### Compiling Debug APK:
```powershell
.\gradlew assembleDebug
```
Output APK location: `app/build/outputs/apk/debug/app-debug.apk`.

---

## 3. Technical Decisions & Open Questions

1. **Local Properties SDK Path:** Configured `local.properties` with `sdk.dir=C:\Users\Admin\AppData\Local\Android\Sdk` to allow local command-line builds.
2. **Hybrid Input Execution:** Implemented a clean, accessible hybrid layout: a 12-key numeric keypad (`0–9`, `CLR`, `⌫`, `SUBMIT`) paired with a "💡 Reveal Hint" button that steps through intermediate Vedic decomposition calculations.
3. **Open Question for Phase 2:** Story text and character art are currently placeholders (`TODO: sutra-master joke here`). Phase 2 can focus on writing story dialogue, visual art theme integration, and expanding World 2 & 3 engines.
