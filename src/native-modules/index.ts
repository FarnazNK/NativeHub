import { NativeModules } from 'react-native';

// Biometric Types
export type BiometricType = 'fingerprint' | 'face' | 'iris' | 'none';

export interface BiometricAvailability {
  available: boolean;
  biometricType: BiometricType;
  error?: string;
}

export interface BiometricAuthOptions {
  title?: string;
  subtitle?: string;
  description?: string;
  cancelButtonText?: string;
  allowDeviceCredentials?: boolean;
}

export interface BiometricAuthResult {
  success: boolean;
  error?: { code: string; message: string };
}

export interface IBiometricModule {
  isAvailable(): Promise<BiometricAvailability>;
  getBiometricType(): Promise<BiometricType>;
  authenticate(options?: BiometricAuthOptions): Promise<BiometricAuthResult>;
  hasDeviceCredentials(): Promise<boolean>;
}

// Performance Types
export interface PerformanceMetrics {
  cpuUsage: number;
  memoryUsage: number;
  availableMemory: number;
  totalMemory: number;
  batteryLevel: number;
  isCharging: boolean;
  timestamp: number;
}

export interface FrameMetrics {
  fps: number;
  droppedFrames: number;
  totalFrames: number;
  avgFrameTime: number;
}

export interface TraceMetrics {
  name: string;
  startTime: number;
  endTime?: number;
  duration?: number;
  attributes: Record<string, string | number | boolean>;
}

export interface IPerformanceModule {
  getMetrics(): Promise<PerformanceMetrics>;
  getFrameMetrics(): Promise<FrameMetrics>;
  startTrace(traceName: string): Promise<void>;
  stopTrace(traceName: string): Promise<TraceMetrics>;
  startFPSMonitoring(intervalMs?: number): Promise<void>;
  stopFPSMonitoring(): Promise<void>;
}

const { BiometricModule, PerformanceModule } = NativeModules;

export const Biometric: IBiometricModule = BiometricModule;
export const Performance: IPerformanceModule = PerformanceModule;

export const initializeNativeModules = async (): Promise<void> => {
  try {
    const biometricStatus = await Biometric.isAvailable();
    console.log('[NativeModules] Biometric available:', biometricStatus.available);
  } catch (error) {
    console.error('[NativeModules] Initialization failed:', error);
  }
};

export default { Biometric, Performance, initializeNativeModules };
