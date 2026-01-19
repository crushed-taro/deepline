import type { BaseResponse } from "@/types/BaseResponse.ts";
import type { PageResponse } from "@/types/member/member.types.ts";

export interface NoticeRequest {
  title: string;
  content: string;
  isPinned: boolean;
}

export interface NoticeResponse {
  notice: any;
  noticeCode: number;
  title: string;
  content: string;
  authorName: string;
  viewCount: number;
  isPinned: boolean;
  createdAt: string;
}

export type NoticeListApiResponse = BaseResponse & {
  result: PageResponse<NoticeResponse>;
};

export type NoticeDetailApiResponse = BaseResponse & {
  result: NoticeResponse;
};
