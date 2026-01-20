import { useState } from "react";
import { useLogin } from "@/hooks/auth/useAuth.ts";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card.tsx";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Link, useLocation } from "react-router-dom";

export default function Login() {
  const location = useLocation() as { state?: { foundId?: string } };
  const foundId = location.state?.foundId;

  const [memberId, setMemberId] = useState(foundId ?? "");
  const [password, setPassword] = useState("");

  const { mutate: login, isPending } = useLogin();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const id = memberId.trim();
    const pw = password.trim();

    if (!id || !pw) return;

    login({ memberId: id, memberPassword: pw });
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold text-center">Deepline ERP</CardTitle>
          <CardDescription className="text-center">
            사내 시스템 접속을 위해 로그인해주세요.
          </CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="id" className="text-sm font-medium">
                아이디
              </Label>
              <Input
                id="id"
                name="memberId"
                type="text"
                autoComplete="username"
                placeholder="아이디를 입력하세요"
                value={memberId}
                onChange={(e) => setMemberId(e.target.value)}
                disabled={isPending}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password" className="text-sm font-medium">
                비밀번호
              </Label>
              <Input
                id="password"
                type="password"
                autoComplete="current-password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                disabled={isPending}
              />
            </div>
          </CardContent>
          <CardFooter className="flex flex-col gap-3 pt-6">
            <Button type="submit" className="w-full" disabled={isPending}>
              {isPending ? "로그인 중..." : "로그인"}
            </Button>
            <div className="text-center text-sm text-muted-foreground">
              계정이 없으신가요? <span className="mx-2"></span>
              <Link to="/signup" className="text-primary font-medium hover:underline">
                회원가입
              </Link>
            </div>
            <div className="text-center text-sm text-muted-foreground">
              계정을 잊으셨나요?
              <span className="mx-2"></span>
              <Link to="/find-id" className="text-primary font-medium hover:underline">
                아이디 찾기
              </Link>
              <span className="mx-2">·</span>
              <Link to="/reset-password" className="text-primary font-medium hover:underline">
                비밀번호 찾기
              </Link>
            </div>
          </CardFooter>
        </form>
      </Card>
    </div>
  );
}
