import { useState, useEffect, useCallback, useRef } from "react";
import { Client, type StompSubscription } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import type { ChatMessage } from "@/types/chat/chat.types";
import { useMutation } from "@tanstack/react-query";
import { ChatApi } from "@/api/chat/chatApi.ts";
import { useChatStore } from "@/store/useChatStore.ts";

export const useChat = (myId: string, roomId: string | null) => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [isConnected, setIsConnected] = useState(false);

  const stompClient = useRef<Client | null>(null);
  const roomSubscription = useRef<StompSubscription | null>(null);

  const { addUnread } = useChatStore();

  useEffect(() => {
    if (!myId) return;
    const token = localStorage.getItem("accessToken");

    const client = new Client({
      webSocketFactory: () => new SockJS("/ws-chat"),
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 5000,
      onConnect: () => {
        setIsConnected(true);

        client.subscribe(`/topic/chat.user.${myId}`, (message) => {
          const receivedMsg: ChatMessage = JSON.parse(message.body);

          if (receivedMsg.roomId !== roomId) {
            addUnread(receivedMsg.senderId);
          }
        });
      },
    });

    stompClient.current = client;
    client.activate();

    return () => {
      client.deactivate();
    };
  }, [myId]);

  useEffect(() => {
    if (!isConnected || !stompClient.current || !roomId) return;

    if (roomSubscription.current) {
      roomSubscription.current.unsubscribe();
    }

    roomSubscription.current = stompClient.current.subscribe(`/topic/chat.${roomId}`, (message) => {
      const receivedMsg: ChatMessage = JSON.parse(message.body);
      setMessages((prev) => [...prev, receivedMsg]);
    });

    return () => {
      if (roomSubscription.current) {
        roomSubscription.current.unsubscribe();
      }
    };
  }, [isConnected, roomId]);

  const sendMessage = useCallback((chatDTO: Omit<ChatMessage, "timestamp">) => {
    if (stompClient.current && stompClient.current.connected) {
      stompClient.current.publish({
        destination: "/pub/chat.sendMessage",
        body: JSON.stringify(chatDTO),
      });
    }
  }, []);

  return { messages, setMessages, isConnected, sendMessage };
};

export function useGetOrCreateRoom() {
  return useMutation({
    mutationFn: (data: string) => ChatApi.getOrCreateRoom(data),
  });
}

export function useChatMessages() {
  return useMutation({
    mutationFn: (data: number) => ChatApi.getChatHistory(data),
  });
}
