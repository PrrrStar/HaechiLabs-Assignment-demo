package com.example.demo.service;


import com.example.demo.domain.DepositMindedDomain;
import com.example.demo.repository.DepositMindedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositMindedService {

    private DepositMindedRepository depositMindedRepository;

    public List<DepositMindedDomain> retrieveDepositMinded(String transferType, String status) {
        return null;
    }

}
