package com.ankh.sutrasaga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.components.VedicAppLogo
import com.ankh.sutrasaga.ui.components.VedicCornerBorder
import com.ankh.sutrasaga.ui.components.VedicThemeSwitcher
import com.ankh.sutrasaga.ui.components.YantraProgressRing
import com.ankh.sutrasaga.ui.components.YantraWatermark
import com.ankh.sutrasaga.ui.theme.VedicTheme

data class SutraModule(
    val id: String,
    val sanskritTitle: String,
    val englishTitle: String,
    val category: String,
    val estMinutes: Int,
    val progress: Float, // 0.0 to 1.0
    val worldNumber: Int
)

val SampleSutraModules = listOf(
    // 1. Multiplication Shortcuts
    SutraModule(
        id = "urdhva",
        sanskritTitle = "ऊर्ध्व तिर्यग्भ्याम्",
        englishTitle = "Vertically and Crosswise (2x2 Multiplication)",
        category = "Multiplication Shortcuts",
        estMinutes = 8,
        progress = 1.0f,
        worldNumber = 5
    ),
    SutraModule(
        id = "ekadhikena",
        sanskritTitle = "एकाधिकेन पूर्वेण",
        englishTitle = "By One More than the Previous One (Squaring 5s)",
        category = "Multiplication Shortcuts",
        estMinutes = 5,
        progress = 0.85f,
        worldNumber = 1
    ),
    SutraModule(
        id = "nikhilam",
        sanskritTitle = "निखिलं नवतश्चरमं दशतः",
        englishTitle = "All from 9 and Last from 10 (Base Subtraction)",
        category = "Multiplication Shortcuts",
        estMinutes = 6,
        progress = 0.50f,
        worldNumber = 2
    ),
    SutraModule(
        id = "ekanyunena",
        sanskritTitle = "एकन्यूनेन पूर्वेण",
        englishTitle = "By One Less than the Previous One (Multiplication by 9s)",
        category = "Multiplication Shortcuts",
        estMinutes = 5,
        progress = 0.0f,
        worldNumber = 3
    ),

    // 2. Squaring & Cubing
    SutraModule(
        id = "yavadunam",
        sanskritTitle = "यावदूनम्",
        englishTitle = "Lessen by Deficiency (Deficiency Squaring near base)",
        category = "Squaring & Cubing",
        estMinutes = 7,
        progress = 0.70f,
        worldNumber = 4
    ),
    SutraModule(
        id = "anurupyena_square",
        sanskritTitle = "आनुरूप्येण शून्यमन्यत्",
        englishTitle = "Proportionately (Working Sub-Base Squaring & Ratios)",
        category = "Squaring & Cubing",
        estMinutes = 6,
        progress = 0.0f,
        worldNumber = 7
    ),
    SutraModule(
        id = "puranapuranabhyam",
        sanskritTitle = "पूरणापूरणाभ्याम्",
        englishTitle = "By the Completion or Non-Completion",
        category = "Squaring & Cubing",
        estMinutes = 8,
        progress = 0.0f,
        worldNumber = 10
    ),

    // 3. Division & Reciprocals
    SutraModule(
        id = "paravartya",
        sanskritTitle = "परावर्त्य योजयेत्",
        englishTitle = "Transpose and Apply (Synthetic Polynomial Division)",
        category = "Division & Reciprocals",
        estMinutes = 9,
        progress = 0.30f,
        worldNumber = 6
    ),
    SutraModule(
        id = "shesanyankena",
        sanskritTitle = "शेषाण्यङ्केन चरमेण",
        englishTitle = "The Remainders by the Last Digit (Decimal Expansions)",
        category = "Division & Reciprocals",
        estMinutes = 7,
        progress = 0.0f,
        worldNumber = 12
    ),
    SutraModule(
        id = "chalana",
        sanskritTitle = "चलनकलनाभ्याम्",
        englishTitle = "Differential Calculus & Residue Operations",
        category = "Division & Reciprocals",
        estMinutes = 10,
        progress = 0.0f,
        worldNumber = 16
    ),

    // 4. Algebra & Linear Systems
    SutraModule(
        id = "sankalana",
        sanskritTitle = "संकलनव्यवकलनाभ्याम्",
        englishTitle = "By Addition and by Subtraction (Simultaneous Equations)",
        category = "Algebra & Linear Systems",
        estMinutes = 8,
        progress = 0.0f,
        worldNumber = 8
    ),
    SutraModule(
        id = "shunyam",
        sanskritTitle = "शून्यं साम्यसमुच्चये",
        englishTitle = "When the Collection is Equal, it is Zero",
        category = "Algebra & Linear Systems",
        estMinutes = 7,
        progress = 0.0f,
        worldNumber = 9
    ),
    SutraModule(
        id = "sopantyadvayamantyam",
        sanskritTitle = "सोपान्त्यद्वयमन्त्यम्",
        englishTitle = "The Ultimate and Twice the Penultimate",
        category = "Algebra & Linear Systems",
        estMinutes = 6,
        progress = 0.0f,
        worldNumber = 13
    ),

    // 5. Factorization & Verification
    SutraModule(
        id = "vyashtisamashtih",
        sanskritTitle = "व्यष्टिसमष्टिः",
        englishTitle = "Specific and General (Biquadratic Factorization)",
        category = "Factorization & Verification",
        estMinutes = 8,
        progress = 0.0f,
        worldNumber = 11
    ),
    SutraModule(
        id = "gunitasamuccayah",
        sanskritTitle = "गुणितसमुच्चयः",
        englishTitle = "Product of the Sum is the Sum of the Products",
        category = "Factorization & Verification",
        estMinutes = 7,
        progress = 0.0f,
        worldNumber = 14
    ),
    SutraModule(
        id = "gunakasamuccayah",
        sanskritTitle = "गुणकसमुच्चयः",
        englishTitle = "The Factor of the Sum is the Sum of the Factors",
        category = "Factorization & Verification",
        estMinutes = 7,
        progress = 0.0f,
        worldNumber = 15
    )
)

