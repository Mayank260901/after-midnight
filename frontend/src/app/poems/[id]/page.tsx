"use client";

import React, { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { PoemService } from "@/services/poem.service";
import { Poem } from "@/types/content";
import { toast } from "sonner";
import { Button } from "@/components/ui/Button";
import { ArrowLeft, Trash2 } from "lucide-react";
import { format } from "date-fns";
import Link from "next/link";
import { motion } from "framer-motion";

export default function PoemDetailPage() {
    const { id } = useParams();
    const router = useRouter();
    const [poem, setPoem] = useState<Poem | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadPoem = async () => {
            try {
                const data = await PoemService.getById(Number(id));
                setPoem(data);
                // Increment view count
                PoemService.incrementView(Number(id)).catch(() => { });
            } catch (e) {
                toast.error("Poem not found");
                router.push("/poems");
            } finally {
                setLoading(false);
            }
        };
        if (id) loadPoem();
    }, [id, router]);

    const handleDelete = async () => {
        if (!confirm("Are you sure you want to delete this poem?")) return;
        try {
            await PoemService.delete(Number(id));
            toast.success("Poem deleted");
            router.push("/poems");
        } catch (e) {
            toast.error("Failed to delete poem");
        }
    };

    if (loading) return <div className="flex justify-center py-20"><div className="animate-spin h-8 w-8 border-t-2 border-white rounded-full" /></div>;
    if (!poem) return null;

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="max-w-3xl mx-auto"
        >
            <div className="mb-8 flex justify-between items-center">
                <Link href="/poems" className="text-white/50 hover:text-white flex items-center gap-2">
                    <ArrowLeft size={18} /> Back
                </Link>
                <div className="flex gap-2">
                    {/* Add Edit button if needed later */}
                    <Button variant="danger" onClick={handleDelete} className="w-auto px-3 py-2 text-sm bg-red-500/10 hover:bg-red-500/20 text-red-200">
                        <Trash2 size={16} className="mr-2" /> Delete
                    </Button>
                </div>
            </div>

            <div className="text-center space-y-4 mb-16">
                <h1 className="text-4xl md:text-6xl font-serif bg-gradient-to-br from-white to-white/60 bg-clip-text text-transparent">
                    {poem.title}
                </h1>
                <div className="flex justify-center items-center gap-4 text-sm text-white/40 font-mono">
                    <span>{format(new Date(poem.createdAt), "MMMM d, yyyy")}</span>
                    <span>•</span>
                    <span>{poem.viewCount} Views</span>
                </div>
            </div>

            <article className="prose prose-invert prose-lg mx-auto text-center font-serif leading-loose text-white/80 whitespace-pre-wrap">
                {poem.content}
            </article>

        </motion.div>
    );
}
