# 🚀S09P12D206 치뽈팀 프로젝트

## 🔍프로젝트 소개

### 📌서비스 요약

- 마피아 게임의 룰을 기반으로하여 웹에서 즐길 수 있는 게임 서비스
- 기존 다른 마피아 게임 서비스에 WEB RTC를 적용하여 화상 및 음성 채팅을 통해 게임을 즐길 수 있음

### 📌기획 의도

- 마피아 게임을 웹에서 즐길 수 있는 서비스를 제공하여 마피아 게임의 접근성을 높임
- WEB RTC를 적용하여 화상 및 음성 채팅을 통해 마피아 게임을 더 즐겁게 즐길 수 있음

### 📌기술 스택

<img src="https://img.shields.io/badge/typescript-3178C6?styleflat&logo=typescript&logoColor=white">
<img src="https://img.shields.io/badge/vite-646CFF?styleflat&logo=vite&logoColor=white">
<img src="https://img.shields.io/badge/react-61DAFB?styleflat&logo=react&logoColor=white">
<img src="https://img.shields.io/badge/tailwindcss-06B6D4?styleflat&logo=tailwindcss&logoColor=white">

<img src="https://img.shields.io/badge/springboot-6DB33F?styleflat&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?styleflat&logo=springsecurity&logoColor=white">

### 📌협업 및 배포 툴

<img src="https://img.shields.io/badge/figma-F24E1E?styleflat&logo=figma&logoColor=white">
<img src="https://img.shields.io/badge/gitlab-FC6D26?styleflat&logo=gitlab&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?styleflat&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/jenkins-D24939?styleflat&logo=jenkins&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?styleflat&logo=Notion&logoColor=white">
<img src="https://img.shields.io/badge/mattermost-0058CC?styleflat&logo=mattermost&logoColor=white">
<img src="https://img.shields.io/badge/jira-0052CC?styleflat&logo=jira&logoColor=white">

### 📌프로젝트 기간

- 2023.07.10 ~ 2023.08.18

## 🙍팀원 소개

- 이제성 : 팀장, 프론트엔드, 게임 로직
- 강예찬 : 프론트엔드, openvidu, 디자인
- 정슬호 : 프론트엔드, STOMP, 인프라
- 고예림 : 백엔드, STOMP, 인프라
- 서현덕 : 백엔드, 게임 로직
- 시민주 : 백엔드, openvidu, 게임 로직

## 📑Convention

### 📌Git Commit Convention

#### 1. 커밋 유형

- 커밋 제목 첫 글자는 대문자로 작성하기

  | 커밋 유형        | 의미                                                         |
  | ---------------- | ------------------------------------------------------------ |
  | Feat             | 새로운 기능 추가                                             |
  | Fix              | 버그 수정                                                    |
  | Docs             | 문서 수정                                                    |
  | Style            | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
  | Refactor         | 코드 리팩토링                                                |
  | Test             | 테스트 코드, 리팩토링 테스트 코드 추가                       |
  | Chore            | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore           |
  | Design           | CSS 등 사용자 UI 디자인 변경                                 |
  | Comment          | 필요한 주석 추가 및 변경                                     |
  | Rename           | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우          |
  | Remove           | 파일을 삭제하는 작업만 수행한 경우                           |
  | !BREAKING CHANGE | 커다란 API 변경의 경우                                       |
  | !HOTFIX          | 급하게 치명적인 버그를 고쳐야 하는 경우                      |

#### 2. 제목과 본문을 빈행으로 분리

- 커밋 유형 이후 제목과 본문은 한글로 작성하여 내용이 잘 전달될 수 있도록 할 것
- 본문에는 변경한 내용과 이유 설명 (어떻게보다는 무엇 & 왜를 설명)

#### 3. 제목 첫 글자는 대문자로, 끝에는 `.` 금지

#### 4. 제목은 영문 기준 50자 이내로 할 것

#### 5. 무엇을 왜 했는지 적기, 어떻게 했는지 적지 않기

### 📌Git Branch Convention

#### 규칙

- `master`에서 각자의 기능 브랜치를 분기
- 브랜치 이름 규칙
  - `작업종류/기능` 으로 브랜치 만들기
  - (지라 스토리 이슈 = 기능 명세서 소 분류 = 브랜치 1개)
  - (지라 작업 이슈 = 커밋 1개)
  - `design/FE-logout` `feat/BE-signup` `fix/FE-not-render-nav`
- 해당 기능의 브랜치에 작업이 완료 되면 해당 브랜치를 원격 저장소에 `push`하고 git Lab 페이지에서 `merge request(source : 본인 기능 브랜치, target : master)`(로컬에서 그냥 `merge` X)
  - `merge request` 오픈 이벤트 발생 시 EC2에서 빌드, 배포 실행 ⇒ MM으로 결과 알림
- `merge request` 위 결과에 따라 승인 여부 결정, 필요한 경우 코드 리뷰 및 토의
- `merge request`가 승인되면 `merge된 master` 브랜치에대하여 다시 EC2에서 빌드, 배포 실행 ⇒ MM으로 결과 알림
  - EC2에서 테스트 코드 실행 / 빌드(번들링 등의 파일 빌드) / 배포 진행 ⇒ 메신저로 결과 알림

#### 주의

- `master` 브랜치로 부터 개발하는 용도 외의 복사본 용을 git clone하여 `master` 브랜치로 부터 계속 pull 받으면서 복사본으로 사용\*\*
- `merge request` 승인 후 에러 생길 시 `git revert` 사용 권장\*\*
- **원격 저장소 `master`에 `merge` 이후 꼭 잊지말고 `master` 에서 `pull` 하기**

