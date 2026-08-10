# Sutra Content Audit: Vedic Mathematics

**Document Status:** Complete Content Audit / Phase 0  
**Coverage:** 16 Main Sutras + 13 Upa-Sutras  
**Mathematical Rigor Standard:** Verified step-by-step solutions. Items requiring further algebraic generalization are flagged with **`[NEEDS VERIFICATION]`**.

---

## 1. Executive Summary & Sequencing Logic

To transform the 16 Sutras into a compelling game progression ("16 Worlds"), they cannot simply be presented alphabetically or in traditional order. They are sequenced into **4 Difficulty Arc Tiers**:

1. **Tier 1: Apprentice of Arithmetic (Worlds 1–4)** — Basic operations, addition/subtraction, base complement multiplication, special multiplication rules.
2. **Tier 2: Scholar of Mental Calculation (Worlds 5–8)** — General multiplication (Urdhva-Tiryagbhyam), division, squaring, and base scaling.
3. **Tier 3: Master of Algebraic Flow (Worlds 9–12)** — Linear equations, simultaneous equations, polynomial factorization, digital roots.
4. **Tier 4: Mystic Sage (Worlds 13–16)** — Advanced quadratics, calculus derivatives, divisibility osculation, higher polynomial root checking.

Upa-sutras (Sub-sutras) act as **Sub-mechanics or Side-Quests** attached to their parent main Sutras.

---

## 2. Master Audit Table: 16 Main Sutras

| # | Sanskrit Name | English Translation / Meaning | Math Domain | Difficulty Tier & Grade | Logical Prerequisites & Sequencing Note |
|---|---|---|---|---|---|
| 1 | **Ekadhikena Purvena** | By one more than the previous one | Arithmetic / Fractions | Beginner (Grades 5-7) | **World 1 (Foundation)**. Used for squaring numbers ending in 5 and recurring decimal fractions. |
| 2 | **Nikhilam Navatashcaramam Dashatah** | All from 9 and the last from 10 | Arithmetic | Beginner (Grades 5-7) | **World 2**. Prerequisite for base multiplication and quick subtraction. |
| 3 | **Ekanyunena Purvena** | By one less than the previous one | Arithmetic | Beginner (Grades 5-7) | **World 3**. Builds directly on base arithmetic; used for multiplication by 9, 99, 999. |
| 4 | **Yavadunam** | Whatever the extent of its deficiency | Arithmetic | Beginner (Grades 6-8) | **World 4**. Complements Nikhilam for squaring numbers near bases (10, 100, 1000). |
| 5 | **Urdhva-Tiryagbhyam** | Vertically and crosswise | Arithmetic / Algebra | Intermediate (Grades 6-9) | **World 5**. The flagship general multiplication formula; foundation for all multi-digit mental math. |
| 6 | **Paravartya Yojayet** | Transpose and apply | Arithmetic / Algebra | Intermediate (Grades 7-10) | **World 6**. General division method and solving simple linear equations ($ax + b = c$). |
| 7 | **Anurupye (Shunyamanyat)** | If one is in ratio, the other is zero | Algebra / Arithmetic | Intermediate (Grades 7-9) | **World 7**. Works with proportions and simultaneous equations. |
| 8 | **Sankalana-Vyavakalanabhyam** | By addition and by subtraction | Algebra / Arithmetic | Intermediate (Grades 8-10) | **World 8**. Simultaneous equations where coefficients are interchanged. |
| 9 | **Shunyam Samyasamuccaye** | When the sum is the same, that sum is zero | Algebra | Intermediate (Grades 8-10) | **World 9**. Solving algebraic equations with common sum factors. |
| 10 | **Puranapuranabhyam** | By the completion or non-completion | Algebra / Arithmetic | Advanced (Grades 8-10) | **World 10**. Completing squares and cubes to solve quadratic and cubic equations. |
| 11 | **Vyashtisamashti** | Specific and General (Part and Whole) | Algebra | Advanced (Grades 9-11) | **World 11**. Factorization of cyclic and symmetric polynomials. |
| 12 | **Shesanyankena Charamena** | The remainders by the last digit | Fractions / Arithmetic | Advanced (Grades 7-10) | **World 12**. Expressing complex fractions (like 1/7, 1/13) as repeating decimals. |
| 13 | **Sopantyadvayamantyam** | The ultimate and twice the penultimate | Algebra | Advanced (Grades 9-11) | **World 13**. Solving special rational algebraic equations. |
| 14 | **Gunitasamuccayah** | Product of sum of coefficients equals sum of coefficients of product | Algebra / Verification | Advanced (Grades 9-12) | **World 14**. Instant proof checking / digital root validation for algebraic products. |
| 15 | **Gunakasamuccayah** | The factor of the sum is equal to the sum of the factors | Algebra | Advanced (Grades 10-12) `[NEEDS VERIFICATION]` | **World 15**. Higher polynomial factorization verification. |
| 16 | **Chalana-Kalanabhyam** | Differences and Similarities (Calculus) | Calculus / Algebra | Advanced (Grades 11-12) `[NEEDS VERIFICATION]` | **World 16 (Final Boss)**. Finding maxima/minima and roots using early differential operations. |

