import type { FindIdApiResponse, FindIdRequest } from "@/types/member/member.types.ts";
import { deeplineApi } from "@/lib/api/clients.ts";

const findid = async (data: FindIdRequest): Promise<FindIdApiResponse> => {
  const response = await deeplineApi.post<FindIdApiResponse>("/members/find-id", data);

  console.info("findid response : ", response);

  return response.data;
};

export const MemberApi = {
  findid,
};
