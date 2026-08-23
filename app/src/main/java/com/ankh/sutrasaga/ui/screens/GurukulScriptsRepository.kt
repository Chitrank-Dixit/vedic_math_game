package com.ankh.sutrasaga.ui.screens

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.DiscipleEyes
import com.ankh.sutrasaga.domain.models.DiscipleState
import com.ankh.sutrasaga.domain.models.GuruMouth
import com.ankh.sutrasaga.domain.models.GuruPose
import com.ankh.sutrasaga.domain.models.GurukulDialogueBeat
import com.ankh.sutrasaga.domain.models.GurukulScript
import com.ankh.sutrasaga.domain.models.SutraProblem

/**
 * Repository of dedicated, authentic Guru-Shishya dialogue scripts and problems
 * for all 16 Primary Vedic Sutras.
 */
object GurukulScriptsRepository {

    val allScripts: Map<String, GurukulScript> = listOf(
        // World 1: Ekadhikena Purvena
        GurukulScript(
            worldId = 1,
            title = "एकाधिकेन पूर्वेण",
            subtitle = "World 1 • By One More than the Previous One",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "प्रणाम शिष्य! Welcome to the sacred grove of mathematical insight. Today we unveil our first key: 'एकाधिकेन पूर्वेण' — By One More Than The Previous One.",
                    discipleText = "Guru-ji, does this ancient sutra help us square numbers like 65 in our heads?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Indeed! Look at the prefix of 65, which is 6. The sutra commands us: take one more than 6, which is 7.",
                    discipleText = "Ah! 6 + 1 = 7!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Now multiply the prefix by its increment: 6 × 7 = 42. This forms the left hemisphere of your answer.",
                    discipleText = "6 × 7 gives 42 for the left side!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "For the right hemisphere, simply square the units digit 5: 5² = 25. Now combine: 42 and 25.",
                    discipleText = "4225! That is lightning fast, Guru-ji!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 3
                ),
                GurukulDialogueBeat(
                    guruText = "उत्तमम्! 65² is 4225. Now step up to the slate and prove your mastery across the realm!",
                    discipleText = "I am ready, Guru-ji!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 2: Nikhilam Navatashcaramam Dashatah
        GurukulScript(
            worldId = 2,
            title = "निखिलं नवतश्चरमं दशतः",
            subtitle = "World 2 • All from 9 and Last from 10",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "Welcome, seeker. Our second sutra is 'निखिलं नवतश्चरमं दशतः' — All from nine, and the last from ten.",
                    discipleText = "Guru-ji, how do we subtract 364 from 1000 without borrowing backwards?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.CONFUSED,
                    discipleEyes = DiscipleEyes.CONFUSED,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Observe the digits of 364. Subtract each leading digit from 9: 9 − 3 = 6, and 9 − 6 = 3.",
                    discipleText = "So the hundreds digit is 6, and the tens digit is 3...",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "And the final rule: take the last digit from 10: 10 − 4 = 6!",
                    discipleText = "6, 3, and 6... 636! We solved it from left to right!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Exact and effortless. No carries, no borrowing. The power of Vedic mental arithmetic is yours!",
                    discipleText = "Let me practice this on the sacred slate!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 3: Ekanyunena Purvena
        GurukulScript(
            worldId = 3,
            title = "एकन्यूनेन पूर्वेण",
            subtitle = "World 3 • By One Less than the Previous One",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "Today we explore the twin counterpart to Ekadhikena: 'एकन्यूनेन पूर्वेण' — By One Less Than The Previous One.",
                    discipleText = "Can we use this to multiply 47 by 99 instantly?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "First, lessen the multiplicand by 1: 47 − 1 = 46. This is your left hemisphere.",
                    discipleText = "Left part is 46!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Now take the 9s complement of 46: 99 − 46 = 53. This forms your right hemisphere.",
                    discipleText = "Right part is 53! Combined: 4653!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "47 × 99 = 4653. Mental math transforms heavy multiplication into simple subtraction.",
                    discipleText = "Astounding! I am eager to solve more.",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 4: Yavadunam
        GurukulScript(
            worldId = 4,
            title = "यावदूनम्",
            subtitle = "World 4 • Lessen by Deficiency",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "Behold 'यावदूनं तावदूनीकृत्य वर्गं च योजयेत्' — Whatever the deficiency, lessen by that amount, and append the square of deficiency.",
                    discipleText = "How does this square 96 near base 100?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.CONFUSED,
                    discipleEyes = DiscipleEyes.CONFUSED,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "96 is 4 less than 100. So deficiency d = 4. Lessen 96 by 4: 96 − 4 = 92.",
                    discipleText = "Left side is 92!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Now square the deficiency: 4² = 16. Append it to 92: 9216.",
                    discipleText = "96² is 9216! In just two mental steps!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "You see clearly, Ankh. Harness this deficiency rule in your trials!",
                    discipleText = "Guide me forward, Guru-ji!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 5: Urdhva Tiryagbhyam
        GurukulScript(
            worldId = 5,
            title = "ऊर्ध्व तिर्यग्भ्याम्",
            subtitle = "World 5 • Vertically and Crosswise",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "We arrive at the crown jewel of Vedic multiplication: 'ऊर्ध्वतिर्यग्भ्यां गुणयेत्' — Vertically and Crosswise.",
                    discipleText = "The universal multiplication sutra! How do we apply it to 23 × 14?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Step 1: Vertical right digits: 3 × 4 = 12. Write 2, carry 1.",
                    discipleText = "Units digit is 2, with 1 carried over.",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Step 2: Crosswise diagonal sums: 2(4) + 3(1) + carry 1 = 8 + 3 + 1 = 12. Write 2, carry 1.",
                    discipleText = "Tens digit is 2, with 1 carried over.",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Step 3: Vertical left digits: 2(1) + carry 1 = 3. Final answer: 322!",
                    discipleText = "322! The geometric ray pattern solves any two-digit product!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 3
                ),
                GurukulDialogueBeat(
                    guruText = "Master this rhythm, for it is the foundation of all higher arithmetic.",
                    discipleText = "Let us begin the battle!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 6: Paravartya Yojayet
        GurukulScript(
            worldId = 6,
            title = "परावर्त्य योजयेत्",
            subtitle = "World 6 • Transpose and Apply",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "When dividing polynomials or numbers near a base, invoke 'परावर्त्य योजयेत्' — Transpose and Apply.",
                    discipleText = "What does transposing mean in polynomial division?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.CONFUSED,
                    discipleEyes = DiscipleEyes.CONFUSED,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Invert the divisor constant +2 into −2. Drop the leading coefficient 1.",
                    discipleText = "First term of quotient is 1x.",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Now multiply and add: 5 + (1 × −2) = 3. The remainder is 6 + (3 × −2) = 0.",
                    discipleText = "The quotient is (x + 3) with zero remainder!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Synthetic division executed with pure mental symmetry. Proceed to your test!",
                    discipleText = "I shall apply it with precision!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 7: Anurupyena
        GurukulScript(
            worldId = 7,
            title = "आनुरूप्येण शून्यमन्यत्",
            subtitle = "World 7 • Proportionately (Sub-Base Squaring)",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "When a number is not near 100 but near 50, we use 'आनुरूप्येण' — Proportionately.",
                    discipleText = "How do we bridge 48 with base 50?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Working base is 50, which is 100 ÷ 2. Deficiency from 50 is 2. Lessen 48 by 2 = 46.",
                    discipleText = "48 − 2 = 46. Do we divide by 2 for the base scale?",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Yes! 46 ÷ 2 = 23. Append 2² = 04. Combined: 2304!",
                    discipleText = "2304! Proportionate scaling unlocks any working base!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Proportion is the harmony of number theory. Step into the arena!",
                    discipleText = "Ready, Guru-ji!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 8: Sankalana-Vyavakalanabhyam
        GurukulScript(
            worldId = 8,
            title = "संकलनव्यवकलनाभ्याम्",
            subtitle = "World 8 • By Addition and by Subtraction",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "When equations share symmetric coefficients, use 'संकलनव्यवकलनाभ्याम्' — By Addition and by Subtraction.",
                    discipleText = "Instead of substitution, we add and subtract the system?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Add both equations: 5x + 5y = 25 ⟶ x + y = 5.",
                    discipleText = "The sum simplifies to x + y = 5!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Now subtract: (3−2)x + (2−3)y = 12 − 13 ⟶ x − y = −1. Add them: 2x = 4 ⟶ x = 2.",
                    discipleText = "x = 2, and y = 3! In seconds without fractions!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Simplicity triumphs over brute force. Test your skills now!",
                    discipleText = "Onward to mastery!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 9: Shunyam Samyasamuccaye
        GurukulScript(
            worldId = 9,
            title = "शून्यं साम्यसमुच्चये",
            subtitle = "World 9 • When Collection is Equal, it is Zero",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "Observe equations where linear constant sums balance: 'शून्यं साम्यसमुच्चये' — When the collection is equal, equate to zero.",
                    discipleText = "How do we spot the zero root instantly?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.CONFUSED,
                    discipleEyes = DiscipleEyes.CONFUSED,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Left side constants: 1 + 2 = 3. Right side constant = 3. Because constant sums match, the variable x balances to 0.",
                    discipleText = "x = 0 immediately satisfies the equation!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Recognizing symmetry eliminates entire pages of algebraic manipulation. Master this vision!",
                    discipleText = "I see the harmony, Guru-ji!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 10: Puranapuranabhyam
        GurukulScript(
            worldId = 10,
            title = "पूरणापूरणाभ्याम्",
            subtitle = "World 10 • By Completion or Non-Completion",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "'पूरणापूरणाभ्याम्' guides us to complete algebraic squares and geometric figures.",
                    discipleText = "How do we complete x² + 6x = 16?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Take half of 6, which is 3. Square it: 3² = 9. Add 9 to both sides: (x + 3)² = 16 + 9 = 25.",
                    discipleText = "Both sides become perfect squares!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Take the square root: x + 3 = 5 ⟶ x = 2.",
                    discipleText = "x = 2! Quadratic roots found through completion!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Completion brings wholeness to math. Claim your victory!",
                    discipleText = "I shall conquer the slate!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 11: Vyashtisamashtih
        GurukulScript(
            worldId = 11,
            title = "व्यष्टिसमष्टिः",
            subtitle = "World 11 • Specific and General",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "Higher-degree polynomials yield to 'व्यष्टिसमष्टिः' — By Part and Whole.",
                    discipleText = "Can we factor cubic equations mentally?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.CONFUSED,
                    discipleEyes = DiscipleEyes.CONFUSED,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Examine the constant term 6. Test specific part x = −1: (−1)³ + 6(1) + 11(−1) + 6 = 0.",
                    discipleText = "(x + 1) is confirmed as a factor!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Decompose into (x + 1)(x + 2)(x + 3) = 0. The roots are −1, −2, −3.",
                    discipleText = "Decomposing the whole into harmonious parts solves the cubic!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "You understand the relation between the individual and the totality. Proceed!",
                    discipleText = "Ready, Guru-ji!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 12: Shesanyankena Charamena
        GurukulScript(
            worldId = 12,
            title = "शेषाण्यङ्केन चरमेण",
            subtitle = "World 12 • The Remainders by the Last Digit",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "Fractions with denominators ending in 9 (like 1/19) obey 'शेषाण्यङ्केन चरमेण'.",
                    discipleText = "How do we compute 1/19 without endless long division?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "The multiplier is (1 + 1) = 2. Starting from the last digit 1, multiply leftwards: 1 × 2 = 2, 2 × 2 = 4, 4 × 2 = 8...",
                    discipleText = "We generate the decimal cycle from right to left!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Leading digits emerge as 0.0526315... Full 18-digit recurring period generated by simple doubling!",
                    discipleText = "Incredible! Division transformed into multiplication!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "The remainders tell the complete story. Shine your light on the slate!",
                    discipleText = "I shall conquer 1/19!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 13: Sopantyadvayamantyam
        GurukulScript(
            worldId = 13,
            title = "सोपान्त्यद्वयमन्त्यम्",
            subtitle = "World 13 • The Ultimate and Twice the Penultimate",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "'सोपान्त्यद्वयमन्त्यम्' resolves complex rational fractions where constant sums are equal.",
                    discipleText = "Here 2+3 = 5 and 1+4 = 5. How do we solve for x?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.CONFUSED,
                    discipleEyes = DiscipleEyes.CONFUSED,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Twice the variable plus the sum constant equals zero: 2x + 5 = 0 ⟶ x = −5/2.",
                    discipleText = "x = −2.5 without multiplying common denominators!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "The penultimate rule turns intricate rational fractions into a one-line solution!",
                    discipleText = "Let me prove this in the world solver!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Go forth and solve with swift certainty!",
                    discipleText = "Ready, Guru-ji!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 14: Gunitasamuccayah
        GurukulScript(
            worldId = 14,
            title = "गुणितसमुच्चयः",
            subtitle = "World 14 • Product of the Sum is the Sum of the Products",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "To verify whether algebraic factorization is correct, invoke 'गुणितसमुच्चयः समुच्चयगुणितः'.",
                    discipleText = "How do we verify (x+2)(x+3) = x² + 5x + 6?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Substitute x = 1. Factor sum 1: (1+2)=3. Factor sum 2: (1+3)=4. Product of sums: 3 × 4 = 12.",
                    discipleText = "And the polynomial coefficients: 1 + 5 + 6 = 12!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "12 = 12! The product of the sums equals the sum of the product. Factorization verified!",
                    discipleText = "Instant proof for algebraic factorization!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Verification gives unshakeable confidence. Enter the arena!",
                    discipleText = "I am ready!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 15: Gunakasamuccayah
        GurukulScript(
            worldId = 15,
            title = "गुणकसमुच्चयः",
            subtitle = "World 15 • The Factor of the Sum is the Sum of the Factors",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "'गुणकसमुच्चयः' verifies that polynomial factors match their quadratic roots.",
                    discipleText = "How do we check 2x² + 7x + 3 = (2x+1)(x+3)?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Sum of quadratic coefficients: 2 + 7 + 3 = 12. Factor 1 sum: 2 + 1 = 3. Factor 2 sum: 1 + 3 = 4.",
                    discipleText = "Product of factor sums: 3 × 4 = 12!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "Both equal 12. Every quadratic polynomial can be audited in a single breath.",
                    discipleText = "Ready for the test, Guru-ji!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "Apply your knowledge and master the factors!",
                    discipleText = "Victory shall be ours!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        ),

        // World 16: Chalana-Kalanabhyam
        GurukulScript(
            worldId = 16,
            title = "चलनकलनाभ्याम्",
            subtitle = "World 16 • Differential Calculus & Residue Operations",
            beats = listOf(
                GurukulDialogueBeat(
                    guruText = "We reach the summit of the 16 Sutras: 'चलनकलनाभ्याम्' — Differential Calculus and Sequential Change.",
                    discipleText = "Vedic calculus for finding turning points and roots?",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.NEUTRAL,
                    discipleEyes = DiscipleEyes.NEUTRAL,
                    slateStepIndex = 0
                ),
                GurukulDialogueBeat(
                    guruText = "Differentiate 3x² + 12x: 6x + 12. Set to zero to find the stationary point: 6x = −12 ⟶ x = −2.",
                    discipleText = "The critical turning point magnitude is 2!",
                    guruPose = GuruPose.EXPLAINING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 1
                ),
                GurukulDialogueBeat(
                    guruText = "You have traversed all 16 realms of Vedic Mathematics, Ankh. Step forward and claim the title of Sutra Master!",
                    discipleText = "Thank you, Guru-ji! Let us complete the final challenge!",
                    guruPose = GuruPose.TALKING,
                    guruMouth = GuruMouth.TALKING,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 2
                ),
                GurukulDialogueBeat(
                    guruText = "May the eternal light of mathematics guide your mind forever!",
                    discipleText = "Om Shanti Shanti Shanti!",
                    guruPose = GuruPose.BASE,
                    guruMouth = GuruMouth.SMILE,
                    discipleState = DiscipleState.UNDERSTANDING,
                    discipleEyes = DiscipleEyes.HAPPY,
                    slateStepIndex = 5
                )
            )
        )
    ).associateBy { it.title }

    fun getScriptForModule(module: SutraModule): GurukulScript {
        return allScripts[module.sanskritTitle]
            ?: allScripts.values.firstOrNull { it.worldId == module.worldNumber }
            ?: allScripts["ऊर्ध्व तिर्यग्भ्याम्"]
            ?: allScripts.values.first()
    }

    fun getProblemForModule(module: SutraModule): SutraProblem {
        val lesson = SutraLessonsRepository.getLessonForModule(module)
        return SutraProblem(
            id = lesson.id,
            sutraName = lesson.englishTitle,
            questionText = "Calculate: ${lesson.primaryEquation}",
            operand = 65L,
            correctAnswer = lesson.targetAnswer.toLongOrNull() ?: 4225L,
            prefixPart = 6L,
            incrementedPrefix = 7L,
            prefixProduct = 42L,
            appendedSuffix = "25",
            decompositionSteps = lesson.steps,
            distractors = listOf(4025L, 4235L, 3625L),
            difficultyTier = DifficultyTier.TIER_1_EASY
        )
    }
}
