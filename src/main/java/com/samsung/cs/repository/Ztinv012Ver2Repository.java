package com.samsung.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samsung.cs.model.entity.Ztinv012Ver2;

import jakarta.transaction.Transactional;

@Repository
public interface Ztinv012Ver2Repository extends JpaRepository<Ztinv012Ver2, String>{

	@Transactional
	@Modifying
	@Query(value="truncate salesforce.ztinv012_ver2__c", nativeQuery=true)
	void truncateZtinv012Ver2c(); 
}
