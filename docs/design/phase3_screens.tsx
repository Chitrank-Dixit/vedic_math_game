import React, { useState, useEffect } from 'react';
import {
  VedicSutraCard,
  YantraProgressRing,
  MentalMathNumpad,
  tokens,
  StepDetail
} from './phase2_components';

/**
 * Phase 3 — Screen Architectures & Workflows
 * Multi-Platform React / TypeScript / JSX Specifications
 *
 * Screens Included:
 * 1. HomeScreen (Portal, Streak Banner, Module Categories, Speed Practice CTA)
 * 2. SutraSolverScreen (Interactive Workspace, Blueprint Drawer, VedicSutraCard, Numpad)
 * 3. PracticeArenaScreen (Timed Challenge, Mandala Countdown Ring, Real-Time Feedback)
 * 4. AppNavigator (Stateful Tab/Stack Router)
 */

// -------------------------------------------------------------
// Screen 01: HomeScreen (Learning Portal)
// -------------------------------------------------------------
export interface HomeScreenProps {
  streakDays?: number;
  onSelectSutra: (sutraId: string) => void;
  onStartPracticeArena: () => void;
  isDark?: boolean;
  onToggleTheme: () => void;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({
  streakDays = 7,
  onSelectSutra,
  onStartPracticeArena,
  isDark = true,
  onToggleTheme,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;

  const modules = [
    {
      category: 'Multiplication Shortcuts',
      items: [
        { id: 'urdhva', sanskrit: 'ऊर्ध्व तिर्यग्भ्याम्', english: 'Vertically and Crosswise (2x2)', estMin: 8, progress: 1.0 },
        { id: 'ekadhikena', sanskrit: 'एकाधिकेन पूर्वेण', english: 'By One More than the Previous (Squaring 5s)', estMin: 5, progress: 0.85 },
      ],
    },
    {
      category: 'Squaring & Cubing',
      items: [
        { id: 'yavadunam', sanskrit: 'यावदूनम्', english: 'Deficiency Squaring Near Base 100', estMin: 7, progress: 0.40 },
      ],
    },
    {
      category: 'Division & Reciprocals',
      items: [
        { id: 'paravartya', sanskrit: 'परावर्त्य योजयेत्', english: 'Transpose and Apply (Polynomial Division)', estMin: 15, progress: 0.0 },
      ],
    },
  ];

  return (
    <div
      style={{
        width: '100%',
        minHeight: '100vh',
        background: currentColors.backgroundPrimary,
        color: currentColors.textPrimary,
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        gap: '16px',
        maxWidth: '480px',
        margin: '0 auto',
      }}
    >
      {/* Top Header: Streak & Theme Toggle */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            background: currentColors.surfaceCard,
            border: `1px solid ${currentColors.secondaryGold}`,
            borderRadius: '999px',
            padding: '6px 14px',
            fontSize: '13px',
            fontWeight: 700,
            color: currentColors.secondaryGold,
            boxShadow: currentColors.shadowAmbient,
          }}
        >
          <span>⭐</span>
          <span>{streakDays} Day Sutra Streak</span>
        </div>

        <button
          onClick={onToggleTheme}
          style={{
            background: currentColors.surfaceCard,
            border: `1px solid ${currentColors.borderSubtle}`,
            borderRadius: '999px',
            padding: '6px 12px',
            color: currentColors.textPrimary,
            fontSize: '12px',
            fontWeight: 700,
            cursor: 'pointer',
          }}
        >
          {isDark ? '🌙 Midnight' : '📜 Parchment'}
        </button>
      </div>

      {/* Hero Title */}
      <div>
        <h1 style={{ margin: 0, fontFamily: "'Cinzel', serif", fontSize: '26px', fontWeight: 700, color: currentColors.primarySaffron }}>
          Vedic Mathematics
        </h1>
        <p style={{ margin: '4px 0 0', fontSize: '13px', color: currentColors.textSecondary }}>
          Sacred Geometry & Mental Arithmetic Engine
        </p>
      </div>

      {/* Speed Practice Hero CTA Banner */}
      <div
        onClick={onStartPracticeArena}
        style={{
          background: currentColors.primarySaffron,
          color: isDark ? '#0F172A' : '#FFFFFF',
          borderRadius: '12px',
          padding: '16px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          cursor: 'pointer',
          boxShadow: '0 4px 14px rgba(230, 81, 0, 0.35)',
        }}
      >
        <div>
          <div style={{ fontWeight: 800, fontSize: '16px', letterSpacing: '0.5px' }}>⚡ SPEED PRACTICE ARENA</div>
          <div style={{ fontSize: '12px', opacity: 0.9 }}>Test your calculation speed with timed drills</div>
        </div>
        <div
          style={{
            width: '36px',
            height: '36px',
            borderRadius: '50%',
            background: isDark ? '#0F172A' : '#FFFFFF',
            color: currentColors.primarySaffron,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontWeight: 900,
          }}
        >
          ▶
        </div>
      </div>

      {/* Module Categories Grid */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        {modules.map((cat, cIdx) => (
          <div key={cIdx}>
            <div style={{ fontSize: '12px', fontWeight: 700, letterSpacing: '1px', color: currentColors.secondaryGold, marginBottom: '8px' }}>
              {cat.category.toUpperCase()}
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {cat.items.map((mod) => (
                <div
                  key={mod.id}
                  onClick={() => onSelectSutra(mod.id)}
                  style={{
                    background: currentColors.surfaceCard,
                    border: `1px solid ${mod.progress >= 1.0 ? currentColors.statusSuccess : currentColors.borderSubtle}`,
                    borderRadius: '12px',
                    padding: '14px',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    cursor: 'pointer',
                    boxShadow: currentColors.shadowAmbient,
                  }}
                >
                  <div>
                    <div style={{ fontFamily: "'Cinzel', serif", fontWeight: 700, fontSize: '15px' }}>{mod.sanskrit}</div>
                    <div style={{ fontSize: '12px', color: currentColors.textSecondary, marginTop: '2px' }}>{mod.english}</div>
                    <div style={{ fontSize: '11px', color: currentColors.primarySaffron, marginTop: '4px', fontWeight: 600 }}>
                      ⏱ ~{mod.estMin} mins
                    </div>
                  </div>
                  <YantraProgressRing progress={mod.progress} size={44} isDark={isDark} />
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

// -------------------------------------------------------------
// Screen 02: SutraSolverScreen (Interactive Workspace)
// -------------------------------------------------------------
export interface SutraSolverScreenProps {
  onBack: () => void;
  onComplete: () => void;
  isDark?: boolean;
}

export const SutraSolverScreen: React.FC<SutraSolverScreenProps> = ({
  onBack,
  onComplete,
  isDark = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;
  const [isBlueprintOpen, setIsBlueprintOpen] = useState(false);
  const [activeStep, setActiveStep] = useState(0);
  const [inputBuffer, setInputBuffer] = useState('');
  const [isCompleted, setIsCompleted] = useState(false);

  const steps: StepDetail[] = [
    { stepNumber: 1, label: 'Units × Units', formula: '3 × 4', result: '12 (write 2, carry 1)' },
    { stepNumber: 2, label: 'Crosswise Multiplication', formula: '2(4) + 3(1) + 1', result: '12 (write 2, carry 1)' },
    { stepNumber: 3, label: 'Tens × Tens', formula: '2(1) + 1', result: '3' },
  ];

  const handleDigit = (d: string) => {
    if (!isCompleted) setInputBuffer((prev) => prev + d);
  };

  const handleBackspace = () => {
    setInputBuffer((prev) => prev.slice(0, -1));
  };

  const handleClear = () => {
    setInputBuffer('');
  };

  const handleSubmit = () => {
    if (activeStep + 1 < steps.length) {
      setActiveStep((prev) => prev + 1);
      setInputBuffer('');
    } else {
      setIsCompleted(true);
      setTimeout(onComplete, 1200);
    }
  };

  return (
    <div
      style={{
        width: '100%',
        minHeight: '100vh',
        background: currentColors.backgroundPrimary,
        color: currentColors.textPrimary,
        padding: '16px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        maxWidth: '440px',
        margin: '0 auto',
      }}
    >
      {/* Top Header */}
      <div>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
          <button
            onClick={onBack}
            style={{
              background: currentColors.surfaceCard,
              border: `1px solid ${currentColors.borderSubtle}`,
              borderRadius: '8px',
              padding: '6px 12px',
              color: currentColors.primarySaffron,
              fontWeight: 700,
              cursor: 'pointer',
            }}
          >
            ← Home
          </button>
          <button
            onClick={() => setIsBlueprintOpen(!isBlueprintOpen)}
            style={{
              background: 'transparent',
              border: `1px solid ${currentColors.borderSubtle}`,
              borderRadius: '8px',
              padding: '6px 12px',
              color: currentColors.secondaryGold,
              fontSize: '12px',
              fontWeight: 700,
              cursor: 'pointer',
            }}
          >
            {isBlueprintOpen ? 'Hide Blueprint ✕' : '📜 Formula Blueprint ▾'}
          </button>
        </div>

        {/* Collapsible Formula Blueprint Drawer */}
        {isBlueprintOpen && (
          <div
            style={{
              background: currentColors.surfaceCard,
              border: `1px solid ${currentColors.secondaryGold}`,
              borderRadius: '10px',
              padding: '12px',
              marginBottom: '12px',
              fontSize: '12px',
            }}
          >
            <div style={{ fontWeight: 700, color: currentColors.primarySaffron }}>ऊर्ध्वतिर्यग्भ्यां गुणयेत्।</div>
            <div style={{ fontStyle: 'italic', color: currentColors.textSecondary, marginTop: '2px' }}>
              "Vertically and crosswise multiply."
            </div>
            <div style={{ marginTop: '6px', color: currentColors.textPrimary }}>
              Shortcut: Multiply vertical units digits → Cross-multiply & sum → Multiply tens digits.
            </div>
          </div>
        )}
      </div>

      {/* Main Workspace Card */}
      <VedicSutraCard
        sutraSanskrit="ऊर्ध्व तिर्यग्भ्याम्"
        sutraEnglish="Vertically and Crosswise"
        equationDisplay="23 × 14"
        steps={steps}
        activeStep={activeStep}
        isCompleted={isCompleted}
        carries={[1, 1]}
        userInput={inputBuffer}
        isDark={isDark}
      />

      {/* Bottom Pinned Numpad */}
      <MentalMathNumpad
        onDigit={handleDigit}
        onBackspace={handleBackspace}
        onClear={handleClear}
        onSubmit={handleSubmit}
        isDark={isDark}
      />
    </div>
  );
};

// -------------------------------------------------------------
// Screen 03: PracticeArenaScreen (Timed Mental Speed Challenge)
// -------------------------------------------------------------
export interface PracticeArenaScreenProps {
  onExit: () => void;
  isDark?: boolean;
}

export const PracticeArenaScreen: React.FC<PracticeArenaScreenProps> = ({
  onExit,
  isDark = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;
  const [seconds, setSeconds] = useState(60);
  const [score, setScore] = useState(0);
  const [inputVal, setInputVal] = useState('');
  const [problemIdx, setProblemIdx] = useState(0);

  const problems = [
    { eq: '65²', ans: '4225', hint: 'Ekadhikena: 6×7=42 | 25' },
    { eq: '98 × 97', ans: '9506', hint: 'Nikhilam: -2,-3 -> 95 | 06' },
    { eq: '25²', ans: '625', hint: 'Ekadhikena: 2×3=6 | 25' },
    { eq: '104 × 106', ans: '11024', hint: 'Nikhilam: +4,+6 -> 110 | 24' },
  ];

  const currentProb = problems[problemIdx % problems.length];

  useEffect(() => {
    if (seconds <= 0) return;
    const t = setInterval(() => setSeconds((s) => s - 1), 1000);
    return () => clearInterval(t);
  }, [seconds]);

  const handleDigit = (d: string) => {
    const next = inputVal + d;
    setInputVal(next);

    if (next === currentProb.ans) {
      setScore((s) => s + 100);
      setInputVal('');
      setProblemIdx((p) => p + 1);
    }
  };

  const opsPerMin = ((score / 100) / ((60 - seconds || 1) / 60)).toFixed(1);

  return (
    <div
      style={{
        width: '100%',
        minHeight: '100vh',
        background: currentColors.backgroundPrimary,
        color: currentColors.textPrimary,
        padding: '16px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        maxWidth: '440px',
        margin: '0 auto',
      }}
    >
      {/* Header: Exit, Mandala Timer, Score */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <button
          onClick={onExit}
          style={{
            background: currentColors.surfaceCard,
            border: `1px solid ${currentColors.borderSubtle}`,
            borderRadius: '8px',
            padding: '6px 12px',
            color: currentColors.textPrimary,
            cursor: 'pointer',
          }}
        >
          Exit
        </button>

        {/* Central Rotating Mandala Timer */}
        <div style={{ position: 'relative', width: '54px', height: '54px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <YantraProgressRing progress={seconds / 60} size={54} strokeWidth={3} isDark={isDark} />
          <span style={{ position: 'absolute', fontSize: '13px', fontWeight: 800, fontFamily: "'JetBrains Mono', monospace", color: seconds <= 10 ? currentColors.primarySaffron : currentColors.textPrimary }}>
            {seconds}s
          </span>
        </div>

        <div style={{ textAlign: 'right' }}>
          <div style={{ fontSize: '15px', fontWeight: 800, color: currentColors.secondaryGold }}>{score} PTS</div>
          <div style={{ fontSize: '11px', color: currentColors.textSecondary }}>{opsPerMin} ops/min</div>
        </div>
      </div>

      {/* Central Equation Card */}
      <div
        style={{
          background: currentColors.surfaceCard,
          border: `1px solid ${currentColors.borderSubtle}`,
          borderRadius: '12px',
          padding: '24px',
          textAlign: 'center',
          boxShadow: currentColors.shadowAmbient,
        }}
      >
        <div style={{ fontSize: '13px', color: currentColors.secondaryGold, marginBottom: '6px' }}>{currentProb.hint}</div>
        <div style={{ fontFamily: "'JetBrains Mono', monospace", fontSize: '38px', fontWeight: 800, color: currentColors.primarySaffron }}>
          {currentProb.eq} = ?
        </div>
        <div
          style={{
            marginTop: '16px',
            height: '46px',
            background: isDark ? '#0B1120' : '#F7F5F0',
            border: `1px solid ${currentColors.primarySaffron}`,
            borderRadius: '8px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '22px',
            fontWeight: 800,
            fontFamily: "'JetBrains Mono', monospace",
            color: currentColors.primarySaffron,
          }}
        >
          {inputVal || 'Tap digits...'}
        </div>
      </div>

      {/* Streamlined Bottom Numpad */}
      <MentalMathNumpad
        onDigit={handleDigit}
        onBackspace={() => setInputVal((prev) => prev.slice(0, -1))}
        onClear={() => setInputVal('')}
        onSubmit={() => {}}
        isDark={isDark}
      />
    </div>
  );
};

// -------------------------------------------------------------
// 04. AppNavigator (Stateful Router Engine)
// -------------------------------------------------------------
export const AppNavigator: React.FC = () => {
  const [currentScreen, setCurrentScreen] = useState<'HOME' | 'SOLVER' | 'ARENA'>('HOME');
  const [isDark, setIsDark] = useState(true);

  return (
    <div>
      {currentScreen === 'HOME' && (
        <HomeScreen
          onSelectSutra={() => setCurrentScreen('SOLVER')}
          onStartPracticeArena={() => setCurrentScreen('ARENA')}
          isDark={isDark}
          onToggleTheme={() => setIsDark(!isDark)}
        />
      )}
      {currentScreen === 'SOLVER' && (
        <SutraSolverScreen
          onBack={() => setCurrentScreen('HOME')}
          onComplete={() => setCurrentScreen('HOME')}
          isDark={isDark}
        />
      )}
      {currentScreen === 'ARENA' && (
        <PracticeArenaScreen
          onExit={() => setCurrentScreen('HOME')}
          isDark={isDark}
        />
      )}
    </div>
  );
};
