package com.mamamoney.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mamamoney.constant.USSDMenu;
import com.mamamoney.exception.MenuFlowException;
import com.mamamoney.model.Request;
import com.mamamoney.model.Response;
import com.mamamoney.model.TransactionInformation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class USSDTransactionService {
	
	private static Map<String, TransactionInformation> userSession = new HashMap<>();

	public Response process(Request request) {
		Response result = null;
		TransactionInformation transactionInfo = null;
		
		if (userSession.get(request.getSessionId()) == null) {
			transactionInfo = initializeTransaction(request);
		} else {
			transactionInfo = updateTransaction(request);
		}
		
		try {
			result = Response
					.builder()
						.sessionId(request.getSessionId())
						.message(transactionInfo.computeMessage())
					.build();
		} catch (MenuFlowException e) {
			log.error("Something went wrong. Transaction could not be completed");
			userSession.remove(request.getSessionId());
			
			result = process(Request
					.builder()
						.sessionId(request.getSessionId())
						.msisdn(request.getMsisdn())
					.build());
		}
		
		if (!transactionInfo.hasNextMenuScreen()) {
			userSession.remove(request.getSessionId());
		}
		
		return result; 
	}
	
	@Scheduled(fixedDelay = 1000)
	public void cleanupTimedoutSessions() {
		LocalDateTime now = LocalDateTime.now();
		
		userSession.keySet()
				.removeAll(userSession.entrySet().stream()
						.filter(value -> value.getValue().getLastReponse().plusSeconds(20).isBefore(now))
						.map(map -> map.getKey()).collect(Collectors.toList()));
	}

	private TransactionInformation updateTransaction(Request request) {
		TransactionInformation transactionInfo;
		
		transactionInfo = userSession.get(request.getSessionId()); 
		transactionInfo.getParameters().add(request.getUserEntry());
		
		return transactionInfo;
	}

	private TransactionInformation initializeTransaction(Request request) {
		TransactionInformation transactionInfo;
		USSDMenu menu = USSDMenu.INSTANCE;
		
		transactionInfo = TransactionInformation
				.builder()
					.activeMenu(menu)
				.build();
		
		userSession.put(request.getSessionId(), transactionInfo);
		
		return transactionInfo;
	}
}