### 📌Code Convention

#### 백, 프론트 공통 코드 컨벤션

- 메소드 파라미터는 최대 4개 이하
- 의미 있는 공백 넣기
- 메소드는 최대 30줄 이하
- 들여쓰기 깊이는 최대 3칸
- 함수명, 변수명은 카멜케이스
- 주석은 설명하려는 구문에 맞춰 들여쓰기

  ```
  function someFunction() {
  ...

    // statement에 관한 주석
    statements
  }
  ```

- 지역 변수의 범위를 최소화 하기 위해, 처음 사용되는 지점에 가깝게 선언

#### 프론트엔드 코드 컨벤션

- 객체 타입 선언시 interface, 그외 type 사용

  ```typescript
  interface propsType {
    name: string;
  }

  type name = number | string;
  ```

- React는 화살표를 사용한 함수형 컴포넌트, props 타입 직접 지정

  ```typescript
  import React from "react";

  type propsType = {
    name: string;
  };

  const test = ({ name }: propsType) => {
    return <div>test</div>;
  };

  export default test;
  ```

#### 백엔드 코드 컨벤션

- 모든 변수는 하나의 한개만 선언

  ```java
  int a, b; // X

  int a;
  int b; // O
  ```

- package 이름 : 소문자와 숫자만 사용 `ex) com.example.deepspace`

- Class 이름 : 첫 번째 문자를 대문자로 시작하며, 명사로 작성

  ```java
  class Test { }

  //단어가 2개 이상 혼합되어 있다면,
  //각 단어의 첫 번째 문자를 대문자로 표현
  class UserInfo { }
  ```

- Interface 이름 : 첫 번째 문자를 대문자interface Runnable { }로 시작하며, 형용사로 작성

  ```java
  interface Runnable { }
  interface ActionListener { }
  ```

- C style 배열선언 금지
  ```java
  String[] args // O
  String args[] // X
  ```
- 구현부가 없거나 한 줄의 구문을 포함해도 중괄호를 사용한다.

  ```java
  if(a > b) {
  a = b;
  }
  ```

- 상수는 모두 대문자로 작성하며, 단어 사이를 밑줄(\_)로 구분
- 클래스의 멤버와 initializer 의 순서는 따로 없지만, 최대한 논리적인 순서를 따름 새 메서드를 끝에 추가하는 것은 시간순이지 논리적인 순서가 아님
- 빈 블록은 줄바꿈하지 않고 {} 로 사용 multi-block(if/else, try/catch/finally) 은 줄바꿈
- 같은 동일한 이름의 메서드, 생성자는 연속적으로 작성
- @Override: 항상 사용

### 📌Jira Convention

#### 이슈 구조

- `최상단 에픽 이슈` : 개발, 테스트, 배포, 설계 - 포인트 x
- `중단 스토리 이슈` : 기능 명세서 소분류 - 포인트 x
- `하단 작업 이슈` : 명세서 소분류의 하위 작업들 - 포인트 부여
  - 이때, 작업 이슈를 따로 만들고 이슈 연결을 해야함
  - 1 포인트 = 1시간으로 포인트 부여하고 하루 최소 8포인트 부여

#### 이슈 구조 만들기

1. `에픽 이슈` 생성
2. `에픽 이슈`에서 `하위 이슈 추가 버튼` 클릭하여 `스토리 이슈` 생성
3. `작업 이슈`를 따로 생성
4. 2에서 만든 `스토리 이슈`에서 `이슈 연결 버튼` 클릭하여 작업 이슈와 연결

#### 1주일의 스프린트 사용 FLOW

1. `백로그`에 이슈 만들기 - 위 참고
2. `스프린트 만들기`(기간은 그 주 월요일 ~ 일요일)
3. `백로그`에서 만든 이슈(스토리 이슈와 작업 이슈)를 모두 `스프린트`로 옮기기
   1. 이때, `스토리 이슈` 바로 밑에 연결되는 `작업 이슈`를 위치하게 하여 정리하기
4. `스프린트 시작 버튼` 클릭
5. `작업 이슈`에 `진행 중`으로 표시 추가적으로 `진행 중` 표시하고 `포인트 부여`하고 일 시작
   1. 이때 오늘 `하루 8포인트(8시간)`을 적당히 나누어 `작업 이슈`에 부여한다, 최소 8포인트이고 이상 포인트 가능
   2. 1개의 작업 이슈에 할당할 수 있는 포인트는 1~4 포인트
   3. 이때 해당 `작업 이슈` 의 상위에 `스토리 이슈`에도 `진행 중 표시`하기
6. `작업 이슈`에 해당하는 일이 완료 되면 `완료 표시`하기
   1. 이때 `스토리 이슈`에 해당하는 `모든 작업 이슈`가 완료되면 `스토리 이슈`도 `완료 표시`하기
7. 스프린트 완료 일요일 날 되면 꼭 누르기

#### 추가 주의사항

- Git Lab 연동 고려
- 진행 중 이슈는 항상 1개이어야 한다
- 빠질 일이 있으면 그 만큼 포인트 차감하여도 됨
- 싸피에서 진행하는 라이브 등의 싸피 일정 추가하여도 됨

## 📚명세서

### 📌요구사항 명세서

#### 기능적 요구사항

