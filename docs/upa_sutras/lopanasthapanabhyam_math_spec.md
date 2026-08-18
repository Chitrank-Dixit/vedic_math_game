# Mathematical Specification: Upa-Sutra 11 — Lopanasthapanabhyam ("By Elimination and Retention")

**Document Status:** Complete Mathematical Specification & Verification Gate Passed / Phase U3  
**Feature:** Upa-Sutra Treasury — Quest 8 (Tier B)  
**Target Class:** `LopanasthapanabhyamGenerator`  
**Parent World:** World 8 (Sankalana-Vyavakalanabhyam)  
**Unlock Requirement:** Completion of World 8 (Sankalana-Vyavakalanabhyam)  

---

## 1. Critical Correction Gate & Literature Audit

### Correction Gate Verification:
The canonical identity was tested by explicit algebraic expansion:
$$(2x + y + 2)(x + 2y + 1)$$
$$= (2x)(x + 2y + 1) + (y)(x + 2y + 1) + 2(x + 2y + 1)$$
$$= (2x^2 + 4xy + 2x) + (xy + 2y^2 + y) + (2x + 4y + 2)$$
$$= 2x^2 + (4xy + xy) + 2y^2 + (2x + 2x) + (y + 4y) + 2$$
$$= 2x^2 + 5xy + 2y^2 + 4x + 5y + 2$$
**Verdict**: The expansion matches the target bivariate polynomial $2x^2 + 5xy + 2y^2 + 4x + 5y + 2$ **EXACTLY**.

### Literature Audit:
1. **Swami Bharati Krishna Tirtha (Vedic Mathematics, 1965, Ch. 11 & 14)**:
   - Identifies *Lopana-Sthāpanābhyāṃ* ("By Elimination and Retention") as the systematic method for factorizing bivariate second-degree polynomials $Ax^2 + Bxy + Cy^2 + Dx + Ey + F$ into linear binomial/trinomial factors $(px + qy + r)(sx + ty + u)$.
   - Setting $y = 0$ reduces the expression to a single-variable quadratic in $x$; setting $x = 0$ reduces it to a quadratic in $y$. Retaining both sets of linear factors and matching constant terms recovers the bivariate linear factors, which are then verified by expanding the $xy$ term.
2. **Kenneth Williams & Mark Gaskell (The Cosmic Computer)**:
   - Details the 4-step algorithm: (1) Put $y=0$ and factor $f(x,0)$, (2) Put $x=0$ and factor $f(0,y)$, (3) Pair linear parts matching constant terms, (4) Verify $xy$ cross-term.
3. **Dr. S.K. Kapoor (Vedic Mathematical Concepts)**:
   - Confirms *Lopana-Sthāpanābhyāṃ* as the standard algebraic algorithm for non-homogeneous bivariate quadratic factorization.

### Verification Verdict & Confidence Level:
- **Consensus**: **Unanimous Agreement** across all classical and modern references.
- **Confidence Rating**: **`HIGH`**
- **Decision**: **Gate Passed**. Proceed to implementation.

---

## 2. Mathematical Foundation & Algorithmic Process

### Bivariate Quadratic General Form:
$$P(x, y) = Ax^2 + Bxy + Cy^2 + Dx + Ey + F$$
Factored Form:
$$P(x, y) = (px + qy + r)(sx + ty + u)$$

Where:
- $A = ps$
- $B = pt + qs$ (the mixed cross-term)
- $C = qt$
- $D = pu + rs$
- $E = qu + rt$
- $F = ru$

### Step-by-Step Algorithm:

1. **Step 1: Eliminate $y$ ($y = 0$)**:
   $$P(x, 0) = Ax^2 + Dx + F = (px + r)(sx + u)$$
   Factor the univariate quadratic in $x$.

2. **Step 2: Eliminate $x$ ($x = 0$)**:
   $$P(0, y) = Cy^2 + Ey + F = (qy + r)(ty + u)$$
   Factor the univariate quadratic in $y$.

