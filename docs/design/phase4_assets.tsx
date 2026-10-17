import React, { useEffect, useRef } from 'react';
import { tokens } from './phase2_components';

/**
 * Phase 4 — Logo, Visual Motifs, Celebration Engine & Audio/Haptics
 * Multi-Platform React / TypeScript / SVG Specifications
 */

// -------------------------------------------------------------
// 01. VedicAppLogo Component (Production SVG Spec)
// -------------------------------------------------------------
export interface VedicAppLogoProps {
  size?: number;
  isDark?: boolean;
  showBackground?: boolean;
}

export const VedicAppLogo: React.FC<VedicAppLogoProps> = ({
  size = 80,
  isDark = true,
  showBackground = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;
  const center = size / 2;
  const emblemRadius = size * 0.32;

  return (
    <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} fill="none">
      <defs>
        {/* Brand Linear Gradient (Saffron -> Deep Gold) */}
        <linearGradient id="vedicBrandGrad" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" stopColor={currentColors.primarySaffron} />
          <stop offset="100%" stopColor={currentColors.secondaryGold} />
        </linearGradient>
      </defs>

      {/* Outer Squircle Container */}
      {showBackground && (
        <rect
          x="1"
          y="1"
          width={size - 2}
          height={size - 2}
          rx={size * 0.25}
          fill={currentColors.surfaceCard}
          stroke="url(#vedicBrandGrad)"
          strokeWidth="1.5"
          strokeOpacity="0.4"
        />
      )}

      {/* 8-Pointed Mandala Star */}
      <path
        d={generate8PointedStarPath(center, center, emblemRadius, emblemRadius * 0.55)}
        stroke="url(#vedicBrandGrad)"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* Infinity Symbol Loop (∞) */}
      <path
        d={generateInfinityPath(center, center, emblemRadius * 1.35, emblemRadius * 0.65)}
        stroke="url(#vedicBrandGrad)"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* Central Bindu Point */}
      <circle cx={center} cy={center} r="2.5" fill="url(#vedicBrandGrad)" />
    </svg>
  );
};

function generate8PointedStarPath(cx: number, cy: number, outerR: number, innerR: number) {
  let path = '';
  const points = 8;
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

function generateInfinityPath(cx: number, cy: number, width: number, height: number) {
  let path = '';
  const steps = 60;
  const halfW = width / 2;
  const halfH = height / 2;

  for (let i = 0; i <= steps; i++) {
    const t = (i / steps) * (2 * Math.PI);
    const denom = 1 + Math.sin(t) * Math.sin(t);
    const x = cx + (halfW * Math.cos(t)) / denom;
    const y = cy + (halfH * Math.sin(t) * Math.cos(t)) / denom;
    path += (i === 0 ? `M ${x} ${y}` : ` L ${x} ${y}`);
  }
  return path + ' Z';
}

// -------------------------------------------------------------
// 02. Decorative Motifs (VedicMotifs)
// -------------------------------------------------------------
export const VedicCornerBorder: React.FC<{ cornerLength?: number; color?: string }> = ({
  cornerLength = 16,
  color = 'rgba(212, 175, 55, 0.45)',
}) => (
  <svg style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', pointerEvents: 'none' }}>
    {/* Top-Left */}
    <path d={`M 0 ${cornerLength} L 0 0 L ${cornerLength} 0`} stroke={color} strokeWidth="1.2" fill="none" strokeLinecap="round" />
    {/* Top-Right */}
    <path d={`M calc(100% - ${cornerLength}px) 0 L 100% 0 L 100% ${cornerLength}`} stroke={color} strokeWidth="1.2" fill="none" strokeLinecap="round" />
    {/* Bottom-Left */}
    <path d={`M 0 calc(100% - ${cornerLength}px) L 0 100% L ${cornerLength} 100%`} stroke={color} strokeWidth="1.2" fill="none" strokeLinecap="round" />
    {/* Bottom-Right */}
    <path d={`M calc(100% - ${cornerLength}px) 100% L 100% 100% L 100% calc(100% - ${cornerLength}px)`} stroke={color} strokeWidth="1.2" fill="none" strokeLinecap="round" />
  </svg>
);

export const YantraWatermark: React.FC<{ size?: number; opacity?: number; isDark?: boolean }> = ({
  size = 320,
  opacity = 0.04,
  isDark = true,
}) => {
  const currentColors = isDark ? tokens.colors.dark : tokens.colors.light;
  const center = size / 2;
  const maxR = size / 2;

  return (
    <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} style={{ opacity, pointerEvents: 'none' }}>
      {[1, 2, 3, 4, 5, 6, 7, 8].map((i) => (
        <circle
          key={i}
          cx={center}
          cy={center}
          r={maxR * (i / 8)}
          stroke={currentColors.secondaryGold}
          strokeWidth="1"
          fill="none"
        />
      ))}
      {[...Array(16)].map((_, i) => {
        const angle = (i * 360) / 16 * (Math.PI / 180);
        return (
          <line
            key={i}
            x1={center + (maxR * 0.2) * Math.cos(angle)}
            y1={center + (maxR * 0.2) * Math.sin(angle)}
            x2={center + maxR * Math.cos(angle)}
            y2={center + maxR * Math.sin(angle)}
            stroke={currentColors.secondaryGold}
            strokeWidth="0.8"
          />
        );
      })}
    </svg>
  );
};

