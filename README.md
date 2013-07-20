infoC
=====

[TODO]
1. 시간, 분류 단위 기사 출력
1.0 시간대별로 기사 분류해 출력
1.1 그림이 있으면 제목과 그림만 출력
1.2 그림이 없으면 제목과 150자 출력


2. 중복 기사 제거: 여러 기사 소스에서 중복없이 주요뉴스만 선택 방법 
- 중복 여부 선택: 형태소 분석 및 사전 추가는 당장 어렵다.. 단어를 그냥 비교해서 몇글자가 같은지를 보고 유사도를 판단





[기본 개발 진행 방법] 
for(각 page : 전체 web page) {

	1. UX/UI 껍데기 만들고 필요한 로직 설계 
	
    2. service + domain class 구현 => 비지니스 로직 고려 with dummy data, Service class UnitTest 작성

    3. domain + repo + dao 구현 => DB와 연동 확인, Domain, Repo class UnitTest 작성

    4. 리펙토링 => 공통/중복 로직은 common class로~
    
}
