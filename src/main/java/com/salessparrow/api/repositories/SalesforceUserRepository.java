package com.salessparrow.api.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CacheConstants;

/**
 * Repository for SalesforceUser.
 */
@Repository
public class SalesforceUserRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public SalesforceUserRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	/**
	 * Insert a SalesforceUser to the salesforce_users table.
	 * @param salesforceUser
	 * @return SalesforceUser
	 */
	@CacheEvict(value = CacheConstants.SS_SALESFORCE_USER_CACHE, key = "#salesforceUser.externalUserId")
	public SalesforceUser createSalesforceUser(SalesforceUser salesforceUser) {
		// Create a row with status active and created at as current time
		salesforceUser.setStatus(SalesforceUser.Status.ACTIVE);
		salesforceUser.setCreatedAt(Util.getCurrentTimeInDateFormat());

		try {
			dynamoDBMapper.save(salesforceUser);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_csu_1", "something_went_wrong", e.getMessage()));
		}
		return salesforceUser;
	}

	/**
	 * Updates a SalesforceUser to the salesforce_users table.
	 * @param salesforceUser
	 * @return SalesforceUser
	 */
	@CacheEvict(value = CacheConstants.SS_SALESFORCE_USER_CACHE, key = "#salesforceUser.externalUserId")
	public SalesforceUser updateSalesforceUser(SalesforceUser salesforceUser) {
		try {
			dynamoDBMapper.save(salesforceUser);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_usu_1", "something_went_wrong", e.getMessage()));
		}
		return salesforceUser;
	}

	/**
	 * Retrieves a SalesforceUser from the salesforce_users table based on the provided
	 * id.
	 * @param id
	 * @return SalesforceUser
	 */
	@Cacheable(value = CacheConstants.SS_SALESFORCE_USER_CACHE, key = "#externalUserId")
	public SalesforceUser getSalesforceUserByExternalUserId(String externalUserId) {
		try {
			return dynamoDBMapper.load(SalesforceUser.class, externalUserId);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_gsubi_1", "something_went_wrong", e.getMessage()));
		}
	}

}
