import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import type { FindIdRequest, ResetPasswordRequest } from "@/types/member/member.types.ts";
import type { BaseResponse } from "@/types/BaseResponse.ts";
import { MemberApi } from "@/api/member/memerApi.ts";

export function useFindId() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: FindIdRequest) => MemberApi.findId(data) as Promise<BaseResponse<string>>,
    onSuccess: (response) => {
      const foundId = response.result;

      if (foundId) {
        toast.success(`${foundId} ${response.message}`, { duration: 8000 });
        navigate("/login", { state: { foundId } });
      } else {
        toast.error(response.message ?? "일치하는 회원 정보가 없습니다.");
      }
    },
    onError: (error: never) => {
      console.error("FindId Failed : ", error);
      toast.error(`아이디 찾기에 실패했습니다.`);
    },
  });
}

export function useResetPassword() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: ResetPasswordRequest) =>
      MemberApi.resetPassword(data) as Promise<BaseResponse<void>>,
    onSuccess: (response) => {
      const foundId = response.result;

      if (foundId) {
        toast.success(`${foundId} ${response.message}`, { duration: 8000 });
        navigate("/login", { state: { foundId } });
      } else {
        toast.error(response.message ?? "일치하는 회원 정보가 없습니다.");
      }
    },
    onError: (error: never) => {
      console.error("FindId Failed : ", error);
      toast.error(`아이디 찾기에 실패했습니다.`);
    },
  });
}
