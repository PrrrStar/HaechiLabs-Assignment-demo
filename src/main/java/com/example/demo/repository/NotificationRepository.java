package com.example.demo.repository;

import com.example.demo.domain.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * depositId 로 입금 알람정보 가져오기
     * @param depositId
     * @return
     */
    List<Notification> findByDepositId(String depositId);

    /**
     * withdrawId 로 출금 알람정보 가져오기
     * @param withdrawId
     * @return
     */
    List<Notification> findByWithdrawId(String withdrawId);

}
