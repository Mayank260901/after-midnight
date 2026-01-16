import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface User {
    id: number;
    username: string;
    email: string;
    roles: string[];
}

export interface AuthState {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    login: (user: User, token: string) => void;
    logout: () => void;
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set: (partial: Partial<AuthState> | ((state: AuthState) => Partial<AuthState>)) => void) => ({
            user: null,
            token: null,
            isAuthenticated: false,
            login: (user: User, token: string) => set({ user, token, isAuthenticated: true }),
            logout: () => set({ user: null, token: null, isAuthenticated: false }),
        }),
        {
            name: 'auth-storage',
        }
    )
);
