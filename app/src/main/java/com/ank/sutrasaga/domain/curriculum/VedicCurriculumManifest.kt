package com.ank.sutrasaga.domain.curriculum

/**
 * Category classification for Vedic Mathematics curriculum items.
 */
enum class CurriculumCategory {
    PRINCIPAL_SUTRA,
    UPA_SUTRA,
    APPLICATION_LESSON
}

/**
 * Runtime delivery channel through which learners access the content.
 */
enum class RuntimeDeliveryChannel {
    MAIN_CAMPAIGN_WORLD,
    PLAYABLE_TREASURY_QUEST,
    CODEX_REFERENCE
}

/**
 * Explicit verification state flags for a curriculum unit.
 *
 * Distinguishes existence, runtime reachability, mathematical correctness,
 * tutorial-to-quiz alignment, and pedagogical review status.
 */
data class VerificationFlags(
    val contentExists: Boolean,
    val isReachableAtRuntime: Boolean,
    val mathematicsVerified: Boolean,
    val tutorialQuizAligned: Boolean,
    val isReviewedAndComplete: Boolean
)

/**
 * Explicit, testable curriculum entry representing an implemented or cataloged lesson.
 */
data class CurriculumEntry(
    val lessonId: String,
    val worldNumber: Int?,
    val displayName: String,
    val canonicalSanskritName: String,
    val devanagariSpelling: String,
    val traditionalIndex: Int,
    val category: CurriculumCategory,
    val deliveryChannel: RuntimeDeliveryChannel,
    val learningObjective: String,
    val tutorialRuntimeSource: String,
    val quizRuntimeSource: String,
    val verification: VerificationFlags,
    val discrepancyNotes: String? = null
)

/**
 * Machine-readable curriculum manifest formalizing the Vedic Mathematics curriculum
 * across the 16 Principal Sutras and 13 Upa-Sutras.
 *
 * Canonical Reference:
 * Swami Bharati Krishna Tirtha, "Vedic Mathematics: Sixteen Simple Mathematical
 * Formulae from the Vedas", Motilal Banarsidass Publishers, Delhi (1965).
 */
object VedicCurriculumManifest {

    const val REFERENCE_AUTHOR = "Swami Bharati Krishna Tirtha"
    const val REFERENCE_WORK = "Vedic Mathematics"
    const val REFERENCE_PUBLISHER = "Motilal Banarsidass (1965)"

