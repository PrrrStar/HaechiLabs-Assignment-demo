package com.example.demo.repository;


import com.example.demo.domain.DepositMined;
import com.example.demo.domain.WithdrawPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawPendingRepository extends JpaRepository<WithdrawPending, Long> {

    /**
     * Transaction Id 로 출금 Pending 정보 가져오기
     * @param txId
     * @return
     */
    Optional<WithdrawPending> findByTxId(String txId);


    /**
     * walletId 로 입금 알람정보 가져오기
     * @param withdrawId
     * @return
     */
    Optional<WithdrawPending> findByWithdrawId(int withdrawId);


    List<WithdrawPending> findAllByWithdrawId(int withdrawId);
    List<WithdrawPending> findAllByWalletId(int walletId);

}