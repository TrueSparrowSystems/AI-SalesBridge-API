package com.salessparrow.api.lib.crmActions.getNotesList;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;

/**
 * GetNotesList is an interface for the GetNotesList action for the CRM.
 */
@Component
public interface GetNotesList {
    public GetNotesListFormatterDto getNotesList(SalesforceUser user, String accountId);
}

