# HaechiLabs-Assignment-demo

## Environment

> Language : JAVA (JDK 14)<br/>
> Framework : Spring Boot 2.3.4.RELEASED <br/>
> IDE : IntelliJ Ultimate<br/>
> OS : Window 10<br/>

<br/>

## Structure
  ```
  src
  ┗━ main/java           
    ┗━ com.example.demo          
        ┗━ client                       # 데이터 가공
            ┗━dto                       # Data Transfer Object    
        ┗━ configuration                # RestTemplate Build
        ┗━ controller                   # service 호출 및 Endpoint 경로설정
        ┗━ domain                       # repository에 대한 추상화 지원
        ┗━ repository                   # 데이터베이스에 접근
        ┗━ service                      # 비즈니스 로직 처리
  ```
<br/>

## 과정
- 10월 16일 (금)<br>

- 10월 15일 (목)<br> 
응답 받은 JSON 데이터 파일을 Jackson 라이브러리를 활용해 변환하는 과정을 시도했습니다.<br>
아래 Endpoint로 입출금 관련 데이터를 JSON 형식으로 추출했습니다.<br>
    - Deposit Endpoint<br>
    http://localhost:8080/notifications/deposit
        ```
        {"deposit":[{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x6a94d74f430000","wallet_id":"65da4adb8c921685304b709db180cc62","ticker":"ETH","tx_hash":"0x0c0ff4769957fc36e33e921557e73a1bd82a1c54efb9c52f349e8843736891b6","to_address":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","type":"DEPOSIT","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"}]}
        ```
    - Withdrawal Endpoint<br>
    http://localhost:8080/notifications/withdrawal
        ```
        {"withdrawal":[{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"},{"amount":"0x4e28e2290f0000","wallet_id":"346f6920f8059fe5e7614f6c46bd5cbb","ticker":"ETH","tx_hash":"0xa382a4083312740680eb173d415a17ba1ac689c3a6fd6ce3364cb79813f8cbeb","to_address":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","type":"WITHDRAWAL","from_address":"0x4d41332e31a57d14899a3890b665710076a298d4"}]}
        ```
- 10월 14일 (수)<br> 
Spring Boot를 활용한 Microservice 개발관련 서적을 통해 간단한 프로젝트를 따라했습니다.<br>
Youtube 와 구글링을 통해 Spring boot 구조와 코드에 적응하려고 노력했습니다.<br><br>

- 10월 13일 (화)<br> 
Wallet 에서 거래 시 변경되는 JSON Field 를 Postman을 이용하여 분석하고 <br>
Spring 에 대한 구조와 디자인패턴에 대한 이해를 목표로 공부했습니다.<br><br>

- 10월 12일 (월)<br>
Henesis Wallet의 Transaction Mechanism과 API Architecture를 분석했습니다.<br>
JAVA 기초문법 복습을 시작했습니다.<br><br>

## 스케치
```
JPA 장점
객체 지향적인 코드 
> 직관적이고 비즈니스 로직에 집중을 도와줌
> 전체 프로그램 구조를 일관성있게 유지 -> 코드의 가독성 증가
> SQL을 직접적으로 작성하지 않아 유지보수, 재사용성 편리

JPA 단점
복잡한 로직의 경우 JPA로 처리하기 어렵다. (통계 처리 등)
데이터 베이스 중심으로 되어 있는 환경에서는 실제 SQL로 튜닝해야하기 때문에 성능 저하


이벤트 중심 아키텍쳐 설계 필요
거래 상태에 따라 발생시킬 이벤트, 비즈니스 로직이 다름.


기존 API 에 응답을 받아 새로은 REST API 를 구현해야하므로
마이크로서비스에서 주로 사용하는 구조를 선택
서비스 간 상호의존성을 최대한 낮추는 방안 모색
> 데이터 전달 객체(DTO) 기반 모델을 단순히 복사해서 공유하는 방식 채택
    장점 :  
    API 소비자가 많을 때 개발 시간 절약
    
    단점 :  
    API 버전 별로 DTO 를 관리해야함
    DTO 구조를 변경하면 JSON을 역직렬화 할 수 없음.
    > 원하는 필드만 역 직렬화 하는 방식으로 설계 필요

웹서비스가 노출할 자원 식별
식별한 자원에 대한 URI 지정
각 자원에 수행할 수 있는 연산 식별
식별한 연산에 대한 HTTP 메서드 맵핑
```

## 문제점
짧은 기간 내 Spring Boot Framework 적응의 문제
- 섣부른 응용 문제<br>
    급하게 서적과 Youtube 를 통해 배운 Design Pattern 을 적용하려 했으나<br> 
    배움과 경험 부족으로 인해 실패했습니다.<br>
    
- Version에 따른 문제<br>
    중구난방한 정보수집으로 인해 Version 마다 다른 문법 혼동  
    Dependency 문제가 진행속도에 악영향<br><br>

## 느낀점
 새로운 지식을 얻고, 새로운 분야에 도전하는 것은 두렵기도 하지만, 막상 뛰어들면 또 즐겁고 승부욕이 생긴다는 점을 다시한번 느꼈습니다. 아직 스스로 공부해야할 것이 산더미지만 스프링 부트란 프레임 워크만의 매력을 알게되었고, Type Language에 대한 공부의 필요성 또한 느꼈습니다. <br>
 아직 적응이 필요한 문법과 구조지만 점차 눈에 익으니까 도전 정신이 더 불타올랐고, 실무에선 어떤 방식으로 설계하며 공부할까 라는 궁금증과 함께 해치랩스에서 조언과 토론을 통한 공부를 하며 꾸준한 트레이닝을 하면 정말 좋을 것 같다는 생각도 들었습니다. <br>

기초부터 순차적이고 체계적인 학습을 시도했다면 더 좋은 결과를 냈을 것이라는 아쉬움도 있지만,<br>
이번 기회에 어렴풋 알고 있었던 블록체인에 대해 공부해볼 수 있었으며, 새로운 프레임워크에 대한 경험과 과거에 배웠던 JAVA 복습도 함께 할 수 있었던, 짧지만 매우 유익한 시간이었습니다. 감사합니다. <br>

<br>

## 참고문헌

    (YouTube) Spring Boot Tutorials (Telusko)
    처음 배우는 스프링부트2 (김영재 저 | 한빛미디어)<br>
    배워서 바로 쓰는 스프링 프레임워크 (애시시 사린, 제이 샤르마 저/오현석 역 | 한빛미디어)<br>
    스프링 부트를 활용한 마이크로서비스 개발 (모이세스 메이세로 저/한동호 역 | 위키북스) <br>
<br><br>