import React from 'react';
import { motion, HTMLMotionProps } from 'framer-motion';
import { cn } from '@/lib/utils';
import { Loader2 } from 'lucide-react';

interface ButtonProps extends React.ComponentProps<typeof motion.button> {
    isLoading?: boolean;
    variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
    children: React.ReactNode;
}

export const Button = ({
    className,
    children,
    isLoading,
    variant = 'primary',
    disabled,
    ...props
}: ButtonProps) => {
    const variants = {
        primary: "bg-white text-black hover:bg-white/90",
        secondary: "bg-white/10 text-white hover:bg-white/20 border border-white/10",
        ghost: "bg-transparent text-white/70 hover:text-white hover:bg-white/5",
        danger: "bg-red-500/10 text-red-400 hover:bg-red-500/20 border border-red-500/20",
    };

    return (
        <motion.button
            whileHover={{ scale: 1.02 }}
            whileTap={{ scale: 0.98 }}
            className={cn(
                "relative flex items-center justify-center px-6 py-3 rounded-lg font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed w-full",
                variants[variant],
                className
            )}
            disabled={disabled || isLoading}
            {...props}
        >
            {isLoading ? (
                <Loader2 className="animate-spin mr-2" size={18} />
            ) : null}
            {children}
        </motion.button>
    );
};
