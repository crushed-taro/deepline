import { useNavigate, useParams } from "react-router-dom";
import { useUserRole } from "@/hooks/auth/useAuth.ts";
import { useDeleteNotice, useGetNoticeDetail } from "@/hooks/notice/useNotice.ts";
import { Button } from "@/components/ui/button.tsx";
import { ArrowLeft, Eye, Trash2 } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge.tsx";
import { format } from "date-fns";
import { Separator } from "@/components/ui/separator.tsx";

export default function NoticeDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const noticeId = Number(id);

  const role = useUserRole();
  const isAdmin = role === "admin" || role === "ROLE_HR";

  const { data: response, isLoading } = useGetNoticeDetail(noticeId);
  const { mutate: deleteNotice } = useDeleteNotice();

  if (isLoading) return <div className="p-8 text-center">로딩 중...</div>;
  if (!response?.result) return <div className="p-8 text-center">공지사항을 찾을 수 없습니다.</div>;

  const notice = response.result;

  const handleDelete = () => {
    if (confirm("정말 이 공지사항을 삭제하시겠습니까?")) {
      deleteNotice(noticeId);
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <Button variant="ghost" onClick={() => navigate("/notices")} className="pl-0">
          <ArrowLeft className="w-4 h-4 mr-2" /> 목록으로
        </Button>
        {isAdmin && (
          <Button variant="destructive" size="sm" onClick={handleDelete}>
            <Trash2 className="w-4 h-4 mr-2" /> 삭제
          </Button>
        )}
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-start justify-between gap-4">
            <div className="space-y-1">
              <div className="flex items-center gap-2">
                {notice.isPinned && <Badge variant="destructive">필독</Badge>}
                <CardTitle className="text-xl">{notice.title}</CardTitle>
              </div>
              <div className="flex items-center gap-4 text-sm text-muted-foreground">
                <span>작성자: {notice.authorName}</span>
                <span>
                  {notice.createdAt ? format(new Date(notice.createdAt), "yyyy-MM-dd HH:mm") : "-"}
                </span>
                <span className="flex items-center gap-1">
                  <Eye className="w-3 h-3" /> {notice.viewCount}
                </span>
              </div>
            </div>
          </div>
        </CardHeader>
        <Separator />
        <CardContent className="pt-8 min-h-[300px]">
          <div className="whitespace-pre-wrap leading-relaxed">{notice.content}</div>
        </CardContent>
      </Card>
    </div>
  );
}
