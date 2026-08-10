# Phase 2 Addendum: Character Asset Integration Summary & Handoff Manifest

**Project:** Ankh: The Sutra Saga  
**Phase:** Phase 2 Addendum — Full-Body Character Asset Integration  
**Status:** Complete & 100% Verified  

---

## 1. Complete Art Asset Manifest & File Inventory

The code now references the exact asset filenames specified in the approved design breakdown sheet. Transparent `.png` assets delivered by the art pipeline can be placed directly into `app/src/main/res/drawable/` to replace the vector placeholders without code changes.

### A. Guru (Elder Character Assets)
| Asset Filename | Layer Type | Usage & Purpose | Code Reference Site |
| :--- | :--- | :--- | :--- |
| `guru_base_pose.png` | Base Pose | Seated neutral pose | [GurukulModels.kt:8](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L8) |
| `guru_explaining_pose.png` | Base Pose | Seated raised-arm teaching gesture | [GurukulModels.kt:10](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L10) |
| `guru_talking_pose.png` | Base Pose | Seated active talking pose | [GurukulModels.kt:12](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L12) |
| `guru_mouth_neutral.png` | Mouth Overlay | Closed mouth | [GurukulModels.kt:17](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L17) |
| `guru_mouth_talking.png` | Mouth Overlay | Open talking mouth | [GurukulModels.kt:19](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L19) |
| `guru_mouth_smile.png` | Mouth Overlay | Smiling mouth | [GurukulModels.kt:21](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L21) |

### B. Disciple (Calcu-Ghost Sidekick Assets)
| Asset Filename | Layer Type | Usage & Purpose | Code Reference Site |
| :--- | :--- | :--- | :--- |
| `disciple_neutral.png` | Base Pose | Seated neutral stance | [GurukulModels.kt:26](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L26) |
| `disciple_confused.png` | Base Pose | Seated head-tilt scratching pose | [GurukulModels.kt:28](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L28) |
| `disciple_understanding.png` | Base Pose | Seated celebrating hands-raised pose | [GurukulModels.kt:30](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L30) |
| `disciple_eyes_neutral.png` | Eye Overlay | Regular dot eyes | [GurukulModels.kt:35](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L35) |
| `disciple_eyes_confused.png` | Eye Overlay | Slanted confused eyebrows/eyes | [GurukulModels.kt:37](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L37) |
| `disciple_eyes_happy.png` | Eye Overlay | Star/sparkle eyes | [GurukulModels.kt:39](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/GurukulModels.kt#L39) |

### C. Background, Props, and FX Assets
| Asset Filename | Layer Type | Usage & Purpose | Code Reference Site |
| :--- | :--- | :--- | :--- |
| `bg_gurukul_scene.png` | Scene Background | Full-screen mahogany wood & banyan background | [GurukulSceneScreen.kt:80](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L80) |
| `prop_holographic_slate.png` | Prop Container | Frame container enclosing `MathSlate` | [GurukulSceneScreen.kt:180](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L180) |
| `prop_seated_mat.png` | Ground Prop | Seated mat under Guru & Disciple | [GurukulSceneScreen.kt:164](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L164) |
| `prop_floor_mandala.png` | Ground Prop | Centered floor glow mandala | [GurukulSceneScreen.kt:100](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L100) |
| `prop_temple_structure.png` | Background Prop | Architectural background detail | [GurukulSceneScreen.kt:89](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L89) |
| `fx_hologram_glow.png` | Ambient FX | Pulsing ambient glow behind slate | [GurukulSceneScreen.kt:169](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L169) |
| `fx_particles.png` | Victory FX | Particle burst effect on step 5 | [GurukulSceneScreen.kt:216](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/components/GurukulSceneScreen.kt#L216) |

---

## 2. Textual Scene Layout Overview

```
+------------------------------------------------------------------+
| Top Bar: Title, Subtitle, Progress Bar, Refresh & Skip Buttons   |
+------------------------------------------------------------------+
| [bg_gurukul_scene]                                               |
|                                                                  |
|               [prop_temple_structure (alpha=0.3)]                |
|                                                                  |
|                     [prop_floor_mandala]                         |
|                                                                  |
|  [Guru Seated]       [prop_holographic_slate]      [Disciple]    |
| (LayeredCharacter)    +---------------------+  (LayeredCharacter)|
|                      |  [fx_hologram_glow]  |                    |
|                      |   [MathSlate Component]                   |
|                      +---------------------+                     |
| [prop_seated_mat]                              [prop_seated_mat] |
|                                                                  |
+------------------------------------------------------------------+
| Anchored Dialogue Caption Box:                                   |
|   Guru: "Let us calculate 65²! Isolate tens prefix n = 6..."      |
|   Ghost: "6 + 1 = 7! So 6 × 7 = 42!"                              |
+------------------------------------------------------------------+
```

---

## 3. Verification & Build Confirmation

- **JVM Engine & Unit Test Suite (`.\gradlew test`)**: `BUILD SUCCESSFUL in 10s` (33 / 33 tests passed).
- **Debug APK Build (`.\gradlew assembleDebug`)**: Output APK location: `app/build/outputs/apk/debug/app-debug.apk`.
