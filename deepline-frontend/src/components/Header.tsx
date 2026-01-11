import { Button } from "@/components/ui/button.tsx";
import { LogOut } from "lucide-react";
import { useLogout } from "@/hooks/auth/useAuth.ts";

export default function Header() {
  const { logout } = useLogout();

  return (
    <header className="h-16 bg-white border-b border-gray-200 px-6">
      <div className="h-full flex items-center justify-end">
        <Button variant="ghost" size="sm" onClick={logout} className="flex items-center gap-2">
          <LogOut className="h-4 w-4" />
          로그아웃
        </Button>
      </div>
    </header>
  );
}