| 순번  | 요구사항명     | 요구사항 상세                                                                                                                                                                                                      | 우선순위  |
| ----- | -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------- |
| F01_1 | 회원가입       | 1. 이메일, 비밀번호, 닉네임을 입력받고 회원 가입 가능<br>2. Google 계정으로 회원가입                                                                                                                               | 매우 높음 |
| F01_2 | 회원정보 수정  | 기존 비밀번호를 입력하고 이후 새로운 비밀번호 등록하여 비밀번호 수정                                                                                                                                               | 보통      |
| F01_3 | 회원정보 조회  | 닉네임, ID, Email을 확인 가능한 프로필 페이지 보여주기                                                                                                                                                             | 높음      |
| F01_4 | 회원 탈퇴      | 유저 개인 정보만 삭제 후 탈퇴 처리                                                                                                                                                                                 | 높음      |
| F01_5 | 로그인         | - email, password 로 로그인<br>- Google 계정으로 로그인                                                                                                                                                            | 매우 높음 |
| F01_6 | 로그아웃       | token 삭제하여 로그아웃                                                                                                                                                                                            | 매우 높음 |
| F01_7 | 비밀번호 찾기  | Email 인증으로 비밀번호 찾기                                                                                                                                                                                       | 보통      |
| F02   | 회원 전적 확인 | - 본인의 최근 전적 10개 확인<br>- 플레이한 직업, 승패를 색깔로 표시<br>- 참가한 인원수와 직업 대표 이미지 표시                                                                                                     | 높음      |
| F03   | 방 관리        | 1. 방을 100개까지 유지<br>2. 방 존재 여부 및 참가 인원 조회<br>3. 방 인원수 설정, 게임 타이머 설정<br>4. 8명 이하의 화상 및 채팅이 가능한 방 생성<br>5. 게임 준비 상태에서만 게임 시작<br>6. 방장이 방을 삭제 가능 | 매우 높음 |
| F04   | 투표 기능      | 밤이 되면 유저 중 한 명을 선택                                                                                                                                                                                     | 매우 높음 |
| F05   | 게임 기능      | 1. 밤과 낮을 번갈아가며 턴제 기반의 게임 구현<br>2. 랜덤으로 역할 배정<br>3. 역할에 따라 능력 사용 가능<br>4. 상황에 따라 유저의 카메라 및 소리 설정 변경 가능                                                     | 매우 높음 |
| F06   | 도움말 기능    | 게임룰 및 직업 설명 확인                                                                                                                                                                                           | 보통      |
| F07   | 채팅 기능      | 유저들 간의 텍스트 기반 통신 가능                                                                                                                                                                                  | 높음      |
| F08   | 사운드         | - 프로그램 실행 시 초기화면에 배경 음악 재생<br>- 상황에 맞는 효과음 적용                                                                                                                                          | 매우 높음 |
| F09   | 디자인         | 토끼 이미지: 기본, 걷기, 눕기(죽음), 처형 이미지                                                                                                                                                                   | 매우 높음 |
| F10   | 예외 사항 처리 | 통신 불안정 혹은 유저가 중간에 나갔을 경우 다시 접속하도록 처리                                                                                                                                                    | 높음      |

#### 비기능적 요구사항

| 순번 | 요구사항명    | 요구사항 상세                                      | 우선순위 |
| ---- | ------------- | -------------------------------------------------- | -------- |
| NF1  | 사용자 편의성 | 웹 사이트에 대한 사전지식이 없어도 쓰기 편해야 함  |          |
| NF2  | 실시간 통신   | 1. 8명 이하의 동시 화상 통신 및 채팅이 가능해야 함 |          |
|      |               | 2. 영상 통신 딜레이가 100ms 이하여야 함            |          |
|      |               | 3. 채팅 딜레이가 50ms 이하여야 함                  |          |
| NF3  | 응답성        | 방 리스트 조회에 걸리는 시간이 2초 이하여야 함     |          |
|      |               | 방 입장 시 걸리는 시간이 3초 이하                  |          |
|      |               | 방 생성 시 걸리는 시간이 3초 이하                  |          |
| NF4  | 호환성        | DeskTop PC 및 크롬 브라우저 권장                   |          |
|      |               | 화면 해상도 1920*1080 or 1536*846에 최적화         |          |

### 📌기능 명세서

