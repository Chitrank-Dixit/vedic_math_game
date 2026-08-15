# World 14 Mathematical Specification: Gunitasamuccayah

**Document Status:** Complete Mathematical Specification / World 14  
**Sutra Name:** *Gunitasamuccayah* ("The product of the sum is equal to the sum of the products")  
**Target Domain:** Factorization verification for univariate quadratic polynomials $P(x) = F_1(x) \cdot F_2(x)$  

---

## 1. Mathematical Invariant & Fundamental Principles

For univariate polynomial $P(x)$ factored into linear components $F_1(x) \dots F_k(x)$:

Let $SC(Q)$ denote the **Sum of Coefficients** of polynomial $Q(x)$, which equals evaluating $Q(1)$:
$$SC(Q) = Q(1) = \sum_{j=0}^n a_j$$

### The Invariant
$$\text{Product of Sum of Factor Coefficients} = \text{Sum of Coefficients of Product}$$
$$SC(F_1) \times SC(F_2) = SC(P)$$

### Necessary vs. Sufficient Condition
> [!IMPORTANT]
> The coefficient-sum check is a **necessary consistency check**, but **NOT a sufficient proof** of correct factorization. Different polynomials can share the same value at $x = 1$.
> Therefore, every factorization proposal must undergo:
> 1. **Quick Consistency Check**: $SC(F_1) \cdot SC(F_2) \overset{?}{=} SC(P_{\text{claimed}})$.
> 2. **Exact Expansion Comparison**: $F_1(x) \cdot F_2(x) \overset{?}{=} P_{\text{claimed}}(x)$ coefficient by coefficient.

---

## 2. Linear Factorization Domain ($k=2$)

For linear factors $F_1(x) = a \cdot x + b$ and $F_2(x) = c \cdot x + d$:

1. **Exact Product Expansion**:
   $$P_{\text{exact}}(x) = (a \cdot c)x^2 + (a \cdot d + b \cdot c)x + (b \cdot d)$$
2. **Coefficient Sums**:
   - $SC(F_1) = a + b$
   - $SC(F_2) = c + d$
   - $SC(\text{factors}) = (a + b) \cdot (c + d)$
   - $SC(P_{\text{exact}}) = a \cdot c + (a \cdot d + b \cdot c) + b \cdot d = (a + b)(c + d)$

---

## 3. Required Verified Examples

### Example 1: Canonical Positive
- **Factors**: $F_1(x) = x + 3$, $F_2(x) = x + 2$.
- **Exact Expansion**: $x^2 + 5x + 6$.
- **Evaluation at $x=1$**:
  - $SC(F_1) = 1 + 3 = 4$, $SC(F_2) = 1 + 2 = 3 \implies SC(\text{factors}) = 4 \times 3 = 12$.
  - $SC(P) = 1 + 5 + 6 = 12$.
- **Check**: $12 = 12 \checkmark$, Exact coefficients $[1, 5, 6] == [1, 5, 6] \checkmark$.
- **Classification**: `VALID_FACTORISATION`.

### Example 2: Signed Coefficients
- **Factors**: $F_1(x) = x - 4$, $F_2(x) = 2x + 5$.
- **Exact Expansion**: $2x^2 - 3x - 20$.
- **Evaluation at $x=1$**:
  - $SC(F_1) = 1 - 4 = -3$, $SC(F_2) = 2 + 5 = 7 \implies SC(\text{factors}) = -21$.
  - $SC(P) = 2 - 3 - 20 = -21$.
- **Check**: $-21 = -21 \checkmark$, Exact coefficients $[2, -3, -20] == [2, -3, -20] \checkmark$.
- **Classification**: `VALID_FACTORISATION`.

### Example 3: False Proposal (Sum Check Passes, Expansion Fails)
- **Factors**: $F_1(x) = x + 3$, $F_2(x) = x + 2$.
- **Claimed Product**: $x^2 + 7x + 4$.
- **Evaluation at $x=1$**:
  - $SC(\text{factors}) = (1+3)(1+2) = 12$.
  - $SC(P_{\text{claimed}}) = 1 + 7 + 4 = 12$.
- **Check**: Sum check passes ($12 = 12$), but exact coefficients $[1, 5, 6] \neq [1, 7, 4]$.
- **Classification**: `INVALID_EXPANSION`.

### Example 4: Zero Coefficient Sum Edge Case
- **Factors**: $F_1(x) = x - 1$, $F_2(x) = x + 5$.
- **Exact Expansion**: $x^2 + 4x - 5$.
- **Evaluation at $x=1$**:
  - $SC(F_1) = 1 - 1 = 0 \implies SC(\text{factors}) = 0 \times 6 = 0$.
  - $SC(P) = 1 + 4 - 5 = 0$.
- **Check**: $0 = 0 \checkmark$, Exact coefficients match.
- **Classification**: `VALID_FACTORISATION`.

---

## 4. Classification & Scope Matrix

| Classification | Coefficient Sum Check | Exact Expansion Check | Meaning |
|---|---|---|---|
| `VALID_FACTORISATION` | PASSED | PASSED | Identity holds completely |
| `INVALID_EXPANSION` | PASSED | FAILED | Sum matched by coincidence, coefficients differ |
| `COEFFICIENT_SUM_MISMATCH` | FAILED | FAILED | Sum check failed, invalid expansion |
| `UNSUPPORTED_FORM` | N/A | N/A | Non-linear factor or non-quadratic target |
| `OVERFLOW_RISK` | N/A | N/A | Coefficients exceed safe `Long` range |
