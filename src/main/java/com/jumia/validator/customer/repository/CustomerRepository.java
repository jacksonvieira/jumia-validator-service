package com.jumia.validator.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jumia.validator.customer.domain.Customer;

/**
 * @author jacksonvieira
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("from Customer c  where c.id = :id")
	Customer findOne(@Param("id") Long id);

	@Query("select c from Customer c where c.phone like %:phone%")
	Customer findByPhone(String phone);

	@Query("select c from Customer c where c.name like %:name%")
	Customer findByName(String name);

	@Query("select c from Customer c")
	Page<Customer> findAllPageable(Pageable pageable);
}
