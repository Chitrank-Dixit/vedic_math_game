# Usability Testing Protocol: Child Comprehension & Tutorial Accessibility (Ages 5–10)

## 1. Executive Summary & Purpose

This protocol establishes a standardized, child-centered methodology for validating Vedic Mathematics tutorials and interactive handshakes with learners aged 5–10. 

### What Automated Tests Can and Cannot Establish
- **What Automated Tests DO Establish**:
  - Syntactic and schema validity (SSML structure, audio cue tags, Rive character states).
  - Screen reader semantic descriptions and touch-target minimums (WCAG 2.5.5 compliance).
  - Absence of unintroduced jargon, mathematical formula consistency with slate tokens.
  - Fail-closed evaluation of interactive handshakes and verified arithmetic results.
- **What Automated Tests CANNOT Establish**:
  - Whether a 7-year-old child comprehends the spatial logic of crosswise multiplication.
  - Whether an emerging reader stumbles over multi-syllable phrases despite a passing Flesch-Kincaid score.
  - Whether visual motion or character animation distracts from mathematical working memory.
  - Whether a child is guessing answers due to cognitive overload or genuinely applying the sutra shortcut.

*Note: This document represents an empirical testing plan and evaluation protocol. It does not claim that human usability sessions have already been completed.*

---

## 2. Participant Stratification: Reading vs. Mathematical Readiness

To prevent misattributing reading difficulties to mathematical conceptual failure, participants must be screened and categorized along two independent dimensions:

### 2.1 Age and Developmental Cohorts
1. **Early Explorers (Ages 5–7)**:
   - *Reading Profile*: Emerging readers; relying heavily on spoken audio, visual icons, and speech bubble highlights.
   - *Mathematical Readiness*: Solid single-digit number sense, counting, basic addition/subtraction. May not have formal multiplication background.
   - *Focus Worlds*: World 1 (Ekadhikena pattern recognition), World 2 (Nikhilam base subtraction), World 3 (Ekanyunena subtraction patterns).
2. **Young Mathematicians (Ages 8–10)**:
   - *Reading Profile*: Independent readers; capable of reading dialogue text silently or aloud.
   - *Mathematical Readiness*: Familiar with standard multiplication tables (up to $10 \times 10$) and column addition.
   - *Focus Worlds*: World 1–5 (MVP core including Urdhva-Tiryagbhyam 2x2) and advanced worlds (Paravartya, Anurupye).

### 2.2 Pre-Session Intake Matrix (Observer Use Only)
| Dimension | Level 1 (Emerging) | Level 2 (Developing) | Level 3 (Proficient) |
| :--- | :--- | :--- | :--- |
| **Reading Ability** | Needs text read aloud; tracks words with finger | Reads short sentences with guidance | Reads dialogue fluently and unprompted |
| **Math Readiness** | Can add/subtract single digits using counters | Knows $2\times$, $5\times$, $10\times$ tables; understands base 10 | Knows multi-digit addition; understands place value |

---

## 3. Ethics, Guardian Consent, and Child Privacy (COPPA / GDPR-K)

1. **Informed Guardian Consent**:
   - Guardians receive a plain-language summary of session goals, duration (20–25 minutes max), and voluntary participation.
   - Explicit written consent required before observation.
2. **Child Assent**:
   - Before beginning, the facilitator explains in child-friendly language:
     > *"We are testing this math game to see if our instructions are fun and clear. You are helping us make the game better. There are no tests, grades, or wrong answers for you, and we can stop anytime you want."*
3. **Privacy & Data Protection**:
   - Zero Personally Identifiable Information (PII) is stored or logged.
   - Participants are assigned randomized identifiers (e.g., `P-05A`, `P-08B`).
   - Audio/video recordings (if permitted by guardian) are restricted to screen capture with over-the-shoulder hand view; facial recognition or biometric indexing is prohibited.
   - No participant data is sent to external cloud analytics or AI model training pipelines.

---

## 4. Four-Stage Comprehension Testing Tasks

Sessions last 20 to 25 minutes to prevent cognitive fatigue.

### Task 1: Orientation & Character Dynamic (Minutes 0–5)
- **Objective**: Determine if the learner recognizes the Gurukul dialogue hierarchy (Guru explaining, Shishya asking questions).
- **Facilitator Prompt**: *"Who do you think these two characters are? What do you think they are talking about?"*
- **Checklist**:
  - [ ] Child identifies Guru as teacher and Shishya as student.
  - [ ] Child looks at the wooden chalkboard when mathematical expressions appear.

