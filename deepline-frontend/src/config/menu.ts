import {
  BarChart3,
  Building2,
  Clock,
  FileSignature,
  FileText,
  Inbox,
  LayoutDashboard,
  type LucideIcon,
  Megaphone,
  Send,
  UserCog,
} from "lucide-react";

interface MenuItem {
  title: string;
  url: string;
  icon: LucideIcon;
  requiredRole?: "admin" | "ROLE_HR";
  items?: MenuItem[];
}

export const sidebarMenu: MenuItem[] = [
  {
    title: "대시보드",
    url: "/",
    icon: LayoutDashboard,
  },
  {
    title: "전자결재",
    url: "/approvals",
    icon: FileSignature,
    items: [
      {
        title: "결재 작성하기",
        url: "/approvals/new",
        icon: Send,
      },
      {
        title: "기안함",
        url: "/approvals/sent",
        icon: FileText,
      },
      {
        title: "결재함",
        url: "/approvals/received",
        icon: Inbox,
      },
    ],
  },
  {
    title: "공지사항",
    url: "/notices",
    icon: Megaphone,
  },
];

export const adminMenu: MenuItem[] = [
  {
    title: "조직 관리",
    url: "/admin/organization",
    icon: Building2,
    requiredRole: "admin",
  },
  {
    title: "사원 관리",
    url: "/admin/members",
    icon: UserCog,
    requiredRole: "admin",
  },
  {
    title: "결재 상태 차트",
    url: "/admin/approvalstats",
    icon: BarChart3,
    requiredRole: "admin",
  },
  {
    title: "근태 차트",
    url: "/admin/attendancestats",
    icon: Clock,
    requiredRole: "admin",
  },
];
