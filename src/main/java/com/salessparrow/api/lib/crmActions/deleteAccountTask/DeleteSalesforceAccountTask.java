package com.salessparrow.api.lib.crmActions.deleteAccountTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

@Component
public class DeleteSalesforceAccountTask implements DeleteAccountTask{
    Logger logger = LoggerFactory.getLogger(DeleteSalesforceAccountTask.class);

    @Autowired
    private SalesforceConstants salesforceConstants;

    @Autowired
    private MakeCompositeRequest makeCompositeRequest;

    public void deleteAccountTask(User user, String accountId, String taskId) {
        logger.info("Delete Salesforce Account Task called");

        String salesforceUserId = user.getExternalUserId();

        String url = salesforceConstants.salesforceDeleteAccountTaskUrl(taskId);

        CompositeRequestDto compositeReq = new CompositeRequestDto("DELETE", url, "DeleteTask");

        List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
        compositeRequests.add(compositeReq);

        HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

        parseResponse(response.getResponseBody());
        
    }

    private void parseResponse(String responseBody) {
        logger.info("Parsing response body");
        Util util = new Util();
        JsonNode rootNode = util.getJsonNode(responseBody);

        JsonNode deleteNoteCompositeResponse = rootNode.get("compositeResponse").get(0);
        Integer deleteNoteStatusCode = deleteNoteCompositeResponse.get("httpStatusCode").asInt();
        
        if (deleteNoteStatusCode != 200 && deleteNoteStatusCode != 201 && deleteNoteStatusCode != 204) {
        String errorBody = deleteNoteCompositeResponse.get("body").asText();

        throw new CustomException(
            new ParamErrorObject(
                "l_ca_dan_dasn_pr_1", 
                errorBody, 
                Arrays.asList("invalid_task_id")
                )
            );
        }
    }
}
