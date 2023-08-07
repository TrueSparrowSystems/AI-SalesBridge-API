package com.salessparrow.api.changelogs;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.github.dynamobee.changeset.ChangeLog;
import com.github.dynamobee.changeset.ChangeSet;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "001", author = "testAuthor")
    public void createSalesforceOrganizationsTable(AmazonDynamoDB db) {
        System.out.println("Creating table: salesforce_organizations");
        String tableName = "salesforce_organizations";

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S),
                        new AttributeDefinition("external_organization_id", ScalarAttributeType.S))
                .withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                .withGlobalSecondaryIndexes(
                        new GlobalSecondaryIndex()
                                .withIndexName("external_organization_id_index")
                                .withKeySchema(
                                        new KeySchemaElement("external_organization_id", KeyType.HASH))
                                .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)));

        db.createTable(request);
        System.out.println("Done creating table: salesforce_organizations");
    }

    @ChangeSet(order = "002", id = "002", author = "testAuthor")
    public void createSalesforceOAuthTokensTable(AmazonDynamoDB db) {
        System.out.println("Creating table: salesforce_oauth_tokens");

        CreateTableRequest request = new CreateTableRequest()
                .withTableName("salesforce_oauth_tokens")
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S),
                        new AttributeDefinition("salesforce_user_id", ScalarAttributeType.S))
                .withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                .withGlobalSecondaryIndexes(
                        new GlobalSecondaryIndex()
                                .withIndexName("salesforce_user_id_index")
                                .withKeySchema(
                                        new KeySchemaElement("salesforce_user_id", KeyType.HASH))
                                .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)));
        db.createTable(request);

        System.out.println("Done creating table: salesforce_oauth_tokens");
    }

    @ChangeSet(order = "003", id = "003", author = "testAuthor")
    public void createSalesforceUsersTable(AmazonDynamoDB db) {
        System.out.println("Creating table: salesforce_users");

        CreateTableRequest request = new CreateTableRequest()
                .withTableName("salesforce_users")
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S),
                        new AttributeDefinition("external_user_id", ScalarAttributeType.S))
                .withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                .withGlobalSecondaryIndexes(
                        new GlobalSecondaryIndex()
                                .withIndexName("external_user_id_index")
                                .withKeySchema(
                                        new KeySchemaElement("external_user_id", KeyType.HASH))
                                .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)));

        db.createTable(request);

        System.out.println("Done creating table: salesforce_users");
    }
}