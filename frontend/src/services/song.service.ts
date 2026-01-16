import { api } from '@/lib/api';
import { ApiResponse, PageResponse } from '@/types/common';
import { Song, CreateSongRequest, PublicationStatus } from '@/types/content';

export const SongService = {
    async getAll(page = 0, size = 10, status?: PublicationStatus): Promise<PageResponse<Song>> {
        const params = new URLSearchParams({ page: page.toString(), size: size.toString() });
        if (status) params.append('status', status);

        const response = await api.get<any, ApiResponse<PageResponse<Song>>>(`/songs?${params.toString()}`);
        return response.data;
    },

    async getById(id: number): Promise<Song> {
        const response = await api.get<any, ApiResponse<Song>>(`/songs/${id}`);
        return response.data;
    },

    async create(data: CreateSongRequest): Promise<Song> {
        const response = await api.post<any, ApiResponse<Song>>('/songs', data);
        return response.data;
    },

    async updateStatus(id: number, status: PublicationStatus): Promise<Song> {
        const response = await api.patch<any, ApiResponse<Song>>(`/songs/${id}/status`, null, { params: { status } });
        return response.data;
    },

    async delete(id: number): Promise<void> {
        await api.delete<any, ApiResponse<void>>(`/songs/${id}`);
    },

    async incrementView(id: number): Promise<void> {
        await api.post<any, ApiResponse<void>>(`/songs/${id}/view`);
    }
};
