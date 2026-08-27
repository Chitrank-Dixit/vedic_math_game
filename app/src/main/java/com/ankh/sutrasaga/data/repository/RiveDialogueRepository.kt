package com.ankh.sutrasaga.data.repository

import android.content.Context
import com.ankh.sutrasaga.domain.models.AudioCues
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.MathOverlay
import com.ankh.sutrasaga.domain.models.RiveCharacterState
import com.ankh.sutrasaga.domain.models.RiveDialogueNode
import com.ankh.sutrasaga.domain.models.RiveEmotion
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import org.json.JSONArray
import org.json.JSONObject

/**
 * Repository responsible for loading and querying the 16 Canonical Vedic Math Rive Dialogue Trees.
 */
class RiveDialogueRepository(private val context: Context? = null) {

    private val cachedTrees: MutableMap<Int, RiveSutraDialogueTree> = mutableMapOf()

    init {
        context?.let { ctx ->
            loadFromAssets(ctx)
        }
    }

    fun loadFromAssets(ctx: Context) {
        try {
            val jsonString = ctx.assets.open("dialogue/rive_sutra_dialogues.json")
                .bufferedReader()
                .use { it.readText() }
            parseJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parseJson(jsonContent: String): List<RiveSutraDialogueTree> {
        val trees = mutableListOf<RiveSutraDialogueTree>()
        val jsonArray = JSONArray(jsonContent)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val tree = parseTree(obj)
            trees.add(tree)
            cachedTrees[tree.worldNumber] = tree
        }
        return trees
    }

    private fun parseTree(obj: JSONObject): RiveSutraDialogueTree {
        val sutraId = obj.getString("sutra_id")
        val sutraName = obj.getString("sutra_name")
        val englishMeaning = obj.getString("english_meaning")
        val worldNumber = obj.getInt("world_number")

        val nodesArray = obj.getJSONArray("dialogue_nodes")
        val nodes = mutableListOf<RiveDialogueNode>()

        for (j in 0 until nodesArray.length()) {
            val nodeObj = nodesArray.getJSONObject(j)
            val nodeId = nodeObj.getInt("node_id")
            val speakerStr = nodeObj.getString("speaker")
            val speaker = if (speakerStr.equals("GURU", ignoreCase = true)) RiveSpeaker.GURU else RiveSpeaker.SHISHYA
            val text = nodeObj.getString("text")

            val riveStateObj = nodeObj.getJSONObject("rive_state")
            val riveCharStr = riveStateObj.getString("character")
            val riveChar = if (riveCharStr.equals("GURU", ignoreCase = true)) RiveSpeaker.GURU else RiveSpeaker.SHISHYA
            val animation = riveStateObj.getString("animation")
            val emotionStr = riveStateObj.getString("emotion")
            val emotion = when (emotionStr.lowercase()) {
                "happy" -> RiveEmotion.HAPPY
                "thinking" -> RiveEmotion.THINKING
                "surprised" -> RiveEmotion.SURPRISED
                else -> RiveEmotion.NEUTRAL
            }
            val isSpeaking = riveStateObj.getBoolean("is_speaking")

            val riveState = RiveCharacterState(
                character = riveChar,
                animation = animation,
                emotion = emotion,
                isSpeaking = isSpeaking
            )

            val mathOverlayObj = nodeObj.getJSONObject("math_overlay")
            val expression = mathOverlayObj.getString("expression")
            val tokensArray = mathOverlayObj.optJSONArray("highlight_tokens")
            val tokens = mutableListOf<String>()
            if (tokensArray != null) {
                for (k in 0 until tokensArray.length()) {
                    tokens.add(tokensArray.getString(k))
                }
            }
            val mathOverlay = MathOverlay(expression = expression, highlightTokens = tokens)

            val audioCuesObj = nodeObj.getJSONObject("audio_cues")
            val ssmlText = audioCuesObj.getString("ssml_text")
            val sfx = audioCuesObj.getString("sfx")
            val audioCues = AudioCues(ssmlText = ssmlText, sfx = sfx)

            val handshakeObj = nodeObj.getJSONObject("interactive_handshake")
            val requiresTap = handshakeObj.getBoolean("requires_user_tap")
            val targetElementId = if (handshakeObj.isNull("target_element_id")) null else handshakeObj.getString("target_element_id")
            val promptText = if (handshakeObj.isNull("prompt_text")) null else handshakeObj.getString("prompt_text")
            val interactiveHandshake = InteractiveHandshake(
                requiresUserTap = requiresTap,
                targetElementId = targetElementId,
                promptText = promptText
            )

            nodes.add(
                RiveDialogueNode(
                    nodeId = nodeId,
                    speaker = speaker,
                    text = text,
                    riveState = riveState,
                    mathOverlay = mathOverlay,
                    audioCues = audioCues,
                    interactiveHandshake = interactiveHandshake
                )
            )
        }

        return RiveSutraDialogueTree(
            sutraId = sutraId,
            sutraName = sutraName,
            englishMeaning = englishMeaning,
            worldNumber = worldNumber,
            dialogueNodes = nodes
        )
    }

    fun getDialogueForWorld(worldNumber: Int): RiveSutraDialogueTree? {
        return cachedTrees[worldNumber]
    }

    fun getAllDialogues(): List<RiveSutraDialogueTree> {
        return cachedTrees.values.toList()
    }
}
