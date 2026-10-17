package com.ankh.sutrasaga.ui.util

/**
 * MathFormatter — Utility to convert raw LaTeX or algebraic expressions into clean, elegant Unicode math.
 */
object MathFormatter {

    fun format(expression: String): String {
        var text = expression

        // 1. Remove LaTeX text wrappers: \text{...} -> ...
        text = text.replace(Regex("""\\text\{([^}]*)\}""")) { it.groupValues[1] }

        // 2. Fractions: \frac{a}{b} -> (a) / (b)
        text = text.replace(Regex("""\\frac\{([^}]*)\}\{([^}]*)\}""")) {
            val num = it.groupValues[1]
            val den = it.groupValues[2]
            if (num.length <= 3 && den.length <= 3) "$num/$den" else "($num) / ($den)"
        }

        // 3. Square roots: \sqrt{a} -> √a
        text = text.replace(Regex("""\\sqrt\{([^}]*)\}""")) {
            "√${it.groupValues[1]}"
        }

        // 4. Exponents / Superscripts: ^2 -> ², ^3 -> ³, ^4 -> ⁴, ^x -> ˣ
        text = text.replace("^2", "²")
            .replace("^3", "³")
            .replace("^4", "⁴")
            .replace("^x", "ˣ")

        // 5. LaTeX symbols
        text = text.replace("\\times", "×")
            .replace("\\cdot", "·")
            .replace("\\implies", "⟹")
            .replace("\\to", "→")
            .replace("\\in", "∈")
            .replace("\\pm", "±")
            .replace("\\pmod", "mod")
            .replace("\\stackrel{?}{=}", "≟")
            .replace("\\overline{", "")
            .replace("\\begin{cases}", "")
            .replace("\\end{cases}", "")
            .replace("\\\\", ", ")
            .replace("\\;", " ")
            .replace("\\quad", "   ")
            .replace("\\", "")

        // 6. Clean up subscript artifacts like S_c -> Sc, S_L -> S_L
        text = text.replace("S_c", "Sc")
            .replace("S_L", "SL")
            .replace("S_R", "SR")
            .replace("x_{\\text{extrema}}", "x(turning)")
            .replace("x_{extrema}", "x(turning)")

        // 7. Normalize extra whitespace
        text = text.replace(Regex("""\s+"""), " ").trim()

        return text
    }
}
