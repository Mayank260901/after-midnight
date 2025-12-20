"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { motion } from "framer-motion";
import { Playfair_Display } from "next/font/google";

const playfair = Playfair_Display({
  subsets: ["latin"],
  weight: ["400", "500"],
});


export default function Thoughts() {
  const [thoughts, setThoughts] = useState([]);

  useEffect(() => {
    api.get("/thoughts").then(res => setThoughts(res.data));
  }, []);

  return (
    <main className="scroll-smooth">
      {thoughts.map((thought, index) => (
        <section
          key={thought.id}
          className="min-h-screen flex items-center justify-center px-8"
        >
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 1.2 }}
            viewport={{ once: true }}
            className="max-w-2xl text-center"
          >
           <p className={`${playfair.className} text-2xl md:text-3xl font-light leading-relaxed text-gray-200`}>
  {thought.text}
</p>
            <div className="mt-6 text-sm text-gray-500">
              {thought.timeStamp}
            </div>
           
          </motion.div>
          
        </section>
        
      ))} <footer className="py-16 text-center text-gray-600 text-sm">
  some things are meant to be felt, not explained
</footer>
    </main>
  );
}

