package com.samsung.cs.batch;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.samsung.cs.batch.reader.Ztinv001Reader;
import com.samsung.cs.model.entity.Ztinv001;
import com.samsung.cs.model.entity.Ztinv009;
import com.samsung.cs.model.entity.Ztinv012Ver2;
import com.samsung.cs.model.entity.key.Ztinv001Id;
import com.samsung.cs.repository.Ztinv001Repository;
import com.samsung.cs.repository.Ztinv009Repository;
import com.samsung.cs.repository.Ztinv012Ver2Repository;
import com.samsung.cs.vo.Ztinv009Vo;

import org.springframework.batch.core.JobExecutionListener;
import jakarta.persistence.EntityManagerFactory;



@Slf4j
@Configuration
public class OFS_O_Batch {
	

	@Autowired
	private JdbcTemplate jdbctemplate; 
	
	@Autowired
	private Ztinv009Repository ztinv009Repository; 
	@Autowired
	private Ztinv012Ver2Repository ztinv012Ver2Repository; 
	@Value("#{T(java.time.LocalDate).now().minusMonths(1)}") // 1달전 데이터 기준으로 배치실행 -- 임시 
	private LocalDate currentDate;
	private double MonthlyWeight[] = {0.5, 0.3, 0.2}; 
	private Map<String,Ztinv012Ver2> Ztinv012Ver2Map = new HashMap<>(); 
	
	
	
	private int step=1;
	private int insertBatchSize = 1000; 
	
	

	@Bean
    public Job createJob(JobRepository jobRepository, PlatformTransactionManager transactionManager,JobListener jobListener) {
    	
		return  new JobBuilder("OFS-O-JOB",jobRepository)
                .incrementer(new RunIdIncrementer())
                //.listener(jobListener)
                //.start(createFlow(jobRepository,transactionManager))
                .start(getUsedQuantityStep(jobRepository,transactionManager))
                .next(getUsedQuantityStep(jobRepository,transactionManager))
                .next(getUsedQuantityStep(jobRepository,transactionManager))
                //.next(setStockQtyStep(jobRepository,transactionManager))
                .next(FinishedStep(jobRepository,transactionManager))
                .build();
    }
	
