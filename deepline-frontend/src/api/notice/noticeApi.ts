import type {
  NoticeDetailApiResponse,
  NoticeListApiResponse,
  NoticeRequest,
} from "@/types/notice/notice.types.ts";
import type { BaseResponse } from "@/types/BaseResponse.ts";
import { deeplineApi } from "@/lib/api/clients.ts";

const createNotice = async (data: NoticeRequest): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.post<BaseResponse<void>>("/notices", data);
  return response.data;
};

const getNoticeList = async (page = 0, size = 10): Promise<NoticeListApiResponse> => {
  const response = await deeplineApi.get<NoticeListApiResponse>("/notices", {
    params: { page, size },
  });
  return response.data;
};

const getNoticeDetail = async (id: number): Promise<NoticeDetailApiResponse> => {
  const response = await deeplineApi.get<NoticeDetailApiResponse>(`/notices/${id}`);
  return response.data;
};

const deleteNotice = async (id: number): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.delete<BaseResponse<void>>(`/notices/${id}`);
  return response.data;
};

export const NoticeApi = {
  createNotice,
  getNoticeList,
  getNoticeDetail,
  deleteNotice,
};
