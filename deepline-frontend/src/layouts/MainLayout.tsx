import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { Outlet, useNavigate } from "react-router-dom";
import Header from "@/components/Header.tsx";
import { AppSidebar } from "@/components/common/app-siderbar.tsx";
import { toast } from "sonner";
import { useEffect } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";

export default function MainLayout() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");

    if (!token) return;

    const eventSource = new EventSourcePolyfill(
      "http://localhost:8080/api/v1/notifications/subscribe",
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        heartbeatTimeout: 86400000,
      }
    );

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

    eventSource.onerror = (err) => {
      console.error("[SSE] Error:", err);
      eventSource.close();
    };

    return () => {
      eventSource.close();
      console.log("[SSE] Connection Closed");
    };
  }, [navigate]);

  return (
    <SidebarProvider>
      <div className="flex min-h-screen w-full">
        {/* 사이드바 */}
        <AppSidebar />

        {/* 메인 콘텐츠 영역 */}
        <div className="flex flex-col flex-1 min-w-0">
          <Header /> {/* 상단 헤더 (로고, 유저정보 등) */}
          <main className="flex-1 p-6 overflow-y-auto bg-gray-50">
            <div className="flex items-center gap-2 mb-4 md:hidden">
              {/* 모바일에서 사이드바 여는 버튼 */}
              <SidebarTrigger />
            </div>

            {/* 실제 페이지들이 렌더링되는 곳 */}
            <Outlet />
          </main>
        </div>
      </div>
    </SidebarProvider>
  );
}
