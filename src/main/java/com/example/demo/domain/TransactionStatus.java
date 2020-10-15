package com.example.demo.domain;

public enum TransactionStatus {
    PENDING_APPROVAL,   //승인대기
    REJECTED,           //거절됨
    REQUESTED,          //전송 중
    FAILED,             //전송 실패
    PENDING,            //채굴대기
    MINDED,             //채굴됨
    CONFIRMED,          //완료
    REPLACED,           //교체됨

}
