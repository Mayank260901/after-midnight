"use client";

import { motion } from "framer-motion";

export default function Home() {
  return (
    <main className="min-h-screen flex items-center justify-center">
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 2 }}
        className="text-center"
      >
        <h1 className="text-3xl mb-4">After Midnight</h1>
        <p className="text-gray-400">
          Some feelings sound louder in silence.
        </p>
      </motion.div>
    </main>
  );
}
