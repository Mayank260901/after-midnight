"use client";

import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useRouter } from 'next/navigation';
import { ThoughtService } from '@/services/thought.service';
import { CreateThoughtRequest } from '@/types/content';
import { Button } from '@/components/ui/Button';
import { ArrowLeft } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';

export default function CreateThoughtPage() {
    const router = useRouter();
    const [isLoading, setIsLoading] = useState(false);
    const { register, handleSubmit, setValue, watch, formState: { errors } } = useForm<CreateThoughtRequest>({
        defaultValues: { status: 'PUBLISHED' }
    });

    const status = watch('status');

    const onSubmit = async (data: CreateThoughtRequest) => {
        setIsLoading(true);
        try {
            await ThoughtService.create(data);
            toast.success('Thought shared');
            router.push('/thoughts');
        } catch (error) {
            toast.error('Failed to share thought');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="max-w-xl mx-auto pt-20">
            <Link href="/thoughts" className="inline-flex items-center text-white/50 hover:text-white transition-colors mb-8">
                <ArrowLeft size={16} className="mr-2" /> Back to Thoughts
            </Link>

            <h1 className="text-3xl font-serif mb-8 text-center">What's on your mind?</h1>

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <div className="relative">
                    <textarea
                        {...register('content', {
                            required: 'Content is required',
                            maxLength: { value: 280, message: 'Keep it short (max 280 chars)' }
                        })}
                        className="w-full h-40 bg-white/5 border border-white/10 rounded-2xl p-6 text-white text-xl font-serif placeholder-white/20 focus:border-white/30 focus:outline-none transition-colors resize-none text-center"
                        placeholder="Type your thought here..."
                    />
                    {errors.content && <p className="absolute -bottom-6 left-0 text-xs text-red-400">{errors.content.message}</p>}
                </div>

                <div className="flex justify-center space-x-2 pb-4">
                    {(['DRAFT', 'PUBLISHED'] as const).map((s) => (
                        <button
                            key={s}
                            type="button"
                            onClick={() => setValue('status', s)}
                            className={`px-3 py-1 rounded-full text-xs font-medium transition-all ${status === s ? 'bg-white text-black' : 'bg-white/10 text-white/60 hover:bg-white/20'}`}
                        >
                            {s}
                        </button>
                    ))}
                </div>

                <Button type="submit" isLoading={isLoading} className="w-full py-4 text-lg">
                    Share Thought
                </Button>
            </form>
        </div>
    );
}
