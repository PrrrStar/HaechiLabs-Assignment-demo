# HaechiLabs-Assignment-demo

## Environment

> Language      : JAVA (JDK 14)<br/>
> Framework     : Spring Boot 2.3.4.RELEASED <br/>
> BuildTool     : Maven<br/>
> Messaging     : RabbitMQ<br/>
> ORM           : JpaRepository<br/>
> IDE           : IntelliJ<br/>
> OS            : Window 10<br/>

<br/>

## Structure
  ```
  src
  ┗━ main/java           
    ┗━ com.example.demo          
        ┗━ client                           # JSON 데이터 가공 (Jackson)
            ┗━ dto                              # 입출금 데이터 DTO
                ┗━ TransferEventResultDTO            # Nested Class 로 구성됨
            ┗━ TransferEventClient              # 입출금 데이터 가공 인터페이스
            ┗━ TransferEventClientImpl          # 입출금 데이터 가공 구현체

        ┗━ configuration                    # 각종 설정들
            ┗━ QuerydslConfiguration (예정)       # Querydsl 설정파일
            ┗━ RestClientConfiguration           # RestTemplate Builder
            ┗━ RabbitMQConfiguration             # RabbitMQ 설정파일

        ┗━ controller                       # service 호출 및 Endpoint 경로설정
            ┗━ NotificationController            # 알림과 관련된 서비스 호출 및 앤드포인트 경로 설정

        ┗━ domain                           # repository에 대한 추상화 지원
            ┗━ DomainConfirmed                   # 알림관련 Entity 입니다.
            ┗━ DomainMined
            ┗━ DomainReorged
            ┗━ WithdrawConfirmed
            ┗━ WithdrawPending

        ┗━ event                            # 메세징, 요청, 응답 이벤트
            ┗━ EventDispatcher                  # 메세징 보내는 로직 처리
            ┗━ EventHandler                     # 메세징 받는 로직 처리
            ┗━ ResponseDepositMinedEvent        # Deposit Mined 응답
            ┗━ ResponseWithdrawPendingEvent     # Withdraw Pending 응답

        ┗━ repository                       # JPA Repository 입니다.
            ┗━ DepositConfirmedRepository       # Id 로 조회합니다.
            ┗━ DepositMinedRepository           # Id, Transaction Hash 로 조회할 수 있습니다.
            ┗━ DepositReorgedRepository         # Id 로 조회합니다.
            ┗━ WithdrawConfirmedRepository      # Id 로 조회합니다.
            ┗━ WithdrawPendingRepository        # Id, Transaction Id 로 조회할 수 있습니다.

        ┗━ service                          # 비즈니스 로직 처리
            ┗━MonitoringService                 # 모니터링서비스 인터페이스
            ┗━MonitoringServiceImpl             # 모니터링 서비스 로직 구현체
            ┗━NotificationService               # 알림서비스 인터페이스
            ┗━NotificationServiceImpl           # 알림서비스 비즈니스 로직을 처리할 구현체
  ```
<br/>

## Guide-line

**Enclave API 컨테이너 생성** <br>
```
docker run -d -e NODE_ENV=test -p 3000:3000 haechi/sdk-enclave:stable npm start
```
<br>

**Rabbit MQ 컨테이너 생성**<br>
(username = username, password = password)
```
docker run -d --name rabbitmq -p 5672:5672 -p 8080:15672 --restart=unless-stopped -e RABBITMQ_DEFAULT_USER=username -e RABBITMQ_DEFAULT_PASS=password rabbitmq:management
```
<br>

**application.properties 설정**
```
# Rabbit MQ 설정
spring.rabbitmq.username=username
spring.rabbitmq.password=password

# API 서버 Http Header Key 설정
valueTransferEventsHost=http://localhost:3000/api/v2/eth/value-transfer-events
xHenesisSecret=
authorization=
```

## Problem



- @Scheduled 와 Recursive 로직이 구현된 Method 가 충돌을 일으켜 <br>
데이터를 Tracking 할 수 없는 상황 발생<br>

- JPA 에서 snake_case 는 인식하지 못함. <br>
그로 인해 JSON 의 Key 값이 camelCase 로 구성됨.<br>
ex) findByTx_id => 언더바(_)를 인식하지 못함.<br>
 
- URIComponents 에서 masterWalletId나 walletId 맵핑한 URI 로 <br>
조건을 만족하는 데이터를 가져오지 못함.<br>
String Type 의 Encoding 문제가 아닐까 의심중<br>

- Queue 의 Durable = True 했을 때 <br>
```PRECONDITION_FAILED - inequivalent arg 'durable' for queue```
에러 발생. <br> 
-> 이미 durable queue 로써 존재하다는 뜻.

- 1초 안에 Transaction 상태가 여러번 변할 경우,<br>
혹은 target API 서버(value-transfer-events) 가 다운 되었을 경우<br>
해당 트랜잭션의 변화를 저장할 수 없음.

## Solution
- Hystrix, 서킷 브레이커를 사용해 보는 건 어떨까?<br>
page 가 많을 경우 Recursive Depth 가 깊어짐에 따라 Time complexity 가 급격하게 증가. <br> 
pagination 을 Dynamic programming으로 해결해보려고 했으나 도저히 감이 안와서 실패<br>
ZIPKIN Request flow 나 성능 측정/분석에 대한 정보를 수집하는 분산 추적 기술 도입 및 <br>
장애 연쇄

- camelCase 문제 <br>
ORM 을 바꾸거나 @Query 어노테이션을 이용

- Queue Durable 문제 <br>
구체적으로 durable queue를 지정할 필요가 있다.<br>
Mass Transit 을 non-durable 로 다시 생성하도록 해야함.<br>
 
## Impression
 > 이제는 Java 와 Spring에 조금 익숙해 졌지만 적응하는 데 꽤 오래 걸린 것 같습니다.
 과제을 하면서 점점 공부할 심화 내용이 늘어가고 자료는 점점 줄었습니다.
 하지만 계속해서 새로운 것을 적용해보고, 혹은 기존의 것을 개선해 해결하는 과정이 너무 재밌었습니다. 
 에러가 나고 버그가 생길때마다 힘들었지만 그보다 더한 승부욕이 불타오르기도 했습니다.
 속성으로 공부했기 때문에 JVM Garbage Collection 이나 Thread, Exception, Annotation, TDD 등의 이점을 활용한 프로그래밍 하지 못한 아쉬움이 있습니다. 
 Query DSL 이나 반응형 프로그래밍을 적용을 하지 못한, Kakao Push 알림 구현 등 다양한 외부 API와의 연동을 구현하지 못한 아쉬움도 남았습니다.
 <br><br>
 또다시 배울 수 있어서 재밌었고 그로인해 더 배울게 생겨서 더 좋았습니다.
 정말 소중한 기회와 경험 감사드립니다.
 
