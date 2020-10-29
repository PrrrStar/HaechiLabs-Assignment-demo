package com.example.demo.repository;


import com.example.demo.domain.DepositConfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositConfirmedRepository extends JpaRepository<DepositConfirmed, Long> {

    /**
     * depositId 로 입금 확인 정보 가져오기
     * @param deposit_id
     * @return
     */
    @Query("SELECT dc from DepositConfirmed dc where dc.deposit_id = :deposit_id")
    Optional<DepositConfirmed> getDepositConfirmedByDeposit_id(@Param("deposit_id") final int deposit_id);

}
