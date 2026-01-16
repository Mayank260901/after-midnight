"use client";

import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useRouter } from 'next/navigation';
import { PoemService } from '@/services/poem.service';
import { CreatePoemRequest, PublicationStatus } from '@/types/content';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { toast } from 'sonner';
import { ArrowLeft } from 'lucide-react';
import Link from 'next/link';

export default function CreatePoemPage() {
    const router = useRouter();
    const [isLoading, setIsLoading] = useState(false);
    const { register, handleSubmit, watch, setValue, formState: { errors } } = useForm<CreatePoemRequest>({
        defaultValues: {
            status: 'DRAFT'
        }
    });

    const status = watch('status');

    const onSubmit = async (data: CreatePoemRequest) => {
        setIsLoading(true);
        try {
            await PoemService.create(data);
            toast.success('Poem saved successfully');
            router.push('/poems');
        } catch (error) {
            toast.error('Failed to create poem');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto space-y-8">
            <Link href="/poems" className="inline-flex items-center text-white/50 hover:text-white transition-colors">
                <ArrowLeft size={16} className="mr-2" /> Back to Poems
            </Link>

            <div className="space-y-2">
                <h1 className="text-3xl font-serif">Compose Poem</h1>
                <p className="text-white/60">Let your words flow</p>
            </div>

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <Input
                    label="Title"
                    {...register('title', { required: 'Title is required' })}
                    error={errors.title?.message}
                />

                <div className="space-y-2">
                    <label className="text-sm text-white/60">Content</label>
                    <textarea
                        {...register('content', { required: 'Content is required' })}
                        className="w-full h-64 bg-transparent border-2 border-white/10 rounded-xl p-4 text-white placeholder-white/20 focus:border-white/40 focus:outline-none transition-colors resize-none font-serif text-lg leading-relaxed"
                        placeholder="Write something beautiful..."
                    />
                    {errors.content && <span className="text-xs text-red-400">{errors.content.message}</span>}
                </div>

                <div className="flex items-center justify-between pt-4">
                    <div className="flex items-center space-x-4">
                        <label className="text-sm font-medium text-white/80">Status:</label>
                        <div className="flex bg-white/5 rounded-lg p-1">
                            {(['DRAFT', 'PUBLISHED'] as const).map((s) => (
                                <button
                                    key={s}
                                    type="button"
                                    onClick={() => setValue('status', s)}
                                    className={`px-4 py-1.5 rounded-md text-xs font-medium transition-all ${status === s ? 'bg-white text-black shadow-lg' : 'text-white/50 hover:text-white'}`}
                                >
                                    {s}
                                </button>
                            ))}
                        </div>
                    </div>

                    <Button type="submit" isLoading={isLoading} className="w-auto px-8">
                        Save Poem
                    </Button>
                </div>
            </form>
        </div>
    );
}
