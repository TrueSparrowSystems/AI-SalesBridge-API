package com.salessparrow.api.changelogs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.github.dynamobee.changeset.ChangeLog;
import com.github.dynamobee.changeset.ChangeSet;
import com.salessparrow.api.lib.globalConstants.DynamoDbTableNameConstants;

@ChangeLog
public class DatabaseChangelog {
	Logger logger = LoggerFactory.getLogger(DatabaseChangelog.class);

	@ChangeSet(order = "001", id = "001", author = "testAuthor")
	public void createSalesforceOrganizationsTable(AmazonDynamoDB db) {
		String tableName = DynamoDbTableNameConstants.salesforceOrganizationsTableName();
		logger.info("Creating table: " + tableName);

		CreateTableRequest request = new CreateTableRequest()
				.withTableName(tableName)
				.withAttributeDefinitions(
						new AttributeDefinition("external_organization_id",
								ScalarAttributeType.S))
				.withKeySchema(
						new KeySchemaElement("external_organization_id", KeyType.HASH))
				.withBillingMode("PAY_PER_REQUEST");

		db.createTable(request);
		logger.info("Done creating table: " + tableName);
	}

	@ChangeSet(order = "002", id = "002", author = "testAuthor")
	public void createSalesforceOAuthTokensTable(AmazonDynamoDB db) {
		String tableName = DynamoDbTableNameConstants.salesforceOauthTokensTableName();
		logger.info("Creating table:" + tableName);

		CreateTableRequest request = new CreateTableRequest()
				.withTableName(tableName)
				.withAttributeDefinitions(
						new AttributeDefinition("external_user_id", ScalarAttributeType.S))
				.withKeySchema(
						new KeySchemaElement("external_user_id", KeyType.HASH))
				.withBillingMode("PAY_PER_REQUEST");

		db.createTable(request);

		logger.info("Done creating table: " + tableName);
	}

	@ChangeSet(order = "003", id = "003", author = "testAuthor")
	public void createSalesforceUsersTable(AmazonDynamoDB db) {
		String tableName = DynamoDbTableNameConstants.salesforceUsersTableName();
		logger.info("Creating table:" + tableName);

		CreateTableRequest request = new CreateTableRequest()
				.withTableName(tableName)
				.withAttributeDefinitions(
						new AttributeDefinition("external_user_id", ScalarAttributeType.S))
				.withKeySchema(
						new KeySchemaElement("external_user_id", KeyType.HASH))
				.withBillingMode("PAY_PER_REQUEST");

		db.createTable(request);

		logger.info("Done creating table: " + tableName);
	}
}