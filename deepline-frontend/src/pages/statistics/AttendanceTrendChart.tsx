import { useGetAttendanceStats } from "@/hooks/statistics/useStatistics.ts";
import { useState } from "react";
import { format } from "date-fns";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

export default function AttendanceTrendChart() {
  const today = new Date();
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth() + 1);

  const { data: attendanceStatusData } = useGetAttendanceStats(year, month);

  const chartData =
    attendanceStatusData?.result?.map((item: any) => ({
      ...item,
      day: format(new Date(item.date), "dd일"),
    })) || [];

  return (
    <Card className="col-span-4">
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className="text-lg font-medium">
          {year}년 {month}월 근태 추이
        </CardTitle>

        <div className="flex gap-2">
          <Select value={month.toString()} onValueChange={(val) => setMonth(Number(val))}>
            <SelectTrigger className="w-[100px] h-8 text-xs">
              <SelectValue placeholder="월 선택" />
            </SelectTrigger>
            <SelectContent>
              {Array.from({ length: 12 }, (_, i) => i + 1).map((m) => (
                <SelectItem key={m} value={m.toString()}>
                  {m}월
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </CardHeader>

      <CardContent>
        <div className="h-[300px] w-full mt-4">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} />
              <XAxis
                dataKey="day"
                fontSize={12}
                tickLine={false}
                axisLine={false}
                tickMargin={10}
              />
              <YAxis fontSize={12} tickLine={false} axisLine={false} allowDecimals={false} />
              <Tooltip
                cursor={{ fill: "transparent" }}
                contentStyle={{
                  borderRadius: "8px",
                  border: "none",
                  boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
                }}
                formatter={(value: number) => [`${value}명`, ""]}
              />
              <Legend wrapperStyle={{ paddingTop: "20px" }} />

              <Bar
                dataKey="present"
                name="정상출근"
                stackId="a"
                fill="#4ade80"
                radius={[0, 0, 0, 0]}
              />
              <Bar dataKey="late" name="지각" stackId="a" fill="#facc15" />
              <Bar dataKey="vacation" name="휴가" stackId="a" fill="#60a5fa" />
              <Bar dataKey="absent" name="결근" stackId="a" fill="#f87171" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </CardContent>
    </Card>
  );
}
