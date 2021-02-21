# REST API

## 1.회원

```jsx
1. 회원가입 POST
	● request: id ,name, password 
  ○ response: auth_token(가입완료시 자동로그인 되도록)

2. 로그인 POST
	● request : id, password(User json에 넣어주세요)
	○ response : auth_token

3. 회원정보 수정 진입 페이지 비밀번호 입력 POST
	● request : password
	○ response : 결과(성공/실패)

4. 회원정보 수정 페이지 PUT
	● request : password, name, thumbnail_path
	○ response : 결과(성공/실패)

5. 회원탈퇴 DELETE
  ● request : auth_token
	○ response : 결과(성공/실패)

6. 이메일 중복검사 GET
	 ● request : id
   ○ response : 인증번호(validNum)

7. 네임 중복검사 GET
	 **● request : name
   ○ response : 결과(성공/실패)

8. Social 로그인 POST
	 ● request : id, name, thumbnail_path, platform_type 
   ○ response : auth_token

9. Kakao 회원가입 POST
	 ● request : id, name, thumbnail_path, platform_type 
   ○ response : 결과(성공/실패)
```

## 2.마이페이지

```jsx

1. 내가 구독한 큐레이터 GET
	● request : auth_token ( ID )
	○ response : name, thumbnail_path

2. 스크랩한 기사 ( 기사 저장 ) GET
	● request : auth_token ( ID )
	○ response : title, image_path

3. 게시판 ( 커뮤니티 ) GET
	● request : auth_token ( ID )
	○ response : title, content, data, visit_cnt, is_notice

4. 사이드바용 유저 정보 GET
	● request : id
  ○ response : name, thumbnail_path
```

## 3.검색

```jsx
1. 사람 검색 GET
	 ● request : keyword
   ○ response : user list
		"/search/people/{keyword}"
2. 태그 검색 GET
	 ● request : keyword
   ○ response : hashtag list
		"/search/tag/{keyword}"
3. 검색어 자동완성 (태그 키워드 선택시) GET
	 ● request : keyword
   ○ response : hashtag list

4. 검색어 자동완성 (사람 키워드 선택시) GET
	 ● request : keyword
   ○ response : user list
```

## 4.게시판

```jsx
1. 게시글 생성 POST
	● request : user_board_post
	○ response : 결과(성공/실패)

2. 게시글 목록 조회 GET
	● request : boardNo (해당 게시판 번호)
	○ response : user_board_post list (게시글 리스트)

3. 게시글 수정 PUT
	● request : user_board_post
	○ response : 결과(성공/실패)

4. 게시글 삭제 DELETE
  ● request : boardNo(게시글 번호)
	○ response : 결과(성공/실패)

5. 댓글 생성 POST
  ● request : user_board_comment
	○ response : 결과(성공/실패)

6. 댓글 조회 GET
  ● request : boardPostNo(게시글 번호)
	○ response : user_board_comment list

7. 댓글 수정 PUT
  ● request : user_board_comment
	○ response : 결과(성공/실패)

8. 댓글 삭제 DELETE
  ● request : commentNo (댓글 번호)
	○ response : 결과(성공/실패)

9. 게시글 조회수 증가 PUT
	● request :  boardNo (게시글 번호)
	○ response : 없음
```

## 5.메인페이지

```jsx
1. 내가 구독한 큐레이터 GET - UserController - 내가 구독한 큐레이터
	● request : id
	○ response : 팔로우한 큐레이터들의 {id, name, thumbnail_path} 객체 리스트

   1-1. 큐레이터의 최신 스크랩 기사 GET - NewsController -> 내가 구독한 큐레이터의 최신 스크랩 목록
		● request : followUserNo
		○ response : newsNo, title, image_path

2. 추천 기사 GET
   2-1. 중복 가능
		● request : userNo
		○ response : newsNo, title, image_path, scrapNo

   2-2. 중복 불가능
		● request : userNo
		○ response : newsNo, title, image_path
```

## 6.기사

```jsx
1. 뉴스기사 GET
	 ● request : newsNo
   ○ response : user_scrap_news, news

2-1. 좋아요/싫어요 cnt 수정 PUT
	 ● request : userNo, scrapNo, 좋아요/싫어요 여부
   ○ response : 결과(성공/실패)

2-2. 좋아요/싫어요 선택여부 파악 GET
	 ● request : newsNo, userNo
   ○ response : is_like, is_dislike, is_save  (boolean)

2-3 좋아요/싫어요 boolean 수정 PUT
	 ● request : scrapNo, 좋아요/싫어요 여부
   ○ response : 결과(성공/실패)

3. 뉴스기사 url 저장 POST
	 ● request : 객체 배열(객체 : url, summary, tags, id, name )
   ○ response : 결과(성공/실패)
```

## 요청사항-재성-0210

