# World 11 Mathematical Specification: Vyashtisamashtih

**Document Status:** Complete Mathematical Specification / World 11  
**Sutra Name:** *Vyashtisamashtih* ("Part and Whole")  
**Target Domain:** Symmetric products around an average via difference of squares $(A-d)(A+d) = A^2 - d^2$  

---

## 1. Executive Summary & Core Theorem

The Sutra *Vyashtisamashtih* states: *"Vyashtisamashtih"* — part and whole.

For the initial release of World 11, we implement the symmetric-product pattern for multiplying two factors $p$ and $q$ by identifying their midpoint (whole average $A$) and equal deviations (parts $-d$ and $+d$):

Given factors $p$ and $q$:
1. **Average (Whole)**:
   $$A = \frac{p + q}{2}$$

2. **Half-Difference (Deviation / Parts)**:
   $$d = \frac{q - p}{2} \quad (\text{assuming } p \le q)$$

3. **Symmetric Decomposition**:
   $$p \cdot q = (A - d)(A + d)$$

4. **Difference of Squares Identity**:
   $$(A - d)(A + d) = A^2 - d^2$$

---

## 2. Derivation & Steps

Given factors $p$ and $q$:
- **Step 1 (Find Average)**: Compute $A = (p + q) / 2$.
- **Step 2 (Find Deviation)**: Compute $d = (q - p) / 2$.
- **Step 3 (Square Whole)**: Compute $A^2$.
- **Step 4 (Square Deviation)**: Compute $d^2$.
- **Step 5 (Subtract)**: Compute $A^2 - d^2$.
- **Step 6 (Verify)**: Verify $A^2 - d^2 = p \times q$.

---

## 3. Required Verified Examples

### Example 1: Canonical Average ($58 \times 62$)
- **Factors**: $p = 58, q = 62$.
- **Average**: $A = (58 + 62) / 2 = 120 / 2 = 60$.
- **Deviation**: $d = (62 - 58) / 2 = 4 / 2 = 2$.
- **Decomposition**: $58 \times 62 = (60 - 2)(60 + 2)$.
- **Difference of Squares**: $60^2 - 2^2 = 3600 - 4 = 3596$.
- **Direct Multiplication Verification**: $58 \times 62 = 3596 \checkmark$.

### Example 2: Positive Non-Round Average ($25 \times 31$)
- **Factors**: $p = 25, q = 31$.
- **Average**: $A = (25 + 31) / 2 = 56 / 2 = 28$.
- **Deviation**: $d = (31 - 25) / 2 = 6 / 2 = 3$.
- **Decomposition**: $25 \times 31 = (28 - 3)(28 + 3)$.
- **Difference of Squares**: $28^2 - 3^2 = 784 - 9 = 775$.
- **Direct Multiplication Verification**: $25 \times 31 = 775 \checkmark$.

### Example 3: Fractional Average ($12 \times 15$)
- **Factors**: $p = 12, q = 15$.
- **Average**: $A = (12 + 15) / 2 = 27 / 2 = 13.5$.
- **Deviation**: $d = (15 - 12) / 2 = 3 / 2 = 1.5$.
- **Decomposition**: $12 \times 15 = \left(\frac{27}{2} - \frac{3}{2}\right)\left(\frac{27}{2} + \frac{3}{2}\right)$.
- **Difference of Squares**: $\left(\frac{27}{2}\right)^2 - \left(\frac{3}{2}\right)^2 = \frac{729}{4} - \frac{9}{4} = \frac{720}{4} = 180$.
- **Direct Multiplication Verification**: $12 \times 15 = 180 \checkmark$.

---

## 4. Classification Matrix

| Classification | Condition | Value Nature | Example |
|---|---|---|---|
| `SUPPORTED_INTEGER_PATH` | $p + q$ is even $\implies A \in \mathbb{Z}, d \in \mathbb{Z}$ | Whole integer average and deviation | $58 \times 62 = 60^2 - 2^2 = 3596$ |
| `SUPPORTED_FRACTION_PATH` | $p + q$ is odd $\implies A \in \mathbb{Q}, d \in \mathbb{Q}$ | Fractional average and deviation | $12 \times 15 = (27/2)^2 - (3/2)^2 = 180$ |
| `UNSUPPORTED_SYMBOLIC_FORM` | Symbolic polynomial expression | Symbolic factorization (deferred) | $x^2 + 5x + 6$ |
| `OVERFLOW_RISK` | $A^2$ or $d^2$ exceeds safe bounds | Large values causing `Long` overflow | $p, q > 10^9$ |
