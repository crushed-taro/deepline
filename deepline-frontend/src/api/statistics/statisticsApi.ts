import type {
  ApprovalStatApiResponse,
  AttendanceStatApiResponse,
} from "@/types/statistics/statistics.type.ts";
import { deeplineApi } from "@/lib/api/clients.ts";

const getApprovalStats = async (): Promise<ApprovalStatApiResponse> => {
  const response = await deeplineApi.get<ApprovalStatApiResponse>("/statistics/approvals");

  console.info("getApprovalStats : ", response);

  return response.data;
};

const getAttendanceStats = async (
  year?: number,
  month?: number
): Promise<AttendanceStatApiResponse> => {
  const params: Record<string, unknown> = {};

  if (year) params.year = year;
  if (month) params.month = month;

  const response = await deeplineApi.get<AttendanceStatApiResponse>("/statistics/attendances", {
    params,
  });

  console.info("getAttendanceStats : ", response);

  return response.data;
};

export const StatisticsApi = {
  getApprovalStats,
  getAttendanceStats,
};
