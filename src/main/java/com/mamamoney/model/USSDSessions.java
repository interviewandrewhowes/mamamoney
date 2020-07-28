package com.mamamoney.model;

import java.util.HashMap;
import java.util.Map;

public class USSDSessions {

	private static Map<String, TransactionInformation> ussdSessions = new HashMap<>();
	
	public static TransactionInformation getActiveTransaction(String sessionId) {
		return ussdSessions.get(sessionId);
	}

	public static Map<String, TransactionInformation> getAllTransactions() {
		return ussdSessions;
	}
	
}
