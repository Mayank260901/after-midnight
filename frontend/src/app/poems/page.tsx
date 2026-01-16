"use client";

import React, { useEffect, useState } from 'react';
import { PoemService } from '@/services/poem.service';
import { Poem, PublicationStatus } from '@/types/content';
import { Card } from '@/components/ui/Card';
import { Pagination } from '@/components/ui/Pagination';
import { Button } from '@/components/ui/Button';
import { Plus, Eye, Heart } from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';
import { format } from 'date-fns';

export default function PoemsPage() {
  const [poems, setPoems] = useState<Poem[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState<PublicationStatus | undefined>(undefined);

  const fetchPoems = async (p: number, status?: PublicationStatus) => {
    setIsLoading(true);
    try {
      const response = await PoemService.getAll(p, 9, status);
      setPoems(response.content);
      setTotalPages(response.totalPages);
      setPage(response.pageNumber);
    } catch (error) {
      toast.error("Failed to load poems");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchPoems(page, statusFilter);
  }, [page, statusFilter]);

  return (
    <div className="space-y-8">
      <div className="flex flex-col md:flex-row justify-between items-center gap-4">
        <div>
          <h1 className="text-4xl font-serif">Poems</h1>
          <p className="text-white/60 mt-1">Fragments of soul in text</p>
        </div>
        <div className="flex items-center gap-3">
          <select
            className="bg-white/5 border border-white/10 rounded-lg px-3 py-2 text-sm focus:outline-none"
            value={statusFilter || ''}
            onChange={(e) => {
              const val = e.target.value as PublicationStatus | '';
              setStatusFilter(val === '' ? undefined : val);
              setPage(0);
            }}
          >
            <option value="">All Poems</option>
            <option value="PUBLISHED">Published</option>
            <option value="DRAFT">Drafts</option>
          </select>
          <Link href="/poems/create">
            <Button className="w-auto px-4"><Plus size={18} className="mr-2" /> Create</Button>
          </Link>
        </div>
      </div>

      {isLoading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3].map(i => (
            <div key={i} className="h-64 rounded-2xl bg-white/5 animate-pulse" />
          ))}
        </div>
      ) : poems.length === 0 ? (
        <div className="text-center py-20 bg-white/5 rounded-3xl border border-white/5">
          <p className="text-white/40 mb-4">No poems found.</p>
          <Link href="/poems/create">
            <Button variant="secondary" className="w-auto">Write your first poem</Button>
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {poems.map((poem, i) => (
            <Link href={`/poems/${poem.id}`} key={poem.id}>
              <Card delay={i * 0.05} className="h-full flex flex-col justify-between group hover:bg-white/10">
                <div>
                  <div className="flex justify-between items-start mb-2">
                    <span className={`text-xs px-2 py-1 rounded-full ${poem.status === 'PUBLISHED' ? 'bg-green-500/20 text-green-300' : 'bg-yellow-500/20 text-yellow-300'}`}>
                      {poem.status}
                    </span>
                    <span className="text-xs text-white/40 font-mono">
                      {format(new Date(poem.createdAt), 'MMM d, yyyy')}
                    </span>
                  </div>
                  <h3 className="text-xl font-medium mb-3 group-hover:text-purple-300 transition-colors line-clamp-2">
                    {poem.title}
                  </h3>
                  <p className="text-white/60 text-sm line-clamp-4 font-serif leading-relaxed">
                    {poem.content}
                  </p>
                </div>
                <div className="mt-6 flex items-center gap-4 text-xs text-white/40">
                  <div className="flex items-center gap-1"><Eye size={12} /> {poem.viewCount}</div>
                  <div className="flex items-center gap-1"><Heart size={12} /> {poem.likeCount}</div>
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
