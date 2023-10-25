package com.salessparrow.api.lib.crmActions.createAccountTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountTaskDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCreateTaskDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceErrorObject;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceCompositeResponseHelper;

/**
 * CreateSalesforceTask class is responsible for creating a task in Salesforce
 */
@Component
public class CreateSalesforceAccountTask implements CreateAccountTaskInterface {

	Logger logger = LoggerFactory.getLogger(CreateSalesforceAccountTask.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private SalesforceCompositeResponseHelper salesforceCompositeResponseHelper;

	/**
	 * Create a task in Salesforce
	 * @param User User object
	 * @param accountId Salesforce account id
	 * @param task CreateTaskDto object
	 * @return CreateTaskFormatterDto object
	 */
	public CreateTaskFormatterDto createAccountTask(User User, String accountId, CreateAccountTaskDto task) {
		String salesforceUserId = User.getExternalUserId();

		logger.info("createAccountTask task description: {}", task.getDescription());

		String unEscapedTaskDescription = Util.unEscapeSpecialCharactersForPlainText(task.getDescription());
		String taskSubject = getTaskSubjectFromDescription(unEscapedTaskDescription);

		logger.info("performing create task in salesforce");

		Map<String, String> taskBody = new HashMap<String, String>();
		taskBody.put("Subject", taskSubject);
		taskBody.put("Description", unEscapedTaskDescription);
		taskBody.put("OwnerId", task.getCrmOrganizationUserId());
		taskBody.put("ActivityDate", task.getDueDate());
		taskBody.put("WhatId", accountId);

		CompositeRequestDto createTaskCompositeRequestDto = new CompositeRequestDto("POST",
				salesforceConstants.salesforceCreateTaskUrl(), "CreateTask", taskBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(createTaskCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce
	 * @param createTaskResponse String response from Salesforce
	 * @return CreateTaskFormatterDto object
	 */
	private CreateTaskFormatterDto parseResponse(String createTaskResponse) {
		Util util = new Util();
		logger.info("parsing response from salesforce");

		JsonNode rootNode = util.getJsonNode(createTaskResponse);

		SalesforceErrorObject errorObject = salesforceCompositeResponseHelper
			.getErrorObjectFromCompositeResponse(rootNode);

		if (!errorObject.isSuccess()) {

			if (errorObject.getErrorCode().equals("invalid_params")) {
				throw new CustomException(new ParamErrorObject("l_ca_cat_csat_pr_1", errorObject.getErrorCode(),
						Arrays.asList("invalid_account_id", "invalid_crm_organization_user_id")));
			}
			else {
				throw new CustomException(
						new ErrorObject("l_ca_cat_csat_pr_2", errorObject.getErrorCode(), errorObject.getMessage()));
			}
		}

		JsonNode createTaskNodeResponseBody = rootNode.get("compositeResponse").get(0).get("body");

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		SalesforceCreateTaskDto salesforceCreateTaskDto = mapper.convertValue(createTaskNodeResponseBody,
				SalesforceCreateTaskDto.class);

		CreateTaskFormatterDto createTaskFormatterDto = new CreateTaskFormatterDto();
		createTaskFormatterDto.setTaskId(salesforceCreateTaskDto.getId());

		return createTaskFormatterDto;
	}

	/**
	 * Get task subject from description
	 * @param task CreateTaskDto object
	 * @return String task subject
	 */
	private String getTaskSubjectFromDescription(String taskDescription) {
		logger.info("getting task subject from description");

		if (taskDescription.length() < 60) {
			return taskDescription;
		}

		return taskDescription.substring(0, 60);
	}

}
