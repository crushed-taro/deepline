# Deepline ERP (사내 인트라넷 시스템)

![Project Status](https://img.shields.io/badge/Project-Active-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)
![React](https://img.shields.io/badge/React-18-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)

> **"효율적인 근태 관리와 유기적인 전자결재 시스템을 갖춘 기업형 올인원 ERP 서비스"**

## 프로젝트 개요
**Deepline**은 중소규모 기업의 업무 효율성을 극대화하기 위해 설계된 인트라넷 시스템입니다.  
복잡한 결재 라인을 체계적으로 관리하고, 전자결재 승인 시 연차 차감 및 근태가 자동으로 반영되는 **자동화된 워크플로우**를 구현했습니다. 또한 Spring Security의 계층형 권한 관리(RoleHierarchy)를 도입하여 직급별로 안전한 데이터 접근을 보장합니다.

* **개발 기간**: 2025.11.01 ~ 2026.01 (약 3개월)
* **개발 인원**: 1인 (Full-Stack)

---

## Key Features (핵심 기능)

### 1. 전자결재 시스템 (Approval System)
* **기안 작성**: 휴가 신청, 일반 기안 등 다양한 양식 제공.
* **결재선 지정**: 조직도 기반으로 결재자 검색 및 다중 결재선(순차 승인) 설정.
* **승인 프로세스**: 
    * `대기` -> `진행중` -> `승인/반려` 상태 관리.
    * **자동 연동**: 휴가 신청서 최종 승인 시 **잔여 연차 자동 차감** 및 **근태 캘린더 반영**.

### 2. 근태 관리 (Attendance Management)
* **출퇴근 체크**: 대시보드에서 원클릭으로 출/퇴근 기록 (지각/조퇴 자동 판별).
* **근태 현황**: 월별 근태 기록 및 근무 시간 시각화.

### 3. 보안 및 권한 관리 (Security & RBAC)
* **계층형 권한 (Role Hierarchy)**:
    * `ROLE_ADMIN`: 전체 시스템 설정, 조직(부서/직급) 관리, 사원 관리.
    * `ROLE_HR`: 인사 발령, 사원 정보 수정.
    * `ROLE_USER`: 본인 근태/결재 관리, 공지사항 조회.
* **보안 기술**: JWT(Json Web Token) 기반 인증, Password Encoder(BCrypt) 적용.

### 4. 소통 및 정보 공유
* **공지사항**: 전사 공지 등록 및 필독(Pinned) 기능.
* **대시보드**: 로그인 직후 나의 근태, 결재 대기 건수, 최신 공지사항을 한눈에 파악.

---

## Tech Stack

### Frontend
| Category | Tech | Description |
| --- | --- | --- |
| **Language** | ![TypeScript](https://img.shields.io/badge/TypeScript-blue) | 정적 타입 시스템을 통한 안정성 확보 |
| **Framework** | ![React](https://img.shields.io/badge/React-18-blue) | Vite 기반의 SPA 개발 |
| **State Mgt** | **TanStack Query** | 서버 상태 동기화, 캐싱, Optimistic Update |
| **Styling** | **Tailwind CSS**, **Shadcn UI** | 유틸리티 퍼스트 CSS 및 재사용 가능한 UI 컴포넌트 |
| **HTTP Client** | **Axios** | Interceptor를 활용한 토큰 자동 주입 및 에러 핸들링 |

### Backend
| Category | Tech | Description |
| --- | --- | --- |
| **Framework** | ![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green) | RESTful API 서버 구축 |
| **Database** | **PostgreSQL**, **JPA(Hibernate)** | 관계형 데이터 모델링 및 ORM 사용 |
| **Security** | **Spring Security** | JWT 인증 필터 및 Method Security 적용 |
| **Docs** | **Swagger (SpringDoc)** | API 명세서 자동화 및 테스트 환경 제공 |

### DevOps
* **Docker & Docker Compose**: Database, Backend, Frontend 컨테이너 통합 관리
* **Git / GitHub**: 형상 관리 및 이슈 트래킹
