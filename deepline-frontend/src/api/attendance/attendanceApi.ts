import type { BaseResponse } from "@/types/BaseResponse.ts";
import { deeplineApi } from "@/lib/api/clients.ts";
import type { GetMyAttendanceApiResponse } from "@/types/attendance/attendance.types.ts";

const clockIn = async (): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.post<BaseResponse<void>>("/attendances/clock-in");

  console.info("clockIn response:", response);

  return response.data;
};

const clockOut = async (): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.post<BaseResponse<void>>("/attendances/clock-out");

  console.info("clockIn response:", response);

  return response.data;
};

const getMyAttendance = async (
  year?: number,
  month?: number
): Promise<GetMyAttendanceApiResponse> => {
  const params: Record<string, unknown> = {};

  if (year) params.year = year;
  if (month) params.month = month;

  const response = await deeplineApi.get<GetMyAttendanceApiResponse>("/attendances/me", { params });

  console.info("getMyAttendance response : ", response);

  return response.data;
};

export const AttendanceApi = {
  clockIn,
  clockOut,
  getMyAttendance,
};
