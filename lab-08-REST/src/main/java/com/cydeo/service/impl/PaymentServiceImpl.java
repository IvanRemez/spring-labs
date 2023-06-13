package com.cydeo.service.impl;

import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public boolean existById(Long paymentId) {

        return paymentRepository.existsById(paymentId);
    }
}
