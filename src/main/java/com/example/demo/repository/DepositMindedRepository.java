package com.example.demo.repository;

import com.example.demo.domain.DepositMinded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositMindedRepository extends JpaRepository<DepositMinded, Long> {

//    @Query(value = "SELECT d FROM com.example.demo.domain.DepositMinded d WHERE d.transferType = :transferType GROUP BY d.transferType")
//    int getDepositMinded(@Param("transferType") final String transferType);
    //List<DepositMinded> findByTr
}
