import { useGetMyInfo, useResetPassword, useUpdateMyInfo } from "@/hooks/member/useMember.ts";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card.tsx";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Briefcase, Camera } from "lucide-react";
import { format } from "date-fns";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button";
import { useEffect, useRef, useState } from "react";
import { toast } from "sonner";
import defaultProfile from "@/assets/images/default-profile.jpg";
import { resolveBackendUrl } from "@/lib/url.ts";

export default function Profile() {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [inputName, setInputName] = useState("");
  const [inputEmail, setInputEmail] = useState("");
  const fileInputRef = useRef<HTMLInputElement>(null);

  const { data: myInfo, isLoading } = useGetMyInfo();

  const { mutate: resetPassword, isPending: isResetting } = useResetPassword();

  const { mutate: updateInfo, isPending: isUpdating } = useUpdateMyInfo();

  const member = myInfo?.result;

  useEffect(() => {
    if (member) {
      if (member.profileUrl) {
        setPreviewImage(resolveBackendUrl(member.profileUrl));
      } else {
        setPreviewImage(null);
      }
      setInputName(member.memberName);
      setInputEmail(member.memberEmail);
    }
  }, [member]);

  if (isLoading) {
    return (
      <div className="p-8 space-y-4">
        <Skeleton className="h-12 w-1/3" />
        <Skeleton className="h-64 w-full" />
      </div>
    );
  }

  const handleImageClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setSelectedFile(file);
      const objectUrl = URL.createObjectURL(file);
      setPreviewImage(objectUrl);
    }
  };

  const handleSaveInfo = () => {
    const formData = new FormData();

    const updateDto = {
      memberName: inputName,
      memberEmail: inputEmail,
    };

    const jsonBlob = new Blob([JSON.stringify(updateDto)], {
      type: "application/json",
    });
    formData.append("data", jsonBlob);

    if (selectedFile) {
      formData.append("image", selectedFile);
    }

    updateInfo(formData, {
      onSuccess: () => {
        toast.success("회원 정보가 수정되었습니다.");
        setSelectedFile(null);
      },
      onError: (error: any) => {
        console.error(error);
        toast.error("정보 수정에 실패했습니다.");
      },
    });
  };

  function handleChange() {
    if (!currentPassword || !newPassword || !confirmPassword) {
      toast.error("모든 비밀번호를 입력해주세요.");
      return;
    }

    if (newPassword !== confirmPassword) {
      toast.error("새 비밀번호가 일치하지 않습니다.");
      return;
    }

    if (newPassword.length < 1) {
      toast.error("비밀번호는 최소 8자 이상이어야 합니다.");
      return;
    }

    if (member) {
      resetPassword(
        {
          memberId: member.memberId,
          memberName: member.memberName,
          memberEmail: member.memberEmail,
          newPassword: newPassword,
          confirmPassword: confirmPassword,
        },
        {
          onSuccess: () => {
            toast.success("비밀번호가 성공적으로 변경되었습니다.");
            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");
          },
          onError: (error: any) => {
            toast.error(error.response?.data?.message || "비밀번호 변경에 실패했습니다.");
          },
        }
      );
    }
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold tracking-tight">마이페이지</h1>

      <Card>
        <CardContent className="flex items-center gap-6 p-6">
          <div className="relative group cursor-pointer" onClick={handleImageClick}>
            <Avatar className="h-24 w-24 border-2 border-gray-100">
              <AvatarImage
                src={previewImage || defaultProfile}
                className="object-cover"
                alt="Profile"
              />
              <AvatarFallback className="text-xl bg-primary/10 text-primary">
                {member?.memberName?.slice(0, 2)}
              </AvatarFallback>
            </Avatar>

            <div className="absolute inset-0 bg-black/40 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
              <Camera className="text-white w-8 h-8" />
            </div>

            <input
              type="file"
              ref={fileInputRef}
              className="hidden"
              accept="image/*"
              onChange={handleFileChange}
            />
          </div>
          <div className="space-y-1">
            <h2 className="text-2xl font-bold">{member?.memberName}</h2>
            <div className="text-muted-foreground flex items-center gap-2">
              <Briefcase className="h-4 w-4" />
              {member?.deptName} · {member?.positionName}
            </div>
            <p className="text-sm text-muted-foreground">
              입사일: {member?.createdAt ? format(new Date(member.createdAt), "yyyy-MM-dd") : "-"}
            </p>
          </div>
          <div className="ml-auto text-right">
            <div className="text-sm text-muted-foreground">잔여 연차</div>
            <div className="text-3xl font-bold text-blue-600">{member?.remainVacation}일</div>
          </div>
        </CardContent>
      </Card>

      <Tabs defaultValue="account" className="w-full">
        <TabsList className="grid w-full grid-cols-2 lg:w-[400px]">
          <TabsTrigger value="account">기본 정보</TabsTrigger>
          <TabsTrigger value="password">비밀번호 변경</TabsTrigger>
        </TabsList>

        <TabsContent value="account">
          <Card>
            <CardHeader>
              <CardTitle>계정 정보</CardTitle>
              <CardDescription>회원님의 기본 정보를 확인하고 수정할 수 있습니다.</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-2">
                <Label>아이디 (사번)</Label>
                <Input value={member?.memberId || ""} disabled className="bg-muted" />
              </div>

              <div className="grid gap-2">
                <Label>이름</Label>
                <Input
                  value={inputName}
                  onChange={(e) => setInputName(e.target.value)}
                  placeholder="이름을 입력하세요"
                />
              </div>

              <div className="grid gap-2">
                <Label>이메일</Label>
                <Input
                  value={inputEmail}
                  onChange={(e) => setInputEmail(e.target.value)}
                  placeholder="이메일을 입력하세요"
                />
              </div>

              <div className="flex justify-end">
                <Button onClick={handleSaveInfo} disabled={isUpdating}>
                  {isUpdating ? "저장 중..." : "정보 수정 저장"}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="password">
          <Card>
            <CardHeader>
              <CardTitle>보안 설정</CardTitle>
              <CardDescription>비밀번호를 주기적으로 변경하여 계정을 보호하세요.</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-2">
                <Label htmlFor="current">현재 비밀번호</Label>
                <Input
                  id="current"
                  type="password"
                  value={currentPassword}
                  onChange={(e) => setCurrentPassword(e.target.value)}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="new">새 비밀번호</Label>
                <Input
                  id="new"
                  type="password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="confirm">새 비밀번호 확인</Label>
                <Input
                  id="confirm"
                  type="password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </div>
              <div className="flex justify-end">
                <Button onClick={handleChange} disabled={isResetting} variant="outline">
                  {isResetting ? "변경 중..." : "비밀번호 변경"}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