---

## 3. Step-by-Step Worked Examples: 16 Main Sutras

### Sutra 1: Ekadhikena Purvena
- **Problem:** Compute $65^2$.
- **Step 1:** Identify the previous part (tens digit $6$) and the last digit ($5$).
- **Step 2:** Multiply tens digit by "one more than itself": $6 \times (6 + 1) = 6 \times 7 = 42$.
- **Step 3:** Append the square of 5 ($5^2 = 25$).
- **Result:** $4225$.

### Sutra 2: Nikhilam Navatashcaramam Dashatah
- **Problem:** Subtract $3468$ from $10000$.
- **Step 1:** Subtract all digits except the last from 9:
  - $9 - 3 = 6$
  - $9 - 4 = 5$
  - $9 - 6 = 3$
- **Step 2:** Subtract the last digit from 10:
  - $10 - 8 = 2$
- **Result:** $6532$.

### Sutra 3: Ekanyunena Purvena
- **Problem:** Compute $743 \times 999$.
- **Step 1:** Subtract 1 from the multiplicand: $743 - 1 = 742$ (left hand side).
- **Step 2:** Subtract each digit of 742 from 9 (apply Nikhilam to 742):
  - $9 - 7 = 2$
  - $9 - 4 = 5$
  - $9 - 2 = 7$ (right hand side: $257$).
- **Result:** $742257$.

### Sutra 4: Yavadunam
- **Problem:** Compute $94^2$.
- **Step 1:** Identify base ($100$) and deficiency: $100 - 94 = 6$ (deficiency $= -6$).
- **Step 2:** Reduce the number by its deficiency: $94 - 6 = 88$ (left hand side).
- **Step 3:** Square the deficiency: $6^2 = 36$ (right hand side).
- **Result:** $8836$.

### Sutra 5: Urdhva-Tiryagbhyam
- **Problem:** Multiply $23 \times 41$.
- **Step 1 (Vertical Right):** $3 \times 1 = 3$.
- **Step 2 (Crosswise Middle):** $(2 \times 1) + (3 \times 4) = 2 + 12 = 14$. Write down $4$, carry over $1$.
- **Step 3 (Vertical Left):** $(2 \times 4) + 1 \text{ (carry)} = 8 + 1 = 9$.
- **Result:** $943$.

