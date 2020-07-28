package com.mamamoney.service;

import org.springframework.stereotype.Service;

import com.mamamoney.exception.MenuFlowException;
import com.mamamoney.model.Request;
import com.mamamoney.model.Response;
import com.mamamoney.model.TransactionInformation;
import com.mamamoney.model.USSDSessions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class USSDTransactionService {

	public Response process(Request request) {
		Response result = null;
		TransactionInformation transactionInfo = null;
		
		transactionInfo = initializeOrUpdateTransactionInfo(request);
		
		try {
			result = createResponse(request, transactionInfo);
		} catch (MenuFlowException e) {
			log.error("Something went wrong. Transaction could not be completed");
			
			result = restartTransaction(request);
		}
		
		removeSession(request, transactionInfo);
		
		return result; 
	}

	private void removeSession(Request request, TransactionInformation transactionInfo) {
		
		if (!transactionInfo.hasNextMenuScreen()) {
			USSDSessions.getAllTransactions().remove(request.getSessionId());
		}
	}

	private Response restartTransaction(Request request) {
		Response result;
		
		USSDSessions.getAllTransactions().remove(request.getSessionId());
		
		result = process(Request
				.builder()
					.sessionId(request.getSessionId())
					.msisdn(request.getMsisdn())
				.build());
		
		return result;
	}

	private Response createResponse(Request request, TransactionInformation transactionInfo) throws MenuFlowException {
		return Response
				.builder()
					.sessionId(request.getSessionId())
					.message(transactionInfo.computeMessage())
				.build();
	}

	private TransactionInformation initializeOrUpdateTransactionInfo(Request request) {
		TransactionInformation transactionInfo;
		
		if (USSDSessions.getActiveTransaction(request.getSessionId()) == null) {
			transactionInfo = initializeTransaction(request);
		} else {
			transactionInfo = updateTransaction(request);
		}
		
		return transactionInfo;
	}

	private TransactionInformation updateTransaction(Request request) {
		TransactionInformation transactionInfo;
		
		transactionInfo = USSDSessions.getActiveTransaction(request.getSessionId()); 
		transactionInfo.saveInput(request.getUserEntry());
		
		return transactionInfo;
	}

	private TransactionInformation initializeTransaction(Request request) {
		TransactionInformation transactionInfo = TransactionInformation.newInstance();
		
		USSDSessions.getAllTransactions().put(request.getSessionId(), transactionInfo);
		
		return transactionInfo;
	}
}
