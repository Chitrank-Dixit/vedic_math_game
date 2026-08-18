# Canonical Registry: 13 Vedic Upa-Sutras (Sub-Sutras)

**Document Status:** Complete Canonical Registry / Phase U0  
**Scope:** 13 Vedic Mathematics Upa-Sutras  
**Application:** Side Quests, Treasury Map, and Codex Reference in *Ankh: The Sutra Saga*  

---

## 1. Master Registry Table

| # | ID (`UpaSutraId`) | Sanskrit Name | English Translation / Meaning | Math Domain | Parent Main World | Unlock Requirement | Verification Tier | Canonical Worked Example |
|---|---|---|---|---|---|---|---|---|
| 1 | `ANURUPYENA` | **Anurupyena** | Proportionately | Arithmetic / Sub-base Multiplication | World 2 (Nikhilam) & World 5 (Urdhva) | Complete World 2 | Tier A | $48 \times 46$ near working base $50 = 100/2 \implies 2208$ |
| 2 | `SHISYATE_SHESAMAJNA` | **Shisyate Shesamajna** | The remainder remains constant | Polynomial Residue / Modular Arithmetic | World 6 (Paravartya) | Complete World 6 | Tier B | $(x^3 - 3x^2 + 4x - 5) \div (x - 2) \implies R = -1$ |
| 3 | `ADYAMADYENANTYAMANTYENA` | **Adyamadyenantyamantyena** | First by first, last by last | Quadratic Factorization Structure | World 5 & World 10 | Complete World 10 | Tier A | $2x^2 + 7x + 5 = (2x+5)(x+1)$ |
| 4 | `KEVALAIHSAPTAKAM_GUNYAT` | **Kevalaihsaptakam Gunyat** | For 7 the multiplicand is 143 | Fraction & Decimal Expansion | World 12 (Shesanyankena) | Complete World 12 | Tier B | $\frac{1}{7} \implies 143 \times 7 = 1001 \implies 0.\overline{142857}$ |
| 5 | `VESHTANAM` | **Veshtanam** | Osculation / Prime Divisibility | Number Theory | World 2 & Standalone | Complete World 5 | Tier B | $247 \div 19 \implies$ Osculator $2 \implies 24 + 7(2) = 38$ (divisible) |
| 6 | `YAVADUNAM_TAVADUNAM` | **Yavadunam Tavadunam** | Lessen by deficiency and setup square | Base Cubing | World 4 (Yavadunam) | Complete World 4 | Tier B | $104^3 = 112 \,\|\, 48 \,\|\, 64 = 1124864$ |
| 7 | `YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET` | **Yavadunam Tavadunikritya Vargan-cha Yojayet** | Whatever the deficiency, lessen by that and add square | Multi-Digit Squaring | World 4 (Yavadunam) | Complete World 4 | Tier A | $97^2 = (97-3) \,\|\, 3^2 = 9409$ |
| 8 | `ANTYAYORDASHAKEPI` | **Antyayordashake'pi** | Last digits sum to 10 and first digits are same | Mental Multiplication | World 1 (Ekadhikena) | Complete World 1 | Tier A | $43 \times 47 = (4 \times 5) \,\|\, (3 \times 7) = 2021$ |
| 9 | `ANTYAYEREVA` | **Antyayereva** | Only the last terms | Rational Equations | World 9 & World 13 | Complete World 9 | Tier B | $\frac{x+2}{x+3} = \frac{x+4}{x+6} \implies \text{ratio } \frac{2}{3} = \frac{4}{6} \implies x = 0$ |
| 10 | `SAMUCCAYAGUNITAH` | **Samuccayagunitah** | The sum of products | Algebraic Validation | World 14 (Gunitasamuccayah) | Complete World 14 | Tier B | $(2x+3y)^2 \implies \text{eval at } 1,1 \implies 5^2 = 25$ |
| 11 | `LOPANA_STHAPANABHYAM` | **Lopana-Sthapanabhyam** | By elimination and retention | Bivariate Quadratic Factorization | World 11 (Vyashtisamashtih) | Complete World 11 | Tier B | $2x^2+5xy+2y^2+4x+5y+2 = (2x+y+2)(x+2y+1)$ |
| 12 | `VILOKANAM` | **Vilokanam** | By mere observation | Inspection Puzzles | World 9 (Shunyam) | Complete World 9 | Tier A | $x + \frac{1}{x} = 2.5 \implies x = 2 \text{ or } 0.5$ |
| 13 | `GUNITASAMUCCAYAH_SAMUCCAYAGUNITAH` | **Gunitasamuccayah Samuccayagunitah** | Product of sum is sum of products | Symmetric Polynomial Validation | World 14 & World 15 | Complete World 15 | Tier B | $x(y^2-z^2)+y(z^2-x^2)+z(x^2-y^2) \implies (x-y)(y-z)(z-x)$ |

---

## 2. Detailed Upa-Sutra Specifications

