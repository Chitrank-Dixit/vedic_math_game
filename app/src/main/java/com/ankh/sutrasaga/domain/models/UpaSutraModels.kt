package com.ankh.sutrasaga.domain.models

/**
 * Stable identifiers for all 13 Vedic Upa-Sutras (Sub-Sutras).
 */
enum class UpaSutraId {
    ANURUPYENA,
    SHISYATE_SHESAMAJNA,
    ADYAMADYENANTYAMANTYENA,
    KEVALAIHSAPTAKAM_GUNYAT,
    VESHTANAM,
    YAVADUNAM_TAVADUNAM,
    YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET,
    ANTYAYORDASHAKEPI,
    ANTYAYEREVA,
    SAMUCCAYAGUNITAH,
    LOPANA_STHAPANABHYAM,
    VILOKANAM,
    GUNITASAMUCCAYAH_SAMUCCAYAGUNITAH
}

/**
 * Completion states for Upa-Sutra Quests.
 */
enum class UpaSutraCompletionState {
    LOCKED,
    AVAILABLE,
    LEARNING,
    PRACTICED,
    MASTERED
}

/**
 * Pure Kotlin definition for an Upa-Sutra.
 */
data class UpaSutraDefinition(
    val id: UpaSutraId,
    val displayName: String,
    val sanskritName: String,
    val meaning: String,
    val parentWorldId: Int,
    val unlockAfterWorldId: Int,
    val verificationTier: String,
    val workedExample: String,
    val description: String
)

/**
 * User progress state for a single Upa-Sutra.
 */
data class UpaSutraProgress(
    val id: UpaSutraId,
    val state: UpaSutraCompletionState,
    val practiceCorrectCount: Int = 0,
    val challengeCorrectCount: Int = 0,
    val lastAttemptTimestamp: Long = 0L
)

/**
 * Canonical registry containing all 13 Upa-Sutra definitions.
 */
object UpaSutraRegistry {

