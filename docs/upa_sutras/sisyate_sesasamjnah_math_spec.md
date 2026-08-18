# Mathematical Specification: Upa-Sutra 2 — Sisyate Sesasamjnah ("The Remainder Remains Constant")

**Document Status:** Complete Mathematical Specification & Verification Gate Passed / Phase U3  
**Feature:** Upa-Sutra Treasury — Quest 6 (Tier B)  
**Target Class:** `SisyateSesasamjnahGenerator`  
**Parent World:** World 6 (Paravartya Yojayet)  
**Unlock Requirement:** Completion of World 6 (Paravartya Yojayet)  

---

## 1. Mandatory Verification Gate & Literature Audit

### Source Cross-Check:
1. **Swami Bharati Krishna Tirtha (Vedic Mathematics, 1965, Ch. 7 & Appendix)**:
   - Identifies *Śiṣyate Śeṣasaṃjñaḥ* as the corollary sub-sutra to *Parāvartya Yojayet* (Transpose and Apply).
   - When dividing polynomial $P(x)$ by a binomial $(x - k)$, transposing the divisor yields root $k$. The sub-sutra dictates that direct substitution $P(k)$ yields the exact algebraic remainder without writing the full quotient tableau.
2. **Kenneth Williams & Mark Gaskell (The Cosmic Computer / Vedic Mathematics Teacher's Manual)**:
   - Confirms *Sisyate Shesasamjnah* establishes the **Polynomial Remainder Theorem** and **Factor Theorem** in Vedic Algebra.
3. **Dr. S.K. Kapoor (Vedic Mathematical Concepts)**:
   - Corroborates the formulation: $P(x) = (x - k) Q(x) + R \implies R = P(k)$.

### Verification Verdict & Confidence Level:
- **Consensus**: **Unanimous Agreement** that *Sisyate Shesasamjnah* designates the remainder in polynomial division via direct evaluation.
- **Confidence Rating**: **`HIGH`**
- **Decision**: **Gate Passed**. Proceed to implementation.

---

## 2. Mathematical Foundation

### Algebraic Theorem:
Let $P(x) = a_n x^n + a_{n-1} x^{n-1} + \dots + a_1 x + a_0$ be a polynomial with integer coefficients, divided by a linear divisor $D(x) = (x - k)$.

By Euclidean polynomial division:
$$P(x) = (x - k) Q(x) + R$$
where $Q(x)$ is the quotient polynomial of degree $n - 1$, and $R$ is a constant remainder ($\deg(R) < \deg(x - k) = 1$).

Substituting $x = k$:
$$P(k) = (k - k) Q(k) + R = 0 \cdot Q(k) + R = R$$

Hence:
$$\text{Remainder } R = P(k)$$

### Factor Confirmation (Factor Theorem):
If $R = P(k) = 0$, then $(x - k)$ is an exact factor of $P(x)$, and $x = k$ is a root of the equation $P(x) = 0$.

---

## 3. Supported Scope & Safeguards

- **Degrees Supported**:
  - **Tier 1**: Quadratic polynomials ($n = 2$), $P(x) = ax^2 + bx + c$, with positive integer $k \in [1, 5]$.
  - **Tier 2**: Cubic polynomials ($n = 3$), $P(x) = ax^3 + bx^2 + cx + d$, with positive integer $k \in [1, 4]$.
  - **Tier 3**: Quadratic/Cubic polynomials with negative $k \in [-4, -1]$ and curated zero-remainder cases ($R = 0$, factor confirmation).
- **Coefficient Bounds**: $|a_i| \le 20$, guaranteeing values well within safe 64-bit Long integers without overflow.
- **Independent Test Oracle**: An independent polynomial long division engine computes $Q(x)$ and $R$ directly, verifying $P(x) = (x - k)Q(x) + R$ for every problem.

---

## 4. Required Verified Examples

### Example 1: Canonical Cubic (Positive $k = 2$)
- **Polynomial**: $P(x) = x^3 - 3x^2 + 4x - 5$, Divisor: $(x - 2) \implies k = 2$.
- **Substitution**: $P(2) = (2)^3 - 3(2)^2 + 4(2) - 5 = 8 - 12 + 8 - 5 = -1$.
- **Long Division Oracle**:
  $$x^3 - 3x^2 + 4x - 5 = (x - 2)(x^2 - x + 2) - 1$$
- **Expansion Check**: $(x - 2)(x^2 - x + 2) - 1 = x^3 - x^2 + 2x - 2x^2 + 2x - 4 - 1 = x^3 - 3x^2 + 4x - 5 \checkmark$
- **Remainder**: $-1$ $\checkmark$

### Example 2: Zero-Remainder / Factor Confirmation ($k = 1$)
- **Polynomial**: $P(x) = x^3 - 6x^2 + 11x - 6$, Divisor: $(x - 1) \implies k = 1$.
- **Substitution**: $P(1) = 1^3 - 6(1)^2 + 11(1) - 6 = 1 - 6 + 11 - 6 = 0$.
- **Long Division Oracle**:
  $$x^3 - 6x^2 + 11x - 6 = (x - 1)(x^2 - 5x + 6) + 0 = (x - 1)(x - 2)(x - 3)$$
- **Expansion Check**: $(x - 1)(x^2 - 5x + 6) = x^3 - 5x^2 + 6x - x^2 + 5x - 6 = x^3 - 6x^2 + 11x - 6 \checkmark$
- **Remainder**: $0$ (Factor $(x - 1)$ confirmed!) $\checkmark$

### Example 3: Negative $k$ ($k = -2$, Divisor $x + 2$)
- **Polynomial**: $P(x) = 2x^2 + 3x - 5$, Divisor: $(x + 2) \implies k = -2$.
- **Substitution**: $P(-2) = 2(-2)^2 + 3(-2) - 5 = 2(4) - 6 - 5 = 8 - 11 = -3$.
- **Long Division Oracle**:
  $$2x^2 + 3x - 5 = (x + 2)(2x - 1) - 3$$
- **Expansion Check**: $(x + 2)(2x - 1) - 3 = 2x^2 - x + 4x - 2 - 3 = 2x^2 + 3x - 5 \checkmark$
- **Remainder**: $-3$ $\checkmark$

---

## 5. Classification Schema

| Classification | Condition |
|---|---|
| `NON_ZERO_REMAINDER` | $R = P(k) \neq 0$. |
| `ZERO_REMAINDER_FACTOR_CONFIRMED` | $R = P(k) = 0$ (Linear binomial $(x - k)$ is an exact factor). |
| `OVERFLOW_RISK` | $|P(k)| > 10^7$. |
