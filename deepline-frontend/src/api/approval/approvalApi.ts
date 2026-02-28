import type {
  ApprovalDetailApiResponse,
  ApprovalListApiResponse,
  ApprovalProcessRequest,
  ApprovalRegistRequest,
} from "@/types/approval/approval.types.ts";
import type { BaseResponse } from "@/types/BaseResponse.ts";
import { deeplineApi } from "@/lib/api/clients.ts";

const registerApproval = async (data: ApprovalRegistRequest): Promise<BaseResponse<void>> => {
  const reponse = await deeplineApi.post<BaseResponse<void>>("/approvals", data);

  console.info("registerApproval response : ", reponse);

  return reponse.data;
};

const getSentApprovals = async (): Promise<ApprovalListApiResponse> => {
  const response = await deeplineApi.get<ApprovalListApiResponse>("/approvals/sent");
  console.debug("getSentApprovals response : ", response);
  return response.data;
};

const getReceivedApprovals = async (): Promise<ApprovalListApiResponse> => {
  const response = await deeplineApi.get<ApprovalListApiResponse>("/approvals/received");
  console.debug("getReceivedApprovals response : ", response);
  return response.data;
};

const getApprovalDetail = async (approvalCode: number): Promise<ApprovalDetailApiResponse> => {
  const response = await deeplineApi.get<ApprovalDetailApiResponse>(`/approvals/${approvalCode}`);
  console.debug("getApprovalDetail response : ", response);
  return response.data;
};

const processApproval = async (
  approvalCode: number,
  data: ApprovalProcessRequest
): Promise<BaseResponse<void>> => {
  const response = await deeplineApi.post<BaseResponse<void>>(
    `/approvals/${approvalCode}/process`,
    data
  );

  console.info("processApproval response : ", response);
  return response.data;
};

export const ApprovalApi = {
  registerApproval,
  getSentApprovals,
  getReceivedApprovals,
  getApprovalDetail,
  processApproval,
};
