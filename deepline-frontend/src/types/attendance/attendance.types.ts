import type { BaseResponse } from "@/types/BaseResponse.ts";

export type AttendanceStatus = "PRESENT" | "LATE" | "LEAVE_EARLY" | "ABSENT" | "VACATION";

export interface AttendanceResponse {
  workDate: string;
  startTime: string | null;
  endTime: string | null;
  status: AttendanceStatus;
}

export type GetMyAttendanceApiResponse = BaseResponse<AttendanceResponse[]>;
