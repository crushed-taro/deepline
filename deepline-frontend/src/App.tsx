import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import { Toaster } from "sonner";
import Signup from "@/pages/auth/Signup.tsx";
import Login from "@/pages/auth/Login.tsx";
import FindId from "@/pages/member/FindId.tsx";
import Home from "@/pages/Home.tsx";
import MainLayout from "@/layouts/MainLayout.tsx";
import ApprovalWrite from "@/pages/approval/ApprovalWrite.tsx";
import ApprovalDetail from "@/pages/approval/ApprovalDetail.tsx";
import ApprovalList from "@/pages/approval/ApprovalList.tsx";
import RoleGuard from "@/components/common/RoleGuard.tsx";
import OrganizationManage from "@/pages/admin/OrganizationManage.tsx";
import NoticeList from "@/pages/notice/NoticeList.tsx";
import { NoticeWrite } from "@/pages/notice/NoticeWrite.tsx";
import NoticeDetail from "@/pages/notice/NoticeDetail.tsx";
import MemberManage from "@/pages/admin/MemberManage.tsx";
import ApprovalStatusChart from "@/pages/statistics/ApprovalStatusChart.tsx";
import AttendanceTrendChart from "@/pages/statistics/AttendanceTrendChart.tsx";
import Profile from "@/pages/member/Profile.tsx";

const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const token = localStorage.getItem("accessToken");
  return token ? children : <Navigate to="/login" replace />;
};

const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/signup",
    element: <Signup />,
  },
  {
    path: "/find-id",
    element: <FindId />,
  },

  {
    path: "/",
    element: (
      <PrivateRoute>
        <MainLayout />
      </PrivateRoute>
    ),
    children: [
      { index: true, element: <Home /> },
      { path: "profile", element: <Profile /> },
      { path: "attendance", element: <div>근태 관리 페이지 (준비중)</div> },

      {
        path: "approvals",
        children: [
          { index: true, element: <Navigate to="received" replace /> },
          { path: "received", element: <ApprovalList /> },
          { path: "sent", element: <ApprovalList /> },

          { path: "new", element: <ApprovalWrite /> },
          { path: ":id", element: <ApprovalDetail /> },
        ],
      },

      {
        path: "admin",
        children: [
          {
            path: "organization",
            element: (
              <RoleGuard requiredRole="admin">
                <OrganizationManage />
              </RoleGuard>
            ),
          },
          {
            path: "members",
            element: (
              <RoleGuard requiredRole="admin">
                <MemberManage />
              </RoleGuard>
            ),
          },
          {
            path: "approvalstats",
            element: (
              <RoleGuard requiredRole="admin">
                <ApprovalStatusChart />
              </RoleGuard>
            ),
          },
          {
            path: "attendancestats",
            element: (
              <RoleGuard requiredRole="admin">
                <AttendanceTrendChart />
              </RoleGuard>
            ),
          },
        ],
      },

      {
        path: "notices",
        children: [
          { index: true, element: <NoticeList /> },
          {
            path: "new",
            element: (
              <RoleGuard requiredRole="admin">
                <NoticeWrite />
              </RoleGuard>
            ),
          },
          { path: ":id", element: <NoticeDetail /> },
        ],
      },
    ],
  },
  { path: "*", element: <Navigate to="/" replace /> },
]);

function App() {
  return (
    <>
      <RouterProvider router={router} />
      <Toaster richColors position="top-center" />
    </>
  );
}

export default App;
