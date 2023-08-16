package com.samsung.cs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.samsung.cs.model.entity.Ztinv009;


@Repository
public interface Ztinv009Repository extends JpaRepository<Ztinv009, String>{

	@Query(value="SELECT max(id) id ,company, asc_acctno, asc_code, parts_code, sum(qty) qty, max(erdat) erdat, movty,max(service_type) service_type FROM ztinv009 "
			+ " WHERE movty IN ('3','5','4','6') "
			+ "   AND erdat BETWEEN :lvdat1 AND :lvdat2 "
			+ " GROUP BY company,asc_acctno,asc_code,parts_code,movty"
			+ " ORDER BY company,asc_acctno,asc_code,parts_code,movty"
			, nativeQuery = true
			)
	Page<Ztinv009> getLtBuf(@Param("lvdat1") Timestamp lvdat1, @Param("lvdat2") Timestamp lvdat2,Pageable pageable); 
	
}
