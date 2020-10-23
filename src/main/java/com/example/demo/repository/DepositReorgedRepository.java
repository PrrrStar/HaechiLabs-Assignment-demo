package com.example.demo.repository;

import com.example.demo.domain.DepositConfirmed;
import com.example.demo.domain.DepositReorged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositReorgedRepository extends JpaRepository<DepositReorged, Long> {

    /**
     * depositId 로 입금 알람정보 가져오기
     * @param depositId
     * @return
     */
    Optional<DepositConfirmed> findByDepositId(int depositId);
    List<DepositConfirmed> findAllByDepositId(int depositId);

}