package com.ankh.sutrasaga.domain.models

/**
 * Linear bivariate factor of the form (px*x + py*y + constant).
 */
data class LinearBivariateFactor(
    val px: Long,
    val py: Long,
    val constant: Long
) {
    fun formatDisplay(): String {
        val parts = mutableListOf<String>()

        if (px != 0L) {
            when (px) {
                1L -> parts.add("x")
                -1L -> parts.add("-x")
                else -> parts.add("${px}x")
            }
        }

        if (py != 0L) {
            if (parts.isEmpty()) {
                when (py) {
                    1L -> parts.add("y")
                    -1L -> parts.add("-y")
                    else -> parts.add("${py}y")
                }
            } else {
                when {
                    py == 1L -> parts.add("+ y")
                    py == -1L -> parts.add("- y")
                    py > 0L -> parts.add("+ ${py}y")
                    else -> parts.add("- ${-py}y")
                }
            }
        }

        if (constant != 0L) {
            if (parts.isEmpty()) {
                parts.add("$constant")
            } else {
                if (constant > 0L) parts.add("+ $constant") else parts.add("- ${-constant}")
            }
        }

        val inner = parts.joinToString(" ")
        return "($inner)"
    }
}

/**
 * Bivariate quadratic polynomial of the form:
 * A*x² + B*x*y + C*y² + D*x + E*y + F
 */
data class BivariateQuadratic(
    val a: Long,
    val b: Long,
    val c: Long,
    val d: Long,
    val e: Long,
    val f: Long
) {
    fun formatDisplay(): String {
        val terms = mutableListOf<String>()

        fun addTerm(coeff: Long, suffix: String) {
            if (coeff == 0L) return
            if (terms.isEmpty()) {
                when {
                    suffix.isEmpty() -> terms.add("$coeff")
                    coeff == 1L -> terms.add(suffix)
                    coeff == -1L -> terms.add("-$suffix")
                    else -> terms.add("$coeff$suffix")
                }
            } else {
                val absVal = kotlin.math.abs(coeff)
                val sign = if (coeff > 0) "+" else "-"
                when {
                    suffix.isEmpty() -> terms.add("$sign $absVal")
                    absVal == 1L -> terms.add("$sign $suffix")
                    else -> terms.add("$sign $absVal$suffix")
                }
            }
        }

        addTerm(a, "x²")
        addTerm(b, "xy")
        addTerm(c, "y²")
        addTerm(d, "x")
        addTerm(e, "y")
        addTerm(f, "")

        return if (terms.isEmpty()) "0" else terms.joinToString(" ")
    }
}

/**
 * Classification states for Lopanasthapanabhyam factorizations.
 */
enum class LopanaClassification {
    FACTORED,
    AMBIGUOUS_RECOMBINATION,
    UNSUPPORTED_NONFACTORIZABLE,
    OVERFLOW_RISK
}

/**
 * Step in the elimination, retention, and expansion verification process.
 */
data class LopanaStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution for Lopanasthapanabhyam.
 */
data class LopanaSolution(
    val target: BivariateQuadratic,
    val factor1: LinearBivariateFactor,
    val factor2: LinearBivariateFactor,
    val eliminatedYPolynomial: String,
    val eliminatedXPolynomial: String,
    val rejectedCandidate: String? = null,
    val steps: List<LopanaStep>,
    val classification: LopanaClassification
) {
    val factoredFormDisplay: String
        get() = "${factor1.formatDisplay()}${factor2.formatDisplay()}"
}
