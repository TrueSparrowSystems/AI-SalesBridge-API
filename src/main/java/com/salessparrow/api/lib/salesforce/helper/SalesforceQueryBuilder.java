package com.salessparrow.api.lib.salesforce.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.Util;

/**
 * SalesforceQueries is a class for building the Salesforce queries.
 */
@Component
public class SalesforceQueryBuilder {

	/**
	 * Get the list of accounts for a given searchTerm
	 * @param searchTerm
	 * @return String
	 */
	public String getAccountsQuery(String searchTerm) {
		searchTerm = Util.escapeSpecialChars(searchTerm);

		String query = "";
		if (searchTerm == "") {
			query = "SELECT Id, Name FROM Account ORDER BY LastModifiedDate DESC LIMIT 20";
		}
		else {
			query = "SELECT Id, Name FROM Account WHERE Name LIKE '%" + searchTerm
					+ "%' ORDER BY LastModifiedDate DESC LIMIT 20";
		}

		return Util.urlEncoder(query);
	}

	/**
	 * Get the accounts feed for a given limit and offset
	 * @param limit int
	 * @param offset int
	 * @return String
	 */
	public String getAccountFeedQuery(int limit, int offset) {

		return Util.urlEncoder(String.format(
				"SELECT Id, Name, Website, (SELECT Id, Name, Title, Email, Phone FROM Contacts) FROM Account ORDER BY LastModifiedDate ASC LIMIT %d OFFSET %d",
				limit, offset));
	}

	/**
	 * Get the contacts for a given account
	 * @param accountId
	 * @return String
	 */
	public String getAccountContactsQuery(String accountId) {
		accountId = Util.escapeSpecialChars(accountId);

		return Util.urlEncoder("SELECT Id, Name, Title, Email, Phone FROM Contact WHERE AccountId='" + accountId + "'");
	}

	/**
	 * Get the list of tasks for a given account
	 * @param accountId
	 * @return String
	 */
	public String getAccountTasksQuery(String accountId) {
		accountId = Util.escapeSpecialChars(accountId);

		return Util.urlEncoder(
				"SELECT Id, Description, ActivityDate, CreatedBy.Name, Owner.Name, Owner.Id, LastModifiedDate FROM Task WHERE WhatId='"
						+ accountId + "' ORDER BY LastModifiedDate DESC LIMIT 5");
	}

	/**
	 * Get the list of notes for a given account
	 * @param accountId
	 * @return String
	 */
	public String getContentDocumentIdUrl(String accountId) {
		accountId = Util.escapeSpecialChars(accountId);

		return Util
			.urlEncoder("SELECT ContentDocumentId FROM ContentDocumentLink WHERE LinkedEntityId = '" + accountId + "'");
	}

	/**
	 * Get the list of notes for a given account
	 * @param documentIds
	 * @return String
	 */
	public String getNoteListIdUrl(List<String> documentIds) {
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT Id, Title, TextPreview, CreatedBy.Name, LastModifiedDate FROM ContentNote WHERE Id IN (");

		for (int i = 0; i < documentIds.size(); i++) {
			if (i > 0) {
				queryBuilder.append(", ");
			}

			String documentId = Util.escapeSpecialChars(documentIds.get(i));
			queryBuilder.append("'").append(documentId).append("'");
		}
		queryBuilder.append(") ORDER BY LastModifiedDate DESC LIMIT 5");

		return Util.urlEncoder(queryBuilder.toString());
	}

	public String getNoteDetailsUrl(String noteId) {
		noteId = Util.escapeSpecialChars(noteId);

		return Util
			.urlEncoder("SELECT Id, Title, TextPreview, CreatedBy.Name, LastModifiedDate FROM ContentNote WHERE Id = '"
					+ noteId + "'");
	}

	public String getCrmOrganizationUsersQuery(String searchTerm) {
		searchTerm = Util.escapeSpecialChars(searchTerm);
		String query = "";

		if (searchTerm == "") {
			query = "SELECT Id, Name FROM User WHERE IsActive = true ORDER BY LastModifiedDate DESC LIMIT 20";
		}
		else {
			query = "SELECT Id, Name FROM User WHERE Name LIKE '%" + searchTerm
					+ "%' AND IsActive = true ORDER BY LastModifiedDate DESC LIMIT 20";
		}

		return Util.urlEncoder(query);
	}

	/**
	 * Get the list of events for a given account
	 * @param accountId
	 * @return String
	 */
	public String getAccountEventsQuery(String accountId) {
		accountId = Util.escapeSpecialChars(accountId);

		return Util.urlEncoder(
				"SELECT Id, Description, CreatedBy.Name, StartDateTime, EndDateTime, LastModifiedDate FROM Event WHERE WhatId='"
						+ accountId + "' ORDER BY LastModifiedDate DESC LIMIT 5");
	}

	public String getAccountEventDetailsUrl(String eventId) {
		eventId = Util.escapeSpecialChars(eventId);

		return Util.urlEncoder(
				"SELECT Id, Description, CreatedBy.Name, StartDateTime, EndDateTime, LastModifiedDate FROM Event WHERE Id = '"
						+ eventId + "'");
	}

	public String getAccountTaskDetailsUrl(String taskId) {
		taskId = Util.escapeSpecialChars(taskId);

		return Util.urlEncoder(
				"SELECT Id, Description, ActivityDate, CreatedBy.Name, Owner.Name, Owner.Id, LastModifiedDate FROM Task WHERE Id = '"
						+ taskId + "'");
	}

}
