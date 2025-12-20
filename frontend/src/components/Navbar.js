"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { motion } from "framer-motion";

export default function Navbar() {
  const pathname = usePathname();

  const linkClass = (path) =>
    `transition ${
      pathname === path
        ? "text-white"
        : "text-gray-400 hover:text-white"
    }`;

  return (
    <motion.nav
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 1 }}
      className="fixed top-0 left-0 w-full z-50 bg-black/60 backdrop-blur border-b border-white/5"
    >
      <div className="max-w-6xl mx-auto px-6 py-4 flex justify-between items-center">
        {/* Logo */}
        <Link href="/" className="text-lg font-light tracking-wide">
          After Midnight
        </Link>

        {/* Links */}
        <div className="flex gap-6 text-sm">
          <Link href="/" className={linkClass("/")}>
            Home
          </Link>
          <Link href="/poems" className={linkClass("/poems")}>
            Poems
          </Link>
          <Link href="/music" className={linkClass("/music")}>
            Music
          </Link>

          <Link href="/thoughts" className={linkClass("/thoughts")}>
  Thoughts
</Link>
        </div>
      </div>
    </motion.nav>
  );
}
