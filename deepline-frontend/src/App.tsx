import { createBrowserRouter, RouterProvider, Navigate, redirect } from "react-router-dom";
import { Toaster } from "sonner";
import Signup from "@/pages/auth/Signup.tsx";
import Login from "@/pages/auth/Login.tsx";
import FindId from "@/pages/member/FindId.tsx";
import Home from "@/pages/Home.tsx";

const requireAuthLoader = () => {
  const token = localStorage.getItem("accessToken");
  if (!token) throw redirect("/login");
  return null;
};

const publicOnlyLoader = () => {
  const token = localStorage.getItem("accessToken");
  if (token) throw redirect("/home");
  return null;
};

const router = createBrowserRouter([
  {
    loader: publicOnlyLoader,
    children: [
      { path: "/login", element: <Login /> },
      { path: "/signup", element: <Signup /> },
      { path: "/find-id", element: <FindId /> },
    ],
  },
  {
    loader: requireAuthLoader,
    children: [{ path: "/home", element: <Home /> }],
  },
  { path: "*", element: <Navigate to="/home" replace /> },
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
