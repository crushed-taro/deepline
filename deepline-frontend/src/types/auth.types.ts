import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface LoginRequest {
  memberId: string;
  memberPassword: string;
}

export interface TokenResponse {
  grantType: string;
  memberName: string;
  accessToken: string;
  accessTokenExpiresIn: number;
  tokenStatus: string;
}

export type LoginApiResponse = BaseResponse & {
  result: TokenResponse;
};
