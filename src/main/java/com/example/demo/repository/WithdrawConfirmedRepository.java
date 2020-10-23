package com.example.demo.repository;


import com.example.demo.domain.WithdrawConfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawConfirmedRepository extends JpaRepository<WithdrawConfirmed, Long> {

    /**
     * walletId 로 입금 알람정보 가져오기
     * @param walletId
     * @return
     */
    Optional<WithdrawConfirmed> findByWalletId(String walletId);
    List<WithdrawConfirmed> findAllByWalletId(String walletId);


}