| 대분류        | 소분류                | 주기능                          | 상세기능                                                                                                                                                                                   | 우선순위  | 페이지           |
| ------------- | --------------------- | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------- | ---------------- | --- |
| 1. 회원 관리  | 1.1 회원 가입         | 1.1.1 이메일 입력               | @을 포함한 이메일 형식                                                                                                                                                                     | 매우 높음 | 회원 가입 페이지 |
|               |                       |                                 | 확인                                                                                                                                                                                       |           |                  |
|               | 1.2 로그인            | 1.2.1 이메일 입력               | 입력 받은 이메일 존재 확인                                                                                                                                                                 | 매우 높음 | 메인 홈 화면     |
|               |                       |                                 | (유저 정보 제공 X)                                                                                                                                                                         |           |                  |
|               | 1.3 비밀번호 찾기     | 1.3.1 이메일 입력 및 확인       | 가입시 Email 유저 확인                                                                                                                                                                     | 매우 높음 | PW 찾기 페이지   |
| 2. 마이페이지 | 2.1 회원 정보 수정    | 2.1.1 닉네임 변경               | 현재 닉네임 확인 후 업데이트                                                                                                                                                               | 높음      | 마이 페이지      |
|               | 2.2 회원 탈퇴         | 2.2.1 회원 탈퇴 처리            | db user.idDeleted → 1                                                                                                                                                                      | 매우 높음 | 마이 페이지      |
|               | 2.3 회원 정보 조회    | 2.3.1 닉네임, 이메일 조회       | 진입 시 백으로 요청                                                                                                                                                                        | 높음      | 마이 페이지      |
|               | 2.4 게임 전적 조회    | 2.4.1 이전 플레이 기록 조회     | 이전 플레이 기록 전송<br>페이지 마다 5개씩 표시<br>승패 여부, 내 역할, 날짜,<br>플레이 시간                                                                                                | 보통      | 마이 페이지      |     |
|               | 2.5 게임 전적 통계    | 2.5.1 유저 전적 통계 표시       | 백에서 계산한 데이터 전송<br>총 플레이 수, 총 승률,<br>역할 별 플레이 수, 역할 별<br>승률, 역할 별 능력 발동 횟수                                                                          | 낮음      | 마이 페이지      |
|               | 2.6 로그아웃          | 2.6.1 로그아웃                  | access token과 refresh tokken<br>삭제하여 로그아웃 처리                                                                                                                                    | 매우 높음 | 마이 페이지      |
| 3. 방 관리    | 3.1 방 만들기         | 3.1.1 방 제목 입력              | 16자 이하의 문자열 <br> 프론트에서 검증                                                                                                                                                    | 매우 높음 | 방 입장 전 로비  |
|               | 3.2 방 리스트 조회    | 3.2.1 현재 생성된 방 조회       | 모든 session 정보 요청<br> 현재 생성된 session 전달                                                                                                                                        | 매우 높음 | 방 입장 전 로비  |
|               | 3.3 방 정보 수정      | 3.3.1 사용 역할 설정            | 게임에서 제외할 역할 선택                                                                                                                                                                  | 낮음      | 게임 대기실      |
|               | 3.4 방 삭제           | 3.4.1 방 삭제                   | 방장이 나갔을 때 방 삭제                                                                                                                                                                   | 매우 높음 | 게임 대기실      |
|               | 3.5 게임 시작         | 3.5.1 방장이 게임 시작          | 게임 시작 및 직업 배정 요청                                                                                                                                                                | 매우 높음 | 게임 대기실      |
|               | 3.6 방 준비           | 3.6.1 방장 이외 인원 레디       |                                                                                                                                                                                            | 낮음      | 게임 대기실      |
|               | 3.7 방 빠른 입장      | 3.7.1 남는 방에 랜덤으로 입장   |                                                                                                                                                                                            | 매우 낮음 | 방 입장 전 로비  |
|               | 3.8 채팅 기능         | 3.8.1 대기실 인원끼리 채팅 가능 | 각 인원의 색상으로 채팅 <br>글자 색상 표시                                                                                                                                                 | 매우 높음 | 게임 대기실      |
|               | 3.9 도움말 기능       | 3.8.1 게임 룰 및 역할 설명      | 도움말 버튼 누르면 <br>설명창 표시                                                                                                                                                         | 낮음      | 게임 대기실      |
| 4. 게임       | 4.1 게임 입장 시 기능 | 4.1.1 유저 카메라 연결 확인     | 프론트에서 해결                                                                                                                                                                            | 높음      | 게임 진행 중     |
|               | 4.2 게임 진행 중 기능 | 4.2.1 영상 On/Off 기능(유저)    | 유저 임의로 영상 끄기                                                                                                                                                                      | 매우 높음 | 게임 진행 중     |
|               | 4.3 턴 기능           | 4.3.1 토론 턴                   | 타이머 90초 <br> 모든 유저 카메라/마이크/스피커 On                                                                                                                                         |           |                  |
|               |                       | 4.3.2 투표 턴                   | 타이머 15초 <br>유저 투표 가능 <br>모든 유저 카메라/마이크/스피커 On                                                                                                                       | 높음      | 게임 진행 중     |
|               |                       | 4.3.3 낮 → 밤 전환 턴           | 타이머 5초 <br>투표당한 유저 카메라/마이크/스피커 On<br>나머지 유저 모두 카메라/마이크/스피커 Off<ㅠㄱ>유저 처형 및 유령 생성 <br>처형 애니메이션 재생<br>처형 결과 및 능력 사용 결과 표시 | 매우 높음 | 게임 진행 중     |
|               |                       | 4.3.4 밤 턴                     | 타이머 60초<br>모든 유저 카메라 Off<br>자라만 마이크/스피커 On<br>나머지는 모두 Off<br>능력 사용할 수 있는 유저만 능력 사용                                                                | 매우 높음 | 게임 진행 중     |
|               |                       | 4.3.5 밤 → 낮 전환 턴           | 자라에게 살해된 유저 유령 생성<br>살해 애니메이션 재생<br>살해 결과 표시 후 능력 사용 결과 표시                                                                                            | 높음      | 게임 진행 중     |

### 📌API 명세서

#### HTTPS

