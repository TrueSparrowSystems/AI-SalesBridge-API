package com.salessparrow.api.lib.openAi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.OpenAiConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;

/**
 * OpenAiRequest is a class for making a request to the OpenAI API.
 **/
@Component
public class OpenAiRequest {
  @Autowired
  private OpenAiConstants openAiConstants;
  
  public HttpClient.HttpResponse makeRequest(Object payload) {
    String httpReqUrl = openAiConstants.chatCompletionUrl();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + CoreConstants.openAiApiKey());

    Integer timeoutMillis = openAiConstants.timeoutMillis();
    try {
      HttpClient.HttpResponse response = HttpClient.makePostRequest(
        httpReqUrl,
        headers,
        payload,
        timeoutMillis);

      return response;
    } catch (WebClientResponseException e) {
      if(e.getStatusCode().value() == 401) {
        throw new CustomException(
            new ErrorObject(
              "l_o_a_oar_mr_1",
              "something_went_wrong",
              "Invalid OpenAI API key"));
      } else if(e.getStatusCode().value() == 400) {
        throw new CustomException(
            new ErrorObject(
              "l_o_a_oar_mr_2",
              "something_went_wrong",
              "Invalid request payload"));
      }

      throw new CustomException(
            new ErrorObject(
              "l_o_a_oar_mr_3",
              "something_went_wrong",
              e.getMessage()));
    }
  }
}
