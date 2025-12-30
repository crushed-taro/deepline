import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useClockIn, useClockOut } from "@/hooks/attendance/useAttendance";

function Home() {
  const { mutate: clockIn, isPending: isClockInPending } = useClockIn();
  const { mutate: clockOut, isPending: isClockOutPending } = useClockOut();

  return (
    <div className="p-8 space-y-6">
      <h1 className="text-2xl font-bold">대시보드</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>근태 관리</CardTitle>
          </CardHeader>
          <CardContent className="flex gap-4">
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
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default Home;