    val entries: List<UpaSutraDefinition> = listOf(
        UpaSutraDefinition(
            id = UpaSutraId.ANURUPYENA,
            displayName = "Anurupyena",
            sanskritName = "आनुरूप्येण",
            meaning = "Proportionately",
            parentWorldId = 2,
            unlockAfterWorldId = 2,
            verificationTier = "Tier A",
            workedExample = "48 × 46 near working base 50 (100/2) = 2208",
            description = "Multiplies numbers near working sub-bases (like 50, 200, or 500) by scaling the main base deviation proportionately."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.SHISYATE_SHESAMAJNA,
            displayName = "Shisyate Shesamajna",
            sanskritName = "शिष्यते शेषसंज्ञः",
            meaning = "The remainder remains constant",
            parentWorldId = 6,
            unlockAfterWorldId = 6,
            verificationTier = "Tier B",
            workedExample = "(x³ - 3x² + 4x - 5) ÷ (x - 2) ⟹ Remainder = -1",
            description = "Calculates polynomial remainders and residue values through direct substitution without full polynomial long division."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.ADYAMADYENANTYAMANTYENA,
            displayName = "Adyamadyenantyamantyena",
            sanskritName = "आद्यमाद्येनान्त्यमन्त्येन",
            meaning = "First by first and last by last",
            parentWorldId = 10,
            unlockAfterWorldId = 10,
            verificationTier = "Tier A",
            workedExample = "2x² + 7x + 5 = (2x + 5)(x + 1)",
            description = "Decomposes quadratic coefficients by pairing factors of the leading term and the constant term."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT,
            displayName = "Kevalaihsaptakam Gunyat",
            sanskritName = "केवलैः सप्तकं गुण्यात्",
            meaning = "For seven the multiplicand is 143",
            parentWorldId = 12,
            unlockAfterWorldId = 12,
            verificationTier = "Tier B",
            workedExample = "1/7 = 0.(142857) via 143 × 7 = 1001",
            description = "Calculates repeating decimal expansions for fractions with denominator 7 using the special cyclic factor 143."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.VESHTANAM,
            displayName = "Veshtanam",
            sanskritName = "वेष्टनम्",
            meaning = "Osculation",
            parentWorldId = 12,
            unlockAfterWorldId = 12,
            verificationTier = "Tier B",
            workedExample = "247 ÷ 19 ⟹ 24 + 7(2) = 38 (Divisible by 19)",
            description = "Tests divisibility by prime numbers using single-step positive or negative osculators."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.YAVADUNAM_TAVADUNAM,
            displayName = "Yavadunam Tavadunam",
            sanskritName = "यावदूनं तावदूनम्",
            meaning = "Lessen by deficiency and setup square",
            parentWorldId = 4,
            unlockAfterWorldId = 4,
            verificationTier = "Tier B",
            workedExample = "104³ = 112 || 48 || 64 = 1124864",
            description = "Calculates cubes of numbers near powers of 10 in three mental steps."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET,
            displayName = "Yavadunam Tavadunikrtya Varganca Yojayet",
            sanskritName = "यावदूनं तावदूनीकृत्य वर्गं च योजयेत्",
            meaning = "Whatever the deficiency, lessen by that and add square",
            parentWorldId = 4,
            unlockAfterWorldId = 4,
            verificationTier = "Tier A",
            workedExample = "97² = (97 - 3) || 3² = 9409",
            description = "Mental multi-digit squaring technique using deviation reduction and deficiency squaring."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.ANTYAYORDASHAKEPI,
            displayName = "Antyayordashake'pi",
            sanskritName = "अन्त्ययोर्दशकेऽपि",
            meaning = "Last digits sum to 10 and first digits are same",
            parentWorldId = 1,
            unlockAfterWorldId = 1,
            verificationTier = "Tier A",
            workedExample = "43 × 47 = (4 × 5) || (3 × 7) = 2021",
            description = "Lightning mental multiplication for pairs sharing the same tens prefix whose unit digits sum to 10."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.ANTYAYEREVA,
            displayName = "Antyayereva",
            sanskritName = "अन्त्ययोरेव",
            meaning = "Only the last terms",
            parentWorldId = 9,
            unlockAfterWorldId = 9,
            verificationTier = "Tier B",
            workedExample = "(x + 2)/(x + 3) = (x + 4)/(x + 6) ⟹ 2/3 = 4/6 ⟹ x = 0",
            description = "Solves rational algebraic equations by inspecting whether constant term ratios match across both sides."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.SAMUCCAYAGUNITAH,
            displayName = "Samuccayagunitah",
            sanskritName = "समुच्चयगुणितः",
            meaning = "The sum of products",
            parentWorldId = 14,
            unlockAfterWorldId = 14,
            verificationTier = "Tier B",
            workedExample = "(2x + 3y)² ⟹ eval at (1,1) ⟹ 5² = 25",
            description = "Validates multivariable polynomial identities and expansions via coefficient sum evaluation."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.LOPANA_STHAPANABHYAM,
            displayName = "Lopana-Sthapanabhyam",
            sanskritName = "लोपनस्थापनाभ्याम्",
            meaning = "By elimination and retention",
            parentWorldId = 8,
            unlockAfterWorldId = 8,
            verificationTier = "Tier B",
            workedExample = "2x² + 5xy + 2y² + 4x + 5y + 2 = (2x + y + 2)(x + 2y + 1)",
            description = "Factorizes bivariate second-degree polynomials by sequentially setting each variable to zero."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.VILOKANAM,
            displayName = "Vilokanam",
            sanskritName = "विलोकिनम्",
            meaning = "By mere observation",
            parentWorldId = 9,
            unlockAfterWorldId = 9,
            verificationTier = "Tier A",
            workedExample = "x + 1/x = 2.5 ⟹ x = 2 or x = 0.5",
            description = "Solves symmetric algebraic equations and inspection puzzles purely by visual decomposition."
        ),
        UpaSutraDefinition(
            id = UpaSutraId.GUNITASAMUCCAYAH_SAMUCCAYAGUNITAH,
            displayName = "Gunitasamuccayah Samuccayagunitah",
            sanskritName = "गुणितसमुच्चयः समुच्चयगुणितः",
            meaning = "Product of sum is sum of products",
            parentWorldId = 15,
            unlockAfterWorldId = 15,
            verificationTier = "Tier B",
            workedExample = "x(y² - z²) + y(z² - x²) + z(x² - y²) = (x - y)(y - z)(z - x)",
            description = "Validates cyclic and higher-order symmetric polynomial factorizations."
        )
    )

    fun getById(id: UpaSutraId): UpaSutraDefinition {
        return entries.first { it.id == id }
    }
}