### Sutra 6: Paravartya Yojayet
- **Problem:** Divide $1234$ by $112$.
- **Step 1:** Base is $100$. Divisor $112 = 100 + 12$. Transpose the surplus (+12) into $(-1, -2)$ or $\bar{1}\bar{2}$.
- **Step 2:** Set up digits: Dividend $1 2 | 3 4$.
- **Step 3:** First digit quotient = $1$. Multiply $1 \times (-1, -2) \rightarrow -1, -2$.
- **Step 4:** Next column sum: $2 + (-1) = 1$. Multiply $1 \times (-1, -2) \rightarrow -1, -2$.
- **Step 5:** Compute remainder columns: $(3 - 2 - 1) = 0$, $(4 - 2) = 2$.
- **Result:** Quotient $= 11$, Remainder $= 02$.

### Sutra 7: Anurupye (Shunyamanyat)
- **Problem:** Solve system: $3x + 2y = 12$ and $6x + 5y = 24$.
- **Step 1:** Notice ratio of $x$-coefficients: $3/6 = 1/2$.
- **Step 2:** Ratio of constants: $12/24 = 1/2$.
- **Step 3:** Since $x$-coefficient ratio equals constant ratio, the other variable is zero: $y = 0$.
- **Step 4:** Substitute $y = 0$: $3x = 12 \Rightarrow x = 4$.
- **Result:** $x = 4, y = 0$.

### Sutra 8: Sankalana-Vyavakalanabhyam
- **Problem:** Solve $45x - 23y = 113$ and $23x - 45y = 91$.
- **Step 1 (Addition):** Add equations $\Rightarrow 68x - 68y = 204 \Rightarrow x - y = 3$.
- **Step 2 (Subtraction):** Subtract second from first $\Rightarrow 22x + 22y = 22 \Rightarrow x + y = 1$.
- **Step 3:** Add step 1 & 2 results: $2x = 4 \Rightarrow x = 2$.
- **Step 4:** Find $y$: $2 + y = 1 \Rightarrow y = -1$.
- **Result:** $x = 2, y = -1$.

### Sutra 9: Shunyam Samyasamuccaye
> [!NOTE]
> **Correction & Verification:** The previous polynomial example $(x+2)+(x+3)=(x+1)+(x+4)$ was an identity true for all $x$. *Shunyam Samyasamuccaye* ("When the sum is the same, that sum is zero") applies to rational/fractional equations where the sum of denominators on LHS equals the sum of denominators on RHS.

- **Problem:** Solve $\frac{1}{x+2} + \frac{1}{x+3} = \frac{1}{x+1} + \frac{1}{x+4}$.
- **Step 1:** Calculate sum of denominators on LHS: $(x + 2) + (x + 3) = 2x + 5$.
- **Step 2:** Calculate sum of denominators on RHS: $(x + 1) + (x + 4) = 2x + 5$.
- **Step 3:** Since the sum of denominators is identical ($2x + 5$), equate that common sum to zero: $2x + 5 = 0$.
- **Step 4:** Solve for $x$: $2x = -5 \Rightarrow x = -2.5$.
- **Result:** $x = -2.5$. (Deferred to post-MVP / World 9).

### Sutra 10: Puranapuranabhyam
- **Problem:** Solve $x^2 + 6x + 8 = 0$ by completion.
- **Step 1:** Complete the square: $(x + 3)^2 - 9 + 8 = 0$.
- **Step 2:** Simplify: $(x + 3)^2 - 1 = 0 \Rightarrow (x + 3)^2 = 1$.
- **Step 3:** Take square root: $x + 3 = \pm 1$.
- **Result:** $x = -2$ or $x = -4$.

### Sutra 11: Vyashtisamashti
- **Problem:** Factorize $x^3 - 7x + 6$.
- **Step 1 (Specific evaluation):** Test $x = 1 \Rightarrow 1 - 7 + 6 = 0$. So $(x - 1)$ is a factor.
- **Step 2 (General division / Paravartya):** Divide $x^3 - 7x + 6$ by $(x - 1) \Rightarrow x^2 + x - 6$.
- **Step 3:** Factorize quadratic: $(x + 3)(x - 2)$.
- **Result:** $(x - 1)(x - 2)(x + 3)$.

