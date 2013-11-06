infoC
=====

/////////////////////////////////////////////////////////////////////////
[Hot TODO]

기사 분류 추가 => 종합, IT, 경제, 스포츠, 연예, 쇼핑(?)

구글 크롤러 추가

중복이면 더 긴 기사로 교체

시간당 기사수 제한은 제거하자
 
컨텐츠를 html상태로 내부를 가져와서 <,>로 다시 구분해보자..

도메인 신청


/////////////////////////////////////////////////////////////////////////

[TODO]
1. 150자 정도로 줄이기...
1.1 핵심 문장 찾기 (최대 3개)

- 제목을 기반으로 keyword 리스트를 만들고 가장 핵심 문장을 선택한다.
==> 매칭 카운트로 정렬후 최고 3개를 뽑고 이걸 다시 인덱스 순으로 정렬해서 저장
* ====> CollectionService에 구현한다. *


- 비슷한 기사가 들어오면? 핵심 문장 업데이트를 하는가 ?? 
==> keyword 리스트를 업데이트하고 다시 핵심 문장을 선정한다. 
==> 본문의 문장 리스트가 긴쪽에서 핵심 문장을 선정한다......(?)


- 이 문장을 단어 배열에 추가하고 2번째 유사한 문장을 찾는다.
- 위를 반복하여 핵심 문장 2-3을 출력한다. 150~200자 기준으로

- 핵심 문장의 인덱스와 중복 키워드의 숫자를 각 아티클에 저장한다.
-- *새 아티클*은 타이틀과 본문만으로..(문장이 3개 이하면 이하대로 3상이면 높은순으로)
-- *중복 아티클*은 키워드 리스트와 중복 본문을 분석에서 핵심문장 맵에 추가한다.

1.2 keyword list와 본문 비교 방법
-- 매칭 카운트로 정렬후 최고 3개를 뽑고 이걸 다시 인덱스 순으로 정렬해서 출력


1.3 전체 본문 찾기
- tag 사이 문자를 하나의 문자열로 저장 (단 br은 무시)
- 가장 긴 문자열이 본문이다.
- 조선일보, 세계일보 등 몇몇만 가능
- http://painone7.tistory.com/57



2. 위 레이아웃에 맞쳐 그림 기사 출력(2칸짜리)


3. 각 기사별 확대 버튼 추가


4. web tts


5. 수동 기사 편집(DB 연동)



[DONE]
1. 시간, 분류 단위 기사 출력
1.0 시간대별로 기사 분류해 출력
1.1 현재 시간기준으로 24시간만 표시...


2. 중복 기사 제거: 여러 기사 소스에서 중복없이 주요뉴스만 선택 방법 
- 중복 여부 선택: 형태소 분석 및 사전 추가는 당장 어렵다.. 단어를 그냥 비교해서 몇글자가 같은지를 보고 유사도를 판단

2.1 비교 로직 작성
- 제목 split by ( , ", \,, ', ., [, ], ·, ...) 
- 제목의 단어끼리 비교해서 한단어라도 같으면 제외 => 중복 count++
- 중복 count 높은 순으로 정렬해 출력 (?)

2.2 제목 비교


2.3 내용 비교

==> 바쁜 세상에 같은 기사가 뿐만 아니라 비슷한 기사도 다 필요없다. => 키워드 몇개만 비슷하면 다 제외 




[기본 개발 진행 방법] 
for(각 page : 전체 web page) {

	1. UX/UI 껍데기 만들고 필요한 로직 설계 
	
    2. service + domain class 구현 => 비지니스 로직 고려 with dummy data, Service class UnitTest 작성

    3. domain + repo + dao 구현 => DB와 연동 확인, Domain, Repo class UnitTest 작성

    4. 리펙토링 => 공통/중복 로직은 common class로~
    
}
