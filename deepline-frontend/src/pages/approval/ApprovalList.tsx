import { useNavigate, useSearchParams } from "react-router-dom";
import { useGetReceivedApprovals, useGetSentApprovals } from "@/hooks/approval/useApproval";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { format } from "date-fns";
import { FileText, Inbox, PenTool } from "lucide-react";
import type { ApprovalListResponse } from "@/types/approval/approval.types";

const getStatusBadge = (status: string) => {
  switch (status) {
    case "APPROVED":
      return <Badge className="bg-green-600 hover:bg-green-700">승인</Badge>;
    case "REJECTED":
      return <Badge variant="destructive">반려</Badge>;
    case "PENDING":
      return <Badge className="bg-blue-500 hover:bg-blue-600">진행중</Badge>;
    case "WAITING":
      return <Badge variant="secondary">대기</Badge>;
    default:
      return <Badge variant="outline">{status}</Badge>;
  }
};

const ApprovalTable = ({
  data,
  isLoading,
  emptyMessage,
  onRowClick,
}: {
  data: ApprovalListResponse[] | null | undefined;
  isLoading: boolean;
  emptyMessage: string;
  onRowClick: (code: number) => void;
}) => {
  if (isLoading) return <div className="p-8 text-center text-gray-500">로딩 중...</div>;
  if (!data || data.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-12 text-gray-500 border-2 border-dashed rounded-lg">
        <FileText className="w-10 h-10 mb-2 opacity-20" />
        <p>{emptyMessage}</p>
      </div>
    );
  }

  return (
    <div className="overflow-hidden border rounded-lg">
      <table className="w-full text-sm text-left">
        <thead className="bg-gray-50 border-b">
          <tr>
            <th className="px-4 py-3 font-medium text-gray-500">번호</th>
            <th className="px-4 py-3 font-medium text-gray-500">제목</th>
            <th className="px-4 py-3 font-medium text-gray-500">기안자</th>
            <th className="px-4 py-3 font-medium text-gray-500">상태</th>
            <th className="px-4 py-3 font-medium text-gray-500">기안일</th>
          </tr>
        </thead>
        <tbody className="divide-y">
          {data.map((item) => (
            <tr
              key={item.approvalCode}
              className="hover:bg-gray-50 cursor-pointer transition-colors"
              onClick={() => onRowClick(item.approvalCode)}
            >
              <td className="px-4 py-3">{item.approvalCode}</td>
              <td className="px-4 py-3 font-medium">{item.title}</td>
              <td className="px-4 py-3">{item.drafterName}</td>
              <td className="px-4 py-3">{getStatusBadge(item.approvalStatus)}</td>
              <td className="px-4 py-3 text-gray-500">
                {item.createdAt ? format(new Date(item.createdAt), "yyyy-MM-dd") : "-"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default function ApprovalList() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const activeTab = searchParams.get("tab") || "received";

  const { data: receivedData, isLoading: isReceivedLoading } = useGetReceivedApprovals();
  const { data: sentData, isLoading: isSentLoading } = useGetSentApprovals();

  const handleTabChange = (val: string) => {
    setSearchParams({ tab: val });
  };

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">전자결재</h1>
        <Button onClick={() => navigate("/approvals/new")}>
          <PenTool className="w-4 h-4 mr-2" />
          기안 작성
        </Button>
      </div>

      <Tabs value={activeTab} onValueChange={handleTabChange} className="w-full">
        <TabsList className="grid w-full grid-cols-2 max-w-[400px]">
          <TabsTrigger value="received" className="flex items-center gap-2">
            <Inbox className="w-4 h-4" /> 받은 결재함
          </TabsTrigger>
          <TabsTrigger value="sent" className="flex items-center gap-2">
            <FileText className="w-4 h-4" /> 보낸 결재함
          </TabsTrigger>
        </TabsList>

        <div className="mt-6">
          <TabsContent value="received">
            <Card>
              <CardHeader>
                <CardTitle>승인 대기 문서</CardTitle>
              </CardHeader>
              <CardContent>
                <ApprovalTable
                  data={receivedData?.result}
                  isLoading={isReceivedLoading}
                  emptyMessage="처리할 결재 문서가 없습니다."
                  onRowClick={(code) => navigate(`/approvals/${code}`)}
                />
              </CardContent>
            </Card>
          </TabsContent>

          <TabsContent value="sent">
            <Card>
              <CardHeader>
                <CardTitle>나의 기안 문서</CardTitle>
              </CardHeader>
              <CardContent>
                <ApprovalTable
                  data={sentData?.result}
                  isLoading={isSentLoading}
                  emptyMessage="상신한 문서가 없습니다."
                  onRowClick={(code) => navigate(`/approvals/${code}`)}
                />
              </CardContent>
            </Card>
          </TabsContent>
        </div>
      </Tabs>
    </div>
  );
}
