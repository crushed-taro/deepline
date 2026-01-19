import { useState } from "react";
import {
  useCreateDepartment,
  useCreatePosition,
  useDeleteDepartment,
  useDepartments,
  usePositions,
} from "@/hooks/organization/useOrganization.ts";
import { toast } from "sonner";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator.tsx";
import { Button } from "@/components/ui/button";
import { Briefcase, Building2, Plus, Trash2 } from "lucide-react";
import { Input } from "@/components/ui/input.tsx";

export default function OrganizationManage() {
  const [newDeptName, setNewDeptName] = useState("");
  const { data: deptData, isLoading: isDeptLoading } = useDepartments();
  const { mutate: createDept, isPending: isCreateDeptPending } = useCreateDepartment();
  const { mutate: deleteDept } = useDeleteDepartment();

  const [newPosName, setNewPosName] = useState("");
  const { data: posData, isLoading: isPosLoading } = usePositions();
  const { mutate: createPos, isPending: isCreatePosPending } = useCreatePosition();

  const handleAddDept = () => {
    if (!newDeptName.trim()) {
      toast.error("부서명을 입력해주세요");
      return;
    }

    createDept({ deptCode: 0, deptName: newDeptName });
    setNewDeptName("");
  };

  const handleAddPos = () => {
    if (!newPosName.trim()) {
      toast.error("직급명을 입력해주세요");
      return;
    }

    createPos({ positionCode: 0, positionName: newPosName });
    setNewPosName("");
  };

  const handleDeleteDept = (id: number, name: string) => {
    if (confirm(`'${name} 부서를 정말 삭제하시겠습니까?`)) {
      deleteDept(id);
    }
  };

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div>
        <h1 className="text-2xl font-bold">조직 관리</h1>
        <p className="text-muted-foreground">회사의 부서와 직급 체계를 관리합니다.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Building2 className="h-5 w-5" /> 부서 관리
            </CardTitle>
            <CardDescription>새로운 부서를 등록하거나 삭제합니다.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="flex gap-2">
              <Input
                placeholder="새 부서명 입력 (예: 마케팅팀)"
                value={newDeptName}
                onChange={(e) => setNewDeptName(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && handleAddDept()}
              />
              <Button onClick={handleAddDept} disabled={isCreateDeptPending}>
                <Plus className="h-4 w-4 mr-1" /> 추가
              </Button>
            </div>

            <Separator />

            <div className="space-y-2 max-h-[400px] overflow-y-auto pr-2">
              {isDeptLoading ? (
                <div className="text-center py-4 text-gray-500">로딩 중...</div>
              ) : deptData?.result?.length === 0 ? (
                <div className="text-center py-4 text-gray-500 bg-gray-50 rounded-md">
                  등록된 부서가 없습니다.
                </div>
              ) : (
                deptData?.result?.map((dept) => (
                  <div
                    key={dept.deptCode}
                    className="flex items-center justify-between p-3 border rounded-md bg-white shadow-sm hover:bg-gray-50 transition-colors"
                  >
                    <span className="font-medium">{dept.deptName}</span>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="h-8 w-8 text-gray-400 hover:text-red-600"
                      onClick={() => handleDeleteDept(dept.deptCode, dept.deptName)}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                ))
              )}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Briefcase className="h-5 w-5" /> 직급 관리
            </CardTitle>
            <CardDescription>직급 체계를 관리합니다.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="flex gap-2">
              <Input
                placeholder="새 직급명 입력 (예: 이사)"
                value={newPosName}
                onChange={(e) => setNewPosName(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && handleAddPos()}
              />
              <Button onClick={handleAddPos} disabled={isCreatePosPending}>
                <Plus className="h-4 w-4 mr-1" /> 추가
              </Button>
            </div>

            <Separator />

            <div className="space-y-2 max-h-[400px] overflow-y-auto pr-2">
              {isPosLoading ? (
                <div className="text-center py-4 text-gray-500">로딩 중...</div>
              ) : posData?.result?.length === 0 ? (
                <div className="text-center py-4 text-gray-500 bg-gray-50 rounded-md">
                  등록된 직급이 없습니다.
                </div>
              ) : (
                posData?.result?.map((pos) => (
                  <div
                    key={pos.positionCode}
                    className="flex items-center justify-between p-3 border rounded-md bg-white shadow-sm"
                  >
                    <span className="font-medium">{pos.positionName}</span>
                    <Button variant="ghost" size="icon" className="h-8 w-8 text-gray-400 hover:text-red-600">
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                ))
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
