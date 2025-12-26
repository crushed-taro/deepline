import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
// import MainLayout from '@/layouts/MainLayout.tsx';
// import Home from '@/pages/Home.tsx';
import Login from "@/pages/Login.tsx";
import { Toaster } from "sonner";

// const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
//     const token = localStorage.getItem('accessToken');
//     return token ? children : <Navigate to="/login" replace />;
// };

const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  // {
  //     path: '/',
  //     element: (
  //         <PrivateRoute>
  //             <MainLayout />
  //         </PrivateRoute>
  //     ),
  //     children: [
  //         { path: '/', element: <Home /> },
  //     ],
  // },
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
