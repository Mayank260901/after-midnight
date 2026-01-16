export type PublicationStatus = 'DRAFT' | 'PUBLISHED';

export interface BaseContent {
    id: number;
    createdAt: string;
    status: PublicationStatus;
    publishedAt?: string;
    viewCount: number;
    likeCount: number;
    userId: number;
}

export interface Poem extends BaseContent {
    title: string;
    content: string;
}

export interface Thought extends BaseContent {
    content: string;
}

export interface Song extends BaseContent {
    title: string;
    lyrics: string;
    audioUrl?: string;
}

// Request Types
export interface CreatePoemRequest {
    title: string;
    content: string;
    status?: PublicationStatus;
}

export interface CreateThoughtRequest {
    content: string;
    status?: PublicationStatus;
}

export interface CreateSongRequest {
    title: string;
    lyrics: string;
    audioUrl?: string;
    status?: PublicationStatus;
}
