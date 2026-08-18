# Final 13-Item Coverage Matrix: Upa-Sutra Treasury

**Document Status:** Complete & 100% Verified / Phase U5 Consolidation  
**Feature:** Upa-Sutra Treasury & Codex Final Coverage Review  
**Project:** *Ankh: The Sutra Saga*  

---

## 1. Master 13-Item Coverage Matrix

| # | ID (`UpaSutraId`) | Sanskrit / Display Name | Canonical Meaning | Verification Tier | Final Product Placement | Unlock / Host World | Mathematical Scope | Exact Verification Oracle | Test / Research Status | Confidence | Source / Resolution Reference | Known Limitation |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | `ANURUPYENA` | आनुरूप्येण / Anurupyena | "Proportionately" | Tier A | `PLAYABLE_TREASURY_QUEST` | World 2 (Nikhilam) | Working sub-bases $W = B/k$ or $B \times k$ (Base 50, 200, 500) | Exact BigInteger integer multiplication | 25+ unit tests passed (100%) | [05_anurupyena_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/05_anurupyena_summary.md) | Single working base per problem |
| 2 | `SHISYATE_SHESAMAJNA` | शिष्यते शेषसंज्ञः / Sisyate Sesasamjnah | "The remainder remains constant" | Tier B | `PLAYABLE_TREASURY_QUEST` | World 6 (Paravartya) | Polynomial remainder theorem $P(a)$ for divisor $(x-a)$ without long division | Exact synthetic division and polynomial Horner evaluation | 25+ unit tests passed (100%) | [08_sisyate_sesasamjnah_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/08_sisyate_sesasamjnah_summary.md) | Linear divisors $(x-a)$ only |
| 3 | `ADYAMADYENANTYAMANTYENA` | आद्यमाद्येनान्त्यमन्त्येन / Adyamadyenantyamantyena | "First by first and last by last" | Tier A | `PLAYABLE_TREASURY_QUEST` | World 5 (Urdhva) / World 10 | Factoring non-monic quadratics $Ax^2+Bx+C$ via candidate first/last pairs and cross-term checks | Exact polynomial expansion comparison | 30+ unit tests passed (100%) | [07_adyamadyenantyamantyena_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/07_adyamadyenantyamantyena_summary.md) | Factorable quadratics over integers only |
| 4 | `KEVALAIHSAPTAKAM_GUNYAT` | केवलैः सप्तकं गुण्यात् / Kevalaih Saptakam Gunyat | "For seven the multiplicand is 143" | Tier B | `PLAYABLE_TREASURY_QUEST` | World 12 (Shesanyankena) | Cyclic repetend generation for fractions $n/7$ via $7 \times 143 = 1001 \implies 0.\overline{142857}$ | Exact long division decimal cycle simulator | 25+ unit tests passed (100%) | [10_kevalaih_saptakam_gunyat_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/10_kevalaih_saptakam_gunyat_summary.md) | Denominator 7 fractions ($1/7 \dots 6/7$) |
| 5 | `VESHTANAM` | वेष्टनम् / Vestanam | "By Osculation" | Tier B | `PLAYABLE_TREASURY_QUEST` | World 12 (Shesanyankena) | Prime divisibility testing via single-step positive/negative osculators (divisors 7, 13, 17, 19, 29, 39, 49) | Exact integer modulo arithmetic ($N \pmod d == 0$) | 35+ unit tests passed (100%) | [09_vestanam_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/09_vestanam_summary.md) | Bounded to verified prime osculators |
| 6 | `YAVADUNAM_TAVADUNAM` | यावदूनं तावदूनम् / Yavadunam Tavadunam | "Lessen by deficiency, setup square" | Tier B | `CODEX_ONLY` | World 4 (Yavadunam) | Near-base multi-digit cubing $(B \pm d)^3 = B^2(B \pm 3d) + B(3d^2) \pm d^3$ | Exact integer cubing | Codex verified reference | `HIGH` | [00_canonical_registry.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/00_canonical_registry.md) | High cognitive load for 3-part mental cubing |
| 7 | `YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET` | यावदूनीकृत्य वर्गं च योजयेत् / Yavadunam Remix | "Whatever the deficiency, lessen by that and add square" | Tier A | `PLAYABLE_TREASURY_QUEST` | World 4 (Yavadunam) | Mastery remix of near-base squaring with multi-digit carries ($88^2 \implies 7744$) | Exact integer squaring | 25+ unit tests passed (100%) | [06_yavadunam_remix_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/06_yavadunam_remix_summary.md) | Near base 100/1000 numbers |
| 8 | `ANTYAYORDASHAKEPI` | अन्त्ययोर्दशकेऽपि / Antyayordasake'pi | "Last digits sum to 10 and first same" | Tier A | `PLAYABLE_TREASURY_QUEST` | World 1 (Ekadhikena) | Multiplying numbers with identical tens and units summing to 10 ($43 \times 47 = 2021$) | Exact integer multiplication | 20+ unit tests passed (100%) | [04_u1_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/04_u1_summary.md) | Strict condition: same tens, units sum 10 |
| 9 | `ANTYAYEREVA` | अन्त्ययोरेव / Antyayoreva | "Only the last terms" | Tier C | `CODEX_ONLY` | World 9 (Shunyam) / World 13 | Rational equations with matching constant ratios ($\frac{x+a}{x+b}=\frac{x+c}{x+d} \implies x=0$) | Exact cross-multiplication algebra | Research completed & verified | `HIGH` | [antyayoreva_resolution.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/antyayoreva_resolution.md) | Degenerate gameplay ($x=0$ always) if made interactive |
| 10 | `SAMUCCAYAGUNITAH` | समुच्चयगुणितः / Samuccayagunitah | "The sum of products" | Tier C | `CODEX_ONLY` | World 14 (Gunitasamuccayah) | RHS polynomial coefficient evaluation ($SC(P) = P(1)$) and multivariable extensions | Exact algebraic evaluation at $(1, 1)$ | Research completed & verified | `HIGH` | [samuccayagunitah_resolution.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/samuccayagunitah_resolution.md) | Duplicate of World 14; linguistic half of Upa-Sutra 13 |
| 11 | `LOPANA_STHAPANABHYAM` | लोपनस्थापनाभ्याम् / Lopanasthapanabhyam | "By elimination and retention" | Tier B | `PLAYABLE_TREASURY_QUEST` | World 8 (Sankalana) | Bivariate quadratic factorization via $y=0$ and $x=0$ elimination, constant pairing, and $xy$ term check | Exact 6-coefficient algebraic expansion | 30+ unit tests passed (100%) | [12_lopanasthapanabhyam_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/12_lopanasthapanabhyam_summary.md) | Bivariate quadratics factorable into linear forms |
| 12 | `VILOKANAM` | विलोकिनम् / Vilokanam | "By mere observation" | Tier B | `PLAYABLE_TREASURY_QUEST` | World 9 (Shunyam) | Pattern sight challenge pack: observing visual cues to classify and solve special Vedic structures | Exact integer arithmetic per pattern family | 30+ unit tests passed (100%) | [13_vilokanam_summary.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/13_vilokanam_summary.md) | 5 supported mutually exclusive pattern families |
| 13 | `GUNITASAMUCCAYAH_SAMUCCAYAGUNITAH` | गुणितसमुच्चयः समुच्चयगुणितः / Full Compound | "Product of sum is sum of products" | Tier C | `CODEX_ONLY` | World 14 & World 15 | Master compound verification principle for cyclic and multivariable polynomials | Evaluation homomorphism $\phi_{\mathbf{1}}$ | Research completed & verified | `HIGH` | [gunitasamuccayah_samuccayagunitah_resolution.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/gunitasamuccayah_samuccayagunitah_resolution.md) | Duplicate of World 14; compound parent of Upa-Sutra 10 |

