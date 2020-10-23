package com.example.demo.repository;

import com.example.demo.domain.DepositMined;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositMinedRepository extends JpaRepository<DepositMined, Long> {

    /**
     * depositId 로 입금 알람정보 가져오기
     * @param depositId
     * @return
     */
    Optional<DepositMined> findByDepositId(int depositId);
    Optional<DepositMined> findByTxHash(String txHash);
    List<DepositMined> findAllByDepositId(int depositId);
    List<DepositMined> findAllByWalletId(int walletId);

}
