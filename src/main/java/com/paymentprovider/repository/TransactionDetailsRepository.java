package com.paymentprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentprovider.model.TransactionDetails;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {

	
	
	TransactionDetails findById(String clientId, String orderId);

	void saveTransactionStatus(String orderId);

}
