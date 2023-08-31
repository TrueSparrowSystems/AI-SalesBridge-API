package com.salessparrow.api.helper;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.domain.SalesforceUser;

/**
 * LoadFixture is a helper class for the tests.
 */
public class LoadFixture {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	/**
	 * Load the fixture data.
	 * @param location
	 * @return FixtureData
	 * @throws IOException
	 */
	public void perform(FixtureData fixtureData) throws IOException {

		if (fixtureData.getSalesforce_users() != null) {
			for (FilePathData filePathData : fixtureData.getSalesforce_users()) {
				SalesforceUser salesforceUser = loadSalesForceUserFixture(filePathData.getFilepath());
				dynamoDBMapper.save(salesforceUser);
			}
		}

		if (fixtureData.getSalesforce_oauth_tokens() != null) {
			for (FilePathData filePathData : fixtureData.getSalesforce_oauth_tokens()) {
				SalesforceOauthToken salesforceOauth = loadSalesForceOAuthTokenFixture(filePathData.getFilepath());
				dynamoDBMapper.save(salesforceOauth);
			}
		}

		if (fixtureData.getSalesforce_organizations() != null) {
			for (FilePathData filePathData : fixtureData.getSalesforce_organizations()) {
				SalesforceOrganization salesforceOrganization = loadSalesForceOrganizationFixture(
						filePathData.getFilepath());
				dynamoDBMapper.save(salesforceOrganization);
			}
		}

	}

	/**
	 * Load the SalesforceUser fixture data from the given location.
	 * @param location
	 * @return FixtureData
	 * @throws IOException
	 */
	public SalesforceUser loadSalesForceUserFixture(String location) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		ObjectMapper objectMapper = new ObjectMapper();

		try (InputStream inputStream = resource.getInputStream()) {
			return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceUser>() {
			});
		}
	}

	/**
	 * Load the SalesforceOauthToken fixture data from the given location.
	 * @param location
	 * @return FixtureData
	 * @throws IOException
	 */
	public SalesforceOauthToken loadSalesForceOAuthTokenFixture(String location) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		ObjectMapper objectMapper = new ObjectMapper();

		try (InputStream inputStream = resource.getInputStream()) {
			return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceOauthToken>() {
			});
		}
	}

	/**
	 * Load the SalesforceOrganization fixture data from the given location.
	 * @param location
	 * @return FixtureData
	 * @throws IOException
	 */
	public SalesforceOrganization loadSalesForceOrganizationFixture(String location) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		ObjectMapper objectMapper = new ObjectMapper();

		try (InputStream inputStream = resource.getInputStream()) {
			return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceOrganization>() {
			});
		}
	}

}
