package com.mamamoney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mamamoney.model.Request;
import com.mamamoney.model.Response;
import com.mamamoney.service.USSDTransactionService;

@Controller
public class USSDTransactionController {
	
	@Autowired
	private USSDTransactionService ussdTransactionService;

	@PostMapping("/ussd")
	public ResponseEntity<Response> processRequest(@RequestBody Request request) {
		
		Response result = ussdTransactionService.process(request);
		
		return ResponseEntity.ok(result);
	}
}
