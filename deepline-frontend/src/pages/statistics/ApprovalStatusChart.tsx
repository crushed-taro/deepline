import { useGetApprovalStats } from "@/hooks/statistics/useStatistics.ts";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import { Cell, Legend, Pie, PieChart, ResponsiveContainer, Tooltip } from "recharts";

const COLORS = ["#94a3b8", "#3b82f6", "#22c55e", "#ef4444"];
const STATUS_MAP: Record<string, string> = {
  WAITING: "대기",
  PENDING: "진행중",
  APPROVED: "승인",
  REJECTED: "반려",
};

export default function ApprovalStatusChart() {
  const { data: approvalStatusData } = useGetApprovalStats();

  const chartData =
    approvalStatusData?.result?.map((item: any) => ({
      name: STATUS_MAP[item.status] || item.status,
      value: item.count,
    })) || [];

  return (
    <Card className="col-span-3">
      <CardHeader>
        <CardTitle>결재 상태 분포</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="h-[300px] w-full">
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={chartData}
                cx="50%"
                cy="50%"
                innerRadius={60}
                outerRadius={80}
                paddingAngle={5}
                dataKey="value"
              >
                {chartData.map((_entry: any, index: number) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip formatter={(value: any) => `${value}건`} />
              <Legend verticalAlign="bottom" height={36} />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </CardContent>
    </Card>
  );
}
