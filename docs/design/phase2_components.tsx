import React, { useState, useEffect } from 'react';

/**
 * Phase 2 — Core Components & Math Input Engine
 * Multi-Platform React / TypeScript / JSX Specifications
 *
 * Theme-aware implementations adhering strictly to the Vedic Mathematics Design Tokens.
 */

// -------------------------------------------------------------
// Design Tokens Definition
// -------------------------------------------------------------
export const tokens = {
  colors: {
    light: {
      backgroundPrimary: '#FDFBF7',
      surfaceCard: '#FFFFFF',
      primarySaffron: '#E65100',
      secondaryGold: '#D4AF37',
      textPrimary: '#212121',
      textSecondary: '#616161',
      borderSubtle: 'rgba(212, 175, 55, 0.2)',
      statusSuccess: '#2E7D32',
      statusError: '#C62828',
      shadowAmbient: '0px 4px 12px rgba(0, 0, 0, 0.05)',
      glowSubtle: '0px 0px 12px rgba(212, 175, 55, 0.1)',
    },
    dark: {
      backgroundPrimary: '#0F172A',
      surfaceCard: '#1E293B',
      primarySaffron: '#FF9800',
      secondaryGold: '#FFD700',
      textPrimary: '#F8FAFC',
      textSecondary: '#94A3B8',
      borderSubtle: 'rgba(255, 215, 0, 0.25)',
      statusSuccess: '#4ADE80',
      statusError: '#EF4444',
      shadowAmbient: '0px 4px 16px rgba(0, 0, 0, 0.4)',
      glowSubtle: '0px 0px 12px rgba(255, 215, 0, 0.08)',
    },
  },
};

// -------------------------------------------------------------
// 01. YantraProgressRing Component
// -------------------------------------------------------------
export interface YantraProgressRingProps {
  progress: number; // 0.0 to 1.0
  size?: number;    // px diameter
  strokeWidth?: number;
  isDark?: boolean;
}

