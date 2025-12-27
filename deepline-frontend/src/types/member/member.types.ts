import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface FindIdRequest {
  memberName: string;
  memberEmail: string;
}

export type FindIdApiResponse = BaseResponse & {
  result: string;
};
