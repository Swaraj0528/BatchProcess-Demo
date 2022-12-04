package com.batchUpdate.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.batchUpdate.Repo.BatchRepository;
import com.batchUpdate.model.Employee;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BatchService {
	
	@Autowired
	BatchRepository batchRepository;
	
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private int batchSize;
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() - 1);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public  void saveInBatch(List<Employee> entities) throws InterruptedException, ExecutionException {


		final AtomicInteger count = new AtomicInteger();
		CompletableFuture[] futures = entities.stream()
				.collect(Collectors.groupingBy(c -> count.getAndIncrement() / batchSize)).values().stream()
				.map(this::executeBatch).toArray(CompletableFuture[]::new);

		CompletableFuture<Void> run = CompletableFuture.allOf(futures);

		StopWatch timer = new StopWatch();
		timer.start();
		run.get();
		timer.stop();

		log.info("\nBatch time: " + timer.getTotalTimeMillis() + " ms (" + timer.getTotalTimeSeconds() + " s)");
	}

	public  CompletableFuture<Void> executeBatch(List<Employee> list) {

		return CompletableFuture.runAsync(() -> {
			
			batchRepository.saveAll(list);
			
		}, executor);
	}

}
