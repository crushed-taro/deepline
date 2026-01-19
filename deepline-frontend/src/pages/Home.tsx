import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useClockIn, useClockOut } from "@/hooks/attendance/useAttendance";
import { useNavigate } from "react-router-dom";
import { useGetNoticeList } from "@/hooks/notice/useNotice.ts";
import { useGetReceivedApprovals } from "@/hooks/approval/useApproval.ts";
import { Skeleton } from "@/components/ui/skeleton";
import { ChevronRight, FileSignature, Megaphone, Plane } from "lucide-react";
import { format } from "date-fns";
import { Badge } from "@/components/ui/badge.tsx";
import { useGetMyInfo } from "@/hooks/member/useMember.ts";

function Home() {
  const { mutate: clockIn, isPending: isClockInPending } = useClockIn();
  const { mutate: clockOut, isPending: isClockOutPending } = useClockOut();

  const navigate = useNavigate();

  const { data: noticeData, isLoading: isNoticeLoading } = useGetNoticeList(0, 5);
  const { data: approvalData, isLoading: isApprovalLoading } = useGetReceivedApprovals();

  const { data: myInfoData, isLoading: isMyInfoLoading } = useGetMyInfo();

  return (
    <div className="p-8 space-y-6">
      <h1 className="text-2xl font-bold">대시보드</h1>

      <div className="grid lg:grid-cols-3 md:grid-cols-2 gap-3">
        <Card>
          <CardHeader>
            <CardTitle>근태 관리</CardTitle>
          </CardHeader>
          <CardContent className="flex flex-col gap-4">
            <Button
              onClick={() => clockIn()}
              disabled={isClockInPending}
              className="w-full bg-blue-600 hover:bg-blue-700"
            >
              {isClockInPending ? "처리중..." : "출근하기"}
            </Button>

            <Button variant="outline" onClick={() => clockOut()} className="w-full">
              퇴근하기
              {isClockOutPending ? "처리중..." : "퇴근하기"}
            </Button>
            <div className="h-[200px] flex items-center justify-center bg-muted/20 rounded-md">
              근태 관리 UI 영역
            </div>
          </CardContent>
        </Card>

        <Card className="shadow-sm">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">결재 대기 문서</CardTitle>
            <FileSignature className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            {isApprovalLoading ? (
              <Skeleton className="h-8 w-20" />
            ) : (
              <div className="flex items-baseline space-x-2">
                <span className="text-2xl font-bold">{approvalData?.result?.length || 0}</span>
                <span className="text-sm text-muted-foreground">건</span>
              </div>
            )}
            <div className="mt-3">
              <Button
                variant="link"
                className="p-0 h-auto text-xs text-blue-600"
                onClick={() => navigate("/approvals/received")}
              >
                결재함 바로가기 &rarr;
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card className="shadow-sm">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">잔여 연차</CardTitle>
            <Plane className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            {isMyInfoLoading ? (
              <Skeleton className="h-8 w-20" />
            ) : (
              <div className="flex items-baseline space-x-2">
                <span className="text-2xl font-bold text-green-600">
                  {myInfoData?.result?.remainVacation ?? 0}
                </span>
                <span className="text-sm text-muted-foreground">일</span>
              </div>
            )}
            <div className="mt-3">
              <Button
                variant="link"
                className="p-0 h-auto text-xs text-blue-600"
                onClick={() => navigate("/approvals/new")}
              >
                휴가 신청하기 &rarr;
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-6">
        <Card className="col-span-1 shadow-sm">
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <div className="flex items-center gap-2">
              <Megaphone className="h-5 w-5 text-primary" />
              <CardTitle className="text-lg">최신 공지사항</CardTitle>
            </div>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => navigate("/notices")}
              className="text-muted-foreground"
            >
              더보기 <ChevronRight className="ml-1 h-4 w-4" />
            </Button>
          </CardHeader>
          <CardContent>
            {isNoticeLoading ? (
              <div className="space-y-2">
                <Skeleton className="h-10 w-full" />
                <Skeleton className="h-10 w-full" />
              </div>
            ) : noticeData?.result?.content.length === 0 ? (
              <div className="flex h-32 items-center justify-center text-muted-foreground text-sm">
                등록된 공지사항이 없습니다.
              </div>
            ) : (
              <div className="space-y-1">
                {noticeData?.result?.content.map((notice) => (
                  <div
                    key={notice.noticeCode}
                    className="flex items-center justify-between p-3 rounded-lg hover:bg-muted/50 cursor-pointer transition-colors group"
                    onClick={() => navigate(`/notices/${notice.noticeCode}`)}
                  >
                    <div className="flex items-center gap-3 overflow-hidden">
                      {notice.isPinned && (
                        <Badge variant="destructive" className="shrink-0 text-[10px] px-1.5 py-0.5">
                          필독
                        </Badge>
                      )}
                      <span className="truncate font-medium text-sm group-hover:text-primary transition-colors">
                        {notice.title}
                      </span>
                    </div>
                    <div className="flex items-center gap-4 text-xs text-muted-foreground shrink-0">
                      <span className="hidden sm:inline-block">{notice.authorName}</span>
                      <span>
                        {notice.createdAt && format(new Date(notice.createdAt), "yyyy.MM.dd")}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default Home;
