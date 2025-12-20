"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { motion } from "framer-motion";

export default function Poems() {
  const [poems, setPoems] = useState([]);

  useEffect(() => {
    api.get("/poems").then(res => setPoems(res.data));
  }, []);

  return (
    <main className="min-h-screen p-10">
      <h1 className="text-2xl mb-8">Poems</h1>

      <div className="space-y-6">
        {poems.map(poem => (
          <motion.div
            key={poem.id}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 1 }}
            className="bg-white/5 p-6 rounded-xl backdrop-blur"
          >
            <h2 className="text-xl mb-2">{poem.title}</h2>
            <p className="text-gray-300 whitespace-pre-line">
              {poem.content}
            </p>
          </motion.div>
        ))}
      </div>
    </main>
  );
}
