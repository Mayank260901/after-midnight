"use client";

import React, { useEffect, useState } from 'react';
import { SongService } from '@/services/song.service';
import { Song } from '@/types/content';
import { Card } from '@/components/ui/Card'; // Assuming we reuse Card or make a new one
import { Pagination } from '@/components/ui/Pagination';
import { Button } from '@/components/ui/Button';
import { Plus, PlayCircle, Heart } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';

export default function SongsPage() {
    const [songs, setSongs] = useState<Song[]>([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    const fetchSongs = async (p: number) => {
        setIsLoading(true);
        try {
            const response = await SongService.getAll(p, 9);
            setSongs(response.content);
            setTotalPages(response.totalPages);
            setPage(response.pageNumber);
        } catch (error) {
            toast.error("Failed to load songs");
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchSongs(page);
    }, [page]);

    return (
        <div className="space-y-8">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-4xl font-serif">Music</h1>
                    <p className="text-white/60 mt-1">Melodies from the void</p>
                </div>
                <Link href="/songs/create">
                    <Button className="w-auto px-4"><Plus size={18} className="mr-2" /> Add Song</Button>
                </Link>
            </div>

            {isLoading ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {[1, 2, 3].map(i => <div key={i} className="h-40 bg-white/5 animate-pulse rounded-2xl" />)}
                </div>
            ) : songs.length === 0 ? (
                <div className="text-center py-20 bg-white/5 rounded-3xl border border-white/5">
                    <p className="text-white/40 mb-4">No songs yet.</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {songs.map((song, i) => (
                        <Link href={`/songs/${song.id}`} key={song.id}>
                            <Card delay={i * 0.05} className="h-full flex flex-col justify-between group hover:bg-white/10">
                                <div className="flex items-center gap-4">
                                    <div className="w-12 h-12 rounded-full bg-white/10 flex items-center justify-center text-white/80 group-hover:scale-110 transition-transform">
                                        <PlayCircle size={24} />
                                    </div>
                                    <div>
                                        <h3 className="text-lg font-medium group-hover:text-indigo-300 transition-colors line-clamp-1">{song.title}</h3>
                                        <p className="text-xs text-white/40">{song.status}</p>
                                    </div>
                                </div>
                                <div className="mt-4 flex justify-between items-center text-xs text-white/30">
                                    <span>{new Date(song.createdAt).getFullYear()}</span>
                                    <span className="flex items-center gap-1"><Heart size={12} /> {song.likeCount}</span>
                                </div>
                            </Card>
                        </Link>
                    ))}
                </div>
            )}
            <Pagination currentPage={page} totalPages={totalPages} onPageChange={setPage} />
        </div>
    );
}
