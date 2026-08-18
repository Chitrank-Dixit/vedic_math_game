# Mathematical Specification: Upa-Sutra 1 — Anurupyena ("Proportionately")

**Document Status:** Complete Mathematical Specification / Phase U2  
**Feature:** Upa-Sutra Treasury — Quest 2  
**Target Class:** `AnurupyenaGenerator`  
**Parent Worlds:** World 2 (Nikhilam Navatashcaramam) & World 5 (Urdhva-Tiryagbhyam)  
**Unlock Requirement:** Completion of World 2 (Nikhilam)  

---

## 1. Mathematical Formulation

*Anurupyena* ("Proportionately") extends near-base multiplication to numbers clustering near a **Working Base ($W$)** that is an integer multiple or fraction of a primary power-of-ten reference base ($B = 10^n$):

$$W = \frac{B}{k} \quad \text{or} \quad W = B \times k \quad (B = 10^2 = 100, \, k = 2)$$

### Supported Working Bases in First Release
1. **Working Base 50 ($W = 50, B = 100, k = 2$, operation: division by $2$)**
2. **Working Base 200 ($W = 200, B = 100, k = 2$, operation: multiplication by $2$)**

---

## 2. Step-by-Step Algorithm & Algebraic Proof

Let two numbers be $N_1 = W + d_1$ and $N_2 = W + d_2$, where $d_1, d_2$ are deviations from working base $W$.

### Algebraic Identity
$$N_1 \times N_2 = (W + d_1)(W + d_2) = W^2 + W(d_1 + d_2) + d_1 d_2$$
$$= W(W + d_1 + d_2) + d_1 d_2$$

Let the **Cross-Add Result** be:
$$C = N_1 + d_2 = N_2 + d_1 = W + d_1 + d_2$$

Then:
$$N_1 \times N_2 = W \cdot C + d_1 d_2$$

### Case A: Fractional Working Base ($W = B / k = 100 / 2 = 50$)
$$N_1 \times N_2 = \frac{100}{2} \cdot C + d_1 d_2 = 100 \cdot \left(\frac{C}{2}\right) + (d_1 \times d_2)$$
- **Left Hand Side (Scaled)**: $L = \frac{C}{2} = \frac{N_1 + d_2}{2}$
- **Right Hand Side**: $R = d_1 \times d_2$
- **Total Product**: $N_1 \times N_2 = 100 \cdot L + R$

### Case B: Multiple Working Base ($W = B \times k = 100 \times 2 = 200$)
$$N_1 \times N_2 = (100 \times 2) \cdot C + d_1 d_2 = 100 \cdot (2C) + (d_1 \times d_2)$$
- **Left Hand Side (Scaled)**: $L = 2 \times C = 2 \times (N_1 + d_2)$
- **Right Hand Side**: $R = d_1 \times d_2$
- **Total Product**: $N_1 \times N_2 = 100 \cdot L + R$

---

## 3. Handling Right-Hand Parts & Borrow Normalization

Since primary base $B = 100$ has 2 zeros, the right-hand block $R$ has a standard width of **2 digits**:

1. **Positive Right Part ($R \ge 0, R < 100$)**:
   - Padded to 2 digits: e.g. $R = 8 \implies \text{"08"}$.
   - Product $= L \times 100 + R = L \,\|\, \text{pad2}(R)$.
2. **Negative Right Part ($R < 0$, Mixed-Sign Deviations)**:
   - Borrow 1 unit ($100$) from the left part $L$:
   - Adjusted Left Part $= L - 1$
   - Adjusted Right Part $= 100 + R = 100 - |R|$
   - Product $= (L - 1) \times 100 + (100 + R) = (L - 1) \,\|\, \text{pad2}(100 + R)$.
   - *Example ($196 \times 204$)*: $L = 400, R = -16 \implies 399 \,\|\, 84 = 39984$.
3. **Carry-Over ($R \ge 100$)**:
   - Carry to $L$: $L_{\text{new}} = L + \lfloor R / 100 \rfloor$, $R_{\text{new}} = R \pmod{100}$.

---

## 4. Policy on Non-Integer Scale Division (Base 50)

For working base $W = 50$, the scale step requires dividing $C$ by $2$:
$$C = 50 + d_1 + d_2$$
- $C$ is even if and only if $d_1 + d_2$ is even (i.e. $d_1 \equiv d_2 \pmod 2$).
- If $C$ is odd (e.g. $54 \times 47 \implies d_1 = +4, d_2 = -3 \implies C = 51, 51/2 = 25.5$):
  - **Scope Decision for Phase U2**: We strictly **exclude** odd cross-add cases from problem generation, classifying any such inputs explicitly as `NON_INTEGER_SCALE_EXCLUDED`.
  - All generated problems in Base 50 constructively select deviations where $d_1 \equiv d_2 \pmod 2$.

---

## 5. Verified Canonical Seed Examples

### Canonical Example 1: Base 50 Both Negative Deviations
- **Problem**: $48 \times 46$
- **Working Base**: $W = 50 \, (100 / 2, k = 2)$
- **Deviations**: $d_1 = 48 - 50 = -2, \quad d_2 = 46 - 50 = -4$
- **Cross-Add**: $C = 48 + (-4) = 44$
- **Scale (Divide by 2)**: $L = 44 / 2 = 22$
- **Right Part**: $R = (-2) \times (-4) = 8 \implies \text{"08"}$
- **Combined Answer**: $2208$
- **Direct Check**: $48 \times 46 = 2208 \checkmark$

### Canonical Example 2: Base 200 Mixed-Sign Deviations with Borrow
- **Problem**: $196 \times 204$
- **Working Base**: $W = 200 \, (100 \times 2, k = 2)$
- **Deviations**: $d_1 = 196 - 200 = -4, \quad d_2 = 204 - 200 = +4$
- **Cross-Add**: $C = 196 + 4 = 200$
- **Scale (Multiply by 2)**: $L = 200 \times 2 = 400$
- **Right Part**: $R = (-4) \times (+4) = -16$
- **Borrow Adjustment**: $400 \times 100 - 16 = 40000 - 16 = 39984$
- **Direct Check**: $196 \times 204 = 39984 \checkmark$

### Canonical Example 3: Base 50 Mixed-Sign with Borrow
- **Problem**: $56 \times 48$
- **Working Base**: $W = 50 \, (100 / 2, k = 2)$
- **Deviations**: $d_1 = +6, \quad d_2 = -2 \quad (6 \equiv -2 \pmod 2 \checkmark)$
- **Cross-Add**: $C = 56 + (-2) = 54$
- **Scale**: $L = 54 / 2 = 27$
- **Right Part**: $R = (+6) \times (-2) = -12$
- **Borrow Adjustment**: $2700 - 12 = 2688$
- **Direct Check**: $56 \times 48 = 2688 \checkmark$

---

## 6. Classification Schema

| Classification | Meaning |
|---|---|
| `VALID_BASE_50` | Valid working base 50 problem ($C$ is even, exact integer scaling). |
| `VALID_BASE_200` | Valid working base 200 problem (exact integer scaling). |
| `NON_INTEGER_SCALE_EXCLUDED` | Cross-add $C$ is odd for base 50; excluded from generation. |
| `UNSUPPORTED_BASE` | Base is neither 50 nor 200. |
| `OVERFLOW_RISK` | Numbers exceed safe integer bounds ($|N| > 10^6$). |
