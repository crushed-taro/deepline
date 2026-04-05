# Deepline ERP (사내 인트라넷 시스템)

![Project Status](https://img.shields.io/badge/Project-Active-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)
![React](https://img.shields.io/badge/React-18-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message_Broker-FF6600)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)

> **"Linux 서버 환경에서 Docker 기반 배포를 수행하고, RabbitMQ와 WebSocket을 활용해 비동기/실시간 아키텍처를 설계한 엔터프라이즈 ERP 서비스입니다."**

## 프로젝트 개요
Deepline은 단순한 데이터 관리를 넘어, 조직원 간의 실시간 소통과 끊김 없는 워크플로우에 집중한 ERP 시스템입니다. 단일 서버의 한계를 고려하여 **메시지 브로커(RabbitMQ)**를 도입해 결합도를 낮추고, **WebSocket(STOMP)과 SSE**를 활용해 엔터프라이즈급 실시간 통신 환경을 구축했습니다.

* **개발 기간**: 2025.11. ~ 진행 중
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

### 4. 실시간 통신 아키텍처 및 강력한 소켓 보안 (WebSocket & STOMP)
* **외부 브로커 연동**: 다중 서버 환경의 확장성을 고려하여 Spring 내장 브로커 대신 **RabbitMQ를 Stomp Broker Relay로 연동**하여 메시지 유실 방지 및 라우팅 최적화.
* **소켓 보안**: 일반적인 HTTP 필터(`JwtFilter`)가 웹소켓 환경에서 동작하지 않는 한계를 파악하고, **`ChannelInterceptor`를 구현**하여 Handshake(CONNECT) 시점의 JWT 토큰을 검증해 인가된 사용자만 소켓 세션에 접근하도록 보안 강화.

### 5. 이벤트 기반 비동기 아키텍처 (Event-Driven & RabbitMQ)
* 핵심 비즈니스 로직(결재 승인)과 부가 로직(실시간 알림)의 **강결합 및 트랜잭션 전파 문제**를 해결하기 위해 메시지 큐 도입.
* 결재 승인 시 알림 데이터를 RabbitMQ 큐(`notification.queue`)로 Produce하고, Consumer가 이를 받아 SSE로 비동기 전송함으로써 **장애 격리(Fault Tolerance) 및 응답 속도 개선**.

### 6. 실시간 양방향 알림 및 상태 동기화 (SSE)
* `EventSourcePolyfill`을 활용해 JWT 인증 기반의 **SSE(Server-Sent Events)** 연결 구축.
* 사용자의 온라인/오프라인 접속 상태(Presence) 실시간 동기화 및 결재/공지사항 실시간 알림 푸시.

### 7. 성능 최적화 및 안정성 확보
* **AOP 적용**: 결재 상태 변경 등 주요 비즈니스 액션에 `@AuditLog` 커스텀 어노테이션과 AOP(`@Around`)를 적용하여, 핵심 로직의 수정 없이 선언적으로 감사 로그(Audit Log)를 적재하도록 관심사 분리(SoC).
* **N+1 문제 해결**: 조직도 및 사원 목록 조회 시 연관된 부서/직급 데이터를 가져올 때 발생하는 N+1 쿼리 문제를 JPA `@EntityGraph` (Fetch Join)를 활용해 단일 쿼리로 튜닝.
* **전역 예외 처리**: `@RestControllerAdvice`와 Custom Enum `ErrorCode`를 활용한 Global Exception Handler를 구축하여 프론트엔드와 일관된 에러 JSON 규격 통일.

### 8. 소통 및 정보 공유
* **공지사항**: 전사 공지 등록 및 필독(Pinned) 기능.
* **대시보드**: 로그인 직후 나의 근태, 결재 대기 건수, 최신 공지사항을 한눈에 파악.

---

## Trouble Shooting (문제 해결 경험)

### 1. Docker + Nginx 환경에서 WebSocket 및 SSE 연결 끊김 현상
* **문제 상황**: 로컬 환경과 달리 운영(Docker/Nginx) 환경에서 WebSocket 연결이 실패하고, SSE 스트림이 약 1분 후 강제로 끊어지는 현상 발생.
* **원인 분석**: Nginx Reverse Proxy의 기본 타임아웃 설정이 짧고, HTTP 프로토콜을 WebSocket으로 업그레이드하는 헤더가 백엔드로 전달되지 않음을 로그를 통해 확인.
* **해결 방법**: `nginx.conf`에 `Upgrade` 및 `Connection` 헤더 명시적 추가. SSE 연결 유지를 위해 `proxy_read_timeout`을 24시간(86400s)으로 연장하고, 클라이언트 단에서 `heartbeatTimeout`을 설정하여 프록시 환경의 스트림 통신 안정성 확보.

### 2. 결재 최종 승인과 연차 차감 간의 트랜잭션 정합성
* **문제 상황**: 휴가 결재 최종 승인 시 잔여 연차가 부족하면 에러가 터져야 하나, 알림 전송 등 부가 로직이 먼저 실행되어 데이터 정합성이 깨질 위험 발견.
* **해결 방법**: 도메인 객체(`Member.useVacation()`) 내부에 연차 검증 로직을 캡슐화하여 비즈니스 예외를 먼저 발생시키고, Transactional 범위 내에서 외부 통신(RabbitMQ)이 가장 마지막에 안전하게 실행되도록 순서와 트랜잭션 경계 재설계.

---

## Tech Stack

* **Backend**: Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA
* **Messaging & Real-time**: RabbitMQ, WebSocket(STOMP), SSE
* **Frontend**: React 18, TypeScript, Zustand, TanStack Query, Tailwind CSS
* **Database**: PostgreSQL 15
* **DevOps / Infra**: Docker, Docker Compose, Nginx, Rocky Linux, Git, GitHub

---

## Business Features (주요 업무 기능)

* **조직도 기반 메신저**: 실시간 접속 상태 확인 및 1:1, 부서별 다대다 실시간 채팅
* **결재 워크플로우**: 동적 결재선 지정, 결재/반려 처리, 휴가 승인 시 잔여 연차 자동 차감
* **근태 및 보안 관리**: 원클릭 출퇴근 처리, 계층형 권한(Role Hierarchy) 기반 접근 제어