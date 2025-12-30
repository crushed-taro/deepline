import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import { Toaster } from "sonner";
import Signup from "@/pages/auth/Signup.tsx";
import Login from "@/pages/auth/Login.tsx";
import FindId from "@/pages/member/FindId.tsx";
import Home from "@/pages/Home.tsx";
import MainLayout from "@/layouts/MainLayout.tsx";
import ApprovalWrite from "@/pages/approval/ApprovalWrite.tsx";

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
      {
        index: true,
        element: <Home />,
      },
      {
        path: "attendance",
        element: <div>근태 관리 페이지 (준비중)</div>,
      },
      {
        path: "approvals",
        children: [
          { path: "new", element: <ApprovalWrite /> },
          { path: "sent", element: <div>기안함 페이지 (준비중)</div> },
          { path: "received", element: <div>결재함 페이지 (준비중)</div> },
        ],
      },
      // { path: "profile", element: <MyPage /> },
    ],
  },

  {
    path: "*",
    element: <Navigate to="/" replace />,
  },
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
