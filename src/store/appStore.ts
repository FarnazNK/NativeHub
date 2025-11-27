import { create } from 'zustand';

interface User {
  id: string;
  email: string;
  name: string;
}

interface AppState {
  isInitialized: boolean;
  isLoading: boolean;
  user: User | null;
  isAuthenticated: boolean;
  setInitialized: (value: boolean) => void;
  setLoading: (value: boolean) => void;
  setUser: (user: User | null) => void;
  logout: () => void;
}

export const useAppStore = create<AppState>((set) => ({
  isInitialized: false,
  isLoading: false,
  user: null,
  isAuthenticated: false,
  setInitialized: (value) => set({ isInitialized: value }),
  setLoading: (value) => set({ isLoading: value }),
  setUser: (user) => set({ user, isAuthenticated: !!user }),
  logout: () => set({ user: null, isAuthenticated: false }),
}));

export const useIsAuthenticated = () => useAppStore((state) => state.isAuthenticated);
export const useUser = () => useAppStore((state) => state.user);
export const useIsLoading = () => useAppStore((state) => state.isLoading);
