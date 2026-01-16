import type { Metadata } from "next";
import { Geist, Geist_Mono, Playfair_Display } from "next/font/google";
import "./globals.css";
import { Navbar } from "@/components/ui/Navbar";
import { Footer } from "@/components/ui/Footer";
import AccessGuard from "@/components/AccessGuard";
import { TabTitleHandler } from "@/components/TabTitleHandler";
import { Toaster } from "sonner";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

const playfair = Playfair_Display({
  variable: "--font-serif",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "After Midnight",
  description: "Some feelings sound louder in silence.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} ${playfair.variable} antialiased selection:bg-purple-500/30`}
      >
        <div className="fixed inset-0 z-[-1] bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-indigo-900/20 via-black to-black pointer-events-none" />
        <AccessGuard>
          <TabTitleHandler />
          <Navbar />
          <main className="min-h-screen pt-28 pb-16 px-4 md:px-8 max-w-6xl mx-auto">
            {children}
          </main>
          <Toaster richColors position="top-center" theme="dark" />
          <Footer />
        </AccessGuard>
      </body>
    </html>
  );
}
