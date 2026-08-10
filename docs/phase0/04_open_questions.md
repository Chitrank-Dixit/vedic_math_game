# Consolidated Open Questions & Decision Log

**Document Status:** Resolved / Approved for Phase 1 Entry  
**Purpose:** Actionable checklist of resolved decision points and baseline architectural choices guiding Phase 1 development.

---

## 1. High-Priority Design & Strategy Decisions (LOCKED)

### Q1. Target Audience Persona Selection
- **Status:** **LOCKED (Hybrid Approach)**  
- **Resolution:** Design core gameplay for **Competitive Exam Aspirants** (high speed, utility, fast mental shortcuts for CAT/Banking/SSC), but wrap the UI, narrative, and tutorial scaffolding in **friendly, comedic lore** so general math-curious adults enjoy it without feeling stressed.

### Q2. Game Title Finalization
- **Status:** **LOCKED**  
- **Resolution:** **Ankh: The Sutra Saga** confirmed as official title for app branding, UI headers, and documentation.

### Q3. Game Engine & Technology Stack Choice (Phase 1 Foundation)
- **Status:** **LOCKED**  
- **Resolution:** **Native Android (Kotlin + Jetpack Compose + Custom Canvas/Views)**.
  - *Rationale:* Zero heavy game engine overhead, extremely lightweight APK footprint, native battery efficiency, and rapid declarative UI iteration.

---

## 2. Gameplay Mechanics & UI Architecture (LOCKED)

### Q4. Scaffolded Mental Decomposition Input Design
- **Status:** **LOCKED (Scaffolded Hybrid UI)**  
- **Resolution:** Combine an **on-screen number pad** for final answer and sub-total entries with **tap/swipe gesture tiles** for mental decomposition steps (e.g., dragging deficiency bubbles, splitting base terms, or tapping crosswise product paths).

### Q5. Timer & Penalty Balance
- **Status:** **LOCKED (Scaffolded Timer Curve)**  
- **Resolution:**
  - *World 1:* No timer (pure learning & mechanics tutorial).
  - *Worlds 2–3:* Soft turn-based timer (bonus points for speed, no failure penalty).
  - *Worlds 4–5:* Active countdown timer for tactical boss battles.

### Q6. Mathematical Verification Sign-Off
- **Status:** **LOCKED for MVP (Worlds 1–5)**  
- **Resolution:** Worlds 1–5 main sutras and side-quest upa-sutras are 100% verified. Sutra 9 (*Shunyam Samyasamuccaye*) example corrected to fractional denominator sum model for post-MVP.

---

## 3. Visual Art & Audio Direction (LOCKED)

### Q7. Art Style & Character Aesthetics
- **Status:** **LOCKED**  
- **Resolution:** **Vedic-Futurism 2D Vector**. Clean flat vector graphics with vibrant gold & cyan neon accents in vertical/portrait layout.

### Q8. Sound & Music Strategy
- **Status:** Open for Phase 1  
- **Resolution:** Utilize lightweight royalty-free dynamic synth/ambient audio loops during gameplay.

---

## 4. Final Decision Log Table

| # | Question | Selected Decision | Date Resolved | Notes |
|---|---|---|---|---|
| Q1 | Target Audience | **Hybrid Persona** | 2026-08-08 | High speed utility + comedic friendly lore wrapper |
| Q2 | Title | **Ankh: The Sutra Saga** | 2026-08-08 | Locked official title |
| Q3 | Tech Stack | **Kotlin + Jetpack Compose** | 2026-08-08 | Native Android, lightweight APK footprint |
| Q4 | Step Input | **Scaffolded Hybrid UI** | 2026-08-08 | Keypad + Tap/Swipe gesture tiles |
| Q5 | Timer Balance | **Scaffolded Curve** | 2026-08-08 | World 1 untimed $\rightarrow$ World 5 active boss timer |
| Q6 | Math Verification| **Worlds 1–5 Verified** | 2026-08-08 | Sutra 9 example corrected |
| Q7 | Art Style | **Vedic-Futurism 2D Vector**| 2026-08-08 | Flat vector + gold/cyan neon accents |
