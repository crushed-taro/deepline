import type {
  AssignMemberRequest,
  FindIdApiResponse,
  FindIdRequest,
  GetMemberListApiResponse,
  GetMyInfoApiResponse,
  ResetPasswordRequest,
} from "@/types/member/member.types.ts";
import { deeplineApi } from "@/lib/api/clients.ts";
import type { BaseResponse } from "@/types/BaseResponse.ts";

const findId = async (data: FindIdRequest): Promise<FindIdApiResponse> => {
  const response = await deeplineApi.post<FindIdApiResponse>("/members/find-id", data);

  console.info("findId response : ", response);

  return response.data;
};

const resetPassword = async (data: ResetPasswordRequest): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.post<BaseResponse<void>>("/members/reset-password", data);

  console.info("resetPassword response : ", response);

  return response.data;
};

const getMyInfo = async (): Promise<GetMyInfoApiResponse> => {
  const response = await deeplineApi.get<GetMyInfoApiResponse>("/members/me");

  console.info("getMyInfo response : ", response);

  return response.data;
};

const updateMyInfo = async (data: FormData): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.put<BaseResponse<void>>("/members/me", data, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  console.info("updateMyInfo response : ", response);

  return response.data;
};

const withDraw = async (): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.delete<BaseResponse<void>>("/members/me");

  console.info("withDraw response : ", response);

  return response.data;
};

const getMemberList = async (
  page: number = 0,
  size: number = 10,
  searchName?: string
): Promise<GetMemberListApiResponse> => {
  const params: Record<string, unknown> = { page, size };

  if (searchName) {
    params.searchName = searchName;
  }

  const response = await deeplineApi.get<GetMemberListApiResponse>("/members", {
    params,
  });

  console.info("getMemberList response : ", response);

  return response.data;
};

const assignMember = async (
  memberId: string,
  data: AssignMemberRequest
): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.put<BaseResponse<void>>(`/members/${memberId}/assign`, data);

  console.info("assignMember response : ", response);

  return response.data;
};

export const MemberApi = {
  findId,
  resetPassword,
  getMyInfo,
  updateMyInfo,
  withDraw,
  getMemberList,
  assignMember,
};
