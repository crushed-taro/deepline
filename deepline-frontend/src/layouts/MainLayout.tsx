import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { Outlet } from "react-router-dom";
import Header from "@/components/Header.tsx";
import { AppSidebar } from "@/components/common/app-siderbar.tsx";

export default function MainLayout() {
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
