package com.paymentprovider.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paymentprovider.model.TransactionDetails;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {

	@Query("select a from TransactionDetails as a where a.clientId = ?1 and a.orderId = ?2")
	TransactionDetails findTransaction(String clientId, String orderId);

	@Modifying
	@Query("update TransactionDetails as a set a.status='AUTHORISED' where a.clientId = ?1 and a.orderId = ?2")
	int updateRegiStatus(String clientId, String orderId);
	
	@Modifying
	@Query("update TransactionDetails as a set a.status='CAPTURED' where a.clientId = ?1 and a.orderId = ?2")
	int updateAuthStatus(String clientId, String orderId);
	
	@Modifying
	@Query("update TransactionDetails as a set a.status='REVERSED' where a.clientId = ?1 and a.orderId = ?2")
	void reverseTransaction(String clientId, String orderId);

	@Query("select a from TransactionDetails as a where a.clientId = ?1 and (a.status='REGISTERED' or a.status='AUTHORISED')")
	List<TransactionDetails> findPendingTransations(String clientId);
	
	@Query("select sum((a.amount)*(case when a.currency='EUR' THEN  1 WHEN a.currency='USD' THEN 0.9 WHEN a.currency='GBP' THEN 1.2 END)) from TransactionDetails as a where a.date between ?2 and ?3 and (a.clientId=?1 and a.status ='CAPTURED')") 
	Double findTotalAmont(String clientId, LocalDate strDate, LocalDate endDate);
	
	@Modifying
	@Query("delete from TransactionDetails where clientId = ?1 and orderId = ?2")
	void deleteTransaction(String clientId, String orderId);

}
