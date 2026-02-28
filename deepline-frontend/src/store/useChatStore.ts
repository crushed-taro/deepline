import { create } from "zustand";

interface ChatState {
  unreadSenders: string[];
  addUnread: (senderId: string) => void;
  removeUnread: (senderId: string) => void;
}

export const useChatStore = create<ChatState>((set) => ({
  unreadSenders: [],
  addUnread: (senderId) =>
    set((state) => ({
      unreadSenders: state.unreadSenders.includes(senderId)
        ? state.unreadSenders
        : [...state.unreadSenders, senderId],
    })),
  removeUnread: (senderId) =>
    set((state) => ({
      unreadSenders: state.unreadSenders.filter((id) => id !== senderId),
    })),
}));
