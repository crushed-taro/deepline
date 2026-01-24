import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { MemberApi } from "@/api/member/memerApi";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { toast } from "sonner";

export default function ResetPassword() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    memberId: "",
    memberName: "",
    memberEmail: "",
    newPassword: "",
    confirmPassword: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.memberId || !formData.memberName || !formData.memberEmail) {
      toast.error("모든 정보를 입력해주세요.");
      return;
    }
    if (formData.newPassword !== formData.confirmPassword) {
      toast.error("비밀번호가 일치하지 않습니다.");
      return;
    }
    if (formData.newPassword.length < 4) {
      toast.error("비밀번호는 4자리 이상이어야 합니다.");
      return;
    }

    try {
      const response = await MemberApi.resetPassword(formData);
      if (response.status === "OK" || response.code === "MEMBER_002") {
        toast.success("비밀번호가 성공적으로 변경되었습니다.");
        navigate("/login");
      } else {
        toast.error(response.message || "비밀번호 변경에 실패했습니다.");
      }
    } catch (error: any) {
      console.error(error);
      const msg = error.response?.data?.message || "오류가 발생했습니다.";
      toast.error(msg);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold text-center">비밀번호 재설정</CardTitle>
          <CardDescription className="text-center">
            회원 정보를 입력하고 새로운 비밀번호를 설정하세요.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="memberId">아이디</Label>
              <Input
                id="memberId"
                name="memberId"
                placeholder="아이디를 입력하세요"
                value={formData.memberId}
                onChange={handleChange}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="memberName">이름</Label>
              <Input
                id="memberName"
                name="memberName"
                placeholder="이름을 입력하세요"
                value={formData.memberName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="memberEmail">이메일</Label>
              <Input
                id="memberEmail"
                name="memberEmail"
                type="email"
                placeholder="등록된 이메일을 입력하세요"
                value={formData.memberEmail}
                onChange={handleChange}
                required
              />
            </div>

            <div className="my-4 border-t border-gray-200" />

            <div className="space-y-2">
              <Label htmlFor="newPassword">새 비밀번호</Label>
              <Input
                id="newPassword"
                name="newPassword"
                type="password"
                placeholder="새 비밀번호"
                value={formData.newPassword}
                onChange={handleChange}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="confirmPassword">새 비밀번호 확인</Label>
              <Input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                placeholder="새 비밀번호 확인"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
              />
            </div>

            <Button type="submit" className="w-full mt-4 bg-blue-600 hover:bg-blue-700">
              비밀번호 변경
            </Button>
            <Button
              type="button"
              variant="outline"
              className="w-full mt-2"
              onClick={() => navigate("/login")}
            >
              로그인으로 돌아가기
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
