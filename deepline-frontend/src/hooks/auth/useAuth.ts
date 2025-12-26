import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import type { LoginRequest } from "@/types/auth.types.ts";
import { AuthApi } from "@/api/member/AuthApi.ts";
import { toast } from "sonner";

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
