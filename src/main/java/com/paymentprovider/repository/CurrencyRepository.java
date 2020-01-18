package com.paymentprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentprovider.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

	Currency getCurrencyId(Integer integer);

}
