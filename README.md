# 뉴스 기사 정리, 요약, 번역 서비스  

#### 심심풀이용으로 만들다 다른 사람들에게도 좀 도움이 되었으면 해서 계속 개발중임..ㅋ  


 
[contact]
```
anbv333@gmail.com
```

[Build]
```
mvn -Denv=release -Dmaven.test.skip=true clean package war:exploded
```

[DB]

```
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci

# memory for instance
max_connections         = 100
query_cache_size        = 16M
query_cache_limit       = 1M
max_allowed_packet      = 16M
thread_cache_size       = 30
table_open_cache        = 64
thread_concurrency      = 8

# innodb
default-storage-engine          = InnoDB
innodb_file_format              = barracuda
innodb_large_prefix             = on
innodb_buffer_pool_size         = 64M
innodb_additional_mem_pool_size = 10M
innodb_log_buffer_size          = 8M
innodb_log_file_size            = 8M

[mysql]
default-character-set = utf8mb4

```


/////////////////////////////////////////////////////////////////////////

## TODO List

* ‘카드 스택(Card Stack)’ 및 ‘스토리 스트림(Story Stream)’ 도입 from http://www.vox.com/
  - 기존 기사를 요약해서 카드를 만든 후 일자 별로 연결
  - 사용자가 카드(키워드)를 등록하면 그에 맞는 기사를 일자별로 연결해 보여준다  
  - 독자가 원하는 건 단순한 기사의 연결이 아니라, 효율적으로 맥락을 이해  
  - timeline: http://www.simile-widgets.org/timeline/
  
  
* 요약 기능 개선 => 문장별 점수계산법 개선 corePt = (title * 1.5 + LDA * 2.0 + length * 0.5 + position * 1.0) / 4.0  


  
* 원문을 보여주고 medium 처럼 문장마다 사이드에 커멘트를 추가하자  
  http://aroc.github.io/side-comments-demo/

  
* 회원 가입 추가 => favorite 버튼 => 중요도에 반영 => 내 기사 보기 => 유사 기사 보기
  
  1. facebook, google 로그인  
  http://projects.spring.io/spring-social/core.html  
  http://maven-repository.com/artifact/org.springframework.social/spring-social-google/1.0.0.RELEASE  
  
  3. 관련기사 버튼을 빼고 favorite 저장 버튼을 넣는다. 관련기사는 무조건 밑에 추가한다.



* 버지 기사 추가: http://www.theverge.com/rss/index.xml 



* 뉴스 섹션별 주요 내용 필요 - 메인 => 주식, 날씨, 경제 => 결제 지표, 사회 => 날씨, 기타..
기사 분류별 대쉬영역

    1. 주요: top news, 경제지표, 날씨, 

    2. 사회: 환경문제, 종교문제, 가족문제, 주택

    3. 정치: 새누리, 민주당,, 말말말

    4. 문화생활: 도서순위, 전시회, 음학회 순위, 공연순위

    5. 연예: 드라마시청율, 음반순위, 영화순위, 광고순위

    6. 스포츠: top news



* 나를 위한 기능: contact 추가
* 해외 뉴스: http://www.mediagaon.or.kr/jsp/mlink/medialink_08.jsp



* 번역 기능 추가
- http://translate.google.co.kr/#ko/en/%EB%8D%B0%EC%9D%BC%EB%A6%AC%EC%95%88
- html: #result_box 파싱
- 하지만 유료.. 150자에 3원정도 => 동접이 많이지면 돈 많이 나가므로 캐싱해두자..ㅋㅋ

중요도 지표 추가 및 좋아요 버튼 추가
- 테두리색 변경?, 유사 기사 리스트 출력?
- 유사 리스트는 전체 시간대로 비교해서 유사 기사가 다른 시간대에 안 나오도록
- 언어별 번역 버튼 추가?
- 중요도에 따라 제목 크기 변경?




사용자별 기능 추가
- my favorite기사 보기
- 시간대별 기사 정리 보기
- 원하는 장소, 방법으로 보내주기



형태소 분석 => https://bitbucket.org/bibreen/mecab-ko

경쟁자 분석: Umano, newsblur, LinkedIn Pulse, 한국거는 다 허접

/////////////////////////////////////////////////////////////////////////

[요약 방법]

1. 150자 정도로 줄이기...

* 핵심 문장 찾기 (최대 3개)
  - 제목을 기반으로 keyword 리스트를 만들고 가장 핵심 문장을 선택한다.
  - 매칭 카운트로 정렬후 최고 3개를 뽑고 이걸 다시 인덱스 순으로 정렬해서 저장

* 비슷한 기사가 들어오면?  
  - keyword 리스트를 업데이트하고 다시 핵심 문장을 선정한다. 
  - 단, 얼마나 도움될지 몰라 일단 keyword 리스트만 업데이트하고 핵심 문장 재선정은 skip

* 핵심 문장의 인덱스와 중복 키워드의 숫자를 각 아티클에 저장한다.
  -  *새 아티클*은 타이틀과 본문만으로..(문장이 3개 이하면 이하대로 3상이면 높은순으로)
  -  *중복 아티클*은 키워드 리스트와 중복 본문을 분석에서 핵심문장 맵에 추가한다.

* keyword 리스트 업데이트
  - 제목에서 본문과 매치되지 않는 keyword는 제거?
  - 비슷한 기사인 경우 keyword 추가?

* rss링크에서 본문을 정확히 파싱하는게 젤 어렵다..ㅎㄷㄷㄷ

2. web tts
3. 수동 기사 편집(DB 연동)




[DONE]

1. 시간, 분류 단위 기사 출력

- 시간대별로 기사 분류해 출력
- 현재 시간기준으로 24시간만 표시...


2. 중복 기사 제거: 여러 기사 소스에서 중복없이 주요뉴스만 선택 방법

- 중복 여부 선택: 형태소 분석 및 사전 추가는 당장 어렵다.. 단어를 그냥 비교해서 몇글자가 같은지를 보고 유사도를 판단


3. 비교 로직 작성

- 제목 split by ( , ", \,, ', ., [, ], ·, ...) 
- 제목의 단어끼리 비교해서 한단어라도 같으면 제외 => 중복 count++
- 중복 count 높은 순으로 정렬해 출력 (?)


4. 제목 비교


5. 내용 비교
==> 바쁜 세상에 같은 기사가 뿐만 아니라 비슷한 기사도 다 필요없다. => 키워드 몇개만 비슷하면 다 제외 



[기본 개발 진행 방법] 

for(각 page : 전체 web page) {

    1. UX/UI 껍데기 만들고 필요한 로직 설계 
	
    2. service + domain class 구현 => 비지니스 로직 고려 with dummy data, Service class UnitTest 작성

    3. domain + repo + dao 구현 => DB와 연동 확인, Domain, Repo class UnitTest 작성

    4. 리펙토링 => 공통/중복 로직은 common class로~
    
}


