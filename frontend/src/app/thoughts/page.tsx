"use client";

import React, { useEffect, useState, useRef, useCallback } from 'react';
import { ThoughtService } from '@/services/thought.service';
import { Thought, PublicationStatus } from '@/types/content';
import { Button } from '@/components/ui/Button';
import { Plus, Heart, Eye } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';
import { formatDistanceToNow } from 'date-fns';
import { motion, AnimatePresence } from 'framer-motion';

export default function ThoughtsPage() {
  const [thoughts, setThoughts] = useState<Thought[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [isLoading, setIsLoading] = useState(false);
  const observer = useRef<IntersectionObserver | null>(null);

  const lastElementRef = useCallback((node: HTMLDivElement) => {
    if (isLoading) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prev => prev + 1);
      }
    });
    if (node) observer.current.observe(node);
  }, [isLoading, hasMore]);

  const fetchThoughts = async (pageNum: number) => {
    setIsLoading(true);
    try {
      const response = await ThoughtService.getAll(pageNum, 10, 'PUBLISHED'); // Only published? Or all? User didn't specify. Assuming all for dashboard or published for feed. Feed usually published. Let's show all for user.
      // Actually, feed means public? But "Thoughts Feed Page".
      // Dashboard links to here.

      if (pageNum === 0) {
        setThoughts(response.content);
      } else {
        setThoughts(prev => [...prev, ...response.content]);
      }
      setHasMore(!response.last && response.content.length > 0);
    } catch (error) {
      toast.error("Failed to load thoughts");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchThoughts(page);
  }, [page]);

  return (
    <div className="max-w-2xl mx-auto space-y-12 pb-20">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-4xl font-serif">Thoughts</h1>
          <p className="text-white/60 mt-1">Fleeting moments, captured.</p>
        </div>
        <Link href="/thoughts/create">
          <Button className="w-auto px-4"><Plus size={18} className="mr-2" /> Share Thought</Button>
        </Link>
      </div>

      <div className="space-y-8">
        <AnimatePresence>
          {thoughts.map((thought, index) => (
            <motion.div
              key={thought.id}
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.4, delay: index * 0.05 }}
              className="bg-white/5 border border-white/5 rounded-2xl p-6 md:p-8 backdrop-blur-sm relative overflow-hidden group hover:bg-white/10 transition-colors"
            >
              <div className="absolute top-4 right-4 text-xs text-white/30 font-mono">
                {formatDistanceToNow(new Date(thought.createdAt), { addSuffix: true })}
              </div>

              <p className="text-xl md:text-2xl font-serif text-center py-8 text-white/90 leading-relaxed italic">
                "{thought.content}"
              </p>

              <div className="flex justify-between items-center mt-4 text-sm text-white/40 border-t border-white/5 pt-4">
                <span className={`px-2 py-0.5 rounded-full text-xs ${thought.status === 'PUBLISHED' ? 'bg-green-500/10 text-green-400' : 'bg-yellow-500/10 text-yellow-400'}`}>
                  {thought.status}
                </span>
                <div className="flex items-center gap-4">
                  <span className="flex items-center gap-1"><Eye size={14} /> {thought.viewCount}</span>
                  <span className="flex items-center gap-1"><Heart size={14} /> {thought.likeCount}</span>
                </div>
              </div>
            </motion.div>
          ))}
        </AnimatePresence>
      </div>

      {isLoading && (
        <div className="flex justify-center py-8">
          <div className="animate-spin h-6 w-6 border-t-2 border-white rounded-full"></div>
        </div>
      )}

      {!isLoading && hasMore && (
        <div ref={lastElementRef} className="h-10 bg-transparent" />
      )}

      {!isLoading && !hasMore && thoughts.length > 0 && (
        <p className="text-center text-white/30 text-sm">You have reached the end of your thoughts.</p>
      )}
    </div>
  );
}
