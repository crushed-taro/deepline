import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import type {
  FindIdRequest,
  MemberAssignmentRequest,
  ResetPasswordRequest,
} from "@/types/member/member.types.ts";
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

export function useGetMemberList(page: number, size: number, searchName?: string) {
  return useQuery({
    queryKey: ["members", page, size, searchName],
    queryFn: () => MemberApi.getMemberList(page, size, searchName),
    placeholderData: (previousData) => previousData,
  });
}

export function useAssignMember() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ memberId, data }: { memberId: string; data: MemberAssignmentRequest }) =>
      MemberApi.assignMember(memberId, data),
    onSuccess: () => {
      toast.success("인사 발령이 완료되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["members"] });
    },
    onError: (error) => {
      console.error("Assign Member Failed:", error);
      toast.error("인사 발령에 실패했습니다.");
    },
  });
}

export function useGetMyInfo() {
  return useQuery({
    queryKey: ["my-info"],
    queryFn: MemberApi.getMyInfo,
  });
}

export function useUpdateMyInfo() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: FormData) => MemberApi.updateMyInfo(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["my-info"] });
    },
  });
}
