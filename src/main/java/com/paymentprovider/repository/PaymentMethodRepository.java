package com.paymentprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentprovider.model.PaymentMethod;



@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

	PaymentMethod getPaymentId(Integer paymentMethodId);

	
	
}
