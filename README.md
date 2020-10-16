# HaechiLabs-Assignment-demo

## Environment

> Language      : JAVA (JDK 14)<br/>
> Framework     : Spring Boot 2.3.4.RELEASED <br/>
> ORM           : JpaRepository, (QueryDSL 도입 준비중)<br/>
> IDE           : IntelliJ Ultimate<br/>
> OS            : Window 10<br/>

<br/>

## Structure
  ```
  src
  ┗━ main/java           
    ┗━ com.example.demo          
        ┗━ client                           # JSON 데이터 가공 (Jackson)
            ┗━ dto                                  
                ┗━ TransferEventResultDTO            # 입출금 데이터 DTO
            ┗━ TransferEventClient              # 입출금 데이터 가공 인터페이스
            ┗━ TransferEventClientImpl          # 입출금 데이터 가공 구현체

        ┗━ configuration                    # 각종 설정들
            ┗━ RestClientConfiguration           # RestTemplate Builder
            ┗━ QuerydslConfiguration (예정)       # Querydsl 설정파일

        ┗━ controller                       # service 호출 및 Endpoint 경로설정
            ┗━ NotificationController            # 알림과 관련된 서비스 호출 및 앤드포인트 경로 설정

        ┗━ domain                           # repository에 대한 추상화 지원
            ┗━ Transaction                       # 트랜잭션 Entity
            ┗━ TransferType                      # Enum 입출금 타입 모음
            ┗━ TransactionStatus                 # Enum 트랜잭션 상태 모음

        ┗━ repository                       # ORM 인터페이스
            ┗━ NotificationRepository           # JPA Repository 상속

        ┗━ service                          # 비즈니스 로직 처리
            ┗━NotificationService               # 알림서비스 인터페이스
            ┗━NotificationServiceImpl           # 알림서비스 비즈니스 로직을 처리할 구현체
  ```
<br/>

## Timeline
- 10월 19일 (월)<br>

- 10월 18일 (일)<br>

- 10월 17일 (토)<br>
DTO의 데이터 바로 조회하는 방식으로 변경 <br>
Stream 병렬처리 학습 및 적용 <br>
(DB에 저장 -> 데이터 조회 = 비효율) <br>
    - 생성한 마스터 월렛의 상태가 입금 및 MINED 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/deposit_mined
        ```
        []
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
    - 생성한 마스터 월렛의 상태가 입금 및 PENDING 일 떄, 해당 거래내역을 조회합니다. <br>
    http://localhost:8080/notifications/withdraw_pending
        ```
        []
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
JAVA 기초문법 복습을 시작했습니다.<br><br>

## Problem
짧은 기간 내 Spring Boot Framework 적응의 문제
- 섣부른 응용 문제<br>
    급하게 서적과 Youtube 를 통해 배운 Design Pattern 을 적용하려 했으나<br> 
    배움과 경험 부족으로 인해 실패했습니다.<br>
    
- Version에 따른 문제<br>
    중구난방한 정보수집으로 인해 Version 마다 다른 문법 혼동  
    Dependency 문제가 진행속도에 악영향<br><br>

## Solution
- 순차적인 학습 시작
    Youtube 무료강좌 활용 <br>

- 기본기 다지기
    문법위주의 공부 (Annotation 활용) <br>

- 학습범위 확장
    Spring Boot 에 국한되지 않고 Spring Framework 전반적인 <br>

<br><br>

## Impression
 새로운 지식을 얻고, 새로운 분야에 도전하는 것은 두렵기도 하지만, 막상 뛰어들면 또 즐겁고 승부욕이 생긴다는 점을 다시한번 느꼈습니다. 아직 스스로 공부해야할 것이 산더미지만 스프링 부트란 프레임 워크만의 매력을 알게되었고, Type Language에 대한 공부의 필요성 또한 느꼈습니다. <br>
 아직 적응이 필요한 문법과 구조지만 점차 눈에 익으니까 도전 정신이 더 불타올랐고, 실무에선 어떤 방식으로 설계하며 공부할까 라는 궁금증과 함께 해치랩스에서 조언과 토론을 통한 공부를 하며 꾸준한 트레이닝을 하면 정말 좋을 것 같다는 생각도 들었습니다. <br>

 기초부터 순차적이고 체계적인 학습을 시도했다면 더 좋은 결과를 냈을 것이라는 아쉬움도 있지만,<br>
 이번 기회에 어렴풋 알고 있었던 블록체인에 대해 공부해 볼 수 있었으며, 새로운 프레임워크에 대한 경험과 과거에 배웠던 JAVA 복습도 함께 할 수 있었던, 짧지만 매우 유익한 시간이었습니다. 감사합니다. <br>

