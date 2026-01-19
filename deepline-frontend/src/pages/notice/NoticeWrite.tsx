import { useNavigate } from "react-router-dom";
import { useCreateNotice } from "@/hooks/notice/useNotice.ts";
import { useState } from "react";
import { toast } from "sonner";
import { Label } from "@/components/ui/label.tsx";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input.tsx";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import { ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Checkbox } from "@/components/ui/checkbox.tsx";

export function NoticeWrite() {
  const navigate = useNavigate();
  const { mutate: createNotice, isPending } = useCreateNotice();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [isPinned, setIsPinned] = useState(false);

  const handleSubmit = () => {
    if (!title.trim() || !content.trim()) {
      toast.error("제목 또는 내용을 입력해주세요.");
      return;
    }

    createNotice({
      title,
      content,
      isPinned,
    });
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <Button variant="ghost" onClick={() => navigate(-1)} className="pl-0">
        <ArrowLeft className="w-4 h-4 mr-2" /> 목록으로
      </Button>

      <Card>
        <CardHeader>
          <CardTitle>공지사항 작성</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="title">제목</Label>
            <Input
              id="title"
              placeholder="공지 제목을 입력하세요"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>

          <div className="flex items-center space-x-2">
            <Checkbox
              id="pinned"
              checked={isPinned}
              onCheckedChange={(checked) => setIsPinned(checked as boolean)}
            />
            <Label htmlFor="pinned" className="cursor-pointer font-normal text-muted-foreground">
              상단 고정 (중요 공지사항으로 등록)
            </Label>
          </div>

          <div className="space-y-2">
            <Label htmlFor="content">내용</Label>
            <Textarea
              id="content"
              placeholder="공지 내용을 상세히 입력하세요."
              className="min-h-[400px]"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>

          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => navigate(-1)}>
              취소
            </Button>
            <Button onClick={handleSubmit} disabled={isPending}>
              {isPending ? "등록 중..." : "등록하기"}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
