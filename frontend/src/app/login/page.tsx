"use client";

import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { motion } from 'framer-motion';
import { useRouter } from 'next/navigation';
import { toast } from 'sonner';
import { AuthService } from '@/services/auth.service';
import { useAuthStore } from '@/store/authStore';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import Link from 'next/link';
import { LoginRequest } from '@/types/auth';

export default function LoginPage() {
    const router = useRouter();
    const login = useAuthStore((state) => state.login);
    const [isLoading, setIsLoading] = useState(false);

    const { register, handleSubmit, formState: { errors } } = useForm<LoginRequest>();

    const onSubmit = async (data: LoginRequest) => {
        setIsLoading(true);
        try {
            const response = await AuthService.login(data);
            login(response.user, response.token);
            toast.success('Welcome back, ' + response.user.username);
            router.push('/dashboard');
        } catch (error: any) {
            // Error handled by interceptor ideally, but for specific form errors:
            console.error(error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-[70vh]">
            <motion.div
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                className="w-full max-w-md p-8 rounded-2xl bg-white/5 border border-white/10 backdrop-blur-xl shadow-2xl"
            >
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-serif mb-2 bg-gradient-to-r from-purple-200 to-indigo-200 bg-clip-text text-transparent">
                        Welcome Back
                    </h1>
                    <p className="text-white/60 text-sm">Sign in to continue your journey</p>
                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <Input
                        label="Email"
                        type="email"
                        {...register('email', {
                            required: 'Email is required',
                            pattern: { value: /^\S+@\S+$/i, message: 'Invalid email address' }
                        })}
                        error={errors.email?.message}
                    />

                    <Input
                        label="Password"
                        type="password"
                        {...register('password', { required: 'Password is required' })}
                        error={errors.password?.message}
                    />

                    <Button type="submit" isLoading={isLoading} className="mt-4">
                        Sign In
                    </Button>
                </form>

                <div className="mt-6 text-center text-sm text-white/40">
                    Don't have an account?{' '}
                    <Link href="/register" className="text-white hover:text-purple-300 transition-colors">
                        Register for free
                    </Link>
                </div>
            </motion.div>
        </div>
    );
}