3. **Step 3: Retain & Recombine**:
   Match linear factors based on common constant terms $r$ and $u$:
   - Combine $(px + r)$ with $(qy + r) \implies (px + qy + r)$
   - Combine $(sx + u)$ with $(ty + u) \implies (sx + ty + u)$

4. **Step 4: Expand & Cross-Verify**:
   Compute the candidate mixed term $(px)(ty) + (qy)(sx) = (pt + qs)xy$.
   Verify that $pt + qs = B$. If true, the factorization is confirmed.

---

## 3. Required Verified Examples

### Example 1: Canonical Example (Positive Coefficients)
$$P(x, y) = 2x^2 + 5xy + 2y^2 + 4x + 5y + 2$$
- Eliminate $y$ ($y=0$): $P(x, 0) = 2x^2 + 4x + 2 = (2x + 2)(x + 1)$
- Eliminate $x$ ($x=0$): $P(0, y) = 2y^2 + 5y + 2 = (y + 2)(2y + 1)$
- Recombine matching constants:
  - Constant 2: $(2x + 2)$ and $(y + 2) \implies (2x + y + 2)$
  - Constant 1: $(x + 1)$ and $(2y + 1) \implies (x + 2y + 1)$
- Verify $xy$ term: $(2x)(2y) + (y)(x) = 4xy + xy = 5xy = B \checkmark$
- Confirmed Factors: **$(2x + y + 2)(x + 2y + 1)$**

### Example 2: Simpler Monic Example
$$P(x, y) = x^2 + 3xy + 2y^2 + 3x + 4y + 2$$
- Eliminate $y$ ($y=0$): $P(x, 0) = x^2 + 3x + 2 = (x + 1)(x + 2)$
- Eliminate $x$ ($x=0$): $P(0, y) = 2y^2 + 4y + 2 = (y + 1)(2y + 2)$
- Recombine matching constants:
  - Constant 1: $(x + 1)$ and $(y + 1) \implies (x + y + 1)$
  - Constant 2: $(x + 2)$ and $(2y + 2) \implies (x + 2y + 2)$
- Verify $xy$ term: $(x)(2y) + (y)(x) = 2xy + xy = 3xy = B \checkmark$
- Confirmed Factors: **$(x + y + 1)(x + 2y + 2)$**

### Example 3: Corrected Verified Negative Example
$$P(x, y) = x^2 - xy - 2y^2 - x - 4y - 2$$
- Eliminate $y$ ($y=0$): $P(x, 0) = x^2 - x - 2 = (x + 1)(x - 2)$
- Eliminate $x$ ($x=0$): $P(0, y) = -2y^2 - 4y - 2 = (y + 1)(-2y - 2)$
- Recombine matching constants:
  - Constant 1: $(x + 1)$ and $(y + 1) \implies (x + y + 1)$
  - Constant $-2$: $(x - 2)$ and $(-2y - 2) \implies (x - 2y - 2)$
- Verify $xy$ term: $(x)(-2y) + (y)(x) = -2xy + xy = -xy = B \checkmark$
- Confirmed Factors: **$(x + y + 1)(x - 2y - 2)$**

---

## 4. Scope Safeguards & Non-Examples

- **Constructive Problem Generation**: Problems are generated constructively from valid linear integer factors $(px + qy + r)(sx + ty + u)$.
- **Small Integer Bounds**: $p, q, s, t \in [-3, 3] \setminus \{0\}$, $r, u \in [-4, 4] \setminus \{0\}$.
- **Canonical Ordering**: Factors are sorted lexicographically by $(p, q, r)$ to ensure deterministic output.
- **Classification Schema**:
  - `FACTORED`: Valid factorization confirmed by exact coefficient match.
  - `AMBIGUOUS_RECOMBINATION`: Multiple candidate pairings tested; rejected candidate logged.
  - `UNSUPPORTED_NONFACTORIZABLE`: Non-factorable polynomial rejected.
  - `OVERFLOW_RISK`: Coefficients exceeding integer range rejected.
