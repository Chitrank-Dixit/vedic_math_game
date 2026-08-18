# Research Dossier: Upa-Sutra 9 — Antyayoreva ("Only the Last Terms")

**Document Status:** Complete Research Dossier / Phase U4 (Tier C Resolution)  
**Target Subject:** Upa-Sutra 9 (*Antyayoreva* / अन्त्ययोरेव)  
**Classification:** Tier C (Conflicting Interpretations & Overlapping Methods)  
**Project:** *Ankh: The Sutra Saga* — Upa-Sutra Treasury  

---

## 1. Executive Summary & Problem Statement

Upa-Sutra 9, **Antyayoreva** (*Antyayor-eva*, अन्त्ययोरेव — literally "Only in/of the last terms" or "Only the final terms"), is classified as **Tier C** in the Canonical Registry. 

Across primary literature and secondary pedagogical sources, the name *Antyayoreva* is attributed to three fundamentally conflicting mathematical techniques:
1. **Special Rational / Algebraic Equations**: When the ratio of the independent constant (last) terms on the LHS equals the ratio of the last terms on the RHS, the root is immediately $x = 0$.
2. **Multiplication by 11 via Adjacent-Digit Addition**: The "zero-sandwich" method ($3475 \times 11 = 38225$), retaining only the outer digits at the extremes and summing adjacent interior pairs.
3. **Same-Prefix / Units Total 10 Multiplication**: e.g., $43 \times 47 = 2021$, which is an exact duplication of Upa-Sutra 8 (*Antyayordasake'pi*).

The objective of this research dossier is to examine primary and secondary sources, determine historical and mathematical authenticity, and establish whether *Antyayoreva* warrants a distinct gameplay quest, should merge into an existing World as an advanced variant, or should remain Codex-only reference material.

---

## 2. Primary & Secondary Literature Comparison

### Source 1: Swami Bharati Krishna Tirtha — *Vedic Mathematics* (1965, Motilal Banarsidass)
- **Location**: Chapter 9 (*Shūnyam Sāmyasamuccaye* & Sub-Sūtras), pp. 104–111.
- **Exact Sanskrit Text**: अन्त्ययोरेव (*Antyayoreva* = "Only the last terms" / "Only in the last terms").
- **Documented Application**:
  - Applied directly to special rational equations and polynomial fractions.
  - Specifically, for linear-fraction equations of the form:
    $$\frac{a x + b}{c x + d} = \frac{p x + q}{r x + s}$$
    When the ratio of the independent (last) terms is identical on both sides:
    $$\frac{b}{d} = \frac{q}{s} \iff b s = d q$$
    then the constant terms eliminate during cross-multiplication, leaving:
    $$(a s + b r) x = (c q + d p) x \implies x = 0 \quad (\text{provided } a s + b r \ne c q + d p)$$
  - Swami BKT categorizes this as an Upa-Sutra corollary to *Shunyam Samyasamuccaye* (equating to zero).
- **Independence & Authenticity**: **Primary Source (Ground Truth)**.
- **Confidence Rating**: **`HIGH`** (Definitive textual anchor for rational equations).

---

### Source 2: Kenneth Williams & Mark Gaskell — *The Cosmic Computer* (1998) & *Teacher's Manual*
- **Translation**: "Only the End-Terms" or "Only the Last Terms".
- **Documented Application**:
  - Williams documents the primary algebraic rule $\frac{x+a}{x+b} = \frac{x+c}{x+d}$ yielding $x = 0$ when $\frac{a}{b} = \frac{c}{d}$.
  - In introductory elementary arithmetic modules, Williams notes that some popular teachers refer to the "ends remain at the ends" rule for multiplication by 11 ($N \times 11$) as *Antyayoreva*, but clarifies that multiplication by 11 is formally the $N=1$ base case of *Sopantyadvayamantyam* (World 13) or 2-digit vertical/crosswise multiplication (*Urdhva-Tiryagbhyam*).
- **Independence & Authenticity**: **High-Quality Secondary Academic Source**.
- **Confidence Rating**: **`HIGH`**.

---

### Source 3: Dr. S.K. Kapoor — *Vedic Mathematical Concepts*
- **Translation**: "Only the final/last terms".
- **Documented Application**:
  - Explains *Antyayoreva* as evaluation at $x=0$ (focusing purely on the constant/independent terms of polynomial and rational systems).
  - Corroborates Swami BKT's classification that it is a specialized rational equation inspection tool.
- **Independence & Authenticity**: Secondary analytical commentary.
- **Confidence Rating**: **`MEDIUM-HIGH`**.

---

### Source 4: Pop-Math Compilations & Web Tutorials (e.g. Dhaval Bathia, Pradeep Kumar)
- **Documented Application**:
  - Frequently conflate *Antyayoreva* with *Antyayordasake'pi* ("last digits sum to 10") due to the shared Sanskrit prefix *Antyayo-* (अन्त्ययो- = "of the last two").
  - Others present $M \times 11$ as a standalone "magic trick" without citing the general *Sopantyadvayamantyam* framework.
- **Independence & Authenticity**: **Derivative / Pop-Math (Low Academic Rigor)**.
- **Confidence Rating**: **`LOW`** (Demonstrates widespread nomenclature conflation).

---

## 3. Comparative Synthesis Table

| Candidate Interpretation | Source Support | Distinct from Existing Gameplay? | Mathematical Reproducibility | Assessment & Recommendation |
|---|---|---|---|---|
| **Candidate A: Rational Equations ($x=0$ via Constant Ratio)** | Swami BKT (1965 Ch. 9), K. Williams, Dr. S.K. Kapoor | Distinct condition from World 9, but trivial answer ($x=0$ always) | Exact algebraic derivation ($ad = bc \implies x=0$) | **Historically Valid**, but structurally weak for interactive keypad gameplay (repeated $x=0$). Ideal for **Codex Entry** or World 9 bonus variant. |
| **Candidate B: Multiplication by 11 ($M \times 11$)** | Secondary pop-math tutorials; noted in Williams | **Strict Subset** of World 13 (*Sopantyadvayamantyam*) | Exact integer algorithm ($R[i] = d[i+1] + 1 \cdot d[i]$) | **Algorithmically Redundant** with World 13 ($N=1$). Do not build a standalone quest. |
| **Candidate C: Same-Prefix / Endings Sum to 10** | Pop-math web blogs (erroneous conflation) | **100% Duplicate** of Upa-Sutra 8 (*Antyayordasake'pi*) | Exact integer algorithm ($T(T+1) \parallel U_1 U_2$) | **Nomenclature Confusion**. Already implemented in Quest 1. |
| **Candidate D: Proportions / Extreme Terms ($ad = bc$)** | Classical proportion rule ("product of extremes = product of means") | Covered in World 5 (*Urdhva-Tiryagbhyam*) | Exact elementary algebra | Standard high-school algebra; not a distinct Vedic speed technique. |

---

## 4. Source Disagreement & Synthesis Finding

1. **The Authentic Vedic Meaning**: The authentic historical definition of *Antyayoreva* per Swami Bharati Krishna Tirtha (1965) is **Candidate A: Rational equations where the ratio of constant (last) terms is equal, yielding $x = 0$**.
2. **Why Candidate B ($\times 11$) Exists**: Multiplication by 11 is an elementary adjacent-digit shortcut where the leftmost and rightmost digits ("only the last terms") stay at the ends while the middle is summed. However, this is formally the $N=1$ instance of *Sopantyadvayamantyam* (World 13: $N \in [2..9]$).
3. **Why Candidate C (Endings Sum to 10) Exists**: This is an outright conflation caused by similar Sanskrit titles (*Antyayoreva* vs. *Antyayordashake'pi*).
