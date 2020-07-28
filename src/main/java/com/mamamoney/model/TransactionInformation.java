package com.mamamoney.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.mamamoney.constant.USSDMenu;
import com.mamamoney.exception.MenuFlowException;

import lombok.Getter;

public class TransactionInformation {

	@Getter
	private LocalDateTime lastReponse;
	
	private USSDMenu activeMenu;
	
	private final Map<USSDMenu, String> parameters = new HashMap<>();

	public static TransactionInformation newInstance() {
		return new TransactionInformation();
	}
	
	private TransactionInformation() {
		this.activeMenu = USSDMenu.INSTANCE;
		
		lastReponse = LocalDateTime.now();
	}
	
	public String computeMessage() throws MenuFlowException {
		String result = null;
		
		try {
			result = activeMenu.getNextMenu().getMessage(parameters);
			
			activeMenu = activeMenu.getNextMenu();
		} catch (MenuFlowException e) {
			parameters.remove(activeMenu);
			
			result = activeMenu.getMessage(parameters);
		}
		
		lastReponse = LocalDateTime.now();
		
		return result;
	}
	
	public void saveInput(String value) {
		parameters.put(activeMenu, value);
	}
	
	public boolean hasNextMenuScreen() {
		return activeMenu.hasNextMenuScreen();
	}

}
