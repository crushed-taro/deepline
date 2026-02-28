import type { BaseResponse } from "@/types/BaseResponse.ts";

export interface ChatMessage {
  roomId: string;
  senderId: string;
  senderName: string;
  message: string;
  timestamp: string;
  receiverId: string;
}

export interface ChatRoomResponse {
  chatRoomCode: number;
  roomId: string;
}

export type ChatOrCreateRoom = BaseResponse & {
  result: ChatRoomResponse;
};

export type ChatHistoryResponse = BaseResponse<ChatMessage[]>;
