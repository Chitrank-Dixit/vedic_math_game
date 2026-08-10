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
            else -> getTutorialScript(1)
        }
    }
}
