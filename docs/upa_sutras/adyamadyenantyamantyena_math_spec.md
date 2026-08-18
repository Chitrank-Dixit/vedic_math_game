# Mathematical Specification & Overlap Analysis: Upa-Sutra 3 — Adyamadyenantyamantyena

**Document Status:** Complete Mathematical Specification & Overlap Analysis / Phase U2  
**Feature:** Upa-Sutra Treasury — Quest 4  
**Target Class:** `AdyamadyenantyamantyenaGenerator`  
**Parent Worlds:** World 5 (Urdhva-Tiryagbhyam) & World 10 (Puranapuranabhyam)  
**Unlock Requirement:** Completion of World 5 (Urdhva-Tiryagbhyam)  

---

## 1. Pre-Flight Correction: Monic vs. Non-Monic Scope Resolution

> [!IMPORTANT]
> **Pre-Flight Correction Resolution**:
> - The initial Master Plan draft described Upa-Sutra 3 as factoring "monic quadratic expressions", but its canonical worked example `2x² + 7x + 5 = (2x+5)(x+1)` is **non-monic** ($A = 2 \neq 1$).
> - **Scope Resolution**: This quest is strictly scoped to **non-monic quadratic factorization ($A \ge 2$) via first-term/last-term candidate pairing and crosswise validation**. Monic quadratics ($A = 1$) are already mastered in World 15 (*Gunakasamuccayah*) and are routed/classified explicitly as `MONIC_DEFERRED_TO_WORLD_15`.

---

## 2. Overlap Comparison: World 15, World 5, and Upa-Sutra 3

| Domain Aspect | World 5 (Urdhva-Tiryagbhyam) | World 15 (Gunakasamuccayah) | Upa-Sutra 3 (Adyamadyenantyamantyena) |
|---|---|---|---|
| **Mathematical Operation** | Forward crosswise multiplication of polynomials: $(a_1 x + c_1)(a_2 x + c_2)$. | Monic quadratic factorization: $x^2 + Bx + C = (x+p)(x+q)$. | **Inverse crosswise factorization of non-monic quadratics**: $Ax^2 + Bx + C = (a_1 x + c_1)(a_2 x + c_2)$. |
| **Leading Coefficient ($A$)** | Any integer ($A = a_1 a_2$). | Strictly monic ($A = 1$). | **Non-monic** ($A \ge 2$, e.g., $A = 2, 3, 4, 5, 6$). |
| **Core Method** | First $\times$ First ($a_1 a_2$), Cross-products ($a_1 c_2 + a_2 c_1$), Last $\times$ Last ($c_1 c_2$). | Find factor pairs $(p, q)$ of $C$ such that $p + q = B$. | Find first pairs $(a_1, a_2)$ of $A$ and last pairs $(c_1, c_2)$ of $C$; validate cross-term $a_1 c_2 + a_2 c_1 = B$. |
| **Pedagogical Relationship** | Forward foundation (World 5). | Elementary monic stepping-stone (World 15). | **Natural synthesis & reverse mastery** of World 5. |

**Conclusion**: The scope is sharply distinct and pedagogically necessary.

---

## 3. Step-by-Step Factoring Algorithm

Given a quadratic polynomial $P(x) = Ax^2 + Bx + C$ with $A \ge 2$:

1. **Step 1 — List First-Term Pairs**:
   Find all positive integer factor pairs $(a_1, a_2)$ such that $a_1 a_2 = A$ with $a_1 \ge a_2 \ge 1$.
2. **Step 2 — List Last-Term Pairs**:
   Find all integer factor pairs $(c_1, c_2)$ such that $c_1 c_2 = C$. (Include signed permutations if $C < 0$ or $B < 0$).
3. **Step 3 — Form Candidate Factorizations**:
   For each first-pair $(a_1, a_2)$ and last-pair $(c_1, c_2)$, construct candidate $(a_1 x + c_1)(a_2 x + c_2)$ and $(a_1 x + c_2)(a_2 x + c_1)$.
4. **Step 4 — Cross-Term Evaluation**:
   Compute cross-term:
   $$\text{Cross-Term} = a_1 c_2 + a_2 c_1$$
   - If $a_1 c_2 + a_2 c_1 = B$, factorization $(a_1 x + c_1)(a_2 x + c_2)$ is **CONFIRMED**.
   - If not, test remaining candidate permutations.
5. **Step 5 — Unfactorability Check**:
   If no candidate pair satisfies the cross-term check, verify discriminant:
   $$D = B^2 - 4AC$$
   If $D < 0$ or $\sqrt{D} \notin \mathbb{Z}$, classify as `NO_INTEGER_FACTORIZATION`.

---

## 4. Verified Examples

### Example 1 (Canonical Non-Monic):
- **Problem**: $2x^2 + 7x + 5$
- **First-Term Pairs ($A = 2$)**: $(2, 1)$
- **Last-Term Pairs ($C = 5$)**: $(5, 1), (1, 5)$
- **Candidate 1**: $(2x + 5)(x + 1) \implies \text{Cross-term: } 2(1) + 1(5) = 2 + 5 = 7 \checkmark$ ($B = 7$)
- **Factorization**: $(2x + 5)(x + 1)$
- **Verification**: $2x^2 + 2x + 5x + 5 = 2x^2 + 7x + 5 \checkmark$

### Example 2 (Negative Coefficients):
- **Problem**: $3x^2 - 5x - 2$
- **First-Term Pairs ($A = 3$)**: $(3, 1)$
- **Last-Term Pairs ($C = -2$)**: $(1, -2), (-1, 2), (2, -1), (-2, 1)$
- **Candidate Test**:
  - $(3x + 2)(x - 1) \implies 3(-1) + 1(2) = -3 + 2 = -1 \neq -5$ ✗
  - $(3x + 1)(x - 2) \implies 3(-2) + 1(1) = -6 + 1 = -5 \checkmark$ ($B = -5$)
- **Factorization**: $(3x + 1)(x - 2)$
- **Verification**: $3x^2 - 6x + x - 2 = 3x^2 - 5x - 2 \checkmark$

### Example 3 (Unfactorable / Discriminant Check):
- **Problem**: $2x^2 + 3x + 4$
- **First-Term Pairs ($A = 2$)**: $(2, 1)$
- **Last-Term Pairs ($C = 4$)**: $(4, 1), (1, 4), (2, 2)$
- **Candidates**:
  - $(2x + 4)(x + 1) \implies 2(1) + 1(4) = 6 \neq 3$
  - $(2x + 1)(x + 4) \implies 2(4) + 1(1) = 9 \neq 3$
  - $(2x + 2)(x + 2) \implies 2(2) + 1(2) = 6 \neq 3$
- **Discriminant**: $D = 3^2 - 4(2)(4) = 9 - 32 = -23 < 0$
- **Classification**: `NO_INTEGER_FACTORIZATION` $\checkmark$

---

## 5. Classification Schema

| Classification | Condition |
|---|---|
| `FACTORABLE_NON_MONIC` | $A \ge 2$, integer candidate pair matches $a_1 c_2 + a_2 c_1 = B$. |
| `NO_INTEGER_FACTORIZATION` | Discriminant $D = B^2 - 4AC$ is not a perfect square or no integer factors exist. |
| `MONIC_DEFERRED_TO_WORLD_15` | $A = 1$ (handled by World 15 Gunakasamuccayah). |
| `OVERFLOW_RISK` | $|A|, |B|, |C| > 10^5$. |
