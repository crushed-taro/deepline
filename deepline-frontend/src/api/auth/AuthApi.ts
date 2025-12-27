import { deeplineApi } from "@/lib/api/clients.ts";
import type {
  LoginApiResponse,
  LoginRequest,
  SignUpApiResponse,
  SignUpRequest,
} from "@/types/auth/auth.types.ts";

const login = async (data: LoginRequest): Promise<LoginApiResponse> => {
  const response = await deeplineApi.post<LoginApiResponse>("/auth/login", data);

  console.info("login response:", response);

  return response.data;
};

const signup = async (data: SignUpRequest): Promise<SignUpApiResponse> => {
  const response = await deeplineApi.post<SignUpApiResponse>("/auth/signup", data);

  console.info("signup response:", response);

  return response.data;
};

export const AuthApi = {
  login,
  signup,
};
