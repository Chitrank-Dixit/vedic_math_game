# Anurupye Shunyamanyat ("If One Is In Ratio, The Other Is Zero") Specification

**World 7: Anurupye Shunyamanyat**  
**Method Scope:** Special-case solution for simultaneous linear equations of 2 variables.  

---

## 1. System Representation & Mathematical Formulation

Consider a 2x2 system of simultaneous linear equations:
$$a_1 x + b_1 y = c_1 \quad \text{--- (Eq 1)}$$
$$a_2 x + b_2 y = c_2 \quad \text{--- (Eq 2)}$$

Define the **System Determinant**:
$$\Delta = a_1 b_2 - a_2 b_1$$

---

## 2. Applicability Conditions & Decision Tree

### Case A: $y = 0$ ($x$-coefficient ratio matches constant ratio)
- **Condition**: $a_1 c_2 == a_2 c_1$ AND $\Delta \ne 0$.
- **Inferred Zero Variable**: $y = 0$.
- **Non-Zero Variable Solution**: $x = \frac{c_1}{a_1} = \frac{c_2}{a_2}$ (expressed as an exact irreducible fraction $\frac{P}{Q}$).
- **Verification Identity**:
  - Eq 1: $a_1 (c_1/a_1) + b_1 (0) = c_1 \checkmark$
  - Eq 2: $a_2 (c_2/a_2) + b_2 (0) = c_2 \checkmark$

### Case B: $x = 0$ ($y$-coefficient ratio matches constant ratio)
- **Condition**: $b_1 c_2 == b_2 c_1$ AND $\Delta \ne 0$.
- **Inferred Zero Variable**: $x = 0$.
- **Non-Zero Variable Solution**: $y = \frac{c_1}{b_1} = \frac{c_2}{b_2}$ (expressed as an exact irreducible fraction $\frac{P}{Q}$).
- **Verification Identity**:
  - Eq 1: $a_1 (0) + b_1 (c_1/b_1) = c_1 \checkmark$
  - Eq 2: $a_2 (0) + b_2 (c_2/b_2) = c_2 \checkmark$

---

## 3. Classification Rules for Exceptional Cases

1. **`NOT_APPLICABLE`**: Neither $a_1 c_2 == a_2 c_1$ nor $b_1 c_2 == b_2 c_1$.
   - *Behavior*: Reject from Sutra solver pool with explicit message "Sutra does not apply to this system."
2. **`INFINITE_SOLUTIONS`**: Both $a_1 c_2 == a_2 c_1$ and $b_1 c_2 == b_2 c_1$, AND $\Delta = 0$.
   - *Behavior*: Equations are coincident; system has infinitely many solutions.
3. **`INCONSISTENT`**: $\Delta = 0$ but constant ratio does not match coefficients.
   - *Behavior*: Parallel lines; no solution exists.

---

## 4. Derivation of Worked Seed Examples

### Seed Example 1 (Canonical $y = 0$ Case):
$$3x + 2y = 12$$
$$6x + 5y = 24$$

- **Coefficient & Constant Matrix**:
  - $a_1 = 3, b_1 = 2, c_1 = 12$
  - $a_2 = 6, b_2 = 5, c_2 = 24$
- **Ratio Test**: $a_1 c_2 = 3 \times 24 = 72$, $a_2 c_1 = 6 \times 12 = 72$. $72 == 72 \implies \text{Matches!}$
- **Determinant Test**: $\Delta = (3 \times 5) - (6 \times 2) = 15 - 12 = 3 \ne 0$.
- **Inferred Zero Variable**: $y = 0$.
- **Solve $x$**: $x = 12 / 3 = 4$.
- **Exact Solution**: $x = 4, y = 0$. $\checkmark$

### Seed Example 2 (Symmetric $x = 0$ Case):
$$6x + 7y = 8$$
$$19x + 14y = 16$$

- **Coefficient & Constant Matrix**:
  - $a_1 = 6, b_1 = 7, c_1 = 8$
  - $a_2 = 19, b_2 = 14, c_2 = 16$
- **Ratio Test**: $b_1 c_2 = 7 \times 16 = 112$, $b_2 c_1 = 14 \times 8 = 112$. $112 == 112 \implies \text{Matches!}$
- **Determinant Test**: $\Delta = (6 \times 14) - (19 \times 7) = 84 - 133 = -49 \ne 0$.
- **Inferred Zero Variable**: $x = 0$.
- **Solve $y$**: $y = 8 / 7$.
- **Exact Solution**: $x = 0, y = 8/7$. $\checkmark$