    @Bean
    public Step FinishedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                	
                	String insertQuery = "insert into salesforce.ztinv012_ver2__c"
                			+ " ( "
                			+ "	company__c , asc_acctno__c , asc_code__c ,prime_mat__c, mon01__c ,mon02__c ,mon03__c ,usage_avg__c , stock_qty__c , proposal_qty__c, msc_qty__c"
                			+ " ) "
                			+ "values ("
                			+ "?,?,?,?,?,?,?,?,?,?,?"
                			+ ")"; 
                	List<Ztinv012Ver2> ztinv012SaveList = new ArrayList<>();
                	for(String key : Ztinv012Ver2Map.keySet()) {
                    	Ztinv012Ver2 z012 = Ztinv012Ver2Map.get(key); 
                    	ztinv012SaveList.add(z012); 
                    }
                	if(ztinv012SaveList.size()>0) {
            	    	//ztinv012Ver2Repository.deleteAll(); // truncate
                		ztinv012Ver2Repository.truncateZtinv012Ver2c();
            	        //ztinv012Ver2Repository.saveAll(ztinv012SaveList);
                		
                		for(int i=0; i< ztinv012SaveList.size(); i+=insertBatchSize) {
                			 List<Ztinv012Ver2> batchList = ztinv012SaveList.subList(i, Math.min(i + insertBatchSize, ztinv012SaveList.size()));
                			 
	                		jdbctemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {
								@Override
								public void setValues(PreparedStatement ps, int j) throws SQLException {
									Ztinv012Ver2 z012 = batchList.get(j);
									ps.setString(1, z012.getCompany());
									ps.setString(2, z012.getAscAcctno());
									ps.setString(3, z012.getAscCode());
									ps.setString(4, z012.getPrimeMat());
									ps.setDouble(5, doubleNVL(z012.getMon01C(),0.0));
									ps.setDouble(6, doubleNVL(z012.getMon02C(),0.0));
									ps.setDouble(7, doubleNVL(z012.getMon03(),0.0));
									ps.setDouble(8, doubleNVL(z012.getUsageAvg(),0.0));
									ps.setDouble(9, doubleNVL(z012.getStockQtyC(),0.0));
									ps.setDouble(10, doubleNVL(z012.getProposalQtyC(),0.0));
									ps.setDouble(11, doubleNVL(z012.getMscQty(),0.0));
									
									
								}
								
								@Override
								public int getBatchSize() {
									return batchList.size();
								}
							});
                		}
                	}
                	
                    System.out.println(":::::::::::::::::::::::::::::: Batch Finished :::::::::::::::::::::::::::::::::::::::::::::");
                    step = 1; 
                    Ztinv012Ver2Map = new HashMap<>(); 
                    
                    return RepeatStatus.FINISHED;
                    
                }, transactionManager).build();
    }

    @Bean
    @JobScope
    public Step getUsedQuantityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    	
        return new StepBuilder("Step getUsedQuantityStep_"+step,jobRepository)
                .<Ztinv009Vo,Ztinv009Vo>chunk(5000,transactionManager)
                .reader(ztinv009Reader())
                .writer(getUsedQuantityStepWriter())
                .build();
    }
   


    
    @Bean
    @StepScope
    public RepositoryItemReader<Ztinv009Vo> ztinv009Reader() {
    	
    	
    	int period = 30; 
    	int period2 = 0; 
    	if(step==1) {
        	period2 = 0; 
    	}else if(step==2) {
        	period = 60; 
        	period2 = 31; 
    	}else {
        	period = 90; 
        	period2 = 61; 
    	}
    	
    	Timestamp lv_dat   = new Timestamp(currentDate.minusDays(period).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()); 
    	Timestamp lv_dat2 = new Timestamp(currentDate.minusDays(period2).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()); 
    	System.out.println("Step : " + step);
    	System.out.println("lv_dat : " + lv_dat);
    	System.out.println("lv_dat2 : " + lv_dat2);
    	step+=1; 
    	
        return new RepositoryItemReaderBuilder<Ztinv009Vo>()
                .repository(ztinv009Repository)
                .methodName("getLtBuf")  // 사용할 리포지토리 메서드명 지정
                .arguments(new Object[] {lv_dat,lv_dat2})
                .sorts(Collections.singletonMap("company", Sort.Direction.ASC))  // 정렬 조건 설정
                .pageSize(10000)  // 한 번에 읽어올 페이지 크기
                .name("ztinv009Reader")
                .build();
    }
    
    
    
    
    @Bean
    public ItemWriter<Ztinv009Vo> getUsedQuantityStepWriter() {
    	 return items -> {
        	Map<String,Ztinv009Vo> ltBuf1 = new HashMap<>(); 
        	Map<String,Ztinv009Vo> ltBuf2 = new HashMap<>();
            for (Ztinv009Vo item : items) {
            	if(item.getMovty() == 3 || item.getMovty() ==5) {
            		ltBuf1.put(item.getCompany()+"_"+item.getAsc_acctno()+"_"+item.getAsc_code()+"_"+item.getParts_code(), item);
            	}else {
            		ltBuf2.put(item.getCompany()+"_"+item.getAsc_acctno()+"_"+item.getAsc_code()+"_"+item.getParts_code(), item);
            	}
            }
            String lv_period = "MON01"; 
        	String lv_month  = "01.";
        	if(step==2) {
        		lv_period = "MON01"; 
            	lv_month  = "01."; 
        	}else if(step==3) {
        		lv_period = "MON02"; 
            	lv_month  = "02."; 
            	 
        	}else if(step==4){
        		lv_period = "MON03"; 
            	lv_month  = "03.";
        	}
            System.out.println("lv_period : " + lv_period);
        	System.out.println("lv_month : " + lv_month);
        	System.out.println( "writer Step : " + step );
            for(String strKey : ltBuf1.keySet()) {
            	double ltBuf1Qty = ltBuf1.get(strKey).getQty();
            	String company = ltBuf1.get(strKey).getCompany();
            	String prime_mat = ltBuf1.get(strKey).getParts_code();
            	String ascCode = ltBuf1.get(strKey).getAsc_code(); 
            	String ascAcctno = ltBuf1.get(strKey).getAsc_acctno();
            	Double stockQty = ltBuf1.get(strKey).getStock_qty(); 
            	
            	if(ltBuf2.containsKey(strKey)) {
            		ltBuf1Qty = ltBuf1Qty - ltBuf2.get(strKey).getQty();  
            	}
            	
					//////////////////////////////////////////////////////////////////////////////
					Ztinv012Ver2 ztin012Ver2 = new Ztinv012Ver2();
					ztin012Ver2.setAscAcctno(ascAcctno);
					ztin012Ver2.setCompany(company);
					ztin012Ver2.setPrimeMat(prime_mat);
					ztin012Ver2.setAscCode(ascCode);
					ztin012Ver2.setStockQtyC(stockQty);
					
					if(step==2)      ztin012Ver2.setMon01C(ltBuf1Qty);
					else if(step==3) ztin012Ver2.setMon02C(ltBuf1Qty);
					else if(step==4) {
					ztin012Ver2.setMon03(ltBuf1Qty);
					}
					
					
					
					if(Ztinv012Ver2Map.containsKey(company+"_"+ascAcctno+"_"+ascCode+"_"+prime_mat)) {
					
					Ztinv012Ver2 z012 = Ztinv012Ver2Map.get(company+"_"+ascAcctno+"_"+ascCode+"_"+prime_mat); 
					if(step==2) z012.setMon01C(ltBuf1Qty);
					else if(step==3) z012.setMon02C(ltBuf1Qty);
					else if(step==4) z012.setMon03(ltBuf1Qty);
					Ztinv012Ver2Map.put(company+"_"+ascAcctno+"_"+ascCode+"_"+prime_mat, z012); 
					
					}else Ztinv012Ver2Map.put(company+"_"+ascAcctno+"_"+ascCode+"_"+prime_mat, ztin012Ver2); 
            	
            	
            }
            
	            //3개월치 MON01,MON02,MON03 채운후 --> Monthly Usage 계산, ROP(Safty Qty) --> MSC_QTY에 저장 , MIL(Max Stock) 
	            for(String key : Ztinv012Ver2Map.keySet()) {
	            	Ztinv012Ver2 z012 = Ztinv012Ver2Map.get(key); 
	            	double m01 = doubleNVL(z012.getMon01C(),0.0);
	            	double m02 = doubleNVL(z012.getMon02C(),0.0);
	            	double m03 = doubleNVL(z012.getMon03(),0.0);
	            	double monthlyUsage = Double.valueOf(Math.round(MonthlyWeight[0]*m01 + MonthlyWeight[1]*m02 + MonthlyWeight[2]*m03));
	            	
	            	double max = Math.round(monthlyUsage * 14/ 30); //MIL  
	            	double min = max*0.5; //ROP
	            	double currentStock = z012.getStockQtyC(); 
	            	double proposalQty = 0; 
	            	
	            	// Minumum 주문 수량 
            		double MDQ = 2.0; 
            		
            		if(min<=currentStock) proposalQty = 0; 
            		else {
            			proposalQty = max - currentStock;
            			if(proposalQty < MDQ ) proposalQty = 0; 
            		}
            		z012.setProposalQtyC(proposalQty);
	            	
	            	z012.setUsageAvg(monthlyUsage);
	            	z012.setMscQty(min);
	            	
//	            	System.out.println(
//	            			"##### Z012Ver2 ######## "
//	            			+"companyCode : " + z012.getCompany() 
//	            			+" asc Code : " + z012.getAscAcctno()
//	            			+" parts Code : " + z012.getPrimeMat()
//	            			+" M01 : " + z012.getMon01C()
//	            			+" M02 : " + z012.getMon02C()
//	            			+" M03 : " + z012.getMon03()
//	            			+" Usage Avg : " + z012.getUsageAvg()
//	            			+ " get Safety Stock : " + z012.getMscQty()
//	            			);
	            }
            
            
        };
    }
    
    
    
    
    public static Double doubleNVL(Double value,Double defaultValue) {
    	
    	return value!=null ? value : defaultValue;
    }

}