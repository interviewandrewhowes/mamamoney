package com.mamamoney.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mamamoney.model.USSDSessions;

@Service
public class SessionCleanupService {

	@Scheduled(fixedDelay = 1000)
	public void cleanupTimedoutSessions() {
		LocalDateTime now = LocalDateTime.now();
		
		USSDSessions.getAllTransactions().keySet()
				.removeAll(USSDSessions.getAllTransactions().entrySet().stream()
						.filter(value -> value.getValue().getLastReponse().plusSeconds(20).isBefore(now))
						.map(map -> map.getKey()).collect(Collectors.toList()));
	}
}
