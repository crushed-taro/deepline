import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { DepartmentApi, PositionApi } from "@/api/organization/organizationApi.ts";
import type { Department, Position } from "@/types/organization/organization.types.ts";
import { toast } from "sonner";

export function useDepartments() {
  return useQuery({
    queryKey: ["departments"],
    queryFn: DepartmentApi.getDepartments,
  });
}

export function useCreateDepartment() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: Department) => DepartmentApi.createDepartment(data.deptName),
    onSuccess: () => {
      toast.success("부서가 등록되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["departments"] });
    },
    onError: (err) => {
      console.error(err);
      toast.error("부서 등록 실패");
    },
  });
}

export function useDeleteDepartment() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: number) => DepartmentApi.deleteDepartment(data),
    onSuccess: () => {
      toast.success("부서가 삭제되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["departments"] });
    },
    onError: (err) => {
      console.error(err);
      toast.error("부서 삭제 실패");
    },
  });
}

export function usePositions() {
  return useQuery({
    queryKey: ["positions"],
    queryFn: PositionApi.getPositions,
  });
}

export function useCreatePosition() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: Position) => PositionApi.createPosition(data.positionName),
    onSuccess: () => {
      toast.success("직급이 등록되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["positions"] });
    },
    onError: (err) => {
      console.error(err);
      toast.error("직급 등록 실패");
    },
  });
}

export function useDeletePosition() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: number) => PositionApi.deletePosition(data),
    onSuccess: () => {
      toast.success("직급이 삭제되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["positions"] });
    },
    onError: (err) => {
      console.error(err);
      toast.error("직급 삭제 실패");
    },
  });
}
