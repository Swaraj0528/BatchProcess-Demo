package com.batchUpdate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batchUpdate.Repo.BatchRepository;
import com.batchUpdate.model.Employee;
import com.batchUpdate.service.BatchService;

@RestController
public class BatchContoller {
	
	@Autowired
	BatchService batchService;
	
	@Autowired
	BatchRepository batchRepository;
	
	
	@GetMapping("/Test")
	public void testBatch() throws InterruptedException, ExecutionException {
		List<Employee> list = new ArrayList<>();
		for(int i= 0;i<5000000;i++) {
			Employee e = new Employee();
			e.setId(i+1);
			e.setName("Demo_"+(i+1));
			e.setSeq(i+1);
			e.setEmail("DemoStudent+"+i+"gmail.com");
			//e.setMarks(i*(Math.pow(i, 2)));
			list.add(e);
		}
		batchService.saveInBatch(list);
	}
	
	@GetMapping("/Test1")
	public void testBatch1() throws InterruptedException, ExecutionException {
		List<Employee> findAll = batchRepository.findAll();
		List<Employee> findAll2 = findAll;
		findAll2.stream().forEach(x->{
			x.setName("Student_Name_"+x.getId());
		});
		batchService.saveInBatch(findAll2);
		
		
	}
	
	
	

}
