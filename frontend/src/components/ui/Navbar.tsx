"use client";

import Link from "next/link";
import { useRouter, usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { motion } from "framer-motion";
import { useAuthStore, AuthState } from "@/store/authStore";

export function Navbar() {
    const pathname = usePathname();
    const router = useRouter();
    const isAuthenticated = useAuthStore((state: AuthState) => state.isAuthenticated);
    const logout = useAuthStore((state: AuthState) => state.logout);
    const user = useAuthStore((state: AuthState) => state.user);

    const handleLogout = () => {
        logout();
        router.push("/login");
    };

    const navItems = [
        { name: "Home", href: "/", public: true },
        ...(isAuthenticated ? [
            { name: "Dashboard", href: "/dashboard", public: false },
            { name: "Poems", href: "/poems", public: false },
            { name: "Thoughts", href: "/thoughts", public: false },
            { name: "Songs", href: "/songs", public: false },
        ] : []) // Login/Register moved to right side
    ];

    return (
        <nav className="fixed top-0 left-0 right-0 z-50 px-8 py-6">
            <div className="max-w-7xl mx-auto flex items-center justify-between">
                {/* Left: Logo */}
                <div className="flex-1">
                    <Link href="/" className="font-serif text-2xl tracking-tight font-bold bg-gradient-to-r from-white to-white/60 bg-clip-text text-transparent hover:opacity-80 transition-opacity">
                        After Midnight
                    </Link>
                </div>

                {/* Center: Navigation */}
                <div className="hidden md:flex flex-1 justify-center">
                    <div className="glass rounded-full px-6 py-2 flex items-center gap-1">
                        {navItems.map((item) => {
                            const isActive = pathname === item.href;
                            return (
                                <Link
                                    key={item.href}
                                    href={item.href}
                                    className={cn(
                                        "relative px-4 py-2 text-sm font-medium transition-colors hover:text-white rounded-full",
                                        isActive ? "text-white" : "text-white/60"
                                    )}
                                >
                                    {isActive && (
                                        <motion.div
                                            layoutId="navbar-indicator"
                                            className="absolute inset-0 bg-white/10 rounded-full z-[-1]"
                                            transition={{ type: "spring", bounce: 0.2, duration: 0.6 }}
                                        />
                                    )}
                                    {item.name}
                                </Link>
                            );
                        })}
                    </div>
                </div>

                {/* Right: User / Auth Actions */}
                <div className="flex-1 flex justify-end items-center gap-4">
                    {isAuthenticated && user ? (
                        <div className="flex items-center gap-4">
                            <div className="text-right hidden md:block">
                                <p className="text-sm font-medium text-white">{user.username}</p>
                                <p className="text-xs text-white/40">Member</p>
                            </div>
                            <button
                                onClick={handleLogout}
                                className="text-sm font-medium text-white/60 hover:text-red-300 transition-colors px-4 py-2 hover:bg-white/5 rounded-full"
                            >
                                Logout
                            </button>
                        </div>
                    ) : (
                        <div className="flex items-center gap-2">
                            <Link href="/login" className="px-4 py-2 text-sm font-medium text-white/80 hover:text-white transition-colors">
                                Login
                            </Link>
                            <Link href="/register" className="px-5 py-2 text-sm font-medium bg-white text-black rounded-full hover:bg-white/90 transition-colors">
                                Join
                            </Link>
                        </div>
                    )}
                </div>
            </div>
        </nav>
    );
}
