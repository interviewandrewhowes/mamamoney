package com.mamamoney.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mamamoney.constant.USSDMenu;
import com.mamamoney.exception.MenuFlowException;

import lombok.Builder;
import lombok.Getter;

public class TransactionInformation {

	@Getter
	private LocalDateTime lastReponse;
	
	private USSDMenu activeMenu;
	
	@Getter
	private final List<String> parameters = new ArrayList<>();
	
	@Builder
	private TransactionInformation(USSDMenu activeMenu) {
		this.activeMenu = activeMenu;
		
		lastReponse = LocalDateTime.now();
	}
	
	public String computeMessage() throws MenuFlowException {
		String result = null;
		
		try {
			result = activeMenu.getNextMenu().getMessage(parameters);
			
			activeMenu = activeMenu.getNextMenu();
		} catch (MenuFlowException e) {
			parameters.remove(parameters.size() - 1);
			
			result = activeMenu.getMessage(parameters);
		}
		
		lastReponse = LocalDateTime.now();
		
		return result;
	}
	
	public boolean hasNextMenuScreen() {
		return activeMenu.hasNextMenuScreen();
	}
}