| 대분류                                                                                                              | 주기능                                                                                            | Mapping                      | Data                                                                                                                                                                                                                                                                                                                    | Return                                                                                                             | 설명                                                                                                                                       |
| ------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------- | ---------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------ | --- |
| User                                                                                                                | 이메일 중복 검사, 인증번호 전송                                                                   | POST /users/email/confirm    | ?email=email@email.com                                                                                                                                                                                                                                                                                                  | 200 - 사용 가능<br>409 - 이메일 중복<br>422 - 이메일 형식 오류<br>429 - 재전송 대기 시간<br>502 - 이메일 전송 실패 | 이메일 형식 확인<br>이메일 중복 검사<br>인증번호 이메일 전송                                                                               |
|                                                                                                                     | 이후 이메일로 인증번호 전송                                                                       | POST /users/join             | {<br>&nbsp;&nbsp;email: string,<br>&nbsp;&nbsp;password: string,<br>&nbsp;&nbsp;nickname: string,<br>&nbsp;&nbsp;emailVerificationCode: string<br>}                                                                                                                                                                     |
| 201 - 회원가입 성공<br>400 - 이메일 인증 실패<br>409 - 중복된 이메일<br>422 - 이메일, 비밀번호, 닉네임 중 형식 오류 | 이메일 형식 확인<br>닉네임 형식 확인(10자 이하)<br>비밀번호 형식 확인(영어, 숫자 조합 6자리 이상) |
|                                                                                                                     | 이메일 인증번호 전송 필                                                                           | POST /users/email            | ?email=email@email.com                                                                                                                                                                                                                                                                                                  | 200 - 이메일 전송 성공<br>404 - 사용자를 찾을 수 없습니다<br>429 - 재전송 대기시간<br>502 - 이메일 전송 실패       | 영어 대/소문자, 숫자로 이루어진 8자리 인증번호 전송<br>전송 후 1분 동안 같은 이메일로 재전송 불가<br>인증번호는 전송 후 5분 동안 사용 가능 |
|                                                                                                                     | 비밀번호 초기화                                                                                   | POST /users/reset-password   | {<br>&nbsp;&nbsp;email: string,<br>&nbsp;&nbsp;password: string,<br>&nbsp;&nbsp;emailVerificationCode: string<br>}                                                                                                                                                                                                      | 200 - 초기화 성공<br>400 - 이메일 인증 실패<br>422 - 이메일, 비밀번호 형식 오류                                    | Email로 emailVerificationCode 발송하고 코드와 변경할 비밀번호 입력                                                                         |
|                                                                                                                     | 비밀번호 수정(프로필? 어디에?)                                                                    | PATCH /users/change-password | {<br>&nbsp;&nbsp;password: string,<br>&nbsp;&nbsp;newPassword: string<br>}                                                                                                                                                                                                                                              | 200 - 변경 성공<br>401 - 기존 비밀번호 오류<br>401 - 토큰 오류<br>404 - 사용자를 찾을 수 없습니다                  | 현재 비밀번호 확인<br>새로운 비밀번호 형식 확인<br>비밀번호 변경                                                                           |
|                                                                                                                     | 닉네임 수정                                                                                       | PATCH /users/change-nickname | {<br>&nbsp;&nbsp;nickname: string<br>}                                                                                                                                                                                                                                                                                  |                                                                                                                    | 닉네임 수정                                                                                                                                |
|                                                                                                                     | 회원탈퇴(프로필? 어디에?)                                                                         | DELETE /users                | {<br>&nbsp;&nbsp;password: string<br>}                                                                                                                                                                                                                                                                                  | 200 - 수정 성공<br>401 - 비밀번호 오류<br>401 - 토큰 오류<br>404 - 사용자를 찾을 수 없습니다                       | 비밀번호 확인<br>db user.idDeleted → 1<br>이메일, 비밀번호 삭제 처리<br>닉네임 → “탈퇴한 유저”<br>로그아웃 처리                            |
|                                                                                                                     | 내 정보                                                                                           | GET /users/me                |                                                                                                                                                                                                                                                                                                                         | 200 - 조회 성공<br>401 - 토큰 오류<br>404 - 사용자를 찾을 수 없습니다                                              | 로그인된 사용자의 정보 조회                                                                                                                |
|                                                                                                                     | 토큰 재발급                                                                                       | POST /users/refresh-token    | Cookie(refreshToken)                                                                                                                                                                                                                                                                                                    | 200 - 토큰 재발급 성공<br>401 - 토큰이 유효하지 않음<br>404 - 사용자를 찾을 수 없음                                |                                                                                                                                            |
| rooms                                                                                                               | 소켓 연결시                                                                                       |                              |                                                                                                                                                                                                                                                                                                                         |                                                                                                                    |                                                                                                                                            |     |
|                                                                                                                     | 방 만들기                                                                                         | POST                         | {<br>&nbsp;&nbsp;roomCode: string<br>}                                                                                                                                                                                                                                                                                  | 200 - 방 만들기 성공<br>401 - 토큰이 유효하지 않음<br>404 - 사용자를 찾을 수 없음                                  | 방 생성 시 초기 인원은 0으로 설정                                                                                                          |
|                                                                                                                     | 방 찾기(리스트 조회)                                                                              | GET                          |                                                                                                                                                                                                                                                                                                                         | 200 - 방 리스트 조회 성공<br>List<Room>                                                                            |                                                                                                                                            |
|                                                                                                                     | 방 삭제                                                                                           | DELETE                       | /{roomCode}                                                                                                                                                                                                                                                                                                             |                                                                                                                    | 받은 accessToken과 roomCode를 사용하여 해당 방의 owner가 맞으면 성공 아니면 거부                                                           |
|                                                                                                                     | 방 입장가능 조회                                                                                  | GET                          | /{roomCode}                                                                                                                                                                                                                                                                                                             | 200 - 입장 가능<br>404 - roomCode가 유효하지 않음<br>403 - 인원 수 초과                                            |                                                                                                                                            |
| point                                                                                                               | 포인트 조회                                                                                       | GET /point                   |                                                                                                                                                                                                                                                                                                                         | 200 - 포인트 조회 성공<br>{ "int" }                                                                                |                                                                                                                                            |
|                                                                                                                     | 게임에서 얻은 포인트 조회                                                                         | GET /point/{gameCode}        | {<br>&nbsp;&nbsp;lastPoint: 0,<br>&nbsp;&nbsp;currentPoint: 0<br>}                                                                                                                                                                                                                                                      | 200 - 포인트 조회 성공<br>404 - User Not Found<br>404 - Game Not Found                                             |                                                                                                                                            |
| shop                                                                                                                | 아이템 목록 조회                                                                                  | GET /shop/{itemType}         | {<br>&nbsp;&nbsp;itemSeq: 0,<br>&nbsp;&nbsp;price: 0,<br>&nbsp;&nbsp;image: "string",<br>&nbsp;&nbsp;sold: true<br>}                                                                                                                                                                                                    | 200 - 조회 성공                                                                                                    | sold - true → 구매한 물품                                                                                                                  |
|                                                                                                                     | 아이템 구매                                                                                       | POST /shop/buy               | {<br>&nbsp;&nbsp;items: [<br>&nbsp;&nbsp;&nbsp;&nbsp;itemSeq<br>&nbsp;&nbsp;&nbsp;&nbsp;...<br>&nbsp;&nbsp;]<br>}                                                                                                                                                                                                       | 200 - 구매 성공<br>403 - Insufficient Point<br>403 - Already Purchased<br>404 - 유저를 찾을 수 없습니다            |
|                                                                                                                     | 아이템 장착                                                                                       | PATCH /shop/equipped         | {<br>&nbsp;&nbsp;items: [<br>&nbsp;&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;itemSeq: 0,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;price: 0,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;image: "string",<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sold: true<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;]<br>} | 200 - 아이템 장착 성공<br>401 - 보유하지 않은 아이템<br>404 - 유저를 찾을 수 없습니다.                             | 장착할 아이템 전부 보내줘야 함 (type 중복되지 않게)                                                                                        |
| record                                                                                                              | 전체 승률                                                                                         | GET /record/winRate          |                                                                                                                                                                                                                                                                                                                         | 200 - 조회 성공<br>100<br>404 - 유저 정보가 존재하지 않습니다.                                                     |                                                                                                                                            |
|                                                                                                                     | 최근 10개 기록                                                                                    | GET /record/recent           | [<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;jobSeq: 3,<br>&nbsp;&nbsp;roomSeq: 7,<br>&nbsp;&nbsp;playAt: 2023-08-03T09:02:06,<br>&nbsp;&nbsp;endAt: 2023-08-03T10:02:06,<br>&nbsp;&nbsp;win: true<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;...<br>]                                                                                       | 200 - 조회 성공<br>404 - 유저 정보가 존재하지 않습니다                                                             |                                                                                                                                            |
|                                                                                                                     | 직업별 승률                                                                                       | GET /record/jobWinRate       | [0, 50, 100, 0, 100]                                                                                                                                                                                                                                                                                                    | 200 - 조회 성공<br>404 - 유저 정보가 존재하지 않습니다.                                                            |                                                                                                                                            |
| level                                                                                                               | 유저 레벨 정보                                                                                    | GET /level                   | {<br>&nbsp;&nbsp;level: 1,<br>&nbsp;&nbsp;exp: 0,<br>&nbsp;&nbsp;maxExp: 0<br>}                                                                                                                                                                                                                                         | 200 - 조회 성공                                                                                                    |                                                                                                                                            |
|                                                                                                                     | 게임에서 얻은 경험치 정보                                                                         | GET /level/{gameCode}        | {<br>&nbsp;&nbsp;lastExp: 0,<br>&nbsp;&nbsp;currentExp: 0,<br>&nbsp;&nbsp;maxExp: 0,<br>&nbsp;&nbsp;lastLevel: 0,<br>&nbsp;&nbsp;currentLevel: 0<br>}                                                                                                                                                                   | 200 - 조회 성공<br>404 - User Not Found<br>404 - Game Not Found                                                    |                                                                                                                                            |