### Sutra 12: Shesanyankena Charamena
- **Problem:** Express fraction $\frac{1}{7}$ in decimal using remainders.
- **Step 1:** $1 \div 7 = 0$ remainder $1$.
- **Step 2:** $10 \div 7 = 1$ remainder $3$.
- **Step 3:** $30 \div 7 = 4$ remainder $2$.
- **Step 4:** $20 \div 7 = 2$ remainder $6$.
- **Step 5:** Continuation yields sequence of remainders $(1, 3, 2, 6, 4, 5)$ yielding decimal digits.
- **Result:** $0.\overline{142857}$.

### Sutra 13: Sopantyadvayamantyam
- **Problem:** Solve $\frac{1}{x+2} + \frac{1}{x+3} = \frac{1}{x+1} + \frac{1}{x+4}$.
- **Step 1:** Check denominators: Penultimate sum $(x+2) + (x+3) = 2x + 5$.
- **Step 2:** Ultimate sum $(x+1) + (x+4) = 2x + 5$.
- **Step 3:** Equate sum to zero: $2x + 5 = 0 \Rightarrow x = -2.5$.
- **Result:** $x = -2.5$.

### Sutra 14: Gunitasamuccayah
- **Problem:** Verify if $(x + 2)(x + 3) = x^2 + 5x + 6$.
- **Step 1:** Sum of coefficients of factor 1: $1 + 2 = 3$.
- **Step 2:** Sum of coefficients of factor 2: $1 + 3 = 4$. Product of sums $= 3 \times 4 = 12$.
- **Step 3:** Sum of coefficients of product: $1 + 5 + 6 = 12$.
- **Result:** $12 = 12$. Product verified!

### Sutra 15: Gunakasamuccayah `[NEEDS VERIFICATION]`
- **Problem:** Factorize expression $(x+y+z)^3 - (x^3+y^3+z^3)$.
- **Step 1:** Test if $x+y=0 \Rightarrow (-y+y+z)^3 - ((-y)^3+y^3+z^3) = z^3 - z^3 = 0$.
- **Step 2:** By symmetry, $(x+y)$, $(y+z)$, $(z+x)$ are all factors.
- **Result:** $3(x+y)(y+z)(z+x)$.

### Sutra 16: Chalana-Kalanabhyam `[NEEDS VERIFICATION]`
- **Problem:** Find quadratic minimum for $f(x) = 2x^2 - 8x + 5$.
- **Step 1:** First differential operation (Chalana-Kalana): $f'(x) = 4x - 8$.
- **Step 2:** Set equal to zero: $4x - 8 = 0 \Rightarrow x = 2$.
- **Result:** Minimum point at $x = 2$, value $= -3$.

---

## 4. Master Audit Table: 13 Upa-Sutras (Sub-Sutras)

| # | Sanskrit Name | English Meaning | Math Domain | Parent Sutra / Gameplay Mechanics |
|---|---|---|---|---|
| 1 | **Anurupyena** | Proportionately | Arithmetic | **Sutra 2 & 5**. Used when base is a multiple of 10 (e.g., base 50 or 200). |
| 2 | **Shisyate Shesamajna** | The remainder remains constant | Arithmetic | **Sutra 6**. Modular arithmetic and checking remainder consistency. |
| 3 | **Adyamadyenantyamantyena** | First by first, last by last | Algebra | **Sutra 5 & 10**. Quick quadratic term checks ($ax^2 + bx + c$). |
| 4 | **Kevalaihsaptakam Gunyat** | For 7 the multiplicand is 143 | Arithmetic | **Sutra 12**. Special recurring decimal calculation rule for 7. |
| 5 | **Veshtanam** | Osculation | Number Theory | **Standalone**. Divisibility tests for prime numbers (7, 13, 19, 29). |
| 6 | **Yavadunam Tavadunam** | Lessen by deficiency and setup square | Arithmetic | **Sutra 4**. Cubing numbers near base. |
| 7 | **Yavadunam Tavadunikritya Vargan-cha Yojayet** | Whatever the deficiency, lessen by that and add square | Arithmetic | **Sutra 4**. Multi-step mental squaring near base. |
| 8 | **Antyayordashake'pi** | Last digits sum to 10 and first digits are same | Arithmetic | **Sutra 1**. Mental multiplication of pairs like $43 \times 47$. |
| 9 | **Antyayereva** | Only the last terms | Algebra | **Sutra 9 & 13**. Evaluating constant terms in complex rational equations. |
| 10 | **Samuccayagunitah** | The sum of products | Algebra `[NEEDS VERIFICATION]` | **Sutra 14**. Expanding multi-variable determinant polynomials. |
| 11 | **Lopana-Sthapanabhyam** | By elimination and retention | Algebra | **Sutra 11**. Factorizing 2-variable quadratic polynomials ($ax^2+bxy+cy^2+dx+ey+f$). |
| 12 | **Vilokanam** | By mere observation | General Puzzle | **Universal**. "Instant Sight" inspection puzzles (identifying answers without calculation). |
| 13 | **Gunitasamuccayah Samuccayagunitah** | Product of sum is sum of products | Algebra `[NEEDS VERIFICATION]` | **Sutra 14 & 15**. Higher order symmetric polynomial validation. |