    val entries: List<CurriculumEntry> = listOf(
        // =========================================================================
        // 16 MAIN CAMPAIGN WORLDS (Worlds 1 - 16)
        // =========================================================================

        // World 1: Ekadhikena Purvena (BKT Principal Sutra #1)
        CurriculumEntry(
            lessonId = "ekadhikena",
            worldNumber = 1,
            displayName = "By One More than the Previous One",
            canonicalSanskritName = "Ekādhikena Pūrveṇa",
            devanagariSpelling = "एकाधिकेन पूर्वेण",
            traditionalIndex = 1,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Mental squaring of numbers ending in 5 via prefix multiplication by its successor (e.g. 35² = (3×4)||25 = 1225).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 1, 'एकाधिकेन पूर्वेण') & rive_sutra_dialogues.json",
            quizRuntimeSource = "EkadhikenaPurvenaGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 2: Nikhilam Navatashcaramam Dashatah (BKT Principal Sutra #2)
        CurriculumEntry(
            lessonId = "nikhilam",
            worldNumber = 2,
            displayName = "All from 9 and the Last from 10",
            canonicalSanskritName = "Nikhilam Navataścaramaṁ Daśataḥ",
            devanagariSpelling = "निखिलं नवतश्चरमं दशतः",
            traditionalIndex = 2,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Rapid subtraction from decimal powers of 10 without borrowing (e.g. 1000 - 364 = 636).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 2, 'निखिलं नवतश्चरमं दशतः') & rive_sutra_dialogues.json",
            quizRuntimeSource = "NikhilamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 3: Ekanyunena Purvena (BKT Principal Sutra #14)
        CurriculumEntry(
            lessonId = "ekanyunena",
            worldNumber = 3,
            displayName = "By One Less than the Previous One",
            canonicalSanskritName = "Ekanyūnena Pūrveṇa",
            devanagariSpelling = "एकन्यूनेन पूर्वेण",
            traditionalIndex = 14,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Multiplication by strings of nines using predecessor reduction and complement subtraction (e.g. 47 × 99 = 4653).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 3, 'एकन्यूनेन पूर्वेण') & rive_sutra_dialogues.json",
            quizRuntimeSource = "EkanyunenaGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 4: Yavadunam (BKT Principal Sutra #10)
        CurriculumEntry(
            lessonId = "yavadunam",
            worldNumber = 4,
            displayName = "Lessen by Deficiency",
            canonicalSanskritName = "Yāvadūnam",
            devanagariSpelling = "यावदूनम्",
            traditionalIndex = 10,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Squaring numbers near decimal bases via deficiency reduction and deficiency squaring (e.g. 96² = (96-4)||4² = 9216).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 4, 'यावदूनम्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "YavadunamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 5: Urdhva Tiryagbhyam (BKT Principal Sutra #3)
        CurriculumEntry(
            lessonId = "urdhva",
            worldNumber = 5,
            displayName = "Vertically and Crosswise",
            canonicalSanskritName = "Ūrdhva-Tiryagbhyām",
            devanagariSpelling = "ऊर्ध्व तिर्यग्भ्याम्",
            traditionalIndex = 3,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "General cross-multiplication for 2-digit numbers using vertical and diagonal digit products (e.g. 23 × 14 = 322).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 5, 'ऊर्ध्व तिर्यग्भ्याम्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "UrdhvaTiryagbhyamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 6: Paravartya Yojayet (BKT Principal Sutra #4)
        CurriculumEntry(
            lessonId = "paravartya",
            worldNumber = 6,
            displayName = "Transpose and Apply",
            canonicalSanskritName = "Parāvartya Yojayet",
            devanagariSpelling = "परावर्त्य योजयेत्",
            traditionalIndex = 4,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Synthetic division of polynomials and numbers near base by sign-inverting the divisor constant.",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 6, 'परावर्त्य योजयेत्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "ParavartyaYojayetGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 7: Anurupyena / Anurupye Shunyamanyat (BKT Principal Sutra #6 vs Upa-Sutra #1 Divergence)
        CurriculumEntry(
            lessonId = "anurupyena_square",
            worldNumber = 7,
            displayName = "Proportionately (Working Sub-Base Squaring & Ratios)",
            canonicalSanskritName = "Ānurūpye Śūnyamanyat",
            devanagariSpelling = "आनुरूप्येण शून्यमन्यत्",
            traditionalIndex = 6,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Solving linear systems where coefficient ratios match constant ratios (Quiz), or sub-base arithmetic scaling near 50 (Gurukul Tutorial).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 7, sub-base squaring 48² near 50)",
            quizRuntimeSource = "AnurupyeShunyamanyatGenerator (linear equations: 3x+2y=12, 6x+5y=24 -> y=0, x=4)",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = false,
                isReviewedAndComplete = false
            ),
            discrepancyNotes = "CRITICAL CURRICULUM MISCLASSIFICATION: GurukulScriptsRepository and SutraLessonsRepository teach Upa-Sutra #1 (Anurupyena sub-base squaring: 48² = 2304 near working base 50) under category 'Squaring & Cubing', whereas AnurupyeShunyamanyatGenerator and rive_sutra_dialogues.json implement Principal Sutra #6 (Ānurūpye Śūnyamanyat: simultaneous linear systems where matching coefficient ratio zeroes the other variable). Upa-Sutra #1 is also independently implemented as a playable Treasury quest."
        ),

        // World 8: Sankalana-Vyavakalanabhyam (BKT Principal Sutra #7)
        CurriculumEntry(
            lessonId = "sankalana",
            worldNumber = 8,
            displayName = "By Addition and by Subtraction",
            canonicalSanskritName = "Saṅkalana-Vyavakalanābhyām",
            devanagariSpelling = "संकलनव्यवकलनाभ्याम्",
            traditionalIndex = 7,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Solving simultaneous linear equations by summing and subtracting equation pairs (e.g. 3x+2y=12, 2x+3y=13).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 8, 'संकलनव्यवकलनाभ्याम्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "SankalanaVyavakalanabhyamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 9: Shunyam Samyasamuccaye (BKT Principal Sutra #5)
        CurriculumEntry(
            lessonId = "shunyam",
            worldNumber = 9,
            displayName = "When the Collection is Equal, it is Zero",
            canonicalSanskritName = "Śūnyam Sāmyasamuccaye",
            devanagariSpelling = "शून्यं साम्यसमुच्चये",
            traditionalIndex = 5,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Zeroing linear expressions when sum of coefficients or common terms equate to zero across sides.",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 9, 'शून्यं साम्यसमुच्चये') & rive_sutra_dialogues.json",
            quizRuntimeSource = "ShunyamSamyasamuccayeGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 10: Puranapuranabhyam (BKT Principal Sutra #8)
        CurriculumEntry(
            lessonId = "puranapuranabhyam",
            worldNumber = 10,
            displayName = "By the Completion or Non-Completion",
            canonicalSanskritName = "Pūraṇāpūraṇābhyām",
            devanagariSpelling = "पूरणापूरणाभ्याम्",
            traditionalIndex = 8,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Solving quadratic equations by completing the square (e.g. x² + 6x = 16 ⟹ (x+3)² = 25).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 10, 'पूरणापूरणाभ्याम्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "PuranapuranabhyamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 11: Vyashtisamashtih (BKT Principal Sutra #11)
        CurriculumEntry(
            lessonId = "vyashtisamashtih",
            worldNumber = 11,
            displayName = "Specific and General",
            canonicalSanskritName = "Vyaṣṭisamaṣṭiḥ",
            devanagariSpelling = "व्यष्टिसमष्टिः",
            traditionalIndex = 11,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Decomposing problems into individual parts and totality (Cubic factorization in Gurukul; symmetric average multiplication in Quiz: (60-2)(60+2)=3596).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 11, 'व्यष्टिसमष्टिः') & rive_sutra_dialogues.json",
            quizRuntimeSource = "VyashtisamashtihGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Pedagogical focus variance: Gurukul tutorial teaches cubic factorization by roots, while quiz engine tests symmetric average-deviation multiplication (a-b)(a+b) = a² - b²."
        ),

        // World 12: Shesanyankena Charamena (BKT Principal Sutra #12)
        CurriculumEntry(
            lessonId = "shesanyankena",
            worldNumber = 12,
            displayName = "The Remainders by the Last Digit",
            canonicalSanskritName = "Śeṣāṇyaṅkena Carameṇa",
            devanagariSpelling = "शेषाण्यङ्केन चरमेण",
            traditionalIndex = 12,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Generating recurring decimal expansions via remainder sequences (1/7 = 0.(142857) in quiz/rive; 1/19 in Gurukul).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 12, 'शेषाण्यङ्केन चरमेण') & rive_sutra_dialogues.json",
            quizRuntimeSource = "ShesanyankenaCharamenaGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Gurukul tutorial demonstrates 1/19 decimal expansion, while quiz generator and rive_sutra_dialogues.json test 1/7 cyclic remainders."
        ),

        // World 13: Sopantyadvayamantyam (BKT Principal Sutra #13)
        CurriculumEntry(
            lessonId = "sopantyadvayamantyam",
            worldNumber = 13,
            displayName = "The Ultimate and Twice the Penultimate",
            canonicalSanskritName = "Sopāntyadvayamantyam",
            devanagariSpelling = "सोपान्त्यद्वयमन्त्यम्",
            traditionalIndex = 13,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Algebraic solution of symmetric rational equations (Gurukul) or mental multiplication by 12/13 via doubled penultimate digit (Quiz).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 13, 'सोपान्त्यद्वयमन्त्यम्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "SopantyadvayamantyamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Dual application: Gurukul tutorial teaches rational equations (1/(x+2)+1/(x+3)=1/(x+1)+1/(x+4)), while quiz generator and rive dialogue test multiplication by 12 (143×12=1716)."
        ),

        // World 14: Gunitasamuccayah (BKT Principal Sutra #15)
        CurriculumEntry(
            lessonId = "gunitasamuccayah",
            worldNumber = 14,
            displayName = "Product of the Sum is the Sum of the Products",
            canonicalSanskritName = "Guṇitasamuccayaḥ",
            devanagariSpelling = "गुणितसमुच्चयः",
            traditionalIndex = 15,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Auditing algebraic factorizations by substituting x=1 and equating the product of sums to the sum of coefficients.",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 14, 'गुणितसमुच्चयः') & rive_sutra_dialogues.json",
            quizRuntimeSource = "GunitasamuccayahGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 15: Gunakasamuccayah (BKT Principal Sutra #16)
        CurriculumEntry(
            lessonId = "gunakasamuccayah",
            worldNumber = 15,
            displayName = "The Factor of the Sum is the Sum of the Factors",
            canonicalSanskritName = "Guṇakasamuccayaḥ",
            devanagariSpelling = "गुणकसमुच्चयः",
            traditionalIndex = 16,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Factoring quadratic polynomials and verifying that factor root sets match polynomial coefficients.",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 15, 'गुणकसमुच्चयः') & rive_sutra_dialogues.json",
            quizRuntimeSource = "GunakasamuccayahGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // World 16: Chalana-Kalanabhyam (BKT Principal Sutra #9)
        CurriculumEntry(
            lessonId = "chalana",
            worldNumber = 16,
            displayName = "Differential Calculus & Residue Operations",
            canonicalSanskritName = "Calana-Kalanābhyām",
            devanagariSpelling = "चलनकलनाभ्याम्",
            traditionalIndex = 9,
            category = CurriculumCategory.PRINCIPAL_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD,
            learningObjective = "Finding critical turning points and quadratic roots via the first derivative (d/dx = 0 or d/dx = ±√Discriminant).",
            tutorialRuntimeSource = "GurukulScriptsRepository (worldId = 16, 'चलनकलनाभ्याम्') & rive_sutra_dialogues.json",
            quizRuntimeSource = "ChalanaKalanabhyamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            )
        ),

        // =========================================================================
        // 13 CANONICAL UPA-SUTRAS (Sub-Sutras)
        // =========================================================================

        // Upa-Sutra 1: Anurupyena (BKT Upa-Sutra #1)
        CurriculumEntry(
            lessonId = "upa_anurupyena",
            worldNumber = null,
            displayName = "Anurupyena",
            canonicalSanskritName = "Ānurūpyeṇa",
            devanagariSpelling = "आनुरूप्येण",
            traditionalIndex = 1,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Sub-base arithmetic: multiplying numbers near working bases 50, 200, or 500 by scaling base deviations proportionately (48×46 near 50 = 2208).",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 1) & docs/upa_sutras/05_anurupyena_summary.md",
            quizRuntimeSource = "AnurupyenaGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 2. Also duplicated in main campaign World 7 tutorial."
        ),

        // Upa-Sutra 2: Shisyate Shesamajna (BKT Upa-Sutra #2)
        CurriculumEntry(
            lessonId = "upa_shisyate_shesamajna",
            worldNumber = null,
            displayName = "Shisyate Shesamajna",
            canonicalSanskritName = "Śiṣyate Śeṣasaṁjñaḥ",
            devanagariSpelling = "शिष्यते शेषसंज्ञः",
            traditionalIndex = 2,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Evaluating polynomial remainders P(a) for linear divisors (x-a) via direct remainder substitution without long division.",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 2) & docs/upa_sutras/08_sisyate_sesasamjnah_summary.md",
            quizRuntimeSource = "SisyateSesasamjnahGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 6 (Paravartya)."
        ),

        // Upa-Sutra 3: Adyamadyenantyamantyena (BKT Upa-Sutra #3)
        CurriculumEntry(
            lessonId = "upa_adyamadyenantyamantyena",
            worldNumber = null,
            displayName = "Adyamadyenantyamantyena",
            canonicalSanskritName = "Ādyamādyenāntyamantyena",
            devanagariSpelling = "आद्यमाद्येनान्त्यमन्त्येन",
            traditionalIndex = 3,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Decomposing non-monic quadratics Ax²+Bx+C into linear factors pairing factors of leading coefficient A and constant term C.",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 3) & docs/upa_sutras/07_adyamadyenantyamantyena_summary.md",
            quizRuntimeSource = "AdyamadyenantyamantyenaGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 10 (Puranapuranabhyam)."
        ),

        // Upa-Sutra 4: Kevalaihsaptakam Gunyat (BKT Upa-Sutra #4)
        CurriculumEntry(
            lessonId = "upa_kevalaihsaptakam_gunyat",
            worldNumber = null,
            displayName = "Kevalaihsaptakam Gunyat",
            canonicalSanskritName = "Kevalaiḥ Saptakaṁ Guṇyāt",
            devanagariSpelling = "केवलैः सप्तकं गुण्यात्",
            traditionalIndex = 4,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Computing cyclic repeating decimal repetends for fractions with denominator 7 using the cyclic multiplier 143 (143×7=1001).",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 4) & docs/upa_sutras/10_kevalaih_saptakam_gunyat_summary.md",
            quizRuntimeSource = "KevalaihSaptakamGunyatGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 12 (Shesanyankena)."
        ),

        // Upa-Sutra 5: Veshtanam (BKT Upa-Sutra #5)
        CurriculumEntry(
            lessonId = "upa_veshtanam",
            worldNumber = null,
            displayName = "Veshtanam",
            canonicalSanskritName = "Veṣṭanam",
            devanagariSpelling = "वेष्टनम्",
            traditionalIndex = 5,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Single-step prime divisibility testing using positive and negative osculators for divisors ending in 1, 3, 7, 9.",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 5) & docs/upa_sutras/09_vestanam_summary.md",
            quizRuntimeSource = "VestanamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 12 (Shesanyankena)."
        ),

        // Upa-Sutra 6: Yavadunam Tavadunam (BKT Upa-Sutra #6)
        CurriculumEntry(
            lessonId = "upa_yavadunam_tavadunam",
            worldNumber = null,
            displayName = "Yavadunam Tavadunam",
            canonicalSanskritName = "Yāvadūnaṁ Tāvadūnam",
            devanagariSpelling = "यावदूनं तावदूनम्",
            traditionalIndex = 6,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.CODEX_REFERENCE,
            learningObjective = "Near-base 3-step mental cubing (B ± d)³ = B²(B ± 3d) + B(3d²) ± d³.",
            tutorialRuntimeSource = "UpaSutraRegistry (Codex Entry) & docs/upa_sutras/00_canonical_registry.md",
            quizRuntimeSource = "None (Codex Reference Card)",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Codex-only reference due to high 3-part mental load; host World 4."
        ),

        // Upa-Sutra 7: Yavadunam Tavadunikrtya Vargancha Yojayet (BKT Upa-Sutra #7)
        CurriculumEntry(
            lessonId = "upa_yavadunam_remix",
            worldNumber = null,
            displayName = "Yavadunam Tavadunikrtya Varganca Yojayet",
            canonicalSanskritName = "Yāvadūnaṁ Tāvadūnīkṛtya Vargañca Yojayet",
            devanagariSpelling = "यावदूनीकृत्य वर्गं च योजयेत्",
            traditionalIndex = 7,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Advanced multi-digit squaring with carry propagation near bases 100 and 1000 (e.g. 88² = (88-12)||12² = 7744).",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 7) & docs/upa_sutras/06_yavadunam_remix_summary.md",
            quizRuntimeSource = "YavadunamRemixGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 4 (Yavadunam)."
        ),

        // Upa-Sutra 8: Antyayordashakepi (BKT Upa-Sutra #8)
        CurriculumEntry(
            lessonId = "upa_antyayordashakepi",
            worldNumber = null,
            displayName = "Antyayordashake'pi",
            canonicalSanskritName = "Antyayordaśake'pi",
            devanagariSpelling = "अन्त्ययोर्दशकेऽपि",
            traditionalIndex = 8,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Multiplication of two-digit numbers sharing identical tens digits whose units sum to 10 (e.g. 43 × 47 = 2021).",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 8) & docs/upa_sutras/04_u1_summary.md",
            quizRuntimeSource = "AntyayordashakepiGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 1 (Ekadhikena)."
        ),

        // Upa-Sutra 9: Antyayereva (BKT Upa-Sutra #9)
        CurriculumEntry(
            lessonId = "upa_antyayereva",
            worldNumber = null,
            displayName = "Antyayereva",
            canonicalSanskritName = "Antyayoreva",
            devanagariSpelling = "अन्त्ययोरेव",
            traditionalIndex = 9,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.CODEX_REFERENCE,
            learningObjective = "Solving rational equations (x+a)/(x+b) = (x+c)/(x+d) where constant ratios match (a/b = c/d ⟹ x = 0).",
            tutorialRuntimeSource = "UpaSutraRegistry (Codex Entry) & docs/upa_sutras/antyayoreva_resolution.md",
            quizRuntimeSource = "None (Codex Reference Card)",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Codex-only reference to prevent degenerate gameplay (answer is constantly x = 0); host World 9 / 13."
        ),

        // Upa-Sutra 10: Samuccayagunitah (BKT Upa-Sutra #10)
        CurriculumEntry(
            lessonId = "upa_samuccayagunitah",
            worldNumber = null,
            displayName = "Samuccayagunitah",
            canonicalSanskritName = "Samuccayaguṇitaḥ",
            devanagariSpelling = "समुच्चयगुणितः",
            traditionalIndex = 10,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.CODEX_REFERENCE,
            learningObjective = "Evaluating multivariable polynomial expansions via coefficient sum evaluation at (1,1).",
            tutorialRuntimeSource = "UpaSutraRegistry (Codex Entry) & docs/upa_sutras/samuccayagunitah_resolution.md",
            quizRuntimeSource = "None (Codex Reference Card)",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Codex-only reference: linguistic RHS clause of World 14 (Guṇitasamuccayaḥ); host World 14."
        ),

        // Upa-Sutra 11: Lopana-Sthapanabhyam (BKT Upa-Sutra #11)
        CurriculumEntry(
            lessonId = "upa_lopana_sthapanabhyam",
            worldNumber = null,
            displayName = "Lopana-Sthapanabhyam",
            canonicalSanskritName = "Lopana-Sthāpanābhyām",
            devanagariSpelling = "लोपनस्थापनाभ्याम्",
            traditionalIndex = 11,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Factorizing bivariate second-degree polynomials by sequentially setting each variable to zero and cross-verifying terms.",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 11) & docs/upa_sutras/12_lopanasthapanabhyam_summary.md",
            quizRuntimeSource = "LopanasthapanabhyamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 8 / World 11."
        ),

        // Upa-Sutra 12: Vilokanam (BKT Upa-Sutra #12)
        CurriculumEntry(
            lessonId = "upa_vilokanam",
            worldNumber = null,
            displayName = "Vilokanam",
            canonicalSanskritName = "Vilokanam",
            devanagariSpelling = "विलोकिनम्",
            traditionalIndex = 12,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST,
            learningObjective = "Solving symmetric equations and algebraic inspection puzzles purely by visual pattern decomposition (e.g. x + 1/x = 2.5 ⟹ x = 2, 0.5).",
            tutorialRuntimeSource = "UpaSutraRegistry (Quest 12) & docs/upa_sutras/13_vilokanam_summary.md",
            quizRuntimeSource = "VilokanamGenerator",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Host World 9 (Shunyam)."
        ),

        // Upa-Sutra 13: Gunitasamuccayah Samuccayagunitah (BKT Upa-Sutra #13)
        CurriculumEntry(
            lessonId = "upa_gunitasamuccayah_samuccayagunitah",
            worldNumber = null,
            displayName = "Gunitasamuccayah Samuccayagunitah",
            canonicalSanskritName = "Guṇitasamuccayaḥ Samuccayaguṇitaḥ",
            devanagariSpelling = "गुणितसमुच्चयः समुच्चयगुणितः",
            traditionalIndex = 13,
            category = CurriculumCategory.UPA_SUTRA,
            deliveryChannel = RuntimeDeliveryChannel.CODEX_REFERENCE,
            learningObjective = "Validating cyclic and higher-order symmetric polynomial factorizations via compound homomorphism.",
            tutorialRuntimeSource = "UpaSutraRegistry (Codex Entry) & docs/upa_sutras/gunitasamuccayah_samuccayagunitah_resolution.md",
            quizRuntimeSource = "None (Codex Reference Card)",
            verification = VerificationFlags(
                contentExists = true,
                isReachableAtRuntime = true,
                mathematicsVerified = true,
                tutorialQuizAligned = true,
                isReviewedAndComplete = true
            ),
            discrepancyNotes = "Codex-only reference: compound parent sentence of World 14; host World 14 / World 15."
        )
    )

    fun getMainCampaignWorlds(): List<CurriculumEntry> =
        entries.filter { it.deliveryChannel == RuntimeDeliveryChannel.MAIN_CAMPAIGN_WORLD }

    fun getPlayableQuests(): List<CurriculumEntry> =
        entries.filter { it.deliveryChannel == RuntimeDeliveryChannel.PLAYABLE_TREASURY_QUEST }

    fun getCodexReferences(): List<CurriculumEntry> =
        entries.filter { it.deliveryChannel == RuntimeDeliveryChannel.CODEX_REFERENCE }

    fun getPrincipalSutras(): List<CurriculumEntry> =
        entries.filter { it.category == CurriculumCategory.PRINCIPAL_SUTRA }

    fun getUpaSutras(): List<CurriculumEntry> =
        entries.filter { it.category == CurriculumCategory.UPA_SUTRA }

    fun getEntryByLessonId(id: String): CurriculumEntry? =
        entries.firstOrNull { it.lessonId == id }

    fun getEntryByWorldNumber(worldNumber: Int): CurriculumEntry? =
        entries.firstOrNull { it.worldNumber == worldNumber }

    fun getMisclassifiedOrDivergentEntries(): List<CurriculumEntry> =
        entries.filter { !it.verification.tutorialQuizAligned || it.discrepancyNotes != null }
}
