import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRegisterApproval } from "@/hooks/approval/useApproval";
import { useGetMemberList } from "@/hooks/member/useMember";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Trash2, Plus, Search, UserCheck } from "lucide-react";
import { toast } from "sonner";
import { Separator } from "@/components/ui/separator";
import { Badge } from "@/components/ui/badge";

interface SelectedApprover {
  memberCode: number;
  memberName: string;
  deptName: string;
  positionName: string;
}

export default function ApprovalWrite() {
  const navigate = useNavigate();
  const { mutate: register, isPending } = useRegisterApproval();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [approvers, setApprovers] = useState<SelectedApprover[]>([]);

  const [searchTerm, setSearchTerm] = useState("");
  const [isSearchActive, setIsSearchActive] = useState(false);

  const { data: memberPage } = useGetMemberList(0, 5, searchTerm);

  const handleAddApprover = (member: SelectedApprover) => {
    const myName = localStorage.getItem("memberName");
    if (member.memberName === myName) {
      toast.error("본인은 결재자로 지정할 수 없습니다.");
      return;
    }

    if (approvers.some((a) => a.memberCode === member.memberCode)) {
      toast.error("이미 추가된 결재자입니다.");
      return;
    }

    setApprovers([
      ...approvers,
      {
        memberCode: member.memberCode,
        memberName: member.memberName,
        deptName: member.deptName,
        positionName: member.positionName,
      },
    ]);

    toast.success(`${member.memberName}님이 결재선에 추가되었습니다.`);
  };

  const handleRemoveApprover = (index: number) => {
    const newApprovers = [...approvers];
    newApprovers.splice(index, 1);
    setApprovers(newApprovers);
  };

  const handleSubmit = () => {
    if (!title.trim()) {
      toast.error("제목을 입력해주세요.");
      return;
    }
    if (!content.trim()) {
      toast.error("내용을 입력해주세요.");
      return;
    }
    if (approvers.length === 0) {
      toast.error("최소 1명 이상의 결재자를 지정해야 합니다.");
      return;
    }

    register({
      title,
      content,
      approverCodes: approvers.map((a) => a.memberCode),
    });
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold tracking-tight">결재 상신</h1>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => navigate(-1)}>
            취소
          </Button>
          <Button onClick={handleSubmit} disabled={isPending}>
            {isPending ? "상신 중..." : "상신하기"}
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>기안 내용</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="title">제목</Label>
                <Input
                  id="title"
                  placeholder="예: 휴가 신청서"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="content">내용</Label>
                <textarea
                  id="content"
                  className="flex min-h-[300px] w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                  placeholder="결재 내용을 상세히 작성해주세요."
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                />
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <UserCheck className="h-5 w-5" />
                결재선 지정
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-2">
                <Label>결재자 검색</Label>
                <div className="flex gap-2">
                  <Input
                    placeholder="이름으로 검색"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") setIsSearchActive(true);
                    }}
                  />
                  <Button size="icon" onClick={() => setIsSearchActive(true)}>
                    <Search className="h-4 w-4" />
                  </Button>
                </div>

                {searchTerm && (
                  <div className="border rounded-md mt-2 max-h-40 overflow-y-auto bg-white p-2 space-y-1">
                    {memberPage?.result?.content.length === 0 ? (
                      <p className="text-xs text-center text-muted-foreground py-2">
                        검색 결과가 없습니다.
                      </p>
                    ) : (
                      memberPage?.result?.content.map((member) => (
                        <div
                          key={member.memberCode}
                          className="flex items-center justify-between text-sm p-2 hover:bg-gray-100 rounded cursor-pointer"
                          onClick={() => handleAddApprover(member)}
                        >
                          <div>
                            <span className="font-medium">{member.memberName}</span>
                            <span className="text-xs text-muted-foreground ml-1">
                              ({member.deptName}/{member.positionName})
                            </span>
                          </div>
                          <Plus className="h-4 w-4 text-primary" />
                        </div>
                      ))
                    )}
                  </div>
                )}
              </div>

              <Separator />

              <div className="space-y-3">
                <Label>결재 순서 (순차 승인)</Label>
                {approvers.length === 0 ? (
                  <div className="text-sm text-muted-foreground text-center py-4 border border-dashed rounded-md bg-gray-50">
                    지정된 결재자가 없습니다.
                  </div>
                ) : (
                  <div className="space-y-2">
                    {approvers.map((approver, index) => (
                      <div
                        key={approver.memberCode}
                        className="flex items-center justify-between p-3 border rounded-md bg-white shadow-sm"
                      >
                        <div className="flex items-center gap-3">
                          <Badge
                            variant="secondary"
                            className="h-6 w-6 rounded-full flex items-center justify-center p-0"
                          >
                            {index + 1}
                          </Badge>
                          <div>
                            <p className="text-sm font-medium">{approver.memberName}</p>
                            <p className="text-xs text-muted-foreground">
                              {approver.deptName} · {approver.positionName}
                            </p>
                          </div>
                        </div>
                        <Button
                          variant="ghost"
                          size="icon"
                          className="h-8 w-8 text-muted-foreground hover:text-destructive"
                          onClick={() => handleRemoveApprover(index)}
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
