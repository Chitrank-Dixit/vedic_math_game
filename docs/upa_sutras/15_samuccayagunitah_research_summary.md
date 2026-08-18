# Phase U4 Research Summary: Upa-Sutra 10 (Samuccayagunitah — "The Sum of Products")

**Document Status:** Complete & 100% Verified / Phase U4 Research Summary  
**Feature:** Upa-Sutra Treasury — Tier C Research Resolution  
**Parent World:** World 14 (*Gunitasamuccayah*) & Upa-Sutra 13  
**Recommended Product Outcome:** **`CODEX_ONLY`** (Linguistic Twin of Upa-Sutra 13)  
**Confidence Level:** **`HIGH`**  

---

## 1. Sources Consulted & Philological Findings

1. **Swami Bharati Krishna Tirtha — *Vedic Mathematics* (1965, Motilal Banarsidass, Ch. 14)**:
   - **Primary Ground Truth Source**.
   - Demonstrates that *Samuccayagunitah* (समुच्चयगुणितः) is the second half of the compound sentence:
     $$\text{Guṇitasamuccayaḥ (LHS: Product of Sums)} = \text{Samuccayaguṇitaḥ (RHS: Sum of Products)}$$
   - It represents $SC(P) = P(1) = \sum a_j$.
2. **Kenneth Williams & Mark Gaskell — *The Cosmic Computer* (1998) & *Teacher's Manual***:
   - Confirms that listing *Samuccayagunitah* as a separate Upa-Sutra is a typographical/editorial artifact of splitting the compound aphorism into two clauses.
3. **Dr. S.K. Kapoor — *Vedic Mathematical Concepts***:
   - Evaluates the RHS coefficient summation in both univariate and multivariable polynomials.
4. **Pop-Math Compilations**:
   - Lack any independent alternative calculation technique for Upa-Sutra 10.

---

## 2. Candidate Methods & Mathematical Verification

### Candidate A: Univariate Coefficient Sum Check (World 14 Duplicate)
- $(x + 3)(x + 2) = x^2 + 5x + 6$
- $F_1(1) \times F_2(1) = (4)(3) = 12$; $P(1) = 1 + 5 + 6 = 12 \checkmark$.
- **Finding**: 100% duplicate of World 14.

### Candidate B: Multivariable Extension
- $(2x + 3y)^2 = 4x^2 + 12xy + 9y^2$
- Evaluated at $(x, y) = (1, 1)$: $(2+3)^2 = 5^2 = 25$; $4 + 12 + 9 = 25 \checkmark$.
- $(x + 2y + 1)(2x + y + 3) = 2x^2 + 5xy + 2y^2 + 5x + 7y + 3 \implies 24 = 24 \checkmark$.
- **Finding**: Valid algebraic identity, suitable as rich content for the Codex reference card.

---

## 3. False-Positive / Sufficiency Safeguard

- **Proof of Non-Sufficiency**:
  - Factors: $(x + 3)(x + 2) \implies SC = 12$.
  - False proposal: $P_{\text{fake}}(x) = x^2 + 7x + 4 \implies SC = 1 + 7 + 4 = 12$.
  - The coefficient sum check passes ($12 = 12$), but the polynomial expansion is completely false.
- **Rule**: Any software verification engine must enforce exact term-by-term coefficient comparison.

---

## 4. Final Product Outcome: `CODEX_ONLY`

- **Recommended Decision**: **`CODEX_ONLY`** (Confidence: **`HIGH`**).
- **Pedagogical Rationale**:
  - World 14 already teaches the univariate coefficient-sum check.
  - Creating a separate quest for Upa-Sutra 10 would duplicate World 14 and Upa-Sutra 13.
  - The Codex card will detail both univariate and multivariable examples alongside the historical explanation of the compound aphorism.

---

## 5. Explicit Questions for Human Review

1. **Codex Unification**: Does the team approve displaying the multivariable example $(2x + 3y)^2 \implies 5^2 = 25$ as the primary demonstration on the Codex card for Upa-Sutra 10? *(Recommended: Yes)*.
2. **Upa-Sutra 13 Linkage**: Does the team agree that Upa-Sutra 10 and Upa-Sutra 13 represent the two halves of the same mathematical theorem, and should both be maintained as Codex entries rather than redundant playable quests? *(Recommended: Yes)*.
