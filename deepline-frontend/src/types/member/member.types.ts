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

export interface MemberAssignmentRequest {
  deptCode: number;
  positionCode: number;
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

export interface SortInfo {
  sorted: boolean;
  unsorted: boolean;
  empty: boolean;
}

export interface PageableInfo {
  pageNumber: number;
  pageSize: number;
  sort: SortInfo;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface PageResponse<T> {
  content: T[];
  pageable: PageableInfo;
  totalPages: number;
  totalElements: number;
  last: boolean;
  numberOfElements: number;
  size: number;
  sort: SortInfo;
  number: number;
  first: boolean;
  empty: boolean;
}

export type FindIdApiResponse = BaseResponse & {
  result: string;
};

export type GetMyInfoApiResponse = BaseResponse & {
  result: MemberResponse;
};

export type GetMemberListApiResponse = BaseResponse & {
  result: PageResponse<MemberResponse>;
};
