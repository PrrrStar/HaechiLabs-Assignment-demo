package com.example.demo.repository;

import com.example.demo.domain.WithdrawConfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface WithdrawConfirmedRepository extends JpaRepository<WithdrawConfirmed, Long> {

    /**
     * withdraw_id 로 출금 확인 정보 가져오기
     * @param withdraw_id
     * @return
     */
    @Transactional
    @Query("SELECT wc from WithdrawConfirmed wc where wc.withdraw_id = :withdraw_id")
    Optional<WithdrawConfirmed> getWithdrawConfirmedByWithdraw_id(@Param("withdraw_id") final int withdraw_id);

}