package com.samsung.cs.repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsung.cs.model.entity.Ztinv001;
import com.samsung.cs.model.entity.key.Ztinv001Id;

@Repository
public interface Ztinv001Repository extends JpaRepository<Ztinv001, Ztinv001Id>{
	
	
	
	@Query(value="SELECT company, asc_acctno, asc_code, parts_code, stock_qty"
			+ " FROM ztinv001 z"
		    + " WHERE company IN :companys"
		    + " AND   asc_acctno IN :ascAcctnos"
		    + " AND   asc_code IN :ascCodes"
		    + " AND   parts_code IN :partsCodes"
			,nativeQuery = true)
	Page<Ztinv001> getAllByIds( @Param("companys")  List<String>  companys,
								@Param("ascCodes")   List<String> ascCodes,
								@Param("ascAcctnos")  List<String> ascAcctnos,
								@Param("partsCodes") List<String> partsCodes,
								Pageable pageable);
	
}
