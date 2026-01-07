import { deeplineApi } from "@/lib/api/clients.ts";
import type {
  DeptListApiResponse,
  PositionListApiResponse,
} from "@/types/organization/organization.types.ts";

const getDepartments = async () => {
  const response = await deeplineApi.get<DeptListApiResponse>("/organizations/departments");
  return response.data;
};

const createDepartment = async (deptName: string) => {
  const response = await deeplineApi.post("/organizations/departments", { deptName });
  return response.data;
};

const deleteDepartment = async (departmentId: number) => {
  const response = await deeplineApi.delete(`/organizations/departments/${departmentId}`);
  return response.data;
};

const getPositions = async () => {
  const response = await deeplineApi.get<PositionListApiResponse>("/organizations/positions");
  return response.data;
};

const createPosition = async (positionName: string) => {
  const response = await deeplineApi.post("/organizations/positions", { positionName });
  return response.data;
};

export const DepartmentApi = {
  getDepartments,
  createDepartment,
  deleteDepartment,
};

export const PositionApi = {
  getPositions,
  createPosition,
};
