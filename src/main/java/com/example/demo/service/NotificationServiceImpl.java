package com.example.demo.service;

import com.example.demo.domain.Transaction;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;

    NotificationServiceImpl(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Transaction> findByTransferType(String transferType) {
        return notificationRepository.findByTransferType(transferType);
    }
}
