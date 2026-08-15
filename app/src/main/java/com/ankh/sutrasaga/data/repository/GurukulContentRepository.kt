package com.ankh.sutrasaga.data.repository

import com.ankh.sutrasaga.domain.models.DiscipleEyes
import com.ankh.sutrasaga.domain.models.DiscipleState
import com.ankh.sutrasaga.domain.models.GuruMouth
import com.ankh.sutrasaga.domain.models.GuruPose
import com.ankh.sutrasaga.domain.models.GurukulDialogueBeat
import com.ankh.sutrasaga.domain.models.GurukulScript

object GurukulContentRepository {

    fun getStoryScript(worldId: Int): GurukulScript {
        return when (worldId) {
            1 -> GurukulScript(
                worldId = 1,
                title = "World 1: The Gurukul of Fives",
                subtitle = "Master Purva's Sacred Grove",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to my Gurukul! Here, numbers ending in 5 reveal their secrets.",
                        discipleText = "Wait, we're doing math under a tree without battery power?!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Observe! To square any number ending in 5, you don't do tedious long multiplication.",
                        discipleText = "No long multiplication?! My floating-point processor is intrigued!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "I will show you Ekadhikena Purvena — 'By One More Than the Previous One'!",
                        discipleText = "Show me the secret, Master!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            2 -> GurukulScript(
                worldId = 2,
                title = "World 2: Realm of Complements",
                subtitle = "The Temple of Base Ten",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Realm of Complements, where a number's distance from a power of ten is its secret.",
                        discipleText = "So 100 minus 37 can be solved without borrowing through every column?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Use Nikhilam: subtract every digit except the last from 9, and the last digit from 10.",
                        discipleText = "A complement spell! I am ready to try it.",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            3 -> GurukulScript(
                worldId = 3,
                title = "World 3: Temple of Nines",
                subtitle = "The Hall of One Less",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "At the Temple of Nines, multiplying by 99 or 999 follows a compact pattern.",
                        discipleText = "Please tell me the pattern does not involve a scroll full of carries.",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Ekanyunena Purvena means one less than the previous one. Then use the complement for the right side.",
                        discipleText = "One less, then the complement. I can remember that.",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            4 -> GurukulScript(
                worldId = 4,
                title = "World 4: Spire of Proximity",
                subtitle = "Master Yavadunam's Base Sanctuary",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Spire of Proximity! I am Master Yavadunam. I measure all numbers by their deficiency from base perfection!",
                        discipleText = "Deficiency? Are you saying 94 is deficient because it's 6 away from 100?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Precisely! When squaring numbers near 10, 100, or 1000: lessen the number by its deficiency for LHS, and square the deficiency for RHS!",
                        discipleText = "Lessen by deficiency, then square the deficiency? That sounds brilliantly simple!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            5 -> GurukulScript(
                worldId = 5,
                title = "World 5: The Grand Pinnacle",
                subtitle = "Master Urdhva's Supreme Sanctuary (MVP Finale)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Greetings, seeker! I am Grandmaster Urdhva. The previous four masters taught you specialized tricks, but I bring you the universal key!",
                        discipleText = "A universal key?! One formula to rule all multiplication?!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Urdhva-Tiryagbhyam: 'Vertically and Crosswise'! Vertical right, crosswise middle, vertical left. Multiplies ANY numbers in 3 lightning steps!",
                        discipleText = "Vertically and Crosswise! Let us conquer the final trial!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            6 -> GurukulScript(
                worldId = 6,
                title = "World 6: Hall of Transposition",
                subtitle = "Master Paravartya's Division Chamber (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Hall of Transposition! Up to now you have mastered multiplication. Today, we conquer Division with Paravartya Yojayet!",
                        discipleText = "Division?! But division requires endless long division steps and trial subtraction!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Not with Paravartya! 'Transpose and Apply': when divisor is slightly over base 10 or 100, transpose the deviation into a negative adjustment!",
                        discipleText = "Transpose the deviation and apply column by column! The divisor is slightly over the base, so we give it a tiny adjustment!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            7 -> GurukulScript(
                worldId = 7,
                title = "World 7: Sanctuary of Ratios",
                subtitle = "Master Anurupye's Linear Observatory (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Sanctuary of Ratios! I am Master Anurupye. Anurupye Shunyamanyat: 'If one is in ratio, the other is zero'!",
                        discipleText = "If one is in ratio, the other is zero?! Are you saying a variable vanishes into zero?!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Indeed! When solving simultaneous equations, if one variable's coefficient ratio equals the constant ratio, the OTHER variable becomes 0!",
                        discipleText = "A variable hiding in plain sight! If x-coefficients match the constants, y disappears!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            8 -> GurukulScript(
                worldId = 8,
                title = "World 8: Mirror Chamber of Equations",
                subtitle = "Master Sankalana's Dual Sanctuary (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Mirror Chamber! I am Master Sankalana. Sankalana-Vyavakalanabhyam: 'By addition and by subtraction'!",
                        discipleText = "By addition and by subtraction?! Mirror twin equations where coefficients swap places?!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Exactly! When coefficients are interchanged, adding the equations unlocks (x + y) and subtracting them unlocks (x - y)!",
                        discipleText = "Adding unlocks sum, subtracting unlocks difference! Two simple operations conquer two unknowns!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            9 -> GurukulScript(
                worldId = 9,
                title = "World 9: Court of Equated Sums",
                subtitle = "Master Shunyam's Zero Citadel (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Court of Equated Sums! I am Master Shunyam. Shunyam Samyasamuccaye: 'When the sum is the same, that sum is zero'!",
                        discipleText = "When the sum is the same, it is zero?! Can we just set anything to zero with a magic wand?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Ha! The zero button is not a magic wand; it only works when the pattern gives it permission! Look for repeated factors or matching denominator sums!",
                        discipleText = "Repeated factors or matching denominator sums give permission to equate to zero!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            10 -> GurukulScript(
                worldId = 10,
                title = "World 10: Arena of Completion",
                subtitle = "Master Purana's Quadratic Forge (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Arena of Completion! I am Master Purana. Puranapuranabhyam: 'By completion or non-completion'!",
                        discipleText = "By completion or non-completion?! If we add a completion piece to one side, won't the equation tilt and break?!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "A missing square is not lost; it is merely waiting for its matching piece! Adding the exact same completion term to BOTH sides preserves divine balance!",
                        discipleText = "A missing square waiting for its matching piece! Adding the same term to both sides keeps the balance!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            11 -> GurukulScript(
                worldId = 11,
                title = "World 11: Pavilion of Symmetry",
                subtitle = "Master Vyashti's Midpoint Sanctuary (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Pavilion of Symmetry! I am Master Vyashti. Vyashtisamashtih: 'Part and Whole'!",
                        discipleText = "Part and Whole?! Two numbers arguing until they meet halfway?!",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Precisely! The two numbers argued until they met halfway — then math made peace! Find their midpoint average A, measure their equal deviation d, and multiply via A² - d²!",
                        discipleText = "Find midpoint average A, equal deviation d, then A² - d²! Symmetric multiplication made simple!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            12 -> GurukulScript(
                worldId = 12,
                title = "World 12: Tower of Remainder Cycles",
                subtitle = "Master Shesa's Decimal Observatory (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Tower of Remainder Cycles! I am Master Shesa. Shesanyankena Charamena: 'The remainders by the last digit'!",
                        discipleText = "The remainders by the last digit?! Why do decimal digits repeat like a clock?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "The remainder came back because it forgot where it had already been — luckily, the Guru kept notes! When a remainder repeats, the decimal block loops!",
                        discipleText = "Every remainder is a clue left behind! When the remainder repeats, the cycle loops!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            13 -> GurukulScript(
                worldId = 13,
                title = "World 13: Hall of Ultimate & Penultimate",
                subtitle = "Master Sopantya's Multiplication Sanctuary (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Hall of Ultimate & Penultimate! I am Master Sopantya. Sopantyadvayamantyam: 'The ultimate and twice the penultimate'!",
                        discipleText = "The ultimate and twice the penultimate?! Why do we add zero guards to both ends of the number?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "The last digit never works alone — it always calls its neighbor for backup, twice! Zero guards ensure every digit has a neighbor to pair with!",
                        discipleText = "Zero guards give every digit a partner! Pair digit + N × previous digit, then resolve carries!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            14 -> GurukulScript(
                worldId = 14,
                title = "World 14: Chamber of Polynomial Invariants",
                subtitle = "Master Gunita's Factorization Sanctuary (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Chamber of Polynomial Invariants! I am Master Gunita. Gunitasamuccayah: 'The product of the sum is equal to the sum of the products'!",
                        discipleText = "The product of the sum is equal to the sum of the products?! If the sums match, is the factorization definitely correct?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "Not yet — the coefficient sums agree, but the polynomial detective still checks the fingerprints! Sum check is a quick clue; full term expansion is the proof!",
                        discipleText = "Coefficient sums are a quick clue, not the whole proof! We evaluate at x=1 for a quick check, then compare terms!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            15 -> GurukulScript(
                worldId = 15,
                title = "World 15: Factor Pair Forge",
                subtitle = "Master Gunaka's Quadratic Sanctuary (Post-MVP)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Factor Pair Forge! I am Master Gunaka. Gunakasamuccayah: 'The factors of the sum are equal to the sum of the factors'!",
                        discipleText = "The factors of the sum are equal to the sum of the factors?! How do we factor x² + 7x + 10?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "A factor pair is like a dance partner: matching one step is not enough; the rhythm must match too! Product must equal C and sum must equal B!",
                        discipleText = "Product equals C AND sum equals B! For 10, pair (2,5) multiplies to 10 and sums to 7! Roots are -2 and -5!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            16 -> GurukulScript(
                worldId = 16,
                title = "World 16: Apex of Sequential Calculus",
                subtitle = "Master Chalana's Pinnacle Citadel (Campaign Finale)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Welcome to the Apex of Sequential Calculus! I am Master Chalana. Chalana-Kalanabhyam: 'Sequential motion / By calculus'!",
                        discipleText = "The final world of our journey! Are derivative calculus and quadratics truly connected?",
                        isTeachingStep = false,
                        guruPose = GuruPose.BASE,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.CONFUSED,
                        discipleEyes = DiscipleEyes.CONFUSED,
                        slateStepIndex = 0
                    ),
                    GurukulDialogueBeat(
                        guruText = "The derivative did not lose the roots — it simply sent them two invitations, one with a plus sign and one with a minus sign! At any root r, f'(r)² = D!",
                        discipleText = "f'(r)² = D at every root! 2Ax + B = ±√D yields x = (-B ± √D) / 2A! The 16 Sutras are complete!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 0
                    )
                )
            )
            else -> getStoryScript(1)
        }
    }

