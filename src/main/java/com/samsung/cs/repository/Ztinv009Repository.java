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
import com.samsung.cs.vo.Ztinv009Vo;


@Repository
public interface Ztinv009Repository extends JpaRepository<Ztinv009, String>{

	@Query(value="SELECT "
			+ " max(z009.id) id ,z009.company, z009.asc_acctno, z009.asc_code, z009.parts_code, sum(z009.qty) qty, max(z009.erdat) erdat, movty,max(z009.service_type) service_type, coalesce( max(z001.stock_qty) ,0 ) stock_qty"
			+ " FROM ztinv009 z009"
			+" LEFT JOIN ztinv001 z001 "
			+ " on z001.company = z009.company"
			+ "	and z001.asc_acctno  = z009.asc_acctno"
			+ "	and z001.asc_code  = z009.asc_code"
			+ "	and z001.parts_code  = z009.parts_code"
			+ " WHERE z009.movty IN ('3','5','4','6') "
			+ "   AND z009.erdat BETWEEN :lvdat1 AND :lvdat2 "
			+ " GROUP BY z009.company,z009.asc_acctno,z009.asc_code,z009.parts_code,z009.movty"
			+ " ORDER BY z009.company,z009.asc_acctno,z009.asc_code,z009.parts_code,z009.movty"
			, nativeQuery = true
			)
	Page<Ztinv009Vo> getLtBuf(@Param("lvdat1") Timestamp lvdat1, @Param("lvdat2") Timestamp lvdat2,Pageable pageable); 
	
}
