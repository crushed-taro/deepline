import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { EventSourcePolyfill } from "event-source-polyfill";
import { usePresenceStore } from "@/store/usePresenceStore";

export const useSSE = () => {
  const navigate = useNavigate();
  const { setOnlineUsers, addOnlineUser, removeOnlineUser } = usePresenceStore();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (!token) return;

    const eventSource = new EventSourcePolyfill("/api/v1/notifications/subscribe", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      heartbeatTimeout: 86400000,
    });

    eventSource.onopen = () => {
      console.log("[SSE] Connected!");
    };

    eventSource.addEventListener("notification", (event: any) => {
      const message = JSON.parse(event.data);
      console.log("[SSE] Message Received:", message);

      toast.success(message.content, {
        action: {
          label: "이동",
          onClick: () => navigate(message.url),
        },
        duration: 5000,
      });
    });

    eventSource.addEventListener("PRESENCE_LIST", (event: any) => {
      const users = JSON.parse(event.data);
      setOnlineUsers(users);
    });

    eventSource.addEventListener("USER_ONLINE", (event: any) => {
      addOnlineUser(event.data);
    });

    eventSource.addEventListener("USER_OFFLINE", (event: any) => {
      removeOnlineUser(event.data);
    });

    eventSource.onerror = (err) => {
      console.error("[SSE] Error:", err);
      eventSource.close();
    };

    return () => {
      eventSource.close();
      console.log("[SSE] Connection Closed");
    };
  }, [navigate, setOnlineUsers, addOnlineUser, removeOnlineUser]);
};
