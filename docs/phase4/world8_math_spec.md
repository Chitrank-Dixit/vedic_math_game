# Sankalana-Vyavakalanabhyam ("By Addition and By Subtraction") Specification

**World 8: Sankalana-Vyavakalanabhyam**  
**Method Scope:** Special-case solution for 2x2 simultaneous linear equations with swapped / mirror coefficients.

---

## 1. System Representation & Mathematical Derivation

Consider a 2x2 system of simultaneous linear equations with swapped coefficients:
$$a_1 x + b_1 y = c_1 \quad \text{--- (Eq 1)}$$
$$a_2 x + b_2 y = c_2 \quad \text{--- (Eq 2)}$$

### Primary Mirror Form:
Where $a_1 = a, b_1 = b$ and $a_2 = b, b_2 = a$:
$$a x + b y = c \quad \text{--- (Eq 1)}$$
$$b x + a y = d \quad \text{--- (Eq 2)}$$

### Derivation Steps:

1. **Addition Step (Sum / Combined Equation)**:
   Adding Eq 1 and Eq 2:
   $$(a + b)x + (a + b)y = c + d$$
   $$(a + b)(x + y) = c + d$$
   Provided $a + b \ne 0$:
   $$x + y = \frac{c + d}{a + b} = S \quad \text{--- (Exact Fraction } S\text{)}$$

2. **Subtraction Step (Difference Equation)**:
   Subtracting Eq 2 from Eq 1:
   $$(a - b)x - (a - b)y = c - d$$
   $$(a - b)(x - y) = c - d$$
   Provided $a - b \ne 0$:
   $$x - y = \frac{c - d}{a - b} = D_f \quad \text{--- (Exact Fraction } D_f\text{)}$$

3. **Recombination Step (Final Variables)**:
   Now we have:
   $$x + y = S$$
   $$x - y = D_f$$

   - Solve $x$: $x = \frac{S + D_f}{2}$
   - Solve $y$: $y = \frac{S - D_f}{2}$

---

## 2. Classification Rules for Exceptional & Degenerate Cases

Let System Determinant $\Delta = a^2 - b^2 = (a + b)(a - b)$.

1. **`UNIQUE_SOLUTION`**: $a + b \ne 0$ AND $a - b \ne 0$ ($\Delta \ne 0$). System yields unique exact rational pair $(x, y)$.
2. **`NOT_APPLICABLE`**: System is not of mirror form ($a_1 \ne b_2$ or $b_1 \ne a_2$).
3. **`DEGENERATE`**: $a + b = 0$ ($a = -b$) or $a - b = 0$ ($a = b$), where addition or subtraction loses variable information.
4. **`INFINITE_SOLUTIONS`**: $\Delta = 0$ ($a = b$ or $a = -b$) and constants match identically ($c = d$).
5. **`INCONSISTENT`**: $\Delta = 0$ but constants contradict ($c \ne d$).

---

## 3. Derivation of Worked Seed Examples

### Seed Example 1 (Canonical Case):
$$45x - 23y = 113 \quad \text{(Eq 1)}$$
$$23x - 45y = 91 \quad \text{(Eq 2)}$$

- Addition: $(45 + 23)x + (-23 - 45)y = 113 + 91 \implies 68x - 68y = 204 \implies x - y = 3$.
- Subtraction (Eq 1 - Eq 2): $(45 - 23)x + (-23 - (-45))y = 113 - 91 \implies 22x + 22y = 22 \implies x + y = 1$.
- Recombination: $x + y = 1, x - y = 3 \implies x = 2, y = -1$.
- Verification:
  - Eq 1: $45(2) - 23(-1) = 90 + 23 = 113 \checkmark$
  - Eq 2: $23(2) - 45(-1) = 46 + 45 = 91 \checkmark$

### Seed Example 2 (Positive Beginner Case):
$$4x + 7y = 5 \quad \text{(Eq 1)}$$
$$7x + 4y = 17 \quad \text{(Eq 2)}$$

- Addition: $(4 + 7)(x + y) = 5 + 17 \implies 11(x + y) = 22 \implies x + y = 2$.
- Subtraction: $(4 - 7)x + (7 - 4)y = 5 - 17 \implies -3x + 3y = -12 \implies x - y = 4$.
- Recombination: $x + y = 2, x - y = 4 \implies x = 3, y = -1$.
- Verification:
  - Eq 1: $4(3) + 7(-1) = 12 - 7 = 5 \checkmark$
  - Eq 2: $7(3) + 4(-1) = 21 - 4 = 17 \checkmark$

### Seed Example 3 (Simple Visual Tutorial Case):
$$x + y = 10$$
$$x - y = 2$$

- Direct sum/difference recombination: $2x = 12 \implies x = 6$, $2y = 8 \implies y = 4$. $\checkmark$
