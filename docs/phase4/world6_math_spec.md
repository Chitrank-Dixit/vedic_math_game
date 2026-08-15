# Paravartya Yojayet (Transpose and Apply) Division Specification

**World 6: Paravartya Yojayet**  
**Method Scope:** Restricted division by near-base divisors slightly above powers of 10 ($10, 100$).  

---

## 1. Domain Scope & Invariants

### Supported Divisor Ranges:
- **Tier 1 (Base 10)**: Divisor $D \in \{11, 12, 13, 14, 15, 16, 17, 18, 19\}$ ($D = 10 + d, d > 0$).
  - Transposed deviation $\bar{d} = -d$.
  - Dividend $N$: 2 to 4 digits ($10 \le N \le 9999$).
- **Tier 2 (Base 100)**: Divisor $D \in \{101, 102, 103, 104, 105, 106, 107, 108, 109\}$ ($D = 100 + d, d > 0$).
  - Transposed deviation $\bar{d} = (-d_1, -d_0)$.
  - Dividend $N$: 4 to 6 digits ($1000 \le N \le 999999$).

### System Invariants:
1. $D > 0$ and $N \ge 0$.
2. Quotient $Q \ge 0$.
3. $0 \le R < D$.
4. **Fundamental Verification Identity**:
   $$N = D \times Q + R$$

---

## 2. Algorithm Step-by-Step

Given Dividend $N$ and Divisor $D = 10^k + d$:

1. **Transposed Deviation**: Calculate $d = D - 10^k \implies \bar{d} = -d$.
2. **Partitioning**: Split dividend $N$ into Quotient section (all digits except last $k$) and Remainder section (last $k$ digits).
3. **Column-by-Column Operations**:
   - Column 1: Bring down first digit $q_1 = n_1$.
   - Multiply $q_1 \times \bar{d}$ and add to Column 2.
   - Column $i$: Compute column sum $s_i = n_i + \text{incoming adjustments}$.
   - If $s_i < 0$ in quotient columns: Vinculum / borrow conversion is applied ($10 \times \text{prev} + s_i$).
   - Pass $s_i \times \bar{d}$ to subsequent column(s).
4. **Remainder Normalization**:
   - If raw remainder $R < 0$:
     - Decrement quotient $Q \gets Q - 1$.
     - Add divisor $R \gets R + D$.
     - Repeat until $0 \le R < D$.
   - If raw remainder $R \ge D$:
     - Increment quotient $Q \gets Q + (R / D)$.
     - Reduce remainder $R \gets R \pmod D$.

---

## 3. Seed Examples & Derivations

### Example 1: $1225 \div 12 = 102 \text{ remainder } 1$
- Divisor $D = 12$, Base $B = 10$, Transposed deviation $\bar{d} = -2$.
- Partition: `1 2 2 | 5`
- Column 1: `1` $\rightarrow$ multiply $1 \times (-2) = -2$.
- Column 2: $2 + (-2) = 0 \rightarrow$ multiply $0 \times (-2) = 0$.
- Column 3: $2 + 0 = 2 \rightarrow$ multiply $2 \times (-2) = -4$.
- Column 4 (Remainder): $5 + (-4) = 1$.
- **Result**: Quotient $= 102$, Remainder $= 1$.
- **Identity Check**: $12 \times 102 + 1 = 1224 + 1 = 1225$. $\checkmark$

### Example 2: $432 \div 11 = 39 \text{ remainder } 3$
- Divisor $D = 11$, Base $B = 10$, Transposed deviation $\bar{d} = -1$.
- Partition: `4 3 | 2`
- Column 1: `4` $\rightarrow$ multiply $4 \times (-1) = -4$.
- Column 2: $3 + (-4) = -1$.
- Column 3 (Remainder): $2 + ((-1) \times (-1)) = 2 + 1 = 3$.
- Raw Quotient: $4\bar{1} = 40 - 1 = 39$.
- **Result**: Quotient $= 39$, Remainder $= 3$.
- **Identity Check**: $11 \times 39 + 3 = 429 + 3 = 432$. $\checkmark$

### Example 3: $97 \div 12 = 8 \text{ remainder } 1$
- Divisor $D = 12$, Base $B = 10$, Transposed deviation $\bar{d} = -2$.
- Partition: `9 | 7`
- Column 1: `9` $\rightarrow$ multiply $9 \times (-2) = -18$.
- Column 2 (Remainder): $7 + (-18) = -11$.
- Raw Quotient $= 9$, Raw Remainder $= -11$.
- Normalization: $Q = 9 - 1 = 8$, $R = -11 + 12 = 1$.
- **Result**: Quotient $= 8$, Remainder $= 1$.
- **Identity Check**: $12 \times 8 + 1 = 96 + 1 = 97$. $\checkmark$
