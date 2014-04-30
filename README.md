infoC
=====
# contact: anbv333@gmail.com
# license: contact me!

/////////////////////////////////////////////////////////////////////////

[TODO] - 나와 다른 사람들에게도 도움이 되는 기능

* 검색결과에 날자를 포함해서 리턴하고 검색은 어제 기사뿐만 아니라 전체 기사에서 검색

* 특정 기사에 대한 외신반응을 볼수 있는 사이트로 홍보하자

* 맨끝에 컨택 표시 넣기(? 표시), 맨 마지막엔 빼기..ㅋ

* 검색할때 어제 기사마자도 결과가 없으면 더이상 검색안됨 
  => 전체 DB에서 뒤져서 해당 날자와 함께 출력하도록 수정..ㅜㅜ
  
* 류현진, 추신수 기사를 링크로 볼수 있도록 개선

* http://www.bootply.com/render/102931 같은 상단에 간단 설명을 넣을 수 있는 메뉴는 어떨까? ㅋㅋ

* DB 저장과 조회를 분리와 중복 저장 방지 필요

* 그리고 오픈..ㅋㅋ



  
* 회원 가입 추가 => favorite 버튼 => 중요도에 반영 => 내 기사 보기 => 유사 기사 보기
  
  1. 일단계는 이메일만 받고 이메일을 암호화해서 파라미터로 가지고 다니면서 ADD 기능에 사용하고
      개인 페이지에서는 ADD한 기사를 시간순으로 출력~ 

  2. 로그인 폼 암호화: http://www.jcryption.org
  
  3. 관련기사 버튼을 빼로 저장 버튼을 넣는다. 관련기사는 무조건 밑에 추가한다.

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
