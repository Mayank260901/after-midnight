import { Button } from "./Button";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface PaginationProps {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

export function Pagination({ currentPage, totalPages, onPageChange }: PaginationProps) {
    if (totalPages <= 1) return null;

    return (
        <div className="flex items-center justify-center space-x-4 mt-8">
            <Button
                variant="ghost"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 0}
                className="w-auto px-2"
            >
                <ChevronLeft />
            </Button>
            <span className="text-white/60 font-mono text-sm">
                {currentPage + 1} / {totalPages}
            </span>
            <Button
                variant="ghost"
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage >= totalPages - 1}
                className="w-auto px-2"
            >
                <ChevronRight />
            </Button>
        </div>
    );
}
