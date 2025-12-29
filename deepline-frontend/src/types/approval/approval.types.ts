export type ApprovalStatus = "WAITING" | "PENDING" | "APPROVED" | "REJECTED";

export interface ApprovalRegistRequest {
  title: string;
  content: string;
  approverCodes: number[];
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
}

export interface ApprovalProcessRequest {
  status: "APPROVED" | "REJECTED";
  comments: string;
}

export type ApprovalListApiResponse = BaseResponse<ApprovalListResponse[]>;
export type ApprovalDetailApiResponse = BaseResponse<ApprovalDetailResponse>;
