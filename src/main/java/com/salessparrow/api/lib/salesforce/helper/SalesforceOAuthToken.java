package com.salessparrow.api.lib.salesforce.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetRefreshedAccessToken;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

/**
 * SalesforceOAuthToken is a class for fetching and updating the Salesforce OAuth token.
 */
@Service
public class SalesforceOAuthToken {

  @Autowired
  private AwsKms awsKms;

  @Autowired
  private SalesforceOauthTokenRepository salesforceOauthTokenRepository;

  @Autowired
  private SalesforceGetRefreshedAccessToken salesforceGetRefreshedAccessToken;

  @Autowired
  private Util util;

  /**
   * Fetch the access token from the database and decrypt it.
   * 
   * @param sfOAuthToken
   * 
   * @return String
   */
  public String fetchAccessToken(SalesforceOauthToken sfOAuthToken) {
    String decryptedAccessToken = awsKms.decryptToken(sfOAuthToken.getAccessToken());
    return decryptedAccessToken;
  }

  public String updateAndGetRefreshedAccessToken(SalesforceOauthToken sfOAuthToken) {
    String encryptedRefreshToken = sfOAuthToken.getRefreshToken();
    String decryptedRefreshToken = awsKms.decryptToken(encryptedRefreshToken);

    HttpClient.HttpResponse response = salesforceGetRefreshedAccessToken.getRefreshedAccessToken(decryptedRefreshToken);
    String decryptedAccessToken = updateAccessTokenInDatabase(response.getResponseBody(), sfOAuthToken);

    return decryptedAccessToken;
  }

  /**
   * Update the access token in the database and return the decrypted access token.
   * 
   * @param responseBody
   * @param sfOAuthToken
   * 
   * @return String
   */
  private String updateAccessTokenInDatabase(String responseBody, SalesforceOauthToken sfOAuthToken) {
    JsonNode rootNode = util.getJsonNode(responseBody);
    String decryptedAccessToken = rootNode.get("access_token").asText();

    String encryptedAccessToken = awsKms.encryptToken(decryptedAccessToken);

    sfOAuthToken.setAccessToken(encryptedAccessToken);
    salesforceOauthTokenRepository.updateSalesforceOauthToken(sfOAuthToken);

    return decryptedAccessToken;
  }
}