export const YantraProgressRing: React.FC<YantraProgressRingProps> = ({
  progress = 0,
  size = 64,
  strokeWidth = 3,
  isDark = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;
  const clamped = Math.min(Math.max(progress, 0), 1);
  const isCompleted = clamped >= 0.999;

  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const strokeDashoffset = circumference - clamped * circumference;

  return (
    <div style={{ position: 'relative', width: size, height: size, display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        {/* Concentric Background Sacred Geometry Rings */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke={currentColors.borderSubtle}
          strokeWidth={strokeWidth * 0.5}
        />
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius * 0.7}
          fill="none"
          stroke={currentColors.borderSubtle}
          strokeWidth={strokeWidth * 0.35}
          opacity={0.4}
        />
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius * 0.4}
          fill="none"
          stroke={currentColors.borderSubtle}
          strokeWidth={strokeWidth * 0.25}
          opacity={0.25}
        />

        {/* 8 Radiating Ray Ticks */}
        {[...Array(8)].map((_, i) => {
          const angle = (i * 360) / 8 * (Math.PI / 180);
          const x1 = size / 2 + radius * 0.72 * Math.cos(angle);
          const y1 = size / 2 + radius * 0.72 * Math.sin(angle);
          const x2 = size / 2 + radius * 0.94 * Math.cos(angle);
          const y2 = size / 2 + radius * 0.94 * Math.sin(angle);
          return (
            <line
              key={i}
              x1={x1}
              y1={y1}
              x2={x2}
              y2={y2}
              stroke={isCompleted ? currentColors.statusSuccess : currentColors.borderSubtle}
              strokeWidth={strokeWidth * 0.4}
              strokeLinecap="round"
              opacity={isCompleted ? 0.6 : 0.3}
            />
          );
        })}

        {/* Active Animated Sweep Progress Ring */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke={isCompleted ? currentColors.statusSuccess : currentColors.primarySaffron}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={strokeDashoffset}
          transform={`rotate(-90 ${size / 2} ${size / 2})`}
          style={{ transition: 'stroke-dashoffset 0.6s cubic-bezier(0.4, 0, 0.2, 1), stroke 0.4s ease' }}
        />

        {/* 8-Pointed Mandala Star on 100% Completion */}
        {isCompleted && (
          <path
            d={generateMandalaPath(size / 2, size / 2, radius * 0.45, radius * 0.22, 8)}
            fill="none"
            stroke={currentColors.statusSuccess}
            strokeWidth={1.8}
            strokeLinecap="round"
          />
        )}
      </svg>
    </div>
  );
};

function generateMandalaPath(cx: number, cy: number, outerR: number, innerR: number, points: number) {
  let path = '';
  const step = (Math.PI * 2) / (points * 2);
  for (let i = 0; i < points * 2; i++) {
    const r = i % 2 === 0 ? outerR : innerR;
    const angle = i * step - Math.PI / 2;
    const x = cx + r * Math.cos(angle);
    const y = cy + r * Math.sin(angle);
    path += (i === 0 ? `M ${x} ${y}` : ` L ${x} ${y}`);
  }
  return path + ' Z';
}

// -------------------------------------------------------------
// 02. MentalMathNumpad Component
// -------------------------------------------------------------
export interface MentalMathNumpadProps {
  onDigit: (digit: string) => void;
  onBackspace: () => void;
  onClear: () => void;
  onSubmit: () => void;
  isDark?: boolean;
}

export const MentalMathNumpad: React.FC<MentalMathNumpadProps> = ({
  onDigit,
  onBackspace,
  onClear,
  onSubmit,
  isDark = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;

  const keyRows = [
    ['1', '2', '3'],
    ['4', '5', '6'],
    ['7', '8', '9'],
    ['CLR', '0', '⌫'],
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '100%', maxWidth: '340px', margin: '0 auto' }}>
      {keyRows.map((row, rIdx) => (
        <div key={rIdx} style={{ display: 'flex', justifyContent: 'space-between' }}>
          {row.map((k) => {
            const isAction = k === 'CLR' || k === '⌫';
            const actionColor = k === 'CLR' ? currentColors.statusError : (isDark ? '#38BDF8' : currentColors.primarySaffron);

            return (
              <button
                key={k}
                onClick={() => {
                  if (k === 'CLR') onClear();
                  else if (k === '⌫') onBackspace();
                  else onDigit(k);
                }}
                style={{
                  width: '64px',
                  height: '64px',
                  borderRadius: '50%',
                  border: `1px solid ${currentColors.borderSubtle}`,
                  background: isDark ? 'radial-gradient(circle, #1E293B, #0F172A)' : 'radial-gradient(circle, #FFFFFF, #FDFBF7)',
                  color: isAction ? actionColor : currentColors.textPrimary,
                  fontFamily: "'JetBrains Mono', monospace",
                  fontSize: isAction && k === 'CLR' ? '12px' : '22px',
                  fontWeight: 700,
                  cursor: 'pointer',
                  outline: 'none',
                  boxShadow: currentColors.shadowAmbient,
                  transition: 'transform 0.1s ease, box-shadow 0.15s ease, border-color 0.15s ease',
                }}
                onMouseDown={(e) => {
                  e.currentTarget.style.transform = 'scale(0.92)';
                  e.currentTarget.style.borderColor = isDark ? currentColors.secondaryGold : currentColors.primarySaffron;
                }}
                onMouseUp={(e) => {
                  e.currentTarget.style.transform = 'scale(1.0)';
                  e.currentTarget.style.borderColor = currentColors.borderSubtle;
                }}
              >
                {k}
              </button>
            );
          })}
        </div>
      ))}

      {/* Saffron Gradient Submit Button */}
      <button
        onClick={onSubmit}
        style={{
          width: '100%',
          height: '48px',
          borderRadius: '8px',
          border: 'none',
          background: currentColors.primarySaffron,
          color: isDark ? '#0F172A' : '#FFFFFF',
          fontFamily: "'Cinzel', serif",
          fontSize: '15px',
          fontWeight: 800,
          letterSpacing: '1px',
          cursor: 'pointer',
          boxShadow: '0 4px 12px rgba(230, 81, 0, 0.3)',
          marginTop: '4px',
        }}
      >
        SUBMIT ANSWER ⚡
      </button>
    </div>
  );
};

// -------------------------------------------------------------
// 03. VedicSutraCard Component
// -------------------------------------------------------------
export interface StepDetail {
  stepNumber: number;
  label: string;
  formula: string;
  result: string;
}

export interface VedicSutraCardProps {
  sutraSanskrit: string;
  sutraEnglish: string;
  equationDisplay: string;
  steps: StepDetail[];
  activeStep: number;
  isCompleted: boolean;
  carries?: number[];
  userInput?: string;
  isDark?: boolean;
}

export const VedicSutraCard: React.FC<VedicSutraCardProps> = ({
  sutraSanskrit = 'ऊर्ध्व तिर्यग्भ्याम्',
  sutraEnglish = 'Vertically and Crosswise',
  equationDisplay = '23 × 14',
  steps = [],
  activeStep = 0,
  isCompleted = false,
  carries = [],
  userInput = '',
  isDark = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;
  const progress = steps.length > 0 ? (activeStep + (isCompleted ? 1 : 0)) / steps.length : 0;

  return (
    <div
      style={{
        width: '100%',
        maxWidth: '420px',
        borderRadius: '12px',
        background: currentColors.surfaceCard,
        border: `1px solid ${isCompleted ? currentColors.statusSuccess : currentColors.borderSubtle}`,
        boxShadow: isCompleted ? '0 0 16px rgba(74, 222, 128, 0.2)' : currentColors.shadowAmbient,
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        gap: '14px',
        transition: 'border-color 0.4s ease, box-shadow 0.4s ease',
      }}
    >
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h2 style={{ margin: 0, fontFamily: "'Cinzel', serif", fontSize: '18px', fontWeight: 700, color: currentColors.primarySaffron }}>
            {sutraSanskrit}
          </h2>
          <p style={{ margin: '2px 0 0', fontFamily: "'Cinzel', serif", fontSize: '13px', fontStyle: 'italic', color: currentColors.textSecondary }}>
            {sutraEnglish}
          </p>
        </div>
        <YantraProgressRing progress={isCompleted ? 1.0 : progress} size={44} isDark={isDark} />
      </div>

      {/* Numerical Math Slate Display with Tabular Alignments */}
      <div
        style={{
          borderRadius: '10px',
          background: isDark ? '#0B1120' : '#F7F5F0',
          border: `1px solid ${isCompleted ? currentColors.statusSuccess : currentColors.borderSubtle}`,
          padding: '16px',
          textAlign: 'center',
          position: 'relative',
        }}
      >
        {/* Optional Carries */}
        {carries.length > 0 && (
          <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', marginBottom: '4px' }}>
            {carries.map((c, i) => (
              <span key={i} style={{ fontSize: '11px', fontWeight: 700, color: currentColors.secondaryGold, fontFamily: "'JetBrains Mono', monospace" }}>
                {c > 0 ? `+${c}` : ' '}
              </span>
            ))}
          </div>
        )}

        <div
          style={{
            fontFamily: "'JetBrains Mono', monospace",
            fontVariantNumeric: 'tabular-nums',
            fontSize: '32px',
            fontWeight: 700,
            color: isCompleted ? currentColors.statusSuccess : (isDark ? currentColors.secondaryGold : currentColors.primarySaffron),
            letterSpacing: '1px',
          }}
        >
          {equationDisplay}
        </div>
      </div>

      {/* Step-by-Step Working Area */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
        {steps.map((s, idx) => {
          const isActive = idx === activeStep && !isCompleted;
          const isDone = idx < activeStep || isCompleted;

          return (
            <div
              key={s.stepNumber}
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                padding: '6px 10px',
                borderRadius: '6px',
                background: isActive ? 'rgba(255, 152, 0, 0.12)' : 'transparent',
                borderLeft: isActive ? `3px solid ${currentColors.primarySaffron}` : '3px solid transparent',
              }}
            >
              <span
                style={{
                  fontSize: '13px',
                  fontWeight: isActive ? 700 : 400,
                  color: isActive ? currentColors.primarySaffron : (isDone ? currentColors.textPrimary : currentColors.textSecondary),
                }}
              >
                • Step {s.stepNumber} ({s.label}): {s.formula}
              </span>
              <span
                style={{
                  fontFamily: "'JetBrains Mono', monospace",
                  fontSize: '13px',
                  fontWeight: 700,
                  color: isDone ? currentColors.statusSuccess : currentColors.primarySaffron,
                }}
              >
                {isDone ? `= ${s.result}` : (isActive ? '…' : '')}
              </span>
            </div>
          );
        })}
      </div>

      {/* Live Input Feedback Area */}
      {(userInput || isCompleted) && (
        <div
          style={{
            borderRadius: '8px',
            background: isCompleted ? 'rgba(74, 222, 128, 0.12)' : (isDark ? '#0B1120' : '#FFFFFF'),
            border: `1px solid ${isCompleted ? currentColors.statusSuccess : currentColors.borderSubtle}`,
            padding: '10px 14px',
            textAlign: 'center',
            fontFamily: "'JetBrains Mono', monospace",
            fontSize: '18px',
            fontWeight: 800,
            color: isCompleted ? currentColors.statusSuccess : currentColors.textPrimary,
          }}
        >
          {isCompleted ? '✓ MASTERED CALCULATION' : userInput}
        </div>
      )}
    </div>
  );
};
