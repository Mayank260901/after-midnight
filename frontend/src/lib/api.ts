import axios from 'axios';
import { toast } from 'sonner';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

export const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    if (typeof window !== 'undefined') {
      const storage = localStorage.getItem('auth-storage');
      if (storage) {
        try {
          const { state } = JSON.parse(storage);
          if (state?.token) {
            config.headers.Authorization = `Bearer ${state.token}`;
          }
        } catch (e) {
          console.error("Failed to parse auth storage", e);
        }
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const message = error.response?.data?.message || error.message || 'Something went wrong';

    if (error.response?.status === 401) {
      toast.error('Session expired. Please login again.');
      if (typeof window !== 'undefined') {
        // We will handle logout in the store or component, 
        // but checking here ensures we catch token expiry everywhere.
        // Dispatching a custom event or directly manipulating storage might be needed if store isn't accessible here.
        // For now, let's just let the UI handle the redirect based on auth state/guard.
        // Ideally, we'd import the store but circular dependencies can happen.
        localStorage.removeItem('auth-storage');
        window.location.href = '/login';
      }
    } else {
      toast.error(message);
    }
    return Promise.reject(error);
  }
);
