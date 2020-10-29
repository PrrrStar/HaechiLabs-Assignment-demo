package com.example.demo.repository;


import com.example.demo.domain.WithdrawPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WithdrawPendingRepository extends JpaRepository<WithdrawPending, Long> {

    /**
     * Transaction ID 로 출금 Pending 정보 가져오기
     * @param tx_id
     * @return
     */
    @Query("SELECT wp from WithdrawPending wp where wp.tx_id = :tx_id")
    Optional<WithdrawPending> getWithdrawPendingByTx_id(@Param("tx_id") final String tx_id);

    /**
     * withdraw_id 로 출금 Pending 정보 가져오기
     * @param withdraw_id
     * @return
     */
    @Query("SELECT wp from WithdrawPending wp where wp.withdraw_id = :withdraw_id")
    Optional<WithdrawPending> getWithdrawPendingByWithdraw_id(@Param("withdraw_id") final int withdraw_id);

}