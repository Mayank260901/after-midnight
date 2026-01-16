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
import { RegisterRequest } from '@/types/auth';

export default function RegisterPage() {
    const router = useRouter();
    const login = useAuthStore((state) => state.login);
    const [isLoading, setIsLoading] = useState(false);

    const { register, handleSubmit, watch, formState: { errors } } = useForm<RegisterRequest>();
    const password = watch('password');

    const onSubmit = async (data: RegisterRequest) => {
        setIsLoading(true);
        try {
            const response = await AuthService.register(data);
            login(response.user, response.token);
            toast.success('Account created successfully!');
            router.push('/dashboard');
        } catch (error: any) {
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
                        Join Us
                    </h1>
                    <p className="text-white/60 text-sm">Create your space to express</p>
                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <Input
                        label="Username"
                        type="text"
                        {...register('username', { required: 'Username is required', minLength: { value: 3, message: 'Minimum 3 characters' } })}
                        error={errors.username?.message}
                    />

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
                        {...register('password', {
                            required: 'Password is required',
                            minLength: { value: 6, message: 'Minimum 6 characters' }
                        })}
                        error={errors.password?.message}
                    />

                    {password && password.length < 6 && (
                        <p className="text-xs text-yellow-400/80 -mt-2">Password is too weak</p>
                    )}

                    <Button type="submit" isLoading={isLoading} className="mt-4">
                        Sign Up
                    </Button>
                </form>

                <div className="mt-6 text-center text-sm text-white/40">
                    Already have an account?{' '}
                    <Link href="/login" className="text-white hover:text-purple-300 transition-colors">
                        Login here
                    </Link>
                </div>
            </motion.div>
        </div>
    );
}
