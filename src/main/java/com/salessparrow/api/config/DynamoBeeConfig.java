package com.salessparrow.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.dynamobee.Dynamobee;

@Configuration
public class DynamoBeeConfig {
  @Autowired
  private AmazonDynamoDB db;

  @Bean
  public Dynamobee dynamobee(){
  Dynamobee runner = new Dynamobee(db);
  
  runner.setChangeLogsScanPackage(
       "com.salessparrow.api.changelogs");
  
  return runner;
}
}
