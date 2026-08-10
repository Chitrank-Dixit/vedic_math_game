# MVP Scope & Feature Roadmap

**Document Status:** Complete Scope Definition / Phase 0  
**Target Release:** Android v1.0 (MVP)  

---

## 1. Sutra & Content Scope

To ensure a manageable workload for a solo developer while delivering a complete, high-impact gameplay experience, the v1.0 MVP is scoped to **5 Core Sutras (Worlds 1–5)** and **2 Side-Quest Upa-Sutras**.

```
  ┌──────────────────────────────────────────────────────────┐
  │                 v1.0 MVP CONTENT SCOPE                   │
  ├──────────────────────────────────────────────────────────┤
  │  World 1: Ekadhikena Purvena   (Squaring ending in 5)   │
  │  World 2: Nikhilam Navatash... (Base subtraction/mult)   │
  │  World 3: Ekanyunena Purvena   (Multiplication by 9s)    │
  │  World 4: Yavadunam            (Base Squaring)           │
  │  World 5: Urdhva-Tiryagbhyam   (2x2 Crosswise Mult)      │
  └──────────────────────────────────────────────────────────┘
```

### MVP Main Sutras (v1.0):
1. **Sutra 1: Ekadhikena Purvena** (World 1: *The Mystic Fives*) — Squaring numbers ending in 5.
2. **Sutra 2: Nikhilam Navatashcaramam Dashatah** (World 2: *Realm of Base 100*) — Subtractions from 100/1000 & base multiplication.
3. **Sutra 3: Ekanyunena Purvena** (World 3: *The Temple of Nines*) — Lightning multiplication by 9, 99, 999.
4. **Sutra 4: Yavadunam** (World 4: *Deficiency Citadel*) — Mental squaring near base 10 and 100.
5. **Sutra 5: Urdhva-Tiryagbhyam** (World 5: *Crosswise Coliseum*) — General 2-digit by 2-digit multiplication (MVP Climax Boss).

### MVP Upa-Sutras (Side-Quests in v1.0):
- **Upa-Sutra 8: Antyayordashake'pi** (Side Quest 1A) — Special multiplication when units sum to 10 and tens match.
- **Upa-Sutra 1: Anurupyena** (Side Quest 2A) — Proportional base scaling (working with base 50 or 200).

### Deferred Content (v1.1+ Post-Launch Roadmap):
- **Main Sutras 6–16:** Division (Paravartya), Linear Equations, Simultaneous Equations, Polynomial Factorization, and Differential Calculus.
- **Upa-Sutras 2–7 & 9–13:** Advanced osculation prime tests, cubic calculations, and cyclic polynomial factorizations.

---

## 2. Feature Comparison Matrix

| Category | In-Scope for MVP (v1.0) | Explicitly OUT of Scope for v1.0 |
|---|---|---|
| **Gameplay Core** | • Single-player Story Mode (5 Worlds)<br>• Scaffolded mental decomposition UI<br>• Timed spellcasting puzzle loop<br>• 5 Boss Encounters | • Real-time or async Multiplayer<br>• Infinite Endless / Survival Mode<br>• Custom Level Editor |
| **Meta & Progress** | • Local campaign progression save<br>• Level performance rating (1–3 Stars)<br>• Basic Sutra Codex (tutorial library) | • Global Leaderboards & Cloud Sync<br>• Achievement Trophies connected to Play Games<br>• Daily Quest / Battle Pass systems |
| **Art & Sound** | • 2D vector art style (portrait mode)<br>• Dynamic UI sound effects & 3 background audio tracks<br>• Comedic dialogue boxes with character avatars | • 3D graphics or complex particle physics<br>• Animated cutscenes or full voice acting |
| **Monetization & Ads**| • 100% Free / Self-contained hobby release<br>• Zero mandatory ads or microtransactions | • Cosmetic skins shop / Gacha mechanics<br>• In-app purchases (IAP)<br>• Banner / Interstitial Ad SDK integration |

---

## 3. Rationale for Cut Lines

The primary goal of the v1.0 scope cut is **mitigating solo developer fatigue while validating the core gameplay loop**. 

By restricting the initial release to arithmetic sutras (Worlds 1–5), the game establishes a tight, coherent learning arc focused on fast mental calculation without requiring complex algebraic expression parsers or higher-degree equation solvers in code. The inclusion of *Urdhva-Tiryagbhyam* in World 5 provides a satisfying, highly applicable grand finale for players. Advanced algebraic and calculus sutras (Worlds 6–16) require specialized UI controls (drag-and-drop polynomial terms) and are logically deferred to content expansion packs after confirming player engagement with the core loop.
