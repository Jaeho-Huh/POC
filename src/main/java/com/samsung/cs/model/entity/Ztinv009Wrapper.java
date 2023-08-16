package com.samsung.cs.model.entity;

import java.sql.Date;

public interface Ztinv009Wrapper {
	//max(z009.id) id ,z009.company, z009.asc_acctno, z009.asc_code, z009.parts_code, sum(z009.qty) qty, max(z009.erdat) erdat, movty,max(z009.service_type) service_type, coalesce( max(z001.stock_qty) ,0 ) stock_qty
	long getId(); 
	String getCompany(); 
	String getAscAcctno(); 
	String getAscCode(); 
	String getPartsCode(); 
	int getQty(); 
	Date getErdat(); 
	String getServiceType();
	double getStockQty();
}