---

## 2. Product Placement Summary Dashboard

```text
=====================================================
UPA-SUTRA TREASURY CONSOLIDATION DASHBOARD
=====================================================
Playable Treasury Quests:       9 / 13  (69.2%)
Merged Advanced Variants:       0 / 13  ( 0.0%)
Codex-Only Reference Cards:     4 / 13  (30.8%)
Pending Human Review:           0 / 13  ( 0.0%)
-----------------------------------------------------
Total Canonical Upa-Sutras:    13 / 13 (100.0%)
=====================================================
```

---

## 3. Disambiguation & Overlap Rationales

1. **Upa-Sutra 7 (Yavadunam Remix) vs. World 4 (Yavadunam)**:
   - World 4 introduces single-digit deficiency squaring near base 100/1000.
   - Upa-Sutra 7 acts as an advanced **Mastery Remix** testing 2-digit deficiencies requiring carry propagation ($88^2 = (88-12) \parallel 12^2 = 76 \parallel 144 = 7744$).
2. **Upa-Sutra 8 (Antyayordasake'pi) vs. Pop-Math Conflations**:
   - $43 \times 47 = 2021$ is the canonical demonstration of Upa-Sutra 8. Pop-math web blogs sometimes mistakenly call this *Antyayoreva*, but historical ground truth attributes this exclusively to *Antyayordashake'pi*.
3. **Upa-Sutra 9 (Antyayoreva) vs. World 9 & World 13**:
   - Swami BKT (1965 Ch. 9) documents *Antyayoreva* as rational equations with matching constant ratios yielding $x = 0$.
   - Because $x=0$ is always the answer, making it a playable keypad quest produces trivial, degenerate gameplay.
   - Multiplication by 11 ($M \times 11$) is mathematically identical to $N=1$ in World 13 (*Sopantyadvayamantyam*).
   - Resolved as **`CODEX_ONLY`**.
4. **Upa-Sutras 10 & 13 vs. World 14**:
   - Main Sutra 14 (*Gunitasamuccayah*) is the LHS clause ("Product of the sum").
   - Upa-Sutra 10 (*Samuccayagunitah*) is the RHS clause ("Sum of the product").
   - Upa-Sutra 13 (*Gunitasamuccayah Samuccayagunitah*) is the complete compound sentence.
   - World 14 already implements the full two-sided verification. Both Upa-Sutras 10 and 13 are resolved as **`CODEX_ONLY`** references featuring multivariable expansions ($(x+y+z)^2 \implies 3^2 = 9$).
5. **Upa-Sutra 11 (Lopanasthapanabhyam) vs. Main Campaign Worlds**:
   - Distinct 4-step algorithm: (1) $y=0$ reduction, (2) $x=0$ reduction, (3) constant pairing, (4) $xy$ cross-term verification.
   - Fully implemented as Playable Quest 8.
