# Mathematical & Product Specification: Upa-Sutra 12 — Vilokanam ("By Mere Observation")

**Document Status:** Complete Mathematical & Product Specification / Phase U3  
**Feature:** Upa-Sutra Treasury — Quest 9 (Pattern Sight Challenge Pack)  
**Target Class:** `VilokanamGenerator`  
**Parent World:** World 9 (Shunyam Samyasamuccaye) / World 11  
**Unlock Requirement:** Completion of World 9 (Shunyam Samyasamuccaye)  

---

## 1. Mandatory Interpretation Gate & Literature Audit

### Literature Findings:
1. **Swami Bharati Krishna Tirtha (Vedic Mathematics, 1965, Ch. 9 & 13)**:
   - *Vilokanam* ("By Mere Observation / Inspection") is used throughout the Vedic system to describe the instantaneous visual identification of mathematical symmetry, base proximity, digit constraints, or structural form (e.g. $x + 1/x = 2.5 \implies x \in \{2, 0.5\}$, or recognizing $85^2$, $97^2$, $43 \times 47$, $58 \times 62$).
   - It is not an arbitrary blind guess, but the ability to recognize at a glance which Vedic Sutra or Upa-Sutra governs the given expression.
2. **Kenneth Williams & Mark Gaskell (The Cosmic Computer)**:
   - "By Inspection": Identifying the appropriate formula and immediate structural decomposition based on observable properties.
3. **Dr. S.K. Kapoor (Vedic Mathematical Concepts)**:
   - Corroborates *Vilokanam* as visual pattern recognition and instantaneous deduction.

### Verdict & Confidence Rating:
- **Consensus**: **Unanimous Agreement** that *Vilokanam* represents structural pattern recognition.
- **Confidence Rating**: **`HIGH`**
- **Decision**: **Gate Passed**. Proceed with the **Pattern Sight Challenge Pack** implementation.

---

## 2. Supported Pattern Families & Predicate Logic

| Pattern ID | Description & Conditions | Formula / Source |
|---|---|---|
| `ENDS_IN_FIVE_SQUARE` | $N^2$ where $N \equiv 5 \pmod{10}$ | $N = 10a + 5 \implies a(a+1) \parallel 25$ (World 1) |
| `NEAR_BASE_SQUARE` | $N^2$ where $|N - 100| \le 15$ and $N \not\equiv 5 \pmod{10}$ | $(N - d) \parallel d^2$ (World 4) |
| `SAME_TENS_UNITS_SUM_TEN` | $A \times B$ where $\lfloor A/10 \rfloor = \lfloor B/10 \rfloor$ and $(A\%10 + B\%10 = 10)$, $A \ne B$ | $a(a+1) \parallel (u_1 \times u_2)$ (Antyayordasake'pi) |
| `SYMMETRIC_PRODUCT` | $(M - k)(M + k)$ with round mean $M$ (multiple of 10) and small offset $k \in [1, 5]$ | $M^2 - k^2$ (World 11) |
| `NONE_OF_THE_ABOVE` | None of the above conditions are satisfied | Ordinary standard multiplication |

### Precedence & Exclusion Invariant:
- To eliminate any ambiguity, every challenge problem is mutually exclusive by construction.
- Non-examples and negative controls (`NONE_OF_THE_ABOVE`) are constructively tested against all active predicates to ensure zero false matches.

---

## 3. Required Seed Challenges

1. **Seed 1 (Ends-in-Five Square)**:
   - Problem: $85^2$
   - Correct Pattern: `ENDS_IN_FIVE_SQUARE`
   - Explanation: $8 \times 9 = 72$, append $25 \implies 7225$
   - Exact Verification: $85 \times 85 = 7225 \checkmark$

2. **Seed 2 (Near-Base Square)**:
   - Problem: $97^2$
   - Correct Pattern: `NEAR_BASE_SQUARE`
   - Explanation: Base 100, deficiency $3$; $(97 - 3) \parallel 3^2 = 9409$
   - Exact Verification: $97 \times 97 = 9409 \checkmark$

3. **Seed 3 (Same Tens, Units Sum to 10)**:
   - Problem: $43 \times 47$
   - Correct Pattern: `SAME_TENS_UNITS_SUM_TEN`
   - Explanation: Tens match ($4 = 4$), units sum to 10 ($3 + 7 = 10$); $(4 \times 5) \parallel (3 \times 7) = 2021$
   - Exact Verification: $43 \times 47 = 2021 \checkmark$

4. **Seed 4 (Symmetric Product)**:
   - Problem: $58 \times 62$
   - Correct Pattern: `SYMMETRIC_PRODUCT`
   - Explanation: Average is 60, offset is 2; $(60 - 2)(60 + 2) = 60^2 - 2^2 = 3600 - 4 = 3596$
   - Exact Verification: $58 \times 62 = 3596 \checkmark$

5. **Seed 5 (Negative Control / None of the Above)**:
   - Problem: $46 \times 53$
   - Correct Pattern: `NONE_OF_THE_ABOVE`
   - Explanation: Tens differ ($4 \ne 5$), not ending in 5, not a square, not symmetric around a round base ($M = 49.5$).
   - Exact Verification: $46 \times 53 = 2438 \checkmark$

---

## 4. Scope Safeguards

- Every challenge has exactly one correct pattern card.
- Solution steps and explanations are structured data backed by exact integer arithmetic.
- Keypad / user choice is based on selecting the verified pattern card index / integer answer.
