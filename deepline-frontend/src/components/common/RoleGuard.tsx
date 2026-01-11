import { useUserRole } from "@/hooks/auth/useAuth.ts";
import { Navigate } from "react-router-dom";

interface RoleGuardProps {
  children: React.ReactNode;
  requiredRole: string;
}

export default function RoleGuard({ children, requiredRole }: RoleGuardProps) {
  const role = useUserRole();

  if (role !== requiredRole) {
    alert("접근 권한이 없습니다.");
    return <Navigate to="/" replace />;
  }

  return children;
}
