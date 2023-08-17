package com.samsung.cs.controller;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BatchController {
	
	@Autowired 
	JobLauncher jobLauncher;

	
	 @Value("#{T(java.time.LocalDate).now()}")
	 private LocalDate currentDate;
	
	@Autowired 
	@Qualifier("createJob") Job job;
	
	private boolean isJobRunning = false; 
	
    @GetMapping("/run-batch")
    @Async
    public String runBatch(Model model) {
    	
    	if(isJobRunning) {
    		model.addAttribute("message","Batch Job is already running");
    		return "run-batch";
    	}
    	
    	JobParametersBuilder builder = new JobParametersBuilder();
        java.util.Date currentDateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); // Convert LocalDate to java.util.Date
        
        builder.addLong("timestamp", System.currentTimeMillis()); 
        builder.addLong("step", 1L);  
        try {
        	
        	isJobRunning = true; 
            jobLauncher.run(job, builder.toJobParameters() );
            model.addAttribute("message", "Batch job executed successfully!");
            
            
        } catch (Exception e) {
        	
            e.printStackTrace();
            model.addAttribute("message", "Error occurred while running batch job.");
        }finally {
        	isJobRunning = false; 
        }
        return "run-batch";
    }
}