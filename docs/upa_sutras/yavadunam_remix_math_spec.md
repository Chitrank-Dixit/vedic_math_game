# Mathematical Specification & Differentiation Plan: Upa-Sutra 7 — Yavadunam Tavadunikrtya Varganca Yojayet

**Document Status:** Complete Mathematical Specification & Differentiation Plan / Phase U2  
**Feature:** Upa-Sutra Treasury — Quest 3  
**Target Class:** `YavadunamRemixGenerator`  
**Parent World:** World 4 (Yavadunam)  
**Unlock Requirement:** Completion of World 4 (Yavadunam)  

---

## 1. Mathematical Formulation & World 4 Comparison

The arithmetic foundation of Upa-Sutra 7 (*Yavadunam Tavadunikrtya Varganca Yojayet* — "Whatever the deficiency, lessen by that amount and set up the square of the deficiency") is algebraically identical to World 4's base squaring formula:

$$N^2 = (N - d) \cdot 10^k + d^2 \quad \text{where } d = B - N, \, B = 10^k$$

### Side-by-Side Comparison

| Feature | World 4 (Yavadunam - Main Campaign) | Upa-Sutra 7 (Mastery Remix - Treasury Quest) |
|---|---|---|
| **Core Objective** | Introduce near-base mental squaring for the first time. | Mastery challenge proving proficiency with manual padding & overflow carries. |
| **Scaffolding** | Full explanatory walkthrough with auto-scaffolded steps. | Minimal scaffolding; assumes foundational mastery of $(N - d) \,\|\, d^2$. |
| **Digit Block Focus** | Mostly 2-digit near base 100 with automatic block formatting. | Explicit focus on 3-digit base 1000 padding ($036, 009$) and multi-digit overflow carries. |
| **Carry Mechanics** | Covered passively within decomposition hints. | Core gameplay mechanic: forced multi-digit overflow carries (e.g. $12^2 = 144 \implies +1$ carry, $15^2 = 225 \implies +2$ carry, $21^2 = 441 \implies +4$ carry). |
| **Story / Tone** | Learning the grove's deficiency rule for the first time. | Remixed challenge: Guru acknowledges mastery and tests "when the deficiency outgrows its room". |

---

## 2. Step-by-Step Algorithm & Carry Overflow Mechanics

Let operand $N$ have deficiency $d = B - N$ from primary base $B = 10^k$ (block width $k$ digits):

1. **Deficiency Calculation**: $d = B - N$.
2. **Left-Hand Side (Raw LHS)**: $L_{\text{raw}} = N - d = B - 2d$.
3. **Right-Hand Side (Raw RHS)**: $R_{\text{raw}} = d^2$.
4. **Block Width & Overflow Detection**:
   - Primary base $B = 10^k$ has block width $k$ digits ($k = 2$ for base 100, $k = 3$ for base 1000).
   - If $R_{\text{raw}} < 10^k$:
     - Carry $C = 0$.
     - Formatted RHS $= \text{padLeft}(R_{\text{raw}}, k, \text{'0'})$.
     - Final LHS $= L_{\text{raw}}$.
   - If $R_{\text{raw}} \ge 10^k$ (Overflow Case):
     - Carry $C = \lfloor R_{\text{raw}} / 10^k \rfloor$.
     - Remainder RHS $= R_{\text{raw}} \pmod{10^k}$.
     - Formatted RHS $= \text{padLeft}(\text{Remainder RHS}, k, \text{'0'})$.
     - Final LHS $= L_{\text{raw}} + C$.
5. **Concatenation / Summation**:
   $$\text{Final Product} = \text{Final LHS} \cdot 10^k + \text{Remainder RHS} = N^2$$

---

## 3. Verified Seed Examples

### Example 1: Standard Base 100 with Zero Padding (No Carry)
- **Problem**: $97^2$
- **Base**: $B = 100, k = 2$
- **Deficiency**: $d = 100 - 97 = 3$
- **LHS**: $97 - 3 = 94$
- **RHS**: $3^2 = 9 \implies \text{pad2}(9) = \text{"09"}$
- **Result**: $94 \,\|\, 09 = 9409$
- **Direct Check**: $97 \times 97 = 9409 \checkmark$

### Example 2: Base 1000 3-Digit Block Padding (No Carry)
- **Problem**: $994^2$
- **Base**: $B = 1000, k = 3$
- **Deficiency**: $d = 1000 - 994 = 6$
- **LHS**: $994 - 6 = 988$
- **RHS**: $6^2 = 36 \implies \text{pad3}(36) = \text{"036"}$
- **Result**: $988 \,\|\, 036 = 988036$
- **Direct Check**: $994 \times 994 = 988036 \checkmark$

### Example 3: Base 100 Carry Overflow Forcing (+1 Carry)
- **Problem**: $88^2$
- **Base**: $B = 100, k = 2$
- **Deficiency**: $d = 100 - 88 = 12$
- **Raw LHS**: $88 - 12 = 76$
- **Raw RHS**: $12^2 = 144$ (3 digits, exceeds $k = 2$)
- **Overflow Carry**:
  - Carry $C = \lfloor 144 / 100 \rfloor = 1$
  - RHS Remainder $= 144 \pmod{100} = 44$
  - Final LHS $= 76 + 1 = 77$
- **Result**: $77 \,\|\, 44 = 7744$
- **Direct Check**: $88 \times 88 = 7744 \checkmark$

### Example 4: Base 100 Carry Overflow Forcing (+2 Carry)
- **Problem**: $85^2$
- **Base**: $B = 100, k = 2$
- **Deficiency**: $d = 100 - 85 = 15$
- **Raw LHS**: $85 - 15 = 70$
- **Raw RHS**: $15^2 = 225$
- **Overflow Carry**: $C = 2$, Remainder $= 25$, Final LHS $= 70 + 2 = 72$
- **Result**: $72 \,\|\, 25 = 7225$
- **Direct Check**: $85 \times 85 = 7225 \checkmark$

---

## 4. Difficulty Tier Breakdown

- **Tier 1 (Base 100, No-Carry, $d \le 9$)**: Operands $91..99$. Tests correct zero-padding (e.g. $98^2 = 9604, 97^2 = 9409$).
- **Tier 2 (Base 1000, 3-Digit Block Width, $d \le 31$)**: Operands $970..999$. Tests 3-digit padding (e.g. $994^2 = 988036, 997^2 = 994009$).
- **Tier 3 (Base 100, Carry Overflow Forcing, $10 \le d \le 25$)**: Operands $75..89$. Tests multi-digit overflow carries (e.g. $88^2 = 7744, 86^2 = 7396, 84^2 = 7056, 79^2 = 6241$).