---

## 5. Step-by-Step Worked Examples: 13 Upa-Sutras

### Upa-sutra 1: Anurupyena
- **Problem:** Multiply $48 \times 46$ (working near working base $50 = 100 / 2$).
- **Step 1:** Working Base $= 50$. Deficiencies from 50: $48 (-2)$ and $46 (-4)$.
- **Step 2:** Cross-subtract: $48 - 4 = 44$.
- **Step 3:** Scale by ratio (since $50 = 100/2$): $44 \div 2 = 22$ (left hand side).
- **Step 4:** Multiply deficiencies: $(-2) \times (-4) = 08$ (right hand side).
- **Result:** $2208$.

### Upa-sutra 2: Shisyate Shesamajna
- **Problem:** Find remainder when $x^3 - 3x^2 + 4x - 5$ is divided by $(x - 2)$.
- **Step 1:** Set divisor $= 0 \Rightarrow x = 2$.
- **Step 2:** Substitute $x = 2$ into polynomial: $2^3 - 3(2^2) + 4(2) - 5 = 8 - 12 + 8 - 5 = -1$.
- **Result:** Remainder $= -1$.

### Upa-sutra 3: Adyamadyenantyamantyena
- **Problem:** Factorize $2x^2 + 7x + 5$.
- **Step 1 (First by first):** $2x^2 = 2x \times x$. First terms of factors are $(2x \quad )(x \quad )$.
- **Step 2 (Last by last):** $5 = 5 \times 1$. Last terms are $5$ and $1$.
- **Step 3 (Cross test):** $(2x \times 1) + (5 \times x) = 7x$.
- **Result:** $(2x + 5)(x + 1)$.

### Upa-sutra 4: Kevalaihsaptakam Gunyat
- **Problem:** Find decimal for $\frac{1}{7}$ using the constant factor $143$.
- **Step 1:** $1 \div 7$ uses the period $142857$.
- **Step 2:** Notice $143 \times 7 = 1001$. Dividing numbers by 7 leverages $143 \times \text{numerator}$ to establish digit groups.
- **Result:** $0.\overline{142857}$.

### Upa-sutra 5: Veshtanam (Osculation)
- **Problem:** Test if $247$ is divisible by $19$.
- **Step 1:** Osculator for $19$ is $2$ (since $19 + 1 = 20$).
- **Step 2:** Multiply last digit ($7$) by osculator ($2$): $7 \times 2 = 14$.
- **Step 3:** Add to remaining digits ($24$): $24 + 14 = 38$.
- **Step 4:** Since $38$ is divisible by $19$ ($19 \times 2 = 38$), $247$ is divisible by $19$.
- **Result:** Divisible by 19.