#### WSS

**subscribe room**
| destination | action | data | 설명 |
|--------------------------------|---------------------------|-----------------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| /sub/room/${roomCode} | 방 세팅, 입장 | type: ROOM_ENTER_SETTING<br>roomCode: string<br>data: {<br>roomCode: string<br>title: string<br>...<br>} | 어떤 인원이 방입장시 해당 방을 구독한 모든 인원에게 모든 방 정보 뿌리기 |
| | 채팅 | type: ROOM_CHAT<br>roomCode: string<br>data: {<br>nickname: string<br>message: string<br>} | |
| | 퇴장 | type: ROOM_EXIT<br>roomCode: string<br>data: string | |
| | 방 세팅 | type: ROOM_TITLE<br>roomCode: string<br>data: string | 방장이 수정하여 pub 한 데이터에 type 추가하여 전달 |
| | 방 세팅 | type: ROOM_START<br>roomCode: string<br>data: string; | 방장이 게임을 시작한 경우, type 추가하여 pub 전달 |
| | 방 세팅 | type: ROOM_JOB_SETTING<br>roomCode: string<br>data: {<br>"1": boolean,<br>"2": boolean,<br>...<br>"7": boolean<br>} | 방장이 수정하여 pub 한 데이터 type 추가하여 전달 |
| | 방 세팅 | type: ROOM_CHANGE_OWNER<br>roomCode: string<br>data: number(ownerSeq) | 방장이 unsubscribe 한 경우<br>방 폭파시, -1 |
| | 게임 레디, 방 세팅 | type: ROOM_CUR_SEATS<br>roomCode: string<br>data: CureSeats | 방 인원이 퇴장 메세지를 pub 하면 남은 인원들에게 수정된 CUR_SEATS 전송 |
| | 방에서 복귀 | type: ROOM_COMEBACK_SETTING<br>roomCode: string<br>data : {<br>title: string<br>...<br>data: CureSeats<br>} | |