// -------------------------------------------------------------
// 03. MandalaBurst Celebration Component
// -------------------------------------------------------------
export const MandalaBurst: React.FC<{ onComplete?: () => void }> = ({ onComplete }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onComplete?.();
    }, 1100);
    return () => clearInterval(timer);
  }, [onComplete]);

  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        pointerEvents: 'none',
        zIndex: 999,
      }}
    >
      <svg width="280" height="280" viewBox="0 0 280 280">
        <circle
          cx="140"
          cy="140"
          r="110"
          stroke="#4ADE80"
          strokeWidth="2.5"
          fill="none"
          style={{ animation: 'burstPulse 1.1s cubic-bezier(0.16, 1, 0.3, 1) forwards' }}
        />
        <path
          d={generate8PointedStarPath(140, 140, 95, 45)}
          stroke="#FFD700"
          strokeWidth="2"
          fill="none"
          style={{ animation: 'burstStar 1.1s cubic-bezier(0.16, 1, 0.3, 1) forwards' }}
        />
      </svg>
      <style>{`
        @keyframes burstPulse {
          0% { transform: scale(0.2); opacity: 1; stroke: #FF9800; }
          60% { stroke: #FFD700; opacity: 0.9; }
          100% { transform: scale(1.4); opacity: 0; stroke: #4ADE80; }
        }
        @keyframes burstStar {
          0% { transform: scale(0.1) rotate(0deg); opacity: 1; }
          100% { transform: scale(1.3) rotate(90deg); opacity: 0; }
        }
      `}</style>
    </div>
  );
};

// -------------------------------------------------------------
// 04. FeedbackService (Web Audio Tone Synthesizer & Haptics)
// -------------------------------------------------------------
export class FeedbackService {
  private static audioCtx: AudioContext | null = null;

  private static getAudioContext(): AudioContext | null {
    if (typeof window === 'undefined') return null;
    if (!this.audioCtx) {
      const AudioContextClass = window.AudioContext || (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext;
      if (AudioContextClass) this.audioCtx = new AudioContextClass();
    }
    if (this.audioCtx && this.audioCtx.state === 'suspended') {
      this.audioCtx.resume();
    }
    return this.audioCtx;
  }

  static playNumpadTap() {
    this.triggerHaptic([10]);
    this.playTone(800, 'triangle', 0.04, 0.08);
  }

  static playCorrectDigit() {
    this.triggerHaptic([25]);
    this.playTone(528, 'sine', 0.15, 0.2); // Solfeggio 528Hz Transformation chime
  }

  static playIncorrectStep() {
    this.triggerHaptic([40, 60, 40]);
    this.playTone(180, 'sawtooth', 0.2, 0.15); // 180Hz low muted resonance
  }

  static playSutraMastered() {
    this.triggerHaptic([30, 40, 50, 40, 100]);
    // Ascending 3-note classical chime (528Hz -> 660Hz -> 792Hz)
    this.playTone(528, 'sine', 0.15, 0.25, 0);
    this.playTone(660, 'sine', 0.15, 0.25, 0.15);
    this.playTone(792, 'sine', 0.35, 0.3, 0.3);
  }

  private static triggerHaptic(pattern: number[]) {
    if (typeof navigator !== 'undefined' && navigator.vibrate) {
      navigator.vibrate(pattern);
    }
  }

  private static playTone(freq: number, type: OscillatorType, duration: number, gainVal: number, delaySec: number = 0) {
    const ctx = this.getAudioContext();
    if (!ctx) return;

    const osc = ctx.createOscillator();
    const gain = ctx.createGain();

    osc.type = type;
    osc.frequency.setValueAtTime(freq, ctx.currentTime + delaySec);

    gain.gain.setValueAtTime(gainVal, ctx.currentTime + delaySec);
    gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + delaySec + duration);

    osc.connect(gain);
    gain.connect(ctx.destination);

    osc.start(ctx.currentTime + delaySec);
    osc.stop(ctx.currentTime + delaySec + duration);
  }
}
