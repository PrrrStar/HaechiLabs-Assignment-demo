package com.example.demo.repository;

import com.example.demo.domain.DepositMined;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositMinedRepository extends JpaRepository<DepositMined, Long> {

    /**
     * Transaction Hash 로 입금 Mined 정보 가져오기
     * @param tx_hash
     * @return
     */
    @Query("SELECT dm from DepositMined dm where dm.tx_hash = :tx_hash")
    Optional<DepositMined> getDepositMinedByTx_hash(@Param("tx_hash") final String tx_hash);

    /**
     * depositId 로 입금 Mined 정보 가져오기
     * @param deposit_id
     * @return
     */
    @Query("SELECT dm from DepositMined dm where dm.deposit_id = :deposit_id")
    Optional<DepositMined> getDepositMinedByDeposit_id(@Param("deposit_id") final int deposit_id);

}
