import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface FindIdRequest {
  memberName: string;
  memberEmail: string;
}

export interface ResetPasswordRequest {
  memberId: string;
  memberName: string;
  memberEmail: string;
  newPassword: string;
  confirmPassword: string;
}

export interface UpdateMyInfoRequest {
  memberName: string;
  memberEmail: string;
}

export interface AssignMemberRequest {
  deptCode: number;
  positionCode: number;
}

export interface MemberResponse {
  memberCode: number;
  memberId: string;
  memberName: string;
  memberEmail: string;
  deptName: string;
  positionName: string;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  numberOfElements: number;
  empty: boolean;
}

export type FindIdApiResponse = BaseResponse & {
  result: string;
};

export type GetMyInfoApiResponse = BaseResponse & {
  result: MemberResponse;
};

export type GetMemberListApiResponse = BaseResponse<PageResponse<MemberResponse>>;
