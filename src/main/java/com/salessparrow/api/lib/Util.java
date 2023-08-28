package com.salessparrow.api.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Class for utility functions.
 * 
 */
@Component
public class Util {

  /**
   * Get JsonNode from json string
   * 
   * @param jsonString
   * 
   * @return JsonNode
   */
  public JsonNode getJsonNode(String jsonString) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = null;
    try {
      jsonNode = mapper.readTree(jsonString);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "l_u_gjn_1",
              "something_went_wrong",
              e.getMessage()));
    }
    return jsonNode;
  }

/**
 * Retrieves a string representation of all request headers. For security, 
 * the value of the "authorization, cookie, password" header is obfuscated.
 * 
 * @param request - The HTTP request containing the headers to be logged.
 * @return String - A string representation of the headers in the format "{headerName:headerValue, ...}".
 */
  public static String generateHeaderLogString(HttpServletRequest request) {
    StringBuilder headerBuilder = new StringBuilder("{");
    request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
      // Add any other secret headers here that you don't want logged.
      if (headerName.equals("authorization") || 
        headerName.equals("cookie") || 
        headerName.equals("password")) {
        headerBuilder.append(headerName).append(":**********, ");
      } else {
        headerBuilder.append(headerName).append(":").append(request.getHeader(headerName)).append(", ");
      }
    });
    headerBuilder.append("}");

    return  headerBuilder.toString();
  }

  /**
   * Escape special characters in a string for use in a regular expression.
   * 
   * @param input
   * 
   * @return String
   */
  public static String escapeSpecialChars(String input) {
    String[] specialChars = {"\\", "%", "'", "\"", "_"};
    
    for (String specialChar : specialChars) {
        input = input.replace(specialChar, "\\" + specialChar);
    }

    return input;
  }

  public static String urlEncoder(String input){
    if(input == null || input == "") 
      return input;
    try {
      return URLEncoder.encode(input, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new CustomException(
        new ErrorObject(
          "u_u_ue_1",
          "something_went_wrong",
          e.getMessage()
        )
      );
    }
  }
  
  /** Get date format from datetime
   * 
   * @param date
   * 
   * @return String
   */
  public String getDateFormatFromDatetime(Date date) {
    if (date != null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      return dateFormat.format(date);
    }
    return null;
  }
}
