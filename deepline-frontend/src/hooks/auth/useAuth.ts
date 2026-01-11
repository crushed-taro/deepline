import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import type { LoginRequest, SignUpRequest } from "@/types/auth/auth.types.ts";
import { AuthApi } from "@/api/auth/AuthApi.ts";
import { jwtDecode } from "jwt-decode";
import type { CustomJwtPayload } from "@/types/auth/jwt.types.ts";

export function useLogin() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: LoginRequest) => AuthApi.login(data),
    onSuccess: (response) => {
      if (response.result && response.result.accessToken) {
        localStorage.setItem("accessToken", response.result.accessToken);
        localStorage.setItem("memberName", response.result.memberName);

        toast.success(`${response.result.memberName}님 환영합니다.`);
        navigate("/");
      } else {
        toast.error("로그인에 실패했습니다.");
      }
    },
    onError: (error: never) => {
      console.error("Login Failed : ", error);
      toast.error(`아이디 또는 비밀번호를 확인해주세요.`);
    },
  });
}

export function useSignUp() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: SignUpRequest) => AuthApi.signup(data),
    onSuccess: (response) => {
      if (response.result?.signupStatus === "SIGNUP_SUCCESS") {
        toast.success(`회원가입이 완료되었습니다. (${response.result.message})`);
        navigate("/login");
      } else {
        toast.error("회원가입에 실패했습니다.");
      }
    },
    onError: (error) => {
      console.error("Signup Failed : ", error);
      toast.error("회원가입에 실패했습니다. 입력값을 확인해주세요.");
    },
  });
}

export function useLogout() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("memberName");

    queryClient.clear();

    toast.success("로그아웃 되었습니다.");
    navigate("/login", { replace: true });
  };

  return { logout };
}

export function useUserRole() {
  const token = localStorage.getItem("accessToken");
  if (!token) return null;

  const decoded = jwtDecode<CustomJwtPayload>(token);
  return decoded.sub;
}

export function useIsAdmin() {
  const role = useUserRole();
  return role === "admin";
}