/**
 * HomeScreen — Primary Launch Portal & Dashboard
 */
@Composable
fun HomeScreen(
    streakDays: Int = 7,
    modules: List<SutraModule> = SampleSutraModules,
    onSelectSutra: (SutraModule) -> Unit,
    onStartPracticeArena: () -> Unit,
    onOpenTreasury: () -> Unit = {}
) {
    val colors = VedicTheme.colors
    val typography = VedicTheme.typography
    val radii = VedicTheme.radii

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        // Subtle background Yantra watermark
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            YantraWatermark(size = 380.dp, opacity = 0.035f)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Header Row: Streak Banner & Theme Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glowing Gold Streak Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radii.pill))
                        .background(colors.surfaceCard)
                        .border(
                            1.dp,
                            if (colors.isDark) colors.secondaryGold.copy(alpha = 0.35f) else colors.borderSubtle,
                            RoundedCornerShape(radii.pill)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⭐", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$streakDays Day Sutra Streak",
                        style = typography.badge,
                        color = colors.secondaryGold
                    )
                }

                // Theme Switcher Toggle
                VedicThemeSwitcher()
            }

            // 2. Hero Header with App Logo & Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VedicAppLogo(size = 54.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Vedic Mathematics",
                        style = typography.h1.copy(fontSize = 22.sp),
                        color = if (colors.isDark) colors.secondaryGold else colors.primarySaffron
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Sacred Geometry & Mental Math Engine",
                        style = typography.subtitle.copy(fontSize = 12.sp),
                        color = colors.textSecondary
                    )
                }
            }

            // 3. Upa-Sutra Treasury & Codex Access Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { onOpenTreasury() },
                shape = RoundedCornerShape(radii.card),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏛️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Upa-Sutra Treasury & Codex",
                                style = typography.h2.copy(fontSize = 14.sp),
                                color = colors.textPrimary
                            )
                            Text(
                                text = "13 Ancient Sub-Sutras, Quests & Worked Rules",
                                style = typography.subtitle.copy(fontSize = 11.sp),
                                color = colors.textSecondary
                            )
                        }
                    }
                    Text(
                        text = "Explore →",
                        style = typography.badge.copy(fontSize = 12.sp),
                        color = colors.secondaryGold
                    )
                }
            }

            // 4. Speed Practice Hero CTA Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .clickable { onStartPracticeArena() },
                shape = RoundedCornerShape(radii.card),
                colors = CardDefaults.cardColors(containerColor = colors.primarySaffron),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "⚡ SPEED PRACTICE ARENA",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp,
                            color = if (colors.isDark) Color(0xFF0F172A) else Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Test calculation speed with timed mental drills",
                            fontSize = 12.sp,
                            color = if (colors.isDark) Color(0xFF334155) else Color(0xFFFFE0B2)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (colors.isDark) Color(0xFF0F172A) else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start Practice",
                            tint = colors.primarySaffron
                        )
                    }
                }
            }

            // 5. Categorized Sutra Module Grid
            val categories = modules.groupBy { it.category }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                categories.forEach { (categoryName, categoryModules) ->
                    item {
                        Text(
                            text = categoryName.uppercase(),
                            style = typography.badge.copy(fontSize = 11.sp, letterSpacing = 1.sp),
                            color = if (colors.isDark) colors.secondaryGold else colors.primarySaffron,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    items(categoryModules) { module ->
                        SutraModuleCard(
                            module = module,
                            onClick = { onSelectSutra(module) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * SutraModuleCard — Beautiful Modernized Card with Embedded YantraProgressRing
 */
@Composable
fun SutraModuleCard(
    module: SutraModule,
    onClick: () -> Unit
) {
    val colors = VedicTheme.colors
    val typography = VedicTheme.typography
    val radii = VedicTheme.radii
    val isComplete = module.progress >= 1.0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(radii.card),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isComplete) colors.statusSuccess.copy(alpha = 0.35f) else colors.borderSubtle
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            VedicCornerBorder(cornerLength = 12.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Details Column
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = module.sanskritTitle,
                        style = typography.h2.copy(fontSize = 16.sp),
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = module.englishTitle,
                        style = typography.bodyText.copy(fontSize = 12.sp),
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⏱ ${module.estMinutes} min",
                            style = typography.subtitle.copy(fontSize = 11.sp),
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "• World ${module.worldNumber}",
                            style = typography.subtitle.copy(fontSize = 11.sp),
                            color = colors.secondaryGold
                        )
                    }
                }

                // Right Progress Visualizer: YantraProgressRing
                YantraProgressRing(
                    progress = module.progress,
                    size = 54.dp,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}
