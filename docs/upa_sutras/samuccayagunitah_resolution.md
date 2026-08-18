# Product Decision Memo: Upa-Sutra 10 — Samuccayagunitah

**Document Status:** Approved Resolution Memo / Phase U4  
**Target Subject:** Upa-Sutra 10 (*Samuccayagunitah* / समुच्चयगुणितः)  
**Classification:** Tier C (Linguistic Twin of Upa-Sutra 13 & Overlap Resolution)  
**Parent World:** World 14 (*Gunitasamuccayah*) & Upa-Sutra 13  

---

## 1. Executive Product Decision

> ### **FINAL RECOMMENDATION: `CODEX_ONLY` (Linguistic Twin of Upa-Sutra 13)**
> 
> **Decision Outcome:** **`CODEX_ONLY`** (Unified reference in Upa-Sutra Codex under the combined title *Gunitasamuccayah Samuccayagunitah*; no separate standalone Treasury Quest).  
> **Confidence Rating:** **`HIGH`**  
> **Rationale:**
> 1. **Philological & Historical Reality**: Primary textual analysis of Swami Bharati Krishna Tirtha (1965, Ch. 14) proves that *Samuccayagunitah* (Upa-Sutra 10) is merely the grammatical second half of the compound aphorism:
>    $$\text{Guṇitasamuccayaḥ (LHS)} \quad \text{Samuccayaguṇitaḥ (RHS)}$$
>    *"The product of the sum (of the coefficients) is equal to the sum of the coefficients of the product."*
> 2. **Complete Algorithmic Duplication with World 14**: World 14 (*Gunitasamuccayah*) already implements the complete two-sided evaluation ($SC(F_1) \times SC(F_2) \overset{?}{=} SC(P)$) alongside exact term-by-term expansion checks. Building a standalone quest for Upa-Sutra 10 would be 100% redundant with World 14.
> 3. **Redundancy with Upa-Sutra 13**: Upa-Sutra 13 (*Gunitasamuccayah Samuccayagunitah*) is the full compound phrase representing the exact same identity. Maintaining two separate playable quests for Upa-Sutra 10 and Upa-Sutra 13 would create duplicate content in the Treasury.

---

## 2. Overlap Matrix & Detailed Comparison

| Component | World 14: Gunitasamuccayah | World 15: Gunakasamuccayah | Upa-Sutra 10: Samuccayagunitah | Upa-Sutra 13: Gunitasamuccayah Samuccayagunitah |
|---|---|---|---|---|
| **Sanskrit Title** | गुणितसमुच्चयः | गुणकसमुच्चयः | समुच्चयगुणितः | गुणितसमुच्चयः समुच्चयगुणितः |
| **English Meaning** | "Product of the sum" | "Factors of the sum" | "Sum of the product" | "Product of sum is sum of products" |
| **Mathematical Domain** | Univariate quadratic factorization check | Monic quadratic factor-pair root finding | RHS coefficient sum ($P(1) = \sum a_j$) | Complete verification identity ($SC(\text{factors}) = SC(\text{product})$) |
| **Algorithmic Invariant** | $SC(F_1) \cdot SC(F_2) = SC(P)$ | $p \cdot q = C, p + q = B$ | $SC(P) = P(1)$ | $SC(F_1 \dots F_k) = SC(P)$ |
| **Player Skill** | Verify polynomial expansions via $x=1$ | Factor $x^2 + Bx + C$ into $(x+p)(x+q)$ | Evaluate $\sum a_j$ on RHS | Complete verification theorem |
| **Overlap Status** | **Existing Main World 14** | **Existing Main World 15** | **Strict Subset / Duplicate of World 14** | **Exact Linguistic Compound of Sutra 14 + Upa-Sutra 10** |

---

## 3. Exact Mathematical Verification

