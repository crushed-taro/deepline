import { Button } from "@/components/ui/button.tsx";
import { LogOut, Smile, UserCircle } from "lucide-react";
import { useLogout } from "@/hooks/auth/useAuth.ts";
import { useGetMyInfo } from "@/hooks/member/useMember.ts";
import { differenceInDays } from "date-fns";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { useNavigate } from "react-router-dom";

export default function Header() {
  const { logout } = useLogout();
  const navigate = useNavigate();

  const { data: myInfo, isLoading } = useGetMyInfo();

  const getDaysSinceJoined = () => {
    if (!myInfo?.result?.createdAt) return 0;

    const joinedDate = new Date(myInfo.result.createdAt);
    const today = new Date();

    return differenceInDays(today, joinedDate) + 1;
  };

  return (
    <header className="h-16 bg-white border-b border-gray-200 px-6">
      <div className="h-full flex items-center justify-end">
        <div className="flex items-center gap-4">
          {isLoading ? (
            <Skeleton className="h-6 w-48" />
          ) : (
            <div className="flex items-center gap-2 text-sm text-gray-700">
              <div className="p-1.5 bg-blue-50 rounded-full text-blue-600">
                <Smile className="w-4 h-4" />
              </div>
              <span className="font-semibold">{myInfo?.result?.memberName}</span>님,
              <span className="text-gray-500">Deepline과 함께한 지</span>
              <span className="inline-flex items-center justify-center bg-primary/10 text-primary font-bold px-2 py-0.5 rounded text-xs">
                +{getDaysSinceJoined()}일
              </span>
              <span className="text-gray-500">째입니다</span>
            </div>
          )}
        </div>
        <div>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => navigate("/profile")}
            className="flex items-center gap-2 text-gray-600 hover:text-primary hover:bg-primary/5"
          >
            <UserCircle className="h-5 w-5" />내 정보
          </Button>
          <Button variant="ghost" size="sm" onClick={logout} className="flex items-center gap-2">
            <LogOut className="h-4 w-4" />
            로그아웃
          </Button>
        </div>
      </div>
    </header>
  );
}
