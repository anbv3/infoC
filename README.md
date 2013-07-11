infoC
=====

TODO
1. 시간, 분류 단위 기사 출력
1.1 그림이 있으면 제목과 그림만 출력
1.2 그림이 없으면 제목과 150자 출력

* 여러 기사 소스에서 중복없이 주요뉴스만 선택 방법 




for(각 page : 전체 web page) {

    1. page(jsp) + controller 껍데기 구현 => 브라우저와 서버 사이의 데이터 송수신 확인

    2. service + domain class 구현 => 비지니스 로직 고려 with dummy data, Service class UnitTest 작성

    3. domain + repo + dao 구현 => DB와 연동 확인, Domain, Repo class UnitTest 작성

    4. 리펙토링 => 공통/중복 로직은 common class로~
    
}
