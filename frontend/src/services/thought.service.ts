import { api } from '@/lib/api';
import { ApiResponse, PageResponse } from '@/types/common';
import { Thought, CreateThoughtRequest, PublicationStatus } from '@/types/content';

export const ThoughtService = {
    async getAll(page = 0, size = 10, status?: PublicationStatus): Promise<PageResponse<Thought>> {
        const params = new URLSearchParams({ page: page.toString(), size: size.toString() });
        if (status) params.append('status', status);

        const response = await api.get<any, ApiResponse<PageResponse<Thought>>>(`/thoughts?${params.toString()}`);
        return response.data;
    },

    async getById(id: number): Promise<Thought> {
        const response = await api.get<any, ApiResponse<Thought>>(`/thoughts/${id}`);
        return response.data;
    },

    async create(data: CreateThoughtRequest): Promise<Thought> {
        const response = await api.post<any, ApiResponse<Thought>>('/thoughts', data);
        return response.data;
    },

    async updateStatus(id: number, status: PublicationStatus): Promise<Thought> {
        const response = await api.patch<any, ApiResponse<Thought>>(`/thoughts/${id}/status`, null, { params: { status } });
        return response.data;
    },

    async delete(id: number): Promise<void> {
        await api.delete<any, ApiResponse<void>>(`/thoughts/${id}`);
    },

    async incrementView(id: number): Promise<void> {
        await api.post<any, ApiResponse<void>>(`/thoughts/${id}/view`);
    }
};
