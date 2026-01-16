"use client";

import { motion } from "framer-motion";
import { Card } from "@/components/ui/Card";
import Link from "next/link";
import { Music, Feather, Brain } from "lucide-react";

export default function Home() {
  const sections = [
    {
      title: "Music",
      description: "Melodies for the midnight hours.",
      icon: <Music className="w-6 h-6" />,
      href: "/music",
      color: "via-purple-500/10"
    },
    {
      title: "Poems",
      description: "Words woven in silence.",
      icon: <Feather className="w-6 h-6" />,
      href: "/poems",
      color: "via-rose-500/10"
    },
    {
      title: "Thoughts",
      description: "Fragments of a wandering mind.",
      icon: <Brain className="w-6 h-6" />,
      href: "/thoughts",
      color: "via-teal-500/10"
    }
  ];

  return (
    <div className="flex flex-col items-center justify-center min-h-[70vh] space-y-16">
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1, ease: "easeOut" }}
        className="text-center space-y-6"
      >
        <h1 className="text-5xl md:text-8xl font-serif tracking-tight bg-gradient-to-b from-white via-white/80 to-white/40 bg-clip-text text-transparent drop-shadow-2xl">
          After Midnight
        </h1>
        <p className="text-lg md:text-xl text-gray-400 font-light tracking-wide max-w-lg mx-auto">
          Some feelings sound louder in silence.
        </p>
      </motion.div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-4xl px-4">
        {sections.map((section, index) => (
          <Link key={section.title} href={section.href} className="block group">
            <Card
              delay={0.5 + (index * 0.1)}
              className={`h-full flex flex-col items-center text-center space-y-4 group-hover:bg-gradient-to-b from-white/5 ${section.color} to-transparent`}
            >
              <div className="p-4 bg-white/5 rounded-full ring-1 ring-white/10 group-hover:ring-white/30 transition-all duration-300">
                {section.icon}
              </div>
              <h2 className="text-xl font-medium text-white tracking-wide">{section.title}</h2>
              <p className="text-sm text-gray-500 group-hover:text-gray-300 transition-colors">{section.description}</p>
            </Card>
          </Link>
        ))}
      </div>
    </div>
  );
}
