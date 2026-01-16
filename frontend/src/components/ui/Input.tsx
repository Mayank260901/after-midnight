import React, { forwardRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { cn } from '@/lib/utils';
import { AlertCircle } from 'lucide-react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label: string;
    error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
    ({ className, label, error, ...props }, ref) => {
        return (
            <div className="relative mb-6 w-full">
                <input
                    ref={ref}
                    className={cn(
                        "peer w-full bg-transparent border-b-2 border-white/20 py-2 px-1 text-white placeholder-transparent focus:border-white focus:outline-none transition-colors duration-300",
                        error ? "border-red-400 focus:border-red-400" : "",
                        className
                    )}
                    placeholder={label}
                    {...props}
                />
                <label
                    className={cn(
                        "absolute left-0 -top-3.5 text-sm text-white/60 transition-all peer-placeholder-shown:text-base peer-placeholder-shown:text-white/40 peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-sm peer-focus:text-white",
                        error ? "text-red-400 peer-focus:text-red-400" : ""
                    )}
                >
                    {label}
                </label>
                <AnimatePresence>
                    {error && (
                        <motion.div
                            initial={{ opacity: 0, y: -10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: -10 }}
                            className="absolute left-0 top-full mt-1 flex items-center text-xs text-red-400"
                        >
                            <AlertCircle size={12} className="mr-1" />
                            {error}
                        </motion.div>
                    )}
                </AnimatePresence>
            </div>
        );
    }
);

Input.displayName = 'Input';
