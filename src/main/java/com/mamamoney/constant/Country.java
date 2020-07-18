package com.mamamoney.constant;

import java.math.BigDecimal;

public enum Country {
	KENYA("Kenya", "KES", new BigDecimal("6.10")), 
	MALAWI("Malawi", "MWK", new BigDecimal("42.50"));

	private String displayName;
	private String countryCode;
	private BigDecimal conversionRate;
	
	private Country(String displayName, String countryCode, BigDecimal conversionRate) {
		this.displayName = displayName;
		this.countryCode = countryCode;
		this.conversionRate = conversionRate;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public BigDecimal getConversionRate() {
		return conversionRate;
	}
}