<br>

## Sketch
```
Stream 병렬처리 공부해보자
Iterator 가 굉장히 불편함..
주로 사용해야할 부분
filter
collector

DTO Generator 사용 > 와 이거 엄청 편함!!
JSON 데이터 분석 후 DTO 자동생성 (@JsonProperties)
그리고 lombok 추가 > 끝 

근데 jackson 사용 이유??
대량데이터에서는 gson 보다 좋다고들었따.

NestedClass
클래스 안에 클래스 접근 (콤마로)


QueryDSL 을 써볼까..?
컴파일 시점 전에 에러를 발생 시킴
> IDE의 도움 받아 컴파일 하지 않고 오류 수정 가능
> 붸릐 긋

ex)
JPAQueryFactory query = new JPAQueryFactory(em);
QTransaction transaction = QTransaction.transaction;
query.selectFrom(transaction)
    .where(transaction.transferType.eq('DEPOSIT'))
        .and(transaction.status.eq("MINDED")))
    .orderBy(transaction.tx_id.desc())
    .fetch();

# 트랜잭션 도메인에서
# 입출금 타입이 DEPOIST 이고
# 트랜잭션상태가 MINDED 인 트랜잭션만 조회해서
# id 순으로 내림차순 정렬한다.
# 그리고 패치한다.

// 이 서비스를 확장한다면 아주아주 용이할 듯..?

JPA 장점
객체 지향적인 코드 
> 직관적이고 비즈니스 로직에 집중을 도와줌
> 전체 프로그램 구조를 일관성있게 유지 -> 코드의 가독성 증가
> SQL을 직접적으로 작성하지 않아 유지보수, 재사용성 편리

JPA 단점
복잡한 로직의 경우 JPA로 처리하기 어렵다. (통계 처리 등)
데이터 베이스 중심으로 되어 있는 환경에서는 실제 SQL로 튜닝해야하기 때문에 성능 저하
**컴파일 전까지 해당 로직 작동여부 확인 불가능!

이벤트 중심 아키텍쳐 설계 필요
거래 상태에 따라 발생시킬 이벤트, 비즈니스 로직이 다름.


기존 API 에 응답을 받아 새로운 REST API 를 구현해야하므로
마이크로서비스에서 주로 사용하는 구조를 선택
서비스 간 상호의존성을 최대한 낮추는 방안 모색
> 데이터 전달 객체(DTO) 기반 모델을 단순히 복사해서 공유하는 방식 채택
    장점 :  
    API 소비자가 많을 때 개발 시간 절약
    
    단점 :  
    API 버전 별로 DTO 를 관리해야함
    DTO 구조를 변경하면 JSON을 역직렬화 할 수 없음.
    > 원하는 필드만 역 직렬화 하는 방식으로 설계 필요 (일단 실패)

웹서비스가 노출할 자원 식별
식별한 자원에 대한 URI 지정
각 자원에 수행할 수 있는 연산 식별
식별한 연산에 대한 HTTP 메서드 맵핑
```

<br><br>

## References
    (YouTube) [토크ON세미나] JPA 프로그래밍 기본기 다지기 (김영한/우아한형제들 | SKplanet Tacademy)
    (YouTube) Spring Boot Tutorials (Telusko)
    
    처음 배우는 스프링부트2 (김영재 저 | 한빛미디어)
    배워서 바로 쓰는 스프링 프레임워크 (애시시 사린, 제이 샤르마 저/오현석 역 | 한빛미디어)
    스프링 부트를 활용한 마이크로서비스 개발 (모이세스 메이세로 저/한동호 역 | 위키북스)
    
<br><br>