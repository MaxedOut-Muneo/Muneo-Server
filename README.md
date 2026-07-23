<img width="1920" height="1080" alt="문어장표" src="https://github.com/user-attachments/assets/55fa763e-c12b-45bd-9666-37028b038b9a" />

<br>

## Project Overview
- 프로젝트 이름: 문어 (Muneo)
- 프로젝트 설명: 인테리어 견적서를 AI가 진단해 추가금·부실시공 리스크를 사전에 확인시켜주는 RAG 기반 의사결정 지원 서비스

<br>

## Key Features

- **Auth**
  - 이메일 기반 회원가입 및 로그인
  - 카카오 OAuth 소셜 로그인 및 추가정보 입력 플로우

- **가견적서 생성 (RAG 기반)**
  - 시공 조건(지역·공간유형·평수·공정·자재등급) 자연어 쿼리 변환 후 유사 사례 벡터 검색
  - 5단계 점진적 필터 완화(Progressive Fallback)로 검색 결과 보장
  - 건물연식·자재등급·철거여부·층수·거주여부 등 보정계수 적용
  - IQR 기반 이상치 제거로 최소/중간/최대 견적 범위 산출

- **리스크 진단**
  - 업체 견적서 이미지를 Vision 모델로 OCR·파싱해 공종별 line item으로 구조화
  - 누락(공종별 필수 키워드 대조) / 중복(동일 품목·금액 반복) / 불분명(모호 표현·금액 누락·맥락 기반) 3종 리스크 자동 탐지
  - 진단 결과를 종합 인사이트 3문장으로 요약

- **AI 챗봇 (RAG 기반)**
  - 표준 계약서·하자판정기준 문서 기반 질의응답
  - 질문 유형(견적/계약/하자/일반)에 따라 검색 대상을 견적 사례 DB 또는 규정 문서로 분기
  - 실시간 타이핑 이펙트로 응답 렌더링

- **Admin Page**
  - 사용자 목록 조회·검색·필터링(권한/가입방식/탈퇴여부/이메일인증)
  - 사용자 정보 수정, 권한 변경(사용자↔관리자), 탈퇴 처리 및 복구
  - 관리자 본인 계정 보호(자기 자신 권한변경·탈퇴 방지)

<br>

## Tech Stack

**Frontend**
<p>
  <img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white" />
  <img src="https://img.shields.io/badge/Next.js-000000?style=flat-square&logo=nextdotjs&logoColor=white" />
  <img src="https://img.shields.io/badge/Vite-646CFF?style=flat-square&logo=vite&logoColor=white" />
  <img src="https://img.shields.io/badge/React%20Router-CA4245?style=flat-square&logo=reactrouter&logoColor=white" />
  <img src="https://img.shields.io/badge/Vanilla%20Extract-F786AD?style=flat-square&logo=vanillaextract&logoColor=white" />
  <img src="https://img.shields.io/badge/TanStack%20Query-FF4154?style=flat-square&logo=reactquery&logoColor=white" />
  <img src="https://img.shields.io/badge/Zustand-433E38?style=flat-square" />
  <img src="https://img.shields.io/badge/Turborepo-FF1E56?style=flat-square&logo=turborepo&logoColor=white" />
  <img src="https://img.shields.io/badge/pnpm-F69220?style=flat-square&logo=pnpm&logoColor=white" />
</p>

<br>

**Backend**
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=flat-square&logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/OAuth2-000000?style=flat-square" />
</p>

<br>

**AI**
<p>
  <img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white" />
  <img src="https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white" />
  <img src="https://img.shields.io/badge/Anthropic_Claude-D97757?style=flat-square&logoColor=white" />
</p>

<br>

**DB**
<p>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white" />
  <img src="https://img.shields.io/badge/ChromaDB-000000?style=flat-square" />
</p>

<br>

**Infra**
<p>
  <img src="https://img.shields.io/badge/AWS_EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white" />
  <img src="https://img.shields.io/badge/AWS_RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white" />
  <img src="https://img.shields.io/badge/AWS_S3-569A31?style=flat-square&logo=amazons3&logoColor=white" />
  <img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker_Compose-2496ED?style=flat-square&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Cloudflare-F38020?style=flat-square&logo=cloudflare&logoColor=white" />
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white" />
</p>

<br>

## System Architecture
<img width="939" height="877" alt="클라우드 아키텍처 v3 (2)" src="https://github.com/user-attachments/assets/2ddb787b-b307-4ff1-a76b-c03a2b9834ce" />

<br>
<br>

## DEMO

|랜딩 페이지|로그인 / 회원가입|
|---|---|
|![image](https://github.com/user-attachments/assets/783f508d-ff4c-435b-8c19-14013d32390d)|![image](https://github.com/user-attachments/assets/207d77a9-6896-42b9-992a-c9e050489bba)|

|가견적서 생성|리스크 진단 결과|
|---|---|
|![image](https://github.com/user-attachments/assets/8ecb02ad-3fe8-4a3b-9cdb-10e9eed5c180)|![image](https://github.com/user-attachments/assets/647e996a-cc5d-47c3-9dd6-496f31d5a463)|


|홈 대시보드|AI 챗봇|
|---|---|
|![image](https://github.com/user-attachments/assets/cd93a279-9597-4ba8-a228-bb448b4b537d)|![image](https://github.com/user-attachments/assets/3ee6ae30-be96-4f0f-888a-49860a584277)|


|관리자 - 사용자 목록|관리자 - 사용자 상세|
|---|---|
|![image](https://github.com/user-attachments/assets/fbeb95b6-c271-4378-a1ae-f451ce4ce563)|![image](https://github.com/user-attachments/assets/40e4fabb-e912-4ad4-95f1-b5b2b237d09c)|


<br>

## Member

| <img width="130px" src="https://avatars.githubusercontent.com/dyk-im" /> | <img width="130px" src="https://avatars.githubusercontent.com/seoshinehyo" /> | <img width="130px" src="https://avatars.githubusercontent.com/KyeongJooni" /> | <img width="130px" src="https://avatars.githubusercontent.com/tmdcks1103" /> | <img width="130px" src="https://avatars.githubusercontent.com/Kim-Dongdong" /> | <img width="130px" src="https://avatars.githubusercontent.com/bulee5328" /> |
|:---:|:---:|:---:|:---:|:---:|:---:|
| PM, BE | BE | FE | FE | AI | AI |
| [김동윤](https://github.com/dyk-im) | [서상효](https://github.com/seoshinehyo) | [이경준](https://github.com/KyeongJooni) | [김승찬](https://github.com/tmdcks1103) | [김동섭](https://github.com/Kim-Dongdong) | [이병웅](https://github.com/bulee5328) |

<img width="1920" height="1080" alt="문어 마무리 장표" src="https://github.com/user-attachments/assets/52f5be97-6bb9-4b32-83ed-48331b35bee4" />
