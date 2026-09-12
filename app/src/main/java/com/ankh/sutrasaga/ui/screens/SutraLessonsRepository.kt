package com.ankh.sutrasaga.ui.screens

import com.ankh.sutrasaga.domain.models.DecompositionStep

/**
 * Repository of dedicated, authentic interactive lessons for all 16 Primary Vedic Sutras.
 */
object SutraLessonsRepository {

    val allLessons: Map<String, SutraLesson> = listOf(
        // World 1: Ekadhikena Purvena
        SutraLesson(
            id = "ekadhikena",
            sanskritTitle = "एकाधिकेन पूर्वेण",
            englishTitle = "By One More than the Previous One",
            shloka = "एकाधिकेन पूर्वेण।",
            literalMeaning = "By one more than the previous one.",
            shortcutRule = "For squaring numbers ending in 5: Multiply prefix 'a' by (a + 1), then append 25 (5²).",
            primaryEquation = "65²",
            targetAnswer = "4225",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Left Part: Prefix × (Prefix + 1)",
                    formulaDisplay = "6 × (6 + 1)",
                    stepResult = "42",
                    explanation = "Multiply the tens digit 6 by consecutive integer 7"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Right Part: 5²",
                    formulaDisplay = "5 × 5",
                    stepResult = "25",
                    explanation = "Square the units digit 5"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Combine Both Parts",
                    formulaDisplay = "42 | 25",
                    stepResult = "4225",
                    explanation = "Merge left and right parts to form the final product"
                )
            ),
            carries = emptyList()
        ),

        // World 2: Nikhilam Navatashcaramam Dashatah
        SutraLesson(
            id = "nikhilam",
            sanskritTitle = "निखिलं नवतश्चरमं दशतः",
            englishTitle = "All from 9 and Last from 10",
            shloka = "निखिलं नवतश्चरमं दशतः।",
            literalMeaning = "All from nine and the last from ten.",
            shortcutRule = "For base subtraction (1000 - N): Subtract all leading digits from 9 and the last non-zero digit from 10.",
            primaryEquation = "1000 − 364",
            targetAnswer = "636",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Hundreds: Subtract from 9",
                    formulaDisplay = "9 − 3",
                    stepResult = "6",
                    explanation = "Subtract the first digit 3 from 9"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Tens: Subtract from 9",
                    formulaDisplay = "9 − 6",
                    stepResult = "3",
                    explanation = "Subtract the middle digit 6 from 9"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Units: Subtract from 10",
                    formulaDisplay = "10 − 4",
                    stepResult = "6",
                    explanation = "Subtract the last digit 4 from 10 → 636"
                )
            ),
            carries = emptyList()
        ),

        // World 3: Ekanyunena Purvena
        SutraLesson(
            id = "ekanyunena",
            sanskritTitle = "एकन्यूनेन पूर्वेण",
            englishTitle = "By One Less than the Previous One",
            shloka = "एकन्यूनेन पूर्वेण।",
            literalMeaning = "By one less than the previous one.",
            shortcutRule = "For multiplying by 9s: Left part is (Number - 1), Right part is (9s complement of left part).",
            primaryEquation = "47 × 99",
            targetAnswer = "4653",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Left Part: Lessen by 1",
                    formulaDisplay = "47 − 1",
                    stepResult = "46",
                    explanation = "Reduce the multiplicand by 1"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Right Part: 9s Complement",
                    formulaDisplay = "99 − 46",
                    stepResult = "53",
                    explanation = "Subtract the left part from the 9s base"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Combine Left & Right",
                    formulaDisplay = "46 | 53",
                    stepResult = "4653",
                    explanation = "Join 46 and 53 to get 4653"
                )
            ),
            carries = emptyList()
        ),

        // World 4: Yavadunam
        SutraLesson(
            id = "yavadunam",
            sanskritTitle = "यावदूनम्",
            englishTitle = "Lessen by Deficiency",
            shloka = "यावदूनं तावदूनीकृत्य वर्गं च योजयेत्।",
            literalMeaning = "Whatever the deficiency, lessen by that amount and append square of deficiency.",
            shortcutRule = "For squaring numbers near 100: (100 - d)² = (100 - 2d) | d².",
            primaryEquation = "96²",
            targetAnswer = "9216",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Identify Deficiency from 100",
                    formulaDisplay = "100 − 96",
                    stepResult = "4",
                    explanation = "Base is 100, deficiency d = 4"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Left Part: (96 − 4)",
                    formulaDisplay = "96 − 4",
                    stepResult = "92",
                    explanation = "Lessen the number by its deficiency"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Right Part: Deficiency² (d²)",
                    formulaDisplay = "4²",
                    stepResult = "16",
                    explanation = "Square the deficiency 4² = 16 → 9216"
                )
            ),
            carries = emptyList()
        ),

        // World 5: Urdhva Tiryagbhyam
        SutraLesson(
            id = "urdhva",
            sanskritTitle = "ऊर्ध्व तिर्यग्भ्याम्",
            englishTitle = "Vertically and Crosswise",
            shloka = "ऊर्ध्वतिर्यग्भ्यां गुणयेत्।",
            literalMeaning = "Vertically and crosswise multiply.",
            shortcutRule = "Multiply vertical units -> Crosswise product sum + carry -> Vertical tens + carry.",
            primaryEquation = "23 × 14",
            targetAnswer = "322",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Units × Units (Vertical Right)",
                    formulaDisplay = "3 × 4",
                    stepResult = "12 (write 2, carry 1)",
                    explanation = "Multiply vertical right units digits"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Crosswise Multiplication",
                    formulaDisplay = "2(4) + 3(1) + 1",
                    stepResult = "12 (write 2, carry 1)",
                    explanation = "Multiply diagonals and add carry 1"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Tens × Tens (Vertical Left)",
                    formulaDisplay = "2(1) + 1",
                    stepResult = "3",
                    explanation = "Multiply vertical tens digits and add carry 1 → 322"
                )
            ),
            carries = listOf(1, 1)
        ),

        // World 6: Paravartya Yojayet
        SutraLesson(
            id = "paravartya",
            sanskritTitle = "परावर्त्य योजयेत्",
            englishTitle = "Transpose and Apply",
            shloka = "परावर्त्य योजयेत्।",
            literalMeaning = "Transpose and apply.",
            shortcutRule = "Synthetic Division: Invert the divisor sign and multiply-add coefficients column by column.",
            primaryEquation = "(x² + 5x + 6) ÷ (x + 2)",
            targetAnswer = "3",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Transpose Divisor Constant",
                    formulaDisplay = "Divisor (+2) → Transposed (−2)",
                    stepResult = "−2",
                    explanation = "Invert sign of divisor constant"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "First Coefficient",
                    formulaDisplay = "Leading coefficient = 1",
                    stepResult = "1x",
                    explanation = "Drop first coefficient directly into quotient"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Second Coefficient: 5 + (1 × −2)",
                    formulaDisplay = "5 + (1 × −2)",
                    stepResult = "3",
                    explanation = "Multiply quotient by transposed root and add → Quotient is (x + 3)"
                )
            ),
            carries = emptyList()
        ),

        // World 7: Anurupyena
        SutraLesson(
            id = "anurupyena_square",
            sanskritTitle = "आनुरूप्येण शून्यमन्यत्",
            englishTitle = "Proportionately (Working Sub-Base)",
            shloka = "आनुरूप्येण शून्यमन्यत्।",
            literalMeaning = "If one is in ratio, the other is zero.",
            shortcutRule = "Sub-base squaring: Scale base ratio, apply deficiency, then scale left side.",
            primaryEquation = "48² (Base 50 = 100/2)",
            targetAnswer = "2304",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Deficiency from Sub-Base 50",
                    formulaDisplay = "50 − 48",
                    stepResult = "2",
                    explanation = "Deficiency d = 2 from working sub-base 50"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Left Part: (48 − 2) / 2",
                    formulaDisplay = "(48 − 2) ÷ 2",
                    stepResult = "23",
                    explanation = "Lessen by deficiency (46) and divide by base scale factor 2"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Right Part: 2² (2 digits)",
                    formulaDisplay = "2²",
                    stepResult = "04",
                    explanation = "Append 2-digit square of deficiency → 2304"
                )
            ),
            carries = emptyList()
        ),

        // World 8: Sankalana-Vyavakalanabhyam
        SutraLesson(
            id = "sankalana",
            sanskritTitle = "संकलनव्यवकलनाभ्याम्",
            englishTitle = "By Addition and by Subtraction",
            shloka = "संकलनव्यवकलनाभ्यां शोधयेत्।",
            literalMeaning = "By addition and by subtraction.",
            shortcutRule = "For symmetric simultaneous equations: Add both to find (x+y), subtract to find (x-y), then solve x and y.",
            primaryEquation = "3x + 2y = 12, 2x + 3y = 13",
            targetAnswer = "2",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Sum Equations: (3+2)x + (2+3)y",
                    formulaDisplay = "5x + 5y = 25",
                    stepResult = "x + y = 5",
                    explanation = "Divide sum equation by 5"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Subtract Equations: (3−2)x + (2−3)y",
                    formulaDisplay = "x − y = −1",
                    stepResult = "x − y = −1",
                    explanation = "Subtract second equation from first"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Solve for x: (5 + (−1)) / 2",
                    formulaDisplay = "2x = 4",
                    stepResult = "2",
                    explanation = "Add both reduced equations: 2x = 4 → x = 2"
                )
            ),
            carries = emptyList()
        ),

        // World 9: Shunyam Samyasamuccaye
        SutraLesson(
            id = "shunyam",
            sanskritTitle = "शून्यं साम्यसमुच्चये",
            englishTitle = "When Collection is Equal, it is Zero",
            shloka = "शून्यं साम्यसमुच्चये।",
            literalMeaning = "When the collection of terms is equal, equate to zero.",
            shortcutRule = "If the independent constant sum on both sides of a symmetric linear equation is equal, set variable x = 0.",
            primaryEquation = "(x + 1) + (x + 2) = (x + 3)",
            targetAnswer = "0",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Sum Left Side Constants",
                    formulaDisplay = "1 + 2",
                    stepResult = "3",
                    explanation = "Left constant sum is 3"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Sum Right Side Constants",
                    formulaDisplay = "Constant = 3",
                    stepResult = "3",
                    explanation = "Right constant is 3 (Sums are equal)"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Equate Variable to Zero",
                    formulaDisplay = "2x + 3 = x + 3 → x",
                    stepResult = "0",
                    explanation = "By Samyasamuccaye, matching constants yield x = 0"
                )
            ),
            carries = emptyList()
        ),

        // World 10: Puranapuranabhyam
        SutraLesson(
            id = "puranapuranabhyam",
            sanskritTitle = "पूरणापूरणाभ्याम्",
            englishTitle = "By Completion or Non-Completion",
            shloka = "पूरणापूरणाभ्याम्।",
            literalMeaning = "By completion or non-completion.",
            shortcutRule = "Complete the square: Add (b/2)² to both sides of x² + bx = c.",
            primaryEquation = "x² + 6x = 16",
            targetAnswer = "2",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Find Completing Term: (b/2)²",
                    formulaDisplay = "(6 / 2)² = 3²",
                    stepResult = "9",
                    explanation = "Half of 6 squared is 9"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Add 9 to Both Sides: (x + 3)²",
                    formulaDisplay = "16 + 9",
                    stepResult = "25",
                    explanation = "(x + 3)² = 25"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Take Square Root & Solve",
                    formulaDisplay = "x + 3 = 5 → x = 5 − 3",
                    stepResult = "2",
                    explanation = "Positive root is x = 2"
                )
            ),
            carries = emptyList()
        ),

        // World 11: Vyashtisamashtih
        SutraLesson(
            id = "vyashtisamashtih",
            sanskritTitle = "व्यष्टिसमष्टिः",
            englishTitle = "Specific and General",
            shloka = "व्यष्टिसमष्टिः।",
            literalMeaning = "Specific and general (Part and whole).",
            shortcutRule = "Factor cubic/biquadratic polynomials by decomposing the middle term into harmonious parts.",
            primaryEquation = "x³ + 6x² + 11x + 6 = 0",
            targetAnswer = "1",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Test Rational Root x = −1",
                    formulaDisplay = "(−1)³ + 6(1) + 11(−1) + 6",
                    stepResult = "0",
                    explanation = "Sum is 0, so (x + 1) is a verified factor"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Decompose into Quadratic",
                    formulaDisplay = "(x + 1)(x² + 5x + 6)",
                    stepResult = "(x+1)(x+2)(x+3)",
                    explanation = "Factor remaining quadratic into (x+2)(x+3)"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Smallest Root Magnitude",
                    formulaDisplay = "|−1|",
                    stepResult = "1",
                    explanation = "First root magnitude is 1"
                )
            ),
            carries = emptyList()
        ),

        // World 12: Shesanyankena Charamena
        SutraLesson(
            id = "shesanyankena",
            sanskritTitle = "शेषाण्यङ्केन चरमेण",
            englishTitle = "The Remainders by the Last Digit",
            shloka = "शेषाण्यङ्केन चरमेण।",
            literalMeaning = "The remainders by the last digit.",
            shortcutRule = "Decimal recurring expansions for fractions ending in 9 (e.g. 1/19): Multiply previous digit by Ekadhikena multiplier.",
            primaryEquation = "1/19 (First 3 digits)",
            targetAnswer = "526",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Ekadhikena Multiplier for 19",
                    formulaDisplay = "(1 + 1)",
                    stepResult = "2",
                    explanation = "Multiplier is 2"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Generate Digits from Right",
                    formulaDisplay = "1 × 2 = 2 → 2 × 2 = 4",
                    stepResult = "0.0526315...",
                    explanation = "Multiplying digits from right produces recurring decimal"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Leading Significant Digits",
                    formulaDisplay = "First 3 non-zero digits",
                    stepResult = "526",
                    explanation = "Leading decimal digits: 526"
                )
            ),
            carries = emptyList()
        ),

        // World 13: Sopantyadvayamantyam
        SutraLesson(
            id = "sopantyadvayamantyam",
            sanskritTitle = "सोपान्त्यद्वयमन्त्यम्",
            englishTitle = "The Ultimate and Twice the Penultimate",
            shloka = "सोपान्त्यद्वयमन्त्यम्।",
            literalMeaning = "The ultimate and twice the penultimate.",
            shortcutRule = "For equations 1/(x+a) + 1/(x+b) = 1/(x+c) + 1/(x+d): If a+b=c+d, then 2x + (a+b) = 0.",
            primaryEquation = "1/(x+2) + 1/(x+3) = 1/(x+1) + 1/(x+4)",
            targetAnswer = "5",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Check Sum of Constants",
                    formulaDisplay = "2 + 3 = 1 + 4",
                    stepResult = "5",
                    explanation = "Both constant pairs sum to 5"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Apply Penultimate Rule",
                    formulaDisplay = "2x + 5 = 0",
                    stepResult = "2x = −5",
                    explanation = "Twice the variable plus sum constant equals zero"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Constant Sum Value",
                    formulaDisplay = "a + b",
                    stepResult = "5",
                    explanation = "Constant sum value is 5"
                )
            ),
            carries = emptyList()
        ),

        // World 14: Gunitasamuccayah
        SutraLesson(
            id = "gunitasamuccayah",
            sanskritTitle = "गुणितसमुच्चयः",
            englishTitle = "Product of the Sum is Sum of the Products",
            shloka = "गुणितसमुच्चयः समुच्चयगुणितः।",
            literalMeaning = "The product of the sum is equal to the sum of the product.",
            shortcutRule = "Verify algebraic expansions: Substitute x=1; Product of factor sums must equal the sum of polynomial coefficients.",
            primaryEquation = "(x + 2)(x + 3) = x² + 5x + 6",
            targetAnswer = "12",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Sum of Factor 1 (x=1): (1+2)",
                    formulaDisplay = "1 + 2",
                    stepResult = "3",
                    explanation = "First factor evaluates to 3"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Sum of Factor 2 (x=1): (1+3)",
                    formulaDisplay = "1 + 3",
                    stepResult = "4",
                    explanation = "Second factor evaluates to 4"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Product of Sums (3 × 4)",
                    formulaDisplay = "3 × 4 = 12 = (1 + 5 + 6)",
                    stepResult = "12",
                    explanation = "Both sides sum to 12 (Expansion verified)"
                )
            ),
            carries = emptyList()
        ),

        // World 15: Gunakasamuccayah
        SutraLesson(
            id = "gunakasamuccayah",
            sanskritTitle = "गुणकसमुच्चयः",
            englishTitle = "Factor of the Sum is Sum of the Factors",
            shloka = "गुणकसमुच्चयः।",
            literalMeaning = "The factor of the sum is the sum of the factors.",
            shortcutRule = "Quadratic factorization verification: Sum of quadratic coefficients equals product of factor coefficient sums.",
            primaryEquation = "2x² + 7x + 3 = (2x + 1)(x + 3)",
            targetAnswer = "12",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "Sum of Quadratic Coefficients",
                    formulaDisplay = "2 + 7 + 3",
                    stepResult = "12",
                    explanation = "Polynomial coefficient sum is 12"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Factor 1 Sum (2 + 1) = 3",
                    formulaDisplay = "2 + 1",
                    stepResult = "3",
                    explanation = "First factor sum is 3"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Factor 2 Sum (1 + 3) = 4",
                    formulaDisplay = "3 × 4",
                    stepResult = "12",
                    explanation = "Product of factor sums is 3 × 4 = 12 (Verified)"
                )
            ),
            carries = emptyList()
        ),

        // World 16: Chalana-Kalanabhyam
        SutraLesson(
            id = "chalana",
            sanskritTitle = "चलनकलनाभ्याम्",
            englishTitle = "Differential Calculus & Residue Operations",
            shloka = "चलनकलनाभ्याम्।",
            literalMeaning = "By differences and similarities (Calculus).",
            shortcutRule = "Find turning points and roots: Set the Vedic first differential d/dx = 0.",
            primaryEquation = "d/dx(3x² + 12x) = 0",
            targetAnswer = "2",
            steps = listOf(
                DecompositionStep(
                    stepNumber = 1,
                    label = "First Derivative: d/dx(3x² + 12x)",
                    formulaDisplay = "6x + 12",
                    stepResult = "6x + 12",
                    explanation = "Differentiate each term with respect to x"
                ),
                DecompositionStep(
                    stepNumber = 2,
                    label = "Equate Derivative to 0",
                    formulaDisplay = "6x + 12 = 0",
                    stepResult = "6x = −12",
                    explanation = "Set turning point slope to zero"
                ),
                DecompositionStep(
                    stepNumber = 3,
                    label = "Solve Root Magnitude: |−2|",
                    formulaDisplay = "x = −12 / 6",
                    stepResult = "2",
                    explanation = "Magnitude of critical root is 2"
                )
            ),
            carries = emptyList()
        )
    ).associateBy { it.id }

    fun getLessonForModule(module: SutraModule): SutraLesson {
        return allLessons[module.id] ?: allLessons["urdhva"] ?: SampleUrdhvaLesson
    }
}