<br><br>

## Timeline
- 10월 30일 (금)<br><br>
해치랩스 면접<br>
Spring AOP<br>
예외 처리 <br>
TDD 공부 <br>
DDD 정리하기<br>


- 10월 29일 (목)<br><br>
@Query 로 JSON snake_case 문제 해결
Rest template builder 에 timeout 추가 > 재전송 문제 임시 방편 해결 
retry 적용 필요


- 10월 28일 (수)<br><br>
**spring-cloud-sleuth**<br>
MSA 구조에서 클라이언트의 호출이 내부적으로 여러개의 서비스를 거쳐서 일어남<br>
그결과 전체 트랜잭션에 대한 Log tracing이 어려움<br>
Sleuth, Zipkin (분산 환경 모니터링) 공부 시작<br>


- 10월 25일 (일)<br><br>
**통신 메커니즘 변경(Rabbit MQ 사용)**<br>
Monitoring Service 에서 중복되지 않는 새로운 transaction 을 감지하면<br>
해당 Transaction 정보를 Notification controller의 각 Method로 전송합니다. <br>
정보를 받은 Controller 는 response 하는 notification service를 return합니다.<br><br> 
**Request 에 따른 Pagintaion 처리**<br>
size, updatedAtGte, page, walletId 등을 param으로 받게끔 Client 수정했습니다.<br>
URIComponents 를 사용해 해당 param을 담아서 restTemplate에 맵핑합니다.<br>
(한 페이지에 50개의 거래내역 조회, 10분전 까지 알림 조회)<br>

<br><br>

- 10월 24일 (토)<br><br>
**Pagination 관련 에러**<br>
updatedAtGte 을 설정하지 않고<br>
Recursive 방식로 조회 시 Stack overflow 발생 (당연히..ㅎㅎ)<br><br>
**@JsonProperty null 관련 에러**<br>
prevUrl.equal(null)<br>
prevUrl.isempty() 시 에러 발생 > 항상 false 이기 때문에..<br>
prevUrl == null 로 변경<br>

<br><br>

- 10월 23일 (금)<br><br>
**전체적인 구조개편**<br>
value-transfer-events 서버에서 Decimals 값 추가 확인<br>
DTO에 Decimals field 추가했습니다.<br><br>
상태, 타입 별로 다른 요청, 응답, 서비스 로직을 수행하므로
Repository, Service, Event 를 세부적으로 나눴습니다.<br><br>
모니터링 서비스와 알림응답 서비스를 나눴습니다.<br><br>
Reorganization 에 대해 공부하고 해당 로직을 구현했습니다.<br>
하지만 테스트에서 reorg 현상을 캐치하지는 못했습니다.<br><br>
구조개편과 메커니즘 변경으로 RabbitMQ가 필요없어졌습니다.<br>
하지만 다른 어플리케이션에 데이터를 보낼 때 요긴하게 쓰일 것 같습니다.<br>

<br><br>

- 10월 22일 (목)<br>
RabbitMQ 를 활용해 각 큐별로 해당 트랜잭션 정보 보내기 성공했습니다.<br>
하지만 중복, 또는 누락 트렌잭션을 다루는데 다른 방법이 필요해 보입니다.<br>

<br><br>

- 10월 21일 (수)<br>
Flux 예제를 구현해보았습니다.<br>
이론적으론 이해했으나 아직 이 과제에서 구현은 힘들어보입니다. 조금 더 테스트가 필요합니다.<br> 

<br><br>

- 10월 20일 (화)<br>
Reactive Java Spring<br><br>
Responsive<br>
요청이들어오면 in-time 내 응답을 줄 수 있어야함.
문제가 발생한 경우 문제 해결에 빠르게 대응해야함
시스템은 반드시 응답가능상태로 대기<br><br>
Elastic<br>
서버를 동적으로 늘리거나 줄이는 동작들에서도
어플리케이션은 새로 추가된 리소스 삭제된 리스소를 제거하더라도 응답할 수 있어야함<br><br>
Resilient<br>
시스템이 장애에 직면한 상태에서도 응답을 줄 수 있어야한다.
서킷브레이커 같은 것들도 이런 범주에 들어감.
서킷브레이커 : 서비스가 응답불가 상태에 빠진 걸 감지하고,
감지되었을 때 기존 시스템은 타임아웃에 걸리지 않고
내가 지정한 기본값을 리턴해라 하면서 중간에 어떤 놈이 동작하게 끔.
특정 컴퍼런트가 장애는 모든 시스템에장애를 미치기때문에..<br><br>
Message Driven<br>
looselycoupled<br>
이벤츄얼 컨시스턴스 특징을 가져야한다.<br><br>
어플이 어떤요청을 받고 DB가 동작을 할때까지 기다리는 게아닌
중간에 다른레이어가있고 거기서 OK 던지고주변에 다른 worker들이 동작해서 DB에 넣어줌
처리될 때까지 기다릴래가 아니고 너한테줬으니 니가 알아서 처리해, 난 다른 일 할래
    > 인프라 간 상호 운용성에 집중(서버, 디비 , 웹 프레임워크)
    > Non-blocking Back pressure 가 제공하는 비동기 Stream processing 표준
    > Back pressure 가 지원되는publisher + subscriber
     *Back pressure ? <br>
    구독자가 어떤 상태인지 상관없이 발행자는 계속 던질 수 있다.<br>
    ex) 수도관<br>
    배관의 직경이나 길이에 따른 적정압력이 있는데 초과용량이 흐르면 역류.<br><br>
    스트림 처리방식 Mono, Flux
    Mono 하나의 아이디로 repo 에 검색해서 return을  받는 형식
    Flux 여러개를 연속적으로 처리하는 것.

<br><br>

