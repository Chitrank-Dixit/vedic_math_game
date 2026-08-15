# World 9 Mathematical Specification: Shunyam Samyasamuccaye

**Document Status:** Complete Mathematical Specification / World 9  
**Sutra Name:** *Shunyam Samyasamuccaye* ("When the sum is the same, that sum is zero")  
**Target Domain:** Special-pattern algebraic equations  

---

## 1. Executive Summary & Core Theorem

The Sutra *Shunyam Samyasamuccaye* states: *"Samuccaye Shunyam"* — when the sum (or common expression) is the same, that sum is equated to zero.

For the first release of World 9, we explicitly define and support two exact algebraic families. Any equation outside these two families is classified as `NOT_APPLICABLE` or `UNSUPPORTED`.

---

## 2. Supported Equation Families

### Family A: Common-Factor Equations

#### Mathematical Form
Equations normalized to:
$$k_1 \cdot F(x) = k_2 \cdot F(x)$$
where:
- $k_1 \ne k_2$ (scalar multipliers are unequal)
- $F(x) = a x + b$ is a linear expression with $a \ne 0$

#### Derivation & Shortcut Solution
Subtracting $k_2 F(x)$ from both sides yields:
$$(k_1 - k_2) \cdot F(x) = 0$$
Since $k_1 - k_2 \ne 0$, we divide by $(k_1 - k_2)$ to obtain:
$$F(x) = 0 \implies a x + b = 0 \implies x = -\frac{b}{a}$$

#### Canonical Worked Example A1
- **Equation**: $7(x + 1) = 8(x + 1)$
- **Identification**: Multipliers $k_1 = 7, k_2 = 8$ ($7 \ne 8$). Common factor $F(x) = x + 1$.
- **Shortcut Application**: $x + 1 = 0 \implies x = -1$.
- **Verification**: $7(-1 + 1) = 7(0) = 0$. $8(-1 + 1) = 8(0) = 0$. $0 = 0 \checkmark$.

#### Degenerate & Edge Cases (Family A)
- **Identity Case ($k_1 = k_2$)**: e.g., $7(x + 1) = 7(x + 1) \implies 0 = 0$. Valid for all $x$. Classified as `IDENTITY`.
- **Zero Coefficient ($a = 0$)**:
  - If $b \ne 0$: $k_1 b = k_2 b$ with $k_1 \ne k_2 \implies$ contradiction. Classified as `NO_SOLUTION`.
  - If $b = 0$: $0 = 0$. Classified as `IDENTITY`.
- **Mismatched Factors**: e.g., $7(x + 1) = 8(x + 2)$. Factors differ ($x+1 \ne x+2$). Classified as `NOT_APPLICABLE`.

---

### Family B: Equal-Numerator Reciprocal Equations

#### Mathematical Form
Equations of the form:
$$\frac{p}{a_1 x + b_1} + \frac{p}{a_2 x + b_2} = \frac{p}{a_3 x + b_3} + \frac{p}{a_4 x + b_4}$$
where:
- $p \ne 0$ is a non-zero shared numerator across all 4 terms.
- Linear denominators $D_1(x) = a_1 x + b_1, D_2(x) = a_2 x + b_2, D_3(x) = a_3 x + b_3, D_4(x) = a_4 x + b_4$.

#### Denominator Sum Condition
The sum of LHS denominators equals the sum of RHS denominators:
$$D_1(x) + D_2(x) = D_3(x) + D_4(x) = S(x)$$
where $S(x) = A x + B$ with $A = a_1 + a_2 = a_3 + a_4 \ne 0$.

#### Derivation & Shortcut Solution
Factoring $p$ out gives:
$$\frac{1}{D_1} + \frac{1}{D_2} = \frac{1}{D_3} + \frac{1}{D_4} \implies \frac{D_1 + D_2}{D_1 D_2} = \frac{D_3 + D_4}{D_3 D_4} \implies \frac{S(x)}{D_1 D_2} = \frac{S(x)}{D_3 D_4}$$
Cross-subtracting yields:
$$S(x) \left[ \frac{1}{D_1 D_2} - \frac{1}{D_3 D_4} \right] = 0$$
Setting $S(x) = 0$ yields:
$$A x + B = 0 \implies x = -\frac{B}{A}$$

#### Domain Safeguard & Excluded Values
Before accepting $x = -B/A$, we must verify that it does not make any denominator zero:
$$\text{Excluded Domain } E = \left\{ -\frac{b_1}{a_1}, -\frac{b_2}{a_2}, -\frac{b_3}{a_3}, -\frac{b_4}{a_4} \right\}$$
If candidate $x_0 \in E$, then $x_0$ is invalid and classified as `INVALID_DOMAIN`.

#### Canonical Worked Example B1
- **Equation**: $\frac{1}{x+2} + \frac{1}{x+3} = \frac{1}{x+1} + \frac{1}{x+4}$
- **Identification**: $p = 1$. $D_1 = x+2, D_2 = x+3, D_3 = x+1, D_4 = x+4$.
- **LHS Sum**: $(x+2) + (x+3) = 2x + 5$.
- **RHS Sum**: $(x+1) + (x+4) = 2x + 5$.
- **Common Sum $S(x)$**: $2x + 5$.
- **Shortcut Solution**: $2x + 5 = 0 \implies x = -5/2$.
- **Domain Safeguard Exclusions**: $x \ne -2, -3, -1, -4$. Candidate $x = -2.5 \notin E \checkmark$.
- **Verification**:
  - LHS: $\frac{1}{-2.5 + 2} + \frac{1}{-2.5 + 3} = \frac{1}{-0.5} + \frac{1}{0.5} = -2 + 2 = 0$.
  - RHS: $\frac{1}{-2.5 + 1} + \frac{1}{-2.5 + 4} = \frac{1}{-1.5} + \frac{1}{1.5} = -2/3 + 2/3 = 0$.
  - $0 = 0 \checkmark$.

---

## 3. Classification Matrix

| Classification | Condition | Example | Expected Output |
|---|---|---|---|
| `UNIQUE_SOLUTION` | Valid $x$ exists outside excluded domain | $7(x+1) = 8(x+1)$ or $\frac{1}{x+2} + \frac{1}{x+3} = \frac{1}{x+1} + \frac{1}{x+4}$ | Exact Fraction $x = -b/a$ |
| `IDENTITY` | $k_1 = k_2$ or identical denominators on both sides | $7(x+1) = 7(x+1)$ | All real numbers (Identity) |
| `NO_SOLUTION` | Parallel lines or impossible constant condition | $7(0x + 3) = 8(0x + 3) \implies 21 = 24$ | No solution |
| `INVALID_DOMAIN` | Candidate $x$ matches an excluded denominator root | $\frac{1}{x-1} + \frac{1}{x+1} = \frac{1}{x-1} + \frac{1}{x+1}$ where $x=1$ | Invalid (Division by zero) |
| `NOT_APPLICABLE` | Mismatched factors or denominator sums | $7(x+1) = 8(x+2)$ or $\frac{1}{x+1} + \frac{1}{x+2} = \frac{1}{x+3} + \frac{1}{x+5}$ | Shortcut does not apply |
| `UNSUPPORTED` | Higher-degree polynomials or non-matching numerators | $\frac{2}{x+1} + \frac{3}{x+2} = \frac{1}{x+3} + \frac{1}{x+4}$ | Rejected from generation |
