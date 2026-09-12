/**
 * Vedic Mathematics Design System Tokens
 * Framework-agnostic Design Tokens (TypeScript / JSON)
 * Supporting Light Mode (Bhojpatra Parchment) & Dark Mode (Cosmic Midnight)
 */

export const VedicTokens = {
  colors: {
    light: {
      backgroundPrimary: "#FDFBF7", // Soft parchment base
      surfaceCard: "#FFFFFF",        // Elevated card background
      primarySaffron: "#E65100",     // Focus, energy, key interactive elements
      secondaryGold: "#D4AF37",      // Wisdom, badges, streaks
      textPrimary: "#212121",        // Charcoal ink for high readability
      textSecondary: "#616161",      // Muted step descriptions
      borderSubtle: "rgba(212, 175, 55, 0.2)", // Golden micro-borders
      statusSuccess: "#2E7D32",      // Tulsi green for correct math inputs
      statusError: "#C62828",        // Crimson red for errors
      shadowAmbient: "0px 4px 12px rgba(0, 0, 0, 0.05)",
      glowSubtle: "0px 0px 12px rgba(212, 175, 55, 0.1)"
    },
    dark: {
      backgroundPrimary: "#0F172A", // Deep cosmic navy base
      surfaceCard: "#1E293B",        // Elevated slate card background
      primarySaffron: "#FF9800",     // Vibrant saffron glow
      secondaryGold: "#FFD700",      // Bright gold accent
      textPrimary: "#F8FAFC",        // Pure starlight white
      textSecondary: "#94A3B8",      // Soft muted slate
      borderSubtle: "rgba(255, 215, 0, 0.25)", // Glowing golden border
      statusSuccess: "#4ADE80",      // Emerald green for correct math inputs
      statusError: "#EF4444",        // Vibrant red for errors
      shadowAmbient: "0px 4px 16px rgba(0, 0, 0, 0.4)",
      glowSubtle: "0px 0px 12px rgba(255, 215, 0, 0.08)"
    }
  },
  typography: {
    classicalSerif: {
      fontFamily: "'Cinzel', 'Rozha One', 'Noto Serif Devanagari', serif",
      h1: {
        fontSize: "28px",
        fontWeight: 700,
        lineHeight: "34px"
      },
      h2: {
        fontSize: "22px",
        fontWeight: 600,
        lineHeight: "28px"
      },
      subtitle: {
        fontSize: "14px",
        fontStyle: "italic",
        lineHeight: "20px"
      }
    },
    tabularMonospace: {
      fontFamily: "'JetBrains Mono', 'Inter', monospace",
      fontFeatureSettings: "'tnum' on",
      mathDisplay: {
        fontSize: "36px",
        fontWeight: 700,
        letterSpacing: "1px",
        fontFeatureSettings: "'tnum' on"
      },
      mathStep: {
        fontSize: "20px",
        fontWeight: 500,
        fontFeatureSettings: "'tnum' on"
      },
      bodyText: {
        fontSize: "16px",
        fontWeight: 400,
        lineHeight: "24px"
      }
    }
  },
  spacing: {
    xs: "4px",
    sm: "8px",
    md: "16px",
    lg: "24px",
    xl: "32px"
  },
  borderRadius: {
    card: "12px",
    button: "8px",
    pill: "999px"
  }
} as const;

export type VedicTokensType = typeof VedicTokens;
