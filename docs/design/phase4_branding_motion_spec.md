# Vedic Mathematics Branding, Motion & Micro-Interactions Spec
**Phase 4: Logo, Visual Motifs, Celebration Engine & Audio/Haptics**

---

## 1. Brand Logo & Vector Geometry Specifications

The Vedic Mathematics app logo synthesizes two sacred mathematical archetypes:

```
+-------------------------------------------------------------+
|                                                             |
|                       [ 8-Pointed Star ]                    |
|                        (Cosmic Yantra)                      |
|                                ╳                            |
|                     [ Infinity Symbol (∞) ]                 |
|                      (Infinite Calculation)                 |
|                                ╳                            |
|                       [ Bindu Center (•) ]                  |
|                        (Sacred Origin)                      |
|                                                             |
|                     In Squircle Frame (120px)               |
|                                                             |
+-------------------------------------------------------------+
```

* **Color Linear Gradient**: $0\%$ Saffron (`#E65100`) $\rightarrow$ $100\%$ Deep Vedic Gold (`#D4AF37`)
* **Vector Stroke Weight**: $2\text{px}$ (mobile preview) / $12\text{px}$ ($512\text{px}$ master SVG)
* **Stroke Cap & Join**: `Round` / `Round`

---

## 2. Audio & Haptic Feedback Mapping Engine

All touch and validation events are mapped to acoustic and haptic frequencies for a tactile, rewarding mental math experience:

| User Interaction | Haptic Pattern (Vibration) | Audio Synthesizer Frequency | Visual Feedback |
| :--- | :--- | :--- | :--- |
| **Numpad Tap** | `Light Tick` (10ms @ 80 intensity) | $800\text{Hz}$ Triangle wave (40ms, decay) | Circular button micro-glow |
| **Correct Digit** | `Medium Pulse` (25ms @ 160 intensity) | $528\text{Hz}$ Solfeggio Sine wave (150ms) | Emerald text flash |
| **Incorrect Step** | `Double Heavy Tap` (40ms, 60ms gap, 40ms) | $180\text{Hz}$ Sawtooth wave (200ms) | Horizontal card shake (12px) |
| **Sutra Mastered** | `Success Fanfare` (Waveform) | Ascending 3-note chord ($528 \rightarrow 660 \rightarrow 792\text{Hz}$) | Full `MandalaBurst` expansion |

---

## 3. Celebration Engine Motion Specifications (`MandalaBurst`)

* **Rings Radiating Outward**: Duration $1100\text{ms}$, Easing `cubic-bezier(0.16, 1, 0.3, 1)`
* **Color Evolution**:
  - $0\% - 40\%$: Vibrant Saffron (`#FF9800`)
  - $40\% - 70\%$: Luminous Gold (`#FFD700`)
  - $70\% - 100\%$: Tulsi Emerald Green (`#4ADE80`)
* **Mandala Star Rotation**: Continuous $0^\circ \rightarrow 90^\circ$ twist as it expands to maximum radius.
