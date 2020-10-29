package com.example.demo.repository;


import com.example.demo.domain.DepositReorged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositReorgedRepository extends JpaRepository<DepositReorged, Long> {

    /**
     * depositId 로 입금 Reorg 정보 가져오기
     * @param deposit_id
     * @return
     */
    @Query("SELECT dr from DepositReorged dr where dr.deposit_id = :deposit_id")
    Optional<DepositReorged> getDepositReorgedByDeposit_id(@Param("deposit_id") final int deposit_id);


}