- 10월 19일 (월)<br>
Async, Non-blocking 서비스 도입의 필요<br>
섣부른 도입은 일을 그르칠 수 있기 때문에 단계별로 알아보기로 한다.<br>
(완성된 과제 제출 또한 중요하지만 더 엉망으로 만들 순 없으니까..ㅠㅠ)<br>
아래 순서대로 기술들의 개요와 원리를 찬찬히 살펴보고 예제를 clone 해 이리저리 뜯어보았다.<br>
    > @Scheduler<br>
     Observer Pattern <br>
     Websocket<br>
     AMQP, RabbitMQ<br>
     WebFlux<br>

    위 테크닉의 메커니즘을 완벽히 이해했다고 자신할 수 없지만 비동기, 논 블록킹 통신을 위해 <br>
    WebFlux 나 RxJava 을 사용한 반응형 프로그래밍이 필요하다.<br><br>
    그러기 위해 WebFlux 와 같은 Reactive Framework 와 <br>
    기존 도입을 계획했던 RabbitMQ 비교분석이 우선이다.

<br><br>

- 10월 18일 (일)<br>
풀타임 아르바이트로 인해 많이 작업을 하지 못했습니다.<br>
틈틈히 Web socket, Spring AMQP, Rabbit MQ 공부 시작.<br>
[현재 구조에 적용 실패 원인 분석 중....]<br>
notifications/ 을 Server 로 두고<br>
notifications/... 나머지를 Client 로 설정하자.<br><br>
서버가 업데이트 될때마다(1초 마다) 각 client 에 Topic 으로 보내면<br>
Controller의 모든 메서드가 반복실행 될 필요 없다.<br><br>
사용자가 알림내역을 조회하거나 알림 개수를 파악하고 싶을 떄 등등을 고려해<br>
클라이언트는 Notification Domain 에 값을 저장해야 한다.<br>
(하나로 묶을지, 클라이언트 별로 나눌지 고민해보자)<br>

<br><br>