**publish room**
| destination | action | data | 설명 |
|--------------------------------------|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| /pub/room/${roomCode}/enter          | 입장          | 토큰 추가                                                                                                                                                 |                                |
| /pub/room/${roomCode}/chat | 채팅 | {<br>senderSeq: userSeq;<br>message: string<br>} | |
| /pub/room/${roomCode}/exit            | 퇴장          | 토큰 추가                                                                                                                                                 |                                |
| /pub/room/${roomCode}/title | 방 세팅 | {<br>title: string<br>} | 방 인원 모두에게 방 정보 전송 |
| /pub/room/${roomCode}/jobSetting      | 방 세팅       | {<br>jobSetting: {<br>"1": boolean,<br>"2": boolean,<br>...<br>"7": boolean<br>}             |                                |
| /pub/room/${roomCode}/start | 방 세팅 | | |
| /pub/room/${roomCode}/curSeats        | 게임 레디,방 세팅 | {<br>curSeats: CureSeats<br>} |                                |
| /pub/room/${roomCode}/comeBack | 방에서 복귀 | 토큰 추가 | |

**subscribe game**
| destination | action | data | 설명 |
|---------------------------------|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------|
| /sub/game/${gameCode}/all       | 게임 시작             | {<br>&nbsp;&nbsp;type: GAME_START,<br>&nbsp;&nbsp;gameCode: string,<br>&nbsp;&nbsp;data: [<br>&nbsp;&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;userSeq: 0,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;jobSeq: 3,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;nickname: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;equippedItems: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;face: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cap: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;clothing: string<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;equippedItemsGif: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;face: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cap: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;clothing: string<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;....<br>&nbsp;&nbsp;]<br>} | 해당 게임의 모든 유저가 구독하는 destination |
|                                 | 채팅                  | {<br>&nbsp;&nbsp;type: CHAT_ALL,<br>&nbsp;&nbsp;gameCode: string,<br>&nbsp;&nbsp;data: {<br>&nbsp;&nbsp;&nbsp;&nbsp;sender: number,<br>&nbsp;&nbsp;&nbsp;&nbsp;nickname: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;message: string<br>&nbsp;&nbsp;}<br>}                                      |                                            |
|                                 | 투표 중               | {<br>&nbsp;&nbsp;type: GAME_VOTE;<br>&nbsp;&nbsp;gameCode: string;<br>&nbsp;&nbsp;data: [<br>&nbsp;&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;userSeq: userSeq;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cnt: number;<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;]<br>} | userSeq 0번이면 무효표             |
|                                 | 투표 결과             | {<br>&nbsp;&nbsp;type: GAME_VOTE_RESULT;<br>&nbsp;&nbsp;gameCode: string;<br>&nbsp;&nbsp;data: {<br>&nbsp;&nbsp;&nbsp;&nbsp;userSeq: number,<br>&nbsp;&nbsp;&nbsp;&nbsp;politicianSeq: number<br>&nbsp;&nbsp;}<br>}|                                    |
|                                 | 타이머                | {<br>&nbsp;&nbsp;type: GAME_TIMER;<br>&nbsp;&nbsp;gameCode: string;<br>&nbsp;&nbsp;data: {<br>&nbsp;&nbsp;&nbsp;&nbsp;type: string,<br>&nbsp;&nbsp;&nbsp;&nbsp;time: int<br>&nbsp;&nbsp;}<br>}| 타이머 시작 공지<br>type: NONE, DAY, VOTE, NIGHT |
|                                 | 타이머 감소           | {<br>&nbsp;&nbsp;type: "GAME_TIMER_DECREASE",<br>&nbsp;&nbsp;code: string,<br>&nbsp;&nbsp;data: decreaseTime<br>}| 타이머 감소(10초)<br>userSeq별 한번씩만 가능 |
|                                 | 밤 시간 이후 생존 여부 | {<br>&nbsp;&nbsp;type: GAME_NIGHT_RESULT,<br>&nbsp;&nbsp;gameCode: string,<br>&nbsp;&nbsp;data: {<br>&nbsp;&nbsp;&nbsp;&nbsp;userSeq: number,<br>&nbsp;&nbsp;&nbsp;&nbsp;ability: [<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{userSeq: boolean},<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br>&nbsp;&nbsp;&nbsp;&nbsp;]<br>&nbsp;&nbsp;}<br>}| |
|                                 | 최종 게임 결과         | {<br>&nbsp;&nbsp;"type": "GAME_OVER",<br>&nbsp;&nbsp;"code": string,<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"rabbitWin": boolean,<br>&nbsp;&nbsp;&nbsp;&nbsp;"userInfo": [<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"userSeq": number,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"jobSeq": number,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"win": boolean,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"nickname": string,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;]<br>&nbsp;&nbsp;}<br>}|                                    |
|                                 | 캐릭터 위치            | {<br>&nbsp;&nbsp;"type": "GAME_CHAR_LOC",<br>&nbsp;&nbsp;"gameCode": number,<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"orderNo": 0,<br>&nbsp;&nbsp;&nbsp;&nbsp;"xAxis": Double,<br>&nbsp;&nbsp;&nbsp;&nbsp;"yAxis": Double<br>&nbsp;&nbsp;}<br>} | 우선순위 낮음                        |
|                                 | 화면 가리기            | {<br>&nbsp;&nbsp;"type": "GAME_BLACKOUT",<br>&nbsp;&nbsp;"code": "String",<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"userSeq": number,<br>&nbsp;&nbsp;&nbsp;&nbsp;"startSecond": number<br>&nbsp;&nbsp;}<br>}| 낮일때 userSeq 유저에게 startSecond 부터 암막 효과 부여 |
| /sub/game/${gameCode}/zara | 채팅 | {<br>&nbsp;&nbsp;"type": "CHAT_ZARA",<br>&nbsp;&nbsp;"gameCode": number,<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"sender": "userSeq",<br>&nbsp;&nbsp;&nbsp;&nbsp;"nickname": "string",<br>&nbsp;&nbsp;&nbsp;&nbsp;"message": "string"<br>&nbsp;&nbsp;}<br>} | |
| | 능력 사용 | {<br>&nbsp;&nbsp;"type": "ABILITY",<br>&nbsp;&nbsp;"gameCode": number,<br>&nbsp;&nbsp;"targetUserSeq": 2<br>}| |
| /sub/game/${gameCode}/ghost | 채팅 | {<br>&nbsp;&nbsp;"type": "CHAT_GHOST",<br>&nbsp;&nbsp;"gameCode": number,<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"sender": userSeq,<br>&nbsp;&nbsp;&nbsp;&nbsp;"nickname": string,<br>&nbsp;&nbsp;&nbsp;&nbsp;"message": string<br>&nbsp;&nbsp;}<br>}| |
| | 능력 사용 | {<br>&nbsp;&nbsp;"type": "ABILITY_GHOST",<br>&nbsp;&nbsp;"code": string,<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"userSeq": number,<br>&nbsp;&nbsp;&nbsp;&nbsp;"targetUserSeq": number<br>&nbsp;&nbsp;}<br>}| |

