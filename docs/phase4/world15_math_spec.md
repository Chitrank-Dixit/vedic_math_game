# World 15 Mathematical Specification: Gunakasamuccayah

**Document Status:** Complete Mathematical Specification / World 15  
**Sutra Name:** *Gunakasamuccayah* ("The factors of the sum are equal to the sum of the factors")  
**Target Domain:** Monic quadratic factorization by constant-term integer factor pairs  

---

## 1. Executive Summary & Core Algorithm

The Sutra *Gunakasamuccayah* is applied here to structured monic quadratic factorization:
$$x^2 + Bx + C = 0$$

For a given monic quadratic with integer coefficients $B$ and $C$:

1. **Integer Factor Pair Search**:
   Find integer pair $(p, q)$ such that:
   $$p \cdot q = C \quad \text{and} \quad p + q = B$$

2. **Binomial Factorization**:
   $$x^2 + Bx + C = (x + p)(x + q) = 0$$

3. **Roots**:
   Setting each factor to zero:
   $$x + p = 0 \implies x = -p$$
   $$x + q = 0 \implies x = -q$$

4. **Root Verification**:
   Substitute $x = -p$ and $x = -q$ into original polynomial to confirm $0 = 0$.

---

## 2. Required Verified Examples

### Example 1: Positive Constant & Positive Middle Term ($x^2 + 7x + 10 = 0$)
- **Coefficients**: $B = 7, C = 10$.
- **Factor Pairs of 10**: $(1, 10), (2, 5), (-1, -10), (-2, -5)$.
- **Matching Pair**: $p = 2, q = 5 \implies p \cdot q = 10, p + q = 7$.
- **Factors**: $(x + 2)(x + 5) = 0$.
- **Roots**: $x = -2, x = -5$.
- **Verification**: $(-2)^2 + 7(-2) + 10 = 4 - 14 + 10 = 0 \checkmark$.
- **Classification**: `FACTORED`.

### Example 2: Negative Constant ($x^2 + x - 6 = 0$)
- **Coefficients**: $B = 1, C = -6$.
- **Matching Pair**: $p = 3, q = -2 \implies p \cdot q = -6, p + q = 1$.
- **Factors**: $(x + 3)(x - 2) = 0$.
- **Roots**: $x = -3, x = 2$.
- **Verification**: $(-3)^2 + (-3) - 6 = 9 - 9 = 0 \checkmark$.
- **Classification**: `FACTORED`.

### Example 3: Negative Middle Term ($x^2 - 9x + 20 = 0$)
- **Coefficients**: $B = -9, C = 20$.
- **Matching Pair**: $p = -4, q = -5 \implies p \cdot q = 20, p + q = -9$.
- **Factors**: $(x - 4)(x - 5) = 0$.
- **Roots**: $x = 4, x = 5$.
- **Verification**: $(4)^2 - 9(4) + 20 = 16 - 36 + 20 = 0 \checkmark$.
- **Classification**: `FACTORED`.

### Example 4: Non-Integer Factor Case ($x^2 + x + 1 = 0$)
- **Coefficients**: $B = 1, C = 1$.
- **Factor Pairs of 1**: $(1, 1) \implies \text{sum } 2$, $(-1, -1) \implies \text{sum } -2$. No sum equals 1.
- **Classification**: `NO_INTEGER_FACTOR_PAIR`.

---

## 3. Classification Matrix

| Classification | Condition | Example |
|---|---|---|
| `FACTORED` | Unique integer factor pair $(p, q)$ found ($p \neq q$) | $x^2+7x+10 \implies (x+2)(x+5)$ |
| `REPEATED_FACTOR` | Integer factor pair $(p, q)$ with $p = q$ | $x^2+6x+9 \implies (x+3)^2$ |
| `NO_INTEGER_FACTOR_PAIR` | No integer pair multiplies to $C$ and sums to $B$ | $x^2+x+1 = 0$ |
| `UNSUPPORTED_NON_MONIC` | Leading coefficient $A \neq 1$ | $2x^2+5x+3 = 0$ |
| `INVALID_INPUT` | Non-quadratic or invalid expression | N/A |
| `OVERFLOW_RISK` | Coefficients exceed safe `Long` range | $x^2 + 10^{15}x + 10^{30}$ |
