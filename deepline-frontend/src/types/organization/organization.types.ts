import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface Department {
  deptCode: number;
  deptName: string;
}

export interface Position {
  positionCode: number;
  positionName: string;
}

export type DeptListApiResponse = BaseResponse & {
  result: Department[];
};

export type PositionListApiResponse = BaseResponse & {
  result: Position[];
};
