import { useNavigate } from "react-router-dom";
import { useGetNoticeList } from "@/hooks/notice/useNotice";
import { useUserRole } from "@/hooks/auth/useAuth";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { format } from "date-fns";
import { Megaphone, PenTool, Pin } from "lucide-react";

export default function NoticeList() {
  const navigate = useNavigate();
  const role = useUserRole();
  const isAdmin = role === "admin" || role === "ROLE_HR";

  const { data, isLoading } = useGetNoticeList(0, 20);

  if (isLoading) return <div className="p-8 text-center">공지사항을 불러오는 중...</div>;

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold flex items-center gap-2">
          <Megaphone className="h-6 w-6 text-primary" />
          공지사항
        </h1>
        {isAdmin && (
          <Button onClick={() => navigate("/notices/new")}>
            <PenTool className="w-4 h-4 mr-2" />
            공지 작성
          </Button>
        )}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>전사 공지</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="overflow-hidden border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-[80px] text-center">번호</TableHead>
                  <TableHead>제목</TableHead>
                  <TableHead className="w-[120px] text-center">작성자</TableHead>
                  <TableHead className="w-[100px] text-center">조회수</TableHead>
                  <TableHead className="w-[120px] text-center">작성일</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {data?.result?.content.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} className="h-24 text-center">
                      등록된 공지사항이 없습니다.
                    </TableCell>
                  </TableRow>
                ) : (
                  data?.result?.content.map((notice) => (
                    <TableRow
                      key={notice.noticeCode}
                      className={`cursor-pointer hover:bg-muted/50 ${notice.isPinned ? "bg-red-50 hover:bg-red-100/50 dark:bg-red-950/20" : ""}`}
                      onClick={() => navigate(`/notices/${notice.noticeCode}`)}
                    >
                      <TableCell className="text-center font-medium">
                        {notice.isPinned ? (
                          <Pin className="h-4 w-4 mx-auto text-red-500 fill-red-500" />
                        ) : (
                          notice.noticeCode
                        )}
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          {notice.isPinned && (
                            <Badge variant="destructive" className="text-xs px-1.5 py-0">
                              필독
                            </Badge>
                          )}
                          <span className="font-medium">{notice.title}</span>
                        </div>
                      </TableCell>
                      <TableCell className="text-center text-muted-foreground">
                        {notice.authorName}
                      </TableCell>
                      <TableCell className="text-center text-muted-foreground">
                        {notice.viewCount}
                      </TableCell>
                      <TableCell className="text-center text-muted-foreground">
                        {notice.createdAt ? format(new Date(notice.createdAt), "yyyy-MM-dd") : "-"}
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
