# Technical Specification: Rive 2.5D Vector State Machine for Guru-Shishya Dialogues

## 1. Executive Summary & Verdict

For a mobile quiz and learning application such as **Ankh: The Sutra Saga**, **Rive** provides the optimal technical foundation over heavy 3D game engines (like Unity or Unreal).

- **Duolingo Alignment**: Duolingo relies entirely on Rive for character animation (Duo, Lily, Falstaff) via 2.5D vector mesh deformation, bones, and nested state machines.
- **Binary Footprint**: Rive `.riv` character assets range from **50 KB to 300 KB**, compared to **+30 MB to 100 MB+** added by embedding the Unity runtime.
- **Instant Cold Boot & Screen Transitions**: Rive renders directly onto native Jetpack Compose / Android canvases without engine initialization delays.
- **State Machine Binding**: 1:1 direct binding between dialogue domain models (`GurukulDialogueBeat`) and Rive State Machine inputs (`pose_id`, `facial_state`, `is_speaking`).

---

## 2. Head-to-Head Comparison Matrix

| Architectural Dimension | Rive (Recommended) | Unity (UaaL / Embedded) | Compose 2.5D (Active Baseline) |
| :--- | :--- | :--- | :--- |
| **App Bundle Impact** | **~1.5 MB** runtime + **~200 KB** assets | **+35 MB – 90 MB** `.so` and engine assets | **0 MB** (Pure Compose code & vector XML) |
| **Frame Rate & Battery** | 60/120 FPS native vector render; sleeps on idle | Continuous render loop; heavy GPU/battery drain | 60/120 FPS; zero overhead when idle |
| **Native Compose Interop** | Official Android Compose runtime (`app.rive:rive-android`) | Complex `AndroidView(UnityPlayer)` TextureView bridge | **100% Native Jetpack Compose** |
| **Dialogue State Binding** | Direct JSON / State Machine parameter binding | Mecanim Animator Parameters via C# bridge | Direct Composable State hoisting |
| **Asset Iteration Speed** | Instant `.riv` export without rebuilding APK | Recompile Unity player and repackage Android | Immediate Kotlin hot-reload / build |

---

## 3. Rive State Machine Input Specification

### A. Guru Sage State Machine (`GuruStateMachine`)

```
Artboard: "GuruSage"
State Machine: "SM_Guru"
```

| Input Name | Type | Allowed Values | Description |
| :--- | :--- | :--- | :--- |
| `pose_id` | Number / Int | `0`: Base Idle<br>`1`: Explaining (Hand Raised)<br>`2`: Talking (Gesturing)<br>`3`: Contemplating / Thinking | Controls base spine, arm, and head posture bones. |
| `mouth_state` | Number / Int | `0`: Neutral / Closed<br>`1`: Talking / Viseme Blend<br>`2`: Smile / Encouragement | Modulates 2.5D mouth mesh blend paths. |
| `is_speaking` | Boolean | `true` / `false` | Enables speech aura pulsation and mouth viseme cycle. |
| `trigger_celebrate` | Trigger | Fire trigger | Plays full-body blessing / victory gesture on sutra mastery. |

---

### B. Disciple (Shishya) State Machine (`ShishyaStateMachine`)

```
Artboard: "Shishya"
State Machine: "SM_Shishya"
```

| Input Name | Type | Allowed Values | Description |
| :--- | :--- | :--- | :--- |
| `disciple_state` | Number / Int | `0`: Neutral Attentive<br>`1`: Confused (Head Scratch)<br>`2`: Understanding / Eureka<br>`3`: Celebrating | Controls body posture, floating hover offset, and arm positions. |
| `eye_state` | Number / Int | `0`: Neutral Focused<br>`1`: Puzzled / Inquisitive<br>`2`: Joyful / Happy<br>`3`: Blinking | Drives eye vector shapes and eyelid morph curves. |
| `is_speaking` | Boolean | `true` / `false` | Enables cyan speaker aura ring and dialogue bobbing. |
| `trigger_eureka` | Trigger | Fire trigger | Plays lightbulb / spark of insight animation when solving a step. |

---

## 4. Kotlin / Jetpack Compose Integration Pattern

```kotlin
// Example: Binding GurukulDialogueBeat to Rive State Machine
@Composable
fun RiveGurukulAvatar(
    beat: GurukulDialogueBeat,
    modifier: Modifier = Modifier
) {
    // Official Rive Compose View binding
    RiveAnimation(
        animationResId = R.raw.guru_and_shishya,
        stateMachineName = "SM_Gurukul",
        modifier = modifier,
        onInit = { riveView ->
            // Map Dialogue Domain Model -> Rive State Machine Inputs
            riveView.setNumberState("SM_Gurukul", "guru_pose_id", beat.guruPose.ordinal.toFloat())
            riveView.setNumberState("SM_Gurukul", "guru_mouth_id", beat.guruMouth.ordinal.toFloat())
            riveView.setBooleanState("SM_Gurukul", "is_guru_speaking", beat.guruText.isNotBlank())
            
            riveView.setNumberState("SM_Gurukul", "disciple_state_id", beat.discipleState.ordinal.toFloat())
            riveView.setNumberState("SM_Gurukul", "disciple_eye_id", beat.discipleEyes.ordinal.toFloat())
            riveView.setBooleanState("SM_Gurukul", "is_disciple_speaking", !beat.discipleText.isNullOrBlank())
        }
    )
}
```

---

## 5. Implementation Roadmap

1. **Vector Character Design**:
   - Create multi-layered vector assets for Guru Sage (*Acharya*) and Disciple (*Shishya*) in Figma / Illustrator.
2. **Rive 2.5D Rigging**:
   - Rig skeletal bones, eye constraints, and viseme morph meshes.
   - Configure 2.5D depth turns (subtle head tilting and eye parallax).
3. **State Machine Configuration**:
   - Wire input triggers matching `GurukulModels.kt` enums.
4. **Drop-in Deployment**:
   - Drop `guru_and_shishya.riv` into `app/src/main/res/raw/` or `assets/` and link via `RiveGurukulAvatar`.
