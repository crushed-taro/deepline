import type { LoginApiResponse, LoginRequest } from "@/types/auth.types.ts";
import { deeplineApi } from "@/lib/api/clients.ts";

const login = async (data: LoginRequest): Promise<LoginApiResponse> => {
  const response = await deeplineApi.post<LoginApiResponse>("/auth/login", data);

  console.debug("login response:", response);

  return response.data;
};

export const AuthApi = {
  login,
};
