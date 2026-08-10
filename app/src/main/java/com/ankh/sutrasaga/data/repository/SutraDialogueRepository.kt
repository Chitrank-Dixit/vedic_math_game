package com.ankh.sutrasaga.data.repository

import com.ankh.sutrasaga.domain.models.DialoguePanel
import com.ankh.sutrasaga.domain.models.DialogueScript
import com.ankh.sutrasaga.domain.models.Expression
import com.ankh.sutrasaga.domain.models.Speaker

object SutraDialogueRepository {

    fun getStoryBeatScript(worldId: Int): DialogueScript {
        return when (worldId) {
            1 -> DialogueScript(
                worldId = 1,
                title = "World 1: The Fateful Five",
                subtitle = "Meeting Master Purva",
                panels = listOf(
                    DialoguePanel(
                        speaker = Speaker.NARRATOR,
                        expression = Expression.NEUTRAL,
                        text = "Welcome to Math-Loka! The Number Demons stripped the world of mental shortcuts. Armed only with your wits, you must find the 16 Sutra-Masters."
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.CONFUSED,
                        text = "Wait, we have to do mental arithmetic without battery power?! My solar panel is crying!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.EXCITED,
                        speakerNameOverride = "Master Purva",
                        text = "Halt! I am Master Purva! Defender of the High-Fives, Protector of numbers ending in 5!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.NEUTRAL,
                        text = "Great, a guy who refuses to count to six. Why are you staring so intently at 65²?"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.PROUD,
                        speakerNameOverride = "Master Purva",
                        text = "Because squaring 65 takes 2 seconds if you know my secret! Let me show you!"
                    )
                )
            )
            2 -> DialogueScript(
                worldId = 2,
                title = "World 2: Base Camp 1000",
                subtitle = "Meeting Master Nikhil",
                panels = listOf(
                    DialoguePanel(
                        speaker = Speaker.NARRATOR,
                        expression = Expression.NEUTRAL,
                        text = "High in the Base Mountains, giant powers of 10 float in the sky. Borrowing digits here is forbidden!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.CONFUSED,
                        text = "Forbidden to borrow? How am I supposed to subtract 3468 from 10000 without carrying numbers?!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.EXCITED,
                        speakerNameOverride = "Master Nikhil",
                        text = "Carrying is for mortals! I am Master Nikhil. I solve subtraction in one line from left to right!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.NEUTRAL,
                        text = "Left to right?! That violates every elementary school textbook ever printed."
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.PROUD,
                        speakerNameOverride = "Master Nikhil",
                        text = "Observe my sacred law: 'All from 9 and the last from 10'! Watch digits melt away!"
                    )
                )
            )
            3 -> DialogueScript(
                worldId = 3,
                title = "World 3: The Nines Nebula",
                subtitle = "Meeting Master Ekanyuna",
                panels = listOf(
                    DialoguePanel(
                        speaker = Speaker.NARRATOR,
                        expression = Expression.NEUTRAL,
                        text = "Deep inside the Nines Nebula, fields of 9s, 99s, and 999s block your path."
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.CONFUSED,
                        text = "Multiplying 743 by 999?! My floating point processor just overheated!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.CONFUSED,
                        speakerNameOverride = "Master Ekanyuna",
                        text = "Ugh, the number 9... It's just an upside-down 6 with an attitude problem!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.EXCITED,
                        text = "You hate 9 too? Then how do we defeat 743 × 999?"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.PROUD,
                        speakerNameOverride = "Master Ekanyuna",
                        text = "By subtracting 1! My sutra 'Ekanyunena Purvena' turns 9s into instant answers!"
                    )
                )
            )
            else -> getStoryBeatScript(1)
        }
    }

    fun getTutorialScript(worldId: Int): DialogueScript {
        return when (worldId) {
            1 -> DialogueScript(
                worldId = 1,
                title = "Tutorial 1: Ekadhikena Purvena",
                subtitle = "Squaring numbers ending in 5",
                panels = listOf(
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.NEUTRAL,
                        speakerNameOverride = "Master Purva",
                        text = "Behold 65²! First, ignore the 5 and take the remaining prefix digit 6."
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.CONFUSED,
                        text = "Ignore 5? But I thought you loved 5! So prefix n = 6?"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.EXCITED,
                        speakerNameOverride = "Master Purva",
                        text = "Apply Ekadhikena Purvena ('One more than previous'): Multiply 6 by (6 + 1 = 7) to get 42!",
                        highlightMathToken = "42"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.PROUD,
                        text = "6 × 7 = 42... Hey, that's easy! But what about the 5 we ignored?",
                        highlightMathToken = "42"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.PROUD,
                        speakerNameOverride = "Master Purva",
                        text = "Simply append 25 (which is 5²) to 42 to get 4225! So 65² = 4225! Boom!",
                        highlightMathToken = "4225"
                    )
                )
            )
            2 -> DialogueScript(
                worldId = 2,
                title = "Tutorial 2: Nikhilam Navatashcaramam Dashatah",
                subtitle = "All from 9 and last from 10",
                panels = listOf(
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.NEUTRAL,
                        speakerNameOverride = "Master Nikhil",
                        text = "Let's subtract 3468 from 10000! Rule: Subtract every digit from 9, except the last non-zero digit which you subtract from 10."
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.CONFUSED,
                        text = "So for 3, 4, and 6 we subtract from 9? Let me calculate: 9-3=6, 9-4=5, 9-6=3!",
                        highlightMathToken = "653"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.EXCITED,
                        speakerNameOverride = "Master Nikhil",
                        text = "Correct! The first digits give 653! Now apply 'last from 10' to the final digit 8: (10 - 8 = 2)!",
                        highlightMathToken = "2"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.PROUD,
                        text = "Last digit is 2! So putting them together gives 6532?",
                        highlightMathToken = "6532"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.PROUD,
                        speakerNameOverride = "Master Nikhil",
                        text = "Exact! 10000 - 3468 = 6532! Zero borrowing, zero stress!",
                        highlightMathToken = "6532"
                    )
                )
            )
            3 -> DialogueScript(
                worldId = 3,
                title = "Tutorial 3: Ekanyunena Purvena",
                subtitle = "Multiplication by 9s",
                panels = listOf(
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.NEUTRAL,
                        speakerNameOverride = "Master Ekanyuna",
                        text = "To compute 743 × 999: Step 1 is 'Ekanyunena' — subtract 1 from 743!"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.PROUD,
                        text = "743 - 1 = 742! That's the left half of our answer?",
                        highlightMathToken = "742"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.EXCITED,
                        speakerNameOverride = "Master Ekanyuna",
                        text = "Exactly! 742 is the Left side. Step 2: Apply Nikhilam (all from 9) to 742 to get the Right side!",
                        highlightMathToken = "742"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SIDEKICK,
                        expression = Expression.CONFUSED,
                        text = "9-7=2, 9-4=5, 9-2=7... Right side is 257!",
                        highlightMathToken = "257"
                    ),
                    DialoguePanel(
                        speaker = Speaker.SUTRA_MASTER,
                        expression = Expression.PROUD,
                        speakerNameOverride = "Master Ekanyuna",
                        text = "Combine Left (742) and Right (257) to get 742257! 743 × 999 = 742257!",
                        highlightMathToken = "742257"
                    )
                )
            )
            else -> getTutorialScript(1)
        }
    }
}
