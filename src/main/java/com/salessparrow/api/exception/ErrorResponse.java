package com.salessparrow.api.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.lib.errorLib.ErrorConfig;
import com.salessparrow.api.lib.errorLib.ErrorResponseObject;
import com.salessparrow.api.lib.errorLib.ParamErrorConfig;

@Component
public class ErrorResponse {

  @Autowired
  private ResourceLoader resourceLoader;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ErrorResponse.class);

  /**
   * Get error response
   * 
   * @param apiIdentifier
   * @param internalErrorIdentifier
   * 
   * @return ErrorResponseObject
   */
  protected ErrorResponseObject getErrorResponse(String apiIdentifier, String internalErrorIdentifier, String message) {

    String errorConfigPath = "classpath:config/ApiErrorConfig.json";
    Resource resource = resourceLoader.getResource(errorConfigPath);
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, ErrorConfig> errorDataMap = new HashMap<>();
    try {
      errorDataMap = objectMapper.readValue(resource.getInputStream(),
          new TypeReference<HashMap<String, ErrorConfig>>() {
          });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error while reading error config file:" + e.getMessage());
    }

    ErrorConfig errorInfo = errorDataMap.get(apiIdentifier);

    if (errorInfo == null) {
      errorInfo = errorDataMap.get("something_went_wrong");
    }

    logError(message, Integer.parseInt(errorInfo.getHttp_code()), internalErrorIdentifier);

    ErrorResponseObject errorResponseObject = new ErrorResponseObject(
        Integer.parseInt(errorInfo.getHttp_code()),
        errorInfo.getMessage(),
        errorInfo.getCode(),
        internalErrorIdentifier,
        new ArrayList<ParamErrorConfig>());

    return errorResponseObject;
  }

  /**
   * Get error response
   * 
   * @param internalErrorIdentifier
   * @param message
   * @param paramErrorIdentifiers
   * 
   * @return ErrorResponseObject
   */
  protected ErrorResponseObject getParamErrorResponse(String internalErrorIdentifier, String message,
      List<String> paramErrorIdentifiers) {

    String paramsErrorPath = "classpath:config/ParamErrorConfig.json";
    Resource resource = resourceLoader.getResource(paramsErrorPath);
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, ParamErrorConfig> paramErrorDataMap = new HashMap<>();
    try {
      paramErrorDataMap = objectMapper.readValue(resource.getInputStream(),
          new TypeReference<HashMap<String, ParamErrorConfig>>() {
          });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error while reading param error config file:" + e.getMessage());
    }

    List<ParamErrorConfig> paramErrorConfigList = new ArrayList<ParamErrorConfig>();
   
    for (String paramErrorIdentifier : paramErrorIdentifiers) {
      ParamErrorConfig paramErrorConfig = null;

      Pattern pattern = Pattern.compile("^missing_(.*)$");
      Matcher matcher = pattern.matcher(paramErrorIdentifier);

      if (matcher.matches()) {
          String paramName = matcher.group(1);
          String messageString = paramName + " is required parameter. Please provide " + paramName + ".";

          paramErrorConfig = new ParamErrorConfig(paramName, messageString);
          paramErrorConfigList.add(paramErrorConfig);
      } 
      else {
          paramErrorConfig = paramErrorDataMap.get(paramErrorIdentifier);
          if (paramErrorConfig != null) {
            paramErrorConfigList.add(paramErrorConfig);
          }
      } 
    }

    logError(message, 400, internalErrorIdentifier);

    ErrorResponseObject errorResponseObject = new ErrorResponseObject(
        400,
        "At least one parameter is invalid or missing.",
        "INVALID_PARAMS",
        internalErrorIdentifier,
        paramErrorConfigList);

    return errorResponseObject;
  }

  /**
   * Log error
   * 
   * @param message
   * @param httpCode
   * @param internal_error_identifier
   * 
   * @return void
   */
  private void logError(String message, int httpCode, String internal_error_identifier) {
    logger.error("Error message: " + message);
    logger.error("Error code: " + httpCode);
    logger.error("Internal error identifier: " + internal_error_identifier);
  }

}
