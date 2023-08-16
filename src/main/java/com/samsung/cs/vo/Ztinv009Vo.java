package com.samsung.cs.vo;

import java.sql.Date;

public interface Ztinv009Vo {
	 //max(z009.id) id ,z009.company, z009.asc_acctno, z009.asc_code, z009.parts_code, sum(z009.qty) qty, max(z009.erdat) erdat, movty,max(z009.service_type) service_type, coalesce( max(z001.stock_qty) ,0 ) stock_qty
	 String getId();
     String getCompany();
     String getAsc_acctno();
     String getAsc_code();
     String getParts_code();
     int getQty(); 
     Date getErdat();
     int getMovty();
     String getService_type();
     double getStock_qty();
}
