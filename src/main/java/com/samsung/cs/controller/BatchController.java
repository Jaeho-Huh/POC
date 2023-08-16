package com.samsung.cs.controller;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
	
    @GetMapping("/run-batch")
    public String runBatch(Model model) {
    	JobParametersBuilder builder = new JobParametersBuilder();
        java.util.Date currentDateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); // Convert LocalDate to java.util.Date
        builder.addLong("timestamp", System.currentTimeMillis()); 
        builder.addLong("step", 1L); 
        //builder.addDate("currentDate", currentDateAsDate);
    	//builder.addDate("currentDate", Date.valueOf(currentDate)); 
        try {
        	
            jobLauncher.run(job, builder.toJobParameters() );
            model.addAttribute("message", "Batch job executed successfully!");
            
            
        } catch (Exception e) {
        	
            e.printStackTrace();
            model.addAttribute("message", "Error occurred while running batch job.");
        }
        return "run-batch";
    }
}