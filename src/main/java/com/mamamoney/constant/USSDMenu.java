package com.mamamoney.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.mamamoney.exception.MenuFlowException;

public enum USSDMenu {
	INSTANCE {

		@Override
		public String getMessage(List<String> parameters) throws MenuFlowException {
			// No message available at initialization
			return null;
		}

		@Override
		public USSDMenu getNextMenu() {
			return WELCOME_SCREEN;
		}

		@Override
		public boolean hasNextMenuScreen() {
			return true;
		}
		
	},
	
	WELCOME_SCREEN {

		@Override
		public String getMessage(List<String> parameters) throws MenuFlowException {
			return "Welcome to Mama Money! Where would you like to send money today?" + System.lineSeparator() + "1) "
					+ Country.KENYA.getDisplayName() + System.lineSeparator() + "2) " + Country.MALAWI.getDisplayName();
		}

		@Override
		public USSDMenu getNextMenu() {
			return TRANSACTION_SCREEN;
		}

		@Override
		public boolean hasNextMenuScreen() {
			return true;
		}
		
	},
	
	TRANSACTION_SCREEN {
		@Override
		public String getMessage(List<String> parameters) throws MenuFlowException {
			if (parameters == null || parameters.isEmpty() || parameters.get(0).isBlank()
					|| !"12".contains(parameters.get(0))) {
				throw new MenuFlowException("Invalid input found");
			}
			
			String selectedCountry = parameters.get(0);
			Country country = null;
			
			switch(selectedCountry) {
			case "1":
				country = Country.KENYA;
				break;
			case "2":
				country = Country.MALAWI;
				break;
			}
			
			return "How much money (in Rands) would you like to send to " + country.getDisplayName() + "?";
		}

		@Override
		public USSDMenu getNextMenu() {
			return CONFIRMATION_SCREEN;
		}

		@Override
		public boolean hasNextMenuScreen() {
			return true;
		}
		
	},
	
	CONFIRMATION_SCREEN {

		@Override
		public String getMessage(List<String> parameters) throws MenuFlowException {
			if (parameters == null || parameters.size() < 2 || parameters.get(0).isBlank()
					|| !"12".contains(parameters.get(0)) || parameters.get(1).isBlank()
					|| !parameters.get(1).matches("\\d+")) {
				throw new MenuFlowException("Invalid input found");
			}
			
			String selectedCountry = parameters.get(0);
			String amount = parameters.get(1);
			BigDecimal transferAmount = null;
			Country country = null;
			
			switch(selectedCountry) {
			case "1":
				country = Country.KENYA;
				transferAmount = convertAmount(amount, Country.KENYA);
				break;
			case "2":
				country = Country.MALAWI;
				transferAmount = convertAmount(amount, Country.MALAWI);
				break;
			}
			
			return "Your person you are sending to will receive: " + transferAmount + " " + country.getCountryCode() + System.lineSeparator() + "1) OK";
		}

		@Override
		public USSDMenu getNextMenu() {
			return THANK_YOU_SCREEN;
		}

		@Override
		public boolean hasNextMenuScreen() {
			return true;
		}
		
		private BigDecimal convertAmount(String amount, Country country) {
			BigDecimal transferAmount = new BigDecimal(amount);
			
			transferAmount = transferAmount.multiply(country.getConversionRate());
			transferAmount = transferAmount.setScale(2, RoundingMode.CEILING);
			
			return transferAmount;
		}
		
	},
	
	THANK_YOU_SCREEN {

		@Override
		public String getMessage(List<String> parameters) throws MenuFlowException {
			return "Thank you for using Mama Money";
		}

		@Override
		public USSDMenu getNextMenu() {
			return this;
		}

		@Override
		public boolean hasNextMenuScreen() {
			return false;
		}
		
	};
	
	public abstract String getMessage(List<String> parameters) throws MenuFlowException;
	
	public abstract USSDMenu getNextMenu();
	
	public abstract boolean hasNextMenuScreen();
}
