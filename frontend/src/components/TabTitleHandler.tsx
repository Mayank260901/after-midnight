"use client";

import { useEffect, useRef } from 'react';

export function TabTitleHandler() {
    const originalTitle = useRef<string>("");

    useEffect(() => {
        originalTitle.current = document.title;

        const handleVisibilityChange = () => {
            if (document.hidden) {
                document.title = "Come back to the void...";
            } else {
                document.title = originalTitle.current;
            }
        };

        document.addEventListener('visibilitychange', handleVisibilityChange);

        return () => {
            document.removeEventListener('visibilitychange', handleVisibilityChange);
            document.title = originalTitle.current;
        };
    }, []);

    return null;
}