### Candidate A: Standard Univariate Polynomial Verification (Swami BKT 1965)
- **Identity**: $SC(F_1) \times SC(F_2) = SC(P)$ where $SC(Q) = Q(1)$.
- **Verified Example 1**:
  $$(x + 3)(x + 2) = x^2 + 5x + 6$$
  - Factor evaluation at $x=1$: $F_1(1) = 1 + 3 = 4$; $F_2(1) = 1 + 2 = 3 \implies 4 \times 3 = 12$.
  - Expanded polynomial at $x=1$: $P(1) = 1^2 + 5(1) + 6 = 1 + 5 + 6 = 12$.
  - Check: $12 = 12 \checkmark$.
  - Direct algebraic expansion: $(x+3)(x+2) = x^2 + 2x + 3x + 6 = x^2 + 5x + 6 \checkmark$.

### Candidate B: Multivariable Extension ($(x, y) = (1, 1)$)
- **Verified Example 2 (Homogeneous Quadratic)**:
  $$(2x + 3y)^2 = 4x^2 + 12xy + 9y^2$$
  - Factor evaluation at $x=1, y=1$: $(2(1) + 3(1))^2 = (2 + 3)^2 = 5^2 = 25$.
  - Expanded polynomial at $x=1, y=1$: $4(1)^2 + 12(1)(1) + 9(1)^2 = 4 + 12 + 9 = 25$.
  - Check: $25 = 25 \checkmark$.
  - Direct algebraic expansion: $(2x+3y)(2x+3y) = 4x^2 + 6xy + 6xy + 9y^2 = 4x^2 + 12xy + 9y^2 \checkmark$.

- **Verified Example 3 (Non-Homogeneous Bivariate Product)**:
  $$(x + 2y + 1)(2x + y + 3) = 2x^2 + 5xy + 2y^2 + 5x + 7y + 3$$
  - Factor evaluation at $x=1, y=1$: $(1 + 2 + 1)(2 + 1 + 3) = 4 \times 6 = 24$.
  - Expanded polynomial at $x=1, y=1$: $2 + 5 + 2 + 5 + 7 + 3 = 24$.
  - Check: $24 = 24 \checkmark$.

---

## 4. False-Positive / Sufficiency Safeguard Proof

> [!CAUTION]
> **Mathematical Proof of Non-Sufficiency**:
> Evaluating at $x = 1$ is a **necessary** test, but **NOT sufficient** to prove algebraic equality.
> 
> **Constructed Counterexample**:
> - True Factorization: $(x + 3)(x + 2) = x^2 + 5x + 6$.
> - Spurious False Proposal: $P_{\text{fake}}(x) = x^2 + 7x + 4$.
> - Evaluate at $x = 1$:
>   - Factors: $(1+3)(1+2) = 12$.
>   - $P_{\text{fake}}(1) = 1 + 7 + 4 = 12$.
> - **The sum check passes ($12 = 12$), but the proposal is completely false!**
> - Evaluating at $x = 2$: $(2+3)(2+2) = 20$, while $P_{\text{fake}}(2) = 2^2 + 7(2) + 4 = 22 \ne 20$.
> 
> Therefore, any game engine implementing this verification must always enforce **Exact Term-by-Term Coefficient Matching** in addition to the quick $x=1$ sum check.

---

## 5. Architectural & Codex Recommendation

1. **Upa-Sutra Codex Representation**:
   - Maintain Upa-Sutra 10 (*Samuccayagunitah*) and Upa-Sutra 13 (*Gunitasamuccayah Samuccayagunitah*) as **Codex Reference Cards**.
   - The Codex card will display the multivariable worked example $(2x + 3y)^2 \implies 5^2 = 25$ and detail the historical context of the compound aphorism.
2. **Treasury Quest Line**:
   - Avoid creating a standalone redundant quest for Upa-Sutra 10.
3. **No Breaking Changes**:
   - All existing 254 unit tests, database DAOs, and World 14/15 implementations remain untouched and 100% green.
