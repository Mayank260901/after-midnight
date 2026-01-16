"use client";

import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useRouter } from 'next/navigation';
import { SongService } from '@/services/song.service';
import { CreateSongRequest } from '@/types/content';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { ArrowLeft } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';

export default function CreateSongPage() {
    const router = useRouter();
    const [isLoading, setIsLoading] = useState(false);
    const { register, handleSubmit, setValue, watch, formState: { errors } } = useForm<CreateSongRequest>({
        defaultValues: { status: 'DRAFT' }
    });

    const status = watch('status');

    const onSubmit = async (data: CreateSongRequest) => {
        setIsLoading(true);
        try {
            await SongService.create(data);
            toast.success('Song added successfully');
            router.push('/songs');
        } catch (error) {
            toast.error('Failed to add song');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto space-y-8">
            <Link href="/songs" className="inline-flex items-center text-white/50 hover:text-white transition-colors">
                <ArrowLeft size={16} className="mr-2" /> Back to Songs
            </Link>

            <h1 className="text-3xl font-serif">New Song</h1>

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <Input
                    label="Title"
                    {...register('title', { required: 'Title is required' })}
                    error={errors.title?.message}
                />

                <Input
                    label="Audio URL (Optional)"
                    placeholder="https://example.com/song.mp3"
                    {...register('audioUrl')}
                />

                <div className="space-y-2">
                    <label className="text-sm text-white/60">Lyrics</label>
                    <textarea
                        {...register('lyrics', { required: 'Lyrics are required' })}
                        className="w-full h-64 bg-transparent border-2 border-white/10 rounded-xl p-4 text-white placeholder-white/20 focus:border-white/40 focus:outline-none transition-colors resize-none font-mono text-sm leading-relaxed"
                        placeholder="Verse 1..."
                    />
                    {errors.lyrics && <span className="text-xs text-red-400">{errors.lyrics.message}</span>}
                </div>

                <div className="flex justify-between items-center">
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
                    <Button type="submit" isLoading={isLoading} className="w-auto px-8">
                        Save Song
                    </Button>
                </div>
            </form>
        </div>
    );
}
