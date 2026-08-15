# World 10 Mathematical Specification: Puranapuranabhyam

**Document Status:** Complete Mathematical Specification / World 10  
**Sutra Name:** *Puranapuranabhyam* ("By completion or non-completion")  
**Target Domain:** Quadratic equations via completing the square  

---

## 1. Executive Summary & Core Theorem

The Sutra *Puranapuranabhyam* states: *"Puranapuranabhyam"* — by the completion or non-completion.

For the first release of World 10, we implement completing the square for real-coefficient quadratic equations:
$$A x^2 + B x + C = 0 \quad (A \ne 0)$$

Any equation outside the rational-root quadratic domain is classified explicitly according to its discriminant.

---

## 2. Derivation of Completing the Square

Given $A x^2 + B x + C = 0$ with $A \ne 0$:

1. **Normalize by $A$**:
   $$x^2 + \left(\frac{B}{A}\right)x + \frac{C}{A} = 0$$

2. **Move Constant Term to RHS**:
   $$x^2 + \left(\frac{B}{A}\right)x = -\frac{C}{A}$$

3. **Identify Completion Term**:
   The completion term is the square of half the linear coefficient:
   $$T_{comp} = \left( \frac{B}{2A} \right)^2 = \frac{B^2}{4A^2}$$

4. **Add $T_{comp}$ to Both Sides**:
   $$x^2 + \left(\frac{B}{A}\right)x + \frac{B^2}{4A^2} = -\frac{C}{A} + \frac{B^2}{4A^2} = \frac{B^2 - 4AC}{4A^2}$$

5. **Form Perfect Square**:
   $$\left( x + \frac{B}{2A} \right)^2 = \frac{D}{4A^2} \quad \text{where } D = B^2 - 4AC \text{ is the discriminant}$$

6. **Solve Square & Derive Roots**:
   $$x + \frac{B}{2A} = \pm \frac{\sqrt{D}}{2A} \implies x = \frac{-B \pm \sqrt{D}}{2A}$$

---

## 3. Required Verified Examples

### Example 1: Monic Beginner ($x^2 + 6x + 8 = 0$)
- **Coefficients**: $A = 1, B = 6, C = 8$.
- **Discriminant**: $D = 6^2 - 4(1)(8) = 36 - 32 = 4$ ($\sqrt{D} = 2$).
- **Move Constant**: $x^2 + 6x = -8$.
- **Completion Term**: $(6/2)^2 = 9$.
- **Add 9 to Both Sides**: $x^2 + 6x + 9 = -8 + 9 = 1$.
- **Perfect Square**: $(x + 3)^2 = 1$.
- **Roots**: $x + 3 = \pm 1 \implies x_1 = -2, x_2 = -4$.
- **Verification**:
  - $(-2)^2 + 6(-2) + 8 = 4 - 12 + 8 = 0 \checkmark$.
  - $(-4)^2 + 6(-4) + 8 = 16 - 24 + 8 = 0 \checkmark$.

### Example 2: Non-Monic ($2x^2 + 5x - 3 = 0$)
- **Coefficients**: $A = 2, B = 5, C = -3$.
- **Discriminant**: $D = 5^2 - 4(2)(-3) = 25 + 24 = 49$ ($\sqrt{D} = 7$).
- **Normalize by 2**: $x^2 + \frac{5}{2}x - \frac{3}{2} = 0 \implies x^2 + \frac{5}{2}x = \frac{3}{2}$.
- **Completion Term**: $\left( \frac{5}{4} \right)^2 = \frac{25}{16}$.
- **Add $\frac{25}{16}$ to Both Sides**: $x^2 + \frac{5}{2}x + \frac{25}{16} = \frac{3}{2} + \frac{25}{16} = \frac{49}{16}$.
- **Perfect Square**: $\left(x + \frac{5}{4}\right)^2 = \frac{49}{16}$.
- **Roots**: $x + \frac{5}{4} = \pm \frac{7}{4} \implies x_1 = \frac{2}{4} = \frac{1}{2}, x_2 = -\frac{12}{4} = -3$.
- **Verification**:
  - $2(1/2)^2 + 5(1/2) - 3 = 2(1/4) + 5/2 - 3 = 1/2 + 5/2 - 3 = 3 - 3 = 0 \checkmark$.
  - $2(-3)^2 + 5(-3) - 3 = 2(9) - 15 - 3 = 18 - 18 = 0 \checkmark$.

### Example 3: Repeated Root ($x^2 - 6x + 9 = 0$)
- **Coefficients**: $A = 1, B = -6, C = 9$.
- **Discriminant**: $D = (-6)^2 - 4(1)(9) = 36 - 36 = 0$.
- **Perfect Square**: $(x - 3)^2 = 0$.
- **Root**: $x = 3$ (repeated root).
- **Verification**: $3^2 - 6(3) + 9 = 9 - 18 + 9 = 0 \checkmark$.

### Example 4: No Real Roots ($x^2 + 2x + 5 = 0$)
- **Coefficients**: $A = 1, B = 2, C = 5$.
- **Discriminant**: $D = 2^2 - 4(1)(5) = 4 - 20 = -16 < 0$.
- **Classification**: `NO_REAL_ROOTS` (complex roots deferred).

---

## 4. Classification Matrix

| Classification | Discriminant Condition | Root Nature | Example |
|---|---|---|---|
| `TWO_REAL_ROOTS` | $D > 0$ and $\sqrt{D} \in \mathbb{Z}$ | Two distinct rational roots $x_1, x_2$ | $x^2 + 6x + 8 = 0$ ($x = -2, -4$) |
| `REPEATED_REAL_ROOT` | $D = 0$ | Single repeated rational root $x_1 = x_2$ | $x^2 - 6x + 9 = 0$ ($x = 3$) |
| `NO_REAL_ROOTS` | $D < 0$ | Complex conjugate roots (deferred) | $x^2 + 2x + 5 = 0$ |
| `IRRATIONAL_REAL_ROOTS_DEFERRED` | $D > 0$ and $\sqrt{D} \notin \mathbb{Z}$ | Irrational roots (deferred from MVP generation pool) | $x^2 + 2x - 1 = 0$ ($D = 8$) |
| `NOT_QUADRATIC` | $A = 0$ | Linear equation (rejected) | $0x^2 + 5x + 3 = 0$ |
| `UNSUPPORTED` | Overflow or invalid coefficients | Degenerate/invalid input | $A, B, C$ out of safe bounds |
