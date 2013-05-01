infoC
=====
provides the info for U.


for(각 page : 전체 web page) {

    1. page(jsp) + controller 껍데기 구현 => 브라우저와 서버 사이의 데이터 송수신 확인

    2. service + domain class 구현 => 비지니스 로직 고려 with dummy data, Service class UnitTest 작성

    3. domain + repo + dao 구현 => DB와 연동 확인, Domain, Repo class UnitTest 작성

    4. 리펙토링 => 공통/중복 로직은 common class로~
    
}
