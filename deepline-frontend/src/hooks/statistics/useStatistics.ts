import { useQuery } from "@tanstack/react-query";
import { StatisticsApi } from "@/api/statistics/statisticsApi.ts";

export function useGetApprovalStats() {
  return useQuery({
    queryKey: ["stats-approval"],
    queryFn: StatisticsApi.getApprovalStats,
  });
}

export function useGetAttendanceStats(year: number, month: number) {
  return useQuery({
    queryKey: ["stats-attendance", year, month],
    queryFn: () => StatisticsApi.getAttendanceStats(year, month),
  });
}
