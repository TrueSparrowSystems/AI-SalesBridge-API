package com.salessparrow.api.config;

import com.salessparrow.api.lib.globalConstants.SecretConstants;

/**
 * Class to get the environment variables.
 */
public class CoreConstants {
  /* Start: Env variables required before spring application context is initialized */

  public static String environment() {
    return System.getenv("ENVIRONMENT");
  }

  public static String awsAccessKeyId() {
    return System.getenv("AWS_IAM_ACCESS_KEY_ID");
  }

  public static String awsSecretAccessKey() {
    return System.getenv("AWS_IAM_SECRET_ACCESS_KEY");
  }

  public static String awsRegion() {
    return System.getenv("AWS_IAM_REGION");
  }

  public static Boolean isDevEnvironment() {
    return environment().equals("development");
  }

  public static Boolean isTestEnvironment() {
    return environment().equals("test");
  }

  /* End: Env variables required before spring application context is initialized */

  public static String encryptionKey() {
    return SecretConstants.encryptionKey();
  }

  public static String apiCookieSecret() {
    return SecretConstants.apiCookieSecret();
  }

  public static String kmsKeyId() {
    return SecretConstants.kmsKeyId();
  }

  public static String salesforceAuthUrl() {
    return SecretConstants.salesforceAuthUrl();
  }

  public static String salesforceClientId() {
    return SecretConstants.salesforceClientId();
  }

  public static String salesforceClientSecret() {
    return SecretConstants.salesforceClientSecret();
  }

  /**
   * This method returns the memcached address that is going to be used for locals
   *
   * @return String
   */
  public static String memcachedAddress() {
    return SecretConstants.memcachedHost() + ":" + SecretConstants.memcachedPort();
  }

  /**
   * This method returns the list of redirect URIs that are whitelisted in
   * Salesforce connected app for oAuth.
   *
   * @return String[]
   */
  public static String[] getWhitelistedRedirectUris() {
    String redirectUrisJson = SecretConstants.salesforceWhitelistedRedirectUris();
    return redirectUrisJson.split(",");
  }

  /**
   * This method returns the email address that will be used to send error emails.
   * This email address or its domain must be verified in AWS SES.
   *
   * @return String
   */
  public static String errorEmailFrom() {
    return SecretConstants.errorEmailFrom();
  }

  /**
   * This method returns the email address that will receive the error emails.
   *
   * @return String
   */
  public static String errorEmailTo() {
    return SecretConstants.errorEmailTo();
  }

}
