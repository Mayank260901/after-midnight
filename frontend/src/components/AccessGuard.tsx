"use client";

import { useEffect, useState } from "react";
import { usePathname, useRouter } from "next/navigation";
import { useAuthStore, AuthState } from "@/store/authStore";
import { motion, AnimatePresence } from "framer-motion";

const PUBLIC_PATHS = ["/login", "/register", "/"];

export default function AccessGuard({ children }: { children: React.ReactNode }) {
    const pathname = usePathname();
    const router = useRouter();
    const isAuthenticated = useAuthStore((state: AuthState) => state.isAuthenticated);
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(true);
    }, []);

    useEffect(() => {
        if (!mounted) return;

        if (!isAuthenticated && !PUBLIC_PATHS.includes(pathname)) {
            router.push("/login");
        } else if (isAuthenticated && PUBLIC_PATHS.includes(pathname)) {
            router.push("/dashboard");
        }
    }, [mounted, isAuthenticated, pathname, router]);

    // Prevent flash of content logic
    if (!mounted) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-black text-white/50">
                <div className="animate-pulse">Initializing...</div>
            </div>
        );
    }

    // If we are checking auth and on a protected route, show loading or nothing
    if (!isAuthenticated && !PUBLIC_PATHS.includes(pathname)) {
        return null;
    }

    return (
        <>
            {children}
        </>
    );
}
