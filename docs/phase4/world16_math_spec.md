# World 16 Mathematical Specification: Chalana-Kalanabhyam

**Document Status:** Complete Mathematical Specification / World 16 (Post-MVP Campaign Finale)  
**Sutra Name:** *Chalana-Kalanabhyam* ("Sequential motion / By calculus")  
**Target Domain:** Quadratic derivative-discriminant root relation  

---

## 1. Executive Summary & Core Algorithm

The Sutra *Chalana-Kalanabhyam* relates the first derivative of a quadratic function to the square root of its discriminant at any root of the polynomial:
$$f(x) = Ax^2 + Bx + C = 0 \quad (A \neq 0)$$

1. **First Derivative**:
   $$f'(x) = \frac{d}{dx}(Ax^2 + Bx + C) = 2Ax + B$$

2. **Discriminant**:
   $$D = B^2 - 4AC$$

3. **Derivative-Discriminant Invariant**:
   For any root $r$ such that $f(r) = 0$:
   $$(f'(r))^2 = (2Ar + B)^2 = 4A^2 r^2 + 4ABr + B^2$$
   Since $Ar^2 + Br + C = 0 \implies 4A(Ar^2 + Br + C) = 4A^2 r^2 + 4ABr + 4AC = 0$,
   $$(f'(r))^2 = 4A^2 r^2 + 4ABr + B^2 - (4A^2 r^2 + 4ABr + 4AC) = B^2 - 4AC = D$$

4. **Root Derivation**:
   $$f'(x) = \pm \sqrt{D} \implies 2Ax + B = \pm \sqrt{D}$$
   $$x = \frac{-B \pm \sqrt{D}}{2A}$$

This relation is mathematically equivalent to the classical quadratic formula, but frames root finding through the derivative expression $f'(x) = 2Ax + B$.

---

## 2. Required Verified Examples

### Example 1: Canonical Monic ($x^2 - 5x + 6 = 0$)
- **Coefficients**: $A = 1, B = -5, C = 6$.
- **Derivative**: $f'(x) = 2x - 5$.
- **Discriminant**: $D = (-5)^2 - 4(1)(6) = 25 - 24 = 1$ (Perfect Square: $\sqrt{1} = 1$).
- **Branches**:
  - Plus branch: $2x - 5 = +1 \implies 2x = 6 \implies x = 3$.
  - Minus branch: $2x - 5 = -1 \implies 2x = 4 \implies x = 2$.
- **Roots**: $x = 3, x = 2$.
- **Invariant Verification**: $f'(3) = 2(3) - 5 = 1 \implies 1^2 = 1 = D \checkmark$. $f'(2) = 2(2) - 5 = -1 \implies (-1)^2 = 1 = D \checkmark$.
- **Polynomial Verification**: $(3)^2 - 5(3) + 6 = 0 \checkmark$, $(2)^2 - 5(2) + 6 = 0 \checkmark$.
- **Classification**: `TWO_REAL_ROOTS`.

### Example 2: Exact Non-Monic ($2x^2 - 2x - 12 = 0$)
- **Coefficients**: $A = 2, B = -2, C = -12$.
- **Derivative**: $f'(x) = 4x - 2$.
- **Discriminant**: $D = (-2)^2 - 4(2)(-12) = 4 + 96 = 100$ (Perfect Square: $\sqrt{100} = 10$).
- **Branches**:
  - Plus branch: $4x - 2 = +10 \implies 4x = 12 \implies x = 3$.
  - Minus branch: $4x - 2 = -10 \implies 4x = -8 \implies x = -2$.
- **Roots**: $x = 3, x = -2$.
- **Invariant Verification**: $f'(3) = 10 \implies 10^2 = 100 = D \checkmark$. $f'(-2) = -10 \implies (-10)^2 = 100 = D \checkmark$.
- **Classification**: `TWO_REAL_ROOTS`.

### Example 3: Repeated Root ($x^2 - 6x + 9 = 0$)
- **Coefficients**: $A = 1, B = -6, C = 9$.
- **Derivative**: $f'(x) = 2x - 6$.
- **Discriminant**: $D = (-6)^2 - 4(1)(9) = 36 - 36 = 0$.
- **Branch**: $2x - 6 = 0 \implies x = 3$.
- **Root**: $x = 3$ (repeated).
- **Classification**: `REPEATED_REAL_ROOT`.

### Example 4: No Real Roots ($x^2 + 2x + 5 = 0$)
- **Coefficients**: $A = 1, B = 2, C = 5$.
- **Discriminant**: $D = 2^2 - 4(1)(5) = 4 - 20 = -16 < 0$.
- **Classification**: `NO_REAL_ROOTS`.

### Example 5: Non-Square Positive Discriminant ($2x^2 - x - 12 = 0$)
- **Coefficients**: $A = 2, B = -1, C = -12$.
- **Discriminant**: $D = (-1)^2 - 4(2)(-12) = 1 + 96 = 97$ (not a perfect square).
- **Classification**: `IRRATIONAL_ROOTS_DEFERRED`.

---

## 3. Classification Matrix

| Classification | Condition | Example |
|---|---|---|
| `TWO_REAL_ROOTS` | $A \neq 0, D > 0$ and $D$ is a perfect square | $x^2-5x+6 = 0 \implies D=1 \implies x=3, 2$ |
| `REPEATED_REAL_ROOT` | $A \neq 0, D = 0$ | $x^2-6x+9 = 0 \implies D=0 \implies x=3$ |
| `NO_REAL_ROOTS` | $A \neq 0, D < 0$ | $x^2+2x+5 = 0 \implies D=-16$ |
| `IRRATIONAL_ROOTS_DEFERRED` | $A \neq 0, D > 0$ but $D$ is not a perfect square | $2x^2-x-12 = 0 \implies D=97$ |
| `NOT_QUADRATIC` | $A = 0$ | $3x + 5 = 0$ |
| `OVERFLOW_RISK` | Coefficients exceed safe integer limits | $A, B, C > 10^9$ |
