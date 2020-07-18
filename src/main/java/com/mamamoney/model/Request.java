package com.mamamoney.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

	@JsonProperty("sessionId")
	private String sessionId;
	
	@JsonProperty("msisdn")
	private String msisdn;
	
	@JsonProperty("userEntry")
	private String userEntry;
	
	@Builder
	private Request (String sessionId, String msisdn, String userEntry) {
		this.sessionId = sessionId;
		this.msisdn = msisdn;
		this.userEntry = userEntry;
	}
}
