import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useGetApprovalDetail, useProcessApproval } from "@/hooks/approval/useApproval";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Badge } from "@/components/ui/badge";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import { CheckCircle2, XCircle, ArrowLeft, Calendar } from "lucide-react";
import { format } from "date-fns";

export default function ApprovalDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const approvalCode = Number(id);
  const myName = localStorage.getItem("memberName");

  const { data: response, isLoading } = useGetApprovalDetail(approvalCode);
  const { mutate: process, isPending } = useProcessApproval();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [processType, setProcessType] = useState<"APPROVED" | "REJECTED">("APPROVED");
  const [comment, setComment] = useState("");

  if (isLoading) return <div className="p-8 text-center">문서 정보를 불러오는 중...</div>;
  if (!response?.result) return <div className="p-8 text-center">문서를 찾을 수 없습니다.</div>;

  const doc = response.result;

  const myLine = doc.approvalLines.find((line) => line.approverName === myName);
  const isMyTurn = myLine && myLine.status === "PENDING";

  const handleProcess = () => {
    process({
      code: approvalCode,
      data: { status: processType, comments: comment },
    });
    setIsModalOpen(false);
  };

  const openModal = (type: "APPROVED" | "REJECTED") => {
    setProcessType(type);
    setComment("");
    setIsModalOpen(true);
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <Button variant="ghost" onClick={() => navigate("/approvals")} className="pl-0">
          <ArrowLeft className="w-4 h-4 mr-2" /> 목록으로
        </Button>
        <div className="flex gap-2">
          {isMyTurn && (
            <>
              <Button variant="destructive" onClick={() => openModal("REJECTED")}>
                <XCircle className="w-4 h-4 mr-2" /> 반려
              </Button>
              <Button
                className="bg-green-600 hover:bg-green-700"
                onClick={() => openModal("APPROVED")}
              >
                <CheckCircle2 className="w-4 h-4 mr-2" /> 승인
              </Button>
            </>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-2">
          <CardHeader>
            <div className="flex justify-between items-start gap-4">
              <div>
                <CardTitle className="text-xl mb-2 flex items-center gap-2">
                  {doc.title}
                  {doc.type === "VACATION" && <Badge variant="outline">휴가신청</Badge>}
                </CardTitle>
                <div className="text-sm text-muted-foreground flex flex-wrap gap-4">
                  <span>
                    기안자: <span className="text-foreground font-medium">{doc.drafterName}</span>
                  </span>
                  <span>부서: {doc.deptName}</span>
                  <span>기안일: {format(new Date(doc.createdAt), "yyyy-MM-dd HH:mm")}</span>
                </div>
              </div>
              <Badge
                variant={
                  doc.statue === "APPROVED"
                    ? "default"
                    : doc.statue === "REJECTED"
                      ? "destructive"
                      : "secondary"
                }
                className="text-base px-3 py-1"
              >
                {doc.statue}
              </Badge>
            </div>
          </CardHeader>
          <Separator />
          <CardContent className="pt-6 min-h-[400px]">
            {doc.type === "VACATION" && doc.startDate && doc.endDate && (
              <div className="mb-6 p-4 bg-blue-50 dark:bg-blue-950/30 rounded-lg border border-blue-100 dark:border-blue-900 flex items-center gap-3 text-blue-800 dark:text-blue-300">
                <Calendar className="w-5 h-5" />
                <span className="font-semibold">신청 기간:</span>
                <span>
                  {doc.startDate} ~ {doc.endDate}
                </span>
              </div>
            )}

            <div className="whitespace-pre-wrap leading-relaxed text-foreground/90">
              {doc.content}
            </div>
          </CardContent>
        </Card>

        <Card className="h-fit">
          <CardHeader>
            <CardTitle className="text-lg">결재 진행 현황</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="relative border-l-2 border-gray-200 dark:border-gray-800 ml-3 space-y-8 pl-6 py-2">
              {doc.approvalLines.map((line, idx) => (
                <div key={idx} className="relative">
                  <span
                    className={`absolute -left-[33px] flex h-7 w-7 items-center justify-center rounded-full ring-4 ring-background ${
                      line.status === "APPROVED"
                        ? "bg-green-500"
                        : line.status === "REJECTED"
                          ? "bg-red-500"
                          : line.status === "PENDING"
                            ? "bg-blue-500 animate-pulse"
                            : "bg-gray-300"
                    }`}
                  >
                    {line.status === "APPROVED" && <CheckCircle2 className="h-4 w-4 text-white" />}
                    {line.status === "REJECTED" && <XCircle className="h-4 w-4 text-white" />}
                    {line.status === "PENDING" && (
                      <span className="h-2 w-2 rounded-full bg-white" />
                    )}
                  </span>

                  <div className="flex flex-col">
                    <div className="flex justify-between items-center mb-1">
                      <span className="font-semibold text-sm">
                        {line.approverName} {line.positionName}
                      </span>
                      <span
                        className={`text-xs px-1.5 py-0.5 rounded ${
                          line.status === "APPROVED"
                            ? "bg-green-100 text-green-700"
                            : line.status === "REJECTED"
                              ? "bg-red-100 text-red-700"
                              : "bg-gray-100 text-gray-600"
                        }`}
                      >
                        {line.status === "WAITING"
                          ? "대기"
                          : line.status === "PENDING"
                            ? "검토중"
                            : line.status}
                      </span>
                    </div>
                    {line.processedAt && (
                      <span className="text-xs text-muted-foreground mb-1">
                        {format(new Date(line.processedAt), "MM-dd HH:mm")}
                      </span>
                    )}
                    {line.comment && (
                      <div className="mt-1 text-sm bg-muted p-2 rounded text-foreground/80 italic">
                        "{line.comment}"
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{processType === "APPROVED" ? "결재 승인" : "결재 반려"}</DialogTitle>
          </DialogHeader>
          <div className="py-4">
            <Textarea
              placeholder={
                processType === "APPROVED"
                  ? "승인 코멘트를 입력하세요 (선택)"
                  : "반려 사유를 입력하세요 (필수)"
              }
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              className="min-h-[100px]"
            />
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsModalOpen(false)}>
              취소
            </Button>
            <Button
              variant={processType === "APPROVED" ? "default" : "destructive"}
              onClick={handleProcess}
              disabled={isPending}
            >
              {processType === "APPROVED" ? "승인하기" : "반려하기"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