- 10월 17일 (토)<br>
Scheduler 를 이용해 전체 거래 내역을 1초마다 Update 한다.<br>
DTO의 데이터 바로 조회하는 방식으로 변경 <br>
Stream 병렬처리 학습<br>

    - 모든 거래정보 (15개 최대) 를 조회합니다. <br>
    http://localhost:8080/notifications/
    ```
    [{"id":42841,"from":"0x95f124313e01ef40689d0fc0bf1b7bf11be8d7c2","to":"0x24acf1997a3cd0babeb10d7e6436f6c754fb7faa","amount":"0x38d7ea4c68000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x8","walletId":"dc9219e4066565a3f1311ea570c5a9a9","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"346f6920f8059fe5e7614f6c46bd5cbb","transactionId":"8d021b9b74284a2bb6cdcf7f752dbc77","coinSymbol":"ETH","blockHash":"0x997c9aa49246944c0d8c5a4800c06230903c048ff69146bba993eeb0e181f806","transferType":"DEPOSIT","transactionHash":"0x922655ccf822d34978f79024657812cac1ddc0d7f605a826e85f5867e39f8562","createdAt":"1603047971573","updatedAt":"1603047976528","walletName":"PrrrStar-user2","walletType":"USER_WALLET"},{"id":42840,"from":"0x95f124313e01ef40689d0fc0bf1b7bf11be8d7c2","to":"0x24acf1997a3cd0babeb10d7e6436f6c754fb7faa","amount":"0x38d7ea4c68000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x8","walletId":"413766cf22fd840d6d38e9621bc0749b","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"346f6920f8059fe5e7614f6c46bd5cbb","transactionId":"8d021b9b74284a2bb6cdcf7f752dbc77","coinSymbol":"ETH","blockHash":"0x997c9aa49246944c0d8c5a4800c06230903c048ff69146bba993eeb0e181f806","transferType":"WITHDRAWAL","transactionHash":"0x922655ccf822d34978f79024657812cac1ddc0d7f605a826e85f5867e39f8562","createdAt":"1603047926184","updatedAt":"1603047976528","walletName":"PrrrStar-user1","walletType":"USER_WALLET"},{"id":42839,"from":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","to":"0xe9acae32a3b8e428723c79f5862b48700635c5f0","amount":"0x2386f26fc10000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x1612","walletId":"1fc400d0c713d4bbf55133873995842f","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"9f34f6cdf71aa0436cf98389308b16b9","coinSymbol":"ETH","blockHash":"0x81124346ffbafe4d53f57de280dae3b00ac7755999e38afe366376a53a02df52","transferType":"DEPOSIT","transactionHash":"0x2f4a23df9167e1683d50338cb7c7e016ec2ae5db8f614b7c0a3b692441dd8c3b","createdAt":"1602973017761","updatedAt":"1602973019478","walletName":"PrrrStar2-user1","walletType":"USER_WALLET"},{"id":42838,"from":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","to":"0xe9acae32a3b8e428723c79f5862b48700635c5f0","amount":"0x2386f26fc10000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x1612","walletId":"f8c677322bd3fbb8bc1bbb20641cbc42","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"9f34f6cdf71aa0436cf98389308b16b9","coinSymbol":"ETH","blockHash":"0x81124346ffbafe4d53f57de280dae3b00ac7755999e38afe366376a53a02df52","transferType":"WITHDRAWAL","transactionHash":"0x2f4a23df9167e1683d50338cb7c7e016ec2ae5db8f614b7c0a3b692441dd8c3b","createdAt":"1602972973912","updatedAt":"1602973019478","walletName":"PrrrStar2","walletType":"MASTER_WALLET"},{"id":42837,"from":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","to":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","amount":"0x429d069189e0000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x161a","walletId":"65da4adb8c921685304b709db180cc62","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"bc8799ebbfd79ff358c631c94da96f63","coinSymbol":"ETH","blockHash":"0x089b2d58f0839180d1b8356aabd775c91b52f0eaefddc2c8333b016410228b4f","transferType":"DEPOSIT","transactionHash":"0x033c409aff238761171b60e5a118cce01eb5f88b853939866b2ff8cb1ad6c171","createdAt":"1602972913549","updatedAt":"1602972927227","walletName":"PrrrStar3","walletType":"MASTER_WALLET"},{"id":42836,"from":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","to":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","amount":"0x429d069189e0000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x161a","walletId":"f8c677322bd3fbb8bc1bbb20641cbc42","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"bc8799ebbfd79ff358c631c94da96f63","coinSymbol":"ETH","blockHash":"0x089b2d58f0839180d1b8356aabd775c91b52f0eaefddc2c8333b016410228b4f","transferType":"WITHDRAWAL","transactionHash":"0x033c409aff238761171b60e5a118cce01eb5f88b853939866b2ff8cb1ad6c171","createdAt":"1602972892165","updatedAt":"1602972927227","walletName":"PrrrStar2","walletType":"MASTER_WALLET"},{"id":42835,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0x4d41332e31a57d14899a3890b665710076a298d4","amount":"0x5af3107a4000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x1656","walletId":"346f6920f8059fe5e7614f6c46bd5cbb","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"346f6920f8059fe5e7614f6c46bd5cbb","transactionId":"07eb1db4a08cd95273b19e0e38d45e66","coinSymbol":"ETH","blockHash":"0x32a7159fb365972638ca7006943eb180a66fef2c87e1f1207fa942b5b2c3fc7b","transferType":"DEPOSIT","transactionHash":"0xd10f67266dda2e7e89119672c4321cf359b46dd8ccf8c80b3b8af55067f72ecb","createdAt":"1602972183657","updatedAt":"1602972188753","walletName":"PrrrStar","walletType":"MASTER_WALLET"},{"id":42834,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0x4d41332e31a57d14899a3890b665710076a298d4","amount":"0x5af3107a4000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x1656","walletId":"65da4adb8c921685304b709db180cc62","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"07eb1db4a08cd95273b19e0e38d45e66","coinSymbol":"ETH","blockHash":"0x32a7159fb365972638ca7006943eb180a66fef2c87e1f1207fa942b5b2c3fc7b","transferType":"WITHDRAWAL","transactionHash":"0xd10f67266dda2e7e89119672c4321cf359b46dd8ccf8c80b3b8af55067f72ecb","createdAt":"1602972149053","updatedAt":"1602972188753","walletName":"PrrrStar3","walletType":"MASTER_WALLET"},{"id":42833,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","amount":"0x71afd498d0000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x1662","walletId":"f8c677322bd3fbb8bc1bbb20641cbc42","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"3cac9db1ba164ae5b873c2ed8d9b64c4","coinSymbol":"ETH","blockHash":"0x6c8b2cbcc01bd033ef0291a8be4fb271b8536c36d56d0acffeb29a834f31d5f1","transferType":"DEPOSIT","transactionHash":"0xeaaf5bf7a2e2c4dfadffa761e56d2be1de2f1f1907dc351b885db10202d4d885","createdAt":"1602972057013","updatedAt":"1602972066444","walletName":"PrrrStar2","walletType":"MASTER_WALLET"},{"id":42832,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","amount":"0x71afd498d0000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x1662","walletId":"65da4adb8c921685304b709db180cc62","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"3cac9db1ba164ae5b873c2ed8d9b64c4","coinSymbol":"ETH","blockHash":"0x6c8b2cbcc01bd033ef0291a8be4fb271b8536c36d56d0acffeb29a834f31d5f1","transferType":"WITHDRAWAL","transactionHash":"0xeaaf5bf7a2e2c4dfadffa761e56d2be1de2f1f1907dc351b885db10202d4d885","createdAt":"1602972048286","updatedAt":"1602972066444","walletName":"PrrrStar3","walletType":"MASTER_WALLET"},{"id":42831,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","amount":"0x38d7ea4c68000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x16e8","walletId":"1537cb4c79ed13761ab58d22c37ca0a4","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"d7328f14f28b5d90b7332b96ae57e095","coinSymbol":"ETH","blockHash":"0x13f440bb81e9889cc748a6e9ec457c06391fff689dbb7d9bff661ac39deb8ae0","transferType":"DEPOSIT","transactionHash":"0x085dde0c99d18ec24e0edc0167c5689801b3a4e5d11fc700de019deb6e9fdc5e","createdAt":"1602970350554","updatedAt":"1602970354380","walletName":"PrrrStar2-user0","walletType":"USER_WALLET"},{"id":42830,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0xe5b758b9891d665e81c678c33eeec3ff33fa85e5","amount":"0x38d7ea4c68000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x16e8","walletId":"65da4adb8c921685304b709db180cc62","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"d7328f14f28b5d90b7332b96ae57e095","coinSymbol":"ETH","blockHash":"0x13f440bb81e9889cc748a6e9ec457c06391fff689dbb7d9bff661ac39deb8ae0","transferType":"WITHDRAWAL","transactionHash":"0x085dde0c99d18ec24e0edc0167c5689801b3a4e5d11fc700de019deb6e9fdc5e","createdAt":"1602970305713","updatedAt":"1602970354380","walletName":"PrrrStar3","walletType":"MASTER_WALLET"},{"id":42829,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","amount":"0x38d7ea4c68000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x16eb","walletId":"7b6bb1ca9f65fc263c0a640632ec62c9","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"4c6ec8f5aff8394a7884d687240631e3","coinSymbol":"ETH","blockHash":"0x62a082d6e011118e909470a7ed0b65ee13f868dcec5082c66f233d977cea283e","transferType":"DEPOSIT","transactionHash":"0x10a42012c4576f434b4fccc9dfa69c93b9746d61adabfad458c438fa4b60f50a","createdAt":"1602970292799","updatedAt":"1602970296558","walletName":"PrrrStar3-user0","walletType":"USER_WALLET"},{"id":42828,"from":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","to":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","amount":"0x38d7ea4c68000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x16eb","walletId":"65da4adb8c921685304b709db180cc62","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"4c6ec8f5aff8394a7884d687240631e3","coinSymbol":"ETH","blockHash":"0x62a082d6e011118e909470a7ed0b65ee13f868dcec5082c66f233d977cea283e","transferType":"WITHDRAWAL","transactionHash":"0x10a42012c4576f434b4fccc9dfa69c93b9746d61adabfad458c438fa4b60f50a","createdAt":"1602970269431","updatedAt":"1602970296558","walletName":"PrrrStar3","walletType":"MASTER_WALLET"},{"id":42827,"from":"0x8685a2ca366dc8e3f0acf9bf353b31307af9cedd","to":"0x3d1637c1799f0b2199c92da28576f1120bb4227a","amount":"0x2386f26fc10000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x16fa","walletId":"65da4adb8c921685304b709db180cc62","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"1562b8aa0417652d0eb4251713ea367e","coinSymbol":"ETH","blockHash":"0x16f636d7f2923fa662fb5fd156d716d33da0730ccf6af37b01ccb6e3a89ab289","transferType":"DEPOSIT","transactionHash":"0x965a218d4f106a3fc8054231841cabdc3f397f2fb8e854ddc2098faea842e054","createdAt":"1602970064600","updatedAt":"1602970109315","walletName":"PrrrStar3","walletType":"MASTER_WALLET"}]
    ```
    
    - 생성한 마스터 월렛의 상태가 입금 및 MINED 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/deposit_mined
        ```
        [
            {
                "id": 42819,
                "from": "0x4d41332e31a57d14899a3890b665710076a298d4",
                "to": "0xe5b758b9891d665e81c678c33eeec3ff33fa85e5",
                "amount": "0x2386f26fc10000",
                "blockchain": "ETHEREUM",
                "status": "MINED",
                "confirmation": "0x1",
                "walletId": "1537cb4c79ed13761ab58d22c37ca0a4",
                "orgId": "39cfcd04ab40f24308255eba661d45c7",
                "masterWalletId": "f8c677322bd3fbb8bc1bbb20641cbc42",
                "transactionId": "1d4c9086791379097034aa53a192712c",
                "coinSymbol": "ETH",
                "blockHash": "0x66c6802713879992b88a460b6e022ead38c75d6269047a4f5bf8f53becae8b5e",
                "transferType": "DEPOSIT",
                "transactionHash": "0x2842fe0b8521763fa617c006c789e321398b314ba49a1087b699e4a498e0297c",
                "createdAt": "1602888673541",
                "updatedAt": "1602888673541",
                "walletName": "PrrrStar2-user0",
                "walletType": "USER_WALLET"
            }
        ]
        ``` 
    - 생성한 마스터 월렛의 상태가 입금 및 REPLACED 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/deposit_reorged
        ```
        []
        ``` 
    - 생성한 마스터 월렛의 상태가 입금 및 CONFIRMED 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/deposit_confirm
        ```
        [{"id":42815,"from":"0x796591b6ab6430d63ac7da8e445887914546892c","to":"0x7b1d1dfc74cf3b22e18ebfd712833f25500d0e96","amount":"0x0","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0xad6","walletId":"9e0e22b036b5f3a4bed40992e33719db","orgId":"b8348150ef5c022235270f3944132c68","masterWalletId":"9e0e22b036b5f3a4bed40992e33719db","transactionId":"517de975125e85c39cc4a6e2443d92a0","coinSymbol":"ETH","blockHash":"0x1b12690d30a4aa92a0db3649c551b5b3ad48ebce62660bfcfa20784a5d5d02c6","transferType":"DEPOSIT","transactionHash":"0x34c022421193a11704343c1d3a82f06490b44eb2d9ea4862a0aa0296dce7b874","createdAt":"1602838739983","updatedAt":"1602838746826","walletName":"eth_fee","walletType":"MASTER_WALLET"},{"id":42813,"from":"0x423b6b1bf09dc550419fc5bcd84bf465e667fdf3","to":"0x8ee0a12ba1969874ed44a32be682522d1c19421e","amount":"0x2540be400","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0xb8e","walletId":"328f66d1237ac65305421c25b6417952","orgId":"b337949cfcbe11a361ca7c4bc7d1f40a","masterWalletId":"7fbc528b893c57f9a3c06979b6c9ff67","transactionId":"071c0febbbc447951207631963f2e759","coinSymbol":"ETH","blockHash":"0x8ef75a72c5ae4f996b3a0b896553db385febccbb16db6130787c9543e0e1778d","transferType":"DEPOSIT","transactionHash":"0x6908e7715573fd06b99f5f4e80d575b565811173f8ea922e8ea7ec5f88a93cc5","createdAt":"1602836739852","updatedAt":"1602836741142","walletName":"toby-test-user","walletType":"USER_WALLET"},{"id":42805,"from":"0x7196f2603e447b2b2a1410b32ee0e68b962f91c4","to":"0xf08f837e860a10d6cd0ddeadb686b71de3a01344","amount":"0x56f475cbdb76a8000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x32cd","walletId":"92aeaf973be7b761791afaf4d66144f3","orgId":"f31e19d258f6a2a1793ff3f6b2cead85","masterWalletId":"91553d485c546a1165b48ebf7317c700","transactionId":null,"coinSymbol":"TBET","blockHash":"0xbb58f1674dbb620d7f91ac372f8502d7c6c7f8cc9e375cda0693ae94e9af40f5","transferType":"DEPOSIT","transactionHash":"0xbd9859083a7a7f7e0e434e907a265852e28fea787a935ce3ffae5cf14b28f746","createdAt":"1602723298388","updatedAt":"1602723303753","walletName":"96","walletType":"USER_WALLET"},{"id":42798,"from":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0xad78ebc5ac6200000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b1c","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"237cee2a07e163f415c80166fe27fdbe","coinSymbol":"HCUT","blockHash":"0xfed54976b32118e7dd32bb9932c3c0507ed8e76517e47302b3556e3800df5dc1","transferType":"DEPOSIT","transactionHash":"0xfa595fd768b66e705c1d6044f705e399d74f9fa1ff7801577eae031353239eee","createdAt":"1602641992909","updatedAt":"1602641992909","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42794,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b2e","walletId":"0f2243c62df6fdfa319bfdabe102f3ea","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"1e39e606e6428822b81dd0eb2ed3c01b","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"DEPOSIT","transactionHash":"0x45e3919161a4e3df3864af94340f960be57294da324e4cfcc46369a504ed88bb","createdAt":"1602641746795","updatedAt":"1602641757041","walletName":"sergei.ten@gmail.com","walletType":"USER_WALLET"},{"id":42793,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b2e","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"e3396d2b73e7ec27f9a93eaf590ede63","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"DEPOSIT","transactionHash":"0x7bfb028117fcb2235a1d2abde7dbed2d2a994c7c7c8c8b3237e61c80db8173ef","createdAt":"1602641746793","updatedAt":"1602641756942","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42790,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b36","walletId":"0f2243c62df6fdfa319bfdabe102f3ea","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"bc932e68841bd1af5ccfc07ab4858c97","coinSymbol":"HCUT","blockHash":"0xc88fb9d0f0c819dd57752649b7e89df7df960eae8605f1872242565b89e7114f","transferType":"DEPOSIT","transactionHash":"0x6ca0a4e8de9faa34ce9a47d13ba030f0ef351b475ce822ddb347d04589757c48","createdAt":"1602641662492","updatedAt":"1602641662492","walletName":"sergei.ten@gmail.com","walletType":"USER_WALLET"},{"id":42782,"from":"0xe9acae32a3b8e428723c79f5862b48700635c5f0","to":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","amount":"0x6e2255f4098000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x5734","walletId":"7b6bb1ca9f65fc263c0a640632ec62c9","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"9357d17c66cc9d80ec9a4fc1a69f3a3d","coinSymbol":"ETH","blockHash":"0x667930144f284eed4422210d0f7481af86988d7e31422baae7651dacb2b7e6f0","transferType":"DEPOSIT","transactionHash":"0xd0b9b65e212f3cf0ba893d919cc2989d9195eb644943267d0e13441a0732c665","createdAt":"1602605417473","updatedAt":"1602605431343","walletName":"PrrrStar3-user0","walletType":"USER_WALLET"}]
        ``` 
    - 생성한 마스터 월렛의 상태가 출금 및 PENDING 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/withdraw_pending
        ```
        [
            {
                "id": 42820,
                "from": "0x4d41332e31a57d14899a3890b665710076a298d4",
                "to": "0x552fec7ab6e3336b976eeb408c3b2d15e96de06f",
                "amount": "0x6a94d74f430000",
                "blockchain": "ETHEREUM",
                "status": "PENDING",
                "confirmation": "0x0",
                "walletId": "346f6920f8059fe5e7614f6c46bd5cbb",
                "orgId": "39cfcd04ab40f24308255eba661d45c7",
                "masterWalletId": "346f6920f8059fe5e7614f6c46bd5cbb",
                "transactionId": "ec3154a3e2715118e85cdb2944df804d",
                "coinSymbol": "ETH",
                "blockHash": null,
                "transferType": "WITHDRAWAL",
                "transactionHash": "0x9f1f76aab7e634a813f60b9366851781ac56dbaf871225cf96f558e5239a256d",
                "createdAt": "1602888832514",
                "updatedAt": "1602888834579",
                "walletName": "PrrrStar",
                "walletType": "MASTER_WALLET"
            }
        ]
        ``` 
    - 생성한 마스터 월렛의 상태가 출금 및 CONFIRMED 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/withdraw_confirmed
        ```
        [{"id":42814,"from":"0x796591b6ab6430d63ac7da8e445887914546892c","to":"0x7b1d1dfc74cf3b22e18ebfd712833f25500d0e96","amount":"0x0","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0xaec","walletId":"03a9066732e924a3958d09da2b7395cb","orgId":"b8348150ef5c022235270f3944132c68","masterWalletId":"9e0e22b036b5f3a4bed40992e33719db","transactionId":"517de975125e85c39cc4a6e2443d92a0","coinSymbol":"ETH","blockHash":"0x1b12690d30a4aa92a0db3649c551b5b3ad48ebce62660bfcfa20784a5d5d02c6","transferType":"WITHDRAWAL","transactionHash":"0x34c022421193a11704343c1d3a82f06490b44eb2d9ea4862a0aa0296dce7b874","createdAt":"1602838739976","updatedAt":"1602838746826","walletName":"ETH_1594019721336","walletType":"USER_WALLET"},{"id":42812,"from":"0x423b6b1bf09dc550419fc5bcd84bf465e667fdf3","to":"0x8ee0a12ba1969874ed44a32be682522d1c19421e","amount":"0x2540be400","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0xba4","walletId":"7fbc528b893c57f9a3c06979b6c9ff67","orgId":"b337949cfcbe11a361ca7c4bc7d1f40a","masterWalletId":"7fbc528b893c57f9a3c06979b6c9ff67","transactionId":"071c0febbbc447951207631963f2e759","coinSymbol":"ETH","blockHash":"0x8ef75a72c5ae4f996b3a0b896553db385febccbb16db6130787c9543e0e1778d","transferType":"WITHDRAWAL","transactionHash":"0x6908e7715573fd06b99f5f4e80d575b565811173f8ea922e8ea7ec5f88a93cc5","createdAt":"1602836650060","updatedAt":"1602836741142","walletName":"toby-test","walletType":"MASTER_WALLET"},{"id":42797,"from":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0xad78ebc5ac6200000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b32","walletId":"0f2243c62df6fdfa319bfdabe102f3ea","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"237cee2a07e163f415c80166fe27fdbe","coinSymbol":"HCUT","blockHash":"0xfed54976b32118e7dd32bb9932c3c0507ed8e76517e47302b3556e3800df5dc1","transferType":"WITHDRAWAL","transactionHash":"0xfa595fd768b66e705c1d6044f705e399d74f9fa1ff7801577eae031353239eee","createdAt":"1602641992906","updatedAt":"1602641992906","walletName":"sergei.ten@gmail.com","walletType":"USER_WALLET"},{"id":42792,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b44","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"e3396d2b73e7ec27f9a93eaf590ede63","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"WITHDRAWAL","transactionHash":"0x7bfb028117fcb2235a1d2abde7dbed2d2a994c7c7c8c8b3237e61c80db8173ef","createdAt":"1602641713435","updatedAt":"1602641756942","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42791,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b44","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"1e39e606e6428822b81dd0eb2ed3c01b","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"WITHDRAWAL","transactionHash":"0x45e3919161a4e3df3864af94340f960be57294da324e4cfcc46369a504ed88bb","createdAt":"1602641705937","updatedAt":"1602641757041","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42789,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4b4c","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"bc932e68841bd1af5ccfc07ab4858c97","coinSymbol":"HCUT","blockHash":"0xc88fb9d0f0c819dd57752649b7e89df7df960eae8605f1872242565b89e7114f","transferType":"WITHDRAWAL","transactionHash":"0x6ca0a4e8de9faa34ce9a47d13ba030f0ef351b475ce822ddb347d04589757c48","createdAt":"1602641662488","updatedAt":"1602641662488","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42781,"from":"0xe9acae32a3b8e428723c79f5862b48700635c5f0","to":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","amount":"0x6e2255f4098000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x574a","walletId":"1fc400d0c713d4bbf55133873995842f","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"9357d17c66cc9d80ec9a4fc1a69f3a3d","coinSymbol":"ETH","blockHash":"0x667930144f284eed4422210d0f7481af86988d7e31422baae7651dacb2b7e6f0","transferType":"WITHDRAWAL","transactionHash":"0xd0b9b65e212f3cf0ba893d919cc2989d9195eb644943267d0e13441a0732c665","createdAt":"1602605409933","updatedAt":"1602605431344","walletName":"PrrrStar2-user1","walletType":"USER_WALLET"}]
        ``` 