**publish game**
| destination | action | data | 설명 |
|-----------------------------------------|------------|-------------------------------------------------------------------|----------------------------|
| /pub/game/${gameCode}/chat/all          | 채팅       | {<br>sender: userSeq;<br>message: string;<br>}                     | 방 인원 모두에게 채팅 전송  |
| /pub/game/${gameCode}/chat/zara | 채팅 | {<br>sender: userSeq;<br>message: string;<br>} | 자라에게만 채팅 전송 |
| /pub/game/${gameCode}/chat/ghost         | 채팅       | {<br>sender: userSeq;<br>message: string;<br>}                     | 유령에게만 채팅 전송       |
| /pub/game/${gameCode}/vote | 투표 | {<br>userSeq: userSeq;<br>targetUserSeq: userSeq;<br>} | targetUserSeq: 0이면 투표 skip |
| /pub/game/${gameCode}/ability            | 능력 사용   | {<br>userSeq: userSeq;<br>targetUserSeq: userSeq;<br>}             |                            |
| /pub/game/${gameCode}/timer | 타이머 | {<br>userSeq: number,<br>decreaseTime: number<br>} | 타이머 종료시 서버에 알리기 |
| /pub/game/${gameCode}/timer/decrease     | 타이머 감소 | {<br>userSeq: number<br>}                                          |                            |
| /pub/game/${gameCode}/loc | 캐릭터 위치 | {<br>orderNumber: 1,<br>xAxis1: Double,<br>yAxis1: Double,<br>xAxis2: Double,<br>yAxis2: Double,<br>} | |

## 🔧설계

### 📌아키텍처 설계

![image (1)](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/fdaabc15-537f-49dc-97e1-759d8332dc6b)

### 📌DB 설계 (ERD)

![image-23](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/d5ab6c69-b29b-4abd-b9d0-a1962b90fcb0)

## 💻실제 화면

### 📌회원가입

![회원가입](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/5a78e29a-6c81-4a67-90ac-0697671fa0db)

### 📌로그인

![로그인](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/6e81432e-db9d-4d91-a58c-c8ba0184bd0c)

### 📌게임 설명

![게임설명](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/aebf4e6c-b2fb-4c45-8682-c36a6378831a)

### 📌방 생성

![방생성](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/b98554f5-07c2-4213-9fff-e022bbd71795)

### 📌방 입장

![방입장](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/dbd22e8e-2dd8-4db7-a518-986204eefaae)

### 📌방 설정 변경 및 채팅

![방안에서수정및채팅](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/c21d81ad-867e-4938-84d0-026d2e0c62a0)

### 📌게임 시작

![게임시작](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/5170d558-1363-4dfd-9cf7-896d0beb7326)

### 📌게임 중 채팅

![게임중채팅](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/014ac7fb-9041-4b1c-a97b-ec4eba6b5e9a)

### 📌캐릭터 이동

![캐릭터이동](https://github.com/hd9775/hd9775/assets/12166357/248e2803-b513-41c9-b858-036850460608)

### 📌투표

![투표](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/29af56ab-2ec7-40f0-a710-1ad661666f80)

### 📌투표 처형

![투표처형](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/c6dfc896-dfa1-4136-8e75-e55e4dd850fd)

### 📌밤에 직업에 따른 능력 사용

![밤능력사용](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/b5e9a354-c0de-4527-a188-ebe3f93707b9)

### 📌능력 사용 성공

![능력성공](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/685d76e3-d89a-480a-9e75-ce187ecacce8)

### 📌게임 결과 및 방 복귀

![게임결과](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/b6b270c7-64b9-4db7-b0d3-3c7332dba21e)

### 📌프로필에서 전적 확인

![프로필](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/491cfe4f-6941-475b-896e-a1b58848e92b)

### 📌옷장에서 아이템 구입 및 장착

![아이템구매](https://github.com/Jeongseulho/JWT-pjt/assets/110578739/c41b4c8d-1f12-4e08-82a4-f2f715c65b20)
