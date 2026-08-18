# Phase U2 Summary: Upa-Sutra 1 (Anurupyena — "Proportionately")

**Document Status:** Complete & 100% Verified / Phase U2 Summary  
**Feature:** Upa-Sutra Treasury — Quest 2  
**Parent Worlds:** World 2 (Nikhilam Navatashcaramam) & World 5 (Urdhva-Tiryagbhyam)  
**Unlock Requirement:** Completion of World 2 (Nikhilam)  

---

## 1. Overview & Mathematical Scope

Phase U2 delivers **Upa-Sutra 1: Anurupyena** ("Proportionately") as the second playable quest in the Upa-Sutra Treasury. This quest teaches near-base multiplication where numbers cluster around a working base ($W$) that is related to a reference power of ten ($B = 100$) by a simple scale factor $k = 2$:
1. **Working Base 50** ($W = 100 / 2$, halving scale $C / 2$).
2. **Working Base 200** ($W = 100 \times 2$, doubling scale $C \times 2$).

---

## 2. Core Decisions & Implementations

### 1. Mathematical Specification & Policy
- **Specification Document**: [anurupyena_math_spec.md](file:///d:/game_dev/VedicMathematics/docs/upa_sutras/anurupyena_math_spec.md)
- **Non-Integer Scale Policy**: Odd cross-add results for Base 50 ($51 / 2 = 25.5$) are excluded from constructive generation and classified as `NON_INTEGER_SCALE_EXCLUDED`. All Base 50 problems constructively generate deviations with matching parity ($d_1 \equiv d_2 \pmod 2$).
- **Borrow Normalization**: Reuses standard World 2 / World 4 convention for negative right-hand values ($L - 1 \,\|\, 100 + R$). For example:
  $$196 \times 204 \implies L = 400, R = -16 \implies 40000 - 16 = 39984$$

### 2. Domain Models & Generator Engine
- **Domain Models**: [AnurupyenaModels.kt](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/domain/models/AnurupyenaModels.kt) (`AnurupyenaBaseType`, `AnurupyenaClassification`, `AnurupyenaStep`, `AnurupyenaSolution`).
- **Engine**: [AnurupyenaGenerator.kt](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/engine/upasutras/AnurupyenaGenerator.kt) implementing `UpaSutraGenerator`.
- **Tiers**:
  - Tier 1: Base 50, positive deviations, even scale division.
  - Tier 2: Base 50, mixed-sign/negative deviations.
  - Tier 3: Base 200, positive/negative deviations with borrow normalization.

### 3. Quest Shell & Treasury Integration
- **Quest UI**: [UpaSutraQuestScreen.kt](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/screens/UpaSutraQuestScreen.kt)
  - Story Beat: Guru explains the "camp halfway to 100" and proportional halving.
  - Guided Example: Step-by-step breakdown for $48 \times 46 = 2208$.
  - Guided Practice: 5 problems with step reveal.
  - Challenge: 3 timed/focused problems.
  - Reward: Badge awarding (`MASTERED` or `PRACTICED`).
- **ViewModel**: [GameViewModel.kt](file:///d:/game_dev/VedicMathematics/app/src/main/java/com/ankh/sutrasaga/ui/viewmodel/GameViewModel.kt) wired to `AnurupyenaGenerator`.

---

## 3. Verification & Test Suite

- **Unit Test Suite**: [AnurupyenaGeneratorTest.kt](file:///d:/game_dev/VedicMathematics/app/src/test/java/com/ankh/sutrasaga/engine/upasutras/AnurupyenaGeneratorTest.kt) and [GameViewModelTest.kt](file:///d:/game_dev/VedicMathematics/app/src/test/java/com/ankh/sutrasaga/ui/GameViewModelTest.kt).
- **Test Scenarios**:
  - Canonical base-50 example: $48 \times 46 = 2208$.
  - Canonical base-200 example with borrow: $196 \times 204 = 39984$.
  - Parity check: $54 \times 47 \implies$ `NON_INTEGER_SCALE_EXCLUDED`.
  - 20 hand-verified Base 50 cases.
  - 10 hand-verified Base 200 cases.
  - 50 randomized property tests per tier.
  - Full quest lifecycle test.
- **Build Status**: `.\gradlew test assembleDebug` $\implies$ **`BUILD SUCCESSFUL in 26s`** (100% Pass Rate).