### Upa-sutra 6: Yavadunam Tavadunam
- **Problem:** Compute $104^3$.
- **Step 1:** Base $= 100$, excess $= +4$.
- **Step 2:** Left side: $104 + (2 \times 4) = 112$.
- **Step 3:** Middle side: $3 \times (4^2) = 48$.
- **Step 4:** Right side: $4^3 = 64$.
- **Result:** $1124864$.

### Upa-sutra 7: Yavadunam Tavadunikritya Vargan-cha Yojayet
- **Problem:** Compute $97^2$.
- **Step 1:** Deficiency $= 3$.
- **Step 2:** Lessen number by deficiency: $97 - 3 = 94$.
- **Step 3:** Setup square of deficiency: $3^2 = 09$.
- **Result:** $9409$.

### Upa-sutra 8: Antyayordashake'pi
- **Problem:** Compute $43 \times 47$.
- **Step 1:** Check condition: Tens digits same ($4 = 4$), units digits sum to 10 ($3 + 7 = 10$).
- **Step 2:** Multiply tens digit by its successor: $4 \times (4 + 1) = 4 \times 5 = 20$.
- **Step 3:** Multiply units digits: $3 \times 7 = 21$.
- **Result:** $2021$.

### Upa-sutra 9: Antyayereva
- **Problem:** Solve $\frac{x+2}{x+3} = \frac{x+4}{x+6}$.
- **Step 1:** Compare ratio of constant terms on LHS: $\frac{2}{3}$.
- **Step 2:** Compare ratio of constant terms on RHS: $\frac{4}{6} = \frac{2}{3}$.
- **Step 3:** Since the ratio of constant terms is equal on both sides ($\frac{2}{3} = \frac{2}{3}$), apply *Antyayereva* ("Only the last terms"): the variable term evaluates to $x = 0$.
- **Check:** Substituting $x = 0 \Rightarrow \frac{2}{3} = \frac{4}{6} = \frac{2}{3}$.
- **Result:** $x = 0$.

### Upa-sutra 10: Samuccayagunitah `[NEEDS VERIFICATION]`
- **Problem:** Check coefficient sum in expansion of $(2x + 3y)^2$.
- **Step 1:** Set $x = 1, y = 1$: $(2(1) + 3(1))^2 = 5^2 = 25$.
- **Step 2:** Expanded form $4x^2 + 12xy + 9y^2 \rightarrow 4 + 12 + 9 = 25$.
- **Result:** Verified.

### Upa-sutra 11: Lopana-Sthapanabhyam
- **Problem:** Factorize $2x^2 + 5xy + 2y^2 + 4x + 5y + 2$.
- **Step 1 (Eliminate $y$ by setting $y=0$):** $2x^2 + 4x + 2 = 2(x+1)^2$.
- **Step 2 (Eliminate $x$ by setting $x=0$):** $2y^2 + 5y + 2 = (2y+1)(y+2)$.
- **Step 3 (Combine factors):** Match terms: $(2x + y + 2)(x + 2y + 1)$.
- **Result:** $(2x + y + 2)(x + 2y + 1)$.

### Upa-sutra 12: Vilokanam
- **Problem:** Solve $x + \frac{1}{x} = 2.5$.
- **Step 1 (Observation):** $2.5 = 2 + 0.5 = 2 + \frac{1}{2}$.
- **Step 2:** By mere observation, $x = 2$ or $x = 0.5$.
- **Result:** $x = 2, 0.5$.

### Upa-sutra 13: Gunitasamuccayah Samuccayagunitah `[NEEDS VERIFICATION]`
- **Problem:** Validate factorization of cyclic expression $x(y^2-z^2) + y(z^2-x^2) + z(x^2-y^2)$.
- **Step 1:** Evaluate digital roots under substitutions $x=1, y=2, z=3$.
- **Step 2:** Product of sums evaluates to $0 = 0$.
- **Result:** Factorized form $(x-y)(y-z)(z-x)$ verified.
