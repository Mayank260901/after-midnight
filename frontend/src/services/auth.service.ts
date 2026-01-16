import { api } from '@/lib/api';
import { LoginRequest, RegisterRequest, AuthResponse } from '@/types/auth';
import { ApiResponse } from '@/types/common';

export const AuthService = {
    async register(data: RegisterRequest): Promise<AuthResponse> {
        const response = await api.post<any, ApiResponse<AuthResponse>>('/auth/register', data);
        return response.data;
    },

    async login(data: LoginRequest): Promise<AuthResponse> {
        const response = await api.post<any, ApiResponse<AuthResponse>>('/auth/login', data);
        return response.data;
    }
};
