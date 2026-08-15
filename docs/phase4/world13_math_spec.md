# World 13 Mathematical Specification: Sopantyadvayamantyam

**Document Status:** Complete Mathematical Specification / World 13  
**Sutra Name:** *Sopantyadvayamantyam* ("The ultimate and twice the penultimate")  
**Target Domain:** Multiplication by 12–19 using Ultimate + $N \times$ Penultimate digit pairing  

---

## 1. Pre-Flight Correction & Discrepancy Note

> [!IMPORTANT]
> **Pre-Flight Content Audit Correction**:
> 1. In `docs/phase0/01_sutra_content_audit.md`, the worked example for Sutra 13 (`1/(x+2) + 1/(x+3) = 1/(x+1) + 1/(x+4)`) was identical to World 9 (*Shunyam Samyasamuccaye*). This was a content audit duplication error and is **NOT** implemented in World 13.
> 2. Alternative fractional AP equation interpretations (e.g. `1/((x+1)(x+2)) + 1/((x+1)(x+3)) = ...`) show significant disagreement across sources regarding formula derivation. This family is **EXPLICITLY DEFERRED** pending primary text verification.

---

## 2. Implemented Supported Family: Multiplication by 12–19

For a multiplicand $M$ (positive integer) and a multiplier $10 + N$ where $N \in [2..9]$ (multipliers 12 through 19):

### Algorithm Steps
1. **Sandwich Multiplicand**: Sandwich digits of $M$ with a leading zero and a trailing zero:
   $$d = [0, d_1, d_2, \dots, d_m, 0]$$
   (Length $k = m + 2$).

2. **Compute Raw Position Values**:
   For $i = 0, 1, \dots, m$:
   $$R[i] = d[i+1] + N \cdot d[i]$$
   (Produces $m+1$ raw values from left to right).

3. **Propagate Carries Right-to-Left**:
   Start at rightmost position $i = m$, with initial carry $c = 0$:
   - For $i = m$ down to $0$:
     $$\text{val} = R[i] + c$$
     $$\text{digit}[i] = \text{val} \bmod 10$$
     $$c = \lfloor \text{val} / 10 \rfloor$$
4. **Final Product**: The carry-resolved digit sequence from left to right yields $M \times (10 + N)$.

---

## 3. Required Verified Examples

### Example 1: Canonical ($143 \times 12 \implies N = 2$)
- **Sandwiched Digits**: `0 1 4 3 0`.
- **Raw Values**:
  - $i=0: 1 + 2(0) = 1$
  - $i=1: 4 + 2(1) = 6$
  - $i=2: 3 + 2(4) = 11$
  - $i=3: 0 + 2(3) = 6$
  - Raw list: $[1, 6, 11, 6]$.
- **Carry Propagation**:
  - Pos 3: $6 + 0 = 6 \implies$ digit 6, carry 0
  - Pos 2: $11 + 0 = 11 \implies$ digit 1, carry 1
  - Pos 1: $6 + 1 = 7 \implies$ digit 7, carry 0
  - Pos 0: $1 + 0 = 1 \implies$ digit 1, carry 0
- **Result**: `1716`. Verification: $143 \times 12 = 1716 \checkmark$.

### Example 2: Multiplier 14 ($124 \times 14 \implies N = 4$)
- **Sandwiched Digits**: `0 1 2 4 0`.
- **Raw Values**: $[1, 6, 12, 16]$.
- **Carry Propagation**:
  - Pos 3: $16 \implies$ digit 6, carry 1
  - Pos 2: $12 + 1 = 13 \implies$ digit 3, carry 1
  - Pos 1: $6 + 1 = 7 \implies$ digit 7, carry 0
  - Pos 0: $1 + 0 = 1 \implies$ digit 1, carry 0
- **Result**: `1736`. Verification: $124 \times 14 = 1736 \checkmark$.

### Example 3: Compounding Carries ($253 \times 19 \implies N = 9$)
- **Sandwiched Digits**: `0 2 5 3 0`.
- **Raw Values**: $[2, 23, 48, 27]$.
- **Carry Propagation**:
  - Pos 3: $27 \implies$ digit 7, carry 2
  - Pos 2: $48 + 2 = 50 \implies$ digit 0, carry 5
  - Pos 1: $23 + 5 = 28 \implies$ digit 8, carry 2
  - Pos 0: $2 + 2 = 4 \implies$ digit 4, carry 0
- **Result**: `4807`. Verification: $253 \times 19 = 4807 \checkmark$.

---

## 4. Scope Safeguards

- Multipliers strictly limited to 12 through 19 ($N \in [2..9]$).
- Multiplicands must be positive integers (1 to 4 digits).
- Negative numbers, non-integers, and multipliers outside 12-19 are rejected.
- Deferred fractional-equation family is completely absent from all generator, slate, and practice modes.