```jsx
~~1. 내정보 비밀번호 수정~~
	● request: id, old_password, newpassword
	○ response: 결과(성공/실패)

~~2. 내정보 이름 수정~~
	● request: id, old_name, new_name
	○ response: 결과(성공/실패)

~~3. 내정보 태그 리스트 받아오기~~
	● request: id
	○ response: name(hashtag) 리스트

~~4. 내정보 태그 리스트 수정~~
	● request: id, hashtags(리스트)
	○ response: 결과(성공/실패) -> name(hashtag) 리스트 반환 ( 수정사항 바로 보이게 )

5. ~~채널 페이지 포스트번호로 기사 리스트 받아오기~~
	 ● request : postNo(user_scrap_news)
   ○ response : news 리스트 { *이거빼먹었네요 newsNo(news), title(news), curator_summary(user_scrap_news), like_cnt(user_scrap_news), dislike_cnt(user_scrap_news), article_last_date(news), image_path(news), ~~is_like(user_good_news), is_dislike(user_good_news), is_save(user_good_news)~~ }

----검색페이지 axios (13:36 추가) ---
6. ~~해쉬태그로 기사 리스트 받아오기~~
	 ● request : hashtag(해쉬태그이름)
   ○ response : news 리스트 { newsNo(news), title(news), curator_summary(user_scrap_news), like_cnt(user_scrap_news), dislike_cnt(user_scrap_news), article_last_date(news), image_path(news), ~~islike(user_good_news), isdislike(user_good_news), is_save(user_good_news)~~ }

7. ~~큐레이터 아이디로 기사 리스트 받아오기~~
	 ● request : id
   ○ response : news 리스트 { newsNo(news), title(news), curator_summary(user_scrap_news), like_cnt(user_scrap_news), dislike_cnt(user_scrap_news), article_last_date(news), image_path(news), ~~islike(user_good_news), isdislike(user_good_news), is_save(user_good_news)~~ }
```

## 요청사항 - 창민

```jsx
~~1. Social 로그인 POST
*~~*	 ● request : id, name, thumbnail_path, platform_type 
   ○ response : auth_token
    -> 소셜 로그인 진행시 만약 유저 정보가 저장되어 있지 않다면 자동 회원가입을 
       진행시켜주시고 auth_token을 내려주실 수 있을까요?
~~2. 게시글 생성 POST~~
	● request : user_board_post
	~~~~○ response : 결과(성공/실패)
    -> 이미지 업데이트는 어떤식으로 진행할까요?
           1. 이미지 등록할 때마다 이미지만 바로바로 업로드
           2. 이미지 관련 파일을 모아두었다가 게시글 생성할 때 한번에 업로드
    -> user_board_post 데이터를 어떤것을 넣어서 보내야 할까요? 
           1. userno를 구하려면 axios를 한번 더 해야할 것 같아요...
           2. boardNo, title, content, userNo만 보내면 될까요?
			     3. boardNo는 어디서 구하나요?

~~3. 댓글 조회 GET~~
  ● request : boardPostNo(게시글 번호)
	○ response : user_board_comment list
   -> 현재 스프링에 /boardDetail로 되어 있는데 /boardCommentList로 변경해주실 수 있을까요?

~~4. 댓글 생성 POST~~
  ● request : user_board_comment
	○ response : user_board_comment
   -> 성공하면 저장한 정보를 내려주실 수 있을까요?

추가 요청사항
1. ~~Social 로그인 POST~~
  -> 현재 아이디가 존재하지 않는다면 insert하고 있다면 insert하지 않는 것까지 잘 되는데
     그 하단에 User loginUser = service.login(user); 부분에서 password에 null값이 들어가서 검색이 되지 않아서 로그인에 실패합니다.
     이 부분에서 해결이 필요할 것 같습니다.

저번에 password가 안넘어온다고 했었죠..
소셜로그인은 id랑 password가 아니라 id랑 name으로 검사해서 로그인 되는걸로 바꿔놨습니다

2. ~~뉴스기사 url 저장 POST~~
	 ● request : 객체 배열(객체 : url, summary, tags, id, name )
   ○ response : postNo
   -> 기사 생성 후 url을 만들어서 링크를 공유하려고 합니다. 
      url에 필요한 부분이 postNo라서 response로 보내주실 수 있을까요?

~~3. Social 로그인 POST~~
	 ● request : id, name, thumbnail_path, platform_type 
   ○ response : auth_token
   -> 이번엔 소셜로그인으로 로그인 했을 경우, user_board에 게시판이 생성이 되지 않네요...
   -> 이부분 수정 부탁드립니다.

~~4. 댓글 생성 POST~~
  ● request : user_board_comment
	○ response : 결과(성공/실패)
   -> requestParam으로 받고 있는데, Post는 requestBody로만 받을 수 있는 것 같습니다.
   -> 수정 부탁드려요

~~5. 유저 정보 Get~~
  ● request : userNo
	○ response : 비밀번호를 제외한 userInfo

6. 비밀번호 변경 Post or Put
  ● request : id, password
	○ response : 결과(성공/실패)
   -> 비밀번호를 잊었을 때, 자신의 이메일로 인증하고, 인증번호를 입력한 뒤, 비밀번호를 변경할 수 있게 하려고 합니다.
   -> 해당 id와 변경할 비밀번호로 비밀번호를 바꿀 수 있게 해주시면 감사합니다!

```