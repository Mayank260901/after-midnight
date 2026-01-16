import { api } from '@/lib/api';
import { ApiResponse, PageResponse } from '@/types/common';
import { Poem, CreatePoemRequest, PublicationStatus } from '@/types/content';

export const PoemService = {
    async getAll(page = 0, size = 10, status?: PublicationStatus): Promise<PageResponse<Poem>> {
        const params = new URLSearchParams({ page: page.toString(), size: size.toString() });
        if (status) params.append('status', status);

        const response = await api.get<any, ApiResponse<PageResponse<Poem>>>(`/poems?${params.toString()}`);
        return response.data;
    },

    async getById(id: number): Promise<Poem> {
        const response = await api.get<any, ApiResponse<Poem>>(`/poems/${id}`);
        return response.data;
    },

    async create(data: CreatePoemRequest): Promise<Poem> {
        const response = await api.post<any, ApiResponse<Poem>>('/poems', data);
        return response.data;
    },

    async updateStatus(id: number, status: PublicationStatus): Promise<Poem> {
        const response = await api.patch<any, ApiResponse<Poem>>(`/poems/${id}/status`, null, { params: { status } });
        return response.data;
    },

    async delete(id: number): Promise<void> {
        await api.delete<any, ApiResponse<void>>(`/poems/${id}`);
    },

    async incrementView(id: number): Promise<void> {
        await api.post<any, ApiResponse<void>>(`/poems/${id}/view`);
    }
};
