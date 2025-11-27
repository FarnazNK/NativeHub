import { useState, useEffect, useCallback } from 'react';
import { Biometric, Performance, BiometricAvailability, PerformanceMetrics } from '../native-modules';

export const useBiometric = () => {
  const [availability, setAvailability] = useState<BiometricAvailability | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const checkAvailability = useCallback(async () => {
    setIsLoading(true);
    const result = await Biometric.isAvailable();
    setAvailability(result);
    setIsLoading(false);
  }, []);

  useEffect(() => { checkAvailability(); }, [checkAvailability]);

  const authenticate = useCallback(async () => {
    const result = await Biometric.authenticate({ title: 'Authenticate' });
    return result.success;
  }, []);

  return { isAvailable: availability?.available ?? false, biometricType: availability?.biometricType ?? 'none', isLoading, authenticate };
};

export const usePerformanceMetrics = (autoRefresh = false, intervalMs = 1000) => {
  const [metrics, setMetrics] = useState<PerformanceMetrics | null>(null);

  useEffect(() => {
    const fetch = async () => setMetrics(await Performance.getMetrics());
    fetch();
    if (autoRefresh) {
      const id = setInterval(fetch, intervalMs);
      return () => clearInterval(id);
    }
  }, [autoRefresh, intervalMs]);

  return metrics;
};

export const useToggle = (initial = false): [boolean, () => void] => {
  const [value, setValue] = useState(initial);
  return [value, useCallback(() => setValue(v => !v), [])];
};
