"use client";

import React, { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/authStore';
import { PoemService } from '@/services/poem.service';
import { SongService } from '@/services/song.service';
import { ThoughtService } from '@/services/thought.service';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Feather, Music, Cloud, Plus } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';

export default function DashboardPage() {
    const { user } = useAuthStore();
    const [stats, setStats] = useState({ poems: 0, thoughts: 0, songs: 0 });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [poems, thoughts, songs] = await Promise.all([
                    PoemService.getAll(0, 1),
                    ThoughtService.getAll(0, 1),
                    SongService.getAll(0, 1)
                ]);
                setStats({
                    poems: poems.totalElements,
                    thoughts: thoughts.totalElements,
                    songs: songs.totalElements
                });
            } catch (error) {
                toast.error("Failed to load dashboard stats");
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    const statCards = [
        { title: 'Poems', count: stats.poems, icon: Feather, href: '/poems', color: 'text-purple-400' },
        { title: 'Thoughts', count: stats.thoughts, icon: Cloud, href: '/thoughts', color: 'text-blue-400' },
        { title: 'Songs', count: stats.songs, icon: Music, href: '/songs', color: 'text-indigo-400' },
    ];

    return (
        <div className="space-y-12 transition-all">
            <div className="space-y-2">
                <h1 className="text-4xl md:text-5xl font-serif">
                    Hello, <span className="text-white/80">{user?.username}</span>
                </h1>
                <p className="text-white/60 text-lg font-light">
                    What will you create today?
                </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {statCards.map((stat, index) => (
                    <Link key={stat.title} href={stat.href}>
                        <Card delay={index * 0.1} className="h-full flex flex-col justify-between group cursor-pointer">
                            <div className="flex justify-between items-start mb-4">
                                <stat.icon className={`w-8 h-8 ${stat.color} opacity-80 group-hover:opacity-100 transition-opacity`} />
                                <span className="text-3xl font-light font-mono">{loading ? '-' : stat.count}</span>
                            </div>
                            <div>
                                <h3 className="text-xl font-medium">{stat.title}</h3>
                                <p className="text-sm text-white/40 group-hover:text-white/60 transition-colors">View collection &rarr;</p>
                            </div>
                        </Card>
                    </Link>
                ))}
            </div>

            <div className="space-y-6">
                <h2 className="text-2xl font-serif text-white/80">Quick Actions</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <Link href="/poems/create">
                        <Button variant="secondary" className="w-full justify-start h-16 text-lg">
                            <Plus className="mr-3" /> New Poem
                        </Button>
                    </Link>
                    <Link href="/thoughts/create">
                        <Button variant="secondary" className="w-full justify-start h-16 text-lg">
                            <Plus className="mr-3" /> New Thought
                        </Button>
                    </Link>
                    <Link href="/songs/create">
                        <Button variant="secondary" className="w-full justify-start h-16 text-lg">
                            <Plus className="mr-3" /> New Song
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    );
}