    fun getTutorialScript(worldId: Int): GurukulScript {
        return when (worldId) {
            1 -> GurukulScript(
                worldId = 1,
                title = "Tutorial 1: Ekadhikena Purvena",
                subtitle = "Demonstration: 65² = 4225",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Let us calculate 65²! First, look at the Slate: ignore the 5 and isolate the tens digit prefix n = 6.",
                        discipleText = "Ah! So we separate 6 and 5!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Apply Ekadhikena (+1) to 6. One more than 6 is 7! So we form 6 × 7.",
                        discipleText = "6 + 1 = 7! So 6 × 7!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Multiply the prefixes! 6 × 7 = 42!",
                        discipleText = "42! That's the left side of our answer!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 4: Now take 5² = 25 and append it to 42!",
                        discipleText = "Append 25 after 42...",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 4
                    ),
                    GurukulDialogueBeat(
                        guruText = "Behold! 42 || 25 = 4225! So 65² = 4225! Boom!",
                        discipleText = "4225! Amazing! Mental arithmetic in 2 seconds!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 5
                    )
                )
            )
            2 -> GurukulScript(
                worldId = 2,
                title = "Tutorial 2: Nikhilam",
                subtitle = "Demonstration: 100 - 37 = 63",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Start with 100 minus 37. Keep the base in view.",
                        discipleText = "The base is 100 and the subtrahend is 37.",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "For the first digit, subtract 3 from 9 to get 6.",
                        discipleText = "All from 9 gives 6.",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "For the final digit, subtract 7 from 10 to get 3. Combine them: 63.",
                        discipleText = "100 minus 37 equals 63!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            3 -> GurukulScript(
                worldId = 3,
                title = "Tutorial 3: Ekanyunena Purvena",
                subtitle = "Demonstration: 47 x 99 = 4653",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "To multiply 47 by 99, first take one less than 47.",
                        discipleText = "That gives 46 for the left side.",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Now take the complement of 46: 53. Put 46 and 53 together.",
                        discipleText = "That makes 4653.",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "So 47 times 99 equals 4653.",
                        discipleText = "The Temple of Nines has a very fast exit.",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    )
                )
            )
            4 -> GurukulScript(
                worldId = 4,
                title = "Tutorial 4: Yavadunam",
                subtitle = "Demonstration: 94² = 8836",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Let us calculate 94²! Look at the Slate: base B = 100. The deficiency is D = 100 - 94 = 6.",
                        discipleText = "Deficiency is 6!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Lessen 94 by its deficiency 6. 94 - 6 = 88! That is our LHS!",
                        discipleText = "94 - 6 = 88 for the left side!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Square the deficiency! 6² = 36! That is our RHS!",
                        discipleText = "6 squared is 36 for the right side!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 4: Combine LHS and RHS! 88 || 36 = 8836!",
                        discipleText = "8836! Squaring 94 in seconds without long multiplication!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            5 -> GurukulScript(
                worldId = 5,
                title = "Tutorial 5: Urdhva-Tiryagbhyam",
                subtitle = "Demonstration: 23 × 41 = 943",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Let us calculate 23 × 41! Step 1: Vertical Right (Units). Multiply 3 × 1 = 3.",
                        discipleText = "3 × 1 = 3 for the units digit!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Crosswise Middle. Sum (2 × 1) + (3 × 4) = 2 + 12 = 14. Write 4, carry 1!",
                        discipleText = "14! Write 4 and carry +1 over to the tens!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Vertical Left. Multiply (2 × 4) + carry 1 = 8 + 1 = 9!",
                        discipleText = "8 + 1 = 9 for the left side!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Behold! 9 || 4 || 3 = 943! So 23 × 41 = 943!",
                        discipleText = "943! Vertically and Crosswise conquers all!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            6 -> GurukulScript(
                worldId = 6,
                title = "Tutorial 6: Paravartya Yojayet",
                subtitle = "Demonstration: 1225 ÷ 12 = 102 remainder 1",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Let us solve 1225 ÷ 12! Base is 10, divisor is 12 $\\rightarrow$ transposed deviation is -2.",
                        discipleText = "Base 10, deviation -2!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Partition 1225 into Quotient '1 2 2' and Remainder '5'. Column 1: bring down 1, multiply 1 × (-2) = -2.",
                        discipleText = "Column 1 is 1! Pass -2 to column 2!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Column 2: 2 + (-2) = 0. Multiply 0 × (-2) = 0. Column 3: 2 + 0 = 2. Multiply 2 × (-2) = -4.",
                        discipleText = "Quotient columns yield 1, 0, 2 $\\rightarrow$ 102!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Remainder column: 5 + (-4) = 1. So 1225 = 12 × 102 + 1!",
                        discipleText = "Quotient 102, Remainder 1! Identity 1225 = 12 × 102 + 1 verified!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            7 -> GurukulScript(
                worldId = 7,
                title = "Tutorial 7: Anurupye Shunyamanyat",
                subtitle = "Demonstration: 3x + 2y = 12 | 6x + 5y = 24",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Look at 3x + 2y = 12 and 6x + 5y = 24! Test x-coefficient ratio vs constant ratio: 3 × 24 = 72, 6 × 12 = 72!",
                        discipleText = "Ratio matches! 72 = 72!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Since x-ratio matches the constant ratio, the OTHER variable y becomes ZERO! y = 0!",
                        discipleText = "y = 0! The ratio detective vanishes y!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Substitute y = 0: 3x + 2(0) = 12 $\\rightarrow$ 3x = 12 $\\rightarrow$ x = 4!",
                        discipleText = "x = 4! Solved in seconds!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Verify in Eq 2: 6(4) + 5(0) = 24 = 24! Solution: x = 4, y = 0!",
                        discipleText = "x = 4, y = 0 verified in both equations!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            8 -> GurukulScript(
                worldId = 8,
                title = "Tutorial 8: Sankalana-Vyavakalanabhyam",
                subtitle = "Demonstration: 45x - 23y = 113 | 23x - 45y = 91",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Look at 45x - 23y = 113 and 23x - 45y = 91! Step 1: Add both equations $\\rightarrow$ 68x - 68y = 204 $\\rightarrow$ x - y = 3!",
                        discipleText = "Addition gives x - y = 3!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Subtract Eq 2 from Eq 1 $\\rightarrow$ 22x + 22y = 22 $\\rightarrow$ x + y = 1!",
                        discipleText = "Subtraction gives x + y = 1!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Combine x + y = 1 and x - y = 3! Adding them gives 2x = 4 $\\rightarrow$ x = 2!",
                        discipleText = "x = 2! And subtracting them gives 2y = -2 $\\rightarrow$ y = -1!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Verify in Eq 1 & 2: 45(2) - 23(-1) = 113 $\\checkmark$ and 23(2) - 45(-1) = 91 $\\checkmark$! Solution: x = 2, y = -1!",
                        discipleText = "x = 2, y = -1 verified! By addition and subtraction!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            9 -> GurukulScript(
                worldId = 9,
                title = "Tutorial 9: Shunyam Samyasamuccaye",
                subtitle = "Demonstration: 7(x+1) = 8(x+1) & 1/(x+2) + 1/(x+3) = 1/(x+1) + 1/(x+4)",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Family A! Look at 7(x+1) = 8(x+1). Spot the repeated common factor (x+1)!",
                        discipleText = "Common factor is (x+1)!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Since multipliers 7 ≠ 8, we equate the common factor to zero: x + 1 = 0 $\\rightarrow$ x = -1!",
                        discipleText = "x = -1! Equating common factor to zero!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Family B! Look at 1/(x+2) + 1/(x+3) = 1/(x+1) + 1/(x+4). Check denominator sums!",
                        discipleText = "LHS sum (x+2)+(x+3) = 2x+5. RHS sum (x+1)+(x+4) = 2x+5!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Denominator sums match (2x+5)! Equate S(x) = 0 $\\rightarrow$ 2x+5 = 0 $\\rightarrow$ x = -5/2! Verify domain: x ≠ -2,-3,-1,-4!",
                        discipleText = "x = -5/2 is valid! Denominators non-zero!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            10 -> GurukulScript(
                worldId = 10,
                title = "Tutorial 10: Puranapuranabhyam",
                subtitle = "Demonstration: x² + 6x + 8 = 0 & 2x² + 5x - 3 = 0",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Monic Completing the Square! Look at x² + 6x + 8 = 0. Step 1: Move constant 8 $\\rightarrow$ x² + 6x = -8.",
                        discipleText = "x² + 6x = -8!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Half of 6 is 3, 3² = 9! Add 9 to both sides $\\rightarrow$ x² + 6x + 9 = -8 + 9 = 1!",
                        discipleText = "Add 9 to both sides $\\rightarrow$ (x + 3)² = 1!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Solve square: x + 3 = ±1 $\\rightarrow$ x = -3 + 1 = -2, or x = -3 - 1 = -4!",
                        discipleText = "Roots are x = -2 and x = -4!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Non-Monic! 2x² + 5x - 3 = 0 $\\rightarrow$ divide by 2 $\\rightarrow$ x² + (5/2)x = 3/2. Add (5/4)² = 25/16 $\\rightarrow$ (x + 5/4)² = 49/16!",
                        discipleText = "Roots are x = 1/2 and x = -3! Verified!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            11 -> GurukulScript(
                worldId = 11,
                title = "Tutorial 11: Vyashtisamashtih",
                subtitle = "Demonstration: 58 × 62 = 3596 & 25 × 31 = 775",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Integer Symmetric Product! Look at 58 × 62. Step 1: Find average A = (58 + 62)/2 = 60.",
                        discipleText = "Average midpoint A = 60!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Find deviation d = (62 - 58)/2 = 2. Decompose into (60 - 2)(60 + 2).",
                        discipleText = "(60 - 2)(60 + 2)!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Difference of squares: 60² - 2² = 3600 - 4 = 3596!",
                        discipleText = "3600 - 4 = 3596!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Non-round average! 25 × 31 $\\rightarrow$ A = 28, d = 3 $\\rightarrow$ 28² - 3² = 784 - 9 = 775! Verified by direct product!",
                        discipleText = "775! Part and Whole symmetric multiplication verified!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            12 -> GurukulScript(
                worldId = 12,
                title = "Tutorial 12: Shesanyankena Charamena",
                subtitle = "Demonstration: 1/7 = 0.(142857) & 1/8 = 0.125",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Pure Recurring Cycle! Look at 1/7. Remainder sequence: 1 $\\rightarrow$ 3 $\\rightarrow$ 2 $\\rightarrow$ 6 $\\rightarrow$ 4 $\\rightarrow$ 5 $\\rightarrow$ 1!",
                        discipleText = "Remainder 1 returned! Cycle start detected!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Emitted digits for 1/7 form the repeating block: 1, 4, 2, 8, 5, 7 $\\rightarrow$ 0.(142857)!",
                        discipleText = "0.(142857)! Cycle length is 6!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Terminating Decimal! Look at 1/8. Remainders: 1 $\\rightarrow$ 2 $\\rightarrow$ 4 $\\rightarrow$ 0!",
                        discipleText = "Remainder reached 0! 1/8 = 0.125 terminates!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Verify: 1/7 = 0.(142857) and 1/8 = 0.125! Remainder-state tracking conquers recurring decimals!",
                        discipleText = "0.(142857) and 0.125 verified!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            13 -> GurukulScript(
                worldId = 13,
                title = "Tutorial 13: Sopantyadvayamantyam",
                subtitle = "Demonstration: 143 × 12 = 1716 & 124 × 14 = 1736",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Multiplier 12 (N=2)! Look at 143 × 12. Step 1: Sandwich 143 with zeros $\\rightarrow$ 0 1 4 3 0.",
                        discipleText = "Sandwiched sequence: 0, 1, 4, 3, 0!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 2: Raw values (digit + 2 × previous): 1+2(0)=1, 4+2(1)=6, 3+2(4)=11, 0+2(3)=6 $\\rightarrow$ [1, 6, 11, 6].",
                        discipleText = "Raw values: 1, 6, 11, 6!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Step 3: Propagate carries right-to-left: 6 $\\rightarrow$ 11 (write 1, carry 1) $\\rightarrow$ 6+1=7 $\\rightarrow$ 1 $\\rightarrow$ 1716!",
                        discipleText = "1716! Direct check: 143 × 12 = 1716!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Multiplier 14 (N=4)! 124 × 14 $\\rightarrow$ Sandwiched 0 1 2 4 0 $\\rightarrow$ Raw [1, 6, 12, 16] $\\rightarrow$ Carries $\\rightarrow$ 1736! Verified!",
                        discipleText = "1736! Ultimate & Penultimate multiplication verified!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            14 -> GurukulScript(
                worldId = 14,
                title = "Tutorial 14: Gunitasamuccayah",
                subtitle = "Demonstration: (x+3)(x+2) = x²+5x+6 & False Proposal (x+3)(x+2) ≠ x²+7x+4",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Valid Factorization! Look at (x+3)(x+2) = x²+5x+6. Evaluate at x=1: SC(factors) = (1+3)(1+2) = 12.",
                        discipleText = "Factor sum product = 4 × 3 = 12!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Evaluate claimed product sum at x=1: SC(x²+5x+6) = 1 + 5 + 6 = 12! Sum check passes 12 = 12!",
                        discipleText = "Product sum = 12! Sum check passes!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Now perform full term expansion: (x+3)(x+2) = x² + 5x + 6 == x² + 5x + 6! Coefficients match! VALID_FACTORISATION!",
                        discipleText = "Coefficients match! Factorization valid!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: False Proposal! Look at (x+3)(x+2) = x²+7x+4. SC(factors)=12, SC(claimed)=1+7+4=12. Sum check passes, but expansion [1,5,6] ≠ [1,7,4]! INVALID_EXPANSION!",
                        discipleText = "Sum check passed by coincidence, but term expansion failed! INVALID_EXPANSION!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            15 -> GurukulScript(
                worldId = 15,
                title = "Tutorial 15: Gunakasamuccayah",
                subtitle = "Demonstration: x² + 7x + 10 = 0 & x² + x - 6 = 0",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Positive Constant! Look at x² + 7x + 10 = 0. List factor pairs of C = 10: (1,10), (2,5).",
                        discipleText = "Pairs for 10 are (1,10) and (2,5)!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Test sums: 1+10 = 11 ≠ 7 (REJECTED!). Test 2+5 = 7 == B! Pair (2,5) MATCHES both product and sum!",
                        discipleText = "Pair (2,5) matches product 10 and sum 7!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Form factors: (x + 2)(x + 5) = 0. Set factors to zero: x = -2, x = -5! Verify: (-2)² + 7(-2) + 10 = 0!",
                        discipleText = "Roots are x = -2 and x = -5! Verified!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Negative Constant! x² + x - 6 = 0. Pairs of -6: (3,-2) sum is 1 == B! Factors (x+3)(x-2) = 0 $\\rightarrow$ roots -3, 2! Verified!",
                        discipleText = "Roots -3 and 2! Factor pair searching solves quadratics in seconds!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            16 -> GurukulScript(
                worldId = 16,
                title = "Tutorial 16: Chalana-Kalanabhyam",
                subtitle = "Demonstration: x² - 5x + 6 = 0 & 2x² - 2x - 12 = 0",
                beats = listOf(
                    GurukulDialogueBeat(
                        guruText = "Lesson 1: Monic Derivative Relation! Look at x² - 5x + 6 = 0. A=1, B=-5, C=6. First derivative: f'(x) = 2x - 5.",
                        discipleText = "f'(x) = 2x - 5!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.NEUTRAL,
                        slateStepIndex = 1
                    ),
                    GurukulDialogueBeat(
                        guruText = "Compute Discriminant: D = (-5)² - 4(1)(6) = 25 - 24 = 1. √D = 1! Set f'(x) = ±√D ⟹ 2x - 5 = ±1!",
                        discipleText = "2x - 5 = ±1!",
                        isTeachingStep = true,
                        guruPose = GuruPose.EXPLAINING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 2
                    ),
                    GurukulDialogueBeat(
                        guruText = "Plus branch: 2x - 5 = 1 ⟹ 2x = 6 ⟹ x = 3! Minus branch: 2x - 5 = -1 ⟹ 2x = 4 ⟹ x = 2!",
                        discipleText = "Roots are x = 3 and x = 2! Invariant f'(r)² = 1² = 1 = D verified!",
                        isTeachingStep = true,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.TALKING,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 3
                    ),
                    GurukulDialogueBeat(
                        guruText = "Lesson 2: Non-Monic! 2x² - 2x - 12 = 0. f'(x) = 4x - 2, D = 4 - 4(2)(-12) = 100 ⟹ √D = 10. 4x - 2 = ±10 ⟹ x = 3, x = -2! Verified!",
                        discipleText = "Roots 3 and -2! The derivative-discriminant invariant completes the 16 Sutra Saga!",
                        isTeachingStep = false,
                        guruPose = GuruPose.TALKING,
                        guruMouth = GuruMouth.SMILE,
                        discipleState = DiscipleState.UNDERSTANDING,
                        discipleEyes = DiscipleEyes.HAPPY,
                        slateStepIndex = 4
                    )
                )
            )
            else -> getTutorialScript(1)
        }
    }
}
