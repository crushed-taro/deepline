import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { NoticeApi } from "@/api/notice/noticeApi.ts";
import { useNavigate } from "react-router-dom";
import type { NoticeRequest } from "@/types/notice/notice.types.ts";
import { toast } from "sonner";

export function useGetNoticeList(page: number, size: number) {
  return useQuery({
    queryKey: ["notices", page, size],
    queryFn: () => NoticeApi.getNoticeList(page, size),
    placeholderData: (prev) => prev,
  });
}

export function useGetNoticeDetail(id: number | null) {
  return useQuery({
    queryKey: ["notice", id],
    queryFn: () => NoticeApi.getNoticeDetail(id!),
    enabled: !!id,
  });
}

export function useCreateNotice() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: NoticeRequest) => NoticeApi.createNotice(data),
    onSuccess: () => {
      toast.success("공지사항이 등록되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["notices"] });
      navigate("/notices");
    },
    onError: (err) => {
      console.error(err);
      toast.error("공지사항 등록 실패");
    },
  });
}

export function useDeleteNotice() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (id: number) => NoticeApi.deleteNotice(id),
    onSuccess: () => {
      toast.success("공지사항이 삭제되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["notices"] });
      navigate("/notices");
    },
  });
}
