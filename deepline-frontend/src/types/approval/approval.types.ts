import type { BaseResponse } from "@/types/BaseResponse.ts";

export type ApprovalStatus = "WAITING" | "PENDING" | "APPROVED" | "REJECTED";

export type ApprovalType = "GENERAL" | "VACATION";

export interface ApprovalRegistRequest {
  title: string;
  content: string;
  approverCodes: number[];
  type?: ApprovalType;
  startDate?: string;
  endDate?: string;
}

export interface ApprovalListResponse {
  approvalCode: number;
  title: string;
  drafterName: string;
  approvalStatus: ApprovalStatus;
  createdAt: string;
}

export interface ApprovalLineResponse {
  lineOrder: number;
  approverName: string;
  positionName: string;
  status: ApprovalStatus;
  comment: string | null;
  processedAt: string | null;
}

export interface ApprovalDetailResponse {
  approvalCode: number;
  title: string;
  content: string;
  drafterName: string;
  deptName: string;
  statue: ApprovalStatus;
  createdAt: string;
  approvalLines: ApprovalLineResponse[];
  type: ApprovalType;
  startDate?: string;
  endDate?: string;
}

export interface ApprovalProcessRequest {
  status: "APPROVED" | "REJECTED";
  comments: string;
}

export type ApprovalListApiResponse = BaseResponse<ApprovalListResponse[]>;
export type ApprovalDetailApiResponse = BaseResponse<ApprovalDetailResponse>;
