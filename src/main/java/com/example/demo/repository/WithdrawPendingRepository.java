package com.example.demo.repository;


import com.example.demo.domain.WithdrawPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawPendingRepository extends JpaRepository<WithdrawPending, Long> {

    /**
     * walletId 로 입금 알람정보 가져오기
     * @param walletId
     * @return
     */
    Optional<WithdrawPending> findByWalletId(String walletId);
    List<WithdrawPending> findAllByWalletId(String walletId);


}