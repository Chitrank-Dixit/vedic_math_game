# Product Decision Memo: Upa-Sutra 13 — Gunitasamuccayah Samuccayagunitah

**Document Status:** Approved Consolidation Memo / Phase U4 (Final Tier C Resolution)  
**Target Subject:** Upa-Sutra 13 (*Gunitasamuccayah Samuccayagunitah* / गुणितसमुच्चयः समुच्चयगुणितः)  
**Classification:** Tier C (Compound Aphorism & Triple Overlap Resolution)  
**Parent World:** World 14 (*Gunitasamuccayah*) & Upa-Sutra 10  

---

## 1. Executive Product Decision

> ### **FINAL RECOMMENDATION: `CODEX_ONLY` (Consolidated with Upa-Sutra 10 & World 14)**
> 
> **Decision Outcome:** **`CODEX_ONLY`** (Comprehensive reference in Upa-Sutra Codex under the complete compound title; no separate standalone Treasury Quest).  
> **Confidence Rating:** **`HIGH`**  
> **Rationale:**
> 1. **Complete Triple Overlap**:
>    - **Main World 14 (*Gunitasamuccayah*)**: Implements the univariate coefficient-sum verification check ($SC(F_1) \times SC(F_2) = SC(P)$).
>    - **Upa-Sutra 10 (*Samuccayagunitah*)**: Represents the RHS clause ($SC(P) = P(1)$), already resolved as `CODEX_ONLY`.
>    - **Upa-Sutra 13 (*Gunitasamuccayah Samuccayagunitah*)**: The complete compound Sanskrit sentence uniting both clauses.
> 2. **Avoidance of Redundant Gameplay**: Building a separate playable quest for Upa-Sutra 13 would result in an exact clone of World 14's practice arena.
> 3. **Educational Value in Codex**: The Codex card for Upa-Sutra 13 will showcase the advanced multivariable/cyclic polynomial verification examples ($(x+y+z)^2 \implies 3^2 = 9$) and provide the complete historical context behind the compound aphorism.

---

## 2. Three-Way Overlap Matrix

| Feature | World 14: Gunitasamuccayah | Upa-Sutra 10: Samuccayagunitah | Upa-Sutra 13: Gunitasamuccayah Samuccayagunitah | World 15: Gunakasamuccayah |
|---|---|---|---|---|
| **Sanskrit Title** | गुणितसमुच्चयः | समुच्चयगुणितः | गुणितसमुच्चयः समुच्चयगुणितः | गुणकसमुच्चयः |
| **Linguistic Function** | LHS clause: "Product of the sum" | RHS clause: "Sum of the product" | Complete compound sentence | Distinct Sutra: "Factors of the sum" |
| **Mathematical Domain** | Univariate polynomial verification | Polynomial coefficient summation | Full algebraic evaluation homomorphism | Monic quadratic factor-pair root search |
| **Core Invariant** | $SC(F_1) \times SC(F_2) = SC(P)$ | $SC(P) = P(1) = \sum a_j$ | $\prod F_i(1) = P(1)$ | $p \cdot q = C, \quad p + q = B$ |
| **Evaluation Map** | $\phi_1(F_1) \cdot \phi_1(F_2) = \phi_1(P)$ | $\phi_1(P)$ | $\phi_{\mathbf{1}}(F_1 \dots F_k) = \phi_{\mathbf{1}}(P)$ | N/A (Factor Search) |
| **Player Action** | Verify quadratic expansions at $x=1$ | Evaluate coefficient sums | Verify multivariable/cyclic expansions | Factor $x^2 + Bx + C$ into $(x+p)(x+q)$ |
| **Overlap Status** | **Existing Main Campaign World** | **Linguistic Fragment (Codex Only)** | **Compound Parent (Codex Only)** | **Distinct Algebra World (Root Finding)** |

> [!NOTE]
> **Contrast with World 15 (*Gunakasamuccayah*)**:
> World 15 is an active factor-finding algorithm (identifying integer pairs $(p, q)$ such that $pq = C$ and $p + q = B$). It is mathematically distinct from the evaluation/verification checks of World 14 and Upa-Sutras 10 & 13.

---

## 3. Exact Mathematical Verification

### Baseline Univariate Identity (Evaluation Homomorphism)
For univariate polynomials $F_1(x), F_2(x) \in \mathbb{Z}[x]$ and product $P(x) = F_1(x) \cdot F_2(x)$:
The evaluation map $\phi_1: \mathbb{Z}[x] \to \mathbb{Z}$ given by $\phi_1(Q) = Q(1) = \sum a_j = SC(Q)$ satisfies:
$$\phi_1(F_1 \cdot F_2) = \phi_1(F_1) \cdot \phi_1(F_2) \implies SC(F_1 \cdot F_2) = SC(F_1) \cdot SC(F_2)$$

#### Verified Example 1:
$$(x + 3)(x + 2) = x^2 + 5x + 6$$
- $SC(F_1) = 1 + 3 = 4$; $SC(F_2) = 1 + 2 = 3 \implies 4 \times 3 = 12$.
- $SC(P) = 1 + 5 + 6 = 12$.
- Check: $12 = 12 \checkmark$.

---

### Insufficiency Safeguard (False-Positive Counterexample)
Evaluating at $x = 1$ is a **necessary** condition, but **NOT sufficient** to guarantee polynomial equality.

#### Counterexample:
- **Target Factored Form**: $(x + 4)(x + 1) = x^2 + 5x + 4$.
  - $SC(\text{factors}) = (1+4)(1+1) = 5 \times 2 = 10$.
  - $SC(P_{\text{true}}) = 1 + 5 + 4 = 10 \checkmark$.
- **False Proposed Polynomial**: $P_{\text{fake}}(x) = x^2 + 3x + 6$.
  - $SC(P_{\text{fake}}) = 1 + 3 + 6 = 10$.
- **Outcome**: The sum check passes ($10 = 10$), but $P_{\text{fake}}(x)$ is algebraically incorrect:
  $$\text{At } x = 2: \quad (2+4)(2+1) = 18 \quad \text{vs.} \quad 2^2 + 3(2) + 6 = 16 \quad (18 \ne 16)$$
- **Conclusion**: Software verification must always couple the quick $x=1$ sum check with exact term-by-term coefficient comparison.

---

### Multivariable & Cyclic Polynomial Verification
For multivariable polynomials, evaluation at $(x_1, \dots, x_m) = (1, \dots, 1)$ preserves the product of sums:

#### Verified Example 2 (Bivariate Expansion):
$$(x - y + 2)(2x + 3y - 1) = 2x^2 + xy - 3y^2 + 3x + 7y - 2$$
- At $(x, y) = (1, 1)$:
  - $F_1(1, 1) = 1 - 1 + 2 = 2$.
  - $F_2(1, 1) = 2(1) + 3(1) - 1 = 4$.
  - $SC(\text{factors}) = 2 \times 4 = 8$.
- Expanded polynomial at $(1, 1)$:
  - $P(1, 1) = 2(1)^2 + 1(1)(1) - 3(1)^2 + 3(1) + 7(1) - 2 = 2 + 1 - 3 + 3 + 7 - 2 = 8$.
- Check: $8 = 8 \checkmark$.

#### Verified Example 3 (Cyclic Trivariate Expansion):
$$(x + y + z)^2 = x^2 + y^2 + z^2 + 2xy + 2yz + 2zx$$
- At $(x, y, z) = (1, 1, 1)$:
  - LHS: $(1 + 1 + 1)^2 = 3^2 = 9$.
  - RHS: $1 + 1 + 1 + 2 + 2 + 2 = 9$.
- Check: $9 = 9 \checkmark$.

---

## 4. Architectural & Codex Presentation Strategy

1. **Upa-Sutra Codex Entry**:
   - In `UpaSutraModels.kt`, maintain `UpaSutraId.GUNITASAMUCCAYAH_SAMUCCAYAGUNITAH` as a **Codex Reference Card**.
   - Display Title: **Gunitasamuccayah Samuccayagunitah** (गुणितसमुच्चयः समुच्चयगुणितः).
   - Meaning: "The product of the sum is equal to the sum of the products".
   - Worked Example: `(x + y + z)² = x² + y² + z² + 2xy + 2yz + 2zx ⟹ (1+1+1)² = 3² = 9 = 1+1+1+2+2+2`.
   - Description: "Master validation principle for polynomial products, powers, and factorizations across univariate and multivariable expressions."
2. **Treasury Screen**:
   - No separate quest scroll is generated in the Treasury quest list, preventing duplicate gameplay.
3. **No Breaking Changes**:
   - All 254 existing unit tests continue to pass with 100% integrity.
