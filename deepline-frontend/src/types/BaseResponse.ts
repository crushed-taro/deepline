export interface BaseResponse<T = unknown> {
  status: string;
  code: string;
  message: string;
  result: T | null;
}
