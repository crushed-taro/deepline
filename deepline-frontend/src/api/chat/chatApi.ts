import type { ChatHistoryResponse, ChatOrCreateRoom } from "@/types/chat/chat.types";
import { deeplineApi } from "@/lib/api/clients.ts";

const getOrCreateRoom = async (opponentId: string): Promise<ChatOrCreateRoom> => {
  const response = await deeplineApi.get<ChatOrCreateRoom>(`/chat/room/${opponentId}`);
  return response.data;
};

const getChatHistory = async (roomId: number): Promise<ChatHistoryResponse> => {
  const response = await deeplineApi.get<ChatHistoryResponse>(`/chat/history/${roomId}`);
  console.info("getChatHistory : {}", response);
  return response.data;
};

export interface MemberSimple {
  memberCode: number;
  memberId: string;
  memberName: string;
  positionName: string;
  deptName?: string;
}

export const ChatApi = {
  getOrCreateRoom,
  getChatHistory,
};
