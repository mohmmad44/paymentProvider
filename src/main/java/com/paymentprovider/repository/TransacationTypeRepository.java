package com.paymentprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentprovider.model.TransactionType;

@Repository
public interface TransacationTypeRepository extends JpaRepository<TransactionType, Integer> {

}
