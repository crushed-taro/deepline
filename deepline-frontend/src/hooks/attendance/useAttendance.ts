import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { AttendanceApi } from "@/api/attendance/attendanceApi.ts";

export function useClockIn() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: AttendanceApi.clockIn,
    onSuccess: () => {
      toast.success("오늘 하루도 화이팅하세요! (출근 처리됨)");
      queryClient.invalidateQueries({ queryKey: ["myAttendance"] });
    },
    onError: (error: any) => {
      console.error("ClockIn Failed:", error);
      const message = error.response?.data?.message || "출근 처리에 실패했습니다.";
      toast.error(message);
    },
  });
}

export function useClockOut() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: AttendanceApi.clockOut,
    onSuccess: () => {
      toast.success("퇴근 처리가 완료되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["myAttendance"] });
    },
    onError: (error: any) => {
      console.error("ClockOut Failed:", error);
      const message = error.response?.data?.message || "퇴근 처리에 실패했습니다.";
      toast.error(message);
    },
  });
}

export function useGetMyAttendance(year: number, month: number) {
  return useQuery({
    queryKey: ["myAttendance", year, month],
    queryFn: () => AttendanceApi.getMyAttendance(year, month),
  });
}
