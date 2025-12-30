import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ApprovalApi } from "@/api/approval/approvalApi.ts";
import { useNavigate } from "react-router-dom";
import type {
  ApprovalProcessRequest,
  ApprovalRegistRequest,
} from "@/types/approval/approval.types.ts";
import { toast } from "sonner";

export function useGetSentApprovals() {
  return useQuery({
    queryKey: ["approvals", "sent"],
    queryFn: ApprovalApi.getSentApprovals,
  });
}

export function useGetReceivedApprovals() {
  return useQuery({
    queryKey: ["approval", "received"],
    queryFn: ApprovalApi.getReceivedApprovals,
  });
}

export function useGetApprovalDetail(approvalCode: number | null) {
  return useQuery({
    queryKey: ["approval", approvalCode],
    queryFn: () => ApprovalApi.getApprovalDetail(approvalCode!),
    enabled: !!approvalCode,
  });
}

export function useRegisterApproval() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: ApprovalRegistRequest) => ApprovalApi.registerApproval(data),
    onSuccess: () => {
      toast.success("결재 문서가 상신되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["approvals", "sent"] });
      navigate("/approvals");
    },
    onError: (error) => {
      console.error("Register Approval Failed : ", error);
      toast.error("결재 상신에 실패했습니다.");
    },
  });
}

export function useProcessApproval() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ code, data }: { code: number; data: ApprovalProcessRequest }) =>
      ApprovalApi.processApproval(code, data),
    onSuccess: () => {
      toast.success("결재 처리가 완료되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["approvals"] });
      queryClient.invalidateQueries({ queryKey: ["approval"] });
      navigate("/approvals");
    },
    onError: (error) => {
      console.error("Process Approval Failed : ", error);
      toast.error("결재 처리에 실패했습니다.");
    },
  });
}