### Task 2: Step-by-Step Explanation & Non-Audio Comprehension (Minutes 5–12)
- **Objective**: Assess whether the child follows intermediate calculation steps (e.g., units vertical, crosswise sum, carry).
- **Method**: Facilitator plays the demonstration with sound, and on a subsequent step with sound muted (testing non-audio visual accessibility).
- **Facilitator Prompt**: *"Can you show me on the slate which numbers Guru-ji just multiplied?"*
- **Checklist**:
  - [ ] Child connects spoken/written step ("3 × 4 = 12") to the highlighted slate token.
  - [ ] Child understands carry notation (`12 (write 2, carry 1)`).

### Task 3: Interactive Handshake ("Let's Try It!") (Minutes 12–18)
- **Objective**: Determine whether the child can independently solve the interactive puzzle.
- **Observation Focus**:
  - Does the child read the prompt ("What is (3 × 1) + (1 × 2)?")?
  - Does the child calculate before tapping, or immediately tap random tiles?
  - When an incorrect tile is tapped, does the child read the corrective hint banner?

### Task 4: Near-Transfer Slate Challenge (Minutes 18–22)
- **Objective**: Test whether the learner retained the principle rather than memorized the specific screen answer.
- **Facilitator Prompt**:
  - Example: If the tutorial demonstrated $23 \times 14 = 322$, provide a blank mini-slate with $21 \times 13$.
  - Ask: *"Can you use Guru-ji's vertical and crosswise secret to find the middle number?"*

---

## 5. Observation Taxonomy: Confusion, Guessing, and Feedback

Observers use standardized behavioral codes:

| Code | Behavior Observed | Operational Definition | Pedagogical Implication |
| :--- | :--- | :--- | :--- |
| **CONF-1** | Gaze wandering / disengagement | Looking away from device for $>8$ seconds during dialogue | Text is too long or pace is too slow |
| **CONF-2** | Vocabulary hesitation | Pausing, furrowing brow, or asking *"What does [word] mean?"* | Unexplained technical vocabulary |
| **GUESS-1** | Rapid tile tapping | Tapping 3 or more choice tiles in $<2$ seconds | Prompt is unclear or math is too advanced |
| **HINT-1** | Productive retry | Reads corrective hint, pauses to calculate, then selects correct tile | Constructive feedback is working as intended |
| **HINT-2** | Ignored hint | Taps another random tile without reading error feedback | Feedback banner lacks visual contrast or clarity |

---

## 6. Actionable Revision Criteria (When Must a Lesson Be Revised?)

A tutorial or handshake module **fails usability validation** and requires immediate design revision if any of the following benchmarks are met across a testing cohort ($N \ge 8$ children per age group):

1. **First-Attempt Handshake Failure Rate > 25%**:
   - If more than 1 in 4 age-eligible children fail the interactive handshake on their initial attempt, the preceding explanation is insufficient.
2. **Rapid Guessing Rate (GUESS-1) > 20%**:
   - If more than 20% of children tap tiles indiscriminately without attempting mental arithmetic, the prompt does not clearly separate the *question* from the choice options.
3. **Repeated Vocabulary Stumbling (CONF-2) $\ge 2$ instances**:
   - If two or more children ask about the meaning of a non-mathematical word or struggle with complex grammar, the phrasing must be simplified.
4. **Slate/Dialogue Disconnect**:
   - If $>30\%$ of children fail to identify the mathematical slate as the focal point during Task 2.
5. **Non-Audio Failure**:
   - If children testing under muted conditions fail comprehension tasks at double the rate of children with audio enabled.

---

## 7. Content-Review Checklist for Curriculum Designers

Before any new Vedic Sutra lesson is merged into active content, authors must complete this checklist:

- [ ] **Dialogue Chunking**: No single speech bubble exceeds 45 words or 250 characters.
- [ ] **Sequential Nodes**: Scene contains between 2 and 4 nodes total.
- [ ] **Vocabulary Whitelist**: All terms are in the approved pedagogical glossary (e.g., *sutra, units, tens, carry, remainder, crosswise*).
- [ ] **Question vs. Answer Boundary**: The handshake prompt poses a clear question and never reveals the target answer in the prompt text or unsolved equation.
- [ ] **Constructive Error Hints**: The incorrect feedback message explains *how* to approach the calculation (e.g., *"Remember to add the carry"*) rather than just stating *"Incorrect"*.
- [ ] **Visual Parity**: The visual slate and text bubbles fully convey the lesson without requiring sound.
- [ ] **Touch Target Compliance**: All interactive buttons and tiles are at least $48 \times 48\text{ dp}$.
