package com.ankh.sutrasaga.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

/**
 * Reusable full-body layered character composable.
 * Composites a base pose image with an optional face/expression overlay (mouth or eyes).
 */
@Composable
fun LayeredCharacter(
    @DrawableRes basePoseResId: Int,
    @DrawableRes overlayResId: Int?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // 1. Base Pose Layer
        // TODO: replace with final art — guru_base_pose.png / disciple_neutral.png
        Crossfade(targetState = basePoseResId, label = "basePoseCrossfade") { resId ->
            Image(
                painter = painterResource(id = resId),
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // 2. Expression Overlay Layer (Mouth or Eyes composited ON TOP)
        // TODO: replace with final art — guru_mouth_talking.png / disciple_eyes_happy.png
        overlayResId?.let { resId ->
            Crossfade(targetState = resId, label = "overlayCrossfade") { overlayId ->
                Image(
                    painter = painterResource(id = overlayId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
