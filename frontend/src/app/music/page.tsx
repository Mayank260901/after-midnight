"use client";

import { useEffect, useRef, useState } from "react";
import { api } from "@/lib/api";
import { motion, AnimatePresence } from "framer-motion";
import { Card } from "@/components/ui/Card";
import { Play, Pause, Music as MusicIcon } from "lucide-react";

interface Track {
  id: string;
  title: string;
  mood: string;
  audioUrl: string;
}

export default function MusicPage() {
  const [tracks, setTracks] = useState<Track[]>([]);
  const [current, setCurrent] = useState<Track | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const audioRef = useRef<HTMLAudioElement>(null);

  useEffect(() => {
    api.get("/music")
      .then((res) => setTracks(res.data))
      .catch((err) => console.error("Failed to load music", err));
  }, []);

  const playTrack = (track: Track) => {
    if (current?.id === track.id) {
      if (isPlaying) {
        audioRef.current?.pause();
        setIsPlaying(false);
      } else {
        audioRef.current?.play();
        setIsPlaying(true);
      }
      return;
    }

    setCurrent(track);
    setIsPlaying(true);
    // Audio element auto-updates src calling play when ready usually, but we need to wait for render
    setTimeout(() => {
      if (audioRef.current) {
        audioRef.current.play().catch(e => console.error("Play error", e));
      }
    }, 100);
  };

  return (
    <div className="space-y-12">
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center space-y-4"
      >
        <h1 className="text-4xl font-serif">Music</h1>
        <p className="text-gray-400">Melodies for the midnight hours.</p>
      </motion.div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {tracks.map((track, i) => (
          <Card
            key={track.id}
            delay={i * 0.1}
            className="cursor-pointer group flex items-center justify-between hover:bg-white/10"
          >
            <div onClick={() => playTrack(track)} className="flex-1">
              <h2 className="text-lg font-medium group-hover:text-white transition-colors">
                {track.title}
              </h2>
              <p className="text-sm text-gray-400">{track.mood}</p>
            </div>
            <button
              onClick={() => playTrack(track)}
              className="p-3 bg-white/5 rounded-full hover:bg-white/20 transition-all text-white"
            >
              {current?.id === track.id && isPlaying ? <Pause className="w-5 h-5" /> : <Play className="w-5 h-5" />}
            </button>
          </Card>
        ))}
      </div>

      <AnimatePresence>
        {current && (
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: 50 }}
            className="fixed bottom-6 left-1/2 -translate-x-1/2 bg-black/80 backdrop-blur-xl border border-white/10 px-8 py-4 rounded-full flex items-center gap-6 shadow-2xl z-40"
          >
            <div className="flex flex-col">
              <span className="text-sm text-white font-medium">{current.title}</span>
              <span className="text-xs text-gray-400">Now Playing</span>
            </div>

            <audio
              ref={audioRef}
              src={current.audioUrl}
              onEnded={() => setIsPlaying(false)}
              onPause={() => setIsPlaying(false)}
              onPlay={() => setIsPlaying(true)}
              controls
              className="h-8 w-64 bg-transparent opacity-80"
            />
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
