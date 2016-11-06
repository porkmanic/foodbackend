package com.intelli5.back.service;

import com.intelli5.back.domain.Payment;
import com.intelli5.back.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);
    
    @Inject
    private PaymentRepository paymentRepository;

    /**
     * Save a payment.
     *
     * @param payment the entity to save
     * @return the persisted entity
     */
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        Payment result = paymentRepository.save(payment);
        return result;
    }

    /**
     *  Get all the payments.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Payment> findAll() {
        log.debug("Request to get all Payments");
        List<Payment> result = paymentRepository.findAll();

        return result;
    }


    /**
     *  get all the payments where FoodOrder is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Payment> findAllWhereFoodOrderIsNull() {
        log.debug("Request to get all payments where FoodOrder is null");
        return StreamSupport
            .stream(paymentRepository.findAll().spliterator(), false)
            .filter(payment -> payment.getFoodOrder() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one payment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Payment findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        Payment payment = paymentRepository.findOne(id);
        return payment;
    }

    /**
     *  Delete the  payment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.delete(id);
    }
}
