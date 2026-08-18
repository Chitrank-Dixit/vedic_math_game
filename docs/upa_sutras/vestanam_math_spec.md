# Mathematical Specification: Upa-Sutra 5 — Vestanam ("By Osculation")

**Document Status:** Complete Mathematical Specification & Verification / Phase U3  
**Feature:** Upa-Sutra Treasury — Quest 5 (Tier B)  
**Target Class:** `VestanamGenerator`  
**Parent World:** World 12 (Shesanyankena Charamena) & World 5 (Number Theory)  
**Unlock Requirement:** Completion of World 12 (Shesanyankena Charamena)  

---

## 1. Mathematical Foundation of Osculation

*Vestanam* ("by osculation") is an iterative modular reduction technique used to determine whether a large integer $N$ is divisible by a prime divisor $d$. 

Let $N = 10a + b$, where $b = N \pmod{10}$ is the last digit and $a = \lfloor N / 10 \rfloor$ is the truncated rest.

### General Theory
We seek an integer constant $m$ (the **osculator**) such that:
$$10m \equiv \pm 1 \pmod d$$

1. **Positive Osculator ($+m$, Operation: Addition)**:
   $$10m \equiv 1 \pmod d \implies 10m - 1 = k \cdot d$$
   Define the transformation:
   $$T_+(N) = a + m \cdot b$$
   Multiplying by 10:
   $$10 \cdot T_+(N) = 10a + 10mb = (10a + b) + (10m - 1)b = N + (kd)b \equiv N \pmod d$$
   Since $\gcd(10, d) = 1$, we have:
   $$T_+(N) \equiv 0 \pmod d \iff N \equiv 0 \pmod d$$

2. **Negative Osculator ($-m$, Operation: Subtraction)**:
   $$10m \equiv -1 \pmod d \implies 10m + 1 = k \cdot d$$
   Define the transformation:
   $$T_-(N) = a - m \cdot b$$
   Multiplying by 10:
   $$10 \cdot T_-(N) = 10a - 10mb = (10a + b) - (10m + 1)b = N - (kd)b \equiv N \pmod d$$
   Since $\gcd(10, d) = 1$:
   $$T_-(N) \equiv 0 \pmod d \iff N \equiv 0 \pmod d$$

---

## 2. Pre-Verified Supported Divisors in First Release

The first release strictly restricts osculation to three pre-verified prime divisors:

| Divisor ($d$) | Osculator ($m$) | Sign / Operation | Mathematical Justification |
|---|---|---|---|
| **19** | **2** | Positive ($+$ Add) | $19 \times 1 = 19 = 20 - 1 \implies 10(2) \equiv 1 \pmod{19}$. |
| **13** | **4** | Positive ($+$ Add) | $13 \times 3 = 39 = 40 - 1 \implies 10(4) \equiv 1 \pmod{13}$. |
| **7** | **2** | Negative ($-$ Subtract) | $7 \times 3 = 21 = 20 + 1 \implies 10(2) \equiv -1 \pmod 7$. |

> [!WARNING]
> **Divisibility vs. True Remainder in Negative Osculation**:
> For positive osculators, $T_+(N)$ maintains a proportional modular link. However, for negative osculators ($d = 7$), $T_-(N)$ strictly preserves the zero-congruence ($T_-(N) \equiv 0 \pmod 7 \iff N \equiv 0 \pmod 7$). When $N$ is not divisible by 7, the final reduced value does **not** generally equal the true remainder $N \pmod 7$. (For example, $2223 \to 9$, $9 \not\equiv 0 \pmod 7$, but $2223 \pmod 7 = 4$). This distinction is mathematically exact and respected throughout our engine and tests.

---

## 3. Termination Condition

The osculation process repeats:
$$N_{k+1} = \text{rest}(N_k) \pm m \cdot \text{lastDigit}(N_k)$$
until $|N_{k+1}| < 100$ or $|N_{k+1}| \le 2d$. At this point, the value is small enough to test directly by basic multiplication tables.

---

## 4. Required Verified Examples

### Example 1: Divisor 19, Osculator +2 (Single Step)
- **Number**: $247$
- **Rest**: $24$, **Last Digit**: $7$
- **Osculation**: $24 + 2(7) = 24 + 14 = 38$
- **Evaluation**: $38 = 19 \times 2 \implies 38 \equiv 0 \pmod{19}$
- **Conclusion**: $247$ is divisible by $19$ ($247 / 19 = 13 \checkmark$).

### Example 2: Divisor 13, Osculator +4 (Single Step)
- **Number**: $143$
- **Rest**: $14$, **Last Digit**: $3$
- **Osculation**: $14 + 4(3) = 14 + 12 = 26$
- **Evaluation**: $26 = 13 \times 2 \implies 26 \equiv 0 \pmod{13}$
- **Conclusion**: $143$ is divisible by $13$ ($143 / 13 = 11 \checkmark$).

### Example 3: Divisor 7, Osculator -2 (Single Step)
- **Number**: $133$
- **Rest**: $13$, **Last Digit**: $3$
- **Osculation**: $13 - 2(3) = 13 - 6 = 7$
- **Evaluation**: $7 = 7 \times 1 \implies 7 \equiv 0 \pmod 7$
- **Conclusion**: $133$ is divisible by $7$ ($133 / 7 = 19 \checkmark$).

### Example 4: Divisor 19, Multi-Step ($N = 2223$)
- **Step 1**: $222 + 2(3) = 228$
- **Step 2**: $22 + 2(8) = 22 + 16 = 38$
- **Evaluation**: $38 = 19 \times 2 \implies 38 \equiv 0 \pmod{19}$
- **Conclusion**: $2223$ is divisible by $19$ ($2223 / 19 = 117 \checkmark$).

### Example 5: Divisor 7, Non-Divisible ($N = 2223$)
- **Step 1**: $222 - 2(3) = 216$
- **Step 2**: $21 - 2(6) = 21 - 12 = 9$
- **Evaluation**: $9$ is not divisible by $7$ ($9 \not\equiv 0 \pmod 7$)
- **Conclusion**: $2223$ is NOT divisible by $7$ ($2223 \pmod 7 = 4 \neq 0 \checkmark$).

---

## 5. Classification Schema

| Classification | Meaning |
|---|---|
| `DIVISIBLE` | Final reduced value is an integer multiple of $d$ ($N \pmod d == 0$). |
| `NOT_DIVISIBLE` | Final reduced value is not a multiple of $d$ ($N \pmod d \neq 0$). |
| `UNSUPPORTED_DIVISOR` | Divisor is not in $\{7, 13, 19\}$. |
| `OVERFLOW_RISK` | $N > 10^7$. |
