package com.example.demo.repository;

import com.example.demo.domain.DepositMindedDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositMindedRepository extends JpaRepository<DepositMindedDomain, Long> {

//    @Query(value = "SELECT d FROM com.example.demo.domain.DepositMinded d WHERE d.transferType = :transferType GROUP BY d.transferType")
//    int getDepositMinded(@Param("transferType") final String transferType);
    //List<DepositMinded> findByTr
}
