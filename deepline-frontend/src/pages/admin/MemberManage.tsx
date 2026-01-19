import type { MemberResponse } from "@/types/member/member.types";
import { useState } from "react";
import { useAssignMember, useGetMemberList } from "@/hooks/member/useMember.ts";
import { useDepartments, usePositions } from "@/hooks/organization/useOrganization";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input.tsx";
import { Search, UserCog } from "lucide-react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet.tsx";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { format } from "date-fns";

export default function MemberManage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedMember, setSelectedMember] = useState<MemberResponse | null>(null);
  const [deptCode, setDeptCode] = useState<string>("");
  const [positionCode, setPositionCode] = useState<string>("");

  const { data: memberPage, isLoading } = useGetMemberList(0, 20, searchTerm);
  const { data: deptData } = useDepartments();
  const { data: positionData } = usePositions();
  const { mutate: assignMember, isPending: isAssigning } = useAssignMember();

  const handleMemberClick = (member: MemberResponse) => {
    setSelectedMember(member);

    const currentDept = deptData?.result?.find((d) => d.deptName === member.deptName);
    const currentPos = positionData?.result?.find((p) => p.positionName === member.positionName);

    setDeptCode(currentDept ? currentDept.deptCode.toString() : "");
    setPositionCode(currentPos ? currentPos.positionCode.toString() : "");
  };

  const handleSave = () => {
    if (!selectedMember || !deptCode || !positionCode) return;

    assignMember(
      {
        memberId: selectedMember.memberId,
        data: {
          deptCode: Number(deptCode),
          positionCode: Number(positionCode),
        },
      },
      {
        onSuccess: () => {
          setSelectedMember(null);
        },
      }
    );
  };

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold flex items-center gap-2">
          <UserCog className="h-6 w-6" />
          사원 관리 (인사 발령)
        </h1>
      </div>

      <div className="flex w-full max-w-sm items-center space-x-2">
        <Input
          placeholder="이름으로 검색..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <Button size="icon" variant="ghost">
          <Search className="h-4 w-4" />
        </Button>
      </div>

      <div className="rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[100px]">사번</TableHead>
              <TableHead>이름</TableHead>
              <TableHead>아이디</TableHead>
              <TableHead>부서</TableHead>
              <TableHead>직급</TableHead>
              <TableHead>가입일</TableHead>
              <TableHead className="text-right">관리</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={7} className="h-24 text-center">
                  로딩 중...
                </TableCell>
              </TableRow>
            ) : memberPage?.result?.content.length === 0 ? (
              <TableRow>
                <TableCell colSpan={7} className="h-24 text-center">
                  검색 결과가 없습니다.
                </TableCell>
              </TableRow>
            ) : (
              memberPage?.result?.content.map((member) => (
                <TableRow key={member.memberCode}>
                  <TableCell>{member.memberCode}</TableCell>
                  <TableCell className="font-medium">{member.memberName}</TableCell>
                  <TableCell>{member.memberId}</TableCell>
                  <TableCell>
                    <Badge variant="outline">{member.deptName || "미배정"}</Badge>
                  </TableCell>
                  <TableCell>
                    <Badge variant="secondary">{member.positionName || "미배정"}</Badge>
                  </TableCell>
                  <TableCell>
                    {member.createdAt ? format(new Date(member.createdAt), "yyyy-MM-dd") : "-"}
                  </TableCell>
                  <TableCell className="text-right">
                    <Button variant="outline" size="sm" onClick={() => handleMemberClick(member)}>
                      발령 관리
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <Sheet open={!!selectedMember} onOpenChange={(open) => !open && setSelectedMember(null)}>
        <SheetContent>
          <SheetHeader>
            <SheetTitle>인사 발령</SheetTitle>
            <SheetDescription>
              {selectedMember?.memberName} 사원의 부서 및 직급을 변경합니다.
            </SheetDescription>
          </SheetHeader>

          <div className="grid gap-4 py-6">
            <div className="grid gap-2">
              <Label htmlFor="dept">부서 선택</Label>
              <Select value={deptCode} onValueChange={setDeptCode}>
                <SelectTrigger>
                  <SelectValue placeholder="부서를 선택하세요" />
                </SelectTrigger>
                <SelectContent>
                  {deptData?.result?.map((dept) => (
                    <SelectItem key={dept.deptCode} value={dept.deptCode.toString()}>
                      {dept.deptName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="grid gap-2">
              <Label htmlFor="position">직급 선택</Label>
              <Select value={positionCode} onValueChange={setPositionCode}>
                <SelectTrigger>
                  <SelectValue placeholder="직급을 선택하세요" />
                </SelectTrigger>
                <SelectContent>
                  {positionData?.result?.map((pos) => (
                    <SelectItem key={pos.positionCode} value={pos.positionCode.toString()}>
                      {pos.positionName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="flex flex-col gap-2 mt-4">
            <Button onClick={handleSave} disabled={isAssigning}>
              {isAssigning ? "저장 중..." : "변경 내용 저장"}
            </Button>
            <Button variant="ghost" onClick={() => setSelectedMember(null)}>
              취소
            </Button>
          </div>
        </SheetContent>
      </Sheet>
    </div>
  );
}
