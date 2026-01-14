import type { ApprovalStatus } from "@/types/approval/approval.types.ts";
import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface ApprovalStat {
  status: ApprovalStatus;
  count: number;
}

export interface AttendanceStat {
  date: string;
  present: number;
  late: number;
  vacation: number;
  absent: number;
}

export type ApprovalStatApiResponse = BaseResponse & {
  result: ApprovalStat[];
};

export type AttendanceStatApiResponse = BaseResponse & {
  result: AttendanceStat[];
};