- 10월 16일 (금)<br>
기본적인 문법 총정리 (타입, 어노테이션 등) <br>
Error Note, Debugging Note 작성 <br><br>
수많은 삽질 끝에 전체적인 구조 개편 성공했습니다!!! (Structure 참고)<br>
이제 서비스(비즈니스 로직)에 집중할 수 있습니다. 
    - 코인 입출금 정보 전체 조회 <br>
    http://localhost:8080/notifications/
        ```
        [{"id":42815,"from":"0x796591b6ab6430d63ac7da8e445887914546892c","to":"0x7b1d1dfc74cf3b22e18ebfd712833f25500d0e96","amount":"0x0","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x7de","walletId":"9e0e22b036b5f3a4bed40992e33719db","orgId":"b8348150ef5c022235270f3944132c68","masterWalletId":"9e0e22b036b5f3a4bed40992e33719db","transactionId":"517de975125e85c39cc4a6e2443d92a0","coinSymbol":"ETH","blockHash":"0x1b12690d30a4aa92a0db3649c551b5b3ad48ebce62660bfcfa20784a5d5d02c6","transferType":"DEPOSIT","transactionHash":"0x34c022421193a11704343c1d3a82f06490b44eb2d9ea4862a0aa0296dce7b874","createdAt":"1602838739983","updatedAt":"1602838746826","walletName":"eth_fee","walletType":"MASTER_WALLET"},{"id":42814,"from":"0x796591b6ab6430d63ac7da8e445887914546892c","to":"0x7b1d1dfc74cf3b22e18ebfd712833f25500d0e96","amount":"0x0","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x7de","walletId":"03a9066732e924a3958d09da2b7395cb","orgId":"b8348150ef5c022235270f3944132c68","masterWalletId":"9e0e22b036b5f3a4bed40992e33719db","transactionId":"517de975125e85c39cc4a6e2443d92a0","coinSymbol":"ETH","blockHash":"0x1b12690d30a4aa92a0db3649c551b5b3ad48ebce62660bfcfa20784a5d5d02c6","transferType":"WITHDRAWAL","transactionHash":"0x34c022421193a11704343c1d3a82f06490b44eb2d9ea4862a0aa0296dce7b874","createdAt":"1602838739976","updatedAt":"1602838746826","walletName":"ETH_1594019721336","walletType":"USER_WALLET"},{"id":42813,"from":"0x423b6b1bf09dc550419fc5bcd84bf465e667fdf3","to":"0x8ee0a12ba1969874ed44a32be682522d1c19421e","amount":"0x2540be400","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x896","walletId":"328f66d1237ac65305421c25b6417952","orgId":"b337949cfcbe11a361ca7c4bc7d1f40a","masterWalletId":"7fbc528b893c57f9a3c06979b6c9ff67","transactionId":"071c0febbbc447951207631963f2e759","coinSymbol":"ETH","blockHash":"0x8ef75a72c5ae4f996b3a0b896553db385febccbb16db6130787c9543e0e1778d","transferType":"DEPOSIT","transactionHash":"0x6908e7715573fd06b99f5f4e80d575b565811173f8ea922e8ea7ec5f88a93cc5","createdAt":"1602836739852","updatedAt":"1602836741142","walletName":"toby-test-user","walletType":"USER_WALLET"},{"id":42812,"from":"0x423b6b1bf09dc550419fc5bcd84bf465e667fdf3","to":"0x8ee0a12ba1969874ed44a32be682522d1c19421e","amount":"0x2540be400","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x896","walletId":"7fbc528b893c57f9a3c06979b6c9ff67","orgId":"b337949cfcbe11a361ca7c4bc7d1f40a","masterWalletId":"7fbc528b893c57f9a3c06979b6c9ff67","transactionId":"071c0febbbc447951207631963f2e759","coinSymbol":"ETH","blockHash":"0x8ef75a72c5ae4f996b3a0b896553db385febccbb16db6130787c9543e0e1778d","transferType":"WITHDRAWAL","transactionHash":"0x6908e7715573fd06b99f5f4e80d575b565811173f8ea922e8ea7ec5f88a93cc5","createdAt":"1602836650060","updatedAt":"1602836741142","walletName":"toby-test","walletType":"MASTER_WALLET"},{"id":42805,"from":"0x7196f2603e447b2b2a1410b32ee0e68b962f91c4","to":"0xf08f837e860a10d6cd0ddeadb686b71de3a01344","amount":"0x56f475cbdb76a8000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x2fd5","walletId":"92aeaf973be7b761791afaf4d66144f3","orgId":"f31e19d258f6a2a1793ff3f6b2cead85","masterWalletId":"91553d485c546a1165b48ebf7317c700","transactionId":null,"coinSymbol":"TBET","blockHash":"0xbb58f1674dbb620d7f91ac372f8502d7c6c7f8cc9e375cda0693ae94e9af40f5","transferType":"DEPOSIT","transactionHash":"0xbd9859083a7a7f7e0e434e907a265852e28fea787a935ce3ffae5cf14b28f746","createdAt":"1602723298388","updatedAt":"1602723303753","walletName":"96","walletType":"USER_WALLET"},{"id":42798,"from":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0xad78ebc5ac6200000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4824","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"237cee2a07e163f415c80166fe27fdbe","coinSymbol":"HCUT","blockHash":"0xfed54976b32118e7dd32bb9932c3c0507ed8e76517e47302b3556e3800df5dc1","transferType":"DEPOSIT","transactionHash":"0xfa595fd768b66e705c1d6044f705e399d74f9fa1ff7801577eae031353239eee","createdAt":"1602641992909","updatedAt":"1602641992909","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42797,"from":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0xad78ebc5ac6200000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4824","walletId":"0f2243c62df6fdfa319bfdabe102f3ea","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"237cee2a07e163f415c80166fe27fdbe","coinSymbol":"HCUT","blockHash":"0xfed54976b32118e7dd32bb9932c3c0507ed8e76517e47302b3556e3800df5dc1","transferType":"WITHDRAWAL","transactionHash":"0xfa595fd768b66e705c1d6044f705e399d74f9fa1ff7801577eae031353239eee","createdAt":"1602641992906","updatedAt":"1602641992906","walletName":"sergei.ten@gmail.com","walletType":"USER_WALLET"},{"id":42794,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4836","walletId":"0f2243c62df6fdfa319bfdabe102f3ea","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"1e39e606e6428822b81dd0eb2ed3c01b","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"DEPOSIT","transactionHash":"0x45e3919161a4e3df3864af94340f960be57294da324e4cfcc46369a504ed88bb","createdAt":"1602641746795","updatedAt":"1602641757041","walletName":"sergei.ten@gmail.com","walletType":"USER_WALLET"},{"id":42793,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4836","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"e3396d2b73e7ec27f9a93eaf590ede63","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"DEPOSIT","transactionHash":"0x7bfb028117fcb2235a1d2abde7dbed2d2a994c7c7c8c8b3237e61c80db8173ef","createdAt":"1602641746793","updatedAt":"1602641756942","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42792,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4836","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"e3396d2b73e7ec27f9a93eaf590ede63","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"WITHDRAWAL","transactionHash":"0x7bfb028117fcb2235a1d2abde7dbed2d2a994c7c7c8c8b3237e61c80db8173ef","createdAt":"1602641713435","updatedAt":"1602641756942","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42791,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x4836","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"1e39e606e6428822b81dd0eb2ed3c01b","coinSymbol":"HCUT","blockHash":"0xcdda563d23ce54d10a0780ed8649d20beb75090198070e9f898588e49b7a8fb9","transferType":"WITHDRAWAL","transactionHash":"0x45e3919161a4e3df3864af94340f960be57294da324e4cfcc46369a504ed88bb","createdAt":"1602641705937","updatedAt":"1602641757041","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42790,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x483e","walletId":"0f2243c62df6fdfa319bfdabe102f3ea","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"bc932e68841bd1af5ccfc07ab4858c97","coinSymbol":"HCUT","blockHash":"0xc88fb9d0f0c819dd57752649b7e89df7df960eae8605f1872242565b89e7114f","transferType":"DEPOSIT","transactionHash":"0x6ca0a4e8de9faa34ce9a47d13ba030f0ef351b475ce822ddb347d04589757c48","createdAt":"1602641662492","updatedAt":"1602641662492","walletName":"sergei.ten@gmail.com","walletType":"USER_WALLET"},{"id":42789,"from":"0x1fc26a0b5d48eb78a2006c1340e0fc7ef8b62071","to":"0x1e6afcc983ec14d37d387c8e8fb5c84846b308e9","amount":"0x56bc75e2d63100000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x483e","walletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","orgId":"d141d8228820976b6f034f8c4bd4db84","masterWalletId":"a7e382d18dd0ecb7ffd2e1eb89b525ba","transactionId":"bc932e68841bd1af5ccfc07ab4858c97","coinSymbol":"HCUT","blockHash":"0xc88fb9d0f0c819dd57752649b7e89df7df960eae8605f1872242565b89e7114f","transferType":"WITHDRAWAL","transactionHash":"0x6ca0a4e8de9faa34ce9a47d13ba030f0ef351b475ce822ddb347d04589757c48","createdAt":"1602641662488","updatedAt":"1602641662488","walletName":"2bko","walletType":"MASTER_WALLET"},{"id":42782,"from":"0xe9acae32a3b8e428723c79f5862b48700635c5f0","to":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","amount":"0x6e2255f4098000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x543c","walletId":"7b6bb1ca9f65fc263c0a640632ec62c9","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"65da4adb8c921685304b709db180cc62","transactionId":"9357d17c66cc9d80ec9a4fc1a69f3a3d","coinSymbol":"ETH","blockHash":"0x667930144f284eed4422210d0f7481af86988d7e31422baae7651dacb2b7e6f0","transferType":"DEPOSIT","transactionHash":"0xd0b9b65e212f3cf0ba893d919cc2989d9195eb644943267d0e13441a0732c665","createdAt":"1602605417473","updatedAt":"1602605431343","walletName":"PrrrStar3-user0","walletType":"USER_WALLET"},{"id":42781,"from":"0xe9acae32a3b8e428723c79f5862b48700635c5f0","to":"0x552fec7ab6e3336b976eeb408c3b2d15e96de06f","amount":"0x6e2255f4098000","blockchain":"ETHEREUM","status":"CONFIRMED","confirmation":"0x543c","walletId":"1fc400d0c713d4bbf55133873995842f","orgId":"39cfcd04ab40f24308255eba661d45c7","masterWalletId":"f8c677322bd3fbb8bc1bbb20641cbc42","transactionId":"9357d17c66cc9d80ec9a4fc1a69f3a3d","coinSymbol":"ETH","blockHash":"0x667930144f284eed4422210d0f7481af86988d7e31422baae7651dacb2b7e6f0","transferType":"WITHDRAWAL","transactionHash":"0xd0b9b65e212f3cf0ba893d919cc2989d9195eb644943267d0e13441a0732c665","createdAt":"1602605409933","updatedAt":"1602605431344","walletName":"PrrrStar2-user1","walletType":"USER_WALLET"}]
        ```

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
JAVA 기초문법 복습을 시작했습니다.<br>

    
<br><br>


## References
    각종 Offcial Documents
    https://shortstories.gitbooks.io/studybook/content/message_queue_c815_b9ac/rabbitmq-c0bd-c9c8.html
    https://www.javainuse.com/messaging/rabbitmq/
    https://docs.spring.io/spring-framework/docs/current/javadoc-api/
    https://spring.io/guides/gs/messaging-stomp-websocket/
    
    https://medium.com/blockvigil/how-we-deal-with-chain-reorganization-at-ethvigil-5a8c06859c7
    https://docs.blockcollider.org/docs/forks-and-reorgs
    
    (YouTube) Spring Boot Tutorials (Telusko)
    (YouTube) [토크ON세미나] 스프링 부트를 이용한 웹 서비스 개발 (SKplanet Tacademy)
    
    처음 배우는 스프링부트2 (김영재 저 | 한빛미디어)
    배워서 바로 쓰는 스프링 프레임워크 (애시시 사린, 제이 샤르마 저/오현석 역 | 한빛미디어)
    스프링 부트를 활용한 마이크로서비스 개발 (모이세스 메이세로 저/한동호 역 | 위키북스)
    
<br><br>