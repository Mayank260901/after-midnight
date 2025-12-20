"use client";

import { useEffect, useRef, useState } from "react";
import { api } from "@/lib/api";
import { motion } from "framer-motion";

export default function Music() {
  const [tracks, setTracks] = useState([]);
  const [current, setCurrent] = useState(null);
  const audioRef = useRef(null);

  useEffect(() => {
    api.get("/music").then(res => setTracks(res.data));
  }, []);

  const playTrack = (track) => {
    setCurrent(track);
    setTimeout(() => audioRef.current?.play(), 100);
  };

  return (
    <main className="min-h-screen p-10">
      <h1 className="text-2xl mb-8">Music</h1>

      <div className="grid gap-6 md:grid-cols-2">
        {tracks.map(track => (
          <motion.div
            key={track.id}
            whileHover={{ scale: 1.02 }}
            className="bg-white/5 p-6 rounded-xl cursor-pointer"
            onClick={() => playTrack(track)}
          >
            <h2 className="text-lg">{track.title}</h2>
            <p className="text-gray-400 text-sm">{track.mood}</p>
          </motion.div>
        ))}
      </div>

      {current && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="fixed bottom-6 left-1/2 -translate-x-1/2 bg-black/70 backdrop-blur px-6 py-4 rounded-xl"
        >
          <p className="bg-white/5 hover:bg-white/10 transition backdrop-blur border border-white/10 p-6 rounded-2xl"
>{current.title}</p>
          <audio ref={audioRef} controls src={current.audioUrl} />
        </motion.div>
      )}
    </main>
  );
}
