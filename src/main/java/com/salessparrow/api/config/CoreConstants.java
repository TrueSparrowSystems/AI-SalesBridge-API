package com.salessparrow.api.config;

/**
 * Class to get the environment variables.
 */
public class CoreConstants {

  public static String cookieDomain(){
    return System.getenv("COOKIE_DOMAIN");
  }

  public static String encryptionKey() {
    return System.getenv("ENCRYPTION_KEY");
  }

  public static String apiCookieSecret() {
    return System.getenv("API_COOKIE_SECRET");
  }

  public static String environment() {
    return System.getenv("ENVIRONMENT");
  }

  public static Boolean isDevEnvironment() {
    return environment().equals("development");
  }

  public static Boolean isTestEnvironment() {
    return environment().equals("test");
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

  public static String kmsKeyId() {
    return System.getenv("KMS_KEY_ID");
  }

  public static String salesforceAuthUrl() {
    return System.getenv("SALESFORCE_AUTH_URL");
  }

  public static String salesforceClientId() {
    return System.getenv("SALESFORCE_CLIENT_ID");
  }

  public static String salesforceClientSecret() {
    return System.getenv("SALESFORCE_CLIENT_SECRET");
  }

  /**
   * This method returns the memcached address that is going to be used for locals
   *
   * @return String
   */
  public static String memcachedAddress() {
    return System.getenv("MEMCACHED_CACHE_HOST") + ":" + System.getenv("MEMCACHED_CACHE_PORT");
  }

  /**
   * This method returns the list of redirect URIs that are whitelisted in
   * Salesforce connected app for oAuth.
   *
   * @return String[]
   */
  public static String[] getWhitelistedRedirectUris() {
    String redirectUrisJson = System.getenv("SALESFORCE_WHITELISTED_REDIRECT_URIS");
    return redirectUrisJson.split(",");
  }

  /**
   * This method returns the email address that will be used to send error emails.
   * This email address or its domain must be verified in AWS SES.
   *
   * @return String
   */
  public static String errorEmailFrom() {
    return System.getenv("ERROR_MAIL_FROM");
  }

  /**
   * This method returns the email address that will receive the error emails.
   *
   * @return String
   */
  public static String errorEmailTo() {
    return System.getenv("ERROR_MAIL_TO");
  }

}
