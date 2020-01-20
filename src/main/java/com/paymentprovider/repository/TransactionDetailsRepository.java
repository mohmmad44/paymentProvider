package com.paymentprovider.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paymentprovider.model.TransactionDetails;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {

	@Query("select a from TransactionDetails as a where a.clientId = ?1 and a.orderId = ?2")
	TransactionDetails findTransaction(String clientId, String orderId);

	@Query("update a SET a.status='AUTHORISED' from TransactionDetails as a where a.clientId = ?1 and a.orderId = ?2")
	void updateRegiStatus(String clientId, String orderId);

	@Query("update a SET a.status='CAPTURED' from TransactionDetails as a where a.clientId = ?1 and a.orderId = ?2")
	void updateAuthStatus(String clientId, String orderId);
	
	@Query("update a SET a.status='REVERSED' from TransactionDetails as a where a.clientId = ?1 and a.orderId = ?2")
	void reverseTransaction(String clientId, String orderId);

	@Query("select a from TransactionDetails as a where a.clientId = ?1 and (a.status='REGISTERED' or a.status='AUTHORISED'")
	TransactionDetails findPendingTransations(String clientId);
	
	
	Integer findTotalAmont(String clientId, Date begindate, Date enddate);

}
