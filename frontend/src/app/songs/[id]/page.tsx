"use client";

import React, { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { SongService } from "@/services/song.service";
import { Song } from "@/types/content";
import { toast } from "sonner";
import { Button } from "@/components/ui/Button";
import { ArrowLeft, Trash2, Disc } from "lucide-react";
import Link from "next/link";
import { motion } from "framer-motion";

export default function SongDetailPage() {
    const { id } = useParams();
    const router = useRouter();
    const [song, setSong] = useState<Song | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadSong = async () => {
            try {
                const data = await SongService.getById(Number(id));
                setSong(data);
                SongService.incrementView(Number(id)).catch(() => { });
            } catch (e) {
                toast.error("Song not found");
                router.push("/songs");
            } finally {
                setLoading(false);
            }
        };
        if (id) loadSong();
    }, [id, router]);

    const handleDelete = async () => {
        if (!confirm("Delete this song?")) return;
        try {
            await SongService.delete(Number(id));
            toast.success("Song deleted");
            router.push("/songs");
        } catch (e) {
            toast.error("Failed to delete song");
        }
    };

    if (loading) return <div className="flex justify-center py-20"><div className="animate-spin h-8 w-8 border-t-2 border-white rounded-full" /></div>;
    if (!song) return null;

    return (
        <motion.div
            initial={{ opacity: 0, scale: 0.98 }}
            animate={{ opacity: 1, scale: 1 }}
            className="max-w-4xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-12"
        >
            <div className="space-y-8">
                <Link href="/songs" className="text-white/50 hover:text-white flex items-center gap-2">
                    <ArrowLeft size={18} /> Back to Music
                </Link>

                <div className="relative aspect-square bg-gradient-to-br from-indigo-900/40 to-black rounded-3xl border border-white/10 flex items-center justify-center shadow-2xl overflow-hidden">
                    <Disc size={120} className="text-white/10 animate-spin-slow duration-[10s]" />
                    <div className="absolute inset-0 flex flex-col items-center justify-center p-8 text-center">
                        <h1 className="text-3xl md:text-4xl font-serif font-bold mb-2">{song.title}</h1>
                        {song.audioUrl && (
                            <audio controls className="mt-8 w-full max-w-xs">
                                <source src={song.audioUrl} />
                                Your browser does not support the audio element.
                            </audio>
                        )}
                        {!song.audioUrl && <p className="text-white/30 text-sm mt-4 italic">No audio source</p>}
                    </div>
                </div>

                <div className="flex justify-between items-center px-2">
                    <span className="text-xs text-white/40">Uploaded on {new Date(song.createdAt).toLocaleDateString()}</span>
                    <Button variant="danger" onClick={handleDelete} className="w-auto px-3 bg-transparent border-red-500/20 text-red-400 hover:bg-red-500/10">
                        <Trash2 size={16} />
                    </Button>
                </div>
            </div>

            <div className="bg-white/5 rounded-3xl p-8 border border-white/5 max-h-[600px] overflow-y-auto custom-scrollbar">
                <h2 className="text-lg font-medium mb-6 text-white/60 uppercase tracking-widest text-center">Lyrics</h2>
                <pre className="font-mono text-sm leading-relaxed whitespace-pre-wrap text-white/80 text-center">
                    {song.lyrics}
                </pre>
            </div>
        </motion.div>
    );
}
