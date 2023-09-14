package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountEventDto;
import com.salessparrow.api.services.accountEvents.CreateAccountEventService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts/{account_id}/events")
@Validated
public class AccountEventController {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(AccountEventController.class);

	@Autowired
	private CreateAccountEventService createEventService;

	@PostMapping("")
	public ResponseEntity<CreateEventFormatterDto> createEvent(HttpServletRequest request,
			@PathVariable("account_id") String accountId, @Valid @RequestBody CreateAccountEventDto createEventDto) {
		logger.info("Create Event Request received");

		CreateEventFormatterDto createEventFormatterDto = createEventService.createEvent(request, accountId,
				createEventDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(createEventFormatterDto);
	}

}
