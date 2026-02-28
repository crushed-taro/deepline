import React, { useState, useEffect, useRef } from "react";
import {
  MessageCircle,
  X,
  Send,
  ArrowLeft,
  Users,
  User,
  ChevronDown,
  ChevronUp,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { type MemberSimple } from "@/api/chat/chatApi";
import { useChat, useChatMessages, useGetOrCreateRoom } from "@/hooks/chat/useChat";
import { useGetMembersByDept, useGetMyInfo } from "@/hooks/member/useMember.ts";
import { useDepartments } from "@/hooks/organization/useOrganization.ts";
import { usePresenceStore } from "@/store/usePresenceStore.ts";
import { useChatStore } from "@/store/useChatStore.ts";

type ChatView = "LIST" | "CHAT_ROOM";

const DepartmentToggleGroup = ({
  dept,
  onMemberDoubleClick,
}: {
  dept: any;
  onMemberDoubleClick: (member: MemberSimple) => void;
}) => {
  const [isOpen, setIsOpen] = useState(false);

  const { data: memberData, isLoading } = useGetMembersByDept(isOpen ? dept.deptCode : null);

  const { onlineUsers } = usePresenceStore();

  return (
    <div className="border-b last:border-b-0">
      <div
        className="flex items-center justify-between p-3 hover:bg-gray-100 cursor-pointer transition-colors"
        onClick={() => setIsOpen(!isOpen)}
      >
        <div className="flex items-center gap-3">
          <div className="bg-blue-100 p-1.5 rounded-md text-blue-600">
            <Users className="h-4 w-4" />
          </div>
          <span className="font-medium text-gray-700 text-sm">{dept.deptName}</span>
        </div>
        {isOpen ? (
          <ChevronUp className="h-4 w-4 text-gray-400" />
        ) : (
          <ChevronDown className="h-4 w-4 text-gray-400" />
        )}
      </div>

      {isOpen && (
        <div className="bg-gray-50 p-2 space-y-1 border-t border-gray-100 shadow-inner">
          {isLoading ? (
            <div className="text-center py-2 text-xs text-gray-400">구성원 로딩 중...</div>
          ) : !memberData || memberData.length === 0 ? (
            <div className="text-center py-2 text-xs text-gray-400">구성원이 없습니다.</div>
          ) : (
            memberData.map((member: MemberSimple) => {
              const isOnline = onlineUsers.includes(member.memberId);

              return (
                <div
                  key={member.memberCode}
                  onDoubleClick={() => onMemberDoubleClick(member)}
                  className="flex items-center gap-3 p-2 hover:bg-white hover:shadow-sm rounded-md cursor-pointer transition-all select-none group"
                >
                  <div className="relative inline-block">
                    <div className="bg-gray-100 p-1.5 rounded-full text-gray-600 group-hover:scale-110 transition-transform">
                      <User className="h-3 w-3" />
                    </div>
                    <span
                      className={`absolute bottom-0 right-0 block h-2.5 w-2.5 rounded-full ring-2 ring-white transition-colors duration-300 ${
                        isOnline ? "bg-green-500" : "bg-gray-300"
                      }`}
                    />
                  </div>

                  <div className="bg-green-100 p-1.5 rounded-full text-green-600 group-hover:scale-110 transition-transform">
                    <User className="h-3 w-3" />
                  </div>
                  <div className="flex flex-col">
                    <span className="font-medium text-gray-700 text-xs">
                      {member.memberName}
                      <span className="text-[10px] text-gray-500 ml-1">
                        ({member.positionName})
                      </span>
                    </span>
                  </div>
                </div>
              );
            })
          )}
        </div>
      )}
    </div>
  );
};

export default function ChatWidget() {
  const [isOpen, setIsOpen] = useState(false);
  const [currentView, setCurrentView] = useState<ChatView>("LIST");

  const [selectedUser, setSelectedUser] = useState<MemberSimple | null>(null);
  const [roomId, setRoomId] = useState<string | null>(null);
  const [inputText, setInputText] = useState("");

  const { data: deptData } = useDepartments();
  const { data: myInfo } = useGetMyInfo();

  const myId = myInfo?.result?.memberId || "";
  const myName = myInfo?.result?.memberName || "나";

  const { messages, setMessages, sendMessage, isConnected } = useChat(myId, roomId);
  const { mutateAsync: getOrCreateRoomAsync } = useGetOrCreateRoom();
  const { mutateAsync: getChatHistoryAsync } = useChatMessages();

  const { unreadSenders, removeUnread } = useChatStore();

  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (currentView === "CHAT_ROOM") {
      messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages, currentView]);

  const handleMemberDoubleClick = async (targetMember: MemberSimple) => {
    if (targetMember.memberId === myId) {
      alert("자기 자신과는 대화할 수 없습니다.");
      return;
    }

    try {
      setSelectedUser(targetMember);
      const newRoomId = await getOrCreateRoomAsync(targetMember.memberId);
      const { chatRoomCode, roomId } = newRoomId.result;

      setRoomId(roomId);

      const history = await getChatHistoryAsync(chatRoomCode);
      setMessages(history.result ?? []);

      setCurrentView("CHAT_ROOM");

      removeUnread(targetMember.memberId);
    } catch (error) {
      console.error("채팅방 입장 실패:", error);
    }
  };

  const handleBack = () => {
    setRoomId(null);
    setMessages([]);
    setCurrentView("LIST");
  };

  const handleSend = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inputText.trim() || !roomId || !selectedUser) return;

    sendMessage({
      roomId: roomId,
      senderId: myId,
      senderName: myName,
      message: inputText,
      receiverId: selectedUser.memberId,
    });
    setInputText("");
  };

  return (
    <div className="fixed bottom-6 right-6 z-50 flex flex-col items-end">
      {isOpen && (
        <Card className="w-80 h-[32rem] mb-4 shadow-2xl flex flex-col overflow-hidden animate-in slide-in-from-bottom-5 border-2 border-primary/20">
          <CardHeader className="p-4 border-b bg-primary text-primary-foreground flex flex-row items-center justify-between shrink-0 h-16">
            <div className="flex items-center gap-2">
              {currentView === "CHAT_ROOM" && (
                <Button
                  variant="ghost"
                  size="icon"
                  className="h-8 w-8 text-primary-foreground hover:bg-white/20 -ml-2"
                  onClick={handleBack}
                >
                  <ArrowLeft className="h-5 w-5" />
                </Button>
              )}
              <CardTitle className="text-sm font-semibold flex items-center gap-2">
                {currentView === "LIST" && "조직도 (더블클릭하여 대화)"}
                {currentView === "CHAT_ROOM" && (
                  <div className="flex flex-col">
                    <span>{selectedUser?.memberName}</span>
                    <span className="text-[10px] font-normal opacity-80">
                      {isConnected ? "● 온라인" : "○ 연결 중..."}
                    </span>
                  </div>
                )}
              </CardTitle>
            </div>
            <Button
              variant="ghost"
              size="icon"
              className="h-6 w-6 rounded-full hover:bg-white/20"
              onClick={() => setIsOpen(false)}
            >
              <X className="h-4 w-4" />
            </Button>
          </CardHeader>

          <CardContent className="p-0 flex-1 flex flex-col overflow-hidden bg-white">
            {currentView === "LIST" && (
              <ScrollArea className="flex-1">
                <div className="flex flex-col">
                  {deptData?.result?.map((dept: any) => (
                    <DepartmentToggleGroup
                      key={dept.deptCode}
                      dept={dept}
                      onMemberDoubleClick={handleMemberDoubleClick}
                    />
                  ))}
                </div>
              </ScrollArea>
            )}

            {currentView === "CHAT_ROOM" && (
              <>
                <ScrollArea className="flex-1 p-4 bg-gray-50">
                  <div className="flex flex-col space-y-3">
                    {messages.map((msg, idx) => {
                      const isMe = msg.senderId === myId;
                      return (
                        <div
                          key={idx}
                          className={`flex flex-col max-w-[75%] ${isMe ? "self-end items-end" : "self-start items-start"}`}
                        >
                          {!isMe && (
                            <span className="text-xs text-muted-foreground mb-1 ml-1">
                              {msg.senderName}
                            </span>
                          )}
                          <div
                            className={`px-3 py-2 rounded-lg text-sm shadow-sm break-all ${isMe ? "bg-primary text-primary-foreground rounded-tr-none" : "bg-white border rounded-tl-none"}`}
                          >
                            {msg.message}
                          </div>
                          <span className="text-[10px] text-gray-400 mt-1 mx-1">
                            {msg.timestamp ? msg.timestamp.substring(11, 16) : ""}
                          </span>
                        </div>
                      );
                    })}
                    <div ref={messagesEndRef} />
                  </div>
                </ScrollArea>

                <form
                  onSubmit={handleSend}
                  className="p-3 border-t bg-background flex items-center gap-2"
                >
                  <Input
                    value={inputText}
                    onChange={(e) => setInputText(e.target.value)}
                    placeholder="메시지 입력..."
                    className="flex-1 rounded-full h-9 bg-gray-50 focus-visible:ring-1"
                  />
                  <Button
                    type="submit"
                    size="icon"
                    className="h-9 w-9 rounded-full shrink-0"
                    disabled={!inputText.trim()}
                  >
                    <Send className="h-4 w-4" />
                  </Button>
                </form>
              </>
            )}
          </CardContent>
        </Card>
      )}

      <Button
        onClick={() => setIsOpen(!isOpen)}
        className="h-14 w-14 rounded-full shadow-lg transition-transform hover:scale-105"
        size="icon"
      >
        {isOpen ? <X className="h-6 w-6" /> : <MessageCircle className="h-6 w-6" />}

        {unreadSenders.length > 0 && !isOpen && (
          <span className="absolute top-0 right-0 block h-3.5 w-3.5 rounded-full bg-red-500 ring-2 ring-white" />
        )}
      </Button>
    </div>
  );
}