### Upa-Sutra 1: Anurupyena ("Proportionately")
- **Parent World:** World 2 (Nikhilam Navatashcaramam) & World 5 (Urdhva-Tiryagbhyam).
- **Core Formula:** Working Base $W = B / k$ or $W = B \times k$. Scale cross-subtracted LHS by $1/k$ or $k$.
- **Example:** $48 \times 46$ near base $50 = 100 / 2$. Deficiencies $(-2, -4)$. LHS: $(48-4)/2 = 22$. RHS: $(-2)(-4) = 08 \implies 2208$.

### Upa-Sutra 2: Shisyate Shesamajna ("The remainder remains constant")
- **Parent World:** World 6 (Paravartya Yojayet).
- **Core Formula:** To find remainder of $P(x) \div (x - a)$, evaluate $P(a)$.
- **Example:** $(x^3 - 3x^2 + 4x - 5) \div (x - 2) \implies P(2) = 8 - 12 + 8 - 5 = -1$.

### Upa-Sutra 3: Adyamadyenantyamantyena ("First by first, last by last")
- **Parent World:** World 10 (Puranapuranabhyam) & World 5.
- **Core Formula:** For $Ax^2 + Bx + C = (a_1 x + c_1)(a_2 x + c_2)$, $a_1 a_2 = A$ and $c_1 c_2 = C$.
- **Example:** $2x^2 + 7x + 5 = (2x + 5)(x + 1)$.

### Upa-Sutra 4: Kevalaihsaptakam Gunyat ("For 7 the multiplicand is 143")
- **Parent World:** World 12 (Shesanyankena Charamena).
- **Core Formula:** $143 \times 7 = 1001 \implies$ period extraction for divisions by 7.
- **Example:** $1/7 = 0.\overline{142857}$.

### Upa-Sutra 5: Veshtanam ("Osculation")
- **Parent World:** World 2 & Number Theory.
- **Core Formula:** Divisibility of $N$ by divisor $d = 10k - 1$ (pos osculator $k$) or $10k + 1$ (neg osculator $k$).
- **Example:** $247 \div 19$ (osculator 2): $24 + 7(2) = 38$ (divisible by 19).

### Upa-Sutra 6: Yavadunam Tavadunam ("Lessen by deficiency and setup square")
- **Parent World:** World 4 (Yavadunam).
- **Core Formula:** $(B + d)^3 = B^2(B + 3d) + B(3d^2) + d^3$.
- **Example:** $104^3 = 100^2(112) + 100(48) + 64 = 1124864$.

### Upa-Sutra 7: Yavadunam Tavadunikritya Vargan-cha Yojayet ("Whatever the deficiency, lessen by that and add square")
- **Parent World:** World 4 (Yavadunam).
- **Core Formula:** $(B \pm d)^2 = (B \pm 2d) \cdot B + d^2$.
- **Example:** $97^2 = (97 - 3) \,\|\, 3^2 = 9409$.

### Upa-Sutra 8: Antyayordashake'pi ("Last digits sum to 10 and first digits same")
- **Parent World:** World 1 (Ekadhikena Purvena).
- **Core Formula:** For $(10T + U_1)(10T + U_2)$ where $U_1 + U_2 = 10$: LHS $= T(T + 1)$, RHS $= U_1 \times U_2$ (2 digits).
- **Example:** $43 \times 47 = (4 \times 5) \,\|\, (3 \times 7) = 2021$.

### Upa-Sutra 9: Antyayereva ("Only the last terms")
- **Parent World:** World 9 (Shunyam Samyasamuccaye) & World 13.
- **Core Formula:** If constant term ratios match on both sides, $x = 0$.
- **Example:** $\frac{x+2}{x+3} = \frac{x+4}{x+6} \implies 2/3 = 4/6 \implies x = 0$.

### Upa-Sutra 10: Samuccayagunitah ("The sum of products")
- **Parent World:** World 14 (Gunitasamuccayah).
- **Core Formula:** Digital root / coefficient sum validation for multi-variable polynomials.
- **Example:** $(2x + 3y)^2$ evaluated at $x=1, y=1 \implies 5^2 = 25$.

### Upa-Sutra 11: Lopana-Sthapanabhyam ("By elimination and retention")
- **Parent World:** World 11 (Vyashtisamashtih).
- **Core Formula:** Setting $y=0$ then $x=0$ to decompose 2-variable second-degree polynomials.
- **Example:** $2x^2 + 5xy + 2y^2 + 4x + 5y + 2 = (2x + y + 2)(x + 2y + 1)$.

### Upa-Sutra 12: Vilokanam ("By mere observation")
- **Parent World:** World 9 (Shunyam Samyasamuccaye).
- **Core Formula:** Symmetry and inspection solving.
- **Example:** $x + 1/x = 2.5 = 2 + 1/2 \implies x = 2, 0.5$.

### Upa-Sutra 13: Gunitasamuccayah Samuccayagunitah ("Product of sum is sum of products")
- **Parent World:** World 14 & World 15.
- **Core Formula:** Validation of cyclic symmetric polynomials $x(y^2-z^2) + y(z^2-x^2) + z(x^2-y^2)$.
- **Example:** Factorized cyclic form $(x-y)(y-z)(z-x)$.
