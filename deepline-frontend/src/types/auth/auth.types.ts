import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface LoginRequest {
  memberId: string;
  memberPassword: string;
}

export interface SignUpRequest {
  memberId: string;
  memberPassword: string;
  memberName: string;
  memberEmail: string;
}

export interface TokenResponse {
  grantType: string;
  memberName: string;
  accessToken: string;
  accessTokenExpiresIn: number;
  tokenStatus: string;
}

export interface SignUpResponse {
  memberId: number;
  signupStatus: string;
  message: string;
}

export type LoginApiResponse = BaseResponse & {
  result: TokenResponse;
};

export type SignUpApiResponse = BaseResponse & {
  result: SignUpResponse;
};
