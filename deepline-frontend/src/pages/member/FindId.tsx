import { useState } from "react";
import { useFindId } from "@/hooks/member/useMember.ts";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card.tsx";
import { Label } from "@/components/ui/label.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Link } from "react-router-dom";

export default function FindId() {
  const [memberName, setMemberName] = useState("");
  const [memberEmail, setMemberEmail] = useState("");

  const { mutate: findid, isPending } = useFindId();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const name = memberName.trim();
    const email = memberEmail.trim();

    if (!name || !email) return;

    findid({ memberName: name, memberEmail: email });
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold text-center">Deepline ERP</CardTitle>
          <CardDescription className="text-center">
            사내 시스템 접속을 위해 아이디를 찾아주세요.
          </CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="name" className="text-sm font-medium">
                이름
              </Label>
              <Input
                id="name"
                name="memberName"
                type="text"
                autoComplete="username"
                placeholder="이름을 입력하세요"
                value={memberName}
                onChange={(e) => setMemberName(e.target.value)}
                disabled={isPending}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="email" className="text-sm font-medium">
                비밀번호
              </Label>
              <Input
                id="email"
                name="memberEmail"
                type="text"
                autoComplete="email"
                placeholder="이메일을 입력하세요"
                value={memberEmail}
                onChange={(e) => setMemberEmail(e.target.value)}
                disabled={isPending}
              />
            </div>
          </CardContent>
          <CardFooter className="flex flex-col gap-3 pt-6">
            <Button type="submit" className="w-full" disabled={isPending}>
              {isPending ? "아이디 찾는 중..." : "아이디 찾기"}
            </Button>

            <div className="text-center text-sm">
              이미 계정이 있으신가요?{" "}
              <Link to="/login" className="text-primary font-medium hover:underline">
                로그인
              </Link>
            </div>
          </CardFooter>
        </form>
      </Card>
    </div>
  );
}
