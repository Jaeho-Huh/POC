package com.samsung.cs.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;
import lombok.Setter;

@Controller
public class BatchJobInfoController {

	@Autowired
    private JobExplorer jobExplorer;

    @GetMapping("/batch-executions")
    public String listBatchExecutions(Model model) {
    	List<JobInstance> jobInstances = jobExplorer.getJobInstances("OFS-O-JOB", 0, Integer.MAX_VALUE);
    	List<JobExecution> jobExecutions = null; 
    	List<Result> result = new ArrayList<>(); 
    	
        for (JobInstance jobInstance : jobInstances) {
        	
            jobExecutions = jobExplorer.getJobExecutions(jobInstance);
            result.add(new Result(jobInstance.getJobName(),jobExecutions.get(0).getStartTime(),jobExecutions.get(0).getEndTime(), jobExecutions.get(0).getStatus()) ); 
            // 각 JobInstance에 대한 실행 정보 처리
        }
        
        
        model.addAttribute("jobExecutions", result);
        return "batch-executions";
    }
    
    @Getter
    @Setter
    private class Result{
    	String name; 
    	LocalDateTime startTime;
    	LocalDateTime endTime; 
    	String status; 
    	public Result(String jobName, LocalDateTime startTime, LocalDateTime endTime, BatchStatus status) {
			this.name = jobName; 
			this.startTime = startTime;
			this.endTime = endTime; 
			this.status = status.toString(); 
					
		}
		
    }
}
