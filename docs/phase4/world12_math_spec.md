# World 12 Mathematical Specification: Shesanyankena Charamena

**Document Status:** Complete Mathematical Specification / World 12  
**Sutra Name:** *Shesanyankena Charamena* ("The remainders by the last digit")  
**Target Domain:** Decimal expansions of rational numbers $p/q$ via remainder-state tracking and cycle detection  

---

## 1. Executive Summary & Core Algorithm

The Sutra *Shesanyankena Charamena* states: *"Shesanyankena Charamena"* — the remainders by the last digit.

For World 12, we implement exact decimal expansion of rational numbers $p/q$ ($q > 0$) using a deterministic remainder state machine:

Given fraction $p/q$:

1. **Reduction**: Compute $g = \gcd(|p|, q)$ and normalize to $p' = |p|/g, q' = q/g$.
2. **Integer Part**: $I = \lfloor p' / q' \rfloor$. Initial remainder $r_0 = p' \bmod q'$.
3. **Digit Emission Loop** (Step $k = 1, 2, \dots$):
   - Value $V_k = 10 \cdot r_{k-1}$.
   - Emitted digit $d_k = \lfloor V_k / q' \rfloor$.
   - Next remainder $r_k = V_k \bmod q'$.
4. **Termination or Cycle Identification**:
   - If $r_k = 0 \implies$ **Terminating Decimal** (stop loop).
   - If $r_k$ was previously seen at step index $j < k \implies$ **Recurring Decimal**.
     - Non-repeating prefix: digits $d_1, \dots, d_j$.
     - Repeating cycle: digits $d_{j+1}, \dots, d_k$.
     - Cycle length: $L = k - j$.

---

## 2. Vedic Presentation Engine Integration

Alongside the reference decimal state machine, the Vedic presentation tracks the remainder sequence $r_k$ and demonstrates how multiplying remainders by the divisor's complementary last-digit parameter generates the decimal block.
- **Reference Engine**: Exact `remainder -> digit` state machine (correctness oracle).
- **Vedic Engine**: Verified step-by-step against the reference engine.

---

## 3. Required Verified Examples

### Example 1: Canonical Recurring ($1/7$)
- **Fraction**: $p=1, q=7$.
- **Integer Part**: $I = 0$, $r_0 = 1$.
- **Step Trace**:
  - $k=1: 10 \cdot 1 = 10 \implies d_1 = 1, r_1 = 3$.
  - $k=2: 10 \cdot 3 = 30 \implies d_2 = 4, r_2 = 2$.
  - $k=3: 10 \cdot 2 = 20 \implies d_3 = 2, r_3 = 6$.
  - $k=4: 10 \cdot 6 = 60 \implies d_4 = 8, r_4 = 4$.
  - $k=5: 10 \cdot 4 = 40 \implies d_5 = 5, r_5 = 5$.
  - $k=6: 10 \cdot 5 = 50 \implies d_6 = 7, r_6 = 1$.
- **Cycle Detection**: $r_6 = 1 = r_0 \implies$ Pure recurring cycle at index 0.
- **Result**: $1/7 = 0.(142857)$, cycle length 6.

### Example 2: Terminating Decimal ($1/8$)
- **Fraction**: $p=1, q=8$.
- **Step Trace**:
  - $k=1: 10 \cdot 1 = 10 \implies d_1 = 1, r_1 = 2$.
  - $k=2: 10 \cdot 2 = 20 \implies d_2 = 2, r_2 = 4$.
  - $k=3: 10 \cdot 4 = 40 \implies d_3 = 5, r_3 = 0$.
- **Termination**: $r_3 = 0 \implies$ Terminating decimal.
- **Result**: $1/8 = 0.125$.

### Example 3: Non-Unit Numerator ($3/7$)
- **Fraction**: $p=3, q=7$.
- **Step Trace**:
  - $k=1: 30 \implies d_1 = 4, r_1 = 2$.
  - $k=2: 20 \implies d_2 = 2, r_2 = 6$.
  - $k=3: 60 \implies d_3 = 8, r_3 = 4$.
  - $k=4: 40 \implies d_4 = 5, r_4 = 5$.
  - $k=5: 50 \implies d_5 = 7, r_5 = 1$.
  - $k=6: 10 \implies d_6 = 1, r_6 = 3 = r_0$.
- **Result**: $3/7 = 0.(428571)$ (phase-shifted 6-digit cycle of $1/7$).

### Example 4: Mixed Recurring ($1/6$)
- **Fraction**: $p=1, q=6$.
- **Step Trace**:
  - $k=1: 10 \implies d_1 = 1, r_1 = 4$.
  - $k=2: 40 \implies d_2 = 6, r_2 = 4 = r_1$.
- **Cycle Detection**: $r_2 = r_1 = 4 \implies$ Non-repeating prefix '1', repeating cycle '6'.
- **Result**: $1/6 = 0.1(6)$.

### Example 5: Longer Cycle ($1/13$)
- **Fraction**: $p=1, q=13$.
- **Result**: $1/13 = 0.(076923)$, cycle length 6.

---

## 4. Classification Matrix

| Classification | Remainder & Cycle Condition | Example |
|---|---|---|
| `TERMINATING` | Remainder reaches $r_k = 0$ | $1/8 = 0.125$ |
| `PURE_RECURRING` | $r_k$ repeats $r_0$ (no non-repeating prefix) | $1/7 = 0.(142857)$ |
| `MIXED_RECURRING` | $r_k$ repeats $r_j$ ($j > 0$, non-repeating prefix present) | $1/6 = 0.1(6)$ |
| `ZERO` | $p = 0$ | $0/7 = 0$ |
| `NEGATIVE` | $p < 0$ | $-1/7 = -0.(142857)$ |
| `OUTPUT_LIMIT_REACHED` | Cycle length exceeds max output limit (e.g. > 20 digits) | $1/29$ (limit 20) |
| `UNSUPPORTED` | $q \le 0$ or non-rational input | Invalid input